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
package org.primefaces.extensions.converter;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.primefaces.extensions.util.json.GsonConverter;
import org.primefaces.extensions.util.json.GsonExposeAwareConverter;

/**
 * Extension of {@link org.primefaces.extensions.converter.JsonConverter}. It uses specific Gson converters from this tag library.
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since 1.1.0
 */
public class JsonExposeAwareConverter extends JsonConverter {

    private static final long serialVersionUID = 1L;

    private boolean excludeFieldsWithoutExposeAnnotation = false;

    public JsonExposeAwareConverter() {
    }

    public JsonExposeAwareConverter(boolean excludeFieldsWithoutExposeAnnotation) {
        this.excludeFieldsWithoutExposeAnnotation = excludeFieldsWithoutExposeAnnotation;
    }

    public boolean isExcludeFieldsWithoutExposeAnnotation() {
        return excludeFieldsWithoutExposeAnnotation;
    }

    public void setExcludeFieldsWithoutExposeAnnotation(boolean excludeFieldsWithoutExposeAnnotation) {
        this.excludeFieldsWithoutExposeAnnotation = excludeFieldsWithoutExposeAnnotation;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        java.lang.reflect.Type objType;

        if (getType() == null) {
            final ValueExpression expression = component.getValueExpression("value");
            objType = expression.getType(context.getELContext());
        }
        else {
            objType = getObjectType(getType().trim(), false);
        }

        if (excludeFieldsWithoutExposeAnnotation) {
            return GsonExposeAwareConverter.getGson().fromJson(value, objType);
        }
        else {
            return GsonConverter.getGson().fromJson(value, objType);
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (excludeFieldsWithoutExposeAnnotation) {
            if (getType() == null) {
                return GsonExposeAwareConverter.getGson().toJson(value);
            }
            else {
                return GsonExposeAwareConverter.getGson().toJson(value, getObjectType(getType().trim(), false));
            }
        }
        else {
            if (getType() == null) {
                return GsonConverter.getGson().toJson(value);
            }
            else {
                return GsonConverter.getGson().toJson(value, getObjectType(getType().trim(), false));
            }
        }
    }
}
