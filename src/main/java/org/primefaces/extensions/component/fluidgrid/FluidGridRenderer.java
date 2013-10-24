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

import org.primefaces.extensions.model.fluidgrid.FluidGridItem;
import org.primefaces.renderkit.CoreRenderer;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Collection;

/**
 * Renderer for {@link FluidGrid} component.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since   0.5
 */
public class FluidGridRenderer extends CoreRenderer
{
    private static final String GRID_CLASS = "pe-fluidgrid";
   	private static final String GRID_ITEM_CLASS = "pe-fluidgrid-item";
    
    private static final String LIST_ROLE = "list";
    private static final String LIST_ITEM_ROLE = "listitem";
    
    @Override
   	public void encodeEnd(final FacesContext fc, final UIComponent component) throws IOException {
        // TODO
    }
    
    protected void encodeMarkup(FacesContext fc, FluidGrid fluidGrid, Collection<FluidGridItem> items) throws IOException {
       // TODO 
    }
    
    protected void encodeScript(FacesContext fc, FluidGrid fluidGrid, Collection<FluidGridItem> items) throws IOException {
        // TODO
    }
    
    @Override
   	public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
   		//Rendering happens on encodeEnd
   	}
   
   	@Override
   	public boolean getRendersChildren() {
   		return true;
   	}
}
