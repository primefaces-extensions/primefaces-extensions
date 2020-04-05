/**
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
package org.primefaces.extensions.model.dynaform;

/**
 * Class representing a nested model inside of <code>DynaFormRow</code>.
 *
 * @author Sébastien Lepage / last modified by $Author$
 * @version $Revision$
 * @since 4.0.0
 */
public class DynaFormModelElement extends AbstractDynaFormElement {

    private static final long serialVersionUID = 1L;

    private final DynaFormModel model;

    public DynaFormModelElement(DynaFormModel model, int colspan, int rowspan, int row, int column, int position, boolean extended) {
        super(colspan, rowspan, row, column, extended);
        this.model = model;

        for (final DynaFormControl control : model.getControls()) {
            control.setPosition(position + control.getPosition());
            control.generateKey();
        }
    }

    public DynaFormModel getModel() {
        return model;
    }

}
