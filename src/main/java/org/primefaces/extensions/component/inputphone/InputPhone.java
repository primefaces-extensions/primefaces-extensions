/**
 * Copyright 2011-2018 PrimeFaces Extensions
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
package org.primefaces.extensions.component.inputphone;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;
import org.primefaces.component.api.InputHolder;
import org.primefaces.component.api.MixedClientBehaviorHolder;
import org.primefaces.component.api.Widget;
import org.primefaces.event.SelectEvent;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.Constants;
import org.primefaces.util.LangUtils;

/**
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 */
// TODO resources need to be minified (pom?), however utils.js must be excluded and needs to be loaded optionally
@ResourceDependencies({
            @ResourceDependency(library = "primefaces", name = "components.css"),
            @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
            @ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js"),
            @ResourceDependency(library = "primefaces", name = "core.js"),
            @ResourceDependency(library = "primefaces-extensions", name = "inputphone/intlTelInput.css"),
            @ResourceDependency(library = "primefaces-extensions", name = "inputphone/intlTelInput.js"),
            @ResourceDependency(library = "primefaces-extensions", name = "inputphone/inputPhoneWidget.js")
})
public class InputPhone extends HtmlInputText implements Widget, InputHolder, MixedClientBehaviorHolder {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.InputPhone";

    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";

    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.InputPhoneRenderer";

    public static final String STYLE_CLASS = "ui-inputphone ui-widget";

    private static final Collection<String> EVENT_NAMES = LangUtils.unmodifiableList("blur", "change", "valueChange", "click", "dblclick",
            "focus", "keydown", "keypress", "keyup", "mousedown", "mousemove", "mouseout", "mouseover", "mouseup", "select", "countrySelect");

    private static final Collection<String> UNOBSTRUSIVE_EVENT_NAMES = LangUtils.unmodifiableList("countrySelect");

    public enum PropertyKeys {
        placeholder,
        widgetVar,
        type,
        allowDropdown,
        autoHideDialCode,
        autoPlaceholder,
        excludeCountries,
        formatOnDisplay,
        initialCountry,
        nationalMode,
        onlyCountries,
        placeholderNumberType,
        preferredCountries,
        separateDialCode,
        inputStyle,
        inputStyleClass
    }

    public enum AutoPlaceholder {
        polite,
        aggressive,
        off
    }

    public enum PlaceholderNumberType {
        fixed_line,
        mobile,
        toll_free,
        shared_cost,
        voip,
        personal_number,
        pager,
        uan,
        voicemail,
        unknown
    }

    public InputPhone() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public String getInputClientId() {
        return getClientId() + "_input";
    }

    @Override
    public String getValidatableInputClientId() {
        return getClientId() + "_input";
    }

    @Override
    public String getLabelledBy() {
        return (String) getStateHelper().get("labelledby");
    }

    @Override
    public void setLabelledBy(String labelledBy) {
        getStateHelper().put("labelledby", labelledBy);
    }

    public String getPlaceholder() {
        return (String) getStateHelper().eval(PropertyKeys.placeholder, null);
    }

    public void setPlaceholder(String placeholder) {
        getStateHelper().put(PropertyKeys.placeholder, placeholder);
    }

    public String getWidgetVar() {
        return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void setWidgetVar(String widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, widgetVar);
    }

    public String getType() {
        return (String) getStateHelper().eval(PropertyKeys.type, "tel");
    }

    public void setType(String type) {
        getStateHelper().put(PropertyKeys.type, type);
    }

    public boolean isAllowDropdown() {
        return (Boolean) getStateHelper().eval(PropertyKeys.allowDropdown, true);
    }

    public void setAllowDropdown(boolean allowDropdown) {
        getStateHelper().put(PropertyKeys.allowDropdown, allowDropdown);
    }

    public boolean isAutoHideDialCode() {
        return (Boolean) getStateHelper().eval(PropertyKeys.autoHideDialCode, true);
    }

    public void setAutoHideDialCode(boolean autoHideDialCode) {
        getStateHelper().put(PropertyKeys.autoHideDialCode, autoHideDialCode);
    }

    public String getAutoPlaceholder() {
        return (String) getStateHelper().eval(PropertyKeys.autoPlaceholder, AutoPlaceholder.polite.name());
    }

    public AutoPlaceholder getAutoPlaceholderEnum() {
        return AutoPlaceholder.valueOf(getAutoPlaceholder());
    }

    public void setAutoPlaceholder(String autoPlaceholder) {
        getStateHelper().put(PropertyKeys.autoPlaceholder, autoPlaceholder);
    }

    public Object getExcludeCountries() {
        return getStateHelper().eval(PropertyKeys.excludeCountries, Collections.emptyList());
    }

    public void setExcludeCountries(Object excludeCountries) {
        getStateHelper().put(PropertyKeys.excludeCountries, excludeCountries);
    }

    public boolean isFormatOnDisplay() {
        return (Boolean) getStateHelper().eval(PropertyKeys.formatOnDisplay, true);
    }

    public void setFormatOnDisplay(boolean formatOnDisplay) {
        getStateHelper().put(PropertyKeys.formatOnDisplay, formatOnDisplay);
    }

    public String getInitialCountry() {
        return (String) getStateHelper().eval(PropertyKeys.initialCountry, null);
    }

    public void setInitialCountry(String initialCountry) {
        getStateHelper().put(PropertyKeys.initialCountry, initialCountry);
    }

    public boolean isNationalMode() {
        return (Boolean) getStateHelper().eval(PropertyKeys.nationalMode, true);
    }

    public void setNationalMode(boolean nationalMode) {
        getStateHelper().put(PropertyKeys.nationalMode, nationalMode);
    }

    public Object getOnlyCountries() {
        return getStateHelper().eval(PropertyKeys.onlyCountries, Collections.emptyList());
    }

    public void setOnlyCountries(Object onlyCountries) {
        getStateHelper().put(PropertyKeys.onlyCountries, onlyCountries);
    }

    public String getPlaceholderNumberType() {
        return (String) getStateHelper().eval(PropertyKeys.placeholderNumberType, PlaceholderNumberType.mobile.name());
    }

    public PlaceholderNumberType getPlaceholderNumberTypeEnum() {
        return PlaceholderNumberType.valueOf(getPlaceholderNumberType());
    }

    public void setPlaceholderNumberType(String placeholderNumberType) {
        getStateHelper().put(PropertyKeys.placeholderNumberType, placeholderNumberType);
    }

    public Object getPreferredCountries() {
        return getStateHelper().eval(PropertyKeys.preferredCountries, Collections.emptyList());
    }

    public void setPreferredCountries(Object preferredCountries) {
        getStateHelper().put(PropertyKeys.preferredCountries, preferredCountries);
    }

    public boolean isSeparateDialCode() {
        return (Boolean) getStateHelper().eval(PropertyKeys.separateDialCode, false);
    }

    public void setSeparateDialCode(boolean separateDialCode) {
        getStateHelper().put(PropertyKeys.separateDialCode, separateDialCode);
    }

    public String getInputStyle() {
        return (String) getStateHelper().eval(PropertyKeys.inputStyle, null);
    }

    public void setInputStyle(String inputStyle) {
        getStateHelper().put(PropertyKeys.inputStyle, inputStyle);
    }

    public String getInputStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.inputStyleClass, null);
    }

    public void setInputStyleClass(String inputStyleClass) {
        getStateHelper().put(PropertyKeys.inputStyleClass, inputStyleClass);
    }

    @Override
    public String resolveWidgetVar() {
        return ComponentUtils.resolveWidgetVar(getFacesContext(), this);
    }

    public boolean isUtilsScriptRequired() {
        return !AutoPlaceholder.off.name().equals(getAutoPlaceholder()) || isFormatOnDisplay();
    }

    @Override
    public Collection<String> getEventNames() {
        return EVENT_NAMES;
    }

    @Override
    public Collection<String> getUnobstrusiveEventNames() {
        return UNOBSTRUSIVE_EVENT_NAMES;
    }

    @Override
    public void queueEvent(FacesEvent event) {
        FacesContext context = getFacesContext();
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        String eventName = params.get(Constants.RequestParams.PARTIAL_BEHAVIOR_EVENT_PARAM);

        if (eventName != null && event instanceof AjaxBehaviorEvent) {
            AjaxBehaviorEvent ajaxBehaviorEvent = (AjaxBehaviorEvent) event;

            if (eventName.equals("countrySelect")) {
                Object selectedCountry = getCountry(getClientId(context), params);
                SelectEvent selectEvent = new SelectEvent(this, ajaxBehaviorEvent.getBehavior(), selectedCountry);
                selectEvent.setPhaseId(ajaxBehaviorEvent.getPhaseId());
                super.queueEvent(selectEvent);
            }
            else {
                //e.g. blur, focus, change
                super.queueEvent(event);
            }
        }
        else {
            //e.g. valueChange
            super.queueEvent(event);
        }
    }

    protected Country getCountry(String clientId, Map<String, String> params) {
        return new Country(params.get(clientId + "_name"),
                params.get(clientId + "_iso2"),
                params.get(clientId + "_dialCode"));
    }

}
