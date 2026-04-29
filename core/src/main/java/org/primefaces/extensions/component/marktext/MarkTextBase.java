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
package org.primefaces.extensions.component.marktext;

import java.util.List;

import jakarta.faces.component.UIComponentBase;

import org.primefaces.cdk.api.FacesBehaviorEvent;
import org.primefaces.cdk.api.FacesBehaviorEvents;
import org.primefaces.cdk.api.FacesComponentBase;
import org.primefaces.cdk.api.Property;
import org.primefaces.component.api.StyleAware;
import org.primefaces.component.api.Widget;
import org.primefaces.extensions.event.MarkEvent;

/**
 * <code>MarkText</code> component base class.
 *
 * @author jxmai
 * @since 16.0.0
 */
@FacesComponentBase
@FacesBehaviorEvents({
            @FacesBehaviorEvent(name = "mark", event = MarkEvent.class, description = "Fires when text is marked/highlighted.", defaultEvent = true)
})
public abstract class MarkTextBase extends UIComponentBase implements Widget, StyleAware {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.MarkText";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.MarkTextRenderer";

    public MarkTextBase() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Property(description = "Name of the client-side widget.")
    public abstract String getWidgetVar();

    @Property(description = "Search terms to highlight. Can be a string or array of strings.", required = true)
    public abstract String getValue();

    @Property(description = "Search expression to resolve the target component to search within.", required = true)
    public abstract String getFor();

    @Property(description = "Whether the search should be case sensitive.", defaultValue = "false")
    public abstract Boolean getCaseSensitive();

    @Property(description = "Whether to match each word separately.", defaultValue = "true")
    public abstract Boolean getSeparateWordSearch();

    @Property(description = "Search accuracy level: 'partially', 'complementarily', or 'exactly'.", defaultValue = "partially")
    public abstract String getAccuracy();

    @Property(description = "Map of synonyms for term matching.")
    public abstract Object getSynonyms();

    @Property(description = "Array of exclusion selectors. Matches inside these elements will be ignored.")
    public abstract List<String> getExclude();

    @Property(description = "Whether matching should include same-origin iframe documents.", defaultValue = "false")
    public abstract Boolean getIframes();

    @Property(description = "Maximum wait time in milliseconds for iframe loading.", defaultValue = "5000")
    public abstract Integer getIframesTimeout();

    @Property(description = "Search across element boundaries.", defaultValue = "false")
    public abstract Boolean getAcrossElements();

    @Property(description = "Wildcard matching mode: 'disabled'|'enabled'|'withSpaces'.", defaultValue = "disabled")
    public abstract String getWildcards();

    @Property(description = "Whether accented and unaccented letters are treated as the same during matching.", defaultValue = "true")
    public abstract Boolean getDiacritics();

    @Property(description = "Client-side callback executed after marking is complete. The callback receives totalMatches as a parameter.")
    public abstract String getOnDone();
}
