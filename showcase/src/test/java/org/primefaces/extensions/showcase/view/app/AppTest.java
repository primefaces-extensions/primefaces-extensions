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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.primefaces.extensions.showcase.domain.Country;
import org.primefaces.extensions.showcase.service.CountryService;

class AppTest {

    @Test
    void presetsAreExactly() {
        assertEquals(List.of("aura", "lara", "nora", "metal", "glass"), App.PRESETS);
        assertEquals(App.PRESETS, new App().getPresets());
    }

    @Test
    void defaultThemeIsGlass() {
        assertEquals("glass", new App().resolveTheme(null));
    }

    @Test
    void validPresetSwitchesTheme() {
        final App app = new App();
        assertEquals("lara", app.resolveTheme("lara"));
        assertEquals("lara", app.resolveTheme(null));
    }

    @Test
    void invalidPresetIgnored() {
        assertEquals("glass", new App().resolveTheme("bootstrap"));
    }

    @Test
    void rtlLocaleGivesRtlDir() {
        final CountryService service = new CountryService();
        service.init();
        final Country arabic = service.getLocales().stream()
                    .filter(c -> "ar".equals(c.getLanguage())).findFirst().orElseThrow();
        assertTrue(arabic.isRtl());
        final App app = new App();
        assertEquals("ltr", app.resolveDir(null));
        app.setLocale(arabic);
        assertEquals("rtl", app.resolveDir(null));
        assertEquals("ltr", app.resolveDir("ltr"));
    }

    @Test
    void extensionsVersionNotBlank() {
        final String version = new App().getExtensionsVersion();
        assertNotNull(version);
        assertFalse(version.isBlank());
    }
}
