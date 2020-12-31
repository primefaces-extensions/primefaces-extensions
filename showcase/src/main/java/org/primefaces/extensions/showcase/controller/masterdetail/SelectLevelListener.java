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
package org.primefaces.extensions.showcase.controller.masterdetail;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.primefaces.extensions.component.masterdetail.SelectLevelEvent;

/**
 * SelectLevelListener
 *
 * @author ova / last modified by $Author$
 * @version $Revision$
 */
@Named
@RequestScoped
public class SelectLevelListener {

    private boolean errorOccured = false;

    public int handleNavigation(final SelectLevelEvent selectLevelEvent) {
        if (errorOccured) {
            return 2;
        }
        else {
            return selectLevelEvent.getNewLevel();
        }
    }

    public void setErrorOccured(final boolean errorOccured) {
        this.errorOccured = errorOccured;
    }
}
