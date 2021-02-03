/*
 * Copyright (c) 2011-2021 PrimeFaces Extensions
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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.Constants;
import org.primefaces.util.WidgetBuilder;

/**
 * Renderer for the {@link LightSwitch} component.
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 * @since 10.0
 */
public class LightSwitchRenderer extends CoreRenderer {

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        decodeBehaviors(context, component);
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        LightSwitch lightSwitch = (LightSwitch) component;

        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        final String eventName = params.get(Constants.RequestParams.PARTIAL_BEHAVIOR_EVENT_PARAM);
        if (LightSwitch.EVENT_SWITCH.equals(eventName)) {
            return;
        }

        String theme = params.get(lightSwitch.getClientId(context) + "_theme");
        if (theme != null) {
            lightSwitch.setSelectedByValueExpression(context, theme);
            return;
        }

        encodeScript(context, lightSwitch);
    }

    protected void encodeScript(FacesContext context, LightSwitch lightSwitch) throws IOException {
        final WidgetBuilder wb = getWidgetBuilder(context)
                    .init("ExtLightSwitch", lightSwitch)
                    .attr("selected", lightSwitch.getSelected())
                    .attr("light", lightSwitch.getLight())
                    .attr("dark", lightSwitch.getDark())
                    .attr("automatic", lightSwitch.isAutomatic())
                    .attr("parent", lightSwitch.getParent().getClientId());

        encodeClientBehaviors(context, lightSwitch);

        wb.finish();
    }

}
