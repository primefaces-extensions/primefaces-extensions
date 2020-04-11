/*
 * Copyright 2011-2020 PrimeFaces Extensions
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.primefaces.extensions.component.masterdetail;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.render.Renderer;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.primefaces.component.breadcrumb.BreadCrumb;
import org.primefaces.extensions.util.Attrs;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.MenuElement;
import org.primefaces.model.menu.MenuItem;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.FastStringWriter;
import org.primefaces.util.LangUtils;

/**
 * Renderer for the {@link MasterDetail} component.
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since 0.2
 */
public class MasterDetailRenderer extends CoreRenderer {

    private static final String FACET_HEADER = "header";
    private static final String FACET_FOOTER = "footer";
    private static final String FACET_LABEL = Attrs.LABEL;

    @Override
    public void encodeEnd(final FacesContext fc, final UIComponent component) throws IOException {
        final MasterDetail masterDetail = (MasterDetail) component;
        MasterDetailLevel mdl;

        if (masterDetail.isSelectDetailRequest(fc)) {
            // component has been navigated via SelectDetailLevel
            final MasterDetailLevel mdlToProcess = masterDetail.getDetailLevelToProcess(fc);

            if (fc.isValidationFailed()) {
                mdl = mdlToProcess;
            }
            else {
                mdl = getDetailLevelToEncode(fc, masterDetail, mdlToProcess, masterDetail.getDetailLevelToGo(fc));

                // reset last saved validation state and stored values of editable components
                final MasterDetailLevelVisitCallback visitCallback = new MasterDetailLevelVisitCallback();
                mdlToProcess.visitTree(VisitContext.createVisitContext(fc), visitCallback);

                final String preserveInputs = masterDetail.getPreserveInputs(fc);
                final String resetInputs = masterDetail.getResetInputs(fc);
                final String[] piIds = preserveInputs != null ? preserveInputs.split("[\\s,]+") : null;
                final String[] riIds = resetInputs != null ? resetInputs.split("[\\s,]+") : null;
                final boolean preserveAll = ArrayUtils.contains(piIds, "@all");
                final boolean resetAll = ArrayUtils.contains(riIds, "@all");

                final List<EditableValueHolder> editableValueHolders = visitCallback.getEditableValueHolders();
                for (final EditableValueHolder editableValueHolder : editableValueHolders) {
                    final String clientId = ((UIComponent) editableValueHolder).getClientId(fc);
                    if (resetAll || ArrayUtils.contains(riIds, clientId)) {
                        editableValueHolder.resetValue();
                    }
                    else if (preserveAll || ArrayUtils.contains(piIds, clientId)) {
                        editableValueHolder.setValue(getConvertedSubmittedValue(fc, editableValueHolder));
                    }
                    else {
                        // default behavior
                        editableValueHolder.resetValue();
                    }
                }
            }

            masterDetail.updateModel(fc, mdl);
        }
        else {
            // component has been navigated from the outside, e.g. GET request or POST update from another component
            mdl = masterDetail.getDetailLevelByLevel(masterDetail.getLevel());
        }

        if (mdl == null) {
            throw new FacesException(
                        "MasterDetailLevel [Level=" + String.valueOf(masterDetail.getLevel()) + "] must be nested inside a MasterDetail component!");
        }

        // render MasterDetailLevel
        encodeMarkup(fc, masterDetail, mdl);

        // reset calculated values
        masterDetail.resetCalculatedValues();
    }

    protected MasterDetailLevel getDetailLevelToEncode(final FacesContext fc, final MasterDetail masterDetail, final MasterDetailLevel mdlToProcess,
                final MasterDetailLevel mdlToGo) {
        if (masterDetail.getSelectLevelListener() != null) {
            final SelectLevelEvent selectLevelEvent = new SelectLevelEvent(masterDetail, mdlToProcess.getLevel(), mdlToGo.getLevel());
            final int levelToEncode = (Integer) masterDetail.getSelectLevelListener().invoke(fc.getELContext(), new Object[] {selectLevelEvent});
            if (levelToEncode != mdlToGo.getLevel()) {
                // new MasterDetailLevel to go
                return masterDetail.getDetailLevelByLevel(levelToEncode);
            }
        }

        return mdlToGo;
    }

    protected void encodeMarkup(final FacesContext fc, final MasterDetail masterDetail, final MasterDetailLevel mdl) throws IOException {
        if (mdl == null) {
            throw new FacesException("MasterDetailLevel must be nested inside a MasterDetail component!");
        }
        final ResponseWriter writer = fc.getResponseWriter();
        final String clientId = masterDetail.getClientId(fc);
        final String styleClass = masterDetail.getStyleClass() == null ? "pe-master-detail" : "pe-master-detail " + masterDetail.getStyleClass();

        writer.startElement("div", masterDetail);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute(Attrs.CLASS, styleClass, "styleClass");
        if (masterDetail.getStyle() != null) {
            writer.writeAttribute(Attrs.STYLE, masterDetail.getStyle(), Attrs.STYLE);
        }

        if (masterDetail.isShowBreadcrumb()) {
            if (masterDetail.isBreadcrumbAboveHeader()) {
                // render breadcrumb and then header
                renderBreadcrumb(fc, masterDetail, mdl);
                encodeFacet(fc, masterDetail, FACET_HEADER);
            }
            else {
                // render header and then breadcrumb
                encodeFacet(fc, masterDetail, FACET_HEADER);
                renderBreadcrumb(fc, masterDetail, mdl);
            }
        }
        else {
            // render header without breadcrumb
            encodeFacet(fc, masterDetail, FACET_HEADER);
        }

        // render container for MasterDetailLevel
        writer.startElement("div", null);
        writer.writeAttribute("id", clientId + "_detaillevel", "id");
        writer.writeAttribute(Attrs.CLASS, "pe-master-detail-level", null);

        // try to get context value if contextVar exists
        Object contextValue = null;
        final String contextVar = mdl.getContextVar();
        if (!LangUtils.isValueBlank(contextVar)) {
            contextValue = masterDetail.getContextValueFromFlow(fc, mdl, true);
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
        encodeFacet(fc, masterDetail, FACET_FOOTER);
        writer.endElement("div");
    }

    protected void renderBreadcrumb(final FacesContext fc, final MasterDetail masterDetail, final MasterDetailLevel mdl) throws IOException {
        // get breadcrumb and its current model
        final BreadCrumb breadcrumb = masterDetail.getBreadcrumb();

        // update breadcrumb items
        updateBreadcrumb(fc, breadcrumb, masterDetail, mdl);

        // render breadcrumb
        breadcrumb.encodeAll(fc);
    }

    protected void encodeFacet(final FacesContext fc, final UIComponent component, final String name) throws IOException {
        final UIComponent facet = component.getFacet(name);
        if (ComponentUtils.shouldRenderFacet(facet)) {
            facet.encodeAll(fc);
        }
    }

    protected void updateBreadcrumb(final FacesContext fc, final BreadCrumb breadcrumb, final MasterDetail masterDetail,
                final MasterDetailLevel mdlToRender) throws IOException {
        boolean lastMdlFound = false;
        final int levelToRender = mdlToRender.getLevel();
        final boolean isShowAllBreadcrumbItems = masterDetail.isShowAllBreadcrumbItems();

        for (final UIComponent child : masterDetail.getChildren()) {
            if (child instanceof MasterDetailLevel) {
                final MasterDetailLevel mdl = (MasterDetailLevel) child;
                final DefaultMenuItem menuItem = getMenuItemByLevel(breadcrumb, masterDetail, mdl);
                if (menuItem == null) {
                    // note: don't throw exception because menuItem can be null when MasterDetail is within DataTable
                    // throw new FacesException("MenuItem to master detail level " + mdl.getLevel() + " was not found");
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

                    final Object contextValue = masterDetail.getContextValueFromFlow(fc, mdl, mdl.getLevel() == mdlToRender.getLevel());
                    final String contextVar = mdl.getContextVar();
                    final boolean putContext = !LangUtils.isValueBlank(contextVar) && contextValue != null;

                    if (putContext) {
                        final Map<String, Object> requestMap = fc.getExternalContext().getRequestMap();
                        requestMap.put(contextVar, contextValue);
                    }

                    final UIComponent facet = mdl.getFacet(FACET_LABEL);
                    if (ComponentUtils.shouldRenderFacet(facet)) {
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
                        menuItem.setValue(StringEscapeUtils.unescapeHtml4(fsw.toString()));
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
                        updateUIParameter(menuItem, masterDetail.getClientId(fc) + MasterDetail.CURRENT_LEVEL, levelToRender);
                    }
                }

                if (!lastMdlFound) {
                    lastMdlFound = mdl.getLevel() == mdlToRender.getLevel();
                }
            }
        }
    }

    protected DefaultMenuItem getMenuItemByLevel(final BreadCrumb breadcrumb, final MasterDetail masterDetail, final MasterDetailLevel mdl) {
        final String menuItemId = masterDetail.getId() + "_bcItem_" + mdl.getLevel();
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
    public void encodeChildren(final FacesContext fc, final UIComponent component) throws IOException {
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
                // convert submitted value by registred (implicit or explicit)
                // converter
                final Converter converter = ComponentUtils.getConverter(fc, component);
                if (converter != null) {
                    return converter.getAsObject(fc, component, (String) submittedValue);
                }
            }
        }
        catch (final Exception e) {
            // an conversion error occured
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
