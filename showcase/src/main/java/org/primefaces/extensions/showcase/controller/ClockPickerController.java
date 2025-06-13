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
import java.time.LocalTime;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 * ClockPickerController
 *
 * @author @jxmai / last modified by $Author$
 * @version $Revision$
 */
@Named
@ViewScoped
public class ClockPickerController implements Serializable {

    private static final long serialVersionUID = 897540091000342926L;

    private LocalTime time;
    private LocalTime time2;
    private LocalTime time3;

    public ClockPickerController() {
        // Initialize time to 8:15 AM
        time = LocalTime.of(8, 15);
        time2 = LocalTime.of(9, 28);
        time3 = LocalTime.of(13, 44);
    }

    public void showTime1() {
        if (time != null) {
            int hour = time.getHour();
            int min = time.getMinute();

            String message = String.format("Selected hour: %d, Selected min: %d", hour, min);
            addMessage(FacesMessage.SEVERITY_INFO, "Info Message", message);
        }
        else {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error Message", "Time is not selected.");
        }
    }

    public void showTime2() {
        if (time2 != null) {
            addMessage(FacesMessage.SEVERITY_INFO, "Info Message", String.format("Ajax Event: %s", time2));
        }
        else {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error Message", "Time is not selected.");
        }
    }

    public void showTime3() {
        if (time3 != null) {
            addMessage(FacesMessage.SEVERITY_INFO, "Info Message", String.format("Ajax Event: %s", time3));
        }
        else {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error Message", "Time is not selected.");
        }
    }

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public LocalTime getTime2() {
        return time2;
    }

    public void setTime2(LocalTime time2) {
        this.time2 = time2;
    }

    public LocalTime getTime3() {
        return time3;
    }

    public void setTime3(LocalTime time3) {
        this.time3 = time3;
    }

}