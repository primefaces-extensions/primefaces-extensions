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
package org.primefaces.extensions.component.cookiepolicy;

import java.util.HashMap;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.component.StateHolder;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.util.LangUtils;
import org.primefaces.util.ResourceUtils;

/**
 * <code>CookiePolicy</code> action listener for saving the cookie policy choice.
 *
 * @author Melloware mellowaredev@gmail.com / Frank Cornelis
 * @since 11.0.3
 */
public class CookiePolicySaveActionListener implements ActionListener, StateHolder {

    private ValueExpression policyValueExpression;

    private ValueExpression retentionValueExpression;

    private boolean _transient;

    public CookiePolicySaveActionListener() {
        super();
    }

    public CookiePolicySaveActionListener(final ValueExpression policyValueExpression,
                final ValueExpression retentionValueExpression) {
        this();
        this.policyValueExpression = policyValueExpression;
        this.retentionValueExpression = retentionValueExpression;
    }

    @Override
    public Object saveState(final FacesContext context) {
        if (context == null) {
            throw new NullPointerException();
        }
        return new Object[] {this.policyValueExpression, this.retentionValueExpression};
    }

    @Override
    public void restoreState(final FacesContext context, final Object state) {
        if (context == null) {
            throw new NullPointerException();
        }
        if (state == null) {
            return;
        }
        final Object[] stateObjects = (Object[]) state;
        if (stateObjects.length == 0) {
            return;
        }
        this.policyValueExpression = (ValueExpression) stateObjects[0];
        this.retentionValueExpression = (ValueExpression) stateObjects[1];
    }

    @Override
    public boolean isTransient() {
        return this._transient;
    }

    @Override
    public void setTransient(final boolean newTransientValue) {
        this._transient = newTransientValue;
    }

    @Override
    public void processAction(final ActionEvent actionEvent) throws AbortProcessingException {
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        final ELContext elContext = facesContext.getELContext();
        final ExternalContext externalContext = facesContext.getExternalContext();

        final String policy = (String) this.policyValueExpression.getValue(elContext);

        final Integer retention;
        if (null != this.retentionValueExpression) {
            retention = (Integer) this.retentionValueExpression.getValue(elContext);
        }
        else {
            retention = 10; // 10 seconds, easy for testing
        }

        final Map<String, Object> cookieOptions = new HashMap<>(6);
        final String path = externalContext.getRequestContextPath();
        // Always add cookies to context root; see #3108
        cookieOptions.put("path", LangUtils.isBlank(path) ? "/" : path);
        cookieOptions.put("maxAge", retention);
        cookieOptions.put("httpOnly", Boolean.TRUE);
        ResourceUtils.addResponseCookie(facesContext, CookiePolicy.COOKIE_POLICY_COOKIE_NAME, policy, cookieOptions);

        final HttpServletRequest httpServletRequest = (HttpServletRequest) externalContext.getRequest();
        httpServletRequest.setAttribute(CookiePolicy.COOKIE_POLICY_REQUEST_ATTRIBUTE, policy);
    }
}
