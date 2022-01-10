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
package org.primefaces.extensions.showcase.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.primefaces.extensions.showcase.model.State;

/**
 * TriStateManyCheckboxConverter converter class.
 *
 * @author Mauricio Fenoglio / last modified by $Author:$
 * @version $Revision:$
 */
@FacesConverter("triStateManyCheckboxConverter")
public class TriStateManyCheckboxConverter implements Converter {

    @Override
    public Object getAsObject(final FacesContext context, final UIComponent component, final String value) {
        State res;
        if ("0".equals(value)) {
            res = new State("One");
        }
        else if ("1".equals(value)) {
            res = new State("Two");
        }
        else {
            res = new State("Three");
        }

        return res;
    }

    @Override
    public String getAsString(final FacesContext context, final UIComponent component, final Object valueO) {
        State value = (State) valueO;
        String res;
        if (value.getState().equals("One")) {
            res = "0";
        }
        else if (value.getState().equals("Two")) {
            res = "1";
        }
        else {
            res = "2";
        }

        return res;
    }
}
