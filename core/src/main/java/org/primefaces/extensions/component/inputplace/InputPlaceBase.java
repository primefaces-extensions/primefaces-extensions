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
package org.primefaces.extensions.component.inputplace;

import org.primefaces.cdk.api.FacesBehaviorEvent;
import org.primefaces.cdk.api.FacesBehaviorEvents;
import org.primefaces.cdk.api.FacesComponentBase;
import org.primefaces.cdk.api.Property;
import org.primefaces.component.api.AbstractPrimeHtmlInputText;
import org.primefaces.component.api.Widget;
import org.primefaces.event.SelectEvent;

/**
 * <code>InputPlace</code> component base class.
 *
 * @since 14.0.0
 */
@FacesComponentBase
@FacesBehaviorEvents({
            @FacesBehaviorEvent(name = "placeChanged", event = SelectEvent.class,
                        description = "Fires when place is selected or changed.", defaultEvent = true)
})
public abstract class InputPlaceBase extends AbstractPrimeHtmlInputText implements Widget {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.InputPlace";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.InputPlaceRenderer";

    public InputPlaceBase() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Property(description = "Place API type: google or similar.", defaultValue = "google")
    public abstract String getApiType();

    @Property(description = "API key for the place service.")
    public abstract String getApiKey();

    @Property(description = "Comma-separated country codes to restrict results.")
    public abstract String getRestrictCountries();

    @Property(description = "Place types to restrict (e.g. address, establishment).")
    public abstract String getRestrictTypes();

    @Property(description = "Client-side callback when place is changed.")
    public abstract String getOnplacechanged();

    @Property(description = "JavaScript extender function name.")
    public abstract String getExtender();
}
