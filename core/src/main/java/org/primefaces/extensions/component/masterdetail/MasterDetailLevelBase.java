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

import jakarta.faces.component.UIComponentBase;

import org.primefaces.cdk.api.FacesComponentBase;
import org.primefaces.cdk.api.Property;

/**
 * <code>MasterDetailLevel</code> component base class.
 *
 * @author Oleg Varaksin / last modified by Melloware
 * @since 0.2
 */
@FacesComponentBase
public abstract class MasterDetailLevelBase extends UIComponentBase {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.MasterDetailLevel";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";

    public MasterDetailLevelBase() {
        setRendererType(null);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Property(description = "Level number (1-based).")
    public abstract Integer getLevel();

    @Property(description = "Request-scoped variable name for the context value at this level.")
    public abstract String getContextVar();

    @Property(description = "Label for this level (e.g. in breadcrumb).")
    public abstract String getLevelLabel();

    @Property(description = "Whether this level is disabled in navigation.", defaultValue = "false")
    public abstract boolean isLevelDisabled();
}
