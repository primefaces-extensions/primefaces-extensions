/**
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
package org.primefaces.extensions.component.badge;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponentBase;

import org.primefaces.component.api.Widget;

/**
 * <code>Badge</code> component.
 *
 * @author Melloware mellowaredev@gmail.com
 * @since 7.1
 */
@ResourceDependencies({
            @ResourceDependency(library = "primefaces", name = "components.css"),
            @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
            @ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js"),
            @ResourceDependency(library = "primefaces", name = "core.js"),
            @ResourceDependency(library = "primefaces-extensions", name = "badge/badge.css"),
            @ResourceDependency(library = "primefaces-extensions", name = "badge/badge.js")
})
public class Badge extends UIComponentBase implements Widget {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Badge";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.BadgeRenderer";

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
