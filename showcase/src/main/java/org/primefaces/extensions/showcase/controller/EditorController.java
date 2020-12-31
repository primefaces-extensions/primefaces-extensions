/*
 * Copyright 2011-2021 PrimeFaces Extensions
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
        if (color.equals("#1433FC")) {
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
