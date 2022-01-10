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
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.Calendar;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.extensions.event.BeforeShowEvent;
import org.primefaces.extensions.event.CloseEvent;
import org.primefaces.extensions.event.TimeSelectEvent;

/**
 * TimePickerController using java.util.Date and java.time.LocalTime.
 *
 * @author ova / last modified by Melloware
 */
@Named
@ViewScoped
public class TimePickerController implements Serializable {

    private static final long serialVersionUID = 20120224L;

    private Date time1;
    private Date time2;
    private LocalTime time3;
    private LocalTime time4;
    private LocalTime time5;
    private Date time6;
    private boolean showTime = false;
    private String locale = "en_US";

    public TimePickerController() {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.AM_PM, Calendar.AM);
        calendar.set(Calendar.HOUR, 8);
        calendar.set(Calendar.MINUTE, 15);
        time1 = calendar.getTime();

        calendar.set(Calendar.HOUR, 11);
        calendar.set(Calendar.MINUTE, 40);
        time2 = calendar.getTime();

        time3 = LocalTime.now();

        time5 = LocalTime.now();

        time6 = new Date();
    }

    public Date getTime1() {
        return time1;
    }

    public void setTime1(final Date time1) {
        this.time1 = time1;
    }

    public Date getTime2() {
        return time2;
    }

    public void setTime2(final Date time2) {
        this.time2 = time2;
    }

    public LocalTime getTime3() {
        return time3;
    }

    public void setTime3(final LocalTime time3) {
        this.time3 = time3;
    }

    public LocalTime getTime4() {
        return time4;
    }

    public void setTime4(final LocalTime time4) {
        this.time4 = time4;
    }

    public LocalTime getTime5() {
        return time5;
    }

    public void setTime5(final LocalTime time5) {
        this.time5 = time5;
    }

    public Date getTime6() {
        return time6;
    }

    public void setTime6(final Date time6) {
        this.time6 = time6;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(final String locale) {
        this.locale = locale;
    }

    public String getFormattedTime1() {
        return getFormattedTime(time1, "HH:mm");
    }

    public String getFormattedTime2() {
        return getFormattedTime(time2, "HH:mm");
    }

    public String getFormattedTime3() {
        return getFormattedTime(time3, "HH:mm");
    }

    public String getFormattedTime4() {
        return getFormattedTime(time4, "hh-mm a");
    }

    public String getFormattedTime5() {
        return getFormattedTime(time5, "HH:mm");
    }

    public String getFormattedTime6() {
        return getFormattedTime(time6, "HH:mm");
    }

    public void showTime(final ActionEvent ae) {
        showTime = true;
    }

    public void changeLocale(final ActionEvent ae) {
        if (StringUtils.equalsIgnoreCase(locale, "pt_BR")) {
            locale = "en_US";
        }
        else {
            locale = "pt_BR";
        }
    }

    public boolean isShowTimeDialog() {
        if (showTime) {
            showTime = false;

            return true;
        }

        return false;
    }

    public void timeSelectListener(final TimeSelectEvent<Date> timeSelectEvent) {
        final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Time select fired",
                    "Selected time: " + getFormattedTime(timeSelectEvent.getTime(), "HH:mm"));
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void beforeShowListener(final BeforeShowEvent beforeShowEvent) {
        final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Before show fired",
                    "Component id: " + beforeShowEvent.getComponent().getId());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void closeListener(final CloseEvent closeEvent) {
        final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Close fired",
                    "Component id: " + closeEvent.getComponent().getId());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    private String getFormattedTime(final Date time, final String format) {
        if (time == null) {
            return null;
        }

        final SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(time);
    }

    private String getFormattedTime(final Temporal time, final String format) {
        if (time == null) {
            return null;
        }

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return formatter.format(time);
    }

}
