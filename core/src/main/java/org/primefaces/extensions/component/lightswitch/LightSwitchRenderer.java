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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.primefaces.renderkit.CoreRenderer;

/**
 * Renderer for the {@link LightSwitch} component.
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 * @since 10.0
 */
public class LightSwitchRenderer extends CoreRenderer {

    public static final String ATTR_THEME = "org.primefaces.extensions.component.lightswitch.THEME";

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        LightSwitch lightSwitch = (LightSwitch) component;

        String theme = context.getExternalContext().getRequestParameterMap().get(component.getId() + "_theme");
        if (theme != null) {
            lightSwitch.getValueExpression("selected").setValue(context.getELContext(), theme);
            return;
        }

        encodeScript(context, lightSwitch);
    }

    protected void encodeScript(FacesContext context, LightSwitch lightSwitch) throws IOException {
        getWidgetBuilder(context)
                    .init("ExtLightSwitch", lightSwitch)
                    .attr("selected", lightSwitch.getSelected())
                    .attr("light", lightSwitch.getLight())
                    .attr("dark", lightSwitch.getDark())
                    .finish();
    }

}
