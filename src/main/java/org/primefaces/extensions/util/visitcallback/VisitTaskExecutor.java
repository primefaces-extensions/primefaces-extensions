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
