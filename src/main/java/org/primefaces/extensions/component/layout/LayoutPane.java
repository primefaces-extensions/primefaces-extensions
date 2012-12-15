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

package org.primefaces.extensions.component.layout;

import java.util.ArrayList;
import java.util.List;

import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ListenerFor;
import javax.faces.event.PreRenderComponentEvent;

import org.primefaces.extensions.model.layout.LayoutOptions;

/**
 * <code>LayoutPane</code> component.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since   0.2
 */
@ListenerFor(systemEventClass = PreRenderComponentEvent.class)
public class LayoutPane extends UIComponentBase {

	public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.LayoutPane";
	public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
	private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.LayoutPaneRenderer";
	private static final String OPTIMIZED_PACKAGE = "org.primefaces.extensions.component.";

	private LayoutOptions options;

	/**
	 * Properties that are tracked by state saving.
	 *
	 * @author  Oleg Varaksin / last modified by $Author$
	 * @version $Revision$
	 */
	protected enum PropertyKeys {

		position,
		combinedPosition,
		styleHeader,
		styleClassHeader,
		styleContent,
		styleClassContent,
		resizable,
		closable,
		size,
		minSize,
		maxSize,
		minWidth,
		maxWidth,
		minHeight,
		maxHeight,
		spacing,
		initClosed,
		initHidden,
		resizeWhileDragging;

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

	public LayoutPane() {
		setRendererType(DEFAULT_RENDERER);
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	public String getStyleHeader() {
		return (String) getStateHelper().eval(PropertyKeys.styleHeader, null);
	}

	public void setStyleHeader(String styleHeader) {
		setAttribute(PropertyKeys.styleHeader, styleHeader);
	}

	public String getStyleClassHeader() {
		return (String) getStateHelper().eval(PropertyKeys.styleClassHeader, null);
	}

	public void setStyleClassHeader(String styleClassHeader) {
		setAttribute(PropertyKeys.styleClassHeader, styleClassHeader);
	}

	public String getStyleContent() {
		return (String) getStateHelper().eval(PropertyKeys.styleContent, null);
	}

	public void setStyleContent(String styleContent) {
		setAttribute(PropertyKeys.styleContent, styleContent);
	}

	public String getStyleClassContent() {
		return (String) getStateHelper().eval(PropertyKeys.styleClassContent, null);
	}

	public void setStyleClassContent(String styleClassContent) {
		setAttribute(PropertyKeys.styleClassContent, styleClassContent);
	}

	// position "north" | "south" | "west" | "east" | "center"
	public String getPosition() {
		return (String) getStateHelper().eval(PropertyKeys.position, Layout.PANE_POSITION_CENTER);
	}

	public void setPosition(String position) {
		setAttribute(PropertyKeys.position, position);
	}

	public String getCombinedPosition() {
		return (String) getStateHelper().eval(PropertyKeys.combinedPosition, Layout.PANE_POSITION_CENTER);
	}

	public void setCombinedPosition(String combinedPosition) {
		setAttribute(PropertyKeys.combinedPosition, combinedPosition);
	}

	public boolean isResizable() {
		return (Boolean) getStateHelper().eval(PropertyKeys.resizable, true);
	}

	public void setResizable(boolean resizable) {
		setAttribute(PropertyKeys.resizable, resizable);
	}

	public boolean isClosable() {
		return (Boolean) getStateHelper().eval(PropertyKeys.closable, true);
	}

	public void setClosable(boolean closable) {
		setAttribute(PropertyKeys.closable, closable);
	}

	public String getSize() {
		return (String) getStateHelper().eval(PropertyKeys.size, null);
	}

	public void setSize(String size) {
		setAttribute(PropertyKeys.size, size);
	}

	public String getMinSize() {
		return (String) getStateHelper().eval(PropertyKeys.minSize, null);
	}

	public void setMinSize(String minSize) {
		setAttribute(PropertyKeys.minSize, minSize);
	}

	public String getMaxSize() {
		return (String) getStateHelper().eval(PropertyKeys.maxSize, null);
	}

	public void setMaxSize(String maxSize) {
		setAttribute(PropertyKeys.maxSize, maxSize);
	}

	public String getMinWidth() {
		return (String) getStateHelper().eval(PropertyKeys.minWidth, null);
	}

	public void setMinWidth(String minWidth) {
		setAttribute(PropertyKeys.minWidth, minWidth);
	}

	public String getMaxWidth() {
		return (String) getStateHelper().eval(PropertyKeys.maxWidth, null);
	}

	public void setMaxWidth(String maxWidth) {
		setAttribute(PropertyKeys.maxWidth, maxWidth);
	}

	public String getMinHeight() {
		return (String) getStateHelper().eval(PropertyKeys.minHeight, null);
	}

	public void setMinHeight(String minHeight) {
		setAttribute(PropertyKeys.minHeight, minHeight);
	}

	public String getMaxHeight() {
		return (String) getStateHelper().eval(PropertyKeys.maxHeight, null);
	}

	public void setMaxHeight(String maxHeight) {
		setAttribute(PropertyKeys.maxHeight, maxHeight);
	}

	public int getSpacing() {
		return (Integer) getStateHelper().eval(PropertyKeys.spacing, 6);
	}

	public void setSpacing(int spacing) {
		setAttribute(PropertyKeys.spacing, spacing);
	}

	public boolean isInitClosed() {
		return (Boolean) getStateHelper().eval(PropertyKeys.initClosed, false);
	}

	public void setInitClosed(boolean initClosed) {
		setAttribute(PropertyKeys.initClosed, initClosed);
	}

	public boolean isInitHidden() {
		return (Boolean) getStateHelper().eval(PropertyKeys.initHidden, false);
	}

	public void setInitHidden(boolean initHidden) {
		setAttribute(PropertyKeys.initHidden, initHidden);
	}

	public boolean isResizeWhileDragging() {
		return (Boolean) getStateHelper().eval(PropertyKeys.resizeWhileDragging, false);
	}

	public void setResizeWhileDragging(boolean resizeWhileDragging) {
		setAttribute(PropertyKeys.resizeWhileDragging, resizeWhileDragging);
	}

	@Override
	public void processEvent(ComponentSystemEvent event) throws AbortProcessingException {
		super.processEvent(event);

		if (!(event instanceof PreRenderComponentEvent) || !getLayout().isBuildOptions()) {
			return;
		}

		setOptions();
	}

	public LayoutOptions getOptions() {
		if (options != null) {
			return options;
		}

		options = new LayoutOptions();
		options.addOption(PropertyKeys.resizable.toString(), isResizable());
		options.addOption(PropertyKeys.closable.toString(), isClosable());
		options.addOption(PropertyKeys.spacing.toString(), getSpacing());
		options.addOption(PropertyKeys.initClosed.toString(), isInitClosed());
		options.addOption(PropertyKeys.initHidden.toString(), isInitHidden());
		options.addOption(PropertyKeys.resizeWhileDragging.toString(), isResizeWhileDragging());

		String size = getSize();
		if (size != null) {
			options.addOption(PropertyKeys.size.toString(), size);
		}

		String minSize = getMinSize();
		if (minSize != null) {
			options.addOption(PropertyKeys.minSize.toString(), minSize);
		}

		String maxSize = getMaxSize();
		if (maxSize != null) {
			options.addOption(PropertyKeys.maxSize.toString(), maxSize);
		}

		String minWidth = getMinWidth();
		if (minWidth != null) {
			options.addOption(PropertyKeys.minWidth.toString(), minWidth);
		}

		String maxWidth = getMaxWidth();
		if (maxWidth != null) {
			options.addOption(PropertyKeys.maxWidth.toString(), maxWidth);
		}

		String minHeight = getMinHeight();
		if (minHeight != null) {
			options.addOption(PropertyKeys.minHeight.toString(), minHeight);
		}

		String maxHeight = getMaxHeight();
		if (maxHeight != null) {
			options.addOption(PropertyKeys.maxHeight.toString(), maxHeight);
		}

		return options;
	}

	private void setOptions() {
		// create layout options for this pane via attributes defined in pe:layoutPane
		String position = getPosition();
		UIComponent parent = getParent();
		LayoutOptions thisLayoutOptions = getOptions();
		LayoutOptions options;

		if (parent instanceof LayoutPane) {
			LayoutOptions parentLayoutOptions = ((LayoutPane) parent).getOptions();
			options = parentLayoutOptions.getChildOptions();
			if (options == null) {
				options = new LayoutOptions();
				parentLayoutOptions.setChildOptions(options);
			}
		} else if (parent instanceof Layout) {
			options = (LayoutOptions) ((Layout) parent).getOptions();
			if (options == null) {
				Layout layout = ((Layout) parent);
				options = new LayoutOptions();
				layout.setOptions(options);

				// options for all panes
				LayoutOptions panes = null;
				String resizerTip = layout.getResizerTip();
				if (resizerTip != null) {
					panes = new LayoutOptions();
					panes.addOption(Layout.PropertyKeys.resizerTip.toString(), resizerTip);
				}

				String togglerTipOpen = layout.getTogglerTipOpen();
				if (togglerTipOpen != null) {
					if (panes == null) {
						panes = new LayoutOptions();
					}

					panes.addOption(Layout.PropertyKeys.togglerTip_open.toString(), togglerTipOpen);
				}

				String togglerTipClosed = layout.getTogglerTipClosed();
				if (togglerTipClosed != null) {
					if (panes == null) {
						panes = new LayoutOptions();
					}

					panes.addOption(Layout.PropertyKeys.togglerTip_closed.toString(), togglerTipClosed);
				}

				if (panes != null) {
					options.setPanesOptions(panes);
				}
			}
		} else if (parent instanceof UIForm) {
			// layout pane can be within a h:form
			setOptions();

			return;
		} else {
			throw new FacesException("LayoutPane can be only placed within another LayoutPane or Layout");
		}

		if (Layout.PANE_POSITION_CENTER.equals(position)) {
			options.setCenterOptions(thisLayoutOptions);
		} else if (Layout.PANE_POSITION_NORTH.equals(position)) {
			options.setNorthOptions(thisLayoutOptions);
		} else if (Layout.PANE_POSITION_SOUTH.equals(position)) {
			options.setSouthOptions(thisLayoutOptions);
		} else if (Layout.PANE_POSITION_WEST.equals(position)) {
			options.setWestOptions(thisLayoutOptions);
		} else if (Layout.PANE_POSITION_EAST.equals(position)) {
			options.setEastOptions(thisLayoutOptions);
		} else {
			throw new FacesException("Pane position " + position
			                         + " is invalid. Valid positions are 'center', 'north' 'south', 'west', 'east'");
		}
	}

	private Layout getLayout() {
		UIComponent parent = getParent();

		while (!(parent instanceof Layout)) {
			parent = parent.getParent();
		}

		return (Layout) parent;
	}

	public void setAttribute(PropertyKeys property, Object value) {
		getStateHelper().put(property, value);

		@SuppressWarnings("unchecked")
		List<String> setAttributes =
		    (List<String>) this.getAttributes().get("javax.faces.component.UIComponentBase.attributesThatAreSet");
		if (setAttributes == null) {
			final String cname = this.getClass().getName();
			if (cname != null && cname.startsWith(OPTIMIZED_PACKAGE)) {
				setAttributes = new ArrayList<String>(6);
				this.getAttributes().put("javax.faces.component.UIComponentBase.attributesThatAreSet", setAttributes);
			}
		}

		if (setAttributes != null && value == null) {
			final String attributeName = property.toString();
			final ValueExpression ve = getValueExpression(attributeName);
			if (ve == null) {
				setAttributes.remove(attributeName);
			} else if (!setAttributes.contains(attributeName)) {
				setAttributes.add(attributeName);
			}
		}
	}
}
