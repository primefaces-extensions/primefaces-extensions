/*
 * Copyright (c) 2011-2021 PrimeFaces Extensions
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
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

    private static final long serialVersionUID = 1L;

    private final int currentLevel;
    private final int newLevel;

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
