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
package org.primefaces.extensions.component.imagezoom;

import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponentBase;

import org.primefaces.component.api.Widget;

/**
 * Component class for the <code>ImageZoom</code> component.
 *
 * @author Melloware
 * @since 11.0.3
 */
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js")
@ResourceDependency(library = "primefaces-extensions", name = "imagezoom/imagezoom.js")
@ResourceDependency(library = "primefaces-extensions", name = "imagezoom/imagezoom.css")
public class ImageZoom extends UIComponentBase implements Widget {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.ImageZoom";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.ImageZoomRenderer";

    @SuppressWarnings("java:S115")
    protected enum PropertyKeys {

        // @formatter:off
        widgetVar,
        margin,
        background,
        scrollOffset,
        container,
        template,
        forValue("for");
        // @formatter:on

        private final String toString;

        PropertyKeys(final String toString) {
            this.toString = toString;
        }

        PropertyKeys() {
            toString = null;
        }

        @Override
        public String toString() {
            return ((toString != null) ? toString : super.toString());
        }
    }

    /**
     * Default constructor
     */
    public ImageZoom() {
        setRendererType(DEFAULT_RENDERER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getWidgetVar() {
        return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void setWidgetVar(final String _widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, _widgetVar);
    }

    public String getFor() {
        return (String) getStateHelper().eval(PropertyKeys.forValue, null);
    }

    public void setFor(final String _for) {
        getStateHelper().put(PropertyKeys.forValue, _for);
    }

    public Integer getMargin() {
        return (Integer) getStateHelper().eval(PropertyKeys.margin, 0);
    }

    public void setMargin(final Integer _margin) {
        getStateHelper().put(PropertyKeys.margin, _margin);
    }

    public Integer getScrollOffset() {
        return (Integer) getStateHelper().eval(PropertyKeys.scrollOffset, 40);
    }

    public void setScrollOffset(final Integer _scrollOffset) {
        getStateHelper().put(PropertyKeys.scrollOffset, _scrollOffset);
    }

    public String getBackground() {
        return (String) getStateHelper().eval(PropertyKeys.background, "#fff");
    }

    public void setBackground(final String _background) {
        getStateHelper().put(PropertyKeys.background, _background);
    }

    public String getContainer() {
        return (String) getStateHelper().eval(PropertyKeys.container, null);
    }

    public void setContainer(final String _container) {
        getStateHelper().put(PropertyKeys.container, _container);
    }

    public String getTemplate() {
        return (String) getStateHelper().eval(PropertyKeys.template, null);
    }

    public void setTemplate(final String _template) {
        getStateHelper().put(PropertyKeys.template, _template);
    }
}
