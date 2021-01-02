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
package org.primefaces.extensions.showcase.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.extensions.component.social.Social;

/**
 * {@link Social} Controller.
 *
 * @author Melloware mellowaredev@gmail.com
 */
@Named
@ViewScoped
public class SocialMediaController implements Serializable {

    private static final long serialVersionUID = 20120224L;

    private int fontSize = 12;
    private String theme = "classic";
    private String showCount = "true";
    private boolean showLabel = true;

    public List<SelectItem> getThemes() {
        final List<SelectItem> results = new ArrayList<>();
        results.add(new SelectItem("classic", "classic"));
        results.add(new SelectItem("flat", "flat"));
        results.add(new SelectItem("minima", "minima"));
        results.add(new SelectItem("plain", "plain"));
        return results;
    }

    public List<SelectItem> getCountOptions() {
        final List<SelectItem> results = new ArrayList<>();
        results.add(new SelectItem("true", "display"));
        results.add(new SelectItem("false", "hide"));
        results.add(new SelectItem("inside", "inside"));
        return results;
    }

    public String getShowCount() {
        return showCount;
    }

    public void setShowCount(final String showCount) {
        this.showCount = showCount;
    }

    public boolean isShowLabel() {
        return showLabel;
    }

    public void setShowLabel(final boolean showLabel) {
        this.showLabel = showLabel;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(final int fontSize) {
        this.fontSize = fontSize;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(final String theme) {
        this.theme = theme;
    }

}
