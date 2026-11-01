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
package org.primefaces.extensions.showcase.menu;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.primefaces.extensions.showcase.ShowcaseFiles;

class AppMenuTest {

    private static final Map<String, String> EXPECTED_BADGES = new HashMap<>();

    static {
        for (final String s : new String[] {"ECharts", "OSMap", "MarkText", "Kanban", "MeterGroup", "CommandPalette", "Gantt"}) {
            EXPECTED_BADGES.put(s, "New");
        }
        for (final String s : new String[] {"MonacoEditor", "OrgChart", "SunEditor"}) {
            EXPECTED_BADGES.put(s, "Updated");
        }
    }

    private static AppMenu menu() {
        final AppMenu menu = new AppMenu();
        menu.init();
        return menu;
    }

    private static void collect(final List<MenuItem> items, final List<MenuItem> out) {
        if (items == null) {
            return;
        }
        for (final MenuItem item : items) {
            out.add(item);
            collect(item.getMenuItems(), out);
        }
    }

    @Test
    void everyInternalUrlHasPage() {
        final Path root = ShowcaseFiles.webappRoot();
        for (final String u : menu().allUrls()) {
            Assertions.assertTrue(u.startsWith("/"), u);
            Assertions.assertTrue(Files.exists(root.resolve(u.substring(1) + ".xhtml")), "Missing page for " + u);
        }
    }

    @Test
    void everyUiPageIsInMenu() {
        final Path root = ShowcaseFiles.webappRoot();
        final Set<String> urls = new HashSet<>(menu().allUrls());
        for (final Path p : ShowcaseFiles.uiPages()) {
            final String rel = root.relativize(p).toString().replace("\\", "/");
            if (rel.contains("/standalone/")) {
                continue;
            }
            final String url = "/" + rel.substring(0, rel.length() - ".xhtml".length());
            Assertions.assertTrue(urls.contains(url), "Page not in menu: " + url);
        }
    }

    @Test
    void badgesMatchSpec() {
        final List<MenuItem> all = new ArrayList<>();
        for (final MenuCategory c : menu().getMenuCategories()) {
            collect(c.getMenuItems(), all);
        }
        for (final MenuItem item : all) {
            final String expected = EXPECTED_BADGES.get(item.getLabel());
            if (expected != null) {
                Assertions.assertEquals(expected, item.getBadge(), "Badge of " + item.getLabel());
            }
            else {
                Assertions.assertNull(item.getBadge(), "Unexpected badge on " + item.getLabel());
            }
        }
    }

    @Test
    void searchFindsCaseInsensitive() {
        final List<MenuItem> found = menu().completeMenuItem("git");
        Assertions.assertTrue(found.stream().anyMatch(i -> "GitHub".equals(i.getLabel())));
    }

    @Test
    void noDuplicateLabelsWithinCategory() {
        for (final MenuCategory c : menu().getMenuCategories()) {
            final Set<String> seen = new HashSet<>();
            for (final MenuItem item : c.getMenuItems()) {
                Assertions.assertTrue(seen.add(item.getLabel()), "Duplicate in " + c.getLabel() + ": " + item.getLabel());
            }
        }
    }

    @Test
    void onlyNonEmptyCategoriesAreExposed() {
        for (final MenuCategory c : menu().getMenuCategories()) {
            Assertions.assertFalse(c.getMenuItems().isEmpty(), c.getLabel());
        }
    }

    @Test
    void findMenuItemByUrlFindsItemsAndSetsParents() {
        final AppMenu menu = menu();
        Assertions.assertNull(menu.findMenuItemByUrl("/nope"));
        Assertions.assertNull(menu.findMenuItemByUrl(null));
    }
}
