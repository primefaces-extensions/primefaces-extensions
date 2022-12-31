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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.commons.lang3.RandomUtils;
import org.primefaces.extensions.component.sheet.Sheet;
import org.primefaces.extensions.showcase.model.sheet.DynaSheetCell;
import org.primefaces.extensions.showcase.model.sheet.DynaSheetRow;

/**
 * {@link Sheet} Controller.
 *
 * @author Melloware mellowaredev@gmail.com
 */
@Named
@ViewScoped
public class SheetDynamicController implements Serializable {

    private static final long serialVersionUID = 20120224L;

    private List<DynaSheetRow> sheetRows = new ArrayList<>();
    private List<DynaSheetRow> filteredSheetRows = new ArrayList<>();
    private List<Integer> hoursOfDay = new ArrayList<>();

    public SheetDynamicController() {
        addRows(24);
    }

    private void addRows(final int count) {
        for (int i = 0; i < count; i++) {
            final DynaSheetRow row = new DynaSheetRow();
            row.setId(RandomUtils.nextLong());
            row.setReadOnly(false);
            final Integer hourOfDay = Integer.valueOf(i);
            hoursOfDay.add(hourOfDay);
            for (int j = 0; j < count; j++) {
                row.getCells().add(DynaSheetCell.create(Integer.valueOf(j), RandomUtils.nextInt(1, 1000)));
            }
            getSheetRows().add(row);
        }
    }

    public List<DynaSheetRow> getSheetRows() {
        return sheetRows;
    }

    public void setSheetRows(final List<DynaSheetRow> sheetRows) {
        this.sheetRows = sheetRows;
    }

    public List<Integer> getHoursOfDay() {
        return hoursOfDay;
    }

    public void setHoursOfDay(final List<Integer> hoursOfDay) {
        this.hoursOfDay = hoursOfDay;
    }

    public List<DynaSheetRow> getFilteredSheetRows() {
        return filteredSheetRows;
    }

    public void setFilteredSheetRows(final List<DynaSheetRow> filteredSheetRows) {
        this.filteredSheetRows = filteredSheetRows;
    }

}
