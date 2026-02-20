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
package org.primefaces.extensions.component.cookiepolicy;

import jakarta.faces.component.FacesComponent;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import org.primefaces.cdk.api.FacesComponentInfo;
import org.primefaces.util.LangUtils;

/**
 * <code>CookiePolicy</code> component.
 *
 * @author Melloware mellowaredev@gmail.com / Frank Cornelis
 * @since 11.0.3
 */
@FacesComponent(value = CookiePolicy.COMPONENT_TYPE, namespace = CookiePolicy.COMPONENT_FAMILY)
@FacesComponentInfo(description = "CookiePolicy conditionally renders its children based on cookie consent.")
public class CookiePolicy extends CookiePolicyBaseImpl {

    public static final String COOKIE_POLICY_COOKIE_NAME = "CookiePolicy";
    public static final String COOKIE_POLICY_REQUEST_ATTRIBUTE = CookiePolicy.class.getName() + ".CookiePolicy";

    public boolean hasCookiePolicyCookie(final FacesContext context) {
        final ExternalContext externalContext = context.getExternalContext();
        final HttpServletRequest httpServletRequest = (HttpServletRequest) externalContext.getRequest();
        if (null != httpServletRequest.getAttribute(COOKIE_POLICY_REQUEST_ATTRIBUTE)) {
            return true;
        }
        return externalContext.getRequestCookieMap().containsKey(COOKIE_POLICY_COOKIE_NAME);
    }

    /**
     * Checks whether the given cookie policy has been accepted.
     *
     * @param cookiePolicy the cookie policy to be verified.
     * @return <code>true</code> if the cookie policy has been accepted, otherwise <code>false</code>.
     */
    public static boolean hasCookiePolicy(final String cookiePolicy) {
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        final ExternalContext externalContext = facesContext.getExternalContext();

        final HttpServletRequest httpServletRequest = (HttpServletRequest) externalContext.getRequest();
        final String requestCookiePolicy = (String) httpServletRequest.getAttribute(COOKIE_POLICY_REQUEST_ATTRIBUTE);
        if (null != requestCookiePolicy) {
            return requestCookiePolicy.contains(cookiePolicy);
        }

        final Cookie cookie = (Cookie) externalContext.getRequestCookieMap().get(COOKIE_POLICY_COOKIE_NAME);
        if (null == cookie) {
            return false;
        }
        final String policy = cookie.getValue();
        if (LangUtils.isBlank(policy)) {
            return false;
        }
        return policy.contains(cookiePolicy);
    }
}
