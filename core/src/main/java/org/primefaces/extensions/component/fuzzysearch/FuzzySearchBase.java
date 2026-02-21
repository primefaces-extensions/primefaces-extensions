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
package org.primefaces.extensions.component.fuzzysearch;

import jakarta.faces.component.UISelectOne;

import org.primefaces.cdk.api.FacesBehaviorEvent;
import org.primefaces.cdk.api.FacesBehaviorEvents;
import org.primefaces.cdk.api.FacesComponentBase;
import org.primefaces.cdk.api.Property;
import org.primefaces.component.api.InputHolder;
import org.primefaces.component.api.PrimeSelect;
import org.primefaces.component.api.StyleAware;
import org.primefaces.component.api.Widget;
import org.primefaces.event.SelectEvent;

/**
 * <code>FuzzySearch</code> component base class.
 *
 * @author https://github.com/aripddev
 * @since 8.0.1
 */
@FacesComponentBase
@FacesBehaviorEvents({
            @FacesBehaviorEvent(name = "change", event = SelectEvent.class,
                        description = "Fires when selection changes.", defaultEvent = true)
})
public abstract class FuzzySearchBase extends UISelectOne implements Widget, InputHolder, StyleAware, PrimeSelect {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.FuzzySearch";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.FuzzySearchRenderer";

    public FuzzySearchBase() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Property(description = "When true, component is disabled.", defaultValue = "false")
    public abstract boolean isDisabled();

    @Property(description = "A localized user presentable name.")
    public abstract String getLabel();

    @Property(description = "Client-side callback when selection changes.")
    public abstract String getOnchange();

    @Property(description = "Tab index for accessibility.")
    public abstract String getTabindex();

    @Property(description = "When true, selection can be cleared.", defaultValue = "true")
    public abstract boolean isUnselectable();

    @Property(description = "Inline style for result items.")
    public abstract String getResultStyle();

    @Property(description = "Style class for result items.")
    public abstract String getResultStyleClass();

    @Property(description = "Placeholder text for the search input.")
    public abstract String getPlaceholder();

    @Property(description = "When true, matching text is highlighted.", defaultValue = "true")
    public abstract boolean isHighlight();

    @Property(description = "When true, list items are shown before search.", defaultValue = "false")
    public abstract boolean isListItemsAtTheBeginning();
}
