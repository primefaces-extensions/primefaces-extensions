/*
 * Copyright (c) 2011-2024 PrimeFaces Extensions
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

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlBody;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.extensions.util.HtmlSanitizer;

/**
 * System event listener that sanitizes HTML content in components with escape="false". This helps prevent XSS vulnerabilities while still allowing some HTML
 * formatting.
 * <p>
 * To enable this listener, add the following to faces-config.xml:
 * 
 * <pre>
 * &lt;application&gt;
 *     &lt;system-event-listener&gt;
 *         &lt;system-event-listener-class&gt;org.primefaces.extensions.application.EscapeSanitizerComponentListener&lt;/system-event-listener-class&gt;
 *         &lt;system-event-class&gt;javax.faces.event.PostAddToViewEvent&lt;/system-event-class&gt;
 *         &lt;source-class&gt;javax.faces.component.html.HtmlBody&lt;/source-class&gt;
 *     &lt;/system-event-listener&gt;
 * &lt;/application&gt;
 * </pre>
 */
public class EscapeSanitizerComponentListener implements SystemEventListener {

    private static final String PASS_THROUGH_SANITIZED = "sanitized";

    /**
     * Processes the system event by traversing the component tree and sanitizing HTML content.
     *
     * @param event The system event to process
     * @throws AbortProcessingException if processing should be aborted
     */
    @Override
    public void processEvent(SystemEvent event) throws AbortProcessingException {
        FacesContext context = event.getFacesContext();
        UIComponent root = context.getViewRoot();
        checkComponents(root);
    }

    /**
     * Determines if this listener should handle events from the given source. Only processes events from HtmlBody components.
     *
     * @param source The event source object
     * @return true if the source is an HtmlBody component
     */
    @Override
    public boolean isListenerForSource(Object source) {
        return source instanceof HtmlBody;
    }

    /**
     * Recursively checks UI components for instances of {@link HtmlOutputText} and {@link OutputLabel} and sanitizes their HTML content if
     * <code>escape="false"</code> is set. Ensures components are not sanitized multiple times by checking a pass through "pt:sanitized" attribute.
     *
     * @param component The root UI component to inspect and sanitize.
     */
    private void checkComponents(UIComponent component) {
        if (component == null) {
            return; // Safety check for null input
        }

        boolean shouldSanitize = false;
        String value = null;

        // Check if the component is HtmlOutputText and meets the sanitization criteria
        if (component instanceof HtmlOutputText) {
            HtmlOutputText htmlOutputText = (HtmlOutputText) component;
            if (!htmlOutputText.isEscape() && isNotAlreadySanitized(component)) {
                value = getValueAsString(htmlOutputText);
                shouldSanitize = value != null;
            }
        }
        // Check if the component is OutputLabel and meets the sanitization criteria
        else if (component instanceof OutputLabel) {
            OutputLabel outputLabel = (OutputLabel) component;
            if (!outputLabel.isEscape() && isNotAlreadySanitized(component)) {
                value = getValueAsString(outputLabel);
                shouldSanitize = value != null;
            }
        }

        // Apply sanitization if needed
        if (shouldSanitize) {
            component.getAttributes().put("value", sanitizeHtml(value));
        }

        // Recursively check all child components
        for (UIComponent child : component.getChildren()) {
            checkComponents(child);
        }

        // Check all facets
        for (UIComponent facet : component.getFacets().values()) {
            checkComponents(facet);
        }
    }

    /**
     * Sanitizes HTML content by allowing only safe HTML elements and attributes. Allows formatting and links but does not allow blocks, styles and media
     * elements.
     *
     * @param html The HTML content to sanitize
     * @return The sanitized HTML content
     */
    private String sanitizeHtml(String html) {
        return HtmlSanitizer.sanitizeHtml(html, true, true, true, true, false, false, false);
    }

    /**
     * Safely retrieves the value of a component as a String.
     *
     * @param component The UI component from which to extract the value.
     * @return The component's value as a String, or <code>null</code> if not applicable.
     */
    private String getValueAsString(UIComponent component) {
        Object value = component.getAttributes().get("value");
        return (value instanceof String) ? (String) value : null;
    }

    /**
     * Checks if a component has not already been marked as sanitized. This is determined by checking the pass-through attribute "sanitized" which should be set
     * to "true" if the component was previously sanitized.
     *
     * @param component The UI component to check for sanitization status
     * @return true if the component has not been sanitized, false if it has been sanitized
     */
    private boolean isNotAlreadySanitized(UIComponent component) {
        return !"true".equals(component.getPassThroughAttributes().get(PASS_THROUGH_SANITIZED));
    }
}