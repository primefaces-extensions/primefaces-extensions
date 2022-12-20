/*
 * Copyright (c) 2011-2022 PrimeFaces Extensions
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.Resource;
import javax.faces.application.ResourceWrapper;
import javax.faces.context.FacesContext;

import org.primefaces.util.Constants;
import org.primefaces.util.LangUtils;

/**
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 * @since 10.0.1
 */
public class ThemeAccentColorResource extends ResourceWrapper {

    private static final Map<String, String> CACHE = new HashMap<>();
    private static final Map<String, List<String>> ACCENT_COLORS = new HashMap<>();

    static {
        // @formatter:off
        // colors ? 0: main, 1: hover, 2: active, 3: focus outline, 4: text
        ACCENT_COLORS.put("primefaces-arya", Arrays.asList("#90CAF9", "#6bb8f7", "#45a6f5", "#b1dafb", "rgba(255, 255, 255, 0.87)"));
        ACCENT_COLORS.put("primefaces-saga", Arrays.asList("#2196F3", "#0d89ec", "#0b7ad1", "#a6d5fa", "#495057"));
        ACCENT_COLORS.put("primefaces-vela", Arrays.asList("#90CAF9", "#6bb8f7", "#45a6f5", "#b1dafb", "rgba(255, 255, 255, 0.87)"));
        // @formatter:on
    }

    private final Resource wrapped;
    private final String charEncoding;

    public ThemeAccentColorResource(final Resource wrapped) {
        this.wrapped = wrapped;
        String encoding = FacesContext.getCurrentInstance().getExternalContext().getRequestCharacterEncoding();
        if (LangUtils.isBlank(encoding)) {
            encoding = StandardCharsets.UTF_8.name();
        }
        this.charEncoding = encoding;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        final String library = wrapped.getLibraryName();
        final List<String> accentColors = ACCENT_COLORS.get(library);
        if (accentColors == null || accentColors.isEmpty()) {
            return wrapped.getInputStream();
        }

        if (CACHE.containsKey(library)) {
            return new ByteArrayInputStream(CACHE.get(library).getBytes(charEncoding));
        }

        final String accentColor = "var(--"
                    + library.replace(ThemeAccentColorResourceHandler.LIBRARY_PREFIX, Constants.EMPTY_STRING)
                    + "%d)";
        String css = getContent();
        for (int i = 0; i < accentColors.size(); i++) {
            css = css.replace(accentColors.get(i), String.format(accentColor, i));
        }
        CACHE.put(library, css);

        return new ByteArrayInputStream(css.getBytes(charEncoding));
    }

    protected String getContent() throws IOException {
        final ByteArrayOutputStream result = new ByteArrayOutputStream();
        final byte[] buffer = new byte[1024];
        final InputStream inputStream = wrapped.getInputStream();
        for (int length; (length = inputStream.read(buffer)) != -1;) {
            result.write(buffer, 0, length);
        }
        return result.toString(charEncoding);
    }

    @Override
    public Resource getWrapped() {
        return wrapped;
    }

}
