/*
* @author  Oleg Varaksin (ovaraksin@googlemail.com)
* $$Id$$
*/

package com.innflow.ebtam.webapp.jsf.masterdetail;

import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.Constants;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class MasterDetailRenderer extends CoreRenderer
{
    @Override
    public void encodeEnd(final FacesContext fc, UIComponent component) throws IOException {
        MasterDetail masterDetail = (MasterDetail) component;

        if (masterDetail.isSelectDetailRequest(fc)) {

            // get AjaxSource caused this ajax request
            String source = fc.getExternalContext().getRequestParameterMap().get(Constants.PARTIAL_SOURCE_PARAM);
            MasterDetailLevel mdl = masterDetail.getDetailLevelToProcess(fc);
            
            // get context value from SelectDetailLevel as value expression
            ValueExpression contextValueVE = (ValueExpression) mdl.getAttributes().get("contextValueVE_" + source);
            if (contextValueVE != null) {
                // resolve context value and store in the corresponding MasterDetailLevel component
                mdl.getAttributes().put("contextValue_" + source, contextValueVE.getValue(fc.getELContext()));
            } else {
                mdl.getAttributes().remove("contextValue_" + source);
            }
        } else {
            // TODO
        }
    }

    @Override
    public void encodeChildren(final FacesContext fc, UIComponent component) throws IOException {
        // rendering happens on encodeEnd
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}

