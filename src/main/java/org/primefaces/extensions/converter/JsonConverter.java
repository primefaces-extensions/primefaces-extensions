/**
 * Copyright 2011-2019 PrimeFaces Extensions
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

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.extensions.util.json.GsonConverter;
import org.primefaces.extensions.util.json.ParameterizedTypeImpl;
import org.primefaces.util.Constants;

/**
 * {@link Converter} which converts a JSON string to an object an vice-versa.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.2
 */
@FacesConverter(value = "org.primefaces.extensions.converter.JsonConverter")
public class JsonConverter implements Converter, Serializable {

    private static final long serialVersionUID = 20121214L;

    private static final Map<String, Class<?>> PRIMITIVE_CLASSES = new HashMap<String, Class<?>>();
    private static final Map<String, Class<?>> PRIMITIVE_ARRAY_CLASSES = new HashMap<String, Class<?>>();

    static {
        PRIMITIVE_CLASSES.put("boolean", boolean.class);
        PRIMITIVE_CLASSES.put("byte", byte.class);
        PRIMITIVE_CLASSES.put("short", short.class);
        PRIMITIVE_CLASSES.put("char", char.class);
        PRIMITIVE_CLASSES.put("int", int.class);
        PRIMITIVE_CLASSES.put("long", long.class);
        PRIMITIVE_CLASSES.put("float", float.class);
        PRIMITIVE_CLASSES.put("double", double.class);

        PRIMITIVE_ARRAY_CLASSES.put("boolean[]", boolean[].class);
        PRIMITIVE_ARRAY_CLASSES.put("byte[]", byte[].class);
        PRIMITIVE_ARRAY_CLASSES.put("short[]", short[].class);
        PRIMITIVE_ARRAY_CLASSES.put("char[]", char[].class);
        PRIMITIVE_ARRAY_CLASSES.put("int[]", int[].class);
        PRIMITIVE_ARRAY_CLASSES.put("long[]", long[].class);
        PRIMITIVE_ARRAY_CLASSES.put("float[]", float[].class);
        PRIMITIVE_ARRAY_CLASSES.put("double[]", double[].class);
    }

    private String type;

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        java.lang.reflect.Type objType;

        if (getType() == null) {
            ValueExpression expression = component.getValueExpression("value");
            objType = expression.getType(context.getELContext());
        }
        else {
            objType = getObjectType(getType().trim(), false);
        }

        return GsonConverter.getGson().fromJson(value, objType);
    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (getType() == null) {
            return GsonConverter.getGson().toJson(value);
        }
        else {
            return GsonConverter.getGson().toJson(value, getObjectType(getType().trim(), false));
        }
    }

    protected java.lang.reflect.Type getObjectType(String type, boolean isTypeArg) {
        Class clazz = PRIMITIVE_CLASSES.get(type);
        if (clazz != null) {
            if (!isTypeArg) {
                return clazz;
            }
            else {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Type argument can not be a primitive type, but it was " + type
                                        + ".",
                            Constants.EMPTY_STRING));
            }
        }

        clazz = PRIMITIVE_ARRAY_CLASSES.get(type);
        if (clazz != null) {
            return clazz;
        }

        int arrayBracketIdx = type.indexOf("[");
        int leftBracketIdx = type.indexOf("<");
        if (arrayBracketIdx >= 0 && (leftBracketIdx < 0 || arrayBracketIdx < leftBracketIdx)) {
            // array
            try {
                clazz = Class.forName(type.substring(0, arrayBracketIdx));

                return Array.newInstance(clazz, 0).getClass();
            }
            catch (ClassNotFoundException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Class " + type.substring(0, arrayBracketIdx) + " not found",
                            Constants.EMPTY_STRING));
            }
        }

        if (leftBracketIdx < 0) {
            try {
                return Class.forName(type);
            }
            catch (ClassNotFoundException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Class " + type + " not found",
                            Constants.EMPTY_STRING));
            }
        }

        int rightBracketIdx = type.lastIndexOf(">");
        if (rightBracketIdx < 0) {
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, type + " is not a valid generic type.",
                        Constants.EMPTY_STRING));
        }

        Class rawType;
        try {
            rawType = Class.forName(type.substring(0, leftBracketIdx));
        }
        catch (ClassNotFoundException e) {
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Class " + type.substring(0, leftBracketIdx) + " not found",
                        Constants.EMPTY_STRING));
        }

        String strTypeArgs = type.substring(leftBracketIdx + 1, rightBracketIdx);
        List<String> listTypeArgs = new ArrayList<String>();
        int startPos = 0;
        int seekPos = 0;

        while (true) {
            int commaPos = strTypeArgs.indexOf(",", seekPos);
            if (commaPos >= 0) {
                String term = strTypeArgs.substring(startPos, commaPos);
                int countLeftBrackets = StringUtils.countMatches(term, "<");
                int countRightBrackets = StringUtils.countMatches(term, ">");
                if (countLeftBrackets == countRightBrackets) {
                    listTypeArgs.add(term.trim());
                    startPos = commaPos + 1;
                }

                seekPos = commaPos + 1;
            }
            else {
                listTypeArgs.add(strTypeArgs.substring(startPos).trim());

                break;
            }
        }

        if (listTypeArgs.isEmpty()) {
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, type + " is not a valid generic type.",
                        Constants.EMPTY_STRING));
        }

        int size = listTypeArgs.size();
        java.lang.reflect.Type[] objectTypes = new java.lang.reflect.Type[size];
        for (int i = 0; i < size; i++) {
            // recursive call for each type argument
            objectTypes[i] = getObjectType(listTypeArgs.get(i), true);
        }

        return new ParameterizedTypeImpl(rawType, objectTypes, null);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
