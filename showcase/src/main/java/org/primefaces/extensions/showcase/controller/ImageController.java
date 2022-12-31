/*
 * Copyright (c) 2011-2023 PrimeFaces Extensions
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
