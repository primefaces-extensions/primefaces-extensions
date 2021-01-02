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
package org.primefaces.extensions.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.extensions.converter.JsonConverter;
import org.primefaces.extensions.converter.JsonExposeAwareConverter;

/**
 * Builder for request parameters.
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since 1.1.0
 */
public class RequestParameterBuilder {

    private StringBuilder buffer;
    private final String originalUrl;
    private final JsonConverter jsonConverter;
    private String encoding;
    private boolean added;

    /**
     * Creates a builder instance. This constructor is useful when we only use encode() and encodeJson() methods.
     */
    public RequestParameterBuilder() {
        this(null);
    }

    /**
     * Creates a builder instance wihout URL or with the current request URL.
     *
     * @param useCurrentRequest boolean flag if the current request URL should be used or not
     */
    public RequestParameterBuilder(boolean useCurrentRequest) {
        this(useCurrentRequest
                    ? ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURL()
                                .toString()
                    : null);
    }

    /**
     * Creates a builder instance by the given URL.
     *
     * @param url URL
     */
    public RequestParameterBuilder(String url) {
        this(url, null);
    }

    /**
     * Creates a builder instance by the given URL.
     *
     * @param url URL
     * @param jsonConverter specific JsonConverter. It should extends {@link JsonConverter} from PrimeFaces Extensions. If null, the default implementation
     *            {@link JsonExposeAwareConverter} is used.
     */
    public RequestParameterBuilder(String url, JsonConverter jsonConverter) {
        buffer = new StringBuilder(url != null ? url : org.primefaces.util.Constants.EMPTY_STRING);
        originalUrl = url != null ? url : org.primefaces.util.Constants.EMPTY_STRING;

        this.jsonConverter = jsonConverter == null ? new JsonExposeAwareConverter(false) : jsonConverter;

        encoding = FacesContext.getCurrentInstance().getExternalContext().getRequestCharacterEncoding();
        if (encoding == null) {
            encoding = "UTF-8";
        }
    }

    /**
     * Adds a request parameter to the URL without specifying a data type of the given parameter value. Parameter's value is converted to JSON notation when
     * adding. Furthermore, it will be encoded according to the acquired encoding.
     *
     * @param name name of the request parameter
     * @param value value of the request parameter
     * @return RequestParameterBuilder updated this instance which can be reused
     * @throws UnsupportedEncodingException DOCUMENT_ME
     */
    public RequestParameterBuilder paramJson(String name, Object value) throws UnsupportedEncodingException {
        return paramJson(name, value, null);
    }

    /**
     * Adds a request parameter to the URL with specifying a data type of the given parameter value. Data type is sometimes required, especially for Java
     * generic types, because type information is erased at runtime and the conversion to JSON will not work properly. Parameter's value is converted to JSON
     * notation when adding. Furthermore, it will be encoded according to the acquired encoding.
     *
     * @param name name of the request parameter
     * @param value value of the request parameter
     * @param type data type of the value object. Any primitive type, array, non generic or generic type is supported. Data type is sometimes required to
     *            convert a value to a JSON representation. All data types should be fully qualified. Examples: "boolean" "int" "long[]" "java.lang.String"
     *            "java.util.Date" "java.util.Collection<java.lang.Integer>" "java.util.Map<java.lang.String, com.durr.FooPair<java.lang.Integer,
     *            java.util.Date>>" "com.durr.FooNonGenericClass" "com.durr.FooGenericClass<java.lang.String, java.lang.Integer>"
     *            "com.durr.FooGenericClass<int[], com.durr.FooGenericClass<com.durr.FooNonGenericClass, java.lang.Boolean>>".
     * @return RequestParameterBuilder updated this instance which can be reused
     * @throws UnsupportedEncodingException DOCUMENT_ME
     */
    public RequestParameterBuilder paramJson(String name, Object value, String type) throws UnsupportedEncodingException {
        final String encodedJsonValue = encodeJson(value, type);

        if (added || originalUrl.contains("?")) {
            buffer.append("&");
        }
        else {
            buffer.append("?");
        }

        buffer.append(name);
        buffer.append("=");
        buffer.append(encodedJsonValue);

        // set a flag that at least one request parameter was added
        added = true;

        return this;
    }

    /**
     * Adds a request parameter to the URL. This is a convenient method for primitive, plain data types. Parameter's value will not be converted to JSON
     * notation when adding. It will be only encoded according to the acquired encoding. Note: null values will not be added.
     *
     * @param name name of the request parameter
     * @param value value of the request parameter
     * @return RequestParameterBuilder updated this instance which can be reused
     * @throws UnsupportedEncodingException DOCUMENT_ME
     */
    public RequestParameterBuilder param(String name, Object value) throws UnsupportedEncodingException {
        final String encodedValue = encode(value);

        if (encodedValue == null) {
            return this;
        }

        if (added || originalUrl.contains("?")) {
            buffer.append("&");
        }
        else {
            buffer.append("?");
        }

        buffer.append(name);
        buffer.append("=");
        buffer.append(encodedValue);

        // set a flag that at least one request parameter was added
        added = true;

        return this;
    }

    /**
     * Encodes given value with a proper encoding. This is a convenient method for primitive, plain data types. Value will not be converted to JSON. Note: Value
     * can be null.
     *
     * @param value value to be encoded
     * @return String encoded value
     * @throws UnsupportedEncodingException DOCUMENT_ME
     */
    public String encode(Object value) throws UnsupportedEncodingException {
        if (value == null) {
            return null;
        }

        return URLEncoder.encode(value.toString(), encoding);
    }

    /**
     * Convertes give value to JSON and encodes the converted value with a proper encoding. Data type is sometimes required, especially for Java generic types,
     * because type information is erased at runtime and the conversion to JSON will not work properly.
     *
     * @param value value to be converted and encoded
     * @param type data type of the value object. Any primitive type, array, non generic or generic type is supported. Data type is sometimes required to
     *            convert a value to a JSON representation. All data types should be fully qualified. Examples: "boolean" "int" "long[]" "java.lang.String"
     *            "java.util.Date" "java.util.Collection<java.lang.Integer>" "java.util.Map<java.lang.String, com.durr.FooPair<java.lang.Integer,
     *            java.util.Date>>" "com.durr.FooNonGenericClass" "com.durr.FooGenericClass<java.lang.String, java.lang.Integer>"
     *            "com.durr.FooGenericClass<int[], com.durr.FooGenericClass<com.durr.FooNonGenericClass, java.lang.Boolean>>".
     * @return String converted and encoded value
     * @throws UnsupportedEncodingException DOCUMENT_ME
     */
    public String encodeJson(Object value, String type) throws UnsupportedEncodingException {
        jsonConverter.setType(type);

        String jsonValue;
        if (value == null) {
            jsonValue = "null";
        }
        else {
            jsonValue = jsonConverter.getAsString(null, null, value);
        }

        return URLEncoder.encode(jsonValue, encoding);
    }

    /**
     * Convertes give value to JSON without to know the data type and encodes the converted value with a proper encoding.
     *
     * @param value value to be converted and encoded
     * @return String converted and encoded value
     * @throws UnsupportedEncodingException DOCUMENT_ME
     */
    public String encodeJson(Object value) throws UnsupportedEncodingException {
        return encodeJson(value, null);
    }

    /**
     * Builds the end result.
     *
     * @return String end result
     */
    public String build() {
        return buffer.toString();
    }

    /**
     * Resets the internal state in order to be reused.
     *
     * @return RequestParameterBuilder reseted builder
     */
    public RequestParameterBuilder reset() {
        buffer = new StringBuilder(originalUrl);
        jsonConverter.setType(null);
        added = false;

        return this;
    }
}
