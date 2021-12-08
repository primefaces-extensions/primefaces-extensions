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
package org.primefaces.extensions.component.keynote;

import javax.faces.component.UIComponentBase;

import org.primefaces.extensions.model.keynote.KeynoteItem;

public class UIKeynoteItem extends UIComponentBase {

    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";

    protected enum PropertyKeys {
        // @formatter:off
        backgroundColor,
        backgroundImage,
        backgroundSize,
        backgroundPosition,
        backgroundRepeat,
        backgroundOpacity,
        backgroundVideo,
        backgroundVideoLoop,
        backgroundVideoMuted,
        markdown,
        note,
        separator,
        separatorVertical,
        type,
        styleClass
        // @formatter:on
    }

    public UIKeynoteItem() {
        setRendererType(null);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getBackgroundColor() {
        return (String) getStateHelper().eval(PropertyKeys.backgroundColor, null);
    }

    public void setBackgroundColor(final String backgroundColor) {
        getStateHelper().put(PropertyKeys.backgroundColor, backgroundColor);
    }

    public String getBackgroundImage() {
        return (String) getStateHelper().eval(PropertyKeys.backgroundImage, null);
    }

    public void setBackgroundImage(final String backgroundImage) {
        getStateHelper().put(PropertyKeys.backgroundImage, backgroundImage);
    }

    public String getBackgroundSize() {
        return (String) getStateHelper().eval(PropertyKeys.backgroundSize, "cover");
    }

    public void setBackgroundSize(final String backgroundSize) {
        getStateHelper().put(PropertyKeys.backgroundSize, backgroundSize);
    }

    public String getBackgroundPosition() {
        return (String) getStateHelper().eval(PropertyKeys.backgroundPosition, "center");
    }

    public void setBackgroundPosition(final String backgroundPosition) {
        getStateHelper().put(PropertyKeys.backgroundPosition, backgroundPosition);
    }

    public String getBackgroundRepeat() {
        return (String) getStateHelper().eval(PropertyKeys.backgroundRepeat, "no-repeat");
    }

    public void setBackgroundRepeat(final String backgroundRepeat) {
        getStateHelper().put(PropertyKeys.backgroundRepeat, backgroundRepeat);
    }

    public Double getBackgroundOpacity() {
        return (Double) getStateHelper().eval(PropertyKeys.backgroundOpacity, 1.0);
    }

    public void setBackgroundOpacity(final Double backgroundOpacity) {
        getStateHelper().put(PropertyKeys.backgroundOpacity, backgroundOpacity);
    }

    public String getBackgroundVideo() {
        return (String) getStateHelper().eval(PropertyKeys.backgroundVideo, null);
    }

    public void setBackgroundVideo(final String backgroundVideo) {
        getStateHelper().put(PropertyKeys.backgroundVideo, backgroundVideo);
    }

    public Boolean isBackgroundVideoLoop() {
        return (Boolean) getStateHelper().eval(PropertyKeys.backgroundVideoLoop, false);
    }

    public void setBackgroundVideoLoop(final Boolean backgroundVideoLoop) {
        getStateHelper().put(PropertyKeys.backgroundVideoLoop, backgroundVideoLoop);
    }

    public Boolean isBackgroundVideoMuted() {
        return (Boolean) getStateHelper().eval(PropertyKeys.backgroundVideoMuted, false);
    }

    public void setBackgroundVideoMuted(final Boolean backgroundVideoMuted) {
        getStateHelper().put(PropertyKeys.backgroundVideoMuted, backgroundVideoMuted);
    }

    public Boolean isMarkdown() {
        return (Boolean) getStateHelper().eval(PropertyKeys.markdown, false);
    }

    public void setMarkdown(final Boolean markdown) {
        getStateHelper().put(PropertyKeys.markdown, markdown);
    }

    public String getNote() {
        return (String) getStateHelper().eval(PropertyKeys.note, null);
    }

    public void setNote(final String note) {
        getStateHelper().put(PropertyKeys.note, note);
    }

    public String getSeparator() {
        return (String) getStateHelper().eval(PropertyKeys.separator, "^---$");
    }

    public void setSeparator(final String separator) {
        getStateHelper().put(PropertyKeys.separator, separator);
    }

    public String getSeparatorVertical() {
        return (String) getStateHelper().eval(PropertyKeys.separatorVertical, null);
    }

    public void setSeparatorVertical(final String separatorVertical) {
        getStateHelper().put(PropertyKeys.separatorVertical, separatorVertical);
    }

    public String getType() {
        return (String) getStateHelper().eval(PropertyKeys.type, KeynoteItem.DEFAULT_TYPE);
    }

    public void setType(final String type) {
        getStateHelper().put(PropertyKeys.type, type);
    }

    public String getStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.styleClass, null);
    }

    public void setStyleClass(final String styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, styleClass);
    }
}
