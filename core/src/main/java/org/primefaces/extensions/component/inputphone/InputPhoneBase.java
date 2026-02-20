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

import org.primefaces.cdk.api.FacesBehaviorEvent;
import org.primefaces.cdk.api.FacesBehaviorEvents;
import org.primefaces.cdk.api.FacesComponentBase;
import org.primefaces.cdk.api.Property;
import org.primefaces.component.api.AbstractPrimeHtmlInputText;
import org.primefaces.component.api.InputHolder;
import org.primefaces.component.api.Widget;
import org.primefaces.event.SelectEvent;

/**
 * <code>InputPhone</code> component base class.
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 * @since 7.0
 */
@FacesComponentBase
@FacesBehaviorEvents({
            @FacesBehaviorEvent(name = "countrySelect", event = SelectEvent.class,
                        description = "Fires when country is selected.", defaultEvent = false)
})
public abstract class InputPhoneBase extends AbstractPrimeHtmlInputText implements Widget, InputHolder {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.InputPhone";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.InputPhoneRenderer";

    public InputPhoneBase() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Property(description = "HTML input type.", defaultValue = "tel")
    public abstract String getType();

    @Property(description = "When true, country dropdown is shown.", defaultValue = "true")
    public abstract boolean isAllowDropdown();

    @Property(description = "When true, dial code is hidden when not focused.", defaultValue = "true")
    public abstract boolean isAutoHideDialCode();

    @Property(description = "Auto placeholder mode: polite, aggressive, or off.", defaultValue = "polite")
    public abstract String getAutoPlaceholder();

    @Property(description = "Countries to exclude (list or comma-separated string).")
    public abstract Object getExcludeCountries();

    @Property(description = "When true, dropdown has fixed width.", defaultValue = "true")
    public abstract boolean isFixDropdownWidth();

    @Property(description = "When true, value is formatted on display.", defaultValue = "true")
    public abstract boolean isFormatOnDisplay();

    @Property(description = "When true, value is formatted as user types.", defaultValue = "true")
    public abstract boolean isFormatAsYouType();

    @Property(description = "JavaScript callback for geo IP lookup (required when initialCountry is 'auto').")
    public abstract String getGeoIpLookup();

    @Property(description = "Initial country (ISO2 code or 'auto').", defaultValue = "us")
    public abstract String getInitialCountry();

    @Property(description = "Localized country names (map or JSON string).")
    public abstract Object getLocalizedCountries();

    @Property(description = "When true, store national number without dial code.", defaultValue = "true")
    public abstract boolean isNationalMode();

    @Property(description = "Countries to show only (list or comma-separated string).")
    public abstract Object getOnlyCountries();

    @Property(description = "Placeholder number type for libphonenumber.", defaultValue = "mobile")
    public abstract String getPlaceholderNumberType();

    @Property(description = "Preferred countries shown at top (list or comma-separated string).")
    public abstract Object getPreferredCountries();

    @Property(description = "When true, dial code is shown separately.", defaultValue = "false")
    public abstract boolean isSeparateDialCode();

    @Property(description = "Inline style for the input.")
    public abstract String getInputStyle();

    @Property(description = "Style class for the input.")
    public abstract String getInputStyleClass();
}
