/*
 * Copyright (c) 2011-2022 PrimeFaces Extensions
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
package org.primefaces.extensions.component.inputphone;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import javax.faces.application.ResourceDependency;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;

import org.primefaces.component.api.AbstractPrimeHtmlInputText;
import org.primefaces.component.api.InputHolder;
import org.primefaces.component.api.MixedClientBehaviorHolder;
import org.primefaces.component.api.Widget;
import org.primefaces.event.SelectEvent;
import org.primefaces.extensions.model.inputphone.Country;
import org.primefaces.util.Constants;
import org.primefaces.util.LangUtils;

/**
 * <code>InputPhone</code> component.
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 * @since 7.0
 */
@ResourceDependency(library = "primefaces", name = "components.css")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = "primefaces-extensions", name = "inputphone/inputphone.css")
@ResourceDependency(library = "primefaces-extensions", name = "inputphone/inputphone.js")
public class InputPhone extends AbstractPrimeHtmlInputText implements Widget, InputHolder, MixedClientBehaviorHolder {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.InputPhone";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.InputPhoneRenderer";

    public static final String STYLE_CLASS = "ui-inputphone ui-widget";
    public static final String EVENT_COUNTRY_SELECT = "countrySelect";
    public static final String COUNTRY_AUTO = "auto";

    private static final Collection<String> EVENT_NAMES = LangUtils
                .unmodifiableList("blur", "change", "valueChange", "select", "click", "dblclick", "focus", "keydown", "keypress", "keyup", "mousedown",
                            "mousemove", "mouseout", "mouseover", "mouseup", "wheel", "cut", "copy", "paste", "contextmenu", "input", "invalid", "reset",
                            "search", "drag", "dragend", "dragenter", "dragleave", "dragover", "dragstart", "drop", "scroll", EVENT_COUNTRY_SELECT);

    private static final Collection<String> UNOBSTRUSIVE_EVENT_NAMES = LangUtils.unmodifiableList(EVENT_COUNTRY_SELECT);

    // @formatter:off
    @SuppressWarnings("java:S115")
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
        inputStyleClass,
        geoIpLookup,
        localizedCountries
    }

    @SuppressWarnings("java:S115")
    public enum AutoPlaceholder {
        polite,
        aggressive,
        off
    }

    @SuppressWarnings("java:S115")
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
    // @formatter:on

    public InputPhone() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public String getDefaultEventName() {
        return EVENT_COUNTRY_SELECT;
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
    public void setLabelledBy(final String labelledBy) {
        getStateHelper().put("labelledby", labelledBy);
    }

    public String getPlaceholder() {
        return (String) getStateHelper().eval(PropertyKeys.placeholder, null);
    }

    public void setPlaceholder(final String placeholder) {
        getStateHelper().put(PropertyKeys.placeholder, placeholder);
    }

    public String getWidgetVar() {
        return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void setWidgetVar(final String widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, widgetVar);
    }

    public String getType() {
        return (String) getStateHelper().eval(PropertyKeys.type, "tel");
    }

    public void setType(final String type) {
        getStateHelper().put(PropertyKeys.type, type);
    }

    public boolean isAllowDropdown() {
        return (Boolean) getStateHelper().eval(PropertyKeys.allowDropdown, true);
    }

    public void setAllowDropdown(final boolean allowDropdown) {
        getStateHelper().put(PropertyKeys.allowDropdown, allowDropdown);
    }

    public boolean isAutoHideDialCode() {
        return (Boolean) getStateHelper().eval(PropertyKeys.autoHideDialCode, true);
    }

    public void setAutoHideDialCode(final boolean autoHideDialCode) {
        getStateHelper().put(PropertyKeys.autoHideDialCode, autoHideDialCode);
    }

    public String getAutoPlaceholder() {
        return (String) getStateHelper().eval(PropertyKeys.autoPlaceholder, AutoPlaceholder.polite.name());
    }

    public AutoPlaceholder getAutoPlaceholderEnum() {
        return AutoPlaceholder.valueOf(getAutoPlaceholder());
    }

    public void setAutoPlaceholder(final String autoPlaceholder) {
        getStateHelper().put(PropertyKeys.autoPlaceholder, autoPlaceholder);
    }

    public Object getExcludeCountries() {
        return getStateHelper().eval(PropertyKeys.excludeCountries, Collections.emptyList());
    }

    public void setExcludeCountries(final Object excludeCountries) {
        getStateHelper().put(PropertyKeys.excludeCountries, excludeCountries);
    }

    public boolean isFormatOnDisplay() {
        return (Boolean) getStateHelper().eval(PropertyKeys.formatOnDisplay, true);
    }

    public void setFormatOnDisplay(final boolean formatOnDisplay) {
        getStateHelper().put(PropertyKeys.formatOnDisplay, formatOnDisplay);
    }

    public String getInitialCountry() {
        return (String) getStateHelper().eval(PropertyKeys.initialCountry, "us");
    }

    public void setInitialCountry(final String initialCountry) {
        getStateHelper().put(PropertyKeys.initialCountry, initialCountry);
    }

    public boolean isNationalMode() {
        return (Boolean) getStateHelper().eval(PropertyKeys.nationalMode, true);
    }

    public void setNationalMode(final boolean nationalMode) {
        getStateHelper().put(PropertyKeys.nationalMode, nationalMode);
    }

    public Object getOnlyCountries() {
        return getStateHelper().eval(PropertyKeys.onlyCountries, Collections.emptyList());
    }

    public void setOnlyCountries(final Object onlyCountries) {
        getStateHelper().put(PropertyKeys.onlyCountries, onlyCountries);
    }

    public String getPlaceholderNumberType() {
        return (String) getStateHelper().eval(PropertyKeys.placeholderNumberType, PlaceholderNumberType.mobile.name());
    }

    public PlaceholderNumberType getPlaceholderNumberTypeEnum() {
        return PlaceholderNumberType.valueOf(getPlaceholderNumberType());
    }

    public void setPlaceholderNumberType(final String placeholderNumberType) {
        getStateHelper().put(PropertyKeys.placeholderNumberType, placeholderNumberType);
    }

    public Object getPreferredCountries() {
        return getStateHelper().eval(PropertyKeys.preferredCountries, Collections.emptyList());
    }

    public void setPreferredCountries(final Object preferredCountries) {
        getStateHelper().put(PropertyKeys.preferredCountries, preferredCountries);
    }

    public boolean isSeparateDialCode() {
        return (Boolean) getStateHelper().eval(PropertyKeys.separateDialCode, false);
    }

    public void setSeparateDialCode(final boolean separateDialCode) {
        getStateHelper().put(PropertyKeys.separateDialCode, separateDialCode);
    }

    public String getInputStyle() {
        return (String) getStateHelper().eval(PropertyKeys.inputStyle, null);
    }

    public void setInputStyle(final String inputStyle) {
        getStateHelper().put(PropertyKeys.inputStyle, inputStyle);
    }

    public String getInputStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.inputStyleClass, null);
    }

    public void setInputStyleClass(final String inputStyleClass) {
        getStateHelper().put(PropertyKeys.inputStyleClass, inputStyleClass);
    }

    public String getGeoIpLookup() {
        return (String) getStateHelper().eval(PropertyKeys.geoIpLookup, null);
    }

    public void setGeoIpLookup(final String geoIpLookup) {
        getStateHelper().put(PropertyKeys.geoIpLookup, geoIpLookup);
    }

    public Object getLocalizedCountries() {
        return getStateHelper().eval(PropertyKeys.localizedCountries, null);
    }

    public void setLocalizedCountries(final Object localizedCountries) {
        getStateHelper().put(PropertyKeys.localizedCountries, localizedCountries);
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
    public void queueEvent(final FacesEvent event) {
        final FacesContext context = getFacesContext();
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        final String eventName = params.get(Constants.RequestParams.PARTIAL_BEHAVIOR_EVENT_PARAM);

        if (eventName != null && event instanceof AjaxBehaviorEvent) {
            final AjaxBehaviorEvent ajaxBehaviorEvent = (AjaxBehaviorEvent) event;

            if (EVENT_COUNTRY_SELECT.equals(eventName)) {
                final Country selectedCountry = getCountry(getClientId(context), params);
                final SelectEvent<Country> selectEvent = new SelectEvent<>(this, ajaxBehaviorEvent.getBehavior(), selectedCountry);
                selectEvent.setPhaseId(ajaxBehaviorEvent.getPhaseId());
                super.queueEvent(selectEvent);
            }
            else {
                // e.g. blur, focus, change
                super.queueEvent(event);
            }
        }
        else {
            // e.g. valueChange
            super.queueEvent(event);
        }
    }

    protected static Country getCountry(final String clientId, final Map<String, String> params) {
        return new Country(params.get(clientId + "_name"),
                    params.get(clientId + "_iso2"),
                    params.get(clientId + "_dialCode"));
    }

}
