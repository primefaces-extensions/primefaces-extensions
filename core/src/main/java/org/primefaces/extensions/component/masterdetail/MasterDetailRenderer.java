/*
 * Copyright (c) 2011-2026 PrimeFaces Extensions
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */
package org.primefaces.extensions.component.masterdetail;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import jakarta.faces.FacesException;
import jakarta.faces.component.EditableValueHolder;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.visit.VisitContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import jakarta.faces.convert.Converter;
import jakarta.faces.render.FacesRenderer;
import jakarta.faces.render.Renderer;

import org.primefaces.component.breadcrumb.BreadCrumb;
import org.primefaces.extensions.util.Attrs;
import org.primefaces.extensions.util.ExtLangUtils;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.MenuElement;
import org.primefaces.model.menu.MenuItem;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.FacetUtils;
import org.primefaces.util.FastStringWriter;
import org.primefaces.util.LangUtils;

/**
 * Renderer for the {@link MasterDetail} component.
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since 0.2
 */
@FacesRenderer(rendererType = MasterDetail.DEFAULT_RENDERER, componentFamily = MasterDetail.COMPONENT_FAMILY)
public class MasterDetailRenderer extends CoreRenderer<MasterDetail> {

    private static final String FACET_HEADER = "header";
    private static final String FACET_FOOTER = "footer";
    private static final String FACET_LABEL = Attrs.LABEL;

    @Override
    public void encodeEnd(final FacesContext fc, final MasterDetail component) throws IOException {
        final MasterDetailLevel mdl;

        if (component.isSelectDetailRequest(fc)) {
            // component has been navigated via SelectDetailLevel
            final MasterDetailLevel mdlToProcess = component.getDetailLevelToProcess(fc);

            if (fc.isValidationFailed()) {
                mdl = mdlToProcess;
            }
            else {
                mdl = getDetailLevelToEncode(fc, component, mdlToProcess, component.getDetailLevelToGo(fc));

                // reset last saved validation state and stored values of editable components
                final MasterDetailLevelVisitCallback visitCallback = new MasterDetailLevelVisitCallback();
                mdlToProcess.visitTree(VisitContext.createVisitContext(fc), visitCallback);

                final String preserveInputs = component.getPreserveInputs(fc);
                final String resetInputs = component.getResetInputs(fc);
                final String[] piIds = preserveInputs != null ? preserveInputs.split("[\\s,]+") : null;
                final String[] riIds = resetInputs != null ? resetInputs.split("[\\s,]+") : null;
                final boolean preserveAll = ExtLangUtils.contains(piIds, "@all");
                final boolean resetAll = ExtLangUtils.contains(riIds, "@all");

                final List<EditableValueHolder> editableValueHolders = visitCallback.getEditableValueHolders();
                for (final EditableValueHolder editableValueHolder : editableValueHolders) {
                    final String clientId = ((UIComponent) editableValueHolder).getClientId(fc);
                    if (resetAll || ExtLangUtils.contains(riIds, clientId)) {
                        editableValueHolder.resetValue();
                    }
                    else if (preserveAll || ExtLangUtils.contains(piIds, clientId)) {
                        editableValueHolder.setValue(getConvertedSubmittedValue(fc, editableValueHolder));
                    }
                    else {
                        // default behavior
                        editableValueHolder.resetValue();
                    }
                }
            }

            component.updateModel(fc, mdl);
        }
        else {
            // component has been navigated from the outside, e.g. GET request or POST update from another component
            mdl = component.getDetailLevelByLevel(component.getLevel());
        }

        if (mdl == null) {
            throw new FacesException(
                        "MasterDetailLevel [Level=" + component.getLevel() +
                                    "] must be nested inside a MasterDetail component!");
        }

        // render MasterDetailLevel
        encodeMarkup(fc, component, mdl);

        // reset calculated values
        component.resetCalculatedValues();
    }

    protected MasterDetailLevel getDetailLevelToEncode(final FacesContext fc, final MasterDetail component,
                final MasterDetailLevel mdlToProcess,
                final MasterDetailLevel mdlToGo) {
        if (component.getSelectLevelListener() != null) {
            final SelectLevelEvent selectLevelEvent = new SelectLevelEvent(component, mdlToProcess.getLevel(),
                        mdlToGo.getLevel());
            final int levelToEncode = (Integer) component.getSelectLevelListener()
                        .invoke(fc.getELContext(), new Object[] {selectLevelEvent});
            if (levelToEncode != mdlToGo.getLevel()) {
                // new MasterDetailLevel to go
                return component.getDetailLevelByLevel(levelToEncode);
            }
        }

        return mdlToGo;
    }

    protected void encodeMarkup(final FacesContext fc, final MasterDetail component, final MasterDetailLevel mdl)
                throws IOException {
        if (mdl == null) {
            throw new FacesException("MasterDetailLevel must be nested inside a MasterDetail component!");
        }
        final ResponseWriter writer = fc.getResponseWriter();
        final String clientId = component.getClientId(fc);
        final String styleClass = component.getStyleClass() == null ? "pe-master-detail" : "pe-master-detail " + component.getStyleClass();

        writer.startElement("div", component);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute(Attrs.CLASS, styleClass, "styleClass");
        if (component.getStyle() != null) {
            writer.writeAttribute(Attrs.STYLE, component.getStyle(), Attrs.STYLE);
        }

        if (component.isShowBreadcrumb()) {
            if (component.isBreadcrumbAboveHeader()) {
                // render breadcrumb and then header
                renderBreadcrumb(fc, component, mdl);
                encodeFacet(fc, component, component.getHeaderFacet());
            }
            else {
                // render header and then breadcrumb
                encodeFacet(fc, component, component.getHeaderFacet());
                renderBreadcrumb(fc, component, mdl);
            }
        }
        else {
            // render header without breadcrumb
            encodeFacet(fc, component, component.getHeaderFacet());
        }

        // render container for MasterDetailLevel
        writer.startElement("div", null);
        writer.writeAttribute("id", clientId + "_detaillevel", "id");
        writer.writeAttribute(Attrs.CLASS, "pe-master-detail-level", null);

        // try to get context value if contextVar exists
        Object contextValue = null;
        final String contextVar = mdl.getContextVar();
        if (LangUtils.isNotBlank(contextVar)) {
            contextValue = component.getContextValueFromFlow(fc, mdl, true);
        }

        if (contextValue != null) {
            final Map<String, Object> requestMap = fc.getExternalContext().getRequestMap();
            requestMap.put(contextVar, contextValue);
        }

        // render MasterDetailLevel
        mdl.encodeAll(fc);

        if (contextValue != null) {
            fc.getExternalContext().getRequestMap().remove(contextVar);
        }

        writer.endElement("div");

        // render footer
        encodeFacet(fc, component, component.getFooterFacet());
        writer.endElement("div");
    }

    protected void renderBreadcrumb(final FacesContext fc, final MasterDetail component, final MasterDetailLevel mdl)
                throws IOException {
        // get breadcrumb and its current model
        final BreadCrumb breadcrumb = component.getBreadcrumb();

        // update breadcrumb items
        updateBreadcrumb(fc, breadcrumb, component, mdl);

        if (!component.isShowBreadcrumbFirstLevel()) {
            final int levelToRender = mdl.getLevel();
            if (levelToRender == 1) {
                breadcrumb.setStyleClass("ui-helper-hidden");
            }
            else {
                breadcrumb.setStyleClass("");
            }
        }

        // render breadcrumb
        breadcrumb.encodeAll(fc);
    }

    protected void encodeFacet(final FacesContext fc, final MasterDetail component, final UIComponent facet)
                throws IOException {
        if (FacetUtils.shouldRenderFacet(facet)) {
            facet.encodeAll(fc);
        }
    }

    protected void updateBreadcrumb(final FacesContext fc, final BreadCrumb breadcrumb, final MasterDetail component,
                final MasterDetailLevel mdlToRender) throws IOException {
        boolean lastMdlFound = false;
        final int levelToRender = mdlToRender.getLevel();
        final boolean isShowAllBreadcrumbItems = component.isShowAllBreadcrumbItems();

        for (final UIComponent child : component.getChildren()) {
            if (child instanceof MasterDetailLevel) {
                final MasterDetailLevel mdl = (MasterDetailLevel) child;
                final DefaultMenuItem menuItem = getMenuItemByLevel(breadcrumb, component, mdl);
                if (menuItem == null) {
                    // note: don't throw exception because menuItem can be null when MasterDetail is within DataTable
                    return;
                }

                if (!child.isRendered()) {
                    menuItem.setRendered(false);
                    if (!lastMdlFound) {
                        lastMdlFound = mdl.getLevel() == mdlToRender.getLevel();
                    }

                    continue;
                }

                if (lastMdlFound && !isShowAllBreadcrumbItems) {
                    menuItem.setRendered(false);
                }
                else {
                    menuItem.setRendered(true);

                    final Object contextValue = component.getContextValueFromFlow(fc, mdl,
                                mdl.getLevel() == mdlToRender.getLevel());
                    final String contextVar = mdl.getContextVar();
                    final boolean putContext = LangUtils.isNotBlank(contextVar) && contextValue != null;

                    if (putContext) {
                        final Map<String, Object> requestMap = fc.getExternalContext().getRequestMap();
                        requestMap.put(contextVar, contextValue);
                    }

                    final UIComponent facet = mdl.getFacet(FACET_LABEL);
                    if (FacetUtils.shouldRenderFacet(facet)) {
                        // swap writers
                        final ResponseWriter writer = fc.getResponseWriter();
                        final FastStringWriter fsw = new FastStringWriter();
                        final ResponseWriter clonedWriter = writer.cloneWithWriter(fsw);
                        fc.setResponseWriter(clonedWriter);

                        // render facet's children
                        facet.encodeAll(fc);

                        // restore the original writer
                        fc.setResponseWriter(writer);

                        // set menuitem label from facet
                        menuItem.setValue(ExtLangUtils.unescapeXml(fsw.toString()));
                    }
                    else {
                        // set menuitem label from tag attribute
                        menuItem.setValue(mdl.getLevelLabel());
                    }

                    if (isShowAllBreadcrumbItems && lastMdlFound) {
                        menuItem.setDisabled(true);
                    }
                    else {
                        menuItem.setDisabled(mdl.isLevelDisabled());
                    }

                    if (putContext) {
                        fc.getExternalContext().getRequestMap().remove(contextVar);
                    }

                    if (!menuItem.isDisabled()) {
                        // set current level parameter
                        updateUIParameter(menuItem, component.getClientId(fc) + MasterDetail.CURRENT_LEVEL,
                                    levelToRender);
                    }
                }

                if (!lastMdlFound) {
                    lastMdlFound = mdl.getLevel() == mdlToRender.getLevel();
                }
            }
        }
    }

    protected DefaultMenuItem getMenuItemByLevel(final BreadCrumb breadcrumb, final MasterDetail component,
                final MasterDetailLevel mdl) {
        final String menuItemId = component.getId() + "_bcItem_" + mdl.getLevel();
        for (final MenuElement child : breadcrumb.getModel().getElements()) {
            if (menuItemId.equals(child.getId())) {
                return (DefaultMenuItem) child;
            }
        }

        return null;
    }

    protected void updateUIParameter(final MenuItem menuItem, final String name, final Object value) {
        final Map<String, List<String>> params = menuItem.getParams();
        if (params == null) {
            return;
        }

        for (final String key : params.keySet()) {
            if (key.equals(name)) {
                params.remove(key);
                menuItem.setParam(name, value);

                break;
            }
        }
    }

    @Override
    public void encodeChildren(final FacesContext fc, final MasterDetail component) {
        // rendering happens on encodeEnd
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    public static Object getConvertedSubmittedValue(final FacesContext fc, final EditableValueHolder evh) {
        final Object submittedValue = evh.getSubmittedValue();
        if (submittedValue == null) {
            return null;
        }

        try {
            final UIComponent component = (UIComponent) evh;
            final Renderer renderer = getRenderer(fc, component);
            if (renderer != null) {
                // convert submitted value by renderer
                return renderer.getConvertedValue(fc, component, submittedValue);
            }
            else if (submittedValue instanceof String) {
                // convert submitted value by registered (implicit or explicit)
                // converter
                final Converter converter = ComponentUtils.getConverter(fc, component);
                if (converter != null) {
                    return converter.getAsObject(fc, component, (String) submittedValue);
                }
            }
        }
        catch (final Exception e) {
            // a conversion error occurred
            return null;
        }

        return submittedValue;
    }

    public static Renderer getRenderer(final FacesContext fc, final UIComponent component) {
        final String rendererType = component.getRendererType();
        if (rendererType != null) {
            return fc.getRenderKit().getRenderer(component.getFamily(), rendererType);
        }

        return null;
    }
}
