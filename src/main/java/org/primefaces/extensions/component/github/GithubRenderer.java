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
package org.primefaces.extensions.component.github;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.HTML;
import org.primefaces.util.WidgetBuilder;

/**
 * Renderer for the {@link Github} component.
 *
 * @author Melloware mellowaredev@gmail.com
 * @since 6.1
 */
public class GithubRenderer extends CoreRenderer {

    /**
     * {@inheritDoc}
     */
    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final Github github = (Github) component;
        encodeMarkup(context, github);
        encodeScript(context, github);
    }

    /**
     * Create the HTML markup for the DOM.
     */
    private void encodeMarkup(final FacesContext context, final Github github) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = github.getClientId(context);
        final String widgetVar = github.resolveWidgetVar();

        writer.startElement("div", github);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute(HTML.WIDGET_VAR, widgetVar, null);
        writer.writeAttribute("data-repo", github.getRepository(), "data-repo");
        if (github.getStyleClass() != null) {
            writer.writeAttribute("class", github.getStyleClass(), "styleClass");
        }
        if (github.getStyle() != null) {
            writer.writeAttribute("style", github.getStyle(), "style");
        }
        writer.endElement("div");
    }

    /**
     * Create the Javascript.
     */
    private void encodeScript(final FacesContext context, final Github github) throws IOException {
        final WidgetBuilder wb = getWidgetBuilder(context);
        final String clientId = github.getClientId(context);
        wb.init("ExtGitHub", github.resolveWidgetVar(), clientId);
        wb.attr("iconForks", github.isIconForks());
        wb.attr("iconIssues", github.isIconIssues());
        wb.attr("iconStars", github.isIconStars());
        wb.finish();
    }

}
