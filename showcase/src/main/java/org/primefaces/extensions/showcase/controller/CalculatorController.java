/*
 * Copyright (c) 2011-2022 PrimeFaces Extensions
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.extensions.event.ButtonEvent;
import org.primefaces.extensions.event.CloseEvent;
import org.primefaces.extensions.event.OpenEvent;

/**
 * Calculator Controller.
 *
 * @author Melloware mellowaredev@gmail.com
 */
@Named
@ViewScoped
public class CalculatorController implements Serializable {

    private static final long serialVersionUID = 20120224L;

    private BigDecimal hInputText;
    private BigDecimal pInputText;
    private BigDecimal pInputNumber = new BigDecimal("0.00");
    private String language = "fr";
    private boolean rtl = false;

    public List<SelectItem> getLanguages() {
        final List<SelectItem> results = new ArrayList<>();
        results.add(new SelectItem("ms", "Bahasa Melayu (Malaysian)"));
        results.add(new SelectItem("ca", "Català (Catalan)"));
        results.add(new SelectItem("da", "Dansk (Danish)"));
        results.add(new SelectItem("de", "Deutsch (German)"));
        results.add(new SelectItem("es", "Español (Spanish)"));
        results.add(new SelectItem("fr", "Français (French)"));
        results.add(new SelectItem("hr", "Hrvatski jezik (Croatian)"));
        results.add(new SelectItem("it", "Italiano (Italian)"));
        results.add(new SelectItem("nl", "Nederlands (Dutch)"));
        results.add(new SelectItem("no", "Norsk (Norwegian)"));
        results.add(new SelectItem("pl", "Polski (Polish)"));
        results.add(new SelectItem("pt_BR", "Português (Portuguese/Brazilian)"));
        results.add(new SelectItem("ru", "\u0420\u0443\u0441\u0441\u043a\u0438\u0439 (Russian)"));
        results.add(new SelectItem("sl", "Slovenski Jezik (Slovenian)"));
        results.add(new SelectItem("sr", "\u0421\u0440\u043f\u0441\u043a\u0438 (Serbian)"));
        results.add(new SelectItem("tr", "Türkçe (Turkish)"));
        results.add(new SelectItem("uk", "\u0423\u043a\u0440\u0430\u0457\u043d\u0441\u044c\u043a\u0430 (Ukrainian)"));
        results.add(new SelectItem("zh_CN", "\u4e2d\u6587 (\u7b80\u4f53) (Chinese, Simplified)"));
        results.add(new SelectItem("zh_TW", "\u6b63\u9ad4\u4e2d\u6587 (\u7e41\u9ad4) (Chinese, Traditional)"));
        return results;
    }

    public void closeListener(final CloseEvent closeEvent) {
        final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Close fired",
                    "Component id: " + closeEvent.getComponent().getId());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void openListener(final OpenEvent openEvent) {
        final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Open fired",
                    "Component id: " + openEvent.getComponent().getId());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void buttonListener(final ButtonEvent buttonEvent) {
        final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Button fired",
                    "Name: " + buttonEvent.getName() + " Value: " + buttonEvent.getValue());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public BigDecimal gethInputText() {
        return hInputText;
    }

    public void sethInputText(final BigDecimal hInputText) {
        this.hInputText = hInputText;
    }

    public BigDecimal getpInputText() {
        return pInputText;
    }

    public void setpInputText(final BigDecimal pInputText) {
        this.pInputText = pInputText;
    }

    public BigDecimal getpInputNumber() {
        return pInputNumber;
    }

    public void setpInputNumber(final BigDecimal pInputNumber) {
        this.pInputNumber = pInputNumber;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(final String language) {
        this.language = language;
    }

    public boolean isRtl() {
        return rtl;
    }

    public void setRtl(final boolean rtl) {
        this.rtl = rtl;
    }

}
