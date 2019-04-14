/**
 * Copyright 2011-2019 PrimeFaces Extensions
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

        for (final UIComponent child : component.getChildren()) {
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

        ClientBehaviorRenderingMode renderingMode = null;
        Collection<ClientBehaviorContext.Parameter> behaviorParameters = behaviorContext.getParameters();

        if (behaviorParameters != null && !behaviorParameters.isEmpty()) {
            for (ClientBehaviorContext.Parameter behaviorParameter : behaviorParameters) {
                if (behaviorParameter.getValue() != null && behaviorParameter.getValue() instanceof ClientBehaviorRenderingMode) {
                    renderingMode = (ClientBehaviorRenderingMode) behaviorParameter.getValue();
                    break;
                }
            }
        }

        if (ClientBehaviorRenderingMode.UNOBSTRUSIVE.equals(renderingMode)) {
            script.append("},ext);");
        }
        else {
            script.append("});");
        }

        return script.toString();
    }
}
