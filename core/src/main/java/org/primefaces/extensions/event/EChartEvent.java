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
package org.primefaces.extensions.event;

import jakarta.faces.component.UIComponent;
import jakarta.faces.component.behavior.Behavior;

import org.primefaces.event.AbstractAjaxBehaviorEvent;

public class EChartEvent extends AbstractAjaxBehaviorEvent {
    private static final long serialVersionUID = 1L;

    private String name;
    private int dataIndex;
    private int seriesIndex;
    private String componentType;
    private String componentSubType;
    private String seriesType;
    private String seriesName;
    private String data;
    private int componentIndex;

    public EChartEvent(UIComponent component, Behavior behavior, String name, int dataIndex, int seriesIndex, String componentType, String componentSubType,
                String seriesType, String seriesName, String data, int componentIndex) {
        super(component, behavior);
        this.name = name;
        this.dataIndex = dataIndex;
        this.seriesIndex = seriesIndex;
        this.componentType = componentType;
        this.componentSubType = componentSubType;
        this.seriesType = seriesType;
        this.seriesName = seriesName;
        this.data = data;
        this.componentIndex = componentIndex;
    }

    public String getName() {
        return name;
    }

    public int getDataIndex() {
        return dataIndex;
    }

    public int getSeriesIndex() {
        return seriesIndex;
    }

    public String getComponentType() {
        return componentType;
    }

    public String getComponentSubType() {
        return componentSubType;
    }

    public String getSeriesType() {
        return seriesType;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public String getData() {
        return data;
    }

    public int getComponentIndex() {
        return componentIndex;
    }
}