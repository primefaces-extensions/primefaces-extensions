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
package org.primefaces.extensions.component.gravatar;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIOutput;

public class Gravatar extends UIOutput {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Gravatar";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.GravatarRenderer";
    static final List<String> NOT_FOUND_VALUES = new ArrayList<>();

    static {
        NOT_FOUND_VALUES.add("default");
        NOT_FOUND_VALUES.add("mm");
        NOT_FOUND_VALUES.add("identicon");
        NOT_FOUND_VALUES.add("monsterid");
        NOT_FOUND_VALUES.add("wavatar");
        NOT_FOUND_VALUES.add("retro");
        NOT_FOUND_VALUES.add("blank");
    }

    @SuppressWarnings("java:S115")
    protected enum PropertyKeys {
        notFound, size, style, qrCode, secure;
    }

    public Gravatar() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public Integer getSize() {
        return (Integer) this.getStateHelper().eval(PropertyKeys.size, null);
    }

    public void setSize(Integer size) {
        this.getStateHelper().put(PropertyKeys.size, size);
    }

    public String getStyle() {
        return (String) this.getStateHelper().eval(PropertyKeys.style, null);
    }

    public void setStyle(String style) {
        this.getStateHelper().put(PropertyKeys.style, style);
    }

    public boolean isQrCode() {
        return Boolean.TRUE.equals(this.getStateHelper().eval(PropertyKeys.qrCode, false));
    }

    public void setQrCode(boolean qrCode) {
        this.getStateHelper().put(PropertyKeys.qrCode, qrCode);
    }

    public boolean isSecure() {
        return Boolean.TRUE.equals(this.getStateHelper().eval(PropertyKeys.secure, true));
    }

    public void setSecure(boolean secure) {
        this.getStateHelper().put(PropertyKeys.secure, secure);
    }

    public String getNotFound() {
        return (String) this.getStateHelper().eval(PropertyKeys.notFound, null);
    }

    public void setNotFound(String notFound) {
        this.getStateHelper().put(PropertyKeys.notFound, notFound);
    }

}
