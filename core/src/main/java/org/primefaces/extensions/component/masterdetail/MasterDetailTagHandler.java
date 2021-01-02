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
package org.primefaces.extensions.component.masterdetail;

import javax.faces.component.UIComponent;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.MetaRuleset;

import org.primefaces.component.breadcrumb.BreadCrumb;
import org.primefaces.facelets.MethodRule;
import org.primefaces.model.menu.DefaultMenuModel;

/**
 * {@link ComponentHandler} for the <code>MasterDetail</code>.
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since 0.2
 */
public class MasterDetailTagHandler extends ComponentHandler {

    private static final MethodRule SELECT_LEVEL_LISTENER = new MethodRule("selectLevelListener", int.class, new Class[] {SelectLevelEvent.class});

    public MasterDetailTagHandler(final ComponentConfig config) {
        super(config);
    }

    @Override
    protected MetaRuleset createMetaRuleset(final Class type) {
        final MetaRuleset metaRuleset = super.createMetaRuleset(type);
        metaRuleset.addRule(SELECT_LEVEL_LISTENER);

        return metaRuleset;
    }

    @Override
    public void onComponentPopulated(final FaceletContext ctx, final UIComponent c, final UIComponent parent) {
        if (!isNew(parent)) {
            return;
        }

        final MasterDetail masterDetail = (MasterDetail) c;

        if (!isBreadcrumbAvailable(masterDetail)) {
            // create BreadCrumb programmatically
            final BreadCrumb breadcrumb = (BreadCrumb) ctx.getFacesContext().getApplication().createComponent(BreadCrumb.COMPONENT_TYPE);
            breadcrumb.setId(masterDetail.getId() + MasterDetail.BREADCRUMB_ID_PREFIX);

            // set empty model
            breadcrumb.setModel(new DefaultMenuModel());

            // add it to the MasterDetail
            masterDetail.getChildren().add(breadcrumb);
        }
    }

    private static boolean isBreadcrumbAvailable(final MasterDetail masterDetail) {
        for (final UIComponent child : masterDetail.getChildren()) {
            if (child instanceof BreadCrumb) {
                return true;
            }
        }

        return false;
    }
}
