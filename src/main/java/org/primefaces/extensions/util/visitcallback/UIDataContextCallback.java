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

package org.primefaces.extensions.util.visitcallback;

import javax.faces.component.ContextCallback;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;

/**
 * See {@link org.primefaces.extensions.component.timeline.Timeline}.
 *
 * @author  Oleg Varaksin
 * @version $Revision: 1.0 $
 * @since   1.0.0
 */
public class UIDataContextCallback implements ContextCallback
{
    private String uiDataId;
    private Object data;

    public UIDataContextCallback(String uiDataId) {
        this.uiDataId = uiDataId;
    }

    public void invokeContextCallback(FacesContext fc, UIComponent component) {
        UIData uiData = (UIData)component;
        String[] idTokens = uiDataId.split(String.valueOf(UINamingContainer.getSeparatorChar(fc)));
        int rowIndex = Integer.parseInt(idTokens[idTokens.length - 2]);
        uiData.setRowIndex(rowIndex);
        data = uiData.getRowData();
        uiData.setRowIndex(-1);
    }

    public Object getData() {
        return data;
    }
}
