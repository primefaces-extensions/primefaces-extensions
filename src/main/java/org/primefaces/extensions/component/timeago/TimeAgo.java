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
package org.primefaces.extensions.component.timeago;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import org.primefaces.component.api.Widget;
import org.primefaces.extensions.util.MessageFactory;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.ResourceUtils;

/**
 * <code>TimeAgo</code> component.
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 * @since 7.0.1
 */
@ResourceDependencies({
    @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
    @ResourceDependency(library = "primefaces", name = "core.js"),
    @ResourceDependency(library = "primefaces-extensions", name = "timeago/jquery.timeago.js"),
    @ResourceDependency(library = "primefaces-extensions", name = "timeago/timeago-widget.js")
})
public class TimeAgo extends UIComponentBase implements Widget {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.TimeAgo";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.TimeAgoRenderer";

    public static final String STYLE_CLASS = "ui-timeago ui-widget";

    private static final List<String> BUNDLED_LOCALES = Arrays.asList(
            "af",
            "am",
            "ar",
            "az",
            "be",
            "bg",
            "bs",
            "ca",
            "cs",
            "cy",
            "da",
            "de",
            "dv",
            "el",
            "en",
            "es",
            "et",
            "eu",
            "fa",
            "fi",
            "fr",
            "gl",
            "he",
            "hr",
            "hu",
            "hy",
            "id",
            "is",
            "it",
            "ja",
            "jv",
            "ko",
            "ky",
            "lt",
            "lv",
            "mk",
            "nl",
            "no",
            "pl",
            "pt-br",
            "pt",
            "ro",
            "rs",
            "ru",
            "rw",
            "si",
            "sk",
            "sl",
            "sq",
            "sr",
            "sv",
            "th",
            "tr",
            "uk",
            "ur",
            "uz",
            "vi",
            "zh-cn",
            "zh-tw");

    // @formatter:off
    public enum PropertyKeys {
        value,
        widgetVar,
        style,
        styleClass
    }
    // @formatter:on

    public TimeAgo() {
        setRendererType(DEFAULT_RENDERER);

        final String i18nAbbr = getBundledLocaleAbbr(MessageFactory.getLocale());
        if (i18nAbbr != null) {
            ResourceUtils.addComponentResource(FacesContext.getCurrentInstance(),
                                               "timeago/i18n/jquery.timeago." + i18nAbbr + ".js",
                                               "primefaces-extensions",
                                               "body");
        }
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public Object getValue() {
        return getStateHelper().eval(PropertyKeys.value, null);
    }

    public void setValue(String value) {
        getStateHelper().put(PropertyKeys.value, value);
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

    @Override
    public String resolveWidgetVar() {
        return ComponentUtils.resolveWidgetVar(getFacesContext(), this);
    }

    public final String getBundledLocaleAbbr(final Locale locale) {
        final String abbr = locale.getLanguage() + "-" + locale.getCountry().toLowerCase();
        if (BUNDLED_LOCALES.contains(abbr)) {
            return abbr;
        }
        if (BUNDLED_LOCALES.contains(locale.getLanguage())) {
            return locale.getLanguage();
        }
        return null;
    }

}
