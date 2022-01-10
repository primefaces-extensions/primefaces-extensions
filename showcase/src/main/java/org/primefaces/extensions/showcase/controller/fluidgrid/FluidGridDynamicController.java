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
package org.primefaces.extensions.showcase.controller.fluidgrid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.extensions.event.LayoutCompleteEvent;
import org.primefaces.extensions.model.fluidgrid.FluidGridItem;
import org.primefaces.extensions.showcase.model.fluidgrid.Image;

/**
 * FluidGridDynamicController
 *
 * @author Oleg Varaksin / last modified by $Author: $
 * @version $Revision: 1.0 $
 */
@Named
@ViewScoped
public class FluidGridDynamicController implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<FluidGridItem> images;

    @PostConstruct
    protected void initialize() {
        images = new ArrayList<FluidGridItem>();

        for (int j = 0; j < 3; j++) {
            for (int i = 1; i <= 10; i++) {
                images.add(new FluidGridItem(new Image(i + ".jpeg")));
            }
        }
    }

    public List<FluidGridItem> getImages() {
        return images;
    }

    public void fireLayoutComplete(final LayoutCompleteEvent event) {
        final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fluid grid has been laid out", null);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
