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

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.extensions.event.MarkEvent;
import org.primefaces.extensions.model.marktext.MarkPosition;

/**
 * MarkText Wildcard Controller. Provides backend for the wildcard example in the showcase.
 *
 * @author jxmai
 */
@Named
@ViewScoped
public class MarkTextWildcardController implements Serializable {

    private static final long serialVersionUID = 1L;

    private String searchTerm = "r*n";

    private String wildcards = "enabled"; // disabled | enabled | withSpaces

    private String processedText = "The quick brown fox jumps over the lazy dog. "
                + "Running is essential for fitness. "
                + "Programming and design go hand in hand.";

    private List<String> lastMatchedTerms = new ArrayList<>();

    private List<MarkPosition> lastPositions = new ArrayList<>();

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public String getWildcards() {
        return wildcards;
    }

    public void setWildcards(String wildcards) {
        this.wildcards = wildcards;
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

        FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Mark Event Triggered",
                                "Found " + lastMatchedTerms.size() + " matched terms and " + lastPositions.size() + " positions."));
    }
}
