/*
 * Copyright (c) 2011-2023 PrimeFaces Extensions
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
package org.primefaces.extensions.util.visitcallback;

import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitResult;

/**
 * Interface for all executor classes which are called by visit callbacks. It can be used from outside (see the showcase).
 *
 * @author Oleg Varaksin
 * @version $Revision: 1.0 $
 * @since 0.7
 */
public interface VisitTaskExecutor {

    /**
     * Execute some task on the given component.
     *
     * @param component UIComponent
     * @return VisitResult (ACCEPT, REJECT, COMPLETE)
     */
    VisitResult execute(UIComponent component);

    /**
     * Should the task on the given component be executed?
     *
     * @param component UIComponent
     * @return boolean true - the task should be executed, false - otherwise
     */
    boolean shouldExecute(UIComponent component);
}
