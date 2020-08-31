/*
 * Copyright 2011-2020 PrimeFaces Extensions
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

import org.primefaces.extensions.event.ImageAreaSelectEvent;
import org.primefaces.extensions.event.ResizeEvent;
import org.primefaces.extensions.event.RotateEvent;

/**
 * ImageController
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 */
@Named
@ViewScoped
public class ImageController implements Serializable {

    private static final long serialVersionUID = 20111020L;

    private String imageUrl;

    public ImageController() {
        imageUrl = "/resources/images/testImage1.png";
    }

    public void selectEndListener(final ImageAreaSelectEvent e) {
        final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Area selected",
                    "X1: " + e.getX1() + ", X2: " + e.getX2() + ", Y1: " + e.getY1() + ", Y2: " + e.getY2()
                                + ", Image width: " + e.getImgWidth() + ", Image height: " + e.getImgHeight());

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void rotateListener(final RotateEvent e) {
        final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Image rotated",
                    "Degree:" + e.getDegree());

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void resizeListener(final ResizeEvent e) {
        final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Image resized",
                    "Width:" + e.getWidth() + ", Height: " + e.getHeight());

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(final String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
