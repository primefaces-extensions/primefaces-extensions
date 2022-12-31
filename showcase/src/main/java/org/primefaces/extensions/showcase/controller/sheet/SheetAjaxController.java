/*
 * Copyright (c) 2011-2023 PrimeFaces Extensions
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
package org.primefaces.extensions.showcase.controller.sheet;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.commons.collections4.IterableUtils;
import org.primefaces.extensions.component.sheet.Sheet;
import org.primefaces.extensions.event.SheetEvent;
import org.primefaces.extensions.model.sheet.SheetUpdate;

/**
 * {@link Sheet} Ajax Controller.
 *
 * @author Melloware mellowaredev@gmail.com
 */
@Named
@ViewScoped
public class SheetAjaxController extends SheetController {

    private static final long serialVersionUID = 20120224L;

    /**
     * Ajax callback from the Sheet component when a cell value is changed.
     */
    @Override
    public void cellChangeEvent(final SheetEvent event) {
        final Sheet sheet = event.getSheet();
        final List<SheetUpdate> updates = sheet.getUpdates();

        // only show 1 update
        SheetUpdate sheetUpdate = IterableUtils.first(updates);
        final Long id = (Long) sheetUpdate.getRowKey();
        final Object oldValue = sheetUpdate.getOldValue();
        final Object newValue = sheetUpdate.getNewValue();

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Update Success",
                    String.format("Asset %s updated. Old Value = %s, New Value = %s", id, oldValue, newValue)));

        sheet.commitUpdates();
    }

    /**
     * Ajax callback from the Sheet component when a column is selected.
     */
    public static void columnSelectEvent(final SheetEvent event) {
        final Sheet sheet = event.getSheet();
        final int column = sheet.getSelectedColumn() + 1;
        FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Column Selected", String.format("Column %d selected.", column)));
    }

    /**
     * Ajax callback from the Sheet component when a row is selected.
     */
    public static void rowSelectEvent(final SheetEvent event) {
        final Sheet sheet = event.getSheet();
        final int row = sheet.getSelectedRow() + 1;
        FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Row Selected", String.format("Row %d selected.", row)));
    }

    public static void validateExactly5(final FacesContext context, final UIComponent comp, final Object value) {
        if (context == null || comp == null) {
            return;
        }
        final Integer integer = (Integer) value;
        if (integer.intValue() != 5) {
            final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                        "Value must only be 5 exactly!");
            throw new ValidatorException(message);
        }
    }

}
