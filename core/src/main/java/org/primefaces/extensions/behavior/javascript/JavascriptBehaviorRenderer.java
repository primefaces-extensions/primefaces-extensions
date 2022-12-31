/*
 * Copyright (c) 2011-2023 PrimeFaces Extensions
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
package org.primefaces.extensions.behavior.javascript;

import java.util.Collection;

import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.context.FacesContext;
import javax.faces.render.ClientBehaviorRenderer;

import org.primefaces.component.api.ClientBehaviorRenderingMode;

/**
 * {@link ClientBehaviorRenderer} implementation for the {@link JavascriptBehavior}.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.2
 */
public class JavascriptBehaviorRenderer extends ClientBehaviorRenderer {

    @Override
    public String getScript(final ClientBehaviorContext behaviorContext, final ClientBehavior behavior) {
        final JavascriptBehavior javascriptBehavior = (JavascriptBehavior) behavior;
        if (javascriptBehavior.isDisabled()) {
            return null;
        }

        final FacesContext context = behaviorContext.getFacesContext();
        final UIComponent component = behaviorContext.getComponent();
        String source = behaviorContext.getSourceId();
        if (source == null) {
            source = component.getClientId(context);
        }

        final StringBuilder script = new StringBuilder();
        script.append("PrimeFacesExt.behavior.Javascript({");
        script.append("source:'").append(source).append("'");
        script.append(",event:'").append(behaviorContext.getEventName()).append("'");
        script.append(",execute:function(source,event,params,ext){");
        script.append(javascriptBehavior.getExecute()).append(";}");

        // params
        boolean paramWritten = false;

        for (int i = 0; i < component.getChildCount(); i++) {
            final UIComponent child = component.getChildren().get(i);
            if (child instanceof UIParameter) {
                final UIParameter parameter = (UIParameter) child;

                if (paramWritten) {
                    script.append(",");
                }
                else {
                    paramWritten = true;
                    script.append(",params:{");
                }

                script.append("'");
                script.append(parameter.getName()).append("':'").append(parameter.getValue());
                script.append("'");
            }
        }

        if (paramWritten) {
            script.append("}");
        }

        ClientBehaviorRenderingMode renderingMode = getClientBehaviorRenderingMode(behaviorContext);

        if (ClientBehaviorRenderingMode.UNOBSTRUSIVE.equals(renderingMode)) {
            script.append("},ext);");
        }
        else {
            script.append("});");
        }

        return script.toString();
    }

    protected static ClientBehaviorRenderingMode getClientBehaviorRenderingMode(final ClientBehaviorContext behaviorContext) {
        final Collection<ClientBehaviorContext.Parameter> behaviorParameters = behaviorContext.getParameters();
        if (behaviorParameters == null || behaviorParameters.isEmpty()) {
            return null;
        }
        for (final ClientBehaviorContext.Parameter behaviorParameter : behaviorParameters) {
            if (behaviorParameter.getValue() instanceof ClientBehaviorRenderingMode) {
                return (ClientBehaviorRenderingMode) behaviorParameter.getValue();
            }
        }
        return null;
    }
}
