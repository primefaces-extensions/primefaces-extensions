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
package org.primefaces.extensions.showcase.controller.fuzzysearch;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.extensions.showcase.model.system.AvailableThemes;
import org.primefaces.extensions.showcase.model.system.Theme;

@Named
@ViewScoped
public class FuzzySearchController implements Serializable {

    private static final long serialVersionUID = 20120224L;

    private List<Theme> themes;
    private Theme selectedTheme;

    private List<String> timezones;
    private String selectedTimezone;

    @PostConstruct
    public void init() {
        themes = AvailableThemes.getInstance().getThemes();
        timezones = Arrays.asList(TimeZone.getAvailableIDs());
    }

    public void onChange(final AjaxBehaviorEvent event) {
        final Theme theme = (Theme) ((UIOutput) event.getSource()).getValue();
        final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Select fired: Theme is " + theme.getName(), null);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onSubmit(final ActionEvent actionEvent) {
        FacesMessage msg;
        if (selectedTheme != null) {
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Theme is: " + selectedTheme.getName(), null);
        }
        else {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No Theme Selected", null);
        }
        FacesContext.getCurrentInstance().addMessage(null, msg);

        if (selectedTimezone != null) {
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Timezone is: " + selectedTimezone, null);
        }
        else {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No Timezone Selected", null);
        }
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public List<Theme> getThemes() {
        return themes;
    }

    public void setThemes(final List<Theme> themes) {
        this.themes = themes;
    }

    public Theme getSelectedTheme() {
        return selectedTheme;
    }

    public void setSelectedTheme(final Theme selectedTheme) {
        this.selectedTheme = selectedTheme;
    }

    public List<String> getTimezones() {
        return timezones;
    }

    public void setTimezones(final List<String> timezones) {
        this.timezones = timezones;
    }

    public String getSelectedTimezone() {
        return selectedTimezone;
    }

    public void setSelectedTimezone(final String selectedTimezone) {
        this.selectedTimezone = selectedTimezone;
    }

}
