/**
 * Copyright 2011-2018 PrimeFaces Extensions
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
            ValueExpression expression = component.getValueExpression("value");
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
