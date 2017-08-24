/**
 * Copyright 2011-2017 PrimeFaces Extensions
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
package org.primefaces.extensions.component.masterdetail;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

/**
 * Event class for master-detail navigation.
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since 0.2
 */
public class SelectLevelEvent extends FacesEvent {

    private int currentLevel;
    private int newLevel;

    public SelectLevelEvent(UIComponent component, int currentLevel, int newLevel) {
        super(component);
        this.currentLevel = currentLevel;
        this.newLevel = newLevel;
    }

    @Override
    public boolean isAppropriateListener(FacesListener listener) {
        return false;
    }

    @Override
    public void processListener(FacesListener listener) {
        throw new UnsupportedOperationException();
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public int getNewLevel() {
        return newLevel;
    }

    public boolean isEntered(int level) {
        return newLevel == level && currentLevel != level;
    }
}
