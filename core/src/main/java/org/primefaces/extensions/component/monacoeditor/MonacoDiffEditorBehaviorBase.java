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
package org.primefaces.extensions.component.monacoeditor;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

import org.primefaces.extensions.util.MessageFactory;

/**
 * Behavior base for Monaco diff editor (validation and language setters). Event mapping is declared via {@link org.primefaces.cdk.api.FacesBehaviorEvents} on
 * {@link MonacoDiffEditorBase}.
 *
 * @since 11.1.0
 */
public abstract class MonacoDiffEditorBehaviorBase extends MonacoDiffEditorBaseImpl {

    private static final String REQUIRED_MESSAGE_ID = "jakarta.faces.component.UIInput.REQUIRED";

    static final boolean DEFAULT_ORIGINAL_DISABLED = true;
    static final boolean DEFAULT_ORIGINAL_READONLY = false;
    static final boolean DEFAULT_ORIGINAL_REQUIRED = false;
    static final String DEFAULT_ORIGINAL_BASENAME = "";
    static final String DEFAULT_ORIGINAL_EXTENSION = "";
    static final String DEFAULT_ORIGINAL_DIRECTORY = "";
    static final String DEFAULT_ORIGINAL_SCHEME = "inmemory";
    static final String DEFAULT_ORIGINAL_LANGUAGE = null;

    /**
     * Allow both string and ELanguage instance for language attribute.
     */
    public void setLanguage(final Object language) {
        getStateHelper().put("language", language != null ? language.toString() : null);
    }

    /**
     * Allow both string and ELanguage instance for originalLanguage attribute.
     */
    public void setOriginalLanguage(final Object originalLanguage) {
        getStateHelper().put("originalLanguage",
                    originalLanguage != null ? originalLanguage.toString() : null);
    }

    @Override
    protected void validateValue(final FacesContext context, final Object newValue) {
        final org.primefaces.extensions.model.monaco.MonacoDiffEditorModel model = (org.primefaces.extensions.model.monaco.MonacoDiffEditorModel) newValue;
        if (isValid() && isRequired() && (model == null || isEmpty(model.getModifiedValue()))) {
            final String requiredMessageStr = getRequiredMessage();
            final FacesMessage message;
            if (null != requiredMessageStr) {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, requiredMessageStr, requiredMessageStr);
            }
            else {
                final Object label = MessageFactory.getLabel(context, this);
                message = MessageFactory.getMessage(REQUIRED_MESSAGE_ID, FacesMessage.SEVERITY_ERROR, label);
            }
            context.addMessage(getClientId(context), message);
            setValid(false);
        }

        if (isValid() && isOriginalRequired() && (model == null || isEmpty(model.getOriginalValue()))) {
            final String requiredMessageStr = getRequiredMessage();
            final FacesMessage message;
            if (null != requiredMessageStr) {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, requiredMessageStr, requiredMessageStr);
            }
            else {
                final Object label = MessageFactory.getLabel(context, this);
                message = MessageFactory.getMessage(REQUIRED_MESSAGE_ID, FacesMessage.SEVERITY_ERROR, label);
            }
            context.addMessage(getClientId(context), message);
            setValid(false);
        }

        super.validateValue(context, newValue);
    }
}
