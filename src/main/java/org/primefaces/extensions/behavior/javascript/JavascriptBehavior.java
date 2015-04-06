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

package org.primefaces.extensions.behavior.javascript;

import java.util.HashMap;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponentBase;
import javax.faces.component.behavior.ClientBehaviorBase;
import javax.faces.context.FacesContext;

/**
 * Client Behavior class for the <code>Javascript</code> behavior.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.2
 */
@ResourceDependencies({ @ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js") })
public class JavascriptBehavior extends ClientBehaviorBase {

	public final static String BEHAVIOR_ID = "org.primefaces.extensions.behavior.JavascriptBehavior";
	private static final String DEFAULT_RENDERER = "org.primefaces.extensions.behavior.JavascriptBehaviorRenderer";

	private Map<String, ValueExpression> bindings;
	private String execute;
	private boolean disabled = false;

	@Override
	public String getRendererType() {
		return DEFAULT_RENDERER;
	}

	public final String getExecute() {
		return (String) eval("execute", execute);
	}

	public void setExecute(final String execute) {
		this.execute = execute;

		clearInitialState();
	}

	public boolean isDisabled() {
		Boolean result = (Boolean) eval("disabled", disabled);

		return ((result != null) ? result : false);
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;

		clearInitialState();
	}

	protected Object eval(final String propertyName, final Object value) {
		if (value != null) {
			return value;
		}

		final ValueExpression expression = getValueExpression(propertyName);
		if (expression != null) {
			return expression.getValue(FacesContext.getCurrentInstance().getELContext());
		}

		return null;
	}

	public ValueExpression getValueExpression(final String name) {
		if (name == null) {
			throw new IllegalArgumentException();
		}

		return ((bindings == null) ? null : bindings.get(name));
	}

	public void setValueExpression(final String name, final ValueExpression expr) {
		if (name == null) {
			throw new IllegalArgumentException();
		}

		if (expr != null) {
			if (expr.isLiteralText()) {
				setLiteralValue(name, expr);
			} else {
				if (bindings == null) {
					bindings = new HashMap<String, ValueExpression>(6, 1.0f);
				}

				bindings.put(name, expr);
			}
		} else {
			if (bindings != null) {
				bindings.remove(name);

				if (bindings.isEmpty()) {
					bindings = null;
				}
			}
		}

		clearInitialState();
	}

	private void setLiteralValue(final String propertyName, final ValueExpression expression) {
		final ELContext context = FacesContext.getCurrentInstance().getELContext();

		try {
			final Object value = expression.getValue(context);

			if ("disabled".equals(propertyName)) {
				disabled = (Boolean) value;
			} else if ("execute".equals(propertyName)) {
				execute = (String) value;
			}
		} catch (ELException eLException) {
			throw new FacesException(eLException);
		}
	}

	@Override
	public Object saveState(final FacesContext context) {
		Object[] values;

		final Object superState = super.saveState(context);

		if (initialStateMarked()) {
			if (superState == null) {
				values = null;
			} else {
				values = new Object[] { superState };
			}
		} else {
			values = new Object[13];

			values[0] = superState;
			values[1] = execute;
			values[2] = disabled;
			values[3] = saveBindings(context, bindings);
		}

		return values;
	}

	@Override
	public void restoreState(final FacesContext context, final Object state) {
		if (state != null) {
			final Object[] values = (Object[]) state;
			super.restoreState(context, values[0]);

			if (values.length != 1) {
				execute = (String) values[1];
				disabled = (Boolean) values[2];
				bindings = restoreBindings(context, values[3]);

				clearInitialState();
			}
		}
	}

	private Object saveBindings(final FacesContext context, final Map<String, ValueExpression> bindings) {
		if (bindings == null) {
			return null;
		}

		final Object values[] = new Object[2];
		values[0] = bindings.keySet().toArray(new String[bindings.size()]);

		final Object[] bindingValues = bindings.values().toArray();
		for (int i = 0; i < bindingValues.length; i++) {
			bindingValues[i] = UIComponentBase.saveAttachedState(context, bindingValues[i]);
		}

		values[1] = bindingValues;

		return values;
	}

	private Map<String, ValueExpression> restoreBindings(final FacesContext context, final Object state) {
		if (state == null) {
			return null;
		}

		final Object values[] = (Object[]) state;
		final String names[] = (String[]) values[0];
		final Object states[] = (Object[]) values[1];
		final Map<String, ValueExpression> bindings = new HashMap<String, ValueExpression>(names.length);

		for (int i = 0; i < names.length; i++) {
			bindings.put(names[i], (ValueExpression) UIComponentBase.restoreAttachedState(context, states[i]));
		}

		return bindings;
	}
}
