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
package org.primefaces.extensions.showcase.controller;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 * EditorController
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 */
@Named
@ViewScoped
public class EditorController implements Serializable {

    private static final long serialVersionUID = 20111020L;

    private String content;
    private String secondContent;
    private String color = "#33fc14";

    public EditorController() {
        content = "Hi Showcase User";
        secondContent = "This is a second editor";
    }

    public void saveListener() {
        content = content.replaceAll("\\r|\\n", "");

        final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Content",
                    content.length() > 150 ? content.substring(0, 100) : content);

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void secondSaveListener() {
        secondContent = secondContent.replaceAll("\\r|\\n", "");

        final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Second Content",
                    secondContent.length() > 150 ? secondContent.substring(0, 100) : secondContent);

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void changeColor() {
        if ("#1433FC".equals(color)) {
            color = "#33fc14";
        }
        else {
            color = "#1433FC";
        }
    }

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public String getColor() {
        return color;
    }

    public void setColor(final String color) {
        this.color = color;
    }

    public String getSecondContent() {
        return secondContent;
    }

    public void setSecondContent(final String secondContent) {
        this.secondContent = secondContent;
    }
}
