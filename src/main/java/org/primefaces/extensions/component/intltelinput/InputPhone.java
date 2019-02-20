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
package org.primefaces.extensions.component.intltelinput;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.html.HtmlInputText;

/**
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 */
@ResourceDependencies({
            @ResourceDependency(library = "primefaces-extensions", name = "intltelinput/intlTelInput.min.css"),
            @ResourceDependency(library = "primefaces-extensions", name = "intltelinput/intlTelInput.min.js")
})
public class InputPhone extends HtmlInputText {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.InputPhone";

    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";

    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.InputPhoneRenderer";
    
    public static final String STYLE_CLASS = "ui-inputfield ui-inputphone ui-widget ui-state-default ui-corner-all";

    public enum PropertyKeys {
        placeholder,
        type
    }
    
    public InputPhone() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getPlaceholder() {
        return (String) getStateHelper().eval(PropertyKeys.placeholder, null);
    }

    public void setPlaceholder(String placeholder) {
        getStateHelper().put(PropertyKeys.placeholder, placeholder);
    }

    public String getType() {
        return (String) getStateHelper().eval(PropertyKeys.type, "tel");
    }

    public void setType(String type) {
        getStateHelper().put(PropertyKeys.type, type);
    }

}
