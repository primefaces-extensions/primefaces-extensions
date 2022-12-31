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
package org.primefaces.extensions.showcase.controller.fluidgrid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.extensions.model.fluidgrid.FluidGridItem;
import org.primefaces.extensions.showcase.model.fluidgrid.DynamicField;

/**
 * FluidGridDynaFormController
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 */
@Named
@ViewScoped
public class FluidGridDynaFormController implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<FluidGridItem> items;

    @PostConstruct
    protected void initialize() {
        items = new ArrayList<FluidGridItem>();

        final List<SelectItem> selectItems = new ArrayList<SelectItem>();
        selectItems.add(new SelectItem("1", "Label 1"));
        selectItems.add(new SelectItem("2", "Label 2"));
        selectItems.add(new SelectItem("3", "Label 3"));

        items.add(new FluidGridItem(new DynamicField("First Label", null, true, null), "input"));
        items.add(new FluidGridItem(new DynamicField("Second Label", "Some default value", false, null), "input"));
        items.add(new FluidGridItem(new DynamicField("Third Label", null, false, selectItems), "select"));
        items.add(new FluidGridItem(new DynamicField("Fourth Label", "2", false, selectItems), "select"));
        items.add(new FluidGridItem(new DynamicField("Fifth Label", null, true, null), "calendar"));
        items.add(new FluidGridItem(new DynamicField("Sixth Label", new Date(), false, null), "calendar"));
        items.add(new FluidGridItem(new DynamicField("Seventh Label", null, false, null), "input"));
        items.add(new FluidGridItem(new DynamicField("Eighth Label", null, false, selectItems), "select"));
        items.add(new FluidGridItem(new DynamicField("Ninth Label", null, false, null), "calendar"));
    }

    public List<FluidGridItem> getItems() {
        return items;
    }
}
