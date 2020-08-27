/*
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
package org.primefaces.extensions.behavior.javascript;

import javax.faces.view.BehaviorHolderAttachedObjectHandler;
import javax.faces.view.facelets.BehaviorConfig;
import javax.faces.view.facelets.TagHandler;

import org.primefaces.behavior.base.AbstractBehaviorHandler;

/**
 * {@link BehaviorHolderAttachedObjectHandler} and {@link TagHandler} implementation for the {@link JavascriptBehavior}.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.2
 */
public class JavascriptBehaviorHandler extends AbstractBehaviorHandler<JavascriptBehavior> {

    public JavascriptBehaviorHandler(final BehaviorConfig config) {
        super(config);
    }

    @Override
    public String getBehaviorId() {
        return JavascriptBehavior.BEHAVIOR_ID;
    }

}
