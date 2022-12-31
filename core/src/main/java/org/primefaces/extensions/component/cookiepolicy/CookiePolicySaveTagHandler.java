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
package org.primefaces.extensions.component.cookiepolicy;

import java.io.IOException;

import javax.el.ValueExpression;
import javax.faces.component.ActionSource;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagException;
import javax.faces.view.facelets.TagHandler;

/**
 * <code>CookiePolicy</code> tag handler for saving the user's choice.
 *
 * @author Melloware mellowaredev@gmail.com / Frank Cornelis
 * @since 11.0.3
 */
public class CookiePolicySaveTagHandler extends TagHandler {

    private final TagAttribute policyAttribute;
    private final TagAttribute retentionAttribute;

    public CookiePolicySaveTagHandler(final TagConfig config) {
        super(config);
        this.policyAttribute = getRequiredAttribute("policy");
        this.retentionAttribute = getAttribute("retention");
    }

    @Override
    public void apply(final FaceletContext context, final UIComponent parent) throws IOException {
        if (parent == null) {
            return;
        }
        if (!ComponentHandler.isNew(parent)) {
            return;
        }
        if (!(parent instanceof ActionSource)) {
            throw new TagException(this.tag, "Can only be attached to ActionSource components.");
        }

        final ValueExpression policyValueExpression = this.policyAttribute.getValueExpression(context, String.class);

        final ValueExpression retentionValueExpression;
        if (null != this.retentionAttribute) {
            retentionValueExpression = this.retentionAttribute.getValueExpression(context, Integer.class);
        }
        else {
            retentionValueExpression = null;
        }

        final ActionSource actionSource = (ActionSource) parent;
        actionSource.addActionListener(
                    new CookiePolicySaveActionListener(policyValueExpression, retentionValueExpression));
    }
}
