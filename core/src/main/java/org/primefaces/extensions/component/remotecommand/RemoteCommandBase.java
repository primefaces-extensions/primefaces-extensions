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
package org.primefaces.extensions.component.remotecommand;

import jakarta.el.MethodExpression;
import jakarta.faces.component.UICommand;
import jakarta.faces.event.ActionListener;

import org.primefaces.cdk.api.FacesComponentBase;
import org.primefaces.cdk.api.Property;
import org.primefaces.component.api.AjaxSource;

/**
 * CDK base for the RemoteCommand component.
 *
 * @since 0.2
 */
@FacesComponentBase
public abstract class RemoteCommandBase extends UICommand implements AjaxSource {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.RemoteCommand";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.RemoteCommandRenderer";

    public RemoteCommandBase() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Property(description = "A method expression or a string outcome to process when command is executed.", skipAccessors = true)
    public MethodExpression getAction() {
        return super.getActionExpression();
    }

    @Property(description = "An action listener to process when command is executed.", skipAccessors = true)
    public ActionListener getActionListener() {
        return super.getActionListeners()[0];
    }

    @Property(description = "Name of the JavaScript function to create.")
    public abstract String getName();

    @Property(description = "Whether to automatically run the JavaScript function when the page is loaded.", defaultValue = "false")
    public abstract boolean isAutoRun();

    @Property(description = "A method expression to invoke by polling.")
    public abstract MethodExpression getActionListenerMethodExpression();

}
