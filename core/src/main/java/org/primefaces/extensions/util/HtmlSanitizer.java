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
package org.primefaces.extensions.util;

import java.util.regex.Pattern;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.primefaces.util.LangUtils;

public class HtmlSanitizer {

    private static final PolicyFactory HTML_IMAGES_SANITIZER = new HtmlPolicyBuilder()
                .allowUrlProtocols("data", "http", "https")
                .allowElements("img", "figure")
                .allowAttributes("src")
                .matching(Pattern.compile("^(?!javascript:|ftp:|file:|blob:|mailto:).*", Pattern.CASE_INSENSITIVE))
                .onElements("img")
                .allowAttributes(
                            "data-rotate",
                            "data-proportion",
                            "data-rotatex",
                            "data-rotatey",
                            "data-size",
                            "data-align",
                            "data-percentage",
                            "data-index",
                            "data-file-name",
                            "data-file-size",
                            "data-origin")
                .onElements("img")
                .allowAttributes("id", "style")
                .onElements("img", "figure")
                .toFactory();

    private static final PolicyFactory HTML_MEDIA_SANITIZER = new HtmlPolicyBuilder()
                .allowUrlProtocols("data", "http", "https")
                .allowElements("video", "audio", "source", "iframe", "figure")
                .allowAttributes("id", "controls", "width", "height", "origin-size", "src", "allowfullscreen", "class", "style", "data-proportion",
                            "data-align",
                            "data-percentage", " data-size", "data-file-name", "data-file-size", "data-origin", "data-rotate", "data-index")
                .onElements("video", "audio", "source", "iframe", "figure")
                .toFactory();

    private static final PolicyFactory HTML_LINKS_SANITIZER = Sanitizers.LINKS
                .and(new HtmlPolicyBuilder()
                            .allowElements("a")
                            .allowAttributes("id", "target")
                            .onElements("a")
                            .toFactory());

    private static final PolicyFactory HTML_STYLES_SANITIZER = Sanitizers.STYLES
                .and(new HtmlPolicyBuilder()
                            .allowElements("table", "span", "li", "p", "pre", "div", "hr")
                            .allowAttributes("id", "class", "style", "contenteditable")
                            .onElements("table", "span", "li", "p", "pre", "div", "hr")
                            .toFactory());

    private static final PolicyFactory HTML_DENY_ALL_SANITIZER = new HtmlPolicyBuilder().toFactory();

    private HtmlSanitizer() {

    }

    public static PolicyFactory creatPolicyFactory(
                boolean allowBlocks,
                boolean allowFormatting,
                boolean allowLinks,
                boolean allowStyles,
                boolean allowImages,
                boolean allowTables,
                boolean allowMedia) {
        PolicyFactory sanitizer = HTML_DENY_ALL_SANITIZER;
        if (allowBlocks) {
            sanitizer = sanitizer.and(Sanitizers.BLOCKS);
        }
        if (allowFormatting) {
            sanitizer = sanitizer.and(Sanitizers.FORMATTING);
        }
        if (allowLinks) {
            sanitizer = sanitizer.and(HTML_LINKS_SANITIZER);
        }
        if (allowStyles) {
            sanitizer = sanitizer.and(HTML_STYLES_SANITIZER);
        }
        if (allowImages) {
            sanitizer = sanitizer.and(HTML_IMAGES_SANITIZER);
        }
        if (allowMedia) {
            sanitizer = sanitizer.and(HTML_MEDIA_SANITIZER);
        }
        if (allowTables) {
            sanitizer = sanitizer.and(Sanitizers.TABLES);
        }
        return sanitizer;
    }

    public static String sanitizeHtml(String value,
                boolean allowBlocks,
                boolean allowFormatting,
                boolean allowLinks,
                boolean allowStyles,
                boolean allowImages,
                boolean allowTables,
                boolean allowMedia) {

        if (LangUtils.isBlank(value)) {
            return value;
        }

        PolicyFactory sanitizer = creatPolicyFactory(allowBlocks, allowFormatting, allowLinks, allowStyles, allowImages, allowTables, allowMedia);

        return sanitizer.sanitize(value);
    }

}