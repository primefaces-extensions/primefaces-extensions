/*
 * Copyright (c) 2011-2021 PrimeFaces Extensions
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
package org.primefaces.extensions.component.badge;

import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponentBase;

import org.primefaces.component.api.Widget;

/**
 * <code>Badge</code> component.
 *
 * @author Melloware mellowaredev@gmail.com
 * @since 7.1
 */
@ResourceDependency(library = "primefaces", name = "components.css")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = "primefaces-extensions", name = "badge/badge.css")
@ResourceDependency(library = "primefaces-extensions", name = "badge/badge.js")
public class Badge extends UIComponentBase implements Widget {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Badge";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.BadgeRenderer";

    @SuppressWarnings("java:S115")
    protected enum PropertyKeys {

        // @formatter:off
        widgetVar,
        content,
        position,
        color,
        forValue("for");
        // @formatter:on

        private String toString;

        PropertyKeys(final String toString) {
            this.toString = toString;
        }

        PropertyKeys() {
        }

        @Override
        public String toString() {
            return toString != null ? toString : super.toString();
        }
    }

    /**
     * Default constructor
     */
    public Badge() {
        setRendererType(DEFAULT_RENDERER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getFor() {
        return (String) getStateHelper().eval(PropertyKeys.forValue, null);
    }

    public void setFor(final String _for) {
        getStateHelper().put(PropertyKeys.forValue, _for);
    }

    public String getContent() {
        return (String) getStateHelper().eval(PropertyKeys.content, null);
    }

    public void setContent(final String content) {
        getStateHelper().put(PropertyKeys.content, content);
    }

    public String getPosition() {
        return (String) getStateHelper().eval(PropertyKeys.position, "top-right");
    }

    public void setPosition(final String position) {
        getStateHelper().put(PropertyKeys.position, position);
    }

    public String getColor() {
        return (String) getStateHelper().eval(PropertyKeys.color, "red");
    }

    public void setColor(final String color) {
        getStateHelper().put(PropertyKeys.color, color);
    }
}
