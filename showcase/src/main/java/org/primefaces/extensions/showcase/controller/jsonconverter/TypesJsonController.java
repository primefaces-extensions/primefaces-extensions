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
package org.primefaces.extensions.showcase.controller.jsonconverter;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 * TypesJsonController
 *
 * @author Oleg Varaksin / last modified by $Author: $
 * @version $Revision: 1.0 $
 */
@Named
@SessionScoped
public class TypesJsonController implements Serializable {

    private static final long serialVersionUID = 1L;

    public String getTypeGenericList() {
        return "java.util.Collection<java.lang.Integer>";
    }

    public String getTypeGenericMap() {
        return "java.util.Map<java.lang.String, org.apache.commons.lang3.tuple.ImmutablePair<java.lang.Integer, java.util.Date>>";
    }

    public String getTypeGenericSimple() {
        return "org.primefaces.extensions.showcase.model.jsonconverter.FooGeneric<java.lang.String, java.lang.Integer>";
    }

    public String getTypeGenericComplex() {
        return "org.primefaces.extensions.showcase.model.jsonconverter.FooGeneric"
                    + "<int[], org.primefaces.extensions.showcase.model.jsonconverter.FooGeneric"
                    + "<org.primefaces.extensions.showcase.model.jsonconverter.FooNonGeneric, java.lang.Boolean>>";
    }
}
