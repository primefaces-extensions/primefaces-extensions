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
package org.primefaces.extensions.component.codemirror;

import java.util.ArrayList;
import java.util.List;

import jakarta.el.MethodExpression;
import jakarta.faces.application.ResourceDependency;
import jakarta.faces.component.FacesComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.FacesEvent;

import org.primefaces.cdk.api.FacesComponentInfo;
import org.primefaces.extensions.event.CompleteEvent;
import org.primefaces.extensions.util.Constants;

/**
 * Component class for the <code>CodeMirror</code> component.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.3
 */
@FacesComponent(value = CodeMirror.COMPONENT_TYPE, namespace = CodeMirror.COMPONENT_FAMILY)
@FacesComponentInfo(description = "CodeMirror to allow syntax highlighting and auto-completion.")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = Constants.LIBRARY, name = "primefaces-extensions.js")
@ResourceDependency(library = Constants.LIBRARY, name = "codemirror/codemirror.js")
@ResourceDependency(library = Constants.LIBRARY, name = "codemirror/codemirror.css")
public class CodeMirror extends CodeMirrorBaseImpl {

    public static final String EVENT_HIGHLIGHT_COMPLETE = "highlightComplete";
    private List<String> suggestions = null;

    @Override
    public void broadcast(final FacesEvent event) {
        super.broadcast(event);

        final FacesContext facesContext = FacesContext.getCurrentInstance();
        final MethodExpression completeMethod = getCompleteMethod();

        if (completeMethod != null && event instanceof CompleteEvent) {
            suggestions = (List<String>) completeMethod.invoke(
                        facesContext.getELContext(),
                        new Object[] {event});

            if (suggestions == null) {
                suggestions = new ArrayList<>();
            }

            facesContext.renderResponse();
        }
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    @Override
    public Object saveState(FacesContext context) {
        // reset component for MyFaces view pooling
        suggestions = null;

        return super.saveState(context);
    }
}
