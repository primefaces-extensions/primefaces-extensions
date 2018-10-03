/**
 * Copyright 2011-2018 PrimeFaces Extensions
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
package org.primefaces.extensions.component.codemirror;

import java.util.List;

import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.MetaRuleset;

import org.primefaces.extensions.event.CompleteEvent;
import org.primefaces.facelets.MethodRule;

/**
 * {@link ComponentHandler} for the {@link CodeMirror} component.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.3
 */
public class CodeMirrorHandler extends ComponentHandler {

    public CodeMirrorHandler(final ComponentConfig config) {
        super(config);
    }

    @Override
    protected MetaRuleset createMetaRuleset(final Class type) {
        final MetaRuleset metaRuleset = super.createMetaRuleset(type);

        metaRuleset.addRule(new MethodRule("completeMethod", List.class, new Class[] {CompleteEvent.class}));

        return metaRuleset;
    }
}
