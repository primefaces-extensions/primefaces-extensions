/*
 * Copyright 2011-2021 PrimeFaces Extensions
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
 * Distance
 *
 * @author Mauricio Fenoglio / last modified by $Author$
 * @version $Revision$
 * @since 0.3
 */
public class Distance implements Serializable {

    private static final long serialVersionUID = 1L;

    private Double meters;
    private Double centimeters;

    public Distance() {
        this.meters = 0d;
        this.centimeters = 0d;
    }

    public Double getCentimeters() {
        return centimeters;
    }

    public void setCentimeters(Double centimeters) {
        this.centimeters = centimeters;
        this.meters = centimeters == 0 ? 0d : centimeters / 100;
    }

    public Double getMeters() {
        return meters;
    }

    public void setMeters(Double meters) {
        this.meters = meters;
        this.centimeters = meters * 100;
    }

    @Override
    public String toString() {
        return "Distance{" + "meters=" + meters + ", centimeters=" + centimeters + '}';
    }
}
