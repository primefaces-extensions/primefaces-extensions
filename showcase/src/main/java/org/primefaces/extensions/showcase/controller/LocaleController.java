package org.primefaces.extensions.showcase.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class LocaleController implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<Locale> selectedLocales;
	private Locale enteredLocale;
	private List<Locale> addedLocales;

	public LocaleController() {
		addedLocales = new ArrayList<Locale>();
		addedLocales.add(Locale.GERMANY);
		addedLocales.add(Locale.ITALIAN);
		addedLocales.add(Locale.KOREA);
	}

	public void add() {
		addedLocales.add(enteredLocale);
	}

	public Locale getEnteredLocale() {
		return enteredLocale;
	}

	public void setEnteredLocale(final Locale enteredLocale) {
		this.enteredLocale = enteredLocale;
	}

	public List<Locale> getAddedLocales() {
		return addedLocales;
	}

	public void setAddedLocales(final List<Locale> addedLocales) {
		this.addedLocales = addedLocales;
	}

	public List<Locale> getSelectedLocales() {
		return selectedLocales;
	}

	public void setSelectedLocales(final List<Locale> selectedLocales) {
		this.selectedLocales = selectedLocales;
	}
}
