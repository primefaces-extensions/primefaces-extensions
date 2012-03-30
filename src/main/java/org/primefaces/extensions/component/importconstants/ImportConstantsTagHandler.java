/*
 * Copyright 2011 PrimeFaces Extensions.
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

package org.primefaces.extensions.component.importconstants;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagHandler;

/**
 * {@link TagHandler} for the <code>ImportConstants</code> component.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.5
 */
public class ImportConstantsTagHandler extends TagHandler {

	private static final Map<Class<?>, Map<String, Object>> CACHE = new Hashtable<Class<?>, Map<String, Object>>();

	private final TagAttribute classNameTagAttribute;
	private final TagAttribute varTagAttribute;

	/**
	 * Default constructor.
	 *
	 * @param config The {@link TagConfig}.
	 */
	public ImportConstantsTagHandler(final TagConfig config) {
		super(config);

		classNameTagAttribute = super.getRequiredAttribute("className");
		varTagAttribute = super.getAttribute("var");
	}

	/**
	 * {@inheritDoc}
	 */
	public void apply(final FaceletContext ctx, final UIComponent parent) throws IOException {
		final Class<?> clazz = getClassFromAttribute(classNameTagAttribute, ctx);

		// Get constants
		Map<String, Object> constants;

		if (CACHE.containsKey(clazz)) {
			constants = CACHE.get(clazz);
		} else {
			constants = loadConstants(clazz);
			CACHE.put(clazz, constants);
		}

		// Create alias/var expression
		String var;
		if (varTagAttribute == null) {
			var = clazz.getSimpleName(); // fall back to class name
		} else {
			var = varTagAttribute.getValue(ctx);
		}

		if (var.charAt(0) != '#') {
			final StringBuilder varBuilder = new StringBuilder();
			varBuilder.append("#{").append(var).append("}");

			var = varBuilder.toString();
		}

		// Assign constants to alias/var expression
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		final ELContext elContext = facesContext.getELContext();
		final ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();

		final ValueExpression aliasValueExpression = expressionFactory.createValueExpression(elContext, var, Map.class);
		aliasValueExpression.setValue(FacesContext.getCurrentInstance().getELContext(), constants);
	}

	/**
	 * Gets the {@link Class} from the {@link TagAttribute}.
	 *
	 * @param attribute The {@link TagAttribute}.
	 * @param ctx The {@link FaceletContext}.
	 * @return The {@link Class}.
	 */
	protected Class<?> getClassFromAttribute(final TagAttribute attribute, final FaceletContext ctx) {
		final String className = attribute.getValue(ctx);

		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new FacesException("Class " + className + " not found.", e);
		}
	}

	/**
	 * Gets all constants of the given {@link Class}.
	 *
	 * @param clazz The class which includes the constants.
	 * @return A {@link Map} with the found constants.
	 */
	protected Map<String, Object> loadConstants(final Class<?> clazz) {
		final Map<String, Object> constants = new HashMap<String, Object>();

		// Go through all the fields, and put static ones in a map.
		final Field[] fields = clazz.getDeclaredFields();

		for (int i = 0; i < fields.length; i++) {
			// Check to see if this is public static final. If not, it's not a constant.
			final int modifiers = fields[i].getModifiers();
			if (!Modifier.isFinal(modifiers) || !Modifier.isStatic(modifiers) || !Modifier.isPublic(modifiers)) {
				continue;
			}

			try {
				final Object value = fields[i].get(null); // null for static fields.
				constants.put(fields[i].getName(), value);
			} catch (Exception e) {
				throw new FacesException("Could not get value of " + fields[i].getName() + " in " + clazz.getName() + ".", e);
			}
		}

		return constants;
	}
}
