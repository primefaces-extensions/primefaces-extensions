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
 *
 * $Id$
 */

package org.primefaces.extensions.showcase.controller.layout;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.extensions.model.layout.LayoutOptions;

/**
 * CustomContentLayoutController
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 */
@Named
@ViewScoped
public class CustomContentBackupController implements Serializable {

	private static final long serialVersionUID = 20120925L;

	private static final String FIRST_NESTED_LAYOUT = "/sections/layout/example-customContentOne.xhtml";
	private static final String SECOND_NESTED_LAYOUT = "/sections/layout/example-customContentTwo.xhtml";

	private String src;
	private LayoutOptions layoutOptions;
	private LayoutOptions optionsNestedOne;
	private LayoutOptions optionsNestedTwo;
	private String jsonNestedOptions = "{}";

	@PostConstruct
	protected void initialize() {
		// show first nested layout on initial page load
		src = FIRST_NESTED_LAYOUT;

		layoutOptions = new LayoutOptions();

		// options for all panes
		final LayoutOptions panes = new LayoutOptions();
		panes.addOption("slidable", false);
		layoutOptions.setPanesOptions(panes);

		// north pane
		final LayoutOptions north = new LayoutOptions();
		north.addOption("resizable", false);
		north.addOption("closable", false);
		north.addOption("size", 60);
		north.addOption("spacing_open", 0);
		layoutOptions.setNorthOptions(north);

		// south pane
		final LayoutOptions south = new LayoutOptions();
		south.addOption("resizable", false);
		south.addOption("closable", false);
		south.addOption("size", 28);
		south.addOption("spacing_open", 0);
		layoutOptions.setSouthOptions(south);

		// west pane
		final LayoutOptions west = new LayoutOptions();
		west.addOption("size", 210);
		west.addOption("minSize", 180);
		west.addOption("maxSize", 500);
		layoutOptions.setWestOptions(west);

		// center pane, instantiated with Id in order to be able to update child options
		final LayoutOptions center = new LayoutOptions("centerOptions");
		center.addOption("resizable", false);
		center.addOption("closable", false);
		center.addOption("minWidth", 200);
		center.addOption("minHeight", 60);
		layoutOptions.setCenterOptions(center);

		// set options for nested center layout
		center.setChildOptions(getLayoutOptionsNestedOne());
	}

	public LayoutOptions getLayoutOptions() {
		return layoutOptions;
	}

	public LayoutOptions getLayoutOptionsNestedOne() {
		if (optionsNestedOne != null) {
			return optionsNestedOne;
		}

		// instantiate options with Id to be able to replace them on AJAX update
		optionsNestedOne = new LayoutOptions("childOptionsOne");

		// options for center-north pane
		final LayoutOptions centerNorth = new LayoutOptions();
		centerNorth.addOption("size", "50%");
		optionsNestedOne.setNorthOptions(centerNorth);

		// options for center-center pane
		final LayoutOptions centerCenter = new LayoutOptions();
		centerCenter.addOption("minHeight", 60);
		optionsNestedOne.setCenterOptions(centerCenter);

		return optionsNestedOne;
	}

	public LayoutOptions getLayoutOptionsNestedTwo() {
		if (optionsNestedTwo != null) {
			return optionsNestedTwo;
		}

		// instantiate options with Id to be able to replace them on AJAX update
		optionsNestedTwo = new LayoutOptions("childOptionsTwo");

		// options for center-east pane
		final LayoutOptions centerEast = new LayoutOptions();
		centerEast.addOption("size", "50%");
		optionsNestedTwo.setEastOptions(centerEast);

		// options for center-center pane
		final LayoutOptions centerCenter = new LayoutOptions();
		centerCenter.addOption("minWidth", 60);
		optionsNestedTwo.setCenterOptions(centerCenter);

		return optionsNestedTwo;
	}

	public String getSrc() {
		return src;
	}

	public void toggleSrc(final ActionEvent evt) {
		if (FIRST_NESTED_LAYOUT.equals(src)) {
			// change path to the nested layout
			src = SECOND_NESTED_LAYOUT;

			// replace child options in layout options
			final LayoutOptions center = layoutOptions.getLayoutOptions("centerOptions");
			center.replace("childOptionsOne", getLayoutOptionsNestedTwo());

			// update options for oncomplete
			jsonNestedOptions = getLayoutOptionsNestedTwo().toJson();
		} else if (SECOND_NESTED_LAYOUT.equals(src)) {
			// change path to the nested layout
			src = FIRST_NESTED_LAYOUT;

			// replace child options in layout options
			final LayoutOptions center = layoutOptions.getLayoutOptions("centerOptions");
			center.replace("childOptionsTwo", getLayoutOptionsNestedOne());

			// update options for oncomplete
			jsonNestedOptions = getLayoutOptionsNestedOne().toJson();
		}
	}

	public String getJsonNestedOptions() {
		return jsonNestedOptions;
	}
}
