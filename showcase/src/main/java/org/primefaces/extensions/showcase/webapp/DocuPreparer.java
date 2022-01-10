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
package org.primefaces.extensions.showcase.webapp;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.FacesException;
import javax.inject.Named;

import org.primefaces.extensions.showcase.model.system.DocuAttribute;
import org.primefaces.extensions.showcase.model.system.DocuEvent;
import org.primefaces.extensions.showcase.model.system.DocuTag;
import org.primefaces.extensions.showcase.util.TagLibParser;

/**
 * Prepares the documentation from the tag library.
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 */
@ApplicationScoped
@Named
public class DocuPreparer {

    private Map<String, DocuTag> tags;

    @PostConstruct
    protected void initialize() {
        try {
            tags = TagLibParser.getTags();
        }
        catch (final Exception e) {
            throw new FacesException("Taglib parsing failed!", e);
        }
    }

    public List<DocuAttribute> getDocuAttributes(final String tagName) {
        if (tagName == null || tags == null) {
            return null;
        }

        final DocuTag docuTag = tags.get(tagName);
        if (docuTag == null) {
            return null;
        }

        return docuTag.getAttributes();
    }

    public List<DocuEvent> getDocuEvents(final String tagName) {
        if (tagName == null || tags == null) {
            return null;
        }

        final DocuTag docuTag = tags.get(tagName);
        if (docuTag == null) {
            return null;
        }

        return docuTag.getEvents();
    }
}
