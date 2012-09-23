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

package org.primefaces.extensions.model.layout;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Model class representing layout options.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 */
public class LayoutOptions implements Serializable {

	private String id;

	// direct options
	private Map<String, String> options = new HashMap<String, String>();

	// options for all panes
	private LayoutOptions panes;

	// options for every specific pane (depends on position)
	private LayoutOptions north;
	private LayoutOptions south;
	private LayoutOptions west;
	private LayoutOptions east;
	private LayoutOptions center;

	// options for child layout
	private LayoutOptions child;

	public LayoutOptions() {
	}

	public LayoutOptions(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map<String, String> getOptions() {
		return options;
	}

	public void setOptions(Map<String, String> options) {
		this.options = options;
	}

	public void addOption(String key, String value) {
		options.put(key, value);
	}

	public void setPanesOptions(LayoutOptions layoutOptions) {
		panes = layoutOptions;
	}

	public LayoutOptions getPanesOption() {
		return panes;
	}

	public void setNorthOptions(LayoutOptions layoutOptions) {
		north = layoutOptions;
	}

	public LayoutOptions getNorthOption() {
		return north;
	}

	public void setSouthOptions(LayoutOptions layoutOptions) {
		south = layoutOptions;
	}

	public LayoutOptions getSouthOption() {
		return south;
	}

	public void setWestOptions(LayoutOptions layoutOptions) {
		west = layoutOptions;
	}

	public LayoutOptions getWestOption() {
		return west;
	}

	public void setEastOptions(LayoutOptions layoutOptions) {
		east = layoutOptions;
	}

	public LayoutOptions getEastOption() {
		return east;
	}

	public void setCenterOptions(LayoutOptions layoutOptions) {
		center = layoutOptions;
	}

	public LayoutOptions getCenterOption() {
		return center;
	}

	public void setChildOptions(LayoutOptions layoutOptions) {
		child = layoutOptions;
	}

	public LayoutOptions getChildOption() {
		return child;
	}

	public LayoutOptions getLayoutOptions(String id) {
		// TODO
		return null;
	}

	public void replace(String id, LayoutOptions layoutOptions) {
		// TODO
	}

	public String render() {
		// TODO

		return null;
	}
}
