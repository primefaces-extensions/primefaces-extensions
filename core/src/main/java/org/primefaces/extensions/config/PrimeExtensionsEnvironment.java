/*
 * Copyright (c) 2011-2021 PrimeFaces Extensions
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
package org.primefaces.extensions.config;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.primefaces.util.LangUtils;

/**
 * @since 8.0.1
 */
public class PrimeExtensionsEnvironment {

    public static final String INSTANCE_KEY = PrimeExtensionsEnvironment.class.getName();

    private final boolean libphonenumberAvailable;

    public PrimeExtensionsEnvironment() {
        libphonenumberAvailable = LangUtils.tryToLoadClassForName("com.google.i18n.phonenumbers.Phonenumber") != null;
    }

    public static PrimeExtensionsEnvironment getCurrentInstance(FacesContext context) {
        if (context == null || context.getExternalContext() == null) {
            return null;
        }

        PrimeExtensionsEnvironment pfeEnv = getFromContext(context);
        if (pfeEnv == null) {
            pfeEnv = new PrimeExtensionsEnvironment();
            setCurrentInstance(pfeEnv, context);
        }

        return pfeEnv;
    }

    private static PrimeExtensionsEnvironment getFromContext(FacesContext context) {
        return (PrimeExtensionsEnvironment) context.getExternalContext().getApplicationMap().get(INSTANCE_KEY);
    }

    public static void setCurrentInstance(PrimeExtensionsEnvironment pfeEnv, FacesContext context) {
        context.getExternalContext().getApplicationMap().put(INSTANCE_KEY, pfeEnv);

        if (context.getExternalContext().getContext() instanceof ServletContext) {
            ((ServletContext) context.getExternalContext().getContext()).setAttribute(INSTANCE_KEY, pfeEnv);
        }
    }

    public boolean isLibphonenumberAvailable() {
        return libphonenumberAvailable;
    }

}
