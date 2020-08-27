/*
 * Copyright 2011-2015 PrimeFaces Extensions
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
 */

package org.primefaces.extensions.showcase.controller.fluidgrid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.extensions.model.fluidgrid.FluidGridItem;
import org.primefaces.extensions.showcase.model.fluidgrid.DynamicField;

/**
 * FluidGridDynaFormController
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 */
@Named
@ViewScoped
public class FluidGridDynaFormController implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<FluidGridItem> items;

	@PostConstruct
	protected void initialize() {
		items = new ArrayList<FluidGridItem>();

		final List<SelectItem> selectItems = new ArrayList<SelectItem>();
		selectItems.add(new SelectItem("1", "Label 1"));
		selectItems.add(new SelectItem("2", "Label 2"));
		selectItems.add(new SelectItem("3", "Label 3"));

		items.add(new FluidGridItem(new DynamicField("First Label", null, true, null), "input"));
		items.add(new FluidGridItem(new DynamicField("Second Label", "Some default value", false, null), "input"));
		items.add(new FluidGridItem(new DynamicField("Third Label", null, false, selectItems), "select"));
		items.add(new FluidGridItem(new DynamicField("Fourth Label", "2", false, selectItems), "select"));
		items.add(new FluidGridItem(new DynamicField("Fifth Label", null, true, null), "calendar"));
		items.add(new FluidGridItem(new DynamicField("Sixth Label", new Date(), false, null), "calendar"));
		items.add(new FluidGridItem(new DynamicField("Seventh Label", null, false, null), "input"));
		items.add(new FluidGridItem(new DynamicField("Eighth Label", null, false, selectItems), "select"));
		items.add(new FluidGridItem(new DynamicField("Ninth Label", null, false, null), "calendar"));
	}

	public List<FluidGridItem> getItems() {
		return items;
	}
}
