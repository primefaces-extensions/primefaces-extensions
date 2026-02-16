/*
 * Copyright (c) 2011-2025 PrimeFaces Extensions
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
package org.primefaces.extensions.component.codemirror;

import jakarta.el.MethodExpression;
import jakarta.faces.component.html.HtmlInputTextarea;

import org.primefaces.cdk.api.FacesComponentBase;
import org.primefaces.cdk.api.Property;
import org.primefaces.component.api.InputAware;
import org.primefaces.component.api.Widget;
import org.primefaces.extensions.component.api.SanitizerAware;

@FacesComponentBase
public abstract class CodeMirrorBase extends HtmlInputTextarea implements InputAware, SanitizerAware, Widget {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.CodeMirror";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.CodeMirrorRenderer";

    public CodeMirrorBase() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Property(description = "The theme to style the editor with.")
    public abstract String getTheme();

    @Property(description = "The mode to use. When not given, this will default to the first mode that was loaded. It may be a string, "
                + "which either simply names the mode or is a MIME type associated with the mode. Alternatively, it may be an object containing " +
                "configuration options for the mode, with a name property that names the mode (for example {name: \"javascript\", json: true}).")
    public abstract String getMode();

    @Property(description = "How many spaces a block (whatever that means in the edited language) should be indented. The default is 2.")
    public abstract Integer getIndentUnit();

    @Property(description = "Whether to use the context-sensitive indentation that the mode provides (or just indent the same as the line before).",
                defaultValue = "true")
    public abstract boolean isSmartIndent();

    @Property(description = "The width of a tab character. Defaults to 4.")
    public abstract Integer getTabSize();

    @Property(description = "If Set false, indent using spaces instead of tabs.", defaultValue = "true")
    public abstract boolean isIndentWithTabs();

    @Property(description = "Configures whether the editor should re-indent the current line when a character is typed that might change its proper "
                + "indentation (only works if the mode supports indentation).", defaultValue = "true")
    public abstract boolean isElectricChars();

    @Property(description = "Configures the keymap to use. The default is \"default\", which is the only keymap defined in codemirror.js itself. " +
                "Extra keymaps are found in the keymap directory.")
    public abstract String getKeyMap();

    @Property(description = "Whether CodeMirror should scroll or wrap for long lines.", defaultValue = "false")
    public abstract boolean isLineWrapping();

    @Property(description = "Whether to show line numbers to the left of the editor.", defaultValue = "false")
    public abstract boolean isLineNumbers();

    @Property(description = "At which number to start counting lines.", defaultValue = "1")
    public abstract Integer getFirstLineNumber();

    @Property(description = "Can be used to force a 'gutter' (empty space on the left of the editor) to be shown even when no line numbers are active. " +
                "This is useful for setting markers.", defaultValue = "false")
    public abstract boolean isGutter();

    @Property(description = "When enabled (off by default), this will make the gutter stay visible when the document is scrolled horizontally.",
                defaultValue = "false")
    public abstract boolean isFixedGutter();

    @Property(description = "Determines whether brackets are matched whenever the cursor is moved next to a bracket.", defaultValue = "false")
    public abstract boolean isMatchBrackets();

    @Property(description = "Highlighting is done by a pseudo background-thread that will work for workTime milliseconds, and then use timeout to sleep for "
                +
                "workDelay milliseconds. Defaults are 200 and 300, you can change these options to make the highlighting more or less aggressive.")
    public abstract Integer getWorkTime();

    @Property(description = "Highlighting is done by a pseudo background-thread that will work for workTime milliseconds, and then use timeout to sleep for "
                +
                "workDelay milliseconds. Defaults are 200 and 300, you can change these options to make the highlighting more or less aggressive.")
    public abstract Integer getWorkDelay();

    @Property(description = "Indicates how quickly CodeMirror should poll its input textarea for changes. Most input is captured by events, but some things, " +
                "like IME input on some browsers, doesn't generate events that allow CodeMirror to properly detect it. Thus, it polls. Default is 100 millis.")
    public abstract Integer getPollInterval();

    @Property(description = "The maximum number of undo levels that the editor stores. Defaults to 40.")
    public abstract Integer getUndoDepth();

    @Property(description = "Can be used to specify extra keybindings for the editor. When given, should be an object with property names " +
                "like Ctrl-A, Home, and Ctrl-Alt-Left.")
    public abstract String getExtraKeys();

    @Property(description = "Method providing suggestions.")
    public abstract MethodExpression getCompleteMethod();

    @Property(description = "Component(s) to process partially instead of whole view.")
    public abstract String getProcess();

    @Property(description = "Javascript handler to execute before ajax request is begins.")
    public abstract String getOnstart();

    @Property(description = "Javascript handler to execute when ajax request is completed.")
    public abstract String getOncomplete();

    @Property(description = "Javascript handler to execute when ajax request fails.")
    public abstract String getOnerror();

    @Property(description = "Javascript handler to execute when ajax request succeeds.")
    public abstract String getOnsuccess();

    @Property(description = "Global ajax requests are listened by ajaxStatus component, setting global to false will not trigger ajaxStatus.",
                defaultValue = "true")
    public abstract boolean isGlobal();

    @Property(description = "When set to true, ajax requests are not queued. Default value: false.", defaultValue = "false")
    public abstract boolean isAsync();

    @Property(description = "Defines if the content of the component should be escaped or not.", defaultValue = "true")
    public abstract boolean isEscape();

    @Property(description = "Defines if the suggestions should be escaped or not.", defaultValue = "true")
    public abstract boolean isEscapeSuggestions();
}
