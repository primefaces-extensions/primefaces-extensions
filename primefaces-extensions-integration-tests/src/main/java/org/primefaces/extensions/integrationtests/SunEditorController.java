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
package org.primefaces.extensions.integrationtests;

import java.io.Serializable;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named
@ViewScoped
public class SunEditorController implements Serializable {

    private static final long serialVersionUID = 4172903358810045913L;

    private String value;
    private String ajaxValue;
    private String toolbarValue;
    private String readonlyValue;
    private String disabledValue;
    private String unsafeValue;
    private int changeCount;

    public SunEditorController() {
        value = "<p>Hello <b>SunEditor</b></p>";
        ajaxValue = "<p>Ajax</p>";
        toolbarValue = "<p>Toolbar</p>";
        readonlyValue = "<p>Read only</p>";
        disabledValue = "<p>Disabled</p>";
        unsafeValue = "<p>Safe</p><script>window.sunEditorXss = true;</script>";
    }

    public void submit() {
        addMessage(FacesMessage.SEVERITY_INFO, "Submitted", "Value saved");
    }

    public void onChange() {
        changeCount++;
    }

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAjaxValue() {
        return ajaxValue;
    }

    public void setAjaxValue(String ajaxValue) {
        this.ajaxValue = ajaxValue;
    }

    public String getToolbarValue() {
        return toolbarValue;
    }

    public void setToolbarValue(String toolbarValue) {
        this.toolbarValue = toolbarValue;
    }

    public String getReadonlyValue() {
        return readonlyValue;
    }

    public void setReadonlyValue(String readonlyValue) {
        this.readonlyValue = readonlyValue;
    }

    public String getDisabledValue() {
        return disabledValue;
    }

    public void setDisabledValue(String disabledValue) {
        this.disabledValue = disabledValue;
    }

    public String getUnsafeValue() {
        return unsafeValue;
    }

    public void setUnsafeValue(String unsafeValue) {
        this.unsafeValue = unsafeValue;
    }

    public int getChangeCount() {
        return changeCount;
    }

    public void setChangeCount(int changeCount) {
        this.changeCount = changeCount;
    }
}
