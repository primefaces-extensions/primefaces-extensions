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

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.FacesComponent;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;

/**
 * JSF component for a column in the Sheet component.
 *
 * @since 6.2
 */
@FacesComponent(value = SheetColumnBase.COMPONENT_TYPE, namespace = SheetColumnBase.COMPONENT_FAMILY)
public class SheetColumn extends SheetColumnBaseImpl {

    private static final String MESSAGE_REQUIRED = "A valid value for this column is required.";

    private Object localValue;
    private Sheet sheet;

    @Override
    public void setValue(final Object value) {
        localValue = value;
        setLocalValueSet(true);
    }

    @Override
    public Object getValue() {
        return localValue;
    }

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

    public void setSheet(final Sheet sheet) {
        this.sheet = sheet;
    }

    @Override
    public boolean isValid() {
        return getSheet().isValid();
    }

    @Override
    public void setValid(final boolean valid) {
        getSheet().setValid(valid);
    }

    @Override
    public void processDecodes(final FacesContext context) {
        // done by sheet
    }

    @Override
    public void processUpdates(final FacesContext context) {
        // done by sheet
    }

    @Override
    public void resetValue() {
        setValue(null);
        setLocalValueSet(false);
    }

    @Override
    public void processValidators(final FacesContext context) {
        // sheet calls validate directly
    }

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
                setValid(false);
                final FacesMessage message;
                final String validatorMessageString = getValidatorMessage();
                if (validatorMessageString != null) {
                    message = new FacesMessage(FacesMessage.SEVERITY_ERROR, validatorMessageString, validatorMessageString);
                }
                else {
                    final Collection<FacesMessage> messages = ve.getFacesMessages();
                    if (messages != null) {
                        final String cid = getClientId(context);
                        for (final FacesMessage m : messages) {
                            context.addMessage(cid, m);
                        }
                        continue;
                    }
                    message = ve.getFacesMessage();
                }
                if (message != null) {
                    final Sheet current = getSheet();
                    if (current != null) {
                        context.addMessage(getClientId(context), message);
                        current.getInvalidUpdates().add(
                                    new SheetInvalidUpdate(current.getRowKeyValue(context), current.getColumns().indexOf(this), this, value,
                                                message.getDetail()));
                    }
                }
            }
        }
    }

    protected boolean validateRequired(final FacesContext context, final Object newValue) {
        if (isValid() && isRequired() && jakarta.faces.component.UIInput.isEmpty(newValue)) {
            final String requiredMessageStr = getRequiredMessage();
            final FacesMessage message;
            if (requiredMessageStr != null) {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, requiredMessageStr, requiredMessageStr);
            }
            else {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, MESSAGE_REQUIRED, MESSAGE_REQUIRED);
            }
            context.addMessage(getClientId(context), message);
            final Sheet current = getSheet();
            if (current != null) {
                current.getInvalidUpdates().add(
                            new SheetInvalidUpdate(current.getRowKeyValue(context), current.getColumns().indexOf(this), this, newValue,
                                        message.getDetail()));
            }
            setValid(false);
            return false;
        }
        return true;
    }
}
