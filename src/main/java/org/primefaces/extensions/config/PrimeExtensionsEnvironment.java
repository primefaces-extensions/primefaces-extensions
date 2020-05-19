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
package org.primefaces.extensions.config;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.primefaces.util.LangUtils;

/**
 * @since 8.0.1
 */
public class PrimeExtensionsEnvironment {

    public static final String INSTANCE_KEY = PrimeExtensionsEnvironment.class.getName();

    private final boolean commonmarkAvailable;

    private final boolean libphonenumberAvailable;

    public PrimeExtensionsEnvironment() {
        commonmarkAvailable = LangUtils.tryToLoadClassForName("org.commonmark.parser.Parser") != null;
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

    public boolean isCommonmarkAvailable() {
        return commonmarkAvailable;
    }

    public boolean isLibphonenumberAvailable() {
        return libphonenumberAvailable;
    }

}
