/*
 * Copyright (c) 2011-2026 PrimeFaces Extensions
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */
package org.primefaces.extensions.component.layout;

import jakarta.faces.FacesException;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIForm;
import jakarta.faces.component.html.HtmlPanelGroup;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ComponentSystemEvent;
import jakarta.faces.event.ListenerFor;
import jakarta.faces.event.PreRenderComponentEvent;

import org.primefaces.component.outputpanel.OutputPanel;
import org.primefaces.extensions.model.layout.LayoutOptions;

/**
 * <code>LayoutPane</code> component.
 *
 * @author Oleg Varaksin / last modified by Melloware
 * @since 0.2
 */
@jakarta.faces.component.FacesComponent(value = LayoutPane.COMPONENT_TYPE, namespace = LayoutPane.COMPONENT_FAMILY)
@org.primefaces.cdk.api.FacesComponentInfo(description = "Layout pane for border layout (north, south, east, west, center).")
@ListenerFor(systemEventClass = PreRenderComponentEvent.class)
public class LayoutPane extends LayoutPaneBaseImpl {

    private LayoutOptions options;

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

        options = new LayoutOptions();

        if (!isResizable()) {
            options.addOption("resizable", false);
        }

        if (!isSlidable()) {
            options.addOption("slideable", false);
        }

        if (!isClosable()) {
            options.addOption("closable", false);
        }

        options.addOption("spacing_open", getSpacingOpen());
        options.addOption("spacing_closed", getSpacingClosed());

        if (isInitClosed()) {
            options.addOption("initClosed", true);
        }

        if (isInitHidden()) {
            options.addOption("initHidden", true);
        }

        if (isResizeWhileDragging()) {
            options.addOption("resizeWhileDragging", true);
        }

        if (isMaskContents()) {
            options.addOption("maskContents", true);
            options.addOption("contentIgnoreSelector", ".ui-layout-mask");
        }

        if (isMaskObjects()) {
            options.addOption("maskObjects", true);
            if (!isMaskContents()) {
                options.addOption("contentIgnoreSelector", ".ui-layout-mask");
            }
        }

        final String paneSelector = getPaneSelector();
        if (paneSelector != null) {
            options.addOption("paneSelector", paneSelector);
        }

        final String size = getSize();
        if (size != null) {
            options.addOption("size", size);
        }

        final String minSize = getMinSize();
        if (minSize != null) {
            options.addOption("minSize", minSize);
        }

        final String maxSize = getMaxSize();
        if (maxSize != null) {
            options.addOption("maxSize", maxSize);
        }

        final String minWidth = getMinWidth();
        if (minWidth != null) {
            options.addOption("minWidth", minWidth);
        }

        final String maxWidth = getMaxWidth();
        if (maxWidth != null) {
            options.addOption("maxWidth", maxWidth);
        }

        final String minHeight = getMinHeight();
        if (minHeight != null) {
            options.addOption("minHeight", minHeight);
        }

        final String maxHeight = getMaxHeight();
        if (maxHeight != null) {
            options.addOption("maxHeight", maxHeight);
        }

        final LayoutOptions tips = new LayoutOptions();
        options.setTips(tips);

        final String resizerTip = getResizerTip();
        if (resizerTip != null) {
            tips.addOption("resizerTip", resizerTip);
        }

        final String sliderTip = getSliderTip();
        if (sliderTip != null) {
            tips.addOption("sliderTip", sliderTip);
        }

        final String togglerTipOpen = getTogglerTipOpen();
        if (togglerTipOpen != null) {
            tips.addOption("togglerTip_open", togglerTipOpen);
        }

        final String togglerTipClosed = getTogglerTipClosed();
        if (togglerTipClosed != null) {
            tips.addOption("togglerTip_closed", togglerTipClosed);
        }

        return options;
    }

    private void setOptions(final UIComponent parent) {
        final String position = getPosition();
        final LayoutOptions thisLayoutOptions = getOptions();
        LayoutOptions parentOpts;

        if (parent instanceof LayoutPane) {
            final LayoutOptions parentLayoutOptions = ((LayoutPane) parent).getOptions();
            parentOpts = parentLayoutOptions.getChildOptions();
            if (parentOpts == null) {
                parentOpts = new LayoutOptions();
                parentLayoutOptions.setChildOptions(parentOpts);
            }
        }
        else if (parent instanceof Layout) {
            parentOpts = (LayoutOptions) ((Layout) parent).getOptions();
            if (parentOpts == null) {
                final Layout layout = (Layout) parent;
                parentOpts = new LayoutOptions();
                layout.setOptions(parentOpts);

                final LayoutOptions defaults = new LayoutOptions();
                parentOpts.setPanesOptions(defaults);
                final LayoutOptions tips = new LayoutOptions();
                defaults.setTips(tips);

                final String resizerTip = layout.getResizerTip();
                if (resizerTip != null) {
                    tips.addOption("resizerTip", resizerTip);
                }

                final String sliderTip = layout.getSliderTip();
                if (sliderTip != null) {
                    tips.addOption("sliderTip", sliderTip);
                }

                final String togglerTipOpen = layout.getTogglerTipOpen();
                if (togglerTipOpen != null) {
                    tips.addOption("togglerTip_open", togglerTipOpen);
                }

                final String togglerTipClosed = layout.getTogglerTipClosed();
                if (togglerTipClosed != null) {
                    tips.addOption("togglerTip_closed", togglerTipClosed);
                }

                if (layout.isMaskPanesEarly()) {
                    parentOpts.addOption("maskPanesEarly", true);
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
            parentOpts.setCenterOptions(thisLayoutOptions);
        }
        else if (Layout.PANE_POSITION_NORTH.equals(position)) {
            parentOpts.setNorthOptions(thisLayoutOptions);
        }
        else if (Layout.PANE_POSITION_SOUTH.equals(position)) {
            parentOpts.setSouthOptions(thisLayoutOptions);
        }
        else if (Layout.PANE_POSITION_WEST.equals(position)) {
            parentOpts.setWestOptions(thisLayoutOptions);
        }
        else if (Layout.PANE_POSITION_EAST.equals(position)) {
            parentOpts.setEastOptions(thisLayoutOptions);
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

    @Override
    public Object saveState(final FacesContext context) {
        options = null;

        return super.saveState(context);
    }
}
