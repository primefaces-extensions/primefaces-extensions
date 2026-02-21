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
package org.primefaces.extensions.component.legend;

import jakarta.faces.application.ResourceDependency;
import jakarta.faces.component.FacesComponent;

import org.primefaces.cdk.api.FacesComponentInfo;
import org.primefaces.extensions.util.Constants;

/**
 * <code>Legend</code> component.
 *
 * @author Melloware mellowaredev@gmail.com
 * @since 7.1
 */
@FacesComponent(value = Legend.COMPONENT_TYPE, namespace = Legend.COMPONENT_FAMILY)
@FacesComponentInfo(description = "Legend component for chart labels and colors.")
@ResourceDependency(library = "primefaces", name = "components.css")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = Constants.LIBRARY, name = "legend/legend.css")
@ResourceDependency(library = Constants.LIBRARY, name = "legend/legend.js")
public class Legend extends LegendBaseImpl {

    public static final String STYLE_CLASS_VERTICAL = "ui-legend-vertical ";
    public static final String STYLE_CLASS_HORIZONTAL = "ui-legend-horizontal ";
    public static final String SCALE_STYLE = "ui-legend-scale";
    public static final String LABELS_STYLE = "ui-legend-labels";
    public static final String TITLE_STYLE = "ui-legend-title";
    public static final String FOOTER_STYLE = "ui-legend-footer";

}
