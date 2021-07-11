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
package org.primefaces.extensions.component.scaffolding;

import javax.el.MethodExpression;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UICommand;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.FacesEvent;

/**
 * <code>Scaffolding</code> component.
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 * @since 10.0.3
 */
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "core.js")
public class Scaffolding extends UICommand {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Scaffolding";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.ScaffoldingRenderer";

    public static final String STYLE_CLASS = "ui-scaffolding";

    // @formatter:off
    @SuppressWarnings("java:S115")
    public enum PropertyKeys {
        ready,
        loader,
        style,
        styleClass,
        global,
        async
    }
    // @formatter:on

    public Scaffolding() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public boolean isReady() {
        return (Boolean) getStateHelper().eval(PropertyKeys.ready, false);
    }

    public void setReady(boolean ready) {
        getStateHelper().put(PropertyKeys.ready, ready);
    }

    public MethodExpression getLoader() {
        return (MethodExpression) getStateHelper().eval(PropertyKeys.loader, null);
    }

    public void setLoader(MethodExpression loader) {
        getStateHelper().put(PropertyKeys.loader, loader);
    }

    public String getStyle() {
        return (String) getStateHelper().eval(PropertyKeys.style, null);
    }

    public void setStyle(String style) {
        getStateHelper().put(PropertyKeys.style, style);
    }

    public String getStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.styleClass, null);
    }

    public void setStyleClass(String styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, styleClass);
    }

    public boolean isGlobal() {
        return (Boolean) getStateHelper().eval(PropertyKeys.global, true);
    }

    public void setGlobal(final boolean global) {
        getStateHelper().put(PropertyKeys.global, global);
    }

    public boolean isAsync() {
        return (Boolean) getStateHelper().eval(PropertyKeys.async, false);
    }

    public void setAsync(final boolean async) {
        getStateHelper().put(PropertyKeys.async, async);
    }

    @Override
    public void broadcast(final FacesEvent event) {
        if (event instanceof ActionEvent) {
            final FacesContext context = getFacesContext();
            final MethodExpression loader = getLoader();
            if (loader != null) {
                loader.invoke(context.getELContext(), new Object[] {});
            }
        }
    }

}
