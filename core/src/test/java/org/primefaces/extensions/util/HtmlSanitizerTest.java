/*
 * Copyright (c) 2011-2024 PrimeFaces Extensions
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

class HtmlSanitizerTest {

    private static final Pattern IMG_SRC_PATTERN = Pattern
                .compile("^(?!javascript:|ftp:|file:|blob:|mailto:).*");

    private boolean isValidSrc(String src) {
        return IMG_SRC_PATTERN.matcher(src).matches();
    }

    @Test
    public void testValidSrc() {
        assertTrue(isValidSrc("myimage.jpg"), "Relative file should be valid");
        assertTrue(isValidSrc("images/myimage.jpg"), "Relative path should be valid");
        assertTrue(isValidSrc("/assets/pic.png"), "Absolute path should be valid");
        assertTrue(isValidSrc("http://example.com/img.jpg"), "HTTP URL should be valid");
        assertTrue(isValidSrc("https://example.com/img.jpg"), "HTTPS URL should be valid");
        assertTrue(isValidSrc("//example.com/img.jpg"), "Protocol-relative URL should be valid");
        assertTrue(isValidSrc("data:image/png;base64,ABC123"), "Data URI should be valid");
    }

    @Test
    public void testInvalidSrc() {
        assertFalse(isValidSrc("javascript:alert(1)"), "Javascript URL should be invalid");
        assertFalse(isValidSrc("ftp://example.com/img.jpg"), "FTP URL should be invalid");
        assertFalse(isValidSrc("mailto:test@junit.org"), "Mail URL should be invalid");
        assertFalse(isValidSrc("file:///C:/images/img.jpg"), "File URL should be invalid");
        assertFalse(isValidSrc("blob:http://example.com/img.jpg"), "Blob URL should be invalid");
    }
}