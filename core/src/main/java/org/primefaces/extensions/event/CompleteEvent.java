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
package org.primefaces.extensions.event;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

public class CompleteEvent extends FacesEvent {

    private static final long serialVersionUID = 1L;

    private final String token;
    private final String context;
    private final int line;
    private final int column;

    public CompleteEvent(final UIComponent component, final String token, final String context,
                final int line, final int column) {
        super(component);
        this.token = token;
        this.context = context;
        this.line = line;
        this.column = column;
    }

    @Override
    public boolean isAppropriateListener(final FacesListener facesListener) {
        return false;
    }

    @Override
    public void processListener(final FacesListener facesListener) {
        /** NOOP */
    }

    public String getToken() {
        return token;
    }

    public String getContext() {
        return context;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }
}
