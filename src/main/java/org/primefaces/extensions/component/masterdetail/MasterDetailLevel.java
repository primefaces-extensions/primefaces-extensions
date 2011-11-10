/*
* @author  Oleg Varaksin (ovaraksin@googlemail.com)
* $$Id$$
*/

package org.primefaces.extensions.component.masterdetail;

import javax.el.ValueExpression;
import javax.faces.component.UIComponentBase;
import java.util.ArrayList;
import java.util.List;

public class MasterDetailLevel extends UIComponentBase
{
    public static final String COMPONENT_FAMILY = "com.innflow.ebtam.webapp.components";
    private static final String DEFAULT_RENDERER = "com.innflow.ebtam.webapp.components.MasterDetailLevel";
    private static final String OPTIMIZED_PACKAGE = "com.innflow.ebtam.webapp.components.";

    /**
     * Properties that are tracked by state saving.
     */
    protected enum PropertyKeys
    {
        level, contextVar, levelLabel, levelDisabled;

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

    public MasterDetailLevel() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public int getLevel() {
        return (Integer) getStateHelper().eval(PropertyKeys.level, null);
    }

    public void setLevel(final int level) {
        setAttribute(PropertyKeys.level, level);
    }

    public String getContextVar() {
        return (String) getStateHelper().eval(PropertyKeys.contextVar, null);
    }

    public void setContextVar(final String contextVar) {
        setAttribute(PropertyKeys.contextVar, contextVar);
    }

    public String getLevelLabel() {
        return (String) getStateHelper().eval(PropertyKeys.levelLabel, null);
    }

    public void setLevelLabel(final String levelLabel) {
        setAttribute(PropertyKeys.levelLabel, levelLabel);
    }

    public boolean isLevelDisabled() {
        return (Boolean) getStateHelper().eval(PropertyKeys.levelDisabled, false);
    }

    public void setLevelDisabled(final boolean levelDisabled) {
        setAttribute(PropertyKeys.levelDisabled, levelDisabled);
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
}
