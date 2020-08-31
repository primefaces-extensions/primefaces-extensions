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
package org.primefaces.extensions.showcase.controller.legend;

import java.io.Serializable;
import java.util.LinkedHashMap;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 * Legend Controller.
 *
 * @author Melloware mellowaredev@gmail.com
 */
@Named
@ViewScoped
public class LegendController implements Serializable {

    private static final long serialVersionUID = 20120224L;

    private LinkedHashMap<String, String> values;

    @PostConstruct
    public void init() {
        values = new LinkedHashMap<String, String>();
        values.put("0 - 20%", "#F1EEF6");
        values.put("40%", "#BDC9E1");
        values.put("60%", "#74A9CF");
        values.put("80%", "#2B8CBE");
        values.put("100%", "#045A8D");
    }

    public LinkedHashMap<String, String> getValues() {
        return values;
    }

    public void setValues(final LinkedHashMap<String, String> values) {
        this.values = values;
    }

}
