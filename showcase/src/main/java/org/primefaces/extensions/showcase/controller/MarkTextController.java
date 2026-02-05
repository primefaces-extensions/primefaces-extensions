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

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

/**
 * MarkText Controller.
 *
 * @author jxmai
 */
@Named
@ViewScoped
public class MarkTextController implements Serializable {

    private static final long serialVersionUID = 1L;

    private String searchTerm = "ipsum";

    private String searchTerm2 = "dolor";

    private String customStyleClass = "custom-highlight";

    private String searchTermCaseSensitivity = "Ut";

    private String searchTermSeparateWord = "search is there";

    private String searchTermAccuracy = "am";

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public String getSearchTerm2() {
        return searchTerm2;
    }

    public void setSearchTerm2(String searchTerm2) {
        this.searchTerm2 = searchTerm2;
    }

    public String getCustomStyleClass() {
        return customStyleClass;
    }

    public void setCustomStyleClass(String customStyleClass) {
        this.customStyleClass = customStyleClass;
    }

    public String getSearchTermCaseSensitivity() {
        return searchTermCaseSensitivity;
    }

    public void setSearchTermCaseSensitivity(String searchTermCaseSensitivity) {
        this.searchTermCaseSensitivity = searchTermCaseSensitivity;
    }

    public String getSearchTermSeparateWord() {
        return searchTermSeparateWord;
    }

    public void setSearchTermSeparateWord(String searchTermSeparateWord) {
        this.searchTermSeparateWord = searchTermSeparateWord;
    }

    public String getSearchTermAccuracy() {
        return searchTermAccuracy;
    }

    public void setSearchTermAccuracy(String searchTermAccuracy) {
        this.searchTermAccuracy = searchTermAccuracy;
    }

    public void updateSearch() {
        // Update the mark when search term changes
    }
}