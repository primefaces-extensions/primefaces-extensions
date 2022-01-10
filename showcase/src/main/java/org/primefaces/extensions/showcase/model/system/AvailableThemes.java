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
package org.primefaces.extensions.showcase.model.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * AvailableThemes
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 */
public class AvailableThemes {

    private static AvailableThemes instance = null;

    private final HashMap<String, Theme> themesAsMap;
    private final List<Theme> themes;

    private AvailableThemes() {
        final List<String> themeNames = new ArrayList<>();

        themeNames.add("arya");
        themeNames.add("luna-amber");
        themeNames.add("luna-blue");
        themeNames.add("luna-green");
        themeNames.add("luna-pink");
        themeNames.add("nova-dark");
        themeNames.add("nova-light");
        themeNames.add("saga");
        themeNames.add("vela");

        themesAsMap = new HashMap<>();
        themes = new ArrayList<>();

        for (final String themeName : themeNames) {
            final Theme theme = new Theme();
            theme.setName(themeName);
            theme.setImage("/resources/images/themeswitcher/" + themeName
                        + ".png");

            themes.add(theme);
            themesAsMap.put(theme.getName(), theme);
        }
    }

    public static AvailableThemes getInstance() {
        if (instance == null) {
            instance = new AvailableThemes();
        }

        return instance;
    }

    public final List<Theme> getThemes() {
        return themes;
    }

    public Theme getThemeForName(final String name) {
        return themesAsMap.get(name);
    }
}
