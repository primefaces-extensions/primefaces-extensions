/**
 * Copyright 2011-2018 PrimeFaces Extensions
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
package org.primefaces.extensions.component.orgchart;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.HTML;
import org.primefaces.util.WidgetBuilder;

/**
 * <code>orgchart</code> component.
 *
 * @author @jxmai / last modified by $Author$
 * @version $Revision$
 * @since 6.3
 */
public class OrgChartRenderer extends CoreRenderer {

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        final OrgChart orgChart = (OrgChart) component;
        encodeMarkup(context, orgChart);
        encodeScript(context, orgChart);
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {

        OrgChart orgChart = (OrgChart) component;

        decodeNodeStrucutre(context, orgChart);
        decodeBehaviors(context, component);
    }

    /**
     * 
     * @param context
     * @param orgChart
     */
    private void decodeNodeStrucutre(FacesContext context, OrgChart orgChart) {
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();

        String hierarchyStr = params.get(orgChart.getClientId() + "_hierarchy");

        if (null != hierarchyStr && !hierarchyStr.isEmpty()) {
            JSONObject hierarchy = new JSONObject(hierarchyStr);

            ValueExpression ve = orgChart.getValueExpression("value");
            OrgChartNode root = (OrgChartNode) ve.getValue(context.getELContext());

            List<OrgChartNode> orgChartNodes = OrgChartHelper.getAllNodesTraverseFromRoot(root);

            for (OrgChartNode o : orgChartNodes) {
                o.clearChildren();
            }

            HashMap<String, OrgChartNode> hashMap = OrgChartHelper
                    .parseOrgChartNodesIntoHashMap(orgChartNodes);

            root = null;

            root = buildNodesFromJSON(hashMap, hierarchy, null);

            ve.setValue(context.getELContext(), root);

        }
    }

    /**
     * 
     * @param orgChartNodes
     * @param hierarchy
     * @param parentNode
     */
    public OrgChartNode buildNodesFromJSON(HashMap<String, OrgChartNode> orgChartNodes,
            JSONObject hierarchy, OrgChartNode parentNode) {
        String id = (String) hierarchy.get("id");
        OrgChartNode node = orgChartNodes.get(id);

        if (parentNode == null) {
            parentNode = node;
        }

        if (hierarchy.has("children")) {
            JSONArray array = (JSONArray) hierarchy.get("children");

            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                buildNodesFromJSON(orgChartNodes, jsonObject,
                        orgChartNodes.get(jsonObject.get("id")));

                parentNode.addChild(orgChartNodes.get(jsonObject.get("id")));

            }
        }

        return node;

    }

    /**
     * 
     * @param context
     * @param orgChart
     * @throws IOException
     */
    private void encodeMarkup(FacesContext context, final OrgChart orgChart) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = orgChart.getClientId();
        final String widgetVar = orgChart.resolveWidgetVar();

        writer.startElement("div", orgChart);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute(HTML.WIDGET_VAR, widgetVar, null);
        writer.endElement("div");
    }

    /**
     * 
     * @param context
     * @param orgChart
     * @throws IOException
     */
    private void encodeScript(final FacesContext context, final OrgChart orgChart)
            throws IOException {
        final WidgetBuilder wb = getWidgetBuilder(context);
        final String clientId = orgChart.getClientId(context);

        OrgChartNode orgChartNode = null;
        if (null != orgChart.getValue()) {
            if (!(orgChart.getValue() instanceof OrgChartNode)) {
                throw new FacesException("The value attribute must be OrgChartNode");
            } 
            else {
                orgChartNode = (OrgChartNode) orgChart.getValue();
            }
        }

        String data = toJSON(orgChartNode, orgChartNode.getChildren()).toString();

        wb.init("ExtOrgChart", orgChart.resolveWidgetVar(), clientId);
        wb.attr("nodeId", orgChart.getNodeId());
        wb.attr("nodeContent", orgChart.getNodeContent());
        wb.attr("direction", orgChart.getDirection());
        wb.attr("pan", orgChart.getPan());
        wb.attr("toggleSiblingsResp", orgChart.getToggleSiblingsResp());
        wb.attr("depth", orgChart.getDepth());
        wb.attr("exportButton", orgChart.getExportButton());
        wb.attr("exportFilename", orgChart.getExportFilename());
        wb.attr("exportFileextension", orgChart.getExportFileextension());
        wb.attr("parentNodeSymbol", orgChart.getParentNodeSymbol());
        wb.attr("draggable", orgChart.getDraggable());
        wb.attr("chartClass", orgChart.getChartClass());
        wb.attr("zoom", orgChart.getZoom());
        wb.attr("zoominLimit", orgChart.getZoominLimit());
        wb.attr("zoomoutLimit", orgChart.getZoomoutLimit());
        wb.attr("verticalDepth", orgChart.getVerticalDepth());
        wb.attr("nodeTitle", orgChart.getNodeTitle());
        wb.attr("data", data);

        encodeClientBehaviors(context, orgChart);
        wb.finish();
    }

    public JSONObject toJSON(OrgChartNode orgChartNode, List<OrgChartNode> children) {

        JSONObject json = new JSONObject();

        if (null != orgChartNode.getId() && !orgChartNode.getId().isEmpty()) {
            json.put("id", orgChartNode.getId());
        }

        json.put("name", orgChartNode.getName());
        json.put("title", orgChartNode.getTitle());
        if (null != orgChartNode.getClassName() && !orgChartNode.getClassName().isEmpty()) {
            json.put("className", orgChartNode.getClassName());
        }

        if (!orgChartNode.getChildren().isEmpty()) {
            List<JSONObject> jsonChildren = new ArrayList<JSONObject>();
            for (int i = 0; i < orgChartNode.getChildren().size(); i++) {
                jsonChildren.add(toJSON(orgChartNode.getChildren().get(i),
                        orgChartNode.getChildren().get(i).getChildren()));
            }
            json.put("children", jsonChildren);
        }

        return json;
    }

}
