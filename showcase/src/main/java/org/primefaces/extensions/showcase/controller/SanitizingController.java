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
package org.primefaces.extensions.showcase.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

@Named
@ViewScoped
public class SanitizingController implements Serializable {

    private static final long serialVersionUID = 1L;

    private String defaultSanitizer = "<p>Test</p>";
    private String customSanitizer = "<p><i>Italic</i></p>";

    /**
     * PolicyFactory to allow certain HTML elements like italic and bold tags.
     *
     * @return the {@code PolicyFactory}
     */
    public PolicyFactory getPolicyFactory() {
        final String[] allowElements = new String[] {"b", "em", "i", "s", "strong", "sub", "sup", "u"};

        return new HtmlPolicyBuilder()
                    .allowElements(allowElements)
                    .allowTextIn(allowElements)
                    .toFactory();
    }

    public String getDefaultSanitizer() {
        return defaultSanitizer;
    }

    public void setDefaultSanitizer(String defaultSanitizer) {
        this.defaultSanitizer = defaultSanitizer;
    }

    public String getCustomSanitizer() {
        return customSanitizer;
    }

    public void setCustomSanitizer(String customSanitizer) {
        this.customSanitizer = customSanitizer;
    }

}
