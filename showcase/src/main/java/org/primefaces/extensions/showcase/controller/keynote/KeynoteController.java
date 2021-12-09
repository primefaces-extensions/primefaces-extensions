/*
 * Copyright (c) 2011-2021 PrimeFaces Extensions
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
package org.primefaces.extensions.showcase.controller.keynote;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.extensions.event.KeynoteEvent;
import org.primefaces.extensions.model.keynote.KeynoteItem;

@Named
@ViewScoped
public class KeynoteController implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<KeynoteItem> items;

    private boolean disabled = true;

    @PostConstruct
    protected void initialize() {
        items = new ArrayList<KeynoteItem>();

        for (int j = 0; j < 3; j++) {
            for (int i = 1; i <= 10; i++) {
                items.add(new KeynoteItem(new KeynoteItemContent("Section " + i, "Lorem ipsum content " + i)));
            }
        }
    }

    public void onSlideChanged(final KeynoteEvent event) {
        disabled = !event.isLastSlide();
        final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Trigger: " + new Date(),
                    "Slide changed: " + event.isCompleted() + ", last slide: " + event.isLastSlide());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onSlideTransitionEnd(final KeynoteEvent event) {
        disabled = !event.isLastSlide();
        final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Trigger: " + new Date(),
                    "Slide transition end: " + event.isCompleted() + ", last slide: " + event.isLastSlide());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public List<KeynoteItem> getItems() {
        return items;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public class KeynoteItemContent implements Serializable {

        private String headline;
        private String content;

        public KeynoteItemContent(String headline, String content) {
            this.headline = headline;
            this.content = content;
        }

        public String getHeadline() {
            return headline;
        }

        public void setHeadline(String headline) {
            this.headline = headline;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

    }

}
