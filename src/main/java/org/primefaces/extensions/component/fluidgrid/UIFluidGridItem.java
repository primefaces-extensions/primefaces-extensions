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
package org.primefaces.extensions.component.fluidgrid;

import javax.faces.component.UIComponentBase;

import org.primefaces.extensions.model.fluidgrid.FluidGridItem;

/**
 * <code>UIFluidGridItem</code> component.
 *
 * @author Oleg Varaksin / last modified by Melloware
 * @since 1.1.0
 */
public class UIFluidGridItem extends UIComponentBase {
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";

    /**
     * Properties that are tracked by state saving.
     *
     * @author Oleg Varaksin / last modified by $Author$
     */
    protected enum PropertyKeys {
      // @formatter:off
      type,
      styleClass;
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

    public UIFluidGridItem() {
        setRendererType(null);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public void setType(final String type) {
        getStateHelper().put(PropertyKeys.type, type);
    }

    public String getType() {
        return (String) getStateHelper().eval(PropertyKeys.type, FluidGridItem.DEFAULT_TYPE);
    }

    public void setStyleClass(final String styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, styleClass);
    }

    public String getStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.styleClass, null);
    }
}
