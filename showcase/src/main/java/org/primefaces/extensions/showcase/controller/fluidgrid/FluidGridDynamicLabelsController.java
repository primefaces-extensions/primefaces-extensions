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
package org.primefaces.extensions.showcase.controller.fluidgrid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.extensions.model.fluidgrid.FluidGridItem;

@Named
@ViewScoped
public class FluidGridDynamicLabelsController implements Serializable {

    private static final long serialVersionUID = 1L;

    private static int counter = 0;

    private List<FluidGridItem> items;

    @PostConstruct
    protected void initialize() {
        items = createItems("First", "Second", "Third", "Fourth", "Fifth");
    }

    public List<FluidGridItem> getItems() {
        return items;
    }

    public void changeItems() {
        counter++;
        switch (counter % 3) {
            case 1:
                items = createItems("Alpha", "Beta", "Gamma");
                break;
            case 2:
                items = createItems("Apple", "Banana", "Cherry", "Date");
                break;
            default:
                items = createItems("First", "Second", "Third", "Fourth", "Fifth");
                break;
        }
    }

    private static List<FluidGridItem> createItems(String... names) {
        final List<FluidGridItem> items = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            final boolean required = i % 2 == 0;
            items.add(new FluidGridItem(new DynamicLabelField(names[i], null, required), "input"));
        }
        return items;
    }

    public static class DynamicLabelField implements Serializable {
        private static final long serialVersionUID = 1L;
        private String label;
        private String value;
        private boolean required;

        public DynamicLabelField() {
        }

        public DynamicLabelField(String label, String value, boolean required) {
            this.label = label;
            this.value = value;
            this.required = required;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public boolean isRequired() {
            return required;
        }

        public void setRequired(boolean required) {
            this.required = required;
        }
    }
}
