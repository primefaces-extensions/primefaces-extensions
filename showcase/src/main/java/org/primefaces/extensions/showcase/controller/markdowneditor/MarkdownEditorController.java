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
package org.primefaces.extensions.showcase.controller.markdowneditor;

import java.io.Serializable;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named
@ViewScoped
public class MarkdownEditorController implements Serializable {

    private static final long serialVersionUID = 1L;

    private String markdown;
    private boolean rtl;

    @PostConstruct
    public void init() {
        this.markdown = "Markdown editing with **inline** styling!";
        this.rtl = false;
    }

    public void addMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
    }

    public void submit() {
        addMessage(getMarkdown());
    }

    public void onPaste(AjaxBehaviorEvent e) {
        addMessage("Paste event.");
    }

    public void onCopy(AjaxBehaviorEvent e) {
        addMessage("Copy event.");
    }

    public void onCut(AjaxBehaviorEvent e) {
        addMessage("Cut event.");
    }

    public String getMarkdown() {
        return this.markdown;
    }

    public void setMarkdown(String markdown) {
        this.markdown = markdown;
    }

    public boolean isRtl() {
        return rtl;
    }

    public void setRtl(final boolean rtl) {
        this.rtl = rtl;
    }

}
