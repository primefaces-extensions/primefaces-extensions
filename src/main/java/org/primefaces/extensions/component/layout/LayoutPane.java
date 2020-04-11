/*
 * Copyright 2011-2020 PrimeFaces Extensions
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
package org.primefaces.extensions.component.layout;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ListenerFor;
import javax.faces.event.PreRenderComponentEvent;

import org.primefaces.component.outputpanel.OutputPanel;
import org.primefaces.extensions.model.layout.LayoutOptions;

/**
 * <code>LayoutPane</code> component.
 *
 * @author Oleg Varaksin / last modified by Melloware
 * @since 0.2
 */
@ListenerFor(systemEventClass = PreRenderComponentEvent.class)
public class LayoutPane extends UIComponentBase {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.LayoutPane";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.LayoutPaneRenderer";

    private LayoutOptions options;

    /**
     * Properties that are tracked by state saving.
     *
     * @author Oleg Varaksin / last modified by $Author$
     * @version $Revision$
     */
    protected enum PropertyKeys {

        // @formatter:off
        position,
        paneSelector,
        combinedPosition,
        styleHeader,
        styleClassHeader,
        styleContent,
        styleClassContent,
        resizable,
        slideable,
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
        togglerTip_open("Open"),
        togglerTip_closed("Close"),
        resizerTip("Resize"),
        sliderTip("Slide"),
        maskContents,
        maskObjects;
        // @formatter:on

        private String toString;

        PropertyKeys(final String toString) {
            this.toString = toString;
        }

        PropertyKeys() {
        }

        @Override
        public String toString() {
            return toString != null ? toString : super.toString();
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

    public void setStyleHeader(final String styleHeader) {
        getStateHelper().put(PropertyKeys.styleHeader, styleHeader);
    }

    public String getStyleClassHeader() {
        return (String) getStateHelper().eval(PropertyKeys.styleClassHeader, null);
    }

    public void setStyleClassHeader(final String styleClassHeader) {
        getStateHelper().put(PropertyKeys.styleClassHeader, styleClassHeader);
    }

    public String getStyleContent() {
        return (String) getStateHelper().eval(PropertyKeys.styleContent, null);
    }

    public void setStyleContent(final String styleContent) {
        getStateHelper().put(PropertyKeys.styleContent, styleContent);
    }

    public String getStyleClassContent() {
        return (String) getStateHelper().eval(PropertyKeys.styleClassContent, null);
    }

    public void setStyleClassContent(final String styleClassContent) {
        getStateHelper().put(PropertyKeys.styleClassContent, styleClassContent);
    }

    // position "north" | "south" | "west" | "east" | "center"
    public String getPosition() {
        return (String) getStateHelper().eval(PropertyKeys.position, Layout.PANE_POSITION_CENTER);
    }

    public void setPosition(final String position) {
        getStateHelper().put(PropertyKeys.position, position);
    }

    public String getPaneSelector() {
        return (String) getStateHelper().eval(PropertyKeys.paneSelector, null);
    }

    public void setPaneSelector(final String paneSelector) {
        getStateHelper().put(PropertyKeys.paneSelector, paneSelector);
    }

    public String getCombinedPosition() {
        return (String) getStateHelper().eval(PropertyKeys.combinedPosition, Layout.PANE_POSITION_CENTER);
    }

    public void setCombinedPosition(final String combinedPosition) {
        getStateHelper().put(PropertyKeys.combinedPosition, combinedPosition);
    }

    public String getTogglerTipOpen() {
        return (String) getStateHelper().eval(PropertyKeys.togglerTip_open, null);
    }

    public void setTogglerTipOpen(final String togglerTipOpen) {
        getStateHelper().put(PropertyKeys.togglerTip_open, togglerTipOpen);
    }

    public String getTogglerTipClosed() {
        return (String) getStateHelper().eval(PropertyKeys.togglerTip_closed, null);
    }

    public void setTogglerTipClosed(final String togglerTipClosed) {
        getStateHelper().put(PropertyKeys.togglerTip_closed, togglerTipClosed);
    }

    public String getResizerTip() {
        return (String) getStateHelper().eval(PropertyKeys.resizerTip, null);
    }

    public void setResizerTip(final String resizerTip) {
        getStateHelper().put(PropertyKeys.resizerTip, resizerTip);
    }

    public String getSliderTip() {
        return (String) getStateHelper().eval(PropertyKeys.sliderTip, null);
    }

    public void setSliderTip(final String sliderTip) {
        getStateHelper().put(PropertyKeys.sliderTip, sliderTip);
    }

    public boolean isResizable() {
        return (Boolean) getStateHelper().eval(PropertyKeys.resizable, true);
    }

    public void setResizable(final boolean resizable) {
        getStateHelper().put(PropertyKeys.resizable, resizable);
    }

    public boolean isSlidable() {
        return (Boolean) getStateHelper().eval(PropertyKeys.slideable, true);
    }

    public void setSlidable(final boolean slideable) {
        getStateHelper().put(PropertyKeys.slideable, slideable);
    }

    public boolean isClosable() {
        return (Boolean) getStateHelper().eval(PropertyKeys.closable, true);
    }

    public void setClosable(final boolean closable) {
        getStateHelper().put(PropertyKeys.closable, closable);
    }

    public String getSize() {
        return (String) getStateHelper().eval(PropertyKeys.size, null);
    }

    public void setSize(final String size) {
        getStateHelper().put(PropertyKeys.size, size);
    }

    public String getMinSize() {
        return (String) getStateHelper().eval(PropertyKeys.minSize, null);
    }

    public void setMinSize(final String minSize) {
        getStateHelper().put(PropertyKeys.minSize, minSize);
    }

    public String getMaxSize() {
        return (String) getStateHelper().eval(PropertyKeys.maxSize, null);
    }

    public void setMaxSize(final String maxSize) {
        getStateHelper().put(PropertyKeys.maxSize, maxSize);
    }

    public String getMinWidth() {
        return (String) getStateHelper().eval(PropertyKeys.minWidth, null);
    }

    public void setMinWidth(final String minWidth) {
        getStateHelper().put(PropertyKeys.minWidth, minWidth);
    }

    public String getMaxWidth() {
        return (String) getStateHelper().eval(PropertyKeys.maxWidth, null);
    }

    public void setMaxWidth(final String maxWidth) {
        getStateHelper().put(PropertyKeys.maxWidth, maxWidth);
    }

    public String getMinHeight() {
        return (String) getStateHelper().eval(PropertyKeys.minHeight, null);
    }

    public void setMinHeight(final String minHeight) {
        getStateHelper().put(PropertyKeys.minHeight, minHeight);
    }

    public String getMaxHeight() {
        return (String) getStateHelper().eval(PropertyKeys.maxHeight, null);
    }

    public void setMaxHeight(final String maxHeight) {
        getStateHelper().put(PropertyKeys.maxHeight, maxHeight);
    }

    public int getSpacingOpen() {
        return (Integer) getStateHelper().eval(PropertyKeys.spacing_open, 6);
    }

    public void setSpacingOpen(final int spacingOpen) {
        getStateHelper().put(PropertyKeys.spacing_open, spacingOpen);
    }

    public int getSpacingClosed() {
        return (Integer) getStateHelper().eval(PropertyKeys.spacing_closed, 6);
    }

    public void setSpacingClosed(final int spacingClosed) {
        getStateHelper().put(PropertyKeys.spacing_closed, spacingClosed);
    }

    public boolean isInitClosed() {
        return (Boolean) getStateHelper().eval(PropertyKeys.initClosed, false);
    }

    public void setInitClosed(final boolean initClosed) {
        getStateHelper().put(PropertyKeys.initClosed, initClosed);
    }

    public boolean isInitHidden() {
        return (Boolean) getStateHelper().eval(PropertyKeys.initHidden, false);
    }

    public void setInitHidden(final boolean initHidden) {
        getStateHelper().put(PropertyKeys.initHidden, initHidden);
    }

    public boolean isResizeWhileDragging() {
        return (Boolean) getStateHelper().eval(PropertyKeys.resizeWhileDragging, false);
    }

    public void setResizeWhileDragging(final boolean resizeWhileDragging) {
        getStateHelper().put(PropertyKeys.resizeWhileDragging, resizeWhileDragging);
    }

    public boolean isMaskContents() {
        return (Boolean) getStateHelper().eval(PropertyKeys.maskContents, false);
    }

    public void setMaskContents(final boolean maskContents) {
        getStateHelper().put(PropertyKeys.maskContents, maskContents);
    }

    public boolean isMaskObjects() {
        return (Boolean) getStateHelper().eval(PropertyKeys.maskObjects, false);
    }

    public void setMaskObjects(final boolean maskObjects) {
        getStateHelper().put(PropertyKeys.maskObjects, maskObjects);
    }

    @Override
    public void processEvent(final ComponentSystemEvent event) {
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

        final boolean isResizable = isResizable();
        if (!isResizable) {
            options.addOption(PropertyKeys.resizable.toString(), false);
        }

        final boolean isSlidable = isSlidable();
        if (!isSlidable) {
            options.addOption(PropertyKeys.slideable.toString(), false);
        }

        final boolean isClosable = isClosable();
        if (!isClosable) {
            options.addOption(PropertyKeys.closable.toString(), false);
        }

        options.addOption(PropertyKeys.spacing_open.toString(), getSpacingOpen());
        options.addOption(PropertyKeys.spacing_closed.toString(), getSpacingClosed());

        final boolean initClosed = isInitClosed();
        if (initClosed) {
            options.addOption(PropertyKeys.initClosed.toString(), true);
        }

        final boolean initHidden = isInitHidden();
        if (initHidden) {
            options.addOption(PropertyKeys.initHidden.toString(), true);
        }

        final boolean isResizeWhileDragging = isResizeWhileDragging();
        if (isResizeWhileDragging) {
            options.addOption(PropertyKeys.resizeWhileDragging.toString(), true);
        }

        final boolean isMaskContents = isMaskContents();
        if (isMaskContents) {
            options.addOption(PropertyKeys.maskContents.toString(), true);
            options.addOption("contentIgnoreSelector", ".ui-layout-mask");
        }

        final boolean isMaskObjects = isMaskObjects();
        if (isMaskObjects) {
            options.addOption(PropertyKeys.maskObjects.toString(), true);
            if (!isMaskContents) {
                options.addOption("contentIgnoreSelector", ".ui-layout-mask");
            }
        }

        final String paneSelector = getPaneSelector();
        if (paneSelector != null) {
            options.addOption(PropertyKeys.paneSelector.toString(), paneSelector);
        }

        final String size = getSize();
        if (size != null) {
            options.addOption(PropertyKeys.size.toString(), size);
        }

        final String minSize = getMinSize();
        if (minSize != null) {
            options.addOption(PropertyKeys.minSize.toString(), minSize);
        }

        final String maxSize = getMaxSize();
        if (maxSize != null) {
            options.addOption(PropertyKeys.maxSize.toString(), maxSize);
        }

        final String minWidth = getMinWidth();
        if (minWidth != null) {
            options.addOption(PropertyKeys.minWidth.toString(), minWidth);
        }

        final String maxWidth = getMaxWidth();
        if (maxWidth != null) {
            options.addOption(PropertyKeys.maxWidth.toString(), maxWidth);
        }

        final String minHeight = getMinHeight();
        if (minHeight != null) {
            options.addOption(PropertyKeys.minHeight.toString(), minHeight);
        }

        final String maxHeight = getMaxHeight();
        if (maxHeight != null) {
            options.addOption(PropertyKeys.maxHeight.toString(), maxHeight);
        }

        final LayoutOptions tips = new LayoutOptions();
        options.setTips(tips);

        final String resizerTip = getResizerTip();
        if (resizerTip != null) {
            tips.addOption(PropertyKeys.resizerTip.toString(), resizerTip);
        }

        final String sliderTip = getSliderTip();
        if (sliderTip != null) {
            tips.addOption(PropertyKeys.sliderTip.toString(), sliderTip);
        }

        final String togglerTipOpen = getTogglerTipOpen();
        if (togglerTipOpen != null) {
            tips.addOption(PropertyKeys.togglerTip_open.toString(), togglerTipOpen);
        }

        final String togglerTipClosed = getTogglerTipClosed();
        if (togglerTipClosed != null) {
            tips.addOption(PropertyKeys.togglerTip_closed.toString(), togglerTipClosed);
        }

        return options;
    }

    private void setOptions(final UIComponent parent) {
        // create layout options for this pane via attributes defined in
        // pe:layoutPane
        final String position = getPosition();
        final LayoutOptions thisLayoutOptions = getOptions();
        LayoutOptions opts;

        if (parent instanceof LayoutPane) {
            final LayoutOptions parentLayoutOptions = ((LayoutPane) parent).getOptions();
            opts = parentLayoutOptions.getChildOptions();
            if (opts == null) {
                opts = new LayoutOptions();
                parentLayoutOptions.setChildOptions(opts);
            }
        }
        else if (parent instanceof Layout) {
            opts = (LayoutOptions) ((Layout) parent).getOptions();
            if (opts == null) {
                final Layout layout = (Layout) parent;
                opts = new LayoutOptions();
                layout.setOptions(opts);

                // options for all panes
                final LayoutOptions defaults = new LayoutOptions();
                opts.setPanesOptions(defaults);
                final LayoutOptions tips = new LayoutOptions();
                defaults.setTips(tips);

                final String resizerTip = layout.getResizerTip();
                if (resizerTip != null) {
                    tips.addOption(Layout.PropertyKeys.resizerTip.toString(), resizerTip);
                }

                final String sliderTip = layout.getSliderTip();
                if (sliderTip != null) {
                    tips.addOption(Layout.PropertyKeys.sliderTip.toString(), sliderTip);
                }

                final String togglerTipOpen = layout.getTogglerTipOpen();
                if (togglerTipOpen != null) {
                    tips.addOption(Layout.PropertyKeys.togglerTip_open.toString(), togglerTipOpen);
                }

                final String togglerTipClosed = layout.getTogglerTipClosed();
                if (togglerTipClosed != null) {
                    tips.addOption(Layout.PropertyKeys.togglerTip_closed.toString(), togglerTipClosed);
                }

                final boolean maskPanesEarly = layout.isMaskPanesEarly();
                if (maskPanesEarly) {
                    opts.addOption(Layout.PropertyKeys.maskPanesEarly.toString(), true);
                }
            }
        }
        else if ((parent instanceof UIForm) || (parent instanceof HtmlPanelGroup
                    && Layout.STYLE_CLASS_LAYOUT_CONTENT.equals(((HtmlPanelGroup) parent).getStyleClass())
                    && "block".equals(((HtmlPanelGroup) parent).getLayout()))
                    || (parent instanceof OutputPanel
                                && Layout.STYLE_CLASS_LAYOUT_CONTENT.equals(((OutputPanel) parent).getStyleClass()))
                    ||
                    (parent != null && parent.toString().contains(Layout.STYLE_CLASS_LAYOUT_CONTENT))) {
            setOptions(parent.getParent());

            return;
        }
        else {
            throw new FacesException(
                        "LayoutPane can be only placed within another LayoutPane, Layout, UIForm or DIV with class 'ui-layout-content'");
        }

        if (Layout.PANE_POSITION_CENTER.equals(position)) {
            options.setCenterOptions(thisLayoutOptions);
        }
        else if (Layout.PANE_POSITION_NORTH.equals(position)) {
            options.setNorthOptions(thisLayoutOptions);
        }
        else if (Layout.PANE_POSITION_SOUTH.equals(position)) {
            options.setSouthOptions(thisLayoutOptions);
        }
        else if (Layout.PANE_POSITION_WEST.equals(position)) {
            options.setWestOptions(thisLayoutOptions);
        }
        else if (Layout.PANE_POSITION_EAST.equals(position)) {
            options.setEastOptions(thisLayoutOptions);
        }
        else {
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
