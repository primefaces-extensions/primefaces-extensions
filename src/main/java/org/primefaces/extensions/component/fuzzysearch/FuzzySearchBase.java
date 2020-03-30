/**
 * Copyright 2011-2019 PrimeFaces Extensions
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
package org.primefaces.extensions.component.fuzzysearch;

import javax.faces.component.behavior.ClientBehaviorHolder;
import org.primefaces.component.api.PrimeClientBehaviorHolder;
import org.primefaces.component.api.UIData;
import org.primefaces.component.api.Widget;

public abstract class FuzzySearchBase extends UIData implements ClientBehaviorHolder, PrimeClientBehaviorHolder, Widget {

    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";

    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.FuzzySearchRenderer";

    protected enum PropertyKeys {
        // @formatter:off
        widgetVar,
        style,
        styleClass,
        resultStyle,
        resultStyleClass,
        onSelect;
        // @formatter:on
    }

    public FuzzySearchBase() {
        super.setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getWidgetVar() {
        return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void setWidgetVar(String widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, widgetVar);
    }

    public String getStyle() {
        return (String) getStateHelper().eval(PropertyKeys.style, null);
    }

    public void setStyle(String style) {
        getStateHelper().put(PropertyKeys.style, style);
    }

    public String getStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.styleClass, null);
    }

    public void setStyleClass(String styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, styleClass);
    }

    public String getResultStyle() {
        return (String) getStateHelper().eval(PropertyKeys.resultStyle, null);
    }

    public void setResultStyle(String resultStyle) {
        getStateHelper().put(PropertyKeys.resultStyle, resultStyle);
    }

    public String getResultStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.resultStyleClass, null);
    }

    public void setResultStyleClass(String resultStyleClass) {
        getStateHelper().put(PropertyKeys.resultStyleClass, resultStyleClass);
    }

    public String getOnSelect() {
        return (String) getStateHelper().eval(PropertyKeys.onSelect, null);
    }

    public void setOnSelect(String onSelect) {
        getStateHelper().put(PropertyKeys.onSelect, onSelect);
    }

}
