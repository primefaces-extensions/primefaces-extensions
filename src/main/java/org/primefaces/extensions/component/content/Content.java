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
package org.primefaces.extensions.component.content;

import java.util.Locale;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import org.primefaces.util.LocaleUtils;

/**
 * <code>Content</code> component.
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 * @since 8.0.3
 */
public class Content extends UIComponentBase {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Content";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.ContentRenderer";

    // @formatter:off
    public enum PropertyKeys {
        src,
        variant,
        locale,
        escape,
        evalEl,
        markdown
    }
    // @formatter:on

    public Content() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public Locale calculateLocale(FacesContext facesContext) {
        return LocaleUtils.resolveLocale(facesContext, getLocale(), getClientId(facesContext));
    }

    public String getSrc() {
        return (String) getStateHelper().eval(PropertyKeys.src, null);
    }

    public void setSrc(final String src) {
        getStateHelper().put(PropertyKeys.src, src);
    }

    public String getVariant() {
        return (String) getStateHelper().eval(PropertyKeys.variant, null);
    }

    public void setVariant(final String variant) {
        getStateHelper().put(PropertyKeys.variant, variant);
    }

    public Object getLocale() {
        return getStateHelper().eval(PropertyKeys.locale, null);
    }

    public void setLocale(final Object locale) {
        getStateHelper().put(PropertyKeys.locale, locale);
    }

    public boolean isEscape() {
        return (Boolean) getStateHelper().eval(PropertyKeys.escape, true);
    }

    public void setEscape(final boolean escape) {
        getStateHelper().put(PropertyKeys.escape, escape);
    }

    public boolean isEvalEl() {
        return (Boolean) getStateHelper().eval(PropertyKeys.evalEl, false);
    }

    public void setEvalEl(final boolean evalEl) {
        getStateHelper().put(PropertyKeys.evalEl, evalEl);
    }

    public boolean isMarkdown() {
        return (Boolean) getStateHelper().eval(PropertyKeys.markdown, false);
    }

    public void setMarkdown(final boolean markdown) {
        getStateHelper().put(PropertyKeys.markdown, markdown);
    }

}
