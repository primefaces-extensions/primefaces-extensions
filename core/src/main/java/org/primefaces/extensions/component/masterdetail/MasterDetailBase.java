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
package org.primefaces.extensions.component.masterdetail;

import jakarta.el.MethodExpression;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIComponentBase;

import org.primefaces.cdk.api.FacesComponentBase;
import org.primefaces.cdk.api.Facet;
import org.primefaces.cdk.api.Property;
import org.primefaces.component.api.StyleAware;

/**
 * <code>MasterDetail</code> component base class.
 *
 * @author Oleg Varaksin / last modified by Melloware
 * @since 0.2
 */
@FacesComponentBase
public abstract class MasterDetailBase extends UIComponentBase implements StyleAware {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.MasterDetail";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.MasterDetailRenderer";

    public MasterDetailBase() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Facet(description = "Header facet")
    public abstract UIComponent getHeaderFacet();

    @Facet(description = "Footer facet")
    public abstract UIComponent getFooterFacet();

    @Property(description = "Current detail level (1-based).", defaultValue = "1")
    public abstract int getLevel();

    @Property(description = "Context value for the current level.")
    public abstract Object getContextValue();

    @Property(description = "Method expression invoked when navigating to a detail level.")
    public abstract MethodExpression getSelectLevelListener();

    @Property(description = "Whether to show the breadcrumb.", defaultValue = "true")
    public abstract boolean isShowBreadcrumb();

    @Property(description = "Whether to show all breadcrumb items.", defaultValue = "false")
    public abstract boolean isShowAllBreadcrumbItems();

    @Property(description = "Whether to show breadcrumb on first level.", defaultValue = "true")
    public abstract boolean isShowBreadcrumbFirstLevel();

    @Property(description = "Whether to render breadcrumb above the header.", defaultValue = "true")
    public abstract boolean isBreadcrumbAboveHeader();
}
