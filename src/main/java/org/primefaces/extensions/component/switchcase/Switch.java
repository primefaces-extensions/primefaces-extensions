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
package org.primefaces.extensions.component.switchcase;

import java.io.IOException;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;

/**
 * Component class for the <code>Switch</code> component.
 *
 * @author Michael Gmeiner / last modified by $Author: $
 * @version $Revision: $
 * @since 0.6
 */
public class Switch extends UIComponentBase {

	public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Switch";
	public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
	
	/**
	 * Properties that are tracked by state saving.
	 *
	 * @author Michael Gmeiner / last modified by $Author: $
	 * @version $Revision: $
	 */
	protected enum PropertyKeys {
		
		value;

		private String toString;

		PropertyKeys(final String toString) {
			this.toString = toString;
		}

		PropertyKeys() {
		}

		@Override
		public String toString() {
			return ((this.toString != null) ? this.toString : super.toString());
		}
	}
	
	public Switch() {
        setRendererType(null);
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}
	
	public Object getValue() {
		return getStateHelper().eval(PropertyKeys.value, null);
	}

	public void setValue(final Object value) {
		getStateHelper().put(PropertyKeys.value, value);
	}

	private void evaluate() {
		DefaultCase caseToRender = null;
		DefaultCase defaultCase = null;

		for (UIComponent child : this.getChildren()) {
			child.setRendered(false);

			if (child instanceof Case) {
				final Case caseComponent = (Case) child;

				if ((caseComponent.getValue() == null && this.getValue() != null)  
						|| (this.getValue() == null && caseComponent.getValue() != null)) { 
					continue;
				}
				if ((caseComponent.getValue() == null && this.getValue() == null) 
						|| caseComponent.getValue().equals(this.getValue())) { 

					caseToRender = caseComponent;
					// mustn't break, because need to set rendered=false to other cases (e.g. for visitTree correctness)
				}
			} else if (child instanceof DefaultCase) {
				defaultCase = (DefaultCase) child;
			} else {
				throw new FacesException("Switch only accepts case or defaultCase as children.");
			}
		}

		if (caseToRender == null) {
			caseToRender = defaultCase;
		}

		if (caseToRender != null) {
			caseToRender.setRendered(true);
		}
	}

	@Override
	public void processDecodes(FacesContext context) {
		evaluate();
		super.processDecodes(context);
	}

	@Override
	public void processValidators(FacesContext context) {
		evaluate();
		super.processValidators(context);
	}

	@Override
	public void processUpdates(FacesContext context) {
		evaluate();
		super.processUpdates(context);
	}

	@Override
	public void broadcast(FacesEvent event) throws AbortProcessingException {
		evaluate();
		super.broadcast(event);
	}

	@Override
	public boolean visitTree(VisitContext context, VisitCallback callback) {
		// mustn't evaluate cases during Restore View
		if (context.getFacesContext().getCurrentPhaseId() != PhaseId.RESTORE_VIEW) {
			evaluate();
		}
		return super.visitTree(context, callback);
	}

	@Override
	public void encodeBegin(FacesContext context) throws IOException {
		evaluate();
		super.encodeBegin(context);
	}

}
