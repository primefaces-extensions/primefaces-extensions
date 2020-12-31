/*
 * Copyright 2011-2021 PrimeFaces Extensions
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
package org.primefaces.extensions.component.legend;

import java.util.Map;

import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponentBase;

import org.primefaces.component.api.Widget;

/**
 * <code>Legend</code> component.
 *
 * @author Melloware mellowaredev@gmail.com
 * @since 7.1
 */
@ResourceDependency(library = "primefaces", name = "components.css")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = "primefaces-extensions", name = "legend/legend.css")
@ResourceDependency(library = "primefaces-extensions", name = "legend/legend.js")
public class Legend extends UIComponentBase implements Widget {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Legend";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String STYLE_CLASS_VERTICAL = "ui-legend-vertical ";
    public static final String STYLE_CLASS_HORIZONTAL = "ui-legend-horizontal ";
    public static final String SCALE_STYLE = "ui-legend-scale";
    public static final String LABELS_STYLE = "ui-legend-labels";
    public static final String TITLE_STYLE = "ui-legend-title";
    public static final String FOOTER_STYLE = "ui-legend-footer";

    private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.LegendRenderer";

    @SuppressWarnings("java:S115")
    protected enum PropertyKeys {

        // @formatter:off
        widgetVar,
        style,
        styleClass,
        title,
        footer,
        values,
        layout
        // @formatter:on
    }

    /**
     * Default constructor
     */
    public Legend() {
        setRendererType(DEFAULT_RENDERER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getLayout() {
        return (String) getStateHelper().eval(PropertyKeys.layout, "vertical");
    }

    public void setLayout(final String layout) {
        getStateHelper().put(PropertyKeys.layout, layout);
    }

    public String getTitle() {
        return (String) getStateHelper().eval(PropertyKeys.title, null);
    }

    public void setTitle(final String title) {
        getStateHelper().put(PropertyKeys.title, title);
    }

    public String getFooter() {
        return (String) getStateHelper().eval(PropertyKeys.footer, null);
    }

    public void setFooter(final String footer) {
        getStateHelper().put(PropertyKeys.footer, footer);
    }

    public String getStyle() {
        return (String) getStateHelper().eval(PropertyKeys.style, null);
    }

    public void setStyle(final String _style) {
        getStateHelper().put(PropertyKeys.style, _style);
    }

    public String getStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.styleClass, null);
    }

    public void setStyleClass(final String _styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, _styleClass);
    }

    public Map<String, String> getValues() {
        return (Map<String, String>) getStateHelper().eval(PropertyKeys.values, null);
    }

    public void setValues(final Map<String, String> map) {
        getStateHelper().put(PropertyKeys.values, map);
    }

}
