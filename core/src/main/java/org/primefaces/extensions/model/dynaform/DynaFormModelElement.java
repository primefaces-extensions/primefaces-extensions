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
package org.primefaces.extensions.model.dynaform;

import java.util.Objects;

/**
 * Class representing a nested model inside of <code>DynaFormRow</code>.
 *
 * @author Sébastien Lepage / last modified by $Author$
 * @version $Revision$
 * @since 4.0.0
 */
public class DynaFormModelElement extends AbstractDynaFormElement {

    private static final long serialVersionUID = 1L;

    private final DynaFormModel model;

    public DynaFormModelElement(DynaFormModel model, int colspan, int rowspan, int row, int column, int position, boolean extended) {
        super(colspan, rowspan, row, column, extended);
        this.model = model;

        for (final DynaFormControl control : model.getControls()) {
            control.setPosition(position + control.getPosition());
            control.generateKey();
        }
    }

    public DynaFormModel getModel() {
        return model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DynaFormModelElement)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        DynaFormModelElement that = (DynaFormModelElement) o;
        return Objects.equals(getModel(), that.getModel());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getModel());
    }
}
