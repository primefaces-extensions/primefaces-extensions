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
package org.primefaces.extensions.showcase.model;

import java.io.Serializable;

/**
 * Circle
 *
 * @author Thomas Andraschko / last modified by $Author: $
 * @version $Revision: 1.0 $
 */
public class Circle implements Serializable {

    private static final long serialVersionUID = 20111020L;

    private int radius;
    private String backgroundColor;
    private String borderColor;
    private double scaleFactor;

    public final int getRadius() {
        return radius;
    }

    public final void setRadius(int radius) {
        this.radius = radius;
    }

    public final String getBackgroundColor() {
        return backgroundColor;
    }

    public final void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public final String getBorderColor() {
        return borderColor;
    }

    public final void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }

    public final double getScaleFactor() {
        return scaleFactor;
    }

    public final void setScaleFactor(double scaleFactor) {
        this.scaleFactor = scaleFactor;
    }
}
