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
package org.primefaces.extensions.showcase.util;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.model.SelectItem;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.extensions.model.monacoeditor.DiffEditorOptions;
import org.primefaces.extensions.model.monacoeditor.ECursorStyle;
import org.primefaces.extensions.model.monacoeditor.ELanguage;
import org.primefaces.extensions.model.monacoeditor.ERenderWhitespace;
import org.primefaces.extensions.model.monacoeditor.ETheme;
import org.primefaces.extensions.model.monacoeditor.EditorOptions;
import org.primefaces.extensions.model.monacoeditor.EditorRulerOption;
import org.primefaces.extensions.model.monacoeditor.EditorStandaloneTheme;
import org.primefaces.extensions.model.monacoeditor.EditorTokenThemeRule;

public class MonacoEditorSettings {
    /**
     * @return The custom theme for the showcase illustrating how to add a custom theme to the Monaco editor.
     */
    public static EditorStandaloneTheme createDemoCustomTheme() {
        return new EditorStandaloneTheme() //
                    .setBase(ETheme.VS) //
                    .setInherit(true) //
                    .setRules(Arrays.asList( //
                                new EditorTokenThemeRule().setToken("comment").setForeground("ffa500")
                                            .setFontStyle("italic underline"), //
                                new EditorTokenThemeRule().setToken("comment.js").setForeground("008800")
                                            .setFontStyle("bold"), //
                                new EditorTokenThemeRule().setToken("comment.css").setForeground("0000ff") //
                    ));
    }

    /**
     * @return A list of all locales supported by the Monaco editor natively.
     */
    public static List<Locale> getBuiltInLocales() {
        return Arrays.asList( //
                    new Locale("cs"), new Locale("de"), new Locale("en"), new Locale("es"), new Locale("fr"),
                    new Locale("it"), new Locale("ja"),
                    new Locale("ko"),
                    new Locale("pl"), new Locale("pt", "BR"), new Locale("ru"), new Locale("tr"),
                    new Locale("zh", "CN"), new Locale("zh", "TW"));
    }

    public static EditorOptions createEditorOptionsExtender() {
        final EditorOptions opts = new EditorOptions();
        opts.setLanguage(ELanguage.JAVASCRIPT);
        opts.setFontSize(12);
        opts.setTheme(ETheme.VS);
        opts.setCursorStyle(ECursorStyle.BLOCK);
        opts.setTabSize(2);
        opts.setUseTabStops(false);
        opts.setRulers(Arrays.asList( //
                    new EditorRulerOption().setColumn(60).setColor("#ccc"), //
                    new EditorRulerOption().setColumn(80).setColor("#c33") //
        ));
        opts.setRenderWhitespace(ERenderWhitespace.ALL);
        return opts;
    }

    public static EditorOptions createEditorOptionsCss() {
        final EditorOptions opts = new EditorOptions();
        opts.setLanguage(ELanguage.CSS);
        opts.setFontSize(12);
        opts.setTheme(ETheme.VS);
        opts.setCursorStyle(ECursorStyle.BLOCK);
        opts.setTabSize(2);
        opts.setUseTabStops(false);
        opts.setRulers(Arrays.asList( //
                    new EditorRulerOption().setColumn(60).setColor("#ccc"), //
                    new EditorRulerOption().setColumn(80).setColor("#c33") //
        ));
        opts.setRenderWhitespace(ERenderWhitespace.ALL);
        return opts;
    }

    public static DiffEditorOptions createEditorOptionsExtenderDiff() {
        final DiffEditorOptions opts = new DiffEditorOptions();
        opts.setFontSize(12);
        opts.setTheme(ETheme.VS);
        opts.setCursorStyle(ECursorStyle.BLOCK);
        opts.setRulers(Arrays.asList( //
                    new EditorRulerOption().setColumn(60).setColor("#ccc"), //
                    new EditorRulerOption().setColumn(80).setColor("#c33") //
        ));
        opts.setRenderWhitespace(ERenderWhitespace.ALL);
        return opts;
    }

    /**
     * @return A list of all examples available in the extender showcase for the code editor.
     */
    public static List<SelectItem> createEditorExamples() {
        return Arrays.asList( //
                    new SelectItem("options", "Adjust editor options"), //
                    new SelectItem("localstorage", "Editor overrides (Storage service)"), //
                    new SelectItem("jsonschema", "JSON Schema"), //
                    new SelectItem("jquery", "Type declarations (JQuery)") //
        );
    }

    /**
     * @return A list of all examples for available in the extender showcase for the diff editor.
     */
    public static List<SelectItem> createEditorExamplesDiff() {
        return Arrays.asList( //
                    new SelectItem("options", "Adjust editor options"), //
                    new SelectItem("diffnavi", "Diff navigator"), //
                    new SelectItem("localstorage", "Editor overrides (Storage service)"), //
                    new SelectItem("jsonschema", "JSON Schema"), //
                    new SelectItem("jquery", "Type declarations (JQuery)") //
        );
    }

    /**
     * Applies random modifications to the original, for showcasing the diff editor.
     *
     * @param original Original string.
     * @return A modified string with random modifications.
     */
    @SuppressWarnings("java:S2245")
    public static String deriveModifiedContent(String original) {
        if (original == null || original.isEmpty()) {
            return original;
        }
        original = replaceRandomOccurence(original, 20, Pattern.compile("\\d"), //
                    () -> String.valueOf((char) RandomUtils.nextInt(48, 58)) //
        );

        original = replaceRandomOccurence(original, 20, Pattern.compile(" "), //
                    () -> StringUtils.repeat(" ", RandomUtils.nextInt(2, 5)) //
        );
        return original;
    }

    @SuppressWarnings("java:S2245")
    private static String replaceRandomOccurence(String value, int count, Pattern search,
                final Supplier<String> replacement) {
        for (int i = 1; i <= count; i += 1) {
            final int pos = (int) Math.floor(Math.random() * value.length());
            final Matcher matcher = search.matcher(value);
            boolean matches = matcher.find(pos);
            if (!matches) {
                matches = matcher.find(0);
            }
            if (!matches) {
                // No more matches left to replace
                break;
            }
            final int start = matcher.start();
            final int end = matcher.end();
            final String repl = replacement.get();
            value = end >= value.length() //
                        ? value.substring(0, start) + repl //
                        : value.substring(0, start) + repl + value.substring(end);
        }
        return value;
    }
}
