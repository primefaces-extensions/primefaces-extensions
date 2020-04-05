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
package org.primefaces.extensions.util.visitcallback;

import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;

/**
 * Visit callback to call any "task executor" implementing {@link VisitTaskExecutor} while traversing the component tree. It can be used from outside (see the
 * showcase).
 *
 * @author Oleg Varaksin
 * @version $Revision: 1.0 $
 * @since 0.7
 */
public class ExecutableVisitCallback implements VisitCallback {

    private VisitTaskExecutor visitTaskExecutor;

    public ExecutableVisitCallback(VisitTaskExecutor visitTaskExecutor) {
        this.visitTaskExecutor = visitTaskExecutor;
    }

    public VisitResult visit(VisitContext context, UIComponent target) {
        if (visitTaskExecutor.shouldExecute(target)) {
            return visitTaskExecutor.execute(target);
        }

        return VisitResult.ACCEPT;
    }
}
