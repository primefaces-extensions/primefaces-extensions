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
    @SuppressWarnings("java:S115")
    protected enum PropertyKeys {
        level, contextVar, levelLabel, levelDisabled
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
