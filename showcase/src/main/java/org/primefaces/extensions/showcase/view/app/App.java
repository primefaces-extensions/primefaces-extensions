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
package org.primefaces.extensions.showcase.view.app;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serial;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

import org.primefaces.context.PrimeApplicationContext;
import org.primefaces.extensions.showcase.domain.Country;

/**
 * Session bean holding the theme preset, locale and text direction of the showcase.
 */
@Named("app")
@SessionScoped
public class App implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The themes of PrimeFaces. The theme configurator switches with ?theme=..., the choice is kept in the session.
     */
    public static final List<String> PRESETS = List.of("aura", "lara", "nora", "metal", "glass");

    private static final String UNKNOWN = "unknown";

    // default preset of the showcase
    private String theme = "glass";
    private Country locale = new Country(0, Locale.US);
    // text direction chosen via ?dir=ltr|rtl; null: the direction of the locale
    private String direction;

    /**
     * The current preset (primefaces.THEME in web.xml); a valid ?theme= request parameter switches it.
     *
     * @return aura, lara, nora, metal or glass
     */
    public String getTheme() {
        return resolveTheme(requestParameter("theme"));
    }

    String resolveTheme(final String requested) {
        if (requested != null && PRESETS.contains(requested)) {
            theme = requested;
        }
        return theme;
    }

    public void setTheme(final String theme) {
        if (PRESETS.contains(theme)) {
            this.theme = theme;
        }
    }

    /**
     * The text direction (primefaces.DIR in web.xml): a valid ?dir= request parameter sets it, without a choice it follows the locale.
     *
     * @return ltr or rtl
     */
    public String getDir() {
        return resolveDir(requestParameter("dir"));
    }

    String resolveDir(final String requested) {
        if ("ltr".equals(requested) || "rtl".equals(requested)) {
            direction = requested;
        }
        if (direction != null) {
            return direction;
        }
        return locale.isRtl() ? "rtl" : "ltr";
    }

    public List<String> getPresets() {
        return PRESETS;
    }

    public Country getLocale() {
        return locale;
    }

    public void setLocale(final Country locale) {
        this.locale = locale;
    }

    public String getPrimeFacesVersion() {
        return PrimeApplicationContext.getCurrentInstance(FacesContext.getCurrentInstance()).getEnvironment().getBuildVersion();
    }

    /**
     * The PrimeFaces Extensions version: the Implementation-Version of the core jar manifest (as TechnicalInfo does). Without a manifest (e.g. classes
     * directory) it falls back to "dev", so the value is never blank.
     *
     * @return never blank
     */
    public String getExtensionsVersion() {
        final String version = org.primefaces.extensions.util.Constants.class.getPackage().getImplementationVersion();
        return version == null || version.isBlank() ? "dev" : version;
    }

    /**
     * The build timestamp from pe-showcase.properties (as TechnicalInfo), formatted as yyyy-MM-dd HH:mm.
     *
     * @return the build time or "unknown"
     */
    public String getBuildTime() {
        final String timestamp = applicationProperty("timestamp");
        if (timestamp == null) {
            return UNKNOWN;
        }
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(Long.parseLong(timestamp.trim())));
        }
        catch (final NumberFormatException e) {
            return UNKNOWN;
        }
    }

    private static String requestParameter(final String name) {
        final FacesContext context = FacesContext.getCurrentInstance();
        return context == null ? null : context.getExternalContext().getRequestParameterMap().get(name);
    }

    /**
     * Reads a key out of the Maven-filtered "application.properties={k=v, ...}" entry of pe-showcase.properties, the same format TechnicalInfo parses.
     */
    private static String applicationProperty(final String key) {
        final Properties props = new Properties();
        try (InputStream in = App.class.getClassLoader().getResourceAsStream("pe-showcase.properties")) {
            if (in == null) {
                return null;
            }
            props.load(in);
        }
        catch (final IOException e) {
            return null;
        }
        final String all = props.getProperty("application.properties");
        if (all == null || all.indexOf('}') < 1) {
            return null;
        }
        for (final String entry : all.substring(1, all.indexOf('}')).split("[\s,]+")) {
            final String[] kv = entry.split("=", 2);
            if (kv.length == 2 && kv[0].equals(key)) {
                return kv[1];
            }
        }
        return null;
    }
}
