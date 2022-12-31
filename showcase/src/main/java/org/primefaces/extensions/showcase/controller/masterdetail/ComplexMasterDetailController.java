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
package org.primefaces.extensions.showcase.controller.masterdetail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.el.ELContext;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.extensions.showcase.model.Person;

/**
 * ComplexMasterDetailController.
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 */
@Named
@ViewScoped
public class ComplexMasterDetailController implements Serializable {

    private static final long serialVersionUID = 20111120L;

    private List<Person> persons;
    private List<SelectItem> availableLanguageSkills = null;
    private final List<String> selectedLanguageSkills = new ArrayList<String>();
    private String languageSkillToAdd;

    public ComplexMasterDetailController() {
        if (persons == null) {
            persons = new ArrayList<Person>();

            final Calendar calendar = Calendar.getInstance();
            calendar.set(1972, 5, 25);
            persons.add(new Person("1", "Max Mustermann", 1, calendar.getTime()));
            calendar.set(1981, 12, 10);
            persons.add(new Person("2", "Sara Schmidt", 2, calendar.getTime()));
            calendar.set(1968, 2, 13);
            persons.add(new Person("3", "Jasper Morgan", 3, calendar.getTime()));
        }
    }

    public List<Person> getPersons() {
        return persons;
    }

    public List<SelectItem> getAvailableLanguageSkills() {
        if (availableLanguageSkills == null) {
            availableLanguageSkills = new ArrayList<SelectItem>();
            availableLanguageSkills.add(new SelectItem("English", "English"));
            availableLanguageSkills.add(new SelectItem("German", "German"));
            availableLanguageSkills.add(new SelectItem("Russian", "Russian"));
            availableLanguageSkills.add(new SelectItem("Turkish", "Turkish"));
            availableLanguageSkills.add(new SelectItem("Dutch", "Dutch"));
            availableLanguageSkills.add(new SelectItem("French", "French"));
            availableLanguageSkills.add(new SelectItem("Italian", "Italian"));
        }

        return availableLanguageSkills;
    }

    public List<String> getSelectedLanguageSkills() {
        return selectedLanguageSkills;
    }

    public void setLanguageSkillToAdd(final String languageSkillToAdd) {
        this.languageSkillToAdd = languageSkillToAdd;
    }

    public String getLanguageSkillToAdd() {
        return languageSkillToAdd;
    }

    public String saveSuccess(final Person person) {
        final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Person " + person.getName() + " has been saved", null);
        FacesContext.getCurrentInstance().addMessage(null, message);

        return null;
    }

    public String saveFailure(final Person person) {
        final FacesContext fc = FacesContext.getCurrentInstance();
        final ELContext elContext = fc.getELContext();

        SelectLevelListener selectLevelListener;
        try {
            selectLevelListener = (SelectLevelListener) elContext.getELResolver().getValue(elContext, null,
                        "selectLevelListener");
            selectLevelListener.setErrorOccured(true);
        }
        catch (final RuntimeException e) {
            throw new FacesException(e.getMessage(), e);
        }

        final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Person " + person.getName() + " could not be saved", null);
        FacesContext.getCurrentInstance().addMessage(null, message);

        return null;
    }

    public String delete(final Person person) {
        for (final Person pers : persons) {
            if (pers.getId().equals(person.getId())) {
                persons.remove(pers);

                break;
            }
        }

        return null;
    }

    public void addLanguageSkill(final Person person) {
        if (languageSkillToAdd != null) {
            person.addLanguageSkill(languageSkillToAdd);
        }

        languageSkillToAdd = null;
    }
}
