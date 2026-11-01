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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

/**
 * Navigation model of the showcase. Component categories are filled in {@link #init()}; only non-empty categories are exposed through
 * {@link #getMenuCategories()}.
 */
@Named
@ApplicationScoped
public class AppMenu {

    static final String INPUT = "Input";
    static final String EDITORS = "Editors";
    static final String DATA = "Data";
    static final String CHARTS = "Charts & Maps";
    static final String PANEL = "Panel & Layout";
    static final String OVERLAY = "Overlay & Feedback";
    static final String MEDIA = "Media";
    static final String MISC = "Misc";
    static final String RESOURCES = "Resources";

    /** All categories in display order, including empty ones. */
    private final Map<String, List<MenuItem>> categoryItems = new LinkedHashMap<>();

    private List<MenuCategory> menuCategories;

    @PostConstruct
    public void init() {
        categoryItems.clear();
        for (final String name : new String[] {INPUT, EDITORS, DATA, CHARTS, PANEL, OVERLAY, MEDIA, MISC, RESOURCES}) {
            categoryItems.put(name, new ArrayList<>());
        }

        // Component entries are added by the migration tasks, e.g.:
        // add(INPUT, new MenuItem("<Label>", "<url>"));

        add(INPUT, new MenuItem("Calculator", "/ui/input/calculator"));
        add(INPUT, new MenuItem("ClockPicker", "/ui/input/clockpicker"));
        add(INPUT, new MenuItem("CodeScanner", "/ui/input/codescanner"));
        add(INPUT, new MenuItem("Converters", "/ui/input/converters"));
        add(INPUT, new MenuItem("CreditCard", "/ui/input/creditcard"));
        add(INPUT, new MenuItem("DynaForm", List.of(
                    new MenuItem("Basic", "/ui/input/dynaform/basic"),
                    new MenuItem("Features", "/ui/input/dynaform/features"),
                    new MenuItem("Controls", "/ui/input/dynaform/controls"))));
        add(INPUT, new MenuItem("FuzzySearch", "/ui/input/fuzzysearch"));
        add(INPUT, new MenuItem("InputPlace", List.of(
                    new MenuItem("Google", "/ui/input/inputplace/google"),
                    new MenuItem("Azure", "/ui/input/inputplace/azure"),
                    new MenuItem("Linked Map", "/ui/input/inputplace/map"))));
        add(INPUT, new MenuItem("TimePicker", "/ui/input/timepicker"));

        add(EDITORS, new MenuItem("CodeMirror", "/ui/editors/codemirror"));
        add(EDITORS, new MenuItem("MarkdownEditor", "/ui/editors/markdowneditor"));
        add(EDITORS, new MenuItem("MonacoEditor", List.of(
                    new MenuItem("Basic", "/ui/editors/monacoeditor/basic"),
                    new MenuItem("Theme", "/ui/editors/monacoeditor/theme"),
                    new MenuItem("Extender", "/ui/editors/monacoeditor/extender"),
                    new MenuItem("Diff", "/ui/editors/monacoeditor/diff"),
                    new MenuItem("Diff Theme", "/ui/editors/monacoeditor/difftheme"),
                    new MenuItem("Diff Extender", "/ui/editors/monacoeditor/diffextender")), "Updated"));
        add(EDITORS, new MenuItem("SunEditor", "/ui/editors/suneditor", "Updated"));

        add(DATA, new MenuItem("Counter", "/ui/data/counter"));

        add(PANEL, new MenuItem("Layout", "/ui/panel/layout"));

        add(MISC, new MenuItem("MarkText", List.of(
                    new MenuItem("Basic", "/ui/misc/marktext/basic"),
                    new MenuItem("Matching", "/ui/misc/marktext/matching"),
                    new MenuItem("Search Terms", "/ui/misc/marktext/terms"),
                    new MenuItem("Scope", "/ui/misc/marktext/scope")), "New"));

        add(RESOURCES, new MenuItem("Tag Library", "/taglib"));
        add(RESOURCES, new MenuItem("GitHub", "https://github.com/primefaces-extensions/primefaces-extensions"));
        add(RESOURCES, new MenuItem("Issues", "https://github.com/primefaces-extensions/primefaces-extensions/issues"));
        add(RESOURCES, new MenuItem("Website", "https://primefaces-extensions.github.io"));
        add(RESOURCES, new MenuItem("PrimeFaces Showcase", "https://www.primefaces.org/showcase/"));

        menuCategories = new ArrayList<>();
        for (final Map.Entry<String, List<MenuItem>> e : categoryItems.entrySet()) {
            if (!e.getValue().isEmpty()) {
                menuCategories.add(new MenuCategory(e.getKey(), e.getValue()));
            }
        }
    }

    private void add(final String category, final MenuItem item) {
        categoryItems.get(category).add(item);
    }

    public List<MenuItem> completeMenuItem(final String query) {
        final String q = query == null ? "" : query.toLowerCase();
        final List<MenuItem> result = new ArrayList<>();
        for (final MenuCategory category : menuCategories) {
            collectMatches(category.getMenuItems(), q, result);
        }
        result.sort(Comparator.comparing(m -> m.getParent() != null ? m.getParent().getLabel() : ""));
        return result;
    }

    private void collectMatches(final List<MenuItem> items, final String q, final List<MenuItem> result) {
        if (items == null) {
            return;
        }
        for (final MenuItem item : items) {
            if (item.getLabel() != null && item.getLabel().toLowerCase().contains(q)) {
                result.add(item);
            }
            collectMatches(item.getMenuItems(), q, result);
        }
    }

    public MenuItem findMenuItemByUrl(final String url) {
        if (url == null) {
            return null;
        }
        for (final MenuCategory category : menuCategories) {
            final MenuItem found = findInItems(category.getMenuItems(), url);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    private MenuItem findInItems(final List<MenuItem> items, final String url) {
        if (items == null) {
            return null;
        }
        for (final MenuItem item : items) {
            if (url.equals(item.getUrl())) {
                return item;
            }
            final MenuItem foundInChildren = findInItems(item.getMenuItems(), url);
            if (foundInChildren != null) {
                return foundInChildren;
            }
        }
        return null;
    }

    public List<MenuCategory> getMenuCategories() {
        return menuCategories;
    }

    /**
     * The first internal page of a category, used by the landing page to link its category cards.
     *
     * @param category the category label, e.g. "Input"
     * @return the URL (starting with "/") or null when the category has no internal page (yet)
     */
    public String getCategoryUrl(final String category) {
        final List<String> urls = new ArrayList<>();
        collectUrls(categoryItems.get(category), urls);
        return urls.isEmpty() ? null : urls.get(0);
    }

    /** Flattened internal URLs (starting with "/") of all menu items; external links are excluded. */
    List<String> allUrls() {
        final List<String> urls = new ArrayList<>();
        for (final MenuCategory category : menuCategories) {
            collectUrls(category.getMenuItems(), urls);
        }
        return urls;
    }

    private void collectUrls(final List<MenuItem> items, final List<String> urls) {
        if (items == null) {
            return;
        }
        for (final MenuItem item : items) {
            if (item.getUrl() != null && item.getUrl().startsWith("/")) {
                urls.add(item.getUrl());
            }
            collectUrls(item.getMenuItems(), urls);
        }
    }
}
