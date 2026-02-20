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
package org.primefaces.extensions.component.lightswitch;

import java.io.IOException;
import java.util.Map;

import jakarta.faces.context.FacesContext;
import jakarta.faces.render.FacesRenderer;

import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.Constants;
import org.primefaces.util.WidgetBuilder;

/**
 * Renderer for the {@link LightSwitch} component.
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 * @since 10.0
 */
@FacesRenderer(componentFamily = LightSwitchBase.COMPONENT_FAMILY, rendererType = LightSwitchBase.DEFAULT_RENDERER)
public class LightSwitchRenderer extends CoreRenderer<LightSwitch> {

    @Override
    public void decode(final FacesContext context, final LightSwitch component) {
        decodeBehaviors(context, component);
    }

    @Override
    public void encodeEnd(final FacesContext context, final LightSwitch component) throws IOException {
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        final String eventName = params.get(Constants.RequestParams.PARTIAL_BEHAVIOR_EVENT_PARAM);
        if (LightSwitch.EVENT_SWITCH.equals(eventName)) {
            return;
        }

        String theme = params.get(component.getClientId(context) + "_theme");
        if (theme != null) {
            component.setSelectedByValueExpression(context, theme);
            return;
        }

        encodeScript(context, component);
    }

    protected void encodeScript(FacesContext context, LightSwitch component) throws IOException {
        final WidgetBuilder wb = getWidgetBuilder(context)
                    .init("ExtLightSwitch", component)
                    .attr("selected", component.getSelected())
                    .attr("light", component.getLight())
                    .attr("dark", component.getDark())
                    .attr("automatic", component.isAutomatic())
                    .attr("parent", component.getParent().getClientId());

        encodeClientBehaviors(context, component);

        wb.finish();
    }

}
