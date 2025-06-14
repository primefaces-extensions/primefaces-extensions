/*
 * Copyright (c) 2011-2025 PrimeFaces Extensions
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

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.faces.FacesException;
import jakarta.faces.application.ProjectStage;
import jakarta.faces.component.UIViewRoot;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AbortProcessingException;
import jakarta.faces.event.SystemEvent;
import jakarta.faces.event.SystemEventListener;
import jakarta.faces.lifecycle.ClientWindow;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import org.primefaces.clientwindow.PrimeClientWindow;
import org.primefaces.clientwindow.PrimeClientWindowUtils;
import org.primefaces.config.PrimeConfiguration;
import org.primefaces.context.PrimeApplicationContext;
import org.primefaces.context.PrimeRequestContext;
import org.primefaces.extensions.util.ResourceExtUtils;
import org.primefaces.util.Constants;
import org.primefaces.util.LocaleUtils;

/**
 * SystemEventListener implementation that processes PrimeFaces scripts and resources. This will run after all those @ResourceDependency annotations of
 * PrimeFaces components have been processed. This is thus an ideal moment to add the PrimeFaces.settings script as a component resource, as intended by
 * PrimeFaces. This listener handles:
 * <ul>
 * <li>Adding validation resources (moment.js)</li>
 * <li>Adding locale resources for client-side localization (locale-xx.js)</li>
 * <li>Encoding PrimeFaces settings (locale, viewId, contextPath, etc)</li>
 * <li>Encoding initialization scripts</li>
 * </ul>
 * The listener only processes non-AJAX requests and avoids duplicate processing during view restore.
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

    private static final Logger LOGGER = Logger.getLogger(PrimeFacesScriptProcessor.class.getName());

    @Override
    public boolean isListenerForSource(final Object source) {
        return source instanceof UIViewRoot;
    }

    @Override
    public void processEvent(final SystemEvent event) throws AbortProcessingException {
        final FacesContext context = event.getFacesContext();
        /*
         * Check if this is an AJAX request by checking postback or partial view context. For AJAX requests, we skip loading core resources since they should
         * already be loaded.
         */
        if (context.getPartialViewContext().isAjaxRequest()) {
            return;
        }

        // Get the current stack trace
        // https://github.com/primefaces-extensions/primefaces-extensions/issues/517
        boolean shouldDiscard = Arrays.stream(Thread.currentThread().getStackTrace())
                    .anyMatch(element -> "org.apache.myfaces.lifecycle.RestoreViewExecutor".equals(element.getClassName()));
        if (shouldDiscard) {
            // Discard this as it is also processed in RenderResponseExecutor
            return; // Exit
        }

        final PrimeRequestContext requestContext = PrimeRequestContext.getCurrentInstance(context);
        final PrimeApplicationContext applicationContext = requestContext.getApplicationContext();
        final PrimeConfiguration configuration = applicationContext.getConfig();

        // normal CSV is a required dependency for some special components like fileupload
        encodeValidationResources(context, configuration);

        // encode client side locale
        encodeLocaleResources(context, configuration);

        // encode PrimeFaces settings
        final StringBuilder script = new StringBuilder(4000);
        encodeSettingScripts(context, script);

        // encode initialization scripts
        encodeInitScripts(context, script);

        // add the scripts to the head
        ResourceExtUtils.addScriptToHead(context, script.toString());
    }

    /**
     * Adds validation related JavaScript resources to the view. This includes the core validation.js and optionally moment.js and validation.bv.js if
     * client-side validation and bean validation are enabled.
     *
     * @param context The FacesContext
     * @param configuration The PrimeFaces configuration
     */
    protected void encodeValidationResources(final FacesContext context, final PrimeConfiguration configuration) {
        if (configuration.isClientSideValidationEnabled()) {
            // moment is needed for Date validation
            ResourceExtUtils.addJavascriptResource(context, Constants.LIBRARY, "moment/moment.js");
        }
    }

    /**
     * Adds locale JavaScript resources to the view if client-side localization is enabled. The locale file is determined by the current locale's language.
     *
     * @param context The FacesContext
     * @param configuration The PrimeFaces configuration
     */
    protected void encodeLocaleResources(final FacesContext context, final PrimeConfiguration configuration) {
        if (!configuration.isClientSideLocalizationEnabled()) {
            return;
        }

        try {
            final Locale locale = LocaleUtils.getCurrentLocale(context);
            ResourceExtUtils.addJavascriptResource(context, Constants.LIBRARY,
                        "locales/locale-" + locale.getLanguage() + ".js");
        }
        catch (FacesException e) {
            if (context.isProjectStage(ProjectStage.Development)) {
                LOGGER.log(Level.WARNING, "Failed to load client side locale.js. {0}", e.getMessage());
            }
        }
    }

    /**
     * Encodes PrimeFaces settings as JavaScript that will be added to the view. This includes settings for locale, viewId, contextPath, cookies, validation,
     * widget namespace, project stage and client window.
     *
     * @param context The FacesContext
     * @param writer StringBuilder to append the settings JavaScript to
     */
    protected void encodeSettingScripts(final FacesContext context, final StringBuilder writer) {
        final PrimeRequestContext requestContext = PrimeRequestContext.getCurrentInstance(context);
        final PrimeApplicationContext applicationContext = requestContext.getApplicationContext();
        final PrimeConfiguration configuration = applicationContext.getConfig();
        final ProjectStage projectStage = context.getApplication().getProjectStage();
        final ExternalContext externalContext = context.getExternalContext();

        writer.append("if(window.PrimeFaces){");

        // Build settings object
        writer.append("PrimeFaces.settings={")
                    .append("locale:'").append(LocaleUtils.getCurrentLocale(context)).append("',")
                    .append("viewId:'").append(context.getViewRoot().getViewId()).append("',")
                    .append("contextPath:'").append(externalContext.getRequestContextPath()).append("',")
                    .append("cookiesSecure:").append(requestContext.isSecure() && configuration.isCookiesSecure());

        String cookiesSameSite = configuration.getCookiesSameSite();
        if (cookiesSameSite != null) {
            writer.append(",cookiesSameSite:'").append(cookiesSameSite).append("'");
        }

        // Client side validation settings
        if (configuration.isClientSideValidationEnabled()) {
            writer.append(",validateEmptyFields:").append(configuration.isValidateEmptyFields())
                        .append(",considerEmptyStringNull:").append(configuration.isInterpretEmptyStringAsNull());
        }

        // Feature flags
        if (configuration.isEarlyPostParamEvaluation()) {
            writer.append(",earlyPostParamEvaluation:true");
        }
        if (configuration.isPartialSubmitEnabled()) {
            writer.append(",partialSubmit:true");
        }

        // Non-production stage
        if (projectStage != ProjectStage.Production) {
            writer.append(",projectStage:'").append(projectStage.toString()).append("'");
        }

        writer.append("};");

        // Client window handling
        ClientWindow clientWindow = externalContext.getClientWindow();
        if (clientWindow instanceof PrimeClientWindow) {
            boolean initialRedirect = false;
            Object cookie = PrimeClientWindowUtils.getInitialRedirectCookie(context, clientWindow.getId());

            if (cookie instanceof Cookie) {
                Cookie servletCookie = (Cookie) cookie;
                initialRedirect = true;
                servletCookie.setMaxAge(0);
                ((HttpServletResponse) externalContext.getResponse()).addCookie(servletCookie);
            }

            writer.append("PrimeFaces.clientwindow.init('")
                        .append(PrimeClientWindowUtils.secureWindowId(clientWindow.getId())).append("',")
                        .append(initialRedirect).append(");");
        }

        writer.append("}");
    }

    /**
     * Encodes initialization scripts that will be added to the view. The scripts can be moved to the bottom of the page based on configuration. When not moved
     * to bottom, scripts are wrapped in a function that executes on DOM ready.
     *
     * @param context The FacesContext
     * @param writer StringBuilder to append the initialization scripts to
     */
    protected void encodeInitScripts(FacesContext context, StringBuilder writer) {
        PrimeRequestContext requestContext = PrimeRequestContext.getCurrentInstance(context);
        List<String> scripts = requestContext.getInitScriptsToExecute();

        if (scripts.isEmpty()) {
            return;
        }

        boolean moveScriptsToBottom = requestContext.getApplicationContext().getConfig()
                    .isMoveScriptsToBottom();

        if (!moveScriptsToBottom) {
            writer.append("(function(){const pfInit=()=>{")
                        .append(String.join(";", scripts))
                        .append("};if(window.$){$(pfInit)}")
                        .append("else if(document.readyState==='complete'){pfInit()}")
                        .append("else{document.addEventListener('DOMContentLoaded',pfInit)}")
                        .append("})();");
        }
        else {
            writer.append(String.join(";", scripts)).append(';');
        }
    }

}