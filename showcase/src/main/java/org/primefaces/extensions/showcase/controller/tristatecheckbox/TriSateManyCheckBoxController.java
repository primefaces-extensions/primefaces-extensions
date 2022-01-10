/*
 * Copyright (c) 2011-2022 PrimeFaces Extensions
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
package org.primefaces.extensions.showcase.controller.tristatecheckbox;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.extensions.showcase.model.State;

/**
 * TriSateManyCheckboxController
 *
 * @author Mauricio Fenoglio / last modified by $Author$
 * @version $Revision$
 * @since 0.3
 */
@Named
@ViewScoped
public class TriSateManyCheckBoxController implements Serializable {

    private static final long serialVersionUID = 20110302L;

    private Map<String, String> selectedOptionsTriStateBasic;
    private Map<String, String> selectedOptionsTriStateAjax;
    private Map<String, State> selectedOptionsTriStateConverted;
    private Map<String, State> selectedOptionsTriStateConvertedInline;
    private Map<String, String> basicOptions;

    public TriSateManyCheckBoxController() {
        basicOptions = new HashMap<String, String>();
        basicOptions.put("Label for Dog", "Dog");
        basicOptions.put("Label for Cat", "Cat");
        basicOptions.put("Label for Fish", "Fish");

        // default will created with state=0
        selectedOptionsTriStateBasic = new HashMap<String, String>();
        selectedOptionsTriStateBasic.put("Cat", "1");

        selectedOptionsTriStateAjax = new HashMap<String, String>();
        selectedOptionsTriStateAjax.put("Tamara", "1");
        selectedOptionsTriStateAjax.put("Mauricio", "1");

        selectedOptionsTriStateConverted = new HashMap<String, State>();
        selectedOptionsTriStateConverted.put("Dog", new State("One"));
        selectedOptionsTriStateConverted.put("Cat", new State("One"));
        selectedOptionsTriStateConverted.put("Fish", new State("One"));

        selectedOptionsTriStateConvertedInline = new HashMap<String, State>();
        selectedOptionsTriStateConvertedInline.put("Dog", new State("One"));
        selectedOptionsTriStateConvertedInline.put("Cat", new State("Two"));
        selectedOptionsTriStateConvertedInline.put("Fish", new State("Three"));
    }

    public Map<String, String> getSelectedOptionsTriStateAjax() {
        return selectedOptionsTriStateAjax;
    }

    public void setSelectedOptionsTriStateAjax(final Map<String, String> selectedOptionsTriStateAjax) {
        this.selectedOptionsTriStateAjax = selectedOptionsTriStateAjax;
    }

    public Map<String, String> getSelectedOptionsTriStateBasic() {
        return selectedOptionsTriStateBasic;
    }

    public void setSelectedOptionsTriStateBasic(final Map<String, String> selectedOptionsTriStateBasic) {
        this.selectedOptionsTriStateBasic = selectedOptionsTriStateBasic;
    }

    public void addMessage() {
        String message = "";
        for (final String key : selectedOptionsTriStateAjax.keySet()) {
            message += key + "=" + selectedOptionsTriStateAjax.get(key) + "  ";
        }

        final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "State has been changed", message.trim());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public Map<String, String> getBasicOptions() {
        return basicOptions;
    }

    public void setBasicOptions(final Map<String, String> basicOptions) {
        this.basicOptions = basicOptions;
    }

    public Map<String, State> getSelectedOptionsTriStateConverted() {
        return selectedOptionsTriStateConverted;
    }

    public void setSelectedOptionsTriStateConverted(final Map<String, State> selectedOptionsTriStateConverted) {
        this.selectedOptionsTriStateConverted = selectedOptionsTriStateConverted;
    }

    public Map<String, State> getSelectedOptionsTriStateConvertedInline() {
        return selectedOptionsTriStateConvertedInline;
    }

    public void setSelectedOptionsTriStateConvertedInline(
                final Map<String, State> selectedOptionsTriStateConvertedInline) {
        this.selectedOptionsTriStateConvertedInline = selectedOptionsTriStateConvertedInline;
    }
}
