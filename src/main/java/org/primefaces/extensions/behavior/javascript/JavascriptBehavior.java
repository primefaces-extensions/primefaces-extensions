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
package org.primefaces.extensions.behavior.javascript;

import javax.faces.application.ResourceDependency;

import org.primefaces.behavior.base.AbstractBehavior;
import org.primefaces.behavior.base.BehaviorAttribute;

/**
 * Client Behavior class for the <code>Javascript</code> behavior.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.2
 */
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js")
public class JavascriptBehavior extends AbstractBehavior {

    public final static String BEHAVIOR_ID = "org.primefaces.extensions.behavior.JavascriptBehavior";
    private static final String DEFAULT_RENDERER = "org.primefaces.extensions.behavior.JavascriptBehaviorRenderer";

    public enum PropertyKeys implements BehaviorAttribute {

        disabled(Boolean.class), execute(String.class);

        private final Class<?> expectedType;

        PropertyKeys(Class<?> expectedType) {
            this.expectedType = expectedType;
        }

        @Override
        public Class<?> getExpectedType() {
            return expectedType;
        }

    }

    @Override
    public String getRendererType() {
        return DEFAULT_RENDERER;
    }

    @Override
    protected BehaviorAttribute[] getAllAttributes() {
        return PropertyKeys.values();
    }

    public final String getExecute() {
        return eval(PropertyKeys.execute, null);
    }

    public void setExecute(final String execute) {
        setLiteral(PropertyKeys.execute, execute);
    }

    public boolean isDisabled() {
        return eval(PropertyKeys.disabled, Boolean.FALSE);
    }

    public void setDisabled(boolean disabled) {
        setLiteral(PropertyKeys.disabled, disabled);
    }

}
