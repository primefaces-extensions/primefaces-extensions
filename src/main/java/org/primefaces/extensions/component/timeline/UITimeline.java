/*
 * Copyright 2011 PrimeFaces Extensions.
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
 * 
 * $Id$
 */
package org.primefaces.extensions.component.timeline;

import java.util.logging.Logger;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;

import org.primefaces.component.api.Widget;

/**
 * @author Nilesh Mali / last modified by $Author$
 * @version $Revision$
 * @since 0.3
 */
@ResourceDependencies({
    @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
    @ResourceDependency(library = "primefaces", name = "primefaces.js"),
    @ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js"),
    @ResourceDependency(library = "primefaces-extensions", name = "timeline/timeline.css"),
    @ResourceDependency(library = "primefaces-extensions", name = "timeline/timeline.js")
})
public class UITimeline extends UIOutput implements Widget {

    private static final Logger LOG = Logger.getLogger(UITimeline.class.getName());
    private static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.TimelineRenderer";
    private static final String OPTIMIZED_PACKAGE = "org.primefaces.extensions.component.";

    /**
     * Properties that are tracked by state saving.
     */
    protected enum PropertyKeys {

        widgetVar;
        private String toString;

        PropertyKeys(final String toString) {
            this.toString = toString;
        }

        PropertyKeys() {
        }

        @Override
        public String toString() {
            return ((this.toString != null) ? this.toString : super.toString());
        }
    }

    public UITimeline() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public String resolveWidgetVar() {
        final FacesContext context = FacesContext.getCurrentInstance();
        final String userWidgetVar = (String) getAttributes().get(PropertyKeys.widgetVar.toString());

        if (userWidgetVar != null) {
            return userWidgetVar;
        }

        return "widget_" + getClientId(context).replaceAll("-|" + UINamingContainer.getSeparatorChar(context), "_");
    }
}
