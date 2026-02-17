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
package org.primefaces.extensions.component.orgchart;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.el.ValueExpression;
import jakarta.faces.FacesException;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import jakarta.faces.render.FacesRenderer;

import org.primefaces.extensions.util.Attrs;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;
import org.primefaces.util.HTML;
import org.primefaces.util.WidgetBuilder;

/**
 * <code>orgchart</code> component.
 *
 * @author @jxmai / last modified by $Author$
 * @version $Revision$
 * @since 7.0
 */
@FacesRenderer(rendererType = OrgChart.DEFAULT_RENDERER, componentFamily = OrgChart.COMPONENT_FAMILY)
public class OrgChartRenderer extends CoreRenderer<OrgChart> {

    private static final String JSON_CHILDREN = "children";

    @Override
    public void encodeEnd(final FacesContext context, final OrgChart component) throws IOException {
        encodeMarkup(context, component);
        encodeScript(context, component);
    }

    @Override
    public void decode(final FacesContext context, final OrgChart component) {
        decodeNodeStructure(context, component);
        decodeBehaviors(context, component);
    }

    private static void decodeNodeStructure(final FacesContext context, final OrgChart component) {
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();

        final String hierarchyStr = params.get(component.getClientId() + "_hierarchy");

        if (null != hierarchyStr && !hierarchyStr.isEmpty()) {
            final JSONObject hierarchy = new JSONObject(hierarchyStr);

            final ValueExpression ve = component.getValueExpression("value");
            OrgChartNode root = ve.getValue(context.getELContext());

            final List<OrgChartNode> orgChartNodes = OrgChartHelper.getAllNodesTraverseFromRoot(root);

            for (final OrgChartNode o : orgChartNodes) {
                o.clearChildren();
            }

            final Map<String, OrgChartNode> hashMap = OrgChartHelper
                        .parseOrgChartNodesIntoHashMap(orgChartNodes);

            root = buildNodesFromJSON(hashMap, hierarchy, null);

            ve.setValue(context.getELContext(), root);

        }
    }

    public static OrgChartNode buildNodesFromJSON(final Map<String, OrgChartNode> orgChartNodes,
                final JSONObject hierarchy, OrgChartNode parentNode) {
        final String id = (String) hierarchy.get("id");
        final OrgChartNode node = orgChartNodes.get(id);

        if (parentNode == null) {
            parentNode = node;
        }

        if (hierarchy.has(JSON_CHILDREN)) {
            final JSONArray array = (JSONArray) hierarchy.get(JSON_CHILDREN);

            for (int i = 0; i < array.length(); i++) {
                final JSONObject jsonObject = array.getJSONObject(i);
                buildNodesFromJSON(orgChartNodes, jsonObject,
                            orgChartNodes.get(jsonObject.get("id")));

                parentNode.addChild(orgChartNodes.get(jsonObject.get("id")));

            }
        }

        return node;

    }

    private void encodeMarkup(final FacesContext context, final OrgChart component) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = component.getClientId();
        final String widgetVar = component.resolveWidgetVar();
        final String styleClass = getStyleClassBuilder(context)
                    .add(OrgChart.STYLE_CLASS)
                    .add(component.getStyleClass())
                    .build();

        writer.startElement("div", component);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute(HTML.WIDGET_VAR, widgetVar, null);
        writer.writeAttribute(Attrs.CLASS, styleClass, "styleClass");
        if (component.getStyle() != null) {
            writer.writeAttribute(Attrs.STYLE, component.getStyle(), Attrs.STYLE);
        }
        writer.endElement("div");
    }

    private void encodeScript(final FacesContext context, final OrgChart component)
                throws IOException {
        final WidgetBuilder wb = getWidgetBuilder(context);

        final OrgChartNode orgChartNode;
        if (component.getValue() == null) {
            throw new FacesException("The value attribute must be OrgChartNode");
        }
        else {
            if (!(component.getValue() instanceof OrgChartNode)) {
                throw new FacesException("The value attribute must be OrgChartNode");
            }
            else {
                orgChartNode = (OrgChartNode) component.getValue();
            }
        }

        final String data = toJSON(orgChartNode, orgChartNode.getChildren()).toString();

        wb.init("ExtOrgChart", component);
        wb.attr("nodeId", component.getNodeId());
        wb.attr("nodeContent", component.getNodeContent());
        wb.attr("direction", component.getDirection());
        wb.attr("pan", component.getPan());
        wb.attr("toggleSiblingsResp", component.getToggleSiblingsResp());
        wb.attr("depth", component.getDepth());
        wb.attr("exportButton", component.getExportButton());
        wb.attr("exportFilename", component.getExportFilename());
        wb.attr("exportFileextension", component.getExportFileextension());
        wb.attr("parentNodeSymbol", component.getParentNodeSymbol());
        wb.attr("draggable", component.getDraggable());
        wb.attr("chartClass", component.getChartClass());
        wb.attr("zoom", component.getZoom());
        wb.attr("zoominLimit", component.getZoominLimit());
        wb.attr("zoomoutLimit", component.getZoomoutLimit());
        wb.attr("verticalDepth", component.getVerticalDepth());
        wb.attr("nodeTitle", component.getNodeTitle());
        wb.nativeAttr("extender", component.getExtender());
        wb.attr("data", data);

        encodeClientBehaviors(context, component);
        wb.finish();
    }

    public static JSONObject toJSON(final OrgChartNode orgChartNode, final List<OrgChartNode> children) {

        final JSONObject json = new JSONObject();

        if (null != orgChartNode.getId() && !orgChartNode.getId().isEmpty()) {
            json.put("id", orgChartNode.getId());
        }

        json.put("name", orgChartNode.getName());
        json.put("title", orgChartNode.getTitle());
        if (null != orgChartNode.getClassName() && !orgChartNode.getClassName().isEmpty()) {
            json.put("className", orgChartNode.getClassName());
        }

        if (orgChartNode.getChildCount() > 0) {
            final List<JSONObject> jsonChildren = new ArrayList<>();
            for (int i = 0; i < orgChartNode.getChildCount(); i++) {
                jsonChildren.add(toJSON(orgChartNode.getChildren().get(i),
                            orgChartNode.getChildren().get(i).getChildren()));
            }
            json.put(JSON_CHILDREN, jsonChildren);
        }

        return json;
    }

}