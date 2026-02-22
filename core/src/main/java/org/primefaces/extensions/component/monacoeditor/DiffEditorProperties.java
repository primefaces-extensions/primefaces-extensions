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
package org.primefaces.extensions.component.monacoeditor;

import org.primefaces.cdk.api.Property;

/**
 * Base properties for both the framed and the inline Monaco diff editor widget.
 *
 * @since 11.1.0
 */
public interface DiffEditorProperties {

    @Property(description = "Language mode for the diff editor.")
    String getLanguage();

    @Property(description = "Whether the original (left) editor is disabled.", defaultValue = "true")
    boolean isOriginalDisabled();

    @Property(description = "Language for the original editor.")
    String getOriginalLanguage();

    @Property(description = "Whether the original editor is readonly.", defaultValue = "false")
    boolean isOriginalReadonly();

    @Property(description = "Whether the original editor is required.", defaultValue = "false")
    boolean isOriginalRequired();

    @Property(description = "Tabindex for the original editor.")
    String getOriginalTabindex();

    @Property(description = "Client-side script for original blur.")
    String getOnoriginalblur();

    @Property(description = "Client-side script for original change.")
    String getOnoriginalchange();

    @Property(description = "Client-side script for original focus.")
    String getOnoriginalfocus();

    @Property(description = "Client-side script for original keydown.")
    String getOnoriginalkeydown();

    @Property(description = "Client-side script for original keyup.")
    String getOnoriginalkeyup();

    @Property(description = "Client-side script for original mouseup.")
    String getOnoriginalmouseup();

    @Property(description = "Client-side script for original mousedown.")
    String getOnoriginalmousedown();

    @Property(description = "Client-side script for original mousemove.")
    String getOnoriginalmousemove();

    @Property(description = "Client-side script for original paste.")
    String getOnoriginalpaste();

    @Property(description = "Directory for the original model.", defaultValue = "")
    String getOriginalDirectory();

    @Property(description = "Extension for the original model.", defaultValue = "")
    String getOriginalExtension();

    @Property(description = "Basename for the original model.", defaultValue = "")
    String getOriginalBasename();

    @Property(description = "Scheme for the original model.", defaultValue = "inmemory")
    String getOriginalScheme();
}
