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
import java.util.Map;

import jakarta.faces.application.ResourceDependency;
import jakarta.faces.component.FacesComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.event.FacesEvent;

import org.primefaces.cdk.api.FacesComponentInfo;
import org.primefaces.extensions.event.MarkEvent;
import org.primefaces.extensions.model.marktext.MarkPosition;
import org.primefaces.extensions.util.Constants;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * <code>MarkText</code> component.
 *
 * @author jxmai
 * @since 16.0.0
 */
@FacesComponent(value = MarkText.COMPONENT_TYPE, namespace = MarkText.COMPONENT_FAMILY)
@FacesComponentInfo(description = "MarkText highlights search terms within specified containers using mark.js.")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = "primefaces", name = "components.js")
@ResourceDependency(library = Constants.LIBRARY, name = "mark/mark.js")
public class MarkText extends MarkTextBaseImpl {

    public static final String STYLE_CLASS = "ui-marktext";

    @Override
    public void queueEvent(final FacesEvent event) {
        if (isAjaxBehaviorEventSource(event)) {
            FacesContext context = event.getFacesContext();
            Map<String, String> params = context.getExternalContext().getRequestParameterMap();
            String clientId = getClientId(context);
            AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;

            if (isAjaxBehaviorEvent(event, ClientBehaviorEventKeys.mark)) {
                final String value = params.get(clientId + "_value");
                final String matchedTermsJson = params.get(clientId + "_matchedTerms");
                final String positionsJson = params.get(clientId + "_positions");

                List<String> matchedTerms = new Gson().fromJson(matchedTermsJson, new TypeToken<List<String>>() {
                }.getType());
                List<MarkPosition> positions = new Gson().fromJson(positionsJson, new TypeToken<List<MarkPosition>>() {
                }.getType());

                final MarkEvent markEvent = new MarkEvent(this, behaviorEvent.getBehavior(), value, matchedTerms, positions);
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
