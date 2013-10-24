/*
 * Copyright 2011-2013 PrimeFaces Extensions.
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

package org.primefaces.extensions.model.fluidgrid;

import org.primefaces.extensions.model.common.KeyData;
import org.primefaces.extensions.model.dynaform.AbstractDynaFormElement;

/**
 * Class representing an item inside of <code>FluidGrid</code>.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since   1.1.0
 */
public class FluidGridItem extends AbstractDynaFormElement implements KeyData
{
    public static final String DEFAULT_TYPE = "default";
    
    public String getKey() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setKey(String key) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public Object getData() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setData(Object data) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
