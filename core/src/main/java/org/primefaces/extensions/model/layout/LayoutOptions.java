/*
 * Copyright (c) 2011-2021 PrimeFaces Extensions
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
package org.primefaces.extensions.model.layout;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.primefaces.extensions.component.layout.GsonLayoutOptions;

/**
 * Model class representing layout options.
 *
 * @author Oleg Varaksin / last modified by Melloware
 * @since 0.6.0
 */
public class LayoutOptions implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    // direct options
    private Map<String, Object> options = new HashMap<>();

    // options for all panes
    private LayoutOptions defaults;

    // tooltips
    private LayoutOptions tips;

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

    public LayoutOptions(final String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public Map<String, Object> getOptions() {
        return options;
    }

    public void setOptions(final Map<String, Object> options) {
        this.options = options;
    }

    public void addOption(final String key, final Object value) {
        options.put(key, value);
    }

    public void addOptions(final Map<String, Object> options) {
        options.putAll(options);
    }

    public void setPanesOptions(final LayoutOptions layoutOptions) {
        defaults = layoutOptions;
    }

    public LayoutOptions getPanesOptions() {
        return defaults;
    }

    public final LayoutOptions getTips() {
        return tips;
    }

    public final void setTips(final LayoutOptions tips) {
        this.tips = tips;
    }

    public void setNorthOptions(final LayoutOptions layoutOptions) {
        north = layoutOptions;
    }

    public LayoutOptions getNorthOptions() {
        return north;
    }

    public void setSouthOptions(final LayoutOptions layoutOptions) {
        south = layoutOptions;
    }

    public LayoutOptions getSouthOptions() {
        return south;
    }

    public void setWestOptions(final LayoutOptions layoutOptions) {
        west = layoutOptions;
    }

    public LayoutOptions getWestOptions() {
        return west;
    }

    public void setEastOptions(final LayoutOptions layoutOptions) {
        east = layoutOptions;
    }

    public LayoutOptions getEastOptions() {
        return east;
    }

    public void setCenterOptions(final LayoutOptions layoutOptions) {
        center = layoutOptions;
    }

    public LayoutOptions getCenterOptions() {
        return center;
    }

    public void setChildOptions(final LayoutOptions layoutOptions) {
        child = layoutOptions;
    }

    public LayoutOptions getChildOptions() {
        return child;
    }

    public Set<LayoutOptions> getDirectionPanes() {
        final Set<LayoutOptions> result = new HashSet<>();
        result.add(getCenterOptions());
        result.add(getNorthOptions());
        result.add(getEastOptions());
        result.add(getWestOptions());
        result.add(getSouthOptions());
        return result;
    }

    public LayoutOptions getLayoutOptions(final String id) {
        if (id == null) {
            return null;
        }

        LayoutOptions loOptions = null;

        if (child != null) {
            if (id.equals(child.getId())) {
                return child;
            }
            else {
                loOptions = child.getLayoutOptions(id);
            }
        }

        if (loOptions == null && defaults != null) {
            if (id.equals(defaults.getId())) {
                return defaults;
            }
            else {
                loOptions = defaults.getLayoutOptions(id);
            }
        }

        if (loOptions == null && tips != null) {
            if (id.equals(tips.getId())) {
                return tips;
            }
            else {
                loOptions = tips.getLayoutOptions(id);
            }
        }

        if (loOptions == null && center != null) {
            if (id.equals(center.getId())) {
                return center;
            }
            else {
                loOptions = center.getLayoutOptions(id);
            }
        }

        if (loOptions == null && north != null) {
            if (id.equals(north.getId())) {
                return north;
            }
            else {
                loOptions = north.getLayoutOptions(id);
            }
        }

        if (loOptions == null && south != null) {
            if (id.equals(south.getId())) {
                return south;
            }
            else {
                loOptions = south.getLayoutOptions(id);
            }
        }

        if (loOptions == null && east != null) {
            if (id.equals(east.getId())) {
                return east;
            }
            else {
                loOptions = east.getLayoutOptions(id);
            }
        }

        if (loOptions == null && west != null) {
            if (id.equals(west.getId())) {
                return west;
            }
            else {
                loOptions = west.getLayoutOptions(id);
            }
        }

        return loOptions;
    }

    public boolean replace(final String id, final LayoutOptions layoutOptions) {
        boolean replaced = false;

        if (id == null) {
            return replaced;
        }

        if (child != null) {
            if (id.equals(child.getId())) {
                child = layoutOptions;

                return true;
            }
            else {
                replaced = child.replace(id, layoutOptions);
            }
        }

        if (!replaced && defaults != null) {
            if (id.equals(defaults.getId())) {
                defaults = layoutOptions;

                return true;
            }
            else {
                replaced = defaults.replace(id, layoutOptions);
            }
        }

        if (!replaced && tips != null) {
            if (id.equals(tips.getId())) {
                tips = layoutOptions;

                return true;
            }
            else {
                replaced = tips.replace(id, layoutOptions);
            }
        }

        if (!replaced && center != null) {
            if (id.equals(center.getId())) {
                center = layoutOptions;

                return true;
            }
            else {
                replaced = center.replace(id, layoutOptions);
            }
        }

        if (!replaced && north != null) {
            if (id.equals(north.getId())) {
                north = layoutOptions;

                return true;
            }
            else {
                replaced = north.replace(id, layoutOptions);
            }
        }

        if (!replaced && south != null) {
            if (id.equals(south.getId())) {
                south = layoutOptions;

                return true;
            }
            else {
                replaced = south.replace(id, layoutOptions);
            }
        }

        if (!replaced && east != null) {
            if (id.equals(east.getId())) {
                east = layoutOptions;

                return true;
            }
            else {
                replaced = east.replace(id, layoutOptions);
            }
        }

        if (!replaced && west != null) {
            if (id.equals(west.getId())) {
                west = layoutOptions;

                return true;
            }
            else {
                replaced = west.replace(id, layoutOptions);
            }
        }

        return replaced;
    }

    /**
     * Serializes layout options to JSON string.
     *
     * @return Layout options as JSON string
     */
    public String toJson() {
        return GsonLayoutOptions.getGson().toJson(this);
    }

}
