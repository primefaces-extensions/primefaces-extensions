/*
 * Copyright (c) 2011-2022 PrimeFaces Extensions
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
