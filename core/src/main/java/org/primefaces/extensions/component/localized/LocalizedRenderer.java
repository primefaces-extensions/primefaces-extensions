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
package org.primefaces.extensions.component.localized;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

import jakarta.el.ExpressionFactory;
import jakarta.faces.FacesException;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.render.FacesRenderer;
import jakarta.servlet.ServletContext;

import org.primefaces.extensions.config.PrimeExtensionsEnvironment;
import org.primefaces.extensions.util.CommonMarkWrapper;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.EscapeUtils;
import org.primefaces.util.FacetUtils;
import org.primefaces.util.LangUtils;

/**
 * Renderer for the {@link Localized} component.
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 * @since 11.0.3
 */
@FacesRenderer(rendererType = Localized.DEFAULT_RENDERER, componentFamily = Localized.COMPONENT_FAMILY)
public class LocalizedRenderer extends CoreRenderer<Localized> {

    public static final String WEB_FOLDER = "WEB-INF/pfe-localized";
    public static final String QUARKUS_FOLDER = "pfe-localized";

    @Override
    public void encodeEnd(final FacesContext context, final Localized component) throws IOException {
        encodeMarkup(context, component);
    }

    protected void encodeMarkup(final FacesContext context, final Localized component) throws IOException {
        if (LangUtils.isBlank(component.getName())) {
            encodeFromFacet(context, component);
        }
        else {
            encodeFromFile(context, component);
        }
    }

    protected void encodeFromFacet(final FacesContext context, final Localized component) throws IOException {
        final Locale locale = component.calculateLocale(context);
        final String language = locale.getLanguage();
        final String country = locale.getCountry();
        resolveFacet(component, language, country).encodeAll(context);
    }

    protected UIComponent resolveFacet(final Localized component, final String language, final String country) {
        UIComponent facet = component.getFacet(language + "_" + country);
        if (FacetUtils.shouldRenderFacet(facet)) {
            return facet;
        }
        facet = component.getFacet(language);
        if (FacetUtils.shouldRenderFacet(facet)) {
            return facet;
        }
        facet = component.getDefaultFacet();
        if (FacetUtils.shouldRenderFacet(facet)) {
            return facet;
        }
        throw new FacesException("No facet found for " + language + "_" + country + ", nor a 'default' facet");
    }

    protected void encodeFromFile(final FacesContext context, final Localized component) throws IOException {
        final Path filePath = resolvePath(context, component);
        final byte[] bytes = Files.readAllBytes(filePath);
        String value = new String(bytes);
        if (component.isEvalEl()) {
            value = evaluateEl(context, value);
        }
        if (component.isEscape()) {
            value = EscapeUtils.forHtml(value);
        }
        if (component.isMarkdown()) {
            value = toHTML(context, value);
        }
        context.getResponseWriter().append(value);
    }

    protected Path resolvePath(final FacesContext context, final Localized component) {
        final ServletContext servletContext = ((ServletContext) context.getExternalContext().getContext());
        final String web = servletContext.getRealPath(WEB_FOLDER);
        final String meta = servletContext.getRealPath(QUARKUS_FOLDER);
        final Locale locale = component.calculateLocale(context);
        final String language = locale.getLanguage();
        final String country = locale.getCountry();
        final String folder = component.getFolder();
        final String name = component.getName();
        Path path = resolvePath(web, folder, name, language, country);
        if (path == null) {
            path = resolvePath(web, null, name, language, country);
        }
        if (path == null) {
            path = resolvePath(meta, folder, name, language, country);
        }
        if (path == null) {
            path = resolvePath(meta, null, name, language, country);
        }
        if (path == null) {
            throw new IllegalStateException("Cannot find Localized file for: " + component.getClientId(context));
        }
        return path;
    }

    protected Path resolvePath(final String base, final String folder, final String name, final String language,
                final String country) {
        final String baseFolder = LangUtils.isBlank(folder)
                    ? base
                    : base + "/" + folder;
        Path path = existingPath(baseFolder, name + "_" + language + "_" + country);
        if (path == null) {
            path = existingPath(baseFolder, name + "_" + language);
        }
        if (path == null) {
            path = existingPath(baseFolder, name);
        }
        return path;
    }

    protected Path existingPath(final String first, final String more) {
        final Path path = Paths.get(first, more);
        Path existingPath = path.toFile().exists() ? path : null;
        if (existingPath == null) {
            final String resourcePath = first + "/" + more;
            try {
                // Quarkus
                final URL url = Thread.currentThread().getContextClassLoader().getResource(resourcePath);
                if (url != null) {
                    existingPath = Paths.get(url.toURI());
                }
            }
            catch (final URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        return existingPath;
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