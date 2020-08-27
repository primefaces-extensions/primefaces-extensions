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
