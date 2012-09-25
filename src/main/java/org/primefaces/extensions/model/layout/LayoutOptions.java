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

import org.primefaces.extensions.renderkit.layout.GsonLayoutOptions;

/**
 * Model class representing layout options.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since   0.6.0
 */
public class LayoutOptions implements Serializable {

	private String id;

	// direct options
	private Map<String, Object> options = new HashMap<String, Object>();

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

	public Map<String, Object> getOptions() {
		return options;
	}

	public void setOptions(Map<String, Object> options) {
		this.options = options;
	}

	public void addOption(String key, Object value) {
		options.put(key, value);
	}

	public void setPanesOptions(LayoutOptions layoutOptions) {
		panes = layoutOptions;
	}

	public LayoutOptions getPanesOptions() {
		return panes;
	}

	public void setNorthOptions(LayoutOptions layoutOptions) {
		north = layoutOptions;
	}

	public LayoutOptions getNorthOptions() {
		return north;
	}

	public void setSouthOptions(LayoutOptions layoutOptions) {
		south = layoutOptions;
	}

	public LayoutOptions getSouthOptions() {
		return south;
	}

	public void setWestOptions(LayoutOptions layoutOptions) {
		west = layoutOptions;
	}

	public LayoutOptions getWestOptions() {
		return west;
	}

	public void setEastOptions(LayoutOptions layoutOptions) {
		east = layoutOptions;
	}

	public LayoutOptions getEastOptions() {
		return east;
	}

	public void setCenterOptions(LayoutOptions layoutOptions) {
		center = layoutOptions;
	}

	public LayoutOptions getCenterOptions() {
		return center;
	}

	public void setChildOptions(LayoutOptions layoutOptions) {
		child = layoutOptions;
	}

	public LayoutOptions getChildOptions() {
		return child;
	}

	public LayoutOptions getLayoutOptions(String id) {
		LayoutOptions loOptions = null;

		if (child != null) {
			if (child.getId().equals(id)) {
				return child;
			} else {
				loOptions = child.getLayoutOptions(id);
			}
		}

		if (loOptions == null && panes != null) {
			if (panes.getId().equals(id)) {
				return panes;
			} else {
				loOptions = panes.getLayoutOptions(id);
			}
		}

		if (loOptions == null && center != null) {
			if (center.getId().equals(id)) {
				return center;
			} else {
				loOptions = center.getLayoutOptions(id);
			}
		}

		if (loOptions == null && north != null) {
			if (north.getId().equals(id)) {
				return north;
			} else {
				loOptions = north.getLayoutOptions(id);
			}
		}

		if (loOptions == null && south != null) {
			if (south.getId().equals(id)) {
				return south;
			} else {
				loOptions = south.getLayoutOptions(id);
			}
		}

		if (loOptions == null && east != null) {
			if (east.getId().equals(id)) {
				return east;
			} else {
				loOptions = east.getLayoutOptions(id);
			}
		}

		if (loOptions == null && west != null) {
			if (west.getId().equals(id)) {
				return west;
			} else {
				loOptions = west.getLayoutOptions(id);
			}
		}

		return loOptions;
	}

	public boolean replace(String id, LayoutOptions layoutOptions) {
		boolean replaced = false;

		if (child != null) {
			if (child.getId().equals(id)) {
				child = layoutOptions;

				return true;
			} else {
				replaced = child.replace(id, layoutOptions);
			}
		}

		if (!replaced && panes != null) {
			if (panes.getId().equals(id)) {
				panes = layoutOptions;

				return true;
			} else {
				replaced = panes.replace(id, layoutOptions);
			}
		}

		if (!replaced && center != null) {
			if (center.getId().equals(id)) {
				center = layoutOptions;

				return true;
			} else {
				replaced = center.replace(id, layoutOptions);
			}
		}

		if (!replaced && north != null) {
			if (north.getId().equals(id)) {
				north = layoutOptions;

				return true;
			} else {
				replaced = north.replace(id, layoutOptions);
			}
		}

		if (!replaced && south != null) {
			if (south.getId().equals(id)) {
				south = layoutOptions;

				return true;
			} else {
				replaced = south.replace(id, layoutOptions);
			}
		}

		if (!replaced && east != null) {
			if (east.getId().equals(id)) {
				east = layoutOptions;

				return true;
			} else {
				replaced = east.replace(id, layoutOptions);
			}
		}

		if (!replaced && west != null) {
			if (west.getId().equals(id)) {
				west = layoutOptions;

				return true;
			} else {
				replaced = west.replace(id, layoutOptions);
			}
		}

		return replaced;
	}

	public String render() {
		return GsonLayoutOptions.getGson().toJson(this);
	}
}
