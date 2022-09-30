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
package org.primefaces.extensions.component.session;

import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;

import org.primefaces.component.api.Widget;

@FacesComponent(Session.COMPONENT_TYPE)
@ResourceDependency(library = "javax.faces", name = "jsf.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = "primefaces-extensions", name = "session/session.js")
public class Session extends UIComponentBase implements Widget {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Session";

    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    enum PropertyKeys {
        onexpire, onexpired, reactionPeriod,
    }

    public String getOnexpire() {
        return (String) getStateHelper().eval(PropertyKeys.onexpire);
    }

    public void setOnexpire(String onexpire) {
        getStateHelper().put(PropertyKeys.onexpire, onexpire);
    }

    public String getOnexpired() {
        return (String) getStateHelper().eval(PropertyKeys.onexpired);
    }

    public void setOnexpired(String onexpired) {
        getStateHelper().put(PropertyKeys.onexpired, onexpired);
    }

    public Integer getReactionPeriod() {
        return (Integer) getStateHelper().eval(PropertyKeys.reactionPeriod);
    }

    public void setReactionPeriod(Integer reactionPeriod) {
        getStateHelper().put(PropertyKeys.reactionPeriod, reactionPeriod);
    }
}
