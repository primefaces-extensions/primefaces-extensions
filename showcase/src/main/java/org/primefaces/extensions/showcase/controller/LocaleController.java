/*
 * Copyright (c) 2011-2025 PrimeFaces Extensions
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
package org.primefaces.extensions.showcase.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named
@ViewScoped
public class LocaleController implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<Locale> selectedLocales;
    private Locale enteredLocale;
    private List<Locale> addedLocales;

    public LocaleController() {
        addedLocales = new ArrayList<>();
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
