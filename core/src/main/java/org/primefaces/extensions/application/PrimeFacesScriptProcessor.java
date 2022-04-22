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

import java.util.List;

import javax.faces.application.ProjectStage;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import javax.faces.lifecycle.ClientWindow;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.clientwindow.PrimeClientWindow;
import org.primefaces.clientwindow.PrimeClientWindowUtils;
import org.primefaces.config.PrimeConfiguration;
import org.primefaces.config.PrimeEnvironment;
import org.primefaces.context.PrimeApplicationContext;
import org.primefaces.context.PrimeRequestContext;
import org.primefaces.util.LocaleUtils;

/**
 * Creates a custom SystemEventListener for PostAddToViewEvent on UIViewRoot. This will run after all those @ResourceDependency annotations of PrimeFaces
 * components have been processed. This is thus an ideal moment to add the PrimeFaces.settings script as a component resource, as intended by PrimeFaces.
 * <p>
 * Register it as below in faces-config.xml:
 * </p>
 * 
 * <pre>
 *     &lt;application&gt;
 *        &lt;system-event-listener&gt;
 *            &lt;system-event-listener-class&gt;com.example.PrimeFacesScriptProcessor&lt;/system-event-listener-class&gt;
 *            &lt;system-event-class&gt;javax.faces.event.PostAddToViewEvent&lt;/system-event-class&gt;
 *            &lt;source-class&gt;javax.faces.component.UIViewRoot&lt;/source-class&gt;
 *        &lt;/system-event-listener&gt;
 *     &lt;/application&gt;
 * </pre>
 *
 * @see <a href="https://github.com/omnifaces/omnifaces/wiki/Combine-hardcoded-PrimeFaces-resources-using-CombinedResourceHandler">OmniFaces</a>
 * @since 10.0.0
 */
public class PrimeFacesScriptProcessor implements SystemEventListener {

    @Override
    public boolean isListenerForSource(final Object source) {
        return source instanceof UIViewRoot;
    }

    @Override
    public void processEvent(final SystemEvent event) throws AbortProcessingException {
        final FacesContext context = event.getFacesContext();
        final StringBuilder script = new StringBuilder(4000);

        encodeSettingScripts(context, script);
        encodeInitScripts(context, script);

        addJS(context, script.toString());
    }

    protected void encodeSettingScripts(final FacesContext context, final StringBuilder writer) {
        final PrimeRequestContext requestContext = PrimeRequestContext.getCurrentInstance(context);
        final PrimeApplicationContext applicationContext = requestContext.getApplicationContext();
        final PrimeConfiguration configuration = applicationContext.getConfig();

        final ProjectStage projectStage = context.getApplication().getProjectStage();

        writer.append("if(window.PrimeFaces){");

        writer.append("PrimeFaces.settings.locale='").append(LocaleUtils.getCurrentLocale(context)).append("';");
        writer.append("PrimeFaces.settings.viewId='").append(context.getViewRoot().getViewId()).append("';");
        writer.append("PrimeFaces.settings.contextPath='").append(context.getExternalContext().getRequestContextPath())
                    .append("';");

        writer.append("PrimeFaces.settings.cookiesSecure=")
                    .append(requestContext.isSecure() && configuration.isCookiesSecure()).append(";");
        if (applicationContext.getConfig().getCookiesSameSite() != null) {
            writer.append("PrimeFaces.settings.cookiesSameSite='").append(configuration.getCookiesSameSite())
                        .append("';");
        }

        if (configuration.isClientSideValidationEnabled()) {
            writer.append("PrimeFaces.settings.validateEmptyFields=").append(configuration.isValidateEmptyFields())
                        .append(";");
            writer.append("PrimeFaces.settings.considerEmptyStringNull=")
                        .append(configuration.isInterpretEmptyStringAsNull()).append(";");
        }

        if (configuration.isLegacyWidgetNamespace()) {
            writer.append("PrimeFaces.settings.legacyWidgetNamespace=true;");
        }

        if (configuration.isEarlyPostParamEvaluation()) {
            writer.append("PrimeFaces.settings.earlyPostParamEvaluation=true;");
        }

        if (configuration.isPartialSubmitEnabled()) {
            writer.append("PrimeFaces.settings.partialSubmit=true;");
        }

        if (projectStage != ProjectStage.Production) {
            writer.append("PrimeFaces.settings.projectStage='").append(projectStage.toString()).append("';");
        }

        if (applicationContext.getEnvironment().isAtLeastJsf22() &&
                    context.getExternalContext().getClientWindow() != null) {

            final ClientWindow clientWindow = context.getExternalContext().getClientWindow();
            if (clientWindow instanceof PrimeClientWindow) {

                boolean initialRedirect = false;

                final Object cookie = PrimeClientWindowUtils.getInitialRedirectCookie(context, clientWindow.getId());
                if (cookie instanceof Cookie) {
                    final Cookie servletCookie = (Cookie) cookie;
                    initialRedirect = true;

                    // expire/remove cookie
                    servletCookie.setMaxAge(0);
                    ((HttpServletResponse) context.getExternalContext().getResponse()).addCookie(servletCookie);
                }
                writer.append(
                            String.format("PrimeFaces.clientwindow.init('%s', %s);",
                                        PrimeClientWindowUtils.secureWindowId(clientWindow.getId()),
                                        initialRedirect));
            }
        }

        writer.append("}");
    }

    protected void encodeInitScripts(final FacesContext context, final StringBuilder writer) {
        final PrimeRequestContext requestContext = PrimeRequestContext.getCurrentInstance(context);
        final List<String> scripts = requestContext.getInitScriptsToExecute();

        if (!scripts.isEmpty()) {
            final boolean moveScriptsToBottom = requestContext.getApplicationContext().getConfig()
                        .isMoveScriptsToBottom();

            if (!moveScriptsToBottom) {
                writer.append("$(function(){");
            }

            for (final String script : scripts) {
                writer.append(script);
                writer.append(';');
            }

            if (!moveScriptsToBottom) {
                writer.append("});");
            }
        }
    }

    private void addJS(final FacesContext context, final String script) {
        final UIOutput js = new UIOutput();
        js.setRendererType("javax.faces.resource.Script");
        // https://github.com/primefaces-extensions/primefaces-extensions/issues/486
        // https://github.com/primefaces-extensions/primefaces-extensions/issues/517
        // MyFaces needs ID set to prevent duplicate ID check in Dev mode, Mojarra does not
        final PrimeEnvironment environment = PrimeApplicationContext.getCurrentInstance(context).getEnvironment();
        if (!environment.isMojarra()) {
            js.setId("primfaces-script-processor");
        }
        final UIOutput content = new UIOutput();
        content.setValue(script);
        js.getChildren().add(content);
        context.getViewRoot().addComponentResource(context, js);
    }
}
