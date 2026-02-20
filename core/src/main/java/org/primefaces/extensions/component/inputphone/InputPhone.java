/*
 * Copyright (c) 2011-2026 PrimeFaces Extensions
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

import java.util.Map;

import jakarta.faces.application.ResourceDependency;
import jakarta.faces.component.FacesComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.event.FacesEvent;

import org.primefaces.cdk.api.FacesComponentInfo;
import org.primefaces.event.SelectEvent;
import org.primefaces.extensions.model.inputphone.Country;

/**
 * <code>InputPhone</code> component.
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 * @since 7.0
 */
@FacesComponent(value = InputPhone.COMPONENT_TYPE, namespace = InputPhone.COMPONENT_FAMILY)
@FacesComponentInfo(description = "InputPhone is an international telephone input with country selector.")
@ResourceDependency(library = "primefaces", name = "components.css")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = org.primefaces.extensions.util.Constants.LIBRARY, name = "inputphone/inputphone.css")
@ResourceDependency(library = org.primefaces.extensions.util.Constants.LIBRARY, name = "inputphone/inputphone.js")
public class InputPhone extends InputPhoneBaseImpl {

    public static final String STYLE_CLASS = "ui-inputphone ui-widget";
    public static final String COUNTRY_AUTO = "auto";
    public static final String INPUT_SUFFIX = "_input";

    // @formatter:off
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

    @Override
    public String getInputClientId() {
        return getClientId() + INPUT_SUFFIX;
    }

    @Override
    public String getValidatableInputClientId() {
        return getClientId() + INPUT_SUFFIX;
    }

    public AutoPlaceholder getAutoPlaceholderEnum() {
        return AutoPlaceholder.valueOf(getAutoPlaceholder());
    }

    public PlaceholderNumberType getPlaceholderNumberTypeEnum() {
        return PlaceholderNumberType.valueOf(getPlaceholderNumberType());
    }

    @Override
    public void queueEvent(final FacesEvent event) {
        if (isAjaxBehaviorEventSource(event)) {
            final FacesContext context = event.getFacesContext();
            final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
            final AjaxBehaviorEvent ajaxBehaviorEvent = (AjaxBehaviorEvent) event;

            if (isAjaxBehaviorEvent(event, ClientBehaviorEventKeys.countrySelect)) {
                final Country selectedCountry = getCountry(getClientId(context), params);
                final SelectEvent<Country> selectEvent = new SelectEvent<>(this, ajaxBehaviorEvent.getBehavior(),
                            selectedCountry);
                selectEvent.setPhaseId(ajaxBehaviorEvent.getPhaseId());
                super.queueEvent(selectEvent);
                return;
            }
            else {
                // blur/focus/change events
                super.queueEvent(event);
                return;
            }
        }
        super.queueEvent(event);
    }

    protected static Country getCountry(final String clientId, final Map<String, String> params) {
        return new Country(params.get(clientId + "_name"),
                    params.get(clientId + "_iso2"),
                    params.get(clientId + "_dialCode"));
    }
}
