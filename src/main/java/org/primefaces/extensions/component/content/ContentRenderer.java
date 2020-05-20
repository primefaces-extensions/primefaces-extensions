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
package org.primefaces.extensions.component.content;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.el.ExpressionFactory;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.primefaces.extensions.config.PrimeExtensionsEnvironment;
import org.primefaces.extensions.util.CommonMarkWrapper;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.EscapeUtils;

/**
 * Renderer for the {@link Content} component.
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 * @since 8.0.3
 */
public class ContentRenderer extends CoreRenderer {

    public static final String FOLDER_CONTENT = "WEB-INF/pfe-content";

    public static final String FILE_PART_SEPARATOR = "_";

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final Content content = (Content) component;
        encodeMarkup(context, content);
    }

    protected void encodeMarkup(final FacesContext context, final Content content) throws IOException {
        String value = Files.readString(resolve(context, content));
        if (content.isEvalEl()) {
            value = evaluateEl(context, value);
        }
        if (content.isEscape()) {
            value = EscapeUtils.forHtml(value);
        }
        if (content.isMarkdown()) {
            value = toHTML(context, value);
        }
        context.getResponseWriter().append(value);
    }

    protected Path resolve(final FacesContext context, final Content content) {
        final String contentFolder = ((ServletContext) context.getExternalContext().getContext()).getRealPath(FOLDER_CONTENT);
        final Locale locale = content.calculateLocale(context);
        final String language = locale.getLanguage();
        final String country = locale.getCountry();
        final String variant = content.getVariant();
        final String src = content.getSrc();
        final List<String> options = new ArrayList<>();
        options.add(src);
        if (!language.isEmpty()) {
            options.add(0, String.join(FILE_PART_SEPARATOR, src, language));
            if (!country.isEmpty()) {
                options.add(0, String.join(FILE_PART_SEPARATOR, src, language, country));
            }
        }
        if (variant != null) {
            options.add(0, String.join(FILE_PART_SEPARATOR, src, variant));
            if (!language.isEmpty()) {
                options.add(0, String.join(FILE_PART_SEPARATOR, src, variant, language));
                if (!country.isEmpty()) {
                    options.add(0, String.join(FILE_PART_SEPARATOR, src, variant, language, country));
                }
            }
        }
        for (String option : options) {
            final Path path = Path.of(contentFolder, option);
            if (path.toFile().exists()) {
                return path;
            }
        }
        throw new IllegalStateException("Cannot find content file for: " + content.getClientId(context));
    }

    protected String toHTML(final FacesContext context, final String value) {
        if (!PrimeExtensionsEnvironment.getCurrentInstance(context).isCommonmarkAvailable()) {
            throw new FacesException("CommonMark not available.");
        }
        return CommonMarkWrapper.toHTML(value);
    }

    protected String evaluateEl(final FacesContext context, final String value) {
        return (String) ExpressionFactory.newInstance()
                    .createValueExpression(context.getELContext(), value, String.class)
                    .getValue(context.getELContext());
    }

}
