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
package org.primefaces.extensions.showcase.webapp;

import java.io.IOException;

import javax.servlet.*;

/**
 * Filter that sets the character encoding to be used in parsing the incoming request, either unconditionally or only if the client did not specify a character
 * encoding.
 */
public class CharacterEncodingFilter implements Filter {
    /**
     * The default character encoding to set for requests that pass through this filter.
     */
    protected String encoding = null;

    /**
     * The filter configuration object we are associated with. If this value is null, this filter instance is not currently configured.
     */
    protected FilterConfig filterConfig = null;

    /**
     * Should a character encoding specified by the client be ignored?
     */
    protected boolean ignore = true;

    @Override
    public void destroy() {
        encoding = null;
        filterConfig = null;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                throws IOException, ServletException {
        // Conditionally select and set the character encoding to be used
        if (ignore || (encoding != null && request.getCharacterEncoding() == null)) {
            request.setCharacterEncoding(encoding);
        }

        // Pass control on to the next filter
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        encoding = filterConfig.getInitParameter("encoding");

        String value = filterConfig.getInitParameter("ignore");
        if (value == null) {
            ignore = true;
        }
        else if (value.equalsIgnoreCase("true")) {
            ignore = true;
        }
        else if (value.equalsIgnoreCase("yes")) {
            ignore = true;
        }
        else {
            ignore = false;
        }
    }
}
