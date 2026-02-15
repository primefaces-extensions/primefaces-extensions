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
package org.primefaces.extensions.showcase.controller.marktext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.extensions.event.MarkEvent;
import org.primefaces.extensions.model.marktext.MarkPosition;

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

    private String searchTermUpdate = "lorem";

    private String replaceTerm = "ipsum";

    private String processedText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
                + " Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."
                + " Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur."
                + " Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
                + "\n\nPrimeFaces Extensions provides additional components for PrimeFaces."
                + " MarkText is a new component that highlights search terms within specified containers using mark.js.";

    private List<String> lastMatchedTerms = new ArrayList<>();

    private List<MarkPosition> lastPositions = new ArrayList<>();

    private String lastMatchedTermsJson;

    private String lastPositionsJson;

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

    public String getSearchTermUpdate() {
        return searchTermUpdate;
    }

    public void setSearchTermUpdate(String searchTermUpdate) {
        this.searchTermUpdate = searchTermUpdate;
    }

    public String getReplaceTerm() {
        return replaceTerm;
    }

    public void setReplaceTerm(String replaceTerm) {
        this.replaceTerm = replaceTerm;
    }

    public String getProcessedText() {
        return processedText;
    }

    public void setProcessedText(String processedText) {
        this.processedText = processedText;
    }

    public List<String> getLastMatchedTerms() {
        return lastMatchedTerms;
    }

    public List<MarkPosition> getLastPositions() {
        return lastPositions;
    }

    public void onHighlight(MarkEvent event) {
        this.lastMatchedTerms = event.getMatchedTerms();
        this.lastPositions = event.getPositions();

        this.lastMatchedTermsJson = lastMatchedTerms.toString();
        this.lastPositionsJson = lastPositions.stream()
                    .map(MarkPosition::toString)
                    .collect(Collectors.joining(", "));

        FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Mark Event Triggered",
                                "Found " + lastMatchedTerms.size() + " matched terms and " + lastPositions.size() + " positions."));
    }

    public String getProcessedTextFirstPart() {
        return processedText.split("\n\n")[0];
    }

    public String getProcessedTextSecondPart() {
        return processedText.split("\n\n")[1];
    }

    public String getLastMatchedTermsJson() {
        return lastMatchedTermsJson;
    }

    public void setLastMatchedTermsJson(String lastMatchedTermsJson) {
        this.lastMatchedTermsJson = lastMatchedTermsJson;
    }

    public String getLastPositionsJson() {
        return lastPositionsJson;
    }

    public void setLastPositionsJson(String lastPositionsJson) {
        this.lastPositionsJson = lastPositionsJson;
    }
}
