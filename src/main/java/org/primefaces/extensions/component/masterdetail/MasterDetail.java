/*
* @author  Oleg Varaksin (ovaraksin@googlemail.com)
* $$Id$$
*/

package org.primefaces.extensions.component.masterdetail;

import org.primefaces.context.RequestContext;
import org.primefaces.util.Constants;

import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ListenerFor;
import javax.faces.event.PostRestoreStateEvent;
import java.util.ArrayList;
import java.util.List;

@ListenerFor(systemEventClass = PostRestoreStateEvent.class)
public class MasterDetail extends UIComponentBase
{
    public static final String COMPONENT_FAMILY = "com.innflow.ebtam.webapp.components";
    private static final String DEFAULT_RENDERER = "com.innflow.ebtam.webapp.components.MasterDetail";
    private static final String OPTIMIZED_PACKAGE = "com.innflow.ebtam.webapp.components.";

    private MasterDetailLevel detailLevelToProcess;
    private MasterDetailLevel detailLevelToGo;
    private int levelPositionToProcess;
    private int levelCount;

    /**
     * Properties that are tracked by state saving.
     */
    protected enum PropertyKeys
    {
        level, flow, showBreadcrumb;

        private String toString;

        PropertyKeys(final String toString) {
            this.toString = toString;
        }

        PropertyKeys() {
        }

        @Override
        public String toString() {
            return ((this.toString != null) ? this.toString : super.toString());
        }
    }

    public MasterDetail() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public int getLevel() {
        return (Integer) getStateHelper().eval(PropertyKeys.level, 1);
    }

    public void setLevel(final int level) {
        setAttribute(PropertyKeys.level, level);
    }

    public Object getFlow() {
        return getStateHelper().eval(PropertyKeys.flow, null);
    }

    public void setFlow(final Object flow) {
        setAttribute(PropertyKeys.flow, flow);
    }

    public boolean isShowBreadcrumb() {
        return (Boolean) getStateHelper().eval(PropertyKeys.showBreadcrumb, true);
    }

    public void setShowBreadcrumb(final boolean showBreadcrumb) {
        setAttribute(PropertyKeys.showBreadcrumb, showBreadcrumb);
    }

    public void setAttribute(final PropertyKeys property, final Object value) {
        getStateHelper().put(property, value);

        List<String> setAttributes = (List<String>) this.getAttributes().get("javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            final String cname = this.getClass().getName();
            if (cname != null && cname.startsWith(OPTIMIZED_PACKAGE)) {
                setAttributes = new ArrayList<String>(6);
                this.getAttributes().put("javax.faces.component.UIComponentBase.attributesThatAreSet", setAttributes);
            }
        }

        if (setAttributes != null && value == null) {
            final String attributeName = property.toString();
            final ValueExpression ve = getValueExpression(attributeName);
            if (ve == null) {
                setAttributes.remove(attributeName);
            } else if (!setAttributes.contains(attributeName)) {
                setAttributes.add(attributeName);
            }
        }
    }

    public void processEvent(ComponentSystemEvent event) throws AbortProcessingException {
        super.processEvent(event);

        FacesContext fc = FacesContext.getCurrentInstance();
        if (!fc.getPartialViewContext().isAjaxRequest() || !isSelectDetailRequest(fc)) {
            return;
        }

        String clienId = this.getClientId(fc);
        PartialViewContext pvc = fc.getPartialViewContext();

        // process and update the MasterDetail component automatically
        pvc.getExecuteIds().add(clienId);
        // PF 2.2.1
        RequestContext.getCurrentInstance().addPartialUpdateTarget(clienId);
        // PF 3.0
        //pvc.getRenderIds().add(clienId);
    }

    public void processDecodes(final FacesContext fc) {
        if (isSelectDetailRequest(fc)) {
            MasterDetailLevel levelToProcess = getDetailLevelToProcess(fc);

            if (isSkipProcessing(fc)) {
                fc.renderResponse();
            } else {
                levelToProcess.processDecodes(fc);
            }
        } else {
            super.processDecodes(fc);
        }
    }

    public void processValidators(final FacesContext fc) {
        if (isSelectDetailRequest(fc)) {
            getDetailLevelToProcess(fc).processValidators(fc);
        } else {
            super.processValidators(fc);
        }
    }

    public void processUpdates(final FacesContext fc) {
        if (isSelectDetailRequest(fc)) {
            ValueExpression levelVE = this.getValueExpression(PropertyKeys.level.toString());
            if (levelVE != null) {
                levelVE.setValue(fc.getELContext(), this.getLevel());
                getStateHelper().remove(PropertyKeys.level);
            }

            // get AjaxSource caused this ajax request
            String source = fc.getExternalContext().getRequestParameterMap().get(Constants.PARTIAL_SOURCE_PARAM);
            MasterDetailLevel mdl = getDetailLevelToProcess(fc);
            
            // get resolved context value
            Object contextValue = mdl.getAttributes().get("contextValue_" + source);
            if (contextValue != null) {
                
            }
             

            ValueExpression flowVE = this.getValueExpression(PropertyKeys.flow.toString());
            if (flowVE != null) {
                flowVE.setValue(fc.getELContext(), this.getLevel());
                getStateHelper().remove(PropertyKeys.flow);
            }

            getDetailLevelToProcess(fc).processUpdates(fc);
        } else {
            super.processUpdates(fc);
        }
    }

    public MasterDetailLevel getDetailLevelToProcess(final FacesContext fc) {
        if (detailLevelToProcess != null) {
            return detailLevelToProcess;
        }

        int currentLevel = Integer.valueOf(fc.getExternalContext().getRequestParameterMap().get(getClientId(fc) + "_currentLevel"));

        int count = 0;
        for (UIComponent child : getChildren()) {
            if (child instanceof MasterDetailLevel) {
                MasterDetailLevel mdl = (MasterDetailLevel) child;
                count++;

                if (detailLevelToProcess == null && mdl.getLevel() == currentLevel) {
                    detailLevelToProcess = mdl;
                    levelPositionToProcess = count;
                }
            }
        }

        levelCount = count;

        if (detailLevelToProcess == null) {
            throw new FacesException("Current MasterDetailLevel to process not found.");
        }

        return detailLevelToProcess;
    }

    public MasterDetailLevel getDetailLevelToGo(final FacesContext fc) {
        if (detailLevelToGo != null) {
            return detailLevelToGo;
        }

        String strSelectedLevel = fc.getExternalContext().getRequestParameterMap().get(getClientId(fc) + "_selectedLevel");
        String strSelectedStep = fc.getExternalContext().getRequestParameterMap().get(getClientId(fc) + "_selectedStep");

        // selected level != null
        if (strSelectedLevel != null) {
            int selectedLevel = Integer.valueOf(strSelectedLevel);

            for (UIComponent child : getChildren()) {
                if (child instanceof MasterDetailLevel) {
                    MasterDetailLevel mdl = (MasterDetailLevel) child;
                    if (mdl.getLevel() == selectedLevel) {
                        detailLevelToGo = mdl;
                        return detailLevelToGo;
                    }
                }
            }

            throw new FacesException("MasterDetailLevel for selected level = " + selectedLevel + " not found.");
        }

        int step;
        if (strSelectedStep != null) {
            // selected step != null
            step = Integer.valueOf(strSelectedStep);
        } else {
            // selected level and selected step are null ==> go to the next level
            step = 1;
        }

        detailLevelToGo = getDetailLevelByStep(step);
        return detailLevelToGo;
    }

    public boolean isSelectDetailRequest(final FacesContext fc) {
        return fc.getPartialViewContext().isAjaxRequest() && fc.getExternalContext().getRequestParameterMap().containsKey(getClientId(fc) + "_selectDetailRequest");
    }

    protected boolean isSkipProcessing(final FacesContext fc) {
        MasterDetailLevel levelToG = getDetailLevelToGo(fc);
        int levelPositionToGo = 0;
        for (UIComponent child : getChildren()) {
            if (child instanceof MasterDetailLevel) {
                MasterDetailLevel mdl = (MasterDetailLevel) child;
                levelPositionToGo++;

                if (mdl.getLevel() == levelToG.getLevel()) {
                    break;
                }
            }
        }

        return (levelPositionToGo <= levelPositionToProcess);
    }

    protected MasterDetailLevel getDetailLevelByStep(final int step) {
        int levelPositionToGo = levelPositionToProcess + step;
        if (levelPositionToGo < 1) {
            levelPositionToGo = 1;
        } else if (levelPositionToGo > levelCount) {
            levelPositionToGo = levelCount;
        }

        int pos = 0;
        for (UIComponent child : getChildren()) {
            if (child instanceof MasterDetailLevel) {
                MasterDetailLevel mdl = (MasterDetailLevel) child;
                pos++;

                if (pos == levelPositionToGo) {
                    return mdl;
                }
            }
        }

        // should not happen
        return null;
    }
}
