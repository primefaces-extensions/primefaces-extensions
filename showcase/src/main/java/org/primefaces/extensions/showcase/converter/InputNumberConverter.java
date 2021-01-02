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
package org.primefaces.extensions.showcase.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.primefaces.extensions.showcase.model.Distance;

/**
 * InputNumberConverter converter class.
 *
 * @author Mauricio Fenoglio / last modified by $Author:$
 * @version $Revision:$
 */
@FacesConverter("inputNumberConverter")
public class InputNumberConverter implements Converter {

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        Double doubleValue = 0.0;
        if (value != null && !value.isEmpty()) {
            doubleValue = Double.valueOf(value);
        }

        Distance res = new Distance();
        res.setMeters(doubleValue);

        return res;
    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {
        Distance distance = (Distance) value;
        Double res = distance.getMeters();

        return res.toString();
    }
}
