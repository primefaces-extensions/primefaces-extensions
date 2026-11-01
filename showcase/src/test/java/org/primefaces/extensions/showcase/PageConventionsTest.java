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
package org.primefaces.extensions.showcase;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.primefaces.extensions.showcase.util.taglib.TagInfo;
import org.primefaces.extensions.showcase.util.taglib.TagLibTestSupport;

/**
 * Static checks of the page conventions of the component pages under webapp/ui (pages below a {@code standalone/} folder are exempt).
 */
class PageConventionsTest {

    private static final Pattern WIDGET_VAR = Pattern.compile("\\bwidgetVar=\"([^\"#$]+)\"");
    private static final Pattern FORM_ID = Pattern.compile("<h:form\\b[^>]*?\\sid=\"([^\"#$]+)\"");
    private static final Pattern TAG_NAME = Pattern.compile("<ui:param\\s+name=\"tagName[23]?\"\\s+value=\"([^\"]*)\"");
    private static final Pattern LEGACY_INCLUDE = Pattern.compile("<ui:include\\b[^>]*src=\"[^\"]*/sections/");

    private static List<Path> pages() {
        return ShowcaseFiles.uiPages().stream()
                    .filter(p -> !p.toString().replace('\\', '/').contains("/standalone/"))
                    .collect(Collectors.toList());
    }

    private static List<String> duplicates(final Pattern pattern, final String content) {
        final Set<String> seen = new HashSet<>();
        final List<String> dups = new ArrayList<>();
        final Matcher m = pattern.matcher(content);
        while (m.find()) {
            if (!seen.add(m.group(1))) {
                dups.add(m.group(1));
            }
        }
        return dups;
    }

    private static int count(final String content, final String token) {
        int n = 0;
        for (int i = content.indexOf(token); i >= 0; i = content.indexOf(token, i + token.length())) {
            n++;
        }
        return n;
    }

    @Test
    void noDuplicateIdsOrWidgetVarsPerPage() {
        final List<String> problems = new ArrayList<>();
        for (final Path page : pages()) {
            final String content = ShowcaseFiles.read(page);
            for (final String dup : duplicates(WIDGET_VAR, content)) {
                problems.add(page.getFileName() + ": duplicate widgetVar '" + dup + "'");
            }
            for (final String dup : duplicates(FORM_ID, content)) {
                problems.add(page.getFileName() + ": duplicate h:form id '" + dup + "'");
            }
        }
        Assertions.assertTrue(problems.isEmpty(), String.join("\n", problems));
    }

    @Test
    void everyPageUsesTemplate() {
        final List<String> problems = new ArrayList<>();
        for (final Path page : pages()) {
            if (!ShowcaseFiles.read(page).contains("template=\"/WEB-INF/template.xhtml\"")) {
                problems.add(page.toString());
            }
        }
        Assertions.assertTrue(problems.isEmpty(), "Pages not using /WEB-INF/template.xhtml: " + problems);
    }

    @Test
    void everyPageHasSourceMarkers() {
        final List<String> problems = new ArrayList<>();
        for (final Path page : pages()) {
            final String content = ShowcaseFiles.read(page);
            final int start = count(content, "<!-- EXAMPLE-SOURCE-START -->");
            final int end = count(content, "<!-- EXAMPLE-SOURCE-END -->");
            if (start != 1 || end != 1 || content.indexOf("<!-- EXAMPLE-SOURCE-START -->") > content.indexOf("<!-- EXAMPLE-SOURCE-END -->")) {
                problems.add(page.getFileName() + " (START=" + start + ", END=" + end + ")");
            }
        }
        Assertions.assertTrue(problems.isEmpty(), "Pages without exactly one EXAMPLE-SOURCE-START/END pair: " + problems);
    }

    @Test
    void noLegacyIncludes() {
        final List<String> problems = new ArrayList<>();
        for (final Path page : pages()) {
            final String content = ShowcaseFiles.read(page);
            if (LEGACY_INCLUDE.matcher(content).find() || content.contains("showcase:getFileContent")) {
                problems.add(page.toString());
            }
        }
        Assertions.assertTrue(problems.isEmpty(), "Pages using legacy /sections/ includes or showcase:getFileContent: " + problems);
    }

    @Test
    void tagNamesExistInTaglib() {
        final List<Path> pages = pages();
        if (pages.isEmpty()) {
            return;
        }
        final Map<String, TagInfo> tags = TagLibTestSupport.provider().getTags();
        final List<String> problems = new ArrayList<>();
        for (final Path page : pages) {
            final Matcher m = TAG_NAME.matcher(ShowcaseFiles.read(page));
            while (m.find()) {
                if (!tags.containsKey(m.group(1))) {
                    problems.add(page.getFileName() + ": unknown tag '" + m.group(1) + "'");
                }
            }
        }
        Assertions.assertTrue(problems.isEmpty(), String.join("\n", problems));
    }
}
