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
package org.primefaces.extensions.showcase.model.fluidgrid;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.SelectItem;

/**
 * DynamicField
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 */
public class DynamicField implements Serializable {

    private static final long serialVersionUID = 1L;
    private String label;
    private Object value;
    private boolean required;
    private List<SelectItem> selectItems;

    public DynamicField() {
    }

    public DynamicField(String label, Object value, boolean required, List<SelectItem> selectItems) {
        this.label = label;
        this.value = value;
        this.required = required;
        this.selectItems = selectItems;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public List<SelectItem> getSelectItems() {
        return selectItems;
    }

    public void setSelectItems(List<SelectItem> selectItems) {
        this.selectItems = selectItems;
    }
}
