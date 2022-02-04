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
package org.primefaces.extensions.component.monacoeditor;

import java.util.Collection;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.BehaviorEvent;

import org.primefaces.extensions.model.monacoeditor.DiffEditorOptions;
import org.primefaces.extensions.model.monacoeditor.MonacoDiffEditorModel;
import org.primefaces.util.MapBuilder;
import org.primefaces.util.MessageFactory;

/**
 * Base component for both the framed and inline monaco diff code editor widget.
 *
 * @since 10.0.0
 */
@SuppressWarnings("java:S110")
public abstract class MonacoDiffEditorBase extends MonacoEditorCommon<DiffEditorOptions> {
    static final String DEFAULT_EVENT = "change";

    static final Map<String, Class<? extends BehaviorEvent>> BASE_BEHAVIOR_EVENT_MAPPING = MapBuilder.<String, Class<? extends BehaviorEvent>> builder() //
                .put(DEFAULT_EVENT, null) //
                .put("initialized", null) //
                .put("blur", null) //
                .put("focus", null) //
                .put("keydown", null) //
                .put("keyup", null) //
                .put("mousedown", null) //
                .put("mousemove", null) //
                .put("mouseup", null) //
                .put("paste", null) //
                .put("originalBlur", null) //
                .put("originalChange", null) //
                .put("originalFocus", null) //
                .put("originalKeydown", null) //
                .put("originalKeyup", null) //
                .put("originalMousedown", null) //
                .put("originalMousemove", null) //
                .put("originalMouseup", null) //
                .put("originalPaste", null) //
                .build();

    static final Collection<String> BASE_EVENT_NAMES = BASE_BEHAVIOR_EVENT_MAPPING.keySet();

    static final boolean DEFAULT_ORIGINAL_DISABLED = true;
    static final boolean DEFAULT_ORIGINAL_READONLY = false;
    static final boolean DEFAULT_ORIGINAL_REQUIRED = false;
    static final String DEFAULT_ORIGINAL_TABINDEX = "";
    static final String DEFAULT_ORIGINAL_BASENAME = "";
    static final String DEFAULT_ORIGINAL_EXTENSION = "";
    static final String DEFAULT_ORIGINAL_DIRECTORY = "";
    static final String DEFAULT_ORIGINAL_SCHEME = "inmemory";
    static final String DEFAULT_ORIGINAL_LANGUAGE = null;

    protected MonacoDiffEditorBase(String rendererType) {
        super(rendererType, DiffEditorOptions.class);
    }

    public final boolean isOriginalDisabled() {
        return (Boolean) getStateHelper().eval(DiffEditorPropertyKeys.originalDisabled, true);
    }

    public final void setOriginalDisabled(final boolean originalEditable) {
        getStateHelper().put(DiffEditorPropertyKeys.originalDisabled, originalEditable);
    }

    public final boolean isOriginalReadonly() {
        return (Boolean) getStateHelper().eval(DiffEditorPropertyKeys.originalReadonly, false);
    }

    public final void setOriginalReadonly(final boolean originalReadonly) {
        getStateHelper().put(DiffEditorPropertyKeys.originalReadonly, originalReadonly);
    }

    public final boolean isOriginalRequired() {
        return (Boolean) getStateHelper().eval(DiffEditorPropertyKeys.originalRequired, false);
    }

    public final void setOriginalRequired(final boolean originalRequired) {
        getStateHelper().put(DiffEditorPropertyKeys.originalRequired, originalRequired);
    }

    @Override
    public final String getDefaultEventName() {
        return DEFAULT_EVENT;
    }

    public String getLanguage() {
        return (String) getStateHelper().eval(DiffEditorPropertyKeys.language, null);
    }

    // We allow both a string and an instance of ELanguage
    public void setLanguage(Object language) {
        getStateHelper().put(DiffEditorPropertyKeys.language, language != null ? language.toString() : null);
    }

    public String getOriginalLanguage() {
        return (String) getStateHelper().eval(DiffEditorPropertyKeys.originalLanguage, null);
    }

    // We allow both a string and an instance of ELanguage
    public void setOriginalLanguage(Object originalLanguage) {
        getStateHelper().put(DiffEditorPropertyKeys.originalLanguage, originalLanguage != null ? originalLanguage.toString() : null);
    }

    public String getOnoriginalblur() {
        return (String) getStateHelper().eval(DiffEditorPropertyKeys.onoriginalblur, null);
    }

    public final void setOnoriginalblur(final String onoriginalblur) {
        getStateHelper().put(DiffEditorPropertyKeys.onoriginalblur, onoriginalblur);
    }

    public String getOnoriginalchange() {
        return (String) getStateHelper().eval(DiffEditorPropertyKeys.onoriginalchange, null);
    }

    public final void setOnoriginalchange(final String onoriginalchange) {
        getStateHelper().put(DiffEditorPropertyKeys.onoriginalchange, onoriginalchange);
    }

    public String getOnoriginalfocus() {
        return (String) getStateHelper().eval(DiffEditorPropertyKeys.onoriginalfocus, null);
    }

    public final void setOnoriginalfocus(final String onoriginalfocus) {
        getStateHelper().put(DiffEditorPropertyKeys.onoriginalfocus, onoriginalfocus);
    }

    public String getOnoriginalkeyup() {
        return (String) getStateHelper().eval(DiffEditorPropertyKeys.onoriginalkeyup, null);
    }

    public final void setOnoriginalkeyup(final String onoriginalkeyup) {
        getStateHelper().put(DiffEditorPropertyKeys.onoriginalkeyup, onoriginalkeyup);
    }

    public String getOnoriginalkeydown() {
        return (String) getStateHelper().eval(DiffEditorPropertyKeys.onoriginalkeydown, null);
    }

    public final void setOnoriginalkeydown(final String onoriginalkeydown) {
        getStateHelper().put(DiffEditorPropertyKeys.onoriginalkeydown, onoriginalkeydown);
    }

    public String getOnoriginalmouseup() {
        return (String) getStateHelper().eval(DiffEditorPropertyKeys.onoriginalmouseup, null);
    }

    public final void setOnoriginalmouseup(final String onoriginalmouseup) {
        getStateHelper().put(DiffEditorPropertyKeys.onoriginalmouseup, onoriginalmouseup);
    }

    public String getOnoriginalmousedown() {
        return (String) getStateHelper().eval(DiffEditorPropertyKeys.onoriginalmousedown, null);
    }

    public final void setOnoriginalmousedown(final String onoriginalmousedown) {
        getStateHelper().put(DiffEditorPropertyKeys.onoriginalmousedown, onoriginalmousedown);
    }

    public String getOnoriginalmousemove() {
        return (String) getStateHelper().eval(DiffEditorPropertyKeys.onoriginalmousemove, null);
    }

    public final void setOnoriginalmousemove(final String onoriginalmousemove) {
        getStateHelper().put(DiffEditorPropertyKeys.onoriginalmousemove, onoriginalmousemove);
    }

    public String getOnoriginalpaste() {
        return (String) getStateHelper().eval(DiffEditorPropertyKeys.onoriginalpaste, null);
    }

    public final void setOnoriginalpaste(final String onoriginalpaste) {
        getStateHelper().put(DiffEditorPropertyKeys.onoriginalpaste, onoriginalpaste);
    }

    public final String getOriginalDirectory() {
        return (String) getStateHelper().eval(DiffEditorPropertyKeys.originalDirectory, DEFAULT_ORIGINAL_DIRECTORY);
    }

    public final void setOriginalDirectory(String originalDirectory) {
        getStateHelper().put(DiffEditorPropertyKeys.originalDirectory, originalDirectory);
    }

    public final String getOriginalExtension() {
        return (String) getStateHelper().eval(DiffEditorPropertyKeys.originalExtension, DEFAULT_ORIGINAL_EXTENSION);
    }

    public final void setOriginalExtension(String originalExtension) {
        getStateHelper().put(DiffEditorPropertyKeys.originalExtension, originalExtension);
    }

    public final String getOriginalBasename() {
        return (String) getStateHelper().eval(DiffEditorPropertyKeys.originalBasename, DEFAULT_ORIGINAL_BASENAME);
    }

    public final void setOriginalBasename(String basename) {
        getStateHelper().put(DiffEditorPropertyKeys.originalBasename, basename);
    }

    public final String getOriginalScheme() {
        return (String) getStateHelper().eval(DiffEditorPropertyKeys.originalScheme, DEFAULT_ORIGINAL_SCHEME);
    }

    public final void setOriginalScheme(String originalScheme) {
        getStateHelper().put(DiffEditorPropertyKeys.originalScheme, originalScheme);
    }

    @Override
    protected void validateValue(FacesContext context, Object newValue) {
        final MonacoDiffEditorModel model = (MonacoDiffEditorModel) newValue;
        // If our value is valid, enforce the required property if present for the modified editor
        if (isValid() && isRequired() && (model == null || isEmpty(model.getModifiedValue()))) {
            String requiredMessageStr = getRequiredMessage();
            FacesMessage message;
            if (null != requiredMessageStr) {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, requiredMessageStr, requiredMessageStr);
            }
            else {
                final Object label = MessageFactory.getLabel(context, this);
                message = MessageFactory.getFacesMessage(REQUIRED_MESSAGE_ID, FacesMessage.SEVERITY_ERROR, label.toString());
            }
            context.addMessage(getClientId(context), message);
            setValid(false);
        }

        // If our value is valid, enforce the required property if present for the original edtor
        if (isValid() && isOriginalRequired() && (model == null || isEmpty(model.getOriginalValue()))) {
            String requiredMessageStr = getRequiredMessage();
            FacesMessage message;
            if (null != requiredMessageStr) {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, requiredMessageStr, requiredMessageStr);
            }
            else {
                final Object label = MessageFactory.getLabel(context, this);
                message = MessageFactory.getFacesMessage(REQUIRED_MESSAGE_ID, FacesMessage.SEVERITY_ERROR, label.toString());
            }
            context.addMessage(getClientId(context), message);
            setValid(false);
        }

        // Call super which calls the validators if our value is valid
        super.validateValue(context, newValue);
    }
}
