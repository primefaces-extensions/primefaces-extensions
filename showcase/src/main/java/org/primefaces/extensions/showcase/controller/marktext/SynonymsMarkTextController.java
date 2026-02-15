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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.extensions.event.MarkEvent;
import org.primefaces.extensions.model.marktext.MarkPosition;

/**
 * Synonyms MarkText Controller.
 *
 * @author jxmai
 */
@Named
@ViewScoped
public class SynonymsMarkTextController implements Serializable {

    private static final long serialVersionUID = 1L;

    private String searchTerm = "one";

    private Map<String, String> synonyms = new HashMap<String, String>() {
        {
            put("one", "1");
            put("two", "2");
            put("three", "3");
            put("four", "4");
            put("five", "5");
        }
    };

    private String processedText = "Counting is easy: one, two, three, four, five. "
                + "Numbers: 1, 2, 3, 4, 5. "
                + "You can use either words or digits: one (1), two (2), three (3). "
                + "This demonstrates synonyms highlighting both ways.";

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

    public Map<String, String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(Map<String, String> synonyms) {
        this.synonyms = synonyms;
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