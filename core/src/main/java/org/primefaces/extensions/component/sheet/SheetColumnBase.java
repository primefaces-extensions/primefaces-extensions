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
package org.primefaces.extensions.component.sheet;

import java.util.Collection;

import jakarta.faces.component.UIInput;
import jakarta.faces.component.behavior.ClientBehaviorHolder;
import jakarta.faces.model.SelectItem;

import org.primefaces.cdk.api.FacesComponentBase;
import org.primefaces.cdk.api.Property;

/**
 * CDK base for the SheetColumn component.
 *
 * @since 6.2
 */
@FacesComponentBase
public abstract class SheetColumnBase extends UIInput implements ClientBehaviorHolder {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.SheetColumn";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";

    public SheetColumnBase() {
        setRendererType(null);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Property(description = "Column width.")
    public abstract Integer getColWidth();

    @Property(description = "Column type (text, numeric, date, etc).", defaultValue = "text")
    public abstract String getColType();

    @Property(description = "Header text for the column.")
    public abstract String getHeaderText();

    @Property(description = "Whether to trim whitespace in cell contents.", defaultValue = "true")
    public abstract Boolean getTrimWhitespace();

    @Property(description = "Password mask symbol.", defaultValue = "*")
    public abstract String getPasswordHashSymbol();

    @Property(description = "Password fixed length.")
    public abstract Integer getPasswordHashLength();

    @Property(description = "Numeric format pattern.", defaultValue = "0 a")
    public abstract String getNumericPattern();

    @Property(description = "Numeric locale.", defaultValue = "en-US")
    public abstract String getNumericLocale();

    @Property(description = "Moment.js date format.", defaultValue = "DD/MM/YYYY")
    public abstract String getDateFormat();

    @Property(description = "JSON config for date picker.")
    public abstract String getDatePickerConfig();

    @Property(description = "Moment.js time format.", defaultValue = "h:mm:ss a")
    public abstract String getTimeFormat();

    @Property(description = "Autocomplete strict mode.", defaultValue = "true")
    public abstract boolean isAutoCompleteStrict();

    @Property(description = "Autocomplete filter.", defaultValue = "true")
    public abstract boolean isAutoCompleteFilter();

    @Property(description = "Autocomplete allow invalid.", defaultValue = "false")
    public abstract boolean isAutoCompleteAllowInvalid();

    @Property(description = "Autocomplete visible rows.")
    public abstract Integer getAutoCompleteVisibleRows();

    @Property(description = "Autocomplete trim dropdown.", defaultValue = "true")
    public abstract boolean isAutoCompleteTrimDropdown();

    @Property(description = "Select items for dropdown/autocomplete.")
    public abstract Object getSelectItems();

    @Property(description = "Column read only.", defaultValue = "false")
    public abstract boolean isReadOnly();

    @Property(description = "Cell read only.", defaultValue = "false")
    public abstract boolean isReadonlyCell();

    @Property(description = "Style class for the cell.")
    public abstract String getStyleClass();

    @Property(description = "Filter by value expression.")
    public abstract Object getFilterBy();

    @Property(description = "Filter options.")
    public abstract Collection<SelectItem> getFilterOptions();

    @Property(description = "Filter match mode.")
    public abstract String getFilterMatchMode();

    @Property(description = "Submitted filter value.")
    public abstract String getFilterValue();

    @Property(description = "Column visible.", defaultValue = "true")
    public abstract boolean isVisible();

    @Property(description = "Word wrap.", defaultValue = "true")
    public abstract Boolean getWordWrap();

    @Property(description = "Client-side cell validation script.")
    public abstract String getOnvalidate();
}
