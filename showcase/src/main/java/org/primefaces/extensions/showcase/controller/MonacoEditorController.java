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

import org.apache.commons.lang3.StringUtils;
import org.primefaces.extensions.model.monaco.MonacoDiffEditorModel;
import org.primefaces.extensions.model.monacoeditor.DiffEditorOptions;
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

    private static final String CUSTOM_CODE_EXTENDER_CODE = "custom_code.extender.code.";
    private static final String CUSTOM_CODE_EXTENDER_DIFF = "custom_code.extender.diff.";
    private static final String INLINE = "inline";
    private static final String CODE = "code";
    private static final String MY_CUSTOM_THEME = "MyCustomTheme";

    private Map<String, EditorStandaloneTheme> customThemes;

    private EditorOptions editorOptions;
    private EditorOptions editorOptionsFramed;
    private EditorOptions editorOptionsExtender;
    private EditorOptions editorOptionsCss;

    private DiffEditorOptions editorOptionsDiff;
    private DiffEditorOptions editorOptionsFramedDiff;
    private DiffEditorOptions editorOptionsExtenderDiff;

    private ELanguage editorLangDiff;
    private ELanguage editorLangFramedDiff;
    private ELanguage editorLangExtenderDiff;

    private transient ResourceBundle examples;

    private List<String> languages;

    private String extenderError;

    private String extenderExample;
    private List<SelectItem> extenderExamples;
    private List<SelectItem> extenderExamplesDiff;
    private String extenderInfo;
    private String extenderName;

    private List<String> themes;

    private String type;
    private String mode;

    private Locale locale;
    private Locale localeFramed;

    private List<Locale> locales;

    private String value;
    private String valueFramed;
    private String valueExtender;
    private String valueCss;

    private MonacoDiffEditorModel valueDiff;
    private MonacoDiffEditorModel valueFramedDiff;

    private boolean required = false;
    private boolean requiredFramed = false;

    private boolean disabled = false;
    private boolean disabledFramed = false;

    private boolean readOnly = false;
    private boolean readOnlyFramed = false;

    private boolean originalRequired = false;
    private boolean originalRequiredFramed = false;

    private boolean originalDisabled = true;
    private boolean originalDisabledFramed = true;

    private boolean originalReadOnly = false;
    private boolean originalReadOnlyFramed = false;

    @PostConstruct
    protected void init() {
        this.examples = ResourceBundle.getBundle("monaco-examples");

        this.editorOptions = new EditorOptions();
        this.editorOptions.setTheme(ETheme.VS);
        this.editorOptions.setFontSize(12);

        this.editorOptionsDiff = new DiffEditorOptions();
        this.editorOptionsDiff.setTheme(ETheme.VS);
        this.editorOptionsDiff.setFontSize(12);
        this.editorOptionsDiff.setRenderSideBySide(true);

        this.editorOptionsFramed = new EditorOptions();
        this.editorOptionsFramed.setTheme(ETheme.VS);
        this.editorOptionsFramed.setFontSize(12);

        this.editorOptionsFramedDiff = new DiffEditorOptions();
        this.editorOptionsFramedDiff.setTheme(ETheme.VS);
        this.editorOptionsFramedDiff.setFontSize(12);
        this.editorOptionsFramedDiff.setRenderSideBySide(true);

        this.extenderExamples = MonacoEditorSettings.createEditorExamples();
        this.extenderExamplesDiff = MonacoEditorSettings.createEditorExamplesDiff();

        this.editorOptionsExtender = MonacoEditorSettings.createEditorOptionsExtender();
        this.editorOptionsExtenderDiff = MonacoEditorSettings.createEditorOptionsExtenderDiff();
        this.editorOptionsExtenderDiff.setRenderSideBySide(true);
        this.editorLangExtenderDiff = ELanguage.JAVASCRIPT;

        this.editorOptionsCss = MonacoEditorSettings.createEditorOptionsCss();

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
     * @return Options to apply to the Monaco diff editor.
     */
    public DiffEditorOptions getEditorOptionsDiff() {
        return editorOptionsDiff;
    }

    /**
     * @return Options to apply to the framed Monaco code editor.
     */
    public EditorOptions getEditorOptionsFramed() {
        return editorOptionsFramed;
    }

    /**
     * @return Options to apply to the Monaco editor for editing the custom CSS for the extender showcase.
     */
    public EditorOptions getEditorOptionsCss() {
        return editorOptionsCss;
    }

    /**
     * @return Options to apply to the framed Monaco code editor.
     */
    public DiffEditorOptions getEditorOptionsFramedDiff() {
        return editorOptionsFramedDiff;
    }

    /**
     * @return Options to apply to the Monaco editor for editing the custom extender.
     */
    public EditorOptions getEditorOptionsExtender() {
        return editorOptionsExtender;
    }

    /**
     * @return Options to apply to the Monaco editor for editing the custom extender.
     */
    public DiffEditorOptions getEditorOptionsExtenderDiff() {
        return editorOptionsExtenderDiff;
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

    /***
     * @return All examples available in the extender showcase.
     */
    public List<SelectItem> getExtenderExamplesDiff() {
        return extenderExamplesDiff;
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
        if (CODE.equals(mode)) {
            return editorOptions.getLanguage();
        }
        else {
            return editorLangDiff.toString();
        }
    }

    /**
     * @return The currently selected code language for the Monaco editor for the extender example.
     */
    public String getLanguageExtender() {
        if (CODE.equals(mode)) {
            return editorOptionsExtender.getLanguage();
        }
        else {
            return editorLangExtenderDiff.toString();
        }
    }

    /**
     * @return The currently selected code language for the framed Monaco editor.
     */
    public String getLanguageFramed() {
        if (CODE.equals(mode)) {
            return editorOptionsFramed.getLanguage();
        }
        else {
            return editorLangFramedDiff.toString();
        }
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
        if (CODE.equals(mode)) {
            return editorOptions.getTheme();
        }
        else {
            return editorOptionsDiff.getTheme();
        }
    }

    /**
     * @return The current color theme of the framed editor.
     */
    public String getThemeFramed() {
        if (CODE.equals(mode)) {
            return editorOptionsFramed.getTheme();
        }
        else {
            return editorOptionsFramedDiff.getTheme();
        }
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
     * @return The code currently being edited by the editor.
     */
    public MonacoDiffEditorModel getValueDiff() {
        return valueDiff;
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
     * @return The code currently being edited by the framed editor.
     */
    public MonacoDiffEditorModel getValueFramedDiff() {
        return valueFramedDiff;
    }

    /**
     * @return The custom CSS for the extender showcase.
     */
    public String getValueCss() {
        return valueCss;
    }

    /**
     * @param valueCss The custom CSS for the extender showcase.
     */
    public void setValueCss(final String valueCss) {
        this.valueCss = valueCss;
    }

    /**
     * @param extenderExample The currently selected example for the extender showcase.
     */
    public void setExtenderExample(final String extenderExample) {
        this.extenderExample = extenderExample;
    }

    /**
     * @return Whether the original editor (left side of the diff editor) is disabled.
     */
    public boolean isOriginalDisabled() {
        return originalDisabled;
    }

    /**
     * @param originalDisabled Whether the original editor (left side of the diff editor) is disabled.
     */
    public void setOriginalDisabled(final boolean originalDisabled) {
        this.originalDisabled = originalDisabled;
    }

    /**
     * @return Whether the original editor (left side of the diff editor) is disabled.
     */
    public boolean isOriginalDisabledFramed() {
        return originalDisabledFramed;
    }

    /**
     * @param originalDisabledFramed Whether the original editor (left side of the diff editor) is disabled.
     */
    public void setOriginalDisabledFramed(final boolean originalDisabledFramed) {
        this.originalDisabledFramed = originalDisabledFramed;
    }

    /**
     * @return Whether the original editor (left side of the diff editor) is read-only.
     */
    public boolean isOriginalReadOnly() {
        return originalReadOnly;
    }

    /**
     * @param originalReadOnly Whether the original editor (left side of the diff editor) is read-only.
     */
    public void setOriginalReadOnly(final boolean originalReadOnly) {
        this.originalReadOnly = originalReadOnly;
    }

    /**
     * @return Whether the original framed editor (left side of the diff editor) is read-only.
     */
    public boolean isOriginalReadOnlyFramed() {
        return originalReadOnlyFramed;
    }

    /**
     * @param originalReadOnlyFramed Whether the original framed editor (left side of the diff editor) is read-only.
     */
    public void setOriginalReadOnlyFramed(final boolean originalReadOnlyFramed) {
        this.originalReadOnlyFramed = originalReadOnlyFramed;
    }

    /**
     * @return Whether the editor is disabled and does not accept submitted values.
     */
    public boolean isDisabled() {
        return disabled;
    }

    /**
     * @param disabled Whether the editor is disabled and does not accept submitted values.
     */
    public void setDisabled(final boolean disabled) {
        this.disabled = disabled;
    }

    /**
     * @return Whether the framed editor is disabled and does not accept submitted values.
     */
    public boolean isDisabledFramed() {
        return disabledFramed;
    }

    /**
     * @param disabledFramed Whether the framed editor is disabled and does not accept submitted values.
     */
    public void setDisabledFramed(final boolean disabledFramed) {
        this.disabledFramed = disabledFramed;
    }

    /**
     * @return Whether the modified editor (right side of the diff editor) is read-only.
     */
    public boolean isReadOnly() {
        return readOnly;
    }

    /**
     * @param readOnly Whether the modified editor (right side of the diff editor) is read-only.
     */
    public void setReadOnly(final boolean readOnly) {
        this.readOnly = readOnly;
    }

    /**
     * @return Whether the modified editor (right side of the diff editor) is read-only.
     */
    public boolean isReadOnlyFramed() {
        return readOnlyFramed;
    }

    /**
     * @param readOnlyFramed Whether the modified editor (right side of the diff editor) is read-only.
     */
    public void setReadOnlyFramed(final boolean readOnlyFramed) {
        this.readOnlyFramed = readOnlyFramed;
    }

    /**
     * @return Whether the editor is required to have a value.
     */
    public boolean isRequired() {
        return required;
    }

    /**
     * @param required Whether the editor is required to have a value.
     */
    public void setRequired(final boolean required) {
        this.required = required;
    }

    /**
     * @return Whether the framed editor is required to have a value.
     */
    public boolean isRequiredFramed() {
        return requiredFramed;
    }

    /**
     * @param requiredFramed Whether the framed editor is required to have a value.
     */
    public void setRequiredFramed(final boolean requiredFramed) {
        this.requiredFramed = requiredFramed;
    }

    /**
     * @return Whether the original editor is required to have a value.
     */
    public boolean isOriginalRequired() {
        return originalRequired;
    }

    /**
     * @param originalRequired Whether the original editor is required to have a value.
     */
    public void setOriginalRequired(final boolean originalRequired) {
        this.originalRequired = originalRequired;
    }

    /**
     * @return Whether the original framed editor is required to have a value.
     */
    public boolean isOriginalRequiredFramed() {
        return originalRequiredFramed;
    }

    /**
     * @param originalRequiredFramed Whether the original framed editor is required to have a value.
     */
    public void setOriginalRequiredFramed(final boolean originalRequiredFramed) {
        this.originalRequiredFramed = originalRequiredFramed;
    }

    /**
     * @return Whether to render the diff side-by-side in two editors or inline in one editor.
     */
    public boolean isRenderSideBySide() {
        if (INLINE.equals(type)) {
            return editorOptionsDiff.isRenderSideBySide();
        }
        else {
            return editorOptionsFramedDiff.isRenderSideBySide();
        }
    }

    /**
     * @param renderSideBySide Whether to render the diff side-by-side in two editors or inline in one editor.
     */
    public void setRenderSideBySide(final boolean renderSideBySide) {
        if (INLINE.equals(type)) {
            editorOptionsDiff.setRenderSideBySide(renderSideBySide);
        }
        else {
            editorOptionsFramedDiff.setRenderSideBySide(renderSideBySide);
        }
    }

    /**
     * @param language The currently selected code language for the monaco editor.
     */
    public void setLanguage(final String language) {
        if (CODE.equals(mode)) {
            if (isEmpty(language)) {
                editorOptions.setLanguage(ELanguage.JAVASCRIPT);
            }
            else {
                editorOptions.setLanguage(language);
            }
        }
        else {
            if (isEmpty(language)) {
                editorLangDiff = ELanguage.JAVASCRIPT;
            }
            else {
                editorLangDiff = ELanguage.parseString(language);
            }
        }
    }

    /**
     * @param language The currently selected code language for the framed Monaco editor.
     */
    public void setLanguageFramed(final String language) {
        if (CODE.equals(mode)) {
            if (isEmpty(language)) {
                editorOptionsFramed.setLanguage(ELanguage.JAVASCRIPT);
            }
            else {
                editorOptionsFramed.setLanguage(language);
            }
        }
        else {
            if (isEmpty(language)) {
                editorLangFramedDiff = ELanguage.JAVASCRIPT;
            }
            else {
                editorLangFramedDiff = ELanguage.parseString(language);
            }
        }
    }

    /**
     * @param theme The current color theme of the editor.
     */
    public void setTheme(final String theme) {
        if (CODE.equals(mode)) {
            if (isEmpty(theme)) {
                editorOptions.setTheme(ETheme.VS);
            }
            else {
                editorOptions.setTheme(theme);
            }
        }
        else {
            if (isEmpty(theme)) {
                editorOptionsDiff.setTheme(ETheme.VS);
            }
            else {
                editorOptionsDiff.setTheme(theme);
            }
        }
    }

    /**
     * @param theme The current color theme of the framed editor.
     */
    public void setThemeFramed(final String theme) {
        if (CODE.equals(mode)) {
            if (isEmpty(theme)) {
                editorOptionsFramed.setTheme(ETheme.VS);
            }
            else {
                editorOptionsFramed.setTheme(theme);
            }
        }
        else {
            if (isEmpty(theme)) {
                editorOptionsFramedDiff.setTheme(ETheme.VS);
            }
            else {
                editorOptionsFramedDiff.setTheme(theme);
            }
        }
    }

    /**
     * @param type Whether to show the inline or framed version of the Monaco code editor.
     */
    public void setType(final String type) {
        this.type = type;
    }

    /**
     * @param mode Whether to show the code or diff version of the Monaco code editor.
     */
    public void setTypeMode(final String mode) {
        this.mode = mode;
    }

    /**
     * @param locale The current UI language used in the Monaco code editor.
     */
    public void setLocale(final Locale locale) {
        this.locale = locale;
    }

    /**
     * @param localeFramed The current UI language used in the framed Monaco code editor.
     */
    public void setLocaleFramed(final Locale localeFramed) {
        this.localeFramed = localeFramed;
    }

    /**
     * @param value The code currently being edited by the editor.
     */
    public void setValue(final String value) {
        this.value = value;
    }

    /**
     * @param valueDiff The code currently being edited by the editor.
     */
    public void setValueDiff(final MonacoDiffEditorModel valueDiff) {
        this.valueDiff = valueDiff;
    }

    /**
     * @param valueExtender Code for the Monaco extender that can be edited on the extender showcase page.
     */
    public void setValueExtender(final String valueExtender) {
        this.valueExtender = valueExtender;
    }

    /**
     * @param valueFramed The code currently being edited by the framed editor.
     */
    public void setValueFramed(final String valueFramed) {
        this.valueFramed = valueFramed;
    }

    /**
     * @param valueFramedDiff The code currently being edited by the framed editor.
     */
    public void setValueFramedDiff(final MonacoDiffEditorModel valueFramedDiff) {
        this.valueFramedDiff = valueFramedDiff;
    }

    /**
     * Callback for when the code language was changed. Loads the code sample for that language.
     */
    public void onLanguageChange() {
        if (INLINE.equals(type)) {
            loadDefaultCode();
        }
        else {
            loadDefaultCodeFramed();
        }
    }

    /**
     * Callback when the selected example was changed in the extender showcase.
     */
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
        final HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
                    .getRequest();
        this.extenderError = req.getParameter("monacoExtenderError");
    }

    /**
     * Loads the default code sample for the current language, if available.
     */
    private void loadDefaultCode() {
        final String language = CODE.equals(mode) ? editorOptions.getLanguage() : editorLangDiff.toString();
        final String propertyKey = "sample_code." + language;
        loadCode(propertyKey);
    }

    /**
     * Loads the default code sample for the current language, if available.
     */
    private void loadDefaultCodeFramed() {
        final String language = CODE.equals(mode) ? editorOptionsFramed.getLanguage() : editorLangFramedDiff.toString();
        final String propertyKey = "sample_code." + language;
        loadCodeFramed(propertyKey);
    }

    /**
     * Loads the code with the given key from the properties file into the code editor.
     *
     * @param propertyKey Key at which the code is stored in {@code monaco-examples.properties}
     */
    private void loadCode(final String propertyKey) {
        if (CODE.equals(mode)) {
            try {
                value = propertyKey != null ? examples.getString(propertyKey) : "";
            }
            catch (final MissingResourceException e) {
                value = "";
            }
        }
        else {
            try {
                final String original = propertyKey != null ? examples.getString(propertyKey) : "";
                final String modified = MonacoEditorSettings.deriveModifiedContent(original);
                valueDiff = new MonacoDiffEditorModel(original, modified);
            }
            catch (final MissingResourceException e) {
                valueDiff = MonacoDiffEditorModel.empty();
            }
        }
    }

    /**
     * Loads the code with the given key from the properties file into the extender editor.
     *
     * @param propertyKey Key at which the code is stored in {@code monaco-examples.properties}
     */
    private void loadCodeExtender(final String propertyKey) {
        try {
            valueExtender = propertyKey != null ? examples.getString(propertyKey) : "";
        }
        catch (final MissingResourceException e) {
            valueExtender = "";
        }
    }

    /**
     * Loads the code with the given key from the properties file into the framed editor.
     *
     * @param propertyKey Key at which the code is stored in {@code monaco-examples.properties}
     */
    private void loadCodeFramed(final String propertyKey) {
        if (CODE.equals(mode)) {
            try {
                valueFramed = propertyKey != null ? examples.getString(propertyKey) : "";
            }
            catch (final MissingResourceException e) {
                valueFramed = "";
            }
        }
        else {
            try {
                final String original = propertyKey != null ? examples.getString(propertyKey) : "";
                final String modified = MonacoEditorSettings.deriveModifiedContent(original);
                valueFramedDiff = new MonacoDiffEditorModel(original, modified);
            }
            catch (final MissingResourceException e) {
                valueFramedDiff = MonacoDiffEditorModel.empty();
            }
        }
    }

    /**
     * Loads the info about the extender example with the given key from the properties file.
     *
     * @param propertyKey Key at which the info is stored in {@code monaco-examples.properties}
     */
    private void loadExtenderInfo(final String propertyKey) {
        try {
            extenderInfo = propertyKey != null ? examples.getString(propertyKey) : "";
        }
        catch (final MissingResourceException e) {
            extenderInfo = "No info available for this extender. Check the ?example=... URL parameter.";
        }
    }

    /**
     * Loads the CSS for the extender example with the given key from the properties file.
     *
     * @param propertyKey Key at which the CSS is stored in {@code monaco-examples.properties}
     */
    private void loadExtenderCss(final String propertyKey) {
        try {
            valueCss = propertyKey != null ? examples.getString(propertyKey) : "";
        }
        catch (final MissingResourceException e) {
            valueCss = "";
        }
    }

    /**
     * Loads the code language for the extender example with the given key from the properties file.
     *
     * @param propertyKey Key at which the code language is stored in {@code monaco-examples.properties}
     */
    private void loadExtenderLanguage(final String propertyKey) {
        try {
            String language = propertyKey != null ? examples.getString(propertyKey) : "";
            language = StringUtils.defaultIfBlank(language, "plaintext");
            if (CODE.equals(mode)) {
                try {
                    editorOptions.setLanguage(language);
                }
                catch (final MissingResourceException e) {
                    editorOptions.setLanguage(ELanguage.TYPESCRIPT);
                }
            }
            else {
                try {
                    editorLangDiff = ELanguage.parseString(language);
                }
                catch (final MissingResourceException e) {
                    editorLangDiff = ELanguage.TYPESCRIPT;
                }
            }
        }
        catch (final MissingResourceException e) {
            if (CODE.equals(mode)) {
                editorOptions.setLanguage(ELanguage.PLAINTEXT);
            }
            else {
                editorLangDiff = ELanguage.PLAINTEXT;
            }
        }
    }

    /**
     * Loads the name of the extender example with the given key from the properties file.
     *
     * @param propertyKey Key at which the name is stored in {@code monaco-examples.properties}
     */
    private void loadExtenderName(final String propertyKey) {
        try {
            extenderName = propertyKey != null ? examples.getString(propertyKey) : "";
        }
        catch (final MissingResourceException e) {
            extenderName = "Unknown extender";
        }
    }

    /**
     * Loads the given example for the extender showcase.
     *
     * @param key Key of the extender example.
     */
    public void loadExtenderExample(final String key) {
        loadCode(getExtenderExampleKey(key, "sample"));
        loadCodeExtender(getExtenderExampleKey(key, "script"));
        loadExtenderName(getExtenderExampleKey(key, "name"));
        loadExtenderInfo(getExtenderExampleKey(key, "info"));
        loadExtenderCss(getExtenderExampleKey(key, "css"));
        loadExtenderLanguage(getExtenderExampleKey(key, "language"));
    }

    /**
     * Demo submit to show that the data can be transferred to the server. Shows a message with the current code.
     */
    public void submitContent() {
        if (CODE.equals(mode)) {
            final String content = INLINE.equals(type) ? value : valueFramed;
            final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success",
                        "MonacoEditor content: " + abbreviate(content, "...", 300));
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        else {
            final MonacoDiffEditorModel value = INLINE.equals(type) ? valueDiff : valueFramedDiff;
            final String left = abbreviate(value.getOriginalValue(), "...", 150);
            final String right = abbreviate(value.getModifiedValue(), "...", 150);
            final FacesMessage msg1 = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success",
                        "MonacoEditor content original: " + left);
            final FacesMessage msg2 = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success",
                        "MonacoEditor content modified: " + right);
            FacesContext.getCurrentInstance().addMessage(null, msg1);
            FacesContext.getCurrentInstance().addMessage(null, msg2);
        }
    }

    /**
     * Set the initial values for the {@code basicUsage} showcase.
     *
     * @param mode Whether to use the code of diff editor.
     */
    public void initBasicUsage(final String mode) {
        this.mode = mode;
        if (CODE.equals(mode)) {
            editorOptions.setLanguage(ELanguage.TYPESCRIPT);
            editorOptionsFramed.setLanguage(ELanguage.TYPESCRIPT);
        }
        else {
            editorLangDiff = ELanguage.TYPESCRIPT;
            editorLangFramedDiff = ELanguage.TYPESCRIPT;
        }
        loadDefaultCode();
        loadDefaultCodeFramed();
    }

    /**
     * Set the initial values for the {@code customTheme} showcase.
     *
     * @param mode Whether to use the code of diff editor.
     */
    public void initCustomTheme(final String mode) {
        this.mode = mode;
        customThemes = singletonMap(MY_CUSTOM_THEME, MonacoEditorSettings.createDemoCustomTheme());
        if (CODE.equals(mode)) {
            editorOptions.setLanguage(ELanguage.HTML);
            editorOptions.setTheme(MY_CUSTOM_THEME);
            editorOptionsFramed.setLanguage(ELanguage.HTML);
            editorOptionsFramed.setTheme(MY_CUSTOM_THEME);
        }
        else {
            editorLangDiff = ELanguage.HTML;
            editorOptionsDiff.setTheme(MY_CUSTOM_THEME);
            editorLangFramedDiff = ELanguage.HTML;
            editorOptionsFramedDiff.setTheme(MY_CUSTOM_THEME);
        }
        loadCode("custom_code.htmlCustomTheme");
        loadCodeFramed("custom_code.htmlCustomTheme");
    }

    /**
     * Set the initial values for the {@code customLocalization} showcase.
     *
     * @param mode Whether to use the code of diff editor.
     */
    public void initCustomLocalization(final String mode) {
        this.mode = mode;
        if (CODE.equals(mode)) {
            editorOptions.setLanguage(ELanguage.TYPESCRIPT);
            editorOptionsFramed.setLanguage(ELanguage.TYPESCRIPT);
        }
        else {
            editorLangDiff = ELanguage.TYPESCRIPT;
            editorLangFramedDiff = ELanguage.TYPESCRIPT;
        }
        loadDefaultCode();
        loadDefaultCodeFramed();
    }

    /**
     * Set the initial values for the {@code events} showcase.
     *
     * @param mode Whether to use the code of diff editor.
     */
    public void initEvents(final String mode) {
        this.mode = mode;
        if (CODE.equals(mode)) {
            editorOptions.setLanguage(ELanguage.TYPESCRIPT);
            editorOptionsFramed.setLanguage(ELanguage.TYPESCRIPT);
        }
        else {
            editorLangDiff = ELanguage.TYPESCRIPT;
            editorLangFramedDiff = ELanguage.TYPESCRIPT;
        }
        loadDefaultCode();
        loadDefaultCodeFramed();
    }

    /**
     * Set the initial values for the {@code extender} showcase.
     *
     * @param mode Whether to use the code of diff editor.
     */
    public void initExtender(final String mode) {
        this.mode = mode;
        if (CODE.equals(mode)) {
            editorOptions.setLanguage(ELanguage.JAVASCRIPT);
        }
        else {
            editorLangExtenderDiff = ELanguage.JAVASCRIPT;
        }
        final String requested = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
                    .get("example");
        if (CODE.equals(mode)) {
            extenderExample = StringUtils.defaultIfBlank(requested, "jquery");
        }
        else {
            extenderExample = StringUtils.defaultIfBlank(requested, "jquery");
        }
        loadExtenderExample(extenderExample);
    }

    /**
     * @param exampleName Name of the example.
     * @param subType Type of the data to get, e.g. {@code name} or {@code description}.
     * @return The key for the given extender example property.
     */
    private String getExtenderExampleKey(final String exampleName, final String subType) {
        if (CODE.equals(mode)) {
            return CUSTOM_CODE_EXTENDER_CODE + exampleName + "." + subType;
        }
        else {
            return CUSTOM_CODE_EXTENDER_DIFF + exampleName + "." + subType;
        }
    }
}
