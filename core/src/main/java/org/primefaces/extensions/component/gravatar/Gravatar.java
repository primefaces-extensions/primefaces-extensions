/*
 * Copyright 2011-2020 PrimeFaces Extensions
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
