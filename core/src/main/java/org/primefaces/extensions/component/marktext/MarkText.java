/*
 * Copyright (c) 2011-2025 PrimeFaces Extensions
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

import java.util.Collection;
import java.util.Map;

import jakarta.faces.application.ResourceDependency;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.event.BehaviorEvent;
import jakarta.faces.event.FacesEvent;

import org.primefaces.extensions.event.MarkEvent;
import org.primefaces.extensions.util.Constants;
import org.primefaces.util.MapBuilder;

/**
 * <code>MarkText</code> component.
 *
 * @author jxmai
 * @since 15.0.13
 */
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = "primefaces", name = "components.js")
@ResourceDependency(library = Constants.LIBRARY, name = "mark/mark.js")
@ResourceDependency(library = Constants.LIBRARY, name = "mark/marktext.js")
public class MarkText extends MarkTextBase {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.MarkText";

    public static final String STYLE_CLASS = "ui-marktext";

    private static final String DEFAULT_EVENT = "mark";

    private static final Map<String, Class<? extends BehaviorEvent>> BEHAVIOR_EVENT_MAPPING = MapBuilder.<String, Class<? extends BehaviorEvent>> builder()
                .put(DEFAULT_EVENT, null)
                .build();

    private static final Collection<String> EVENT_NAMES = BEHAVIOR_EVENT_MAPPING.keySet();

    @Override
    public Map<String, Class<? extends BehaviorEvent>> getBehaviorEventMapping() {
        return BEHAVIOR_EVENT_MAPPING;
    }

    @Override
    public Collection<String> getEventNames() {
        return EVENT_NAMES;
    }

    @Override
    public String getDefaultEventName() {
        return DEFAULT_EVENT;
    }

    @Override
    public void queueEvent(final FacesEvent event) {
        if (event instanceof AjaxBehaviorEvent) {
            final FacesContext context = getFacesContext();
            final AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;
            final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
            final String eventName = params.get(org.primefaces.util.Constants.RequestParams.PARTIAL_BEHAVIOR_EVENT_PARAM);

            if (DEFAULT_EVENT.equals(eventName)) {
                final String value = params.get(getClientId(context) + "_value");
                final MarkEvent markEvent = new MarkEvent(this, behaviorEvent.getBehavior(), value);
                markEvent.setPhaseId(event.getPhaseId());
                super.queueEvent(markEvent);
            }
            else {
                super.queueEvent(event);
            }
        }
        else {
            super.queueEvent(event);
        }
    }
}