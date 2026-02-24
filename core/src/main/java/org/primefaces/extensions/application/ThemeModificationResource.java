/*
 * Copyright (c) 2011-2026 PrimeFaces Extensions
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
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import jakarta.faces.application.Resource;
import jakarta.faces.application.ResourceWrapper;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;

import org.primefaces.util.Constants;
import org.primefaces.util.LangUtils;

/**
 * Resource wrapper that handles theme accent color customization for PrimeFaces themes. This resource dynamically replaces hardcoded accent colors in CSS with
 * CSS variables, allowing for runtime theme customization. It also handles responsive breakpoint configuration.
 * <p>
 * The following themes are supported with their respective accent colors:
 * <ul>
 * <li>Arya Theme - Blue accent (#90caf9)</li>
 * <li>Saga Theme - Blue accent (#2196f3)</li>
 * <li>Vela Theme - Blue accent (#90caf9)</li>
 * </ul>
 * </p>
 * <p>
 * Configuration in web.xml:
 * 
 * <pre>
 * {@code
 * <web-app>
 *     <context-param>
 *         <param-name>primefaces.RESPONSIVE_BREAKPOINT</param-name>
 *         <param-value>640</param-value>
 *     </context-param>
 * </web-app>
 * }
 * </pre>
 * </p>
 * <p>
 * Configuration in faces-config.xml:
 * 
 * <pre>
 * {@code
 * <faces-config>
 *     <application>
 *         <resource-handler>org.primefaces.extensions.application.ThemeModificationResourceHandler</resource-handler>
 *     </application>
 * </faces-config>
 * }
 * </pre>
 * </p>
 * <p>
 * The responsive breakpoint parameter controls the width at which responsive design changes occur. If not specified, it defaults to 640px.
 * </p>
 * <p>
 * For each theme, the following CSS variables are generated:
 * <ul>
 * <li>--themeName0: Main accent color</li>
 * <li>--themeName1: Hover state color</li>
 * <li>--themeName2: Active state color</li>
 * <li>--themeName3: Focus outline color</li>
 * <li>--themeName4: Text color</li>
 * </ul>
 * Where themeName is the theme name without the "primefaces-" prefix.
 * </p>
 * 
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 * @since 10.0.1
 * @see ThemeModificationResourceHandler
 */
public class ThemeModificationResource extends ResourceWrapper {

    public static final String RESPONSIVE_BREAKPOINT = "primefaces.RESPONSIVE_BREAKPOINT";

    private static final Map<String, String> CACHE = new HashMap<>();
    private static final Map<String, List<String>> ACCENT_COLORS = new HashMap<>();

    static {
        // @formatter:off
        // colors ? 0: main, 1: hover, 2: active, 3: focus outline, 4: text
        ACCENT_COLORS.put("primefaces-arya", Arrays.asList("#90CAF9", "rgba(255,255,255,.87)", "rgb(106.7153846154,184.1974358974,246.9846153846)",
                "rgb(177.3, 217.9, 250.8)", "#212529"));
        ACCENT_COLORS.put("primefaces-arya-blue", Arrays.asList("#90CAF9", "rgba(255,255,255,.87)", "rgb(106.7153846154,184.1974358974,246.9846153846)",
                "rgb(177.3, 217.9, 250.8)", "#212529"));
        ACCENT_COLORS.put("primefaces-saga", Arrays.asList("#2196f3", "#e9ecef", "#0b7ad1", "rgb(166.2, 213, 250.2)", "#495057"));
        ACCENT_COLORS.put("primefaces-saga-blue", Arrays.asList("#2196f3", "#e9ecef", "#0b7ad1", "rgb(166.2, 213, 250.2)", "#495057"));
        ACCENT_COLORS.put("primefaces-vela", Arrays.asList("#90CAF9", "#6bb8f7", "#45a6f5", "rgb(177.3, 217.9, 250.8)", "#212529"));
        ACCENT_COLORS.put("primefaces-vela-blue", Arrays.asList("#90caf9", "rgba(255,255,255,.03)", "#45a6f5", "rgb(177.3, 217.9, 250.8)", "#212529"));
        // @formatter:on
    }

    /** Character encoding used for reading/writing CSS content. Defaults to UTF-8 if not specified in request. */
    private final String charEncoding;

    /** Responsive breakpoint width in pixels. Defaults to 640px if not specified in context parameters. */
    private final String responsiveBreakpoint;

    /**
     * Creates a new ThemeModificationResource.
     * 
     * @param wrapped The wrapped resource
     */
    public ThemeModificationResource(final Resource wrapped) {
        super(wrapped);
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        // Get character encoding from request, default to UTF-8 if not specified
        String encoding = externalContext.getRequestCharacterEncoding();
        if (LangUtils.isBlank(encoding)) {
            encoding = StandardCharsets.UTF_8.name();
        }
        this.charEncoding = encoding;

        // Get responsive breakpoint from context param, default to 640px if not specified
        String breakpoint = externalContext.getInitParameter(RESPONSIVE_BREAKPOINT);
        this.responsiveBreakpoint = Objects.toString(breakpoint, "640");
    }

    @Override
    public InputStream getInputStream() throws IOException {
        final String library = getWrapped().getLibraryName();
        final List<String> accentColors = ACCENT_COLORS.get(library);
        if (accentColors == null || accentColors.isEmpty()) {
            return getWrapped().getInputStream();
        }

        if (CACHE.containsKey(library)) {
            return new ByteArrayInputStream(CACHE.get(library).getBytes(charEncoding));
        }

        final String css = filterCss(library, accentColors);
        CACHE.put(library, css);

        return new ByteArrayInputStream(css.getBytes(charEncoding));
    }

    /**
     * Filters the CSS content by replacing accent colors with CSS variables and updating responsive breakpoints.
     * 
     * @param library The theme library name
     * @param accentColors List of accent colors to replace with CSS variables
     * @return The filtered CSS content
     * @throws IOException If there is an error reading the CSS content
     */
    protected String filterCss(String library, List<String> accentColors) throws IOException {
        // Create CSS variable name format based on library name
        final String accentColor = "var(--"
                    + library.replace(ThemeModificationResourceHandler.LIBRARY_PREFIX, Constants.EMPTY_STRING)
                    + "%d)";

        // Get raw CSS content
        String css = getContent();

        // Replace each accent color with corresponding CSS variable
        for (int i = 0; i < accentColors.size(); i++) {
            css = css.replace(accentColors.get(i), String.format(accentColor, i));
        }

        // Update responsive breakpoints to use configured value
        css = css.replaceAll(
                    "@media\\s*\\((min|max)-width:\\s*\\d+px\\)",
                    "@media($1-width: " + this.responsiveBreakpoint + "px)");
        return css;
    }

    protected String getContent() throws IOException {
        try (InputStream inputStream = getWrapped().getInputStream()) {
            return new String(inputStream.readAllBytes(), charEncoding);
        }
    }

}
