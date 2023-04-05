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
package org.primefaces.extensions.showcase.controller.suneditor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class BasicSunEditorController implements Serializable {

    private static final long serialVersionUID = 1L;

    private String html = "<h1>SunEditor</h1><p>Lightweight, flexible, customizable <strong>WYSIWYG</strong> text editor.</p>";
    private String mode = "classic";

    private String language = "fr";
    private boolean rtl = false;

    public void addMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
    }

    public void onPaste(AjaxBehaviorEvent e) {
        addMessage("Paste event.");
    }

    public void onCopy(AjaxBehaviorEvent e) {
        addMessage("Copy event.");
    }

    public void onCut(AjaxBehaviorEvent e) {
        addMessage("Cut event.");
    }

    public void onDrop(AjaxBehaviorEvent e) {
        addMessage("Drop event.");
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public List<SelectItem> getLanguages() {
        final List<SelectItem> results = new ArrayList<>();
        results.add(new SelectItem("ckb", "Central Kurdish"));
        results.add(new SelectItem("da", "Dansk (Danish)"));
        results.add(new SelectItem("de", "Deutsch (German)"));
        results.add(new SelectItem("en", "English (US/UK)"));
        results.add(new SelectItem("es", "Español (Spanish)"));
        results.add(new SelectItem("fr", "Français (French)"));
        results.add(new SelectItem("he", "Hebrew (Israel)"));
        results.add(new SelectItem("it", "Italiano (Italian)"));
        results.add(new SelectItem("ja", "Japanese"));
        results.add(new SelectItem("ko", "Korean"));
        results.add(new SelectItem("lv", "Latvian"));
        results.add(new SelectItem("nl", "Nederlands (Dutch)"));
        results.add(new SelectItem("pl", "Polski (Polish)"));
        results.add(new SelectItem("pt_BR", "Português (Portuguese/Brazilian)"));
        results.add(new SelectItem("ro", "Romanian"));
        results.add(new SelectItem("ru", "\u0420\u0443\u0441\u0441\u043a\u0438\u0439 (Russian)"));
        results.add(new SelectItem("se", "Swedish"));
        results.add(new SelectItem("uk_ua", "\u0423\u043a\u0440\u0430\u0457\u043d\u0441\u044c\u043a\u0430 (Ukrainian)"));
        results.add(new SelectItem("zh_CN", "\u4e2d\u6587 (\u7b80\u4f53) (Chinese, Simplified)"));
        return results;
    }

    public List<SelectItem> getModes() {
        final List<SelectItem> results = new ArrayList<>();
        results.add(new SelectItem("'classic'", "Classic"));
        results.add(new SelectItem("'inline'", "Inline"));
        results.add(new SelectItem("'balloon'", "Balloon"));
        results.add(new SelectItem("balloon-always", "Balloon (Always)"));
        return results;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(final String mode) {
        this.mode = mode;
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
