/*
 * Copyright 2011-2020 PrimeFaces Extensions
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
        final List<SelectItem> results = new ArrayList<SelectItem>();
        results.add(new SelectItem("ms", "Bahasa Melayu (Malaysian)"));
        results.add(new SelectItem("ca", "Català (Catalan)"));
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
