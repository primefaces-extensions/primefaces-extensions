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
package org.primefaces.extensions.model.monacoeditor;

import java.io.Serializable;

import org.primefaces.shaded.owasp.encoder.Encode;

/**
 * Immutable Model for the Monaco diff code editor that encapsulates two strings, corresponding to the value of the original and modified editor.
 * 
 * @since 11.1.0
 */
public class MonacoDiffEditorModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private final static MonacoDiffEditorModel EMPTY = new MonacoDiffEditorModel();

    private final String originalValue;

    private final String modifiedValue;

    public MonacoDiffEditorModel() {
        this.originalValue = "";
        this.modifiedValue = "";
    }

    public MonacoDiffEditorModel(String originalValue, String modifiedValue) {
        this.originalValue = originalValue != null ? originalValue : "";
        this.modifiedValue = modifiedValue != null ? modifiedValue : "";
    }

    public MonacoDiffEditorModel withOriginal(String originalValue) {
        return new MonacoDiffEditorModel(originalValue, modifiedValue);
    }

    public MonacoDiffEditorModel withModified(String modifiedValue) {
        return new MonacoDiffEditorModel(originalValue, modifiedValue);
    }

    /**
     * @return The original text against which perform the comparison.
     */
    public String getOriginalValue() {
        return originalValue;
    }

    /**
     * @return The modified text to compare against the original text.
     */
    public String getModifiedValue() {
        return modifiedValue;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((modifiedValue == null) ? 0 : modifiedValue.hashCode());
        result = prime * result + ((originalValue == null) ? 0 : originalValue.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        MonacoDiffEditorModel other = (MonacoDiffEditorModel) obj;
        if (modifiedValue == null) {
            if (other.modifiedValue != null) {
                return false;
            }
        }
        else if (!modifiedValue.equals(other.modifiedValue)) {
            return false;
        }
        if (originalValue == null) {
            if (other.originalValue != null) {
                return false;
            }
        }
        else if (!originalValue.equals(other.originalValue)) {
            return false;
        }
        return true;
    }

    /**
     * @return Whether this model is empty, e.g. both the original and modified values are empty.
     */
    public boolean isEmpty() {
        return originalValue != null && !originalValue.isEmpty() && modifiedValue != null && !modifiedValue.isEmpty();
    }

    @Override
    public String toString() {
        return "[\"" + Encode.forJava(originalValue) + "\",\"" + Encode.forJava(modifiedValue) + "\"]";
    }

    /**
     * @return An empty model with no value.
     */
    public static MonacoDiffEditorModel empty() {
        return EMPTY;
    }
}
