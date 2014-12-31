/*
 * Copyright 2011-2012 PrimeFaces Extensions.
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

package org.primefaces.extensions.component.blockui;

import java.util.ArrayList;
import java.util.List;

import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;

import org.primefaces.component.api.Widget;
import org.primefaces.util.ComponentUtils;

/**
 * <code>BlockUI</code> component.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since   0.2
 */
@ResourceDependencies({
                          @ResourceDependency(library = "primefaces", name = "primefaces.css"),
                          @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
                          @ResourceDependency(library = "primefaces", name = "primefaces.js"),
                          @ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js"),
                          @ResourceDependency(library = "primefaces-extensions", name = "blockui/blockui.css"),
                          @ResourceDependency(library = "primefaces-extensions", name = "blockui/blockui.js")
                      })
public class BlockUI extends UIComponentBase implements Widget {

	public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.BlockUI";
	public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
	private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.BlockUIRenderer";
	private static final String OPTIMIZED_PACKAGE = "org.primefaces.extensions.component.";

	/**
	 * Properties that are tracked by state saving.
	 *
	 * @author  Oleg Varaksin / last modified by $Author$
	 * @version $Revision$
	 */
	protected enum PropertyKeys {

		widgetVar,
        css,
        cssOverlay,
		source,
		target,
		content,
		event,
		autoShow,
        timeout,
        centerX,
        centerY;

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

	public BlockUI() {
		setRendererType(DEFAULT_RENDERER);
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	public String getWidgetVar() {
		return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
	}

	public void setWidgetVar(final String widgetVar) {
		getStateHelper().put(PropertyKeys.widgetVar, widgetVar);
	}
    
    public void setCss(final String css) {
   		getStateHelper().put(PropertyKeys.css, css);
   	}
   
   	public String getCss() {
   		return (String) getStateHelper().eval(PropertyKeys.css, null);
   	}
    
    public void setCssOverlay(final String cssOverlay) {
   		getStateHelper().put(PropertyKeys.cssOverlay, cssOverlay);
   	}
   
   	public String getCssOverlay() {
   		return (String) getStateHelper().eval(PropertyKeys.cssOverlay, null);
   	}    

	public String getSource() {
		return (String) getStateHelper().eval(PropertyKeys.source, null);
	}

	public void setSource(final String source) {
		getStateHelper().put(PropertyKeys.source, source);
	}

	public String getTarget() {
		return (String) getStateHelper().eval(PropertyKeys.target, null);
	}

	public void setTarget(final String target) {
		getStateHelper().put(PropertyKeys.target, target);
	}

	public String getContent() {
		return (String) getStateHelper().eval(PropertyKeys.content, null);
	}

	public void setContent(final String content) {
		getStateHelper().put(PropertyKeys.content, content);
	}

	public String getEvent() {
		return (String) getStateHelper().eval(PropertyKeys.event, null);
	}

	public void setEvent(final String event) {
		getStateHelper().put(PropertyKeys.event, event);
	}

	public boolean isAutoShow() {
		return (Boolean) getStateHelper().eval(PropertyKeys.autoShow, false);
	}

	public void setAutoShow(final boolean autoShow) {
		getStateHelper().put(PropertyKeys.autoShow, autoShow);
	}
    
    public int getTimeout() {
   		return (Integer) getStateHelper().eval(PropertyKeys.timeout, 0);
   	}
   
   	public void setTimeout(final int timeout) {
   		getStateHelper().put(PropertyKeys.timeout, timeout);
   	}
    
    public boolean isCenterX() {
   		return (Boolean) getStateHelper().eval(PropertyKeys.centerX, true);
   	}
   
   	public void setCenterX(final boolean centerX) {
   		getStateHelper().put(PropertyKeys.centerX, centerX);
   	}
    
    public boolean isCenterY() {
   		return (Boolean) getStateHelper().eval(PropertyKeys.centerY, true);
   	}
   
   	public void setCenterY(final boolean centerY) {
   		getStateHelper().put(PropertyKeys.centerY, centerY);
   	}    

	public String resolveWidgetVar() {
        return ComponentUtils.resolveWidgetVar(getFacesContext(), this);
	}
}
