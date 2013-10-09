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
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ListenerFor;
import javax.faces.event.PreRenderComponentEvent;

import org.primefaces.component.outputpanel.OutputPanel;
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
		spacing_open,
		spacing_closed,
		initClosed,
		initHidden,
		resizeWhileDragging,
		maskContents,
		maskObjects;

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
		getStateHelper().put(PropertyKeys.styleHeader, styleHeader);
	}

	public String getStyleClassHeader() {
		return (String) getStateHelper().eval(PropertyKeys.styleClassHeader, null);
	}

	public void setStyleClassHeader(String styleClassHeader) {
		getStateHelper().put(PropertyKeys.styleClassHeader, styleClassHeader);
	}

	public String getStyleContent() {
		return (String) getStateHelper().eval(PropertyKeys.styleContent, null);
	}

	public void setStyleContent(String styleContent) {
		getStateHelper().put(PropertyKeys.styleContent, styleContent);
	}

	public String getStyleClassContent() {
		return (String) getStateHelper().eval(PropertyKeys.styleClassContent, null);
	}

	public void setStyleClassContent(String styleClassContent) {
		getStateHelper().put(PropertyKeys.styleClassContent, styleClassContent);
	}

	// position "north" | "south" | "west" | "east" | "center"
	public String getPosition() {
		return (String) getStateHelper().eval(PropertyKeys.position, Layout.PANE_POSITION_CENTER);
	}

	public void setPosition(String position) {
		getStateHelper().put(PropertyKeys.position, position);
	}

	public String getCombinedPosition() {
		return (String) getStateHelper().eval(PropertyKeys.combinedPosition, Layout.PANE_POSITION_CENTER);
	}

	public void setCombinedPosition(String combinedPosition) {
		getStateHelper().put(PropertyKeys.combinedPosition, combinedPosition);
	}

	public boolean isResizable() {
		return (Boolean) getStateHelper().eval(PropertyKeys.resizable, true);
	}

	public void setResizable(boolean resizable) {
		getStateHelper().put(PropertyKeys.resizable, resizable);
	}

	public boolean isClosable() {
		return (Boolean) getStateHelper().eval(PropertyKeys.closable, true);
	}

	public void setClosable(boolean closable) {
		getStateHelper().put(PropertyKeys.closable, closable);
	}

	public String getSize() {
		return (String) getStateHelper().eval(PropertyKeys.size, null);
	}

	public void setSize(String size) {
		getStateHelper().put(PropertyKeys.size, size);
	}

	public String getMinSize() {
		return (String) getStateHelper().eval(PropertyKeys.minSize, null);
	}

	public void setMinSize(String minSize) {
		getStateHelper().put(PropertyKeys.minSize, minSize);
	}

	public String getMaxSize() {
		return (String) getStateHelper().eval(PropertyKeys.maxSize, null);
	}

	public void setMaxSize(String maxSize) {
		getStateHelper().put(PropertyKeys.maxSize, maxSize);
	}

	public String getMinWidth() {
		return (String) getStateHelper().eval(PropertyKeys.minWidth, null);
	}

	public void setMinWidth(String minWidth) {
		getStateHelper().put(PropertyKeys.minWidth, minWidth);
	}

	public String getMaxWidth() {
		return (String) getStateHelper().eval(PropertyKeys.maxWidth, null);
	}

	public void setMaxWidth(String maxWidth) {
		getStateHelper().put(PropertyKeys.maxWidth, maxWidth);
	}

	public String getMinHeight() {
		return (String) getStateHelper().eval(PropertyKeys.minHeight, null);
	}

	public void setMinHeight(String minHeight) {
		getStateHelper().put(PropertyKeys.minHeight, minHeight);
	}

	public String getMaxHeight() {
		return (String) getStateHelper().eval(PropertyKeys.maxHeight, null);
	}

	public void setMaxHeight(String maxHeight) {
		getStateHelper().put(PropertyKeys.maxHeight, maxHeight);
	}

	public int getSpacingOpen() {
		return (Integer) getStateHelper().eval(PropertyKeys.spacing_open, 6);
	}

	public void setSpacingOpen(int spacingOpen) {
		getStateHelper().put(PropertyKeys.spacing_open, spacingOpen);
	}

	public int getSpacingClosed() {
		return (Integer) getStateHelper().eval(PropertyKeys.spacing_closed, 6);
	}

	public void setSpacingClosed(int spacingClosed) {
		getStateHelper().put(PropertyKeys.spacing_closed, spacingClosed);
	}

	public boolean isInitClosed() {
		return (Boolean) getStateHelper().eval(PropertyKeys.initClosed, false);
	}

	public void setInitClosed(boolean initClosed) {
		getStateHelper().put(PropertyKeys.initClosed, initClosed);
	}

	public boolean isInitHidden() {
		return (Boolean) getStateHelper().eval(PropertyKeys.initHidden, false);
	}

	public void setInitHidden(boolean initHidden) {
		getStateHelper().put(PropertyKeys.initHidden, initHidden);
	}

	public boolean isResizeWhileDragging() {
		return (Boolean) getStateHelper().eval(PropertyKeys.resizeWhileDragging, false);
	}

	public void setResizeWhileDragging(boolean resizeWhileDragging) {
		getStateHelper().put(PropertyKeys.resizeWhileDragging, resizeWhileDragging);
	}

	public boolean isMaskContents() {
		return (Boolean) getStateHelper().eval(PropertyKeys.maskContents, false);
	}

	public void setMaskContents(boolean maskContents) {
		getStateHelper().put(PropertyKeys.maskContents, maskContents);
	}

	public boolean isMaskObjects() {
		return (Boolean) getStateHelper().eval(PropertyKeys.maskObjects, false);
	}

	public void setMaskObjects(boolean maskObjects) {
		getStateHelper().put(PropertyKeys.maskObjects, maskObjects);
	}

	@Override
	public void processEvent(ComponentSystemEvent event) throws AbortProcessingException {
		super.processEvent(event);

		if (!(event instanceof PreRenderComponentEvent) || !getLayout().isBuildOptions()) {
			return;
		}

		setOptions(getParent());
	}

	public LayoutOptions getOptions() {
		if (options != null) {
			return options;
		}

		// create options object
		options = new LayoutOptions();

		boolean isResizable = isResizable();
		if (!isResizable) {
			options.addOption(PropertyKeys.resizable.toString(), isResizable);
		}

		boolean isClosable = isClosable();
		if (!isClosable) {
			options.addOption(PropertyKeys.closable.toString(), isClosable);
		}

		options.addOption(PropertyKeys.spacing_open.toString(), getSpacingOpen());
		options.addOption(PropertyKeys.spacing_closed.toString(), getSpacingClosed());

		boolean initClosed = isInitClosed();
		if (initClosed) {
			options.addOption(PropertyKeys.initClosed.toString(), initClosed);
		}

		boolean initHidden = isInitHidden();
		if (initHidden) {
			options.addOption(PropertyKeys.initHidden.toString(), initHidden);
		}

		boolean isResizeWhileDragging = isResizeWhileDragging();
		if (isResizeWhileDragging) {
			options.addOption(PropertyKeys.resizeWhileDragging.toString(), isResizeWhileDragging);
		}

		boolean isMaskContents = isMaskContents();
		if (isMaskContents) {
			options.addOption(PropertyKeys.maskContents.toString(), isMaskContents);
			options.addOption("contentIgnoreSelector", ".ui-layout-mask");
		}

		boolean isMaskObjects = isMaskObjects();
		if (isMaskObjects) {
			options.addOption(PropertyKeys.maskObjects.toString(), isMaskObjects);
			if (!isMaskContents) {
				options.addOption("contentIgnoreSelector", ".ui-layout-mask");
			}
		}

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

	private void setOptions(UIComponent parent) {
		// create layout options for this pane via attributes defined in pe:layoutPane
		String position = getPosition();
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

				boolean maskPanesEarly = layout.isMaskPanesEarly();
				if (maskPanesEarly) {
					options.addOption(Layout.PropertyKeys.maskPanesEarly.toString(), maskPanesEarly);
				}

				if (panes != null) {
					options.setPanesOptions(panes);
				}
			}
		} else if (parent instanceof UIForm) {
			// layout pane can be within a h:form
			setOptions(parent.getParent());

			return;
		} else if (parent instanceof HtmlPanelGroup
		           && Layout.STYLE_CLASS_LAYOUT_CONTENT.equals(((HtmlPanelGroup) parent).getStyleClass())
		           && "block".equals(((HtmlPanelGroup) parent).getLayout())) {
			// layout pane can be within h:panelGroup representing a HTML div
			setOptions(parent.getParent());

			return;
		} else if (parent instanceof OutputPanel
		           && Layout.STYLE_CLASS_LAYOUT_CONTENT.equals(((OutputPanel) parent).getStyleClass())) {
			// layout pane can be within p:outputPanel representing a HTML div
			setOptions(parent.getParent());

			return;
		} else if (parent != null && parent.toString().contains(Layout.STYLE_CLASS_LAYOUT_CONTENT)) {
			// plain div (UIInstructions) with class "ui-layout-content"
			setOptions(parent.getParent());

			return;
		} else {
			throw new FacesException(
			    "LayoutPane can be only placed within another LayoutPane, Layout, UIForm or DIV with class 'ui-layout-content'");
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
}
