/**
 * Copyright 2011-2019 PrimeFaces Extensions
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
package org.primefaces.extensions.component.gchart.model;

public class DefaultGChartModelColumn implements GChartModelColumn {

    private static final long serialVersionUID = -4757917806522708660L;

    // column label
    private final String label;

    // column type: number, date, datetime
    private final String type;

    public DefaultGChartModelColumn(String label, String type) {
        super();
        this.label = label;
        this.type = type;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String getType() {
        return type;
    }
}
