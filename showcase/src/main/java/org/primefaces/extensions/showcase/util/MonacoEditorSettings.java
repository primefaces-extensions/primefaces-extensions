/*
 * Copyright (c) 2011-2021 PrimeFaces Extensions
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

import javax.faces.model.SelectItem;

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
                                new EditorTokenThemeRule().setToken("comment").setForeground("ffa500").setFontStyle("italic underline"), //
                                new EditorTokenThemeRule().setToken("comment.js").setForeground("008800").setFontStyle("bold"), //
                                new EditorTokenThemeRule().setToken("comment.css").setForeground("0000ff") //
                    ));
    }

    /**
     * @return A list of all locales supported by the Monaco editor natively.
     */
    public static List<Locale> getBuiltInLocales() {
        return Arrays.asList( //
                    new Locale("cs"),
                    new Locale("de"),
                    new Locale("en"),
                    new Locale("es"),
                    new Locale("fr"),
                    new Locale("it"),
                    new Locale("ja"),
                    new Locale("ko"),
                    new Locale("pl"),
                    new Locale("pt", "BR"),
                    new Locale("ru"),
                    new Locale("tr"),
                    new Locale("zh", "CN"),
                    new Locale("zh", "TW"));
    }

    public static EditorOptions createEditorOptionsExtender() {
        EditorOptions opts = new EditorOptions();
        opts.setLanguage(ELanguage.JAVASCRIPT);
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
     * @return A list of all examples available in the extender showcase.
     */
    public static List<SelectItem> createEditorExamples() {
        return Arrays.asList( //
                    new SelectItem("options", "Adjust editor options"), //
                    new SelectItem("localstorage", "Editor overrides (Storage service)"), //
                    new SelectItem("jsonschema", "JSON Schema"), //
                    new SelectItem("jquery", "Type declarations (JQuery)") //
        );
    }
}
