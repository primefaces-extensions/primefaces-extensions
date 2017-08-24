/**
 * Copyright 2011-2017 PrimeFaces Extensions
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
package org.primefaces.extensions.event;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

@SuppressWarnings("serial")
public class CompleteEvent extends FacesEvent {

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
