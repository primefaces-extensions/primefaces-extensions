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
package org.primefaces.extensions.component.commandpalette;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import javax.faces.application.ResourceDependency;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;

import org.primefaces.event.SelectEvent;
import org.primefaces.extensions.util.Constants;
import org.primefaces.util.Constants.RequestParams;

@ResourceDependency(library = "primefaces", name = "components.css")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = "primefaces", name = "components.js")
@ResourceDependency(library = Constants.LIBRARY, name = "commandpalette/commandpalette.css")
@ResourceDependency(library = Constants.LIBRARY, name = "commandpalette/commandpalette.js")
public class CommandPalette extends CommandPaletteBase implements ClientBehaviorHolder {

    private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(Arrays.asList("select"));

    @Override
    public Collection<String> getEventNames() {
        return EVENT_NAMES;
    }

    @Override
    public String getDefaultEventName() {
        return "select";
    }

    @Override
    public void queueEvent(final FacesEvent event) {
        final FacesContext context = FacesContext.getCurrentInstance();

        if (isSelfRequest(context) && event instanceof AjaxBehaviorEvent) {
            final AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;
            final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
            final String eventName = params.get(RequestParams.PARTIAL_BEHAVIOR_EVENT_PARAM);

            if ("select".equals(eventName)) {
                final String clientId = getClientId(context);
                final String groupName = params.get(clientId + "_group");
                final String itemValue = params.get(clientId + "_value");
                final String itemLabel = params.get(clientId + "_label");
                final CommandPaletteSelection selection = new CommandPaletteSelection(groupName, itemValue, itemLabel);
                final SelectEvent<CommandPaletteSelection> selectEvent = new SelectEvent<>(this, behaviorEvent.getBehavior(), selection);
                selectEvent.setPhaseId(behaviorEvent.getPhaseId());
                super.queueEvent(selectEvent);
                return;
            }
        }

        super.queueEvent(event);
    }

    @Override
    public void processDecodes(final FacesContext context) {
        if (isSelfRequest(context)) {
            decode(context);
        }
        else {
            super.processDecodes(context);
        }
    }

    private boolean isSelfRequest(final FacesContext context) {
        return getClientId(context)
                    .equals(context.getExternalContext().getRequestParameterMap().get(
                                RequestParams.PARTIAL_SOURCE_PARAM));
    }

}
