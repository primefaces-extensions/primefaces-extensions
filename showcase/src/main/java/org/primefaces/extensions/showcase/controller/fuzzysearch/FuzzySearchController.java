/*
 * Copyright 2011-2021 PrimeFaces Extensions
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
