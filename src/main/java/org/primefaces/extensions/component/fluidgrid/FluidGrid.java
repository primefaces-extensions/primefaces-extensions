/*
 * Copyright 2011-2013 PrimeFaces Extensions.
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

package org.primefaces.extensions.component.fluidgrid;

import org.primefaces.component.api.Widget;
import org.primefaces.extensions.component.base.AbstractDynamicData;
import org.primefaces.extensions.model.common.KeyData;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.ContextCallback;
import javax.faces.component.UINamingContainer;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import java.util.Map;

/**
 * <code>FluidGrid</code> component.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since   1.1.0
 */
@ResourceDependencies({
	@ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
    @ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js"),
	@ResourceDependency(library = "primefaces", name = "primefaces.js"),
	@ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.css"),
	@ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js") ,
    @ResourceDependency(library = "primefaces-extensions", name = "fluidgrid/fluidgrid.css"),        
    @ResourceDependency(library = "primefaces-extensions", name = "fluidgrid/fluidgrid.js")
})
public class FluidGrid extends AbstractDynamicData implements Widget
{
    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.FluidGrid";
   	public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
   	public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.FluidGridRenderer";

    private Map<String, UIFluidGridItem> items;
   
   	/**
   	 * Properties that are tracked by state saving.
   	 *
   	 * @author  Oleg Varaksin / last modified by $Author$
   	 * @version $Revision$
   	 */
   	protected enum PropertyKeys {
   
   		widgetVar,
   		style,
   		styleClass,
        hGutter,
        vGutter,
        fitWidth,
        originLeft,
        originTop,
        resizeBound,
        stamp,
        transitionDuration;
   
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
    
    public FluidGrid() {
   		setRendererType(DEFAULT_RENDERER);
   	}
   
   	@Override
   	public String getFamily() {
   		return COMPONENT_FAMILY;
   	}
   
   	public String getWidgetVar() {
   		return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
   	}
   
   	public void setWidgetVar(String widgetVar) {
   		getStateHelper().put(PropertyKeys.widgetVar, widgetVar);
   	}
    
    public String getStyle() {
   		return (String) getStateHelper().eval(PropertyKeys.style, null);
   	}
    
    public void setStyle(String style) {
   		getStateHelper().put(PropertyKeys.style, style);
   	}
   
   	public String getStyleClass() {
   		return (String) getStateHelper().eval(PropertyKeys.styleClass, null);
   	}
    
    public void setStyleClass(String styleClass) {
   		getStateHelper().put(PropertyKeys.styleClass, styleClass);
   	}
    
    public int getHGutter() {
   		return (Integer) getStateHelper().eval(PropertyKeys.hGutter, 0);
   	}
   
   	public void setHGutter(int hGutter) {
   		getStateHelper().put(PropertyKeys.hGutter, hGutter);
   	}
    
    public int getVGutter() {
   		return (Integer) getStateHelper().eval(PropertyKeys.vGutter, 0);
   	}
   
   	public void setVGutter(int vGutter) {
   		getStateHelper().put(PropertyKeys.vGutter, vGutter);
   	}
    
    public boolean isFitWidth() {
   		return (Boolean) getStateHelper().eval(PropertyKeys.fitWidth, false);
   	}
   
   	public void setFitWidth(boolean fitWidth) {
   		getStateHelper().put(PropertyKeys.fitWidth, fitWidth);
   	}
    
    public boolean isOriginLeft() {
   		return (Boolean) getStateHelper().eval(PropertyKeys.originLeft, true);
   	}
   
   	public void setOriginLeft(boolean originLeft) {
   		getStateHelper().put(PropertyKeys.originLeft, originLeft);
   	}
    
    public boolean isOriginTop() {
   		return (Boolean) getStateHelper().eval(PropertyKeys.originTop, true);
   	}
   
   	public void setOriginTop(boolean originTop) {
   		getStateHelper().put(PropertyKeys.originTop, originTop);
   	}
    
    public boolean isResizeBound() {
   		return (Boolean) getStateHelper().eval(PropertyKeys.resizeBound, true);
   	}
   
   	public void setResizeBound(boolean resizeBound) {
   		getStateHelper().put(PropertyKeys.resizeBound, resizeBound);
   	}
    
   	public String getStamp() {
   		return (String) getStateHelper().eval(PropertyKeys.stamp, null);
   	}
    
    public void setStamp(String stamp) {
   		getStateHelper().put(PropertyKeys.stamp, stamp);
   	}
    
    public String getTransitionDuration() {
   		return (String) getStateHelper().eval(PropertyKeys.transitionDuration, null);
   	}
    
    public void setTransitionDuration(String transitionDuration) {
   		getStateHelper().put(PropertyKeys.transitionDuration, transitionDuration);
   	}    
    
    protected KeyData findData(String key) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    protected void processChildren(FacesContext context, PhaseId phaseId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    protected boolean visitChildren(VisitContext context, VisitCallback callback) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    protected boolean invokeOnChildren(FacesContext context, String clientId, ContextCallback callback) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String resolveWidgetVar() {
   		final FacesContext context = FacesContext.getCurrentInstance();
   		final String userWidgetVar = (String) getAttributes().get(PropertyKeys.widgetVar.toString());
   
   		if (userWidgetVar != null) {
   			return userWidgetVar;
   		}
   
   		return "widget_" + getClientId(context).replaceAll("-|" + UINamingContainer.getSeparatorChar(context), "_");
   	}
}
