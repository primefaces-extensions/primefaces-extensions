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
package org.primefaces.extensions.component.keynote;

import jakarta.faces.component.UIComponentBase;

import org.primefaces.cdk.api.FacesComponentBase;
import org.primefaces.cdk.api.Property;

/**
 * <code>UIKeynoteItem</code> component base class.
 *
 * @author Oleg Varaksin / last modified by Melloware
 * @since 6.3
 */
@FacesComponentBase
public abstract class UIKeynoteItemBase extends UIComponentBase {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.UIKeynoteItem";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.KeynoteItemRenderer";

    public UIKeynoteItemBase() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Property(description = "Background color for the slide.")
    public abstract String getBackgroundColor();

    @Property(description = "Background image URL for the slide.")
    public abstract String getBackgroundImage();

    @Property(description = "CSS background-size value.", defaultValue = "cover")
    public abstract String getBackgroundSize();

    @Property(description = "CSS background-position value.", defaultValue = "center")
    public abstract String getBackgroundPosition();

    @Property(description = "CSS background-repeat value.", defaultValue = "no-repeat")
    public abstract String getBackgroundRepeat();

    @Property(description = "Background opacity (0.0 to 1.0).", defaultValue = "1.0")
    public abstract Double getBackgroundOpacity();

    @Property(description = "Background video URL for the slide.")
    public abstract String getBackgroundVideo();

    @Property(description = "Whether the background video loops.", defaultValue = "false")
    public abstract boolean isBackgroundVideoLoop();

    @Property(description = "Whether the background video is muted.", defaultValue = "false")
    public abstract boolean isBackgroundVideoMuted();

    @Property(description = "Whether slide content is Markdown.", defaultValue = "false")
    public abstract boolean isMarkdown();

    @Property(description = "Speaker note text for the slide.")
    public abstract String getNote();

    @Property(description = "Regex for horizontal slide separator (Markdown).", defaultValue = "^---$")
    public abstract String getSeparator();

    @Property(description = "Regex for vertical slide separator (Markdown).")
    public abstract String getSeparatorVertical();

    @Property(description = "Style class for the slide section.")
    public abstract String getStyleClass();

    @Property(description = "Item type identifier; must match a KeynoteItem type in the model.", defaultValue = "default")
    public abstract String getType();

    @Property(description = "Reveal.js visibility condition for the slide.")
    public abstract String getVisibility();
}
