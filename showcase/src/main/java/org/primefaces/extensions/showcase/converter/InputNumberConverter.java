/*
 * Copyright 2011-2021 PrimeFaces Extensions
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
