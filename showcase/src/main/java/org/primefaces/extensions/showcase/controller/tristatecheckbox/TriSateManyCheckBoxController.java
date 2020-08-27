/*
 * Copyright 2011-2015 PrimeFaces Extensions
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
 *
 * $Id$
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
