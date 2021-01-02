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
package org.primefaces.extensions.component.social;

import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponentBase;
import javax.faces.component.behavior.ClientBehaviorHolder;

import org.primefaces.component.api.Widget;

/**
 * <code>Social</code> component.
 *
 * @author Melloware mellowaredev@gmail.com
 * @since 6.2
 */
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces-extensions", name = "social/social.css")
@ResourceDependency(library = "primefaces-extensions", name = "social/social.js")
public class Social extends UIComponentBase implements ClientBehaviorHolder, Widget {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Social";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.SocialRenderer";

    @SuppressWarnings("java:S115")
    protected enum PropertyKeys {

        // @formatter:off
        widgetVar,
        url,
        text,
        showLabel,
        showCount,
        shares,
        shareIn,
        theme,
        emailTo,
        twitterUsername,
        twitterHashtags,
        pinterestMedia,
        style,
        styleClass,
        onclick,
        onmouseenter,
        onmouseleave
        // @formatter:on
    }

    /**
     * Default constructor
     */
    public Social() {
        setRendererType(DEFAULT_RENDERER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getWidgetVar() {
        return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void setWidgetVar(final String _widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, _widgetVar);
    }

    public String getShares() {
        return (String) getStateHelper().eval(PropertyKeys.shares, null);
    }

    public void setShares(final String _text) {
        getStateHelper().put(PropertyKeys.shares, _text);
    }

    public String getUrl() {
        return (String) getStateHelper().eval(PropertyKeys.url, null);
    }

    public void setUrl(final String _url) {
        getStateHelper().put(PropertyKeys.url, _url);
    }

    public String getText() {
        return (String) getStateHelper().eval(PropertyKeys.text, null);
    }

    public void setText(final String _text) {
        getStateHelper().put(PropertyKeys.text, _text);
    }

    public String getShareIn() {
        return (String) getStateHelper().eval(PropertyKeys.shareIn, "blank");
    }

    public void setShareIn(final String _shareIn) {
        getStateHelper().put(PropertyKeys.shareIn, _shareIn);
    }

    public String getTheme() {
        return (String) getStateHelper().eval(PropertyKeys.theme, "classic");
    }

    public void setTheme(final String _theme) {
        getStateHelper().put(PropertyKeys.theme, _theme);
    }

    public void setShowLabel(final boolean _showLabel) {
        getStateHelper().put(PropertyKeys.showLabel, _showLabel);
    }

    public boolean isShowLabel() {
        return (Boolean) getStateHelper().eval(PropertyKeys.showLabel, true);
    }

    public void setShowCount(final String _showCount) {
        getStateHelper().put(PropertyKeys.showCount, _showCount);
    }

    public String getShowCount() {
        return (String) getStateHelper().eval(PropertyKeys.showCount, "true");
    }

    public String getStyle() {
        return (String) getStateHelper().eval(PropertyKeys.style, null);
    }

    public void setStyle(final String _style) {
        getStateHelper().put(PropertyKeys.style, _style);
    }

    public String getStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.styleClass, null);
    }

    public void setStyleClass(final String _styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, _styleClass);
    }

    public String getEmailTo() {
        return (String) getStateHelper().eval(PropertyKeys.emailTo, null);
    }

    public void setEmailTo(final String _emailTo) {
        getStateHelper().put(PropertyKeys.emailTo, _emailTo);
    }

    public String getTwitterUsername() {
        return (String) getStateHelper().eval(PropertyKeys.twitterUsername, null);
    }

    public void setTwitterUsername(final String _twitterUsername) {
        getStateHelper().put(PropertyKeys.twitterUsername, _twitterUsername);
    }

    public String getTwitterHashtags() {
        return (String) getStateHelper().eval(PropertyKeys.twitterHashtags, null);
    }

    public void setTwitterHashtags(final String _twitterHashtags) {
        getStateHelper().put(PropertyKeys.twitterHashtags, _twitterHashtags);
    }

    public String getPinterestMedia() {
        return (String) getStateHelper().eval(PropertyKeys.pinterestMedia, null);
    }

    public void setPinterestMedia(final String _pinterestMedia) {
        getStateHelper().put(PropertyKeys.pinterestMedia, _pinterestMedia);
    }

    public String getOnclick() {
        return (String) getStateHelper().eval(PropertyKeys.onclick, null);
    }

    public void setOnclick(final String _onclick) {
        getStateHelper().put(PropertyKeys.onclick, _onclick);
    }

    public String getOnmouseenter() {
        return (String) getStateHelper().eval(PropertyKeys.onmouseenter, null);
    }

    public void setOnmouseenter(final String _onmouseenter) {
        getStateHelper().put(PropertyKeys.onmouseenter, _onmouseenter);
    }

    public String getOnmouseleave() {
        return (String) getStateHelper().eval(PropertyKeys.onmouseleave, null);
    }

    public void setOnmouseleave(final String _onmouseleave) {
        getStateHelper().put(PropertyKeys.onmouseleave, _onmouseleave);
    }

}
