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
package org.primefaces.extensions.application;

import java.io.IOException;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.ResponseWriter;
import jakarta.faces.context.ResponseWriterWrapper;

/**
 * JSF generates all script tags with 'type="text/javascript"' which throws HTML5 validation warnings.
 *
 * @since 10.0.0
 */
public class Html5ResponseWriter extends ResponseWriterWrapper {

    private static final String SCRIPT = "script";
    private static final String TYPE = "text/javascript";
    private boolean inScriptStartTag;

    public Html5ResponseWriter(ResponseWriter wrapped) {
        super(wrapped);
    }

    @Override
    public void startElement(String name, UIComponent component) throws IOException {
        super.startElement(name, component);
        inScriptStartTag = SCRIPT.equalsIgnoreCase(name);
    }

    @Override
    public void endElement(String name) throws IOException {
        super.endElement(name);
        if (SCRIPT.equalsIgnoreCase(name)) {
            inScriptStartTag = false;
        }
    }

    @Override
    public void writeAttribute(String name, Object value, String property) throws IOException {
        if (inScriptStartTag && TYPE.equals(value)) {
            return;
        }
        super.writeAttribute(name, value, property);
    }
}
