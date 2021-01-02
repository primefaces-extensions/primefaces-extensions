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
        else if ("true".equalsIgnoreCase(value)) {
            ignore = true;
        }
        else if ("yes".equalsIgnoreCase(value)) {
            ignore = true;
        }
        else {
            ignore = false;
        }
    }
}
