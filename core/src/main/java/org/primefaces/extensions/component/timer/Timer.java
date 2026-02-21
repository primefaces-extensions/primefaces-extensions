/*
 * Copyright (c) 2011-2026 PrimeFaces Extensions
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
package org.primefaces.extensions.component.timer;

import java.util.Locale;

import jakarta.el.MethodExpression;
import jakarta.faces.application.ResourceDependency;
import jakarta.faces.component.FacesComponent;
import jakarta.faces.context.FacesContext;

import org.primefaces.cdk.api.FacesComponentInfo;
import org.primefaces.util.LocaleUtils;

/**
 * Timer component
 *
 * @author f.strazzullo
 */
@ResourceDependency(library = "primefaces", name = "components.css")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = "primefaces", name = "moment/moment.js")
@ResourceDependency(library = org.primefaces.extensions.util.Constants.LIBRARY, name = "primefaces-extensions.js")
@ResourceDependency(library = org.primefaces.extensions.util.Constants.LIBRARY, name = "timer/timer.css")
@ResourceDependency(library = org.primefaces.extensions.util.Constants.LIBRARY, name = "timer/timer.js")
@FacesComponent(value = Timer.COMPONENT_TYPE, namespace = Timer.COMPONENT_FAMILY)
@FacesComponentInfo(description = "Timer Component")
public class Timer extends TimerBaseImpl {

    private Locale appropriateLocale;

    public Locale calculateLocale() {
        if (appropriateLocale == null) {
            final FacesContext fc = FacesContext.getCurrentInstance();
            appropriateLocale = LocaleUtils.resolveLocale(fc, getLocale(), getClientId(fc));
        }
        return appropriateLocale;
    }

    @Override
    public boolean isPartialSubmitSet() {
        return getStateHelper().get(PropertyKeys.partialSubmit) != null || getValueExpression(PropertyKeys.partialSubmit.name()) != null;
    }

    @Override
    public boolean isResetValuesSet() {
        return getStateHelper().get(PropertyKeys.resetValues) != null || getValueExpression(PropertyKeys.resetValues.name()) != null;
    }

    @Override
    public boolean isAjaxified() {
        return true;
    }

    @Override
    public void broadcast(final jakarta.faces.event.FacesEvent event) {
        super.broadcast(event); // backward compatibility

        final FacesContext facesContext = getFacesContext();
        final MethodExpression me = getListener();

        if (me != null) {
            me.invoke(facesContext.getELContext(), new Object[] {});
        }
    }

    @Override
    public Object saveState(FacesContext context) {
        // reset component for MyFaces view pooling
        appropriateLocale = null;

        return super.saveState(context);
    }
}
