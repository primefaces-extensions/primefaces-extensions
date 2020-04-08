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
package org.primefaces.extensions.component.masterdetail;

import javax.faces.component.UIComponentBase;

/**
 * <code>MasterDetailLevel</code> component.
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since 0.2
 */
public class MasterDetailLevel extends UIComponentBase {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.MasterDetailLevel";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";

    /**
     * Properties that are tracked by state saving.
     *
     * @author Oleg Varaksin / last modified by $Author$
     * @version $Revision$
     */
    protected enum PropertyKeys {

        level, contextVar, levelLabel, levelDisabled;

        private String toString;

        PropertyKeys(String toString) {
            this.toString = toString;
        }

        PropertyKeys() {
        }

        @Override
        public String toString() {
            return toString != null ? toString : super.toString();
        }
    }

    public MasterDetailLevel() {
        setRendererType(null);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public int getLevel() {
        return (Integer) getStateHelper().eval(PropertyKeys.level, null);
    }

    public void setLevel(int level) {
        getStateHelper().put(PropertyKeys.level, level);
    }

    public String getContextVar() {
        return (String) getStateHelper().eval(PropertyKeys.contextVar, null);
    }

    public void setContextVar(String contextVar) {
        getStateHelper().put(PropertyKeys.contextVar, contextVar);
    }

    public String getLevelLabel() {
        return (String) getStateHelper().eval(PropertyKeys.levelLabel, null);
    }

    public void setLevelLabel(String levelLabel) {
        getStateHelper().put(PropertyKeys.levelLabel, levelLabel);
    }

    public boolean isLevelDisabled() {
        return (Boolean) getStateHelper().eval(PropertyKeys.levelDisabled, false);
    }

    public void setLevelDisabled(boolean levelDisabled) {
        getStateHelper().put(PropertyKeys.levelDisabled, levelDisabled);
    }
}
