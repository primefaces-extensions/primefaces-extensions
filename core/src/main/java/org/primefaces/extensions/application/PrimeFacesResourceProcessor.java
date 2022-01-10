/*
 * Copyright (c) 2011-2022 PrimeFaces Extensions
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
package org.primefaces.extensions.application;

import java.util.Locale;
import java.util.UUID;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.Resource;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.primefaces.config.PrimeConfiguration;
import org.primefaces.context.PrimeApplicationContext;
import org.primefaces.context.PrimeRequestContext;
import org.primefaces.util.LocaleUtils;

/**
 * Creates a custom PhaseListener for RENDER_RESPONSE phase which will during beforePhase() dynamically add those PrimeFaces resources via
 * UIViewRoot#addComponentResource(). This will run far before those @ResourceDependency annotations are processed. This satisfies PrimeFaces' intent of having
 * those hardcoded resources to be rendered before of the dependencies of their components.
 * <p>
 * Register it as below in faces-config.xml:
 * </p>
 * 
 * <pre>
 *   &lt;lifecycle&gt;
 *      &lt;phase-listener&gt;org.primefaces.extensions.application.PrimeFacesResourceProcessor&lt;/phase-listener&gt;
 *   &lt;/lifecycle&gt;
 * </pre>
 *
 * @see <a href="https://github.com/omnifaces/omnifaces/wiki/Combine-hardcoded-PrimeFaces-resources-using-CombinedResourceHandler">OmniFaces</a>
 * @since 10.0.0
 */
public class PrimeFacesResourceProcessor implements PhaseListener {

    private static final long serialVersionUID = 1L;
    private static final String LIBRARY = "primefaces";

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }

    @Override
    public void afterPhase(final PhaseEvent event) {
        // NOOP.
    }

    @Override
    public void beforePhase(final PhaseEvent event) {
        final FacesContext context = event.getFacesContext();
        final PrimeRequestContext requestContext = PrimeRequestContext.getCurrentInstance(context);
        final PrimeApplicationContext applicationContext = requestContext.getApplicationContext();
        final PrimeConfiguration configuration = applicationContext.getConfig();

        final String theme;
        final String themeParamValue = configuration.getTheme();

        if (themeParamValue != null) {
            final ELContext elContext = context.getELContext();
            final ExpressionFactory expressionFactory = context.getApplication().getExpressionFactory();
            final ValueExpression ve = expressionFactory.createValueExpression(elContext, themeParamValue, String.class);
            theme = (String) ve.getValue(elContext);
        }
        else {
            theme = "saga"; // default
        }

        if (theme != null && !"none".equals(theme)) {
            encodeCSS(context, LIBRARY + "-" + theme, "theme.css");
        }

        // Icons
        if (configuration.isPrimeIconsEnabled()) {
            encodeCSS(context, LIBRARY, "primeicons/primeicons.css");
        }

        if (configuration.isClientSideValidationEnabled()) {
            encodeValidationResources(context, configuration.isBeanValidationEnabled());
        }

        if (configuration.isClientSideLocalizationEnabled()) {
            final Locale locale = LocaleUtils.getCurrentLocale(context);
            encodeJS(context, "locales/locale-" + locale.getLanguage() + ".js");
        }
    }

    protected void encodeValidationResources(final FacesContext context, final boolean beanValidationEnabled) {
        encodeJS(context, "validation/validation.js");

        if (beanValidationEnabled) {
            encodeJS(context, "validation/validation.bv.js");
        }
    }

    private void encodeCSS(final FacesContext context, final String library, final String name) {
        final Resource resource = context.getApplication().getResourceHandler().createResource(name, library);
        if (resource == null) {
            throw new FacesException(
                        "Error loading CSS, cannot find \"" + name + "\" resource of \"" + library + "\" library");
        }
        final UIOutput css = new UIOutput();
        css.setId("css-" + UUID.randomUUID());
        css.setRendererType("javax.faces.resource.Stylesheet");
        css.getAttributes().put("library", library);
        css.getAttributes().put("name", name);
        context.getViewRoot().addComponentResource(context, css, "head");
    }

    private void encodeJS(final FacesContext context, final String name) {
        final Resource resource = context.getApplication().getResourceHandler().createResource(name, LIBRARY);
        if (resource == null) {
            throw new FacesException("Error loading JavaScript, cannot find \"" + name + "\" resource of \"" + LIBRARY +
                        "\" library");
        }
        final UIOutput js = new UIOutput();
        js.setId("js-" + UUID.randomUUID());
        js.setRendererType("javax.faces.resource.Script");
        js.getAttributes().put("library", LIBRARY);
        js.getAttributes().put("name", name);
        context.getViewRoot().addComponentResource(context, js, "head");
    }

}
