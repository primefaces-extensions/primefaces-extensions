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

import static java.util.Collections.singletonMap;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.abbreviate;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.extensions.model.monacoeditor.ELanguage;
import org.primefaces.extensions.model.monacoeditor.ETheme;
import org.primefaces.extensions.model.monacoeditor.EditorOptions;
import org.primefaces.extensions.model.monacoeditor.EditorStandaloneTheme;
import org.primefaces.extensions.showcase.util.MonacoEditorSettings;

/**
 * CodeMirrorController
 *
 * @author Andre Wachsmuth / last modified by $Author$
 * @version $Revision$
 */
@Named
@ViewScoped
public class MonacoEditorController implements Serializable {
    private static final long serialVersionUID = 20210216L;

    private static final String CUSTOM_CODE_EXTENDER = "custom_code.extender.";
    private static final String INLINE = "inline";
    private static final String MY_CUSTOM_THEME = "MyCustomTheme";

    private Map<String, EditorStandaloneTheme> customThemes;

    private EditorOptions editorOptions;
    private EditorOptions editorOptionsFramed;
    private EditorOptions editorOptionsExtender;

    private transient ResourceBundle examples;

    private List<String> languages;

    private String extenderError;

    private String extenderExample;
    private List<SelectItem> extenderExamples;
    private String extenderInfo;
    private String extenderName;

    private List<String> themes;

    private String type;

    private Locale locale;
    private Locale localeFramed;

    private List<Locale> locales;

    private String value;
    private String valueFramed;
    private String valueExtender;

    @PostConstruct
    protected void init() {
        this.examples = ResourceBundle.getBundle("monaco-examples");

        this.editorOptions = new EditorOptions();
        this.editorOptions.setTheme(ETheme.VS);
        this.editorOptions.setFontSize(12);

        this.editorOptionsFramed = new EditorOptions();
        this.editorOptionsFramed.setTheme(ETheme.VS);
        this.editorOptionsFramed.setFontSize(12);

        this.extenderExamples = MonacoEditorSettings.createEditorExamples();

        this.editorOptionsExtender = MonacoEditorSettings.createEditorOptionsExtender();

        this.type = INLINE;

        this.languages = Arrays.stream(ELanguage.values()).map(ELanguage::toString).sorted().collect(toList());
        this.themes = Arrays.stream(ETheme.values()).map(ETheme::toString).sorted().collect(toList());
        this.locales = MonacoEditorSettings.getBuiltInLocales();
    }

    /**
     * @return Custom styling themes that should be made available in the Monaco code editor.
     */
    public Map<String, EditorStandaloneTheme> getCustomThemes() {
        return customThemes;
    }

    /**
     * @return Options to apply to the Monaco code editor.
     */
    public EditorOptions getEditorOptions() {
        return editorOptions;
    }

    /**
     * @return Options to apply to the framed Monaco code editor.
     */
    public EditorOptions getEditorOptionsFramed() {
        return editorOptionsFramed;
    }

    /**
     * @return options to apply to the monaco editor for editing the custom extender.
     */
    public EditorOptions getEditorOptionsExtender() {
        return editorOptionsExtender;
    }

    /**
     * @return The error that was throw by the custom extender code in the extender showcase.
     */
    public String getExtenderError() {
        return extenderError;
    }

    /**
     * @return The currently selected example for the extender showcase.
     */
    public String getExtenderExample() {
        return extenderExample;
    }

    /***
     * @return All examples available in the extender showcase.
     */
    public List<SelectItem> getExtenderExamples() {
        return extenderExamples;
    }

    /**
     * @return HTML string with additional info about the loaded extender example.
     */
    public String getExtenderInfo() {
        return extenderInfo;
    }

    /**
     * @return Name of the loaded extender example.
     */
    public String getExtenderName() {
        return extenderName;
    }

    /**
     * @return The currently selected code language for the Monaco editor.
     */
    public String getLanguage() {
        return editorOptions.getLanguage();
    }

    /**
     * @return The currently selected code language for the framed Monaco editor.
     */
    public String getLanguageFramed() {
        return editorOptionsFramed.getLanguage();
    }

    /**
     * @return All available languages for the Monaco editor.
     */
    public List<String> getLanguages() {
        return languages;
    }

    /**
     * @return The current color theme of the editor.
     */
    public String getTheme() {
        return editorOptions.getTheme();
    }

    /**
     * @return The current color theme of the framed editor.
     */
    public String getThemeFramed() {
        return editorOptionsFramed.getTheme();
    }

    /**
     * @return All available themes for the editor.
     */
    public List<String> getThemes() {
        return themes;
    }

    /**
     * @return Whether to show the inline or framed version of the Monaco code editor.
     */
    public String getType() {
        return type;
    }

    /**
     * @return The current UI language used in the Monaco code editor.
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * @return The current UI language used in the framed Monaco code editor.
     */
    public Locale getLocaleFramed() {
        return localeFramed;
    }

    /**
     * @return A list of all built-in UI languages for the Monaco code editor.
     */
    public List<Locale> getLocales() {
        return locales;
    }

    /**
     * @return The code currently being edited by the editor.
     */
    public String getValue() {
        return value;
    }

    /**
     * @return Code for the Monaco extender that can be edited on the extender showcase page.
     */
    public String getValueExtender() {
        return valueExtender;
    }

    /**
     * @return The code currently being edited by the framed editor.
     */
    public String getValueFramed() {
        return valueFramed;
    }

    /**
     * @param extenderExample The currently selected example for the extender showcase.
     */
    public void setExtenderExample(String extenderExample) {
        this.extenderExample = extenderExample;
    }

    /**
     * @param language The currently selected code language for the monaco editor.
     */
    public void setLanguage(String language) {
        if (isEmpty(language)) {
            editorOptions.setLanguage(ELanguage.JAVASCRIPT);
        }
        else {
            editorOptions.setLanguage(language);
        }
    }

    /**
     * @param language The currently selected code language for the framed Monaco editor.
     */
    public void setLanguageFramed(String language) {
        if (isEmpty(language)) {
            editorOptionsFramed.setLanguage(ELanguage.JAVASCRIPT);
        }
        else {
            editorOptionsFramed.setLanguage(language);
        }
    }

    /**
     * @param theme The current color theme of the editor.
     */
    public void setTheme(String theme) {
        if (isEmpty(theme)) {
            editorOptions.setTheme(ETheme.VS);
        }
        else {
            editorOptions.setTheme(theme);
        }
    }

    /**
     * @param theme The current color theme of the framed editor.
     */
    public void setThemeFramed(String theme) {
        if (isEmpty(theme)) {
            editorOptionsFramed.setTheme(ETheme.VS);
        }
        else {
            editorOptionsFramed.setTheme(theme);
        }
    }

    /**
     * @param type Whether to show the inline or framed version of the Monaco code editor.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @param locale The current UI language used in the Monaco code editor.
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    /**
     * @param localeFramed The current UI language used in the framed Monaco code editor.
     */
    public void setLocaleFramed(Locale localeFramed) {
        this.localeFramed = localeFramed;
    }

    /**
     * @param value The code currently being edited by the editor.
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @param valueExtender Code for the Monaco extender that can be edited on the extender showcase page.
     */
    public void setValueExtender(String valueExtender) {
        this.valueExtender = valueExtender;
    }

    /**
     * @param valueFramed The code currently being edited by the framed editor.
     */
    public void setValueFramed(String valueFramed) {
        this.valueFramed = valueFramed;
    }

    /** Callback for when the code language was changed. Loads the code sample for that language. */
    public void onLanguageChange() {
        if (INLINE.equals(type)) {
            loadDefaultCode();
        }
        else {
            loadDefaultCodeFramed();
        }
    }

    /** Callback when the selected example was changed in the extender showcase. */
    public void onExtenderExampleChange() {
        loadExtenderExample(extenderExample);
    }

    /**
     * Callback when the run button was pressed in the extender showcase. Resets the error message.
     */
    public void onMonacoExtenderRun() {
        this.extenderError = null;
    }

    /**
     * Remote command listener when an error occurred in the custom extender entered by the user in the extender showcase.
     */
    public void onMonacoExtenderError() {
        final HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        this.extenderError = req.getParameter("monacoExtenderError");
    }

    /**
     * Loads the default code sample for the current language, if available.
     */
    private void loadDefaultCode() {
        String language = editorOptions.getLanguage();
        String propertyKey = "sample_code." + language;
        loadCode(propertyKey);
    }

    /**
     * Loads the default code sample for the current language, if available.
     */
    private void loadDefaultCodeFramed() {
        String language = editorOptionsFramed.getLanguage();
        String propertyKey = "sample_code." + language;
        loadCodeFramed(propertyKey);
    }

    /**
     * Loads the code with the given key from the properties file into the editor.
     *
     * @param propertyKey Key at which the code is stored in {@code monaco-examples.properties}
     */
    private void loadCode(String propertyKey) {
        try {
            value = propertyKey != null ? examples.getString(propertyKey) : "";
        }
        catch (MissingResourceException e) {
            value = "";
        }
    }

    /**
     * Loads the code with the given key from the properties file into the extender editor.
     *
     * @param propertyKey Key at which the code is stored in {@code monaco-examples.properties}
     */
    private void loadCodeExtender(String propertyKey) {
        try {
            valueExtender = propertyKey != null ? examples.getString(propertyKey) : "";
        }
        catch (MissingResourceException e) {
            valueExtender = "";
        }
    }

    /**
     * Loads the code with the given key from the properties file into the framed editor.
     *
     * @param propertyKey Key at which the code is stored in {@code monaco-examples.properties}
     */
    private void loadCodeFramed(String propertyKey) {
        try {
            valueFramed = propertyKey != null ? examples.getString(propertyKey) : "";
        }
        catch (MissingResourceException e) {
            valueFramed = "";
        }
    }

    /**
     * Loads the info about the extender example with the given key from the properties file.
     *
     * @param propertyKey Key at which the code is stored in {@code monaco-examples.properties}
     */
    private void loadExtenderInfo(String propertyKey) {
        try {
            extenderInfo = propertyKey != null ? examples.getString(propertyKey) : "";
        }
        catch (MissingResourceException e) {
            extenderInfo = "";
        }
    }

    /**
     * Loads the name of the extender example with the given key from the properties file.
     *
     * @param propertyKey Key at which the code is stored in {@code monaco-examples.properties}
     */
    private void loadExtenderName(String propertyKey) {
        try {
            extenderName = propertyKey != null ? examples.getString(propertyKey) : "";
        }
        catch (MissingResourceException e) {
            extenderName = "";
        }
    }

    /**
     * Loads the given example for the extender showcase.
     *
     * @param key Key of the extender example.
     */
    public void loadExtenderExample(String key) {
        loadCode(CUSTOM_CODE_EXTENDER + key + ".sample");
        loadCodeExtender(CUSTOM_CODE_EXTENDER + key + ".script");
        loadExtenderName(CUSTOM_CODE_EXTENDER + key + ".name");
        loadExtenderInfo(CUSTOM_CODE_EXTENDER + key + ".info");
        try {
            editorOptions.setLanguage(examples.getString(CUSTOM_CODE_EXTENDER + key + ".language"));
        }
        catch (MissingResourceException e) {
            editorOptions.setLanguage(ELanguage.TYPESCRIPT);
        }
    }

    /**
     * Demo submit to show that the data can be transferred to the server. Shows a message with the current code.
     */
    public void submitContent() {
        final String content = INLINE.equals(type) ? value : valueFramed;
        final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "MonacoEditor content: " + abbreviate(content, "...", 300));
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    /**
     * Set the initial values for the {@code basicUsage} showcase.
     */
    public void initBasicUsage() {
        editorOptions.setLanguage(ELanguage.TYPESCRIPT);
        loadDefaultCode();

        editorOptionsFramed.setLanguage(ELanguage.TYPESCRIPT);
        loadDefaultCodeFramed();
    }

    /**
     * Set the initial values for the {@code customTheme} showcase.
     */
    public void initCustomTheme() {
        customThemes = singletonMap(MY_CUSTOM_THEME, MonacoEditorSettings.createDemoCustomTheme());
        editorOptions.setLanguage(ELanguage.HTML);
        editorOptions.setTheme(MY_CUSTOM_THEME);
        loadCode("custom_code.htmlCustomTheme");

        editorOptionsFramed.setLanguage(ELanguage.HTML);
        editorOptionsFramed.setTheme(MY_CUSTOM_THEME);
        loadCodeFramed("custom_code.htmlCustomTheme");
    }

    /**
     * Set the initial values for the {@code customLocalization} showcase.
     */
    public void initCustomLocalization() {
        editorOptions.setLanguage(ELanguage.TYPESCRIPT);
        loadDefaultCode();

        editorOptionsFramed.setLanguage(ELanguage.TYPESCRIPT);
        loadDefaultCodeFramed();
    }

    /**
     * Set the initial values for the {@code events} showcase.
     */
    public void initEvents() {
        editorOptions.setLanguage(ELanguage.TYPESCRIPT);
        loadDefaultCode();

        editorOptionsFramed.setLanguage(ELanguage.TYPESCRIPT);
        loadDefaultCodeFramed();
    }

    /**
     * Set the initial values for the {@code extender} showcase.
     */
    public void initExtender() {
        editorOptions.setLanguage(ELanguage.JAVASCRIPT);
        extenderExample = "jquery";
        loadExtenderExample("jquery");
    }
}
