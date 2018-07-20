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
package org.primefaces.extensions.component.sheet;

import java.util.Collection;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * JSF Component used to represent a column in the Sheet component.
 *
 * @author Mark Lassiter / Melloware
 * @since 6.2
 */
public class SheetColumn extends UIInput implements ClientBehaviorHolder {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.SheetColumn";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    private static final String MESSAGE_REQUIRED = "A valid value for this column is required.";

    /**
     * Properties that are tracked by state saving.
     */
    enum PropertyKeys {

        /**
         * The width of the column
         */
        colWidth,

        /**
         * The type of the column (text, numeric, etc)
         */
        colType,

        /**
         * The header text to display for the column
         */
        headerText,

        /**
         * Password mask asterisk * by default
         */
        passwordHashSymbol,

        /**
         * Password fixed length
         */
        passwordHashLength,

        /**
         * Numbers can be formatted to look like currency, percentages, times, or even plain old numbers with decimal places, thousands, and abbreviations.
         */
        numericPattern,

        /**
         * Numbers such as currency can be configured for a Locale. Default to en_us.
         */
        numericLocale,

        /**
         * Moment.js date format for colType="date". Default is "DD/MM/YYYY"
         */
        dateFormat,

        /**
         * JSON config for coltype="date". DatePicker additional options (see https://github.com/dbushell/Pikaday#configuration)
         */
        datePickerConfig,

        /**
         * Moment.js time format for colType="date". Default is "h:mm:ss a"
         */
        timeFormat,

        /**
         * If true, the autocomplete cells will only accept values that are defined in the source array. Default true.
         */
        autoCompleteStrict,

        /**
         * If true, allows manual input of value that does not exist in the source. In this case, the field background highlight becomes red and the selection
         * advances to the next cell.
         */
        autoCompleteAllowInvalid,

        /**
         * Number of rows visible in the autocomplete dropdown.
         */
        autoCompleteVisibleRows,

        /**
         * If true, trims the dropdown to fit the cell size. Default to true.
         */
        autoCompleteTrimDropdown,

        /**
         * List of values to display in colType="dropdown" or "autocomplete".
         */
        selectItems,

        /**
         * Flag indicating whether or not the column is read only
         */
        readonly,

        /**
         * Flag indicating whether or not a cell is read only
         */
        readonlyCell,

        /**
         * the style class for the cell
         */
        styleClass,

        /**
         * Filter by value expression
         */
        filterBy,

        /**
         * Filter options
         */
        filterOptions,

        /**
         * The submitted filtered value
         */
        filterValue,
        /**
         * Controls the visibilty of the column, default is true.
         */
        visible,
        /**
         * A Javascript function, regular expression or a string, which will be used in the process of cell validation. If a function is used, be sure to
         * execute the callback argument with either true (callback(true)) if the validation passed or with false (callback(false)), if the validation failed.
         * Note, that this in the function points to the cellProperties object.
         */
        onvalidate
    }

    private Object localValue;

    /**
     * Sheet reference. Updated on decode.
     */
    private Sheet sheet;

    /*
     * (non-Javadoc)
     * @see javax.faces.component.UIComponent#getFamily()
     */
    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    /**
     * Updates the fixed columns count.
     *
     * @param value
     */
    public void setHeaderText(final String value) {
        getStateHelper().put(PropertyKeys.headerText, value);
    }

    /**
     * The fixed column count.
     *
     * @return
     */
    public String getHeaderText() {
        final Object result = getStateHelper().eval(PropertyKeys.headerText, null);
        if (result == null) {
            return null;
        }
        return result.toString();
    }

    /**
     * Updates the readOnly property
     *
     * @param value
     */
    public void setReadonly(final Boolean value) {
        getStateHelper().put(PropertyKeys.readonly, value);
    }

    /**
     * Flag indicating whether this column is read only.
     *
     * @return true if read only, otherwise false
     */
    public Boolean isReadonly() {
        return Boolean.valueOf(getStateHelper().eval(PropertyKeys.readonly, Boolean.FALSE).toString());
    }

    /**
     * Updates the readOnly property of the cell
     *
     * @param value
     */
    public void setReadonlyCell(final Boolean value) {
        getStateHelper().put(PropertyKeys.readonlyCell, value);
    }

    /**
     * Flag indicating whether this cell is read only. the var reference will be available.
     *
     * @return true if read only, otherwise false
     */
    public Boolean isReadonlyCell() {
        return Boolean.valueOf(getStateHelper().eval(PropertyKeys.readonlyCell, Boolean.FALSE).toString());
    }

    /**
     * Updates the column width
     *
     * @param value
     */
    public void setColWidth(final Integer value) {
        getStateHelper().put(PropertyKeys.colWidth, value);
    }

    /**
     * The column width
     *
     * @return
     */
    public Integer getColWidth() {
        final Object result = getStateHelper().eval(PropertyKeys.colWidth, null);
        if (result == null) {
            return null;
        }
        return Integer.valueOf(result.toString());
    }

    /**
     * Updates the column type. Possible values are: text, numeric, date, checkbox, autocomplete, handsontable.
     *
     * @param value
     */
    public void setColType(final String value) {
        getStateHelper().put(PropertyKeys.colType, value);
    }

    /**
     * the Handsontable column type.
     */
    public String getColType() {
        return getStateHelper().eval(PropertyKeys.colType, "text").toString();
    }

    public Boolean isAutoCompleteAllowInvalid() {
        return Boolean.valueOf(getStateHelper().eval(PropertyKeys.autoCompleteAllowInvalid, Boolean.FALSE).toString());
    }

    public void setAutoCompleteAllowInvalid(final Boolean value) {
        getStateHelper().put(PropertyKeys.autoCompleteAllowInvalid, value);
    }

    public Boolean isAutoCompleteStrict() {
        return Boolean.valueOf(getStateHelper().eval(PropertyKeys.autoCompleteStrict, Boolean.TRUE).toString());
    }

    public void setAutoCompleteStrict(final Boolean value) {
        getStateHelper().put(PropertyKeys.autoCompleteStrict, value);
    }

    public Boolean isAutoCompleteTrimDropdown() {
        return Boolean.valueOf(getStateHelper().eval(PropertyKeys.autoCompleteTrimDropdown, Boolean.TRUE).toString());
    }

    public void setAutoCompleteTrimDropdown(final Boolean value) {
        getStateHelper().put(PropertyKeys.autoCompleteTrimDropdown, value);
    }

    public void setAutoCompleteVisibleRows(final Integer value) {
        getStateHelper().put(PropertyKeys.autoCompleteVisibleRows, value);
    }

    public Integer getAutoCompleteVisibleRows() {
        return (Integer) getStateHelper().eval(PropertyKeys.autoCompleteVisibleRows, null);
    }

    public Object getSelectItems() {
        return getStateHelper().eval(PropertyKeys.selectItems, null);
    }

    public void setSelectItems(final Object selectItems) {
        getStateHelper().put(PropertyKeys.selectItems, selectItems);
    }

    public Integer getPasswordHashLength() {
        return (Integer) getStateHelper().eval(PropertyKeys.passwordHashLength, null);
    }

    public void setPasswordHashLength(final Integer _passwordHashLength) {
        getStateHelper().put(PropertyKeys.passwordHashLength, _passwordHashLength);
    }

    public void setPasswordHashSymbol(final String _passwordHashSymbol) {
        getStateHelper().put(PropertyKeys.passwordHashSymbol, _passwordHashSymbol);
    }

    public String getPasswordHashSymbol() {
        return (String) getStateHelper().eval(PropertyKeys.passwordHashSymbol, "*");
    }

    public void setNumericPattern(final String _numericPattern) {
        getStateHelper().put(PropertyKeys.numericPattern, _numericPattern);
    }

    public String getNumericPattern() {
        return (String) getStateHelper().eval(PropertyKeys.numericPattern, "0 a");
    }

    public String getNumericLocale() {
        return (String) getStateHelper().eval(PropertyKeys.numericLocale, "en-US");
    }

    public void setNumericLocale(final String locale) {
        getStateHelper().put(PropertyKeys.numericLocale, locale);
    }

    public void setDateFormat(final String _dateFormat) {
        getStateHelper().put(PropertyKeys.dateFormat, _dateFormat);
    }

    public String getDateFormat() {
        return (String) getStateHelper().eval(PropertyKeys.dateFormat, "DD/MM/YYYY");
    }

    public void setDatePickerConfig(final String _datePickerConfig) {
        getStateHelper().put(PropertyKeys.datePickerConfig, _datePickerConfig);
    }

    public String getDatePickerConfig() {
        return (String) getStateHelper().eval(PropertyKeys.datePickerConfig, null);
    }

    public void setTimeFormat(final String _timeFormat) {
        getStateHelper().put(PropertyKeys.timeFormat, _timeFormat);
    }

    public String getTimeFormat() {
        return (String) getStateHelper().eval(PropertyKeys.timeFormat, "h:mm:ss a");
    }

    public void setOnvalidate(final String _onvalidate) {
        getStateHelper().put(PropertyKeys.onvalidate, _onvalidate);
    }

    public String getOnvalidate() {
        return (String) getStateHelper().eval(PropertyKeys.onvalidate, null);
    }

    public void setVisible(final Boolean value) {
        getStateHelper().put(PropertyKeys.visible, value);
    }

    public Boolean isVisible() {
        return Boolean.valueOf(getStateHelper().eval(PropertyKeys.visible, Boolean.TRUE).toString());
    }

    /**
     * Update the style class for the cell.
     *
     * @param value
     */
    public void setStyleClass(final String value) {
        getStateHelper().put(PropertyKeys.styleClass, value);
    }

    /**
     * The style class for the cell.
     *
     * @return
     */
    public String getStyleClass() {
        final Object result = getStateHelper().eval(PropertyKeys.styleClass, null);
        if (result == null) {
            return null;
        }
        return result.toString();
    }

    /**
     * The filterBy expression
     *
     * @return
     */
    public Object getFilterBy() {
        return getStateHelper().eval(PropertyKeys.filterBy, null);
    }

    /**
     * Update the filter by field
     *
     * @param filterBy
     */
    public void setFilterBy(final Object filterBy) {
        getStateHelper().put(PropertyKeys.filterBy, filterBy);
    }

    /**
     * The filter value submitted by the user
     *
     * @return
     */
    public String getFilterValue() {
        return (String) getStateHelper().get(PropertyKeys.filterValue);
    }

    /**
     * Update the filter value for this column
     *
     * @param filterValue
     */
    public void setFilterValue(final String filterValue) {
        getStateHelper().put(PropertyKeys.filterValue, filterValue);
    }

    /**
     * The filterOptions expression
     *
     * @return
     */
    public Collection<SelectItem> getFilterOptions() {
        return (Collection<SelectItem>) getStateHelper().eval(PropertyKeys.filterOptions, null);
    }

    /**
     * Update the filterOptions field
     */
    public void setFilterOptions(final Collection<SelectItem> filterOptions) {
        getStateHelper().put(PropertyKeys.filterOptions, filterOptions);
    }

    /**
     * Get the parent sheet
     *
     * @return
     */
    public Sheet getSheet() {
        if (sheet != null) {
            return sheet;
        }

        UIComponent parent = getParent();
        while (parent != null && !(parent instanceof Sheet)) {
            parent = parent.getParent();
        }
        return (Sheet) parent;
    }

    /**
     * Updates the sheet reference to work around getParent sometimes returning null.
     *
     * @param sheet the owning sheet
     */
    public void setSheet(final Sheet sheet) {
        this.sheet = sheet;
    }

    /**
     * --------------------------------------------------
     * <p>
     * Override UIInput methods
     */

    /**
     * Ignore attempts to set the local value for this column, again done by parent.
     */
    @Override
    public void setValue(final Object value) {
        localValue = value;
        setLocalValueSet(true);
    }

    /**
     * When asked for the value, return the local value if available, otherwise the sheet value.
     */
    @Override
    public Object getValue() {
        return localValue;
    }

    /**
     * We are valid if the sheet is valid, we do not track at the individual column.
     */
    @Override
    public boolean isValid() {
        return getSheet().isValid();
    }

    /**
     * when we become valid, invalidate the whole sheet.
     */
    @Override
    public void setValid(final boolean valid) {
        getSheet().setValid(valid);
    }

    /**
     * Sheet handles decoding of all submitted values
     */
    @Override
    public void processDecodes(final FacesContext context) {
        // do nothing, done for us by sheet
    }

    /**
     * Sheet handles updating of model
     */
    @Override
    public void processUpdates(final FacesContext context) {
        // do nothing, done for us by sheet
    }

    /**
     * Reset the local value. No submitted value tracked here. Validity not tracked here.
     */
    @Override
    public void resetValue() {
        setValue(null);
        setLocalValueSet(false);
    }

    /**
     * Don't do anything when called by inherited behavior. Sheet will call validate directly
     */
    @Override
    public void processValidators(final FacesContext context) {
        // do nothing, sheet will call validate directly
    }

    /**
     * Process all validators (skip normal UIInput behavior)
     */
    @Override
    public void validate(final FacesContext context) {
        if (context == null) {
            throw new NullPointerException();
        }

        final Validator[] validators = getValidators();
        final Object value = getValue();

        if (!validateRequired(context, value)) {
            return;
        }

        if (validators == null) {
            return;
        }

        for (final Validator validator : validators) {
            try {
                validator.validate(context, this, value);
            }
            catch (final ValidatorException ve) {
                // If the validator throws an exception, we're
                // invalid, and we need to add a message
                setValid(false);
                FacesMessage message;
                final String validatorMessageString = getValidatorMessage();

                if (null != validatorMessageString) {
                    message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                validatorMessageString,
                                validatorMessageString);
                    message.setSeverity(FacesMessage.SEVERITY_ERROR);
                }
                else {
                    final Collection<FacesMessage> messages = ve.getFacesMessages();
                    if (null != messages) {
                        message = null;
                        final String cid = getClientId(context);
                        for (final FacesMessage m : messages) {
                            context.addMessage(cid, m);
                        }
                    }
                    else {
                        message = ve.getFacesMessage();
                    }
                }
                if (message != null) {
                    final Sheet sheet = getSheet();
                    if (sheet == null) {
                        return;
                    }
                    context.addMessage(getClientId(context), message);
                    sheet.getInvalidUpdates().add(
                                new SheetInvalidUpdate(sheet.getRowKeyValue(context), sheet.getColumns().indexOf(this), this, value,
                                            message.getDetail()));

                }
            }
        }

    }

    /**
     * Validates the value against the required flags on this column.
     *
     * @param context the FacesContext
     * @param newValue the new value for this column
     * @return true if passes validation, otherwise valse
     */
    protected boolean validateRequired(final FacesContext context, final Object newValue) {
        // If our value is valid, enforce the required property if present
        if (isValid() && isRequired() && isEmpty(newValue)) {
            final String requiredMessageStr = getRequiredMessage();
            FacesMessage message;
            if (null != requiredMessageStr) {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            requiredMessageStr,
                            requiredMessageStr);
            }
            else {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            MESSAGE_REQUIRED,
                            MESSAGE_REQUIRED);
            }
            context.addMessage(getClientId(context), message);
            final Sheet sheet = getSheet();
            if (sheet != null) {
                sheet.getInvalidUpdates().add(
                            new SheetInvalidUpdate(sheet.getRowKeyValue(context), sheet.getColumns().indexOf(this), this, newValue,
                                        message.getDetail()));
            }
            setValid(false);
            return false;
        }
        return true;
    }
}
