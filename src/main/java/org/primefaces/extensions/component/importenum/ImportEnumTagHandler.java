/*
 * Copyright 2011-2013 PrimeFaces Extensions.
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

package org.primefaces.extensions.component.importenum;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

import org.primefaces.extensions.util.ClassUtils;

/**
 * {@link TagHandler} for the <code>ImportEnum</code> component.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.7
 */
public class ImportEnumTagHandler extends TagHandler {

	private static final String DEFAULT_ALL_SUFFIX = "ALL_VALUES";

	private static final Map<ClassLoader, Map<Class<?>, Map<String, Object>>> CACHE = new ConcurrentHashMap<ClassLoader, Map<Class<?>, Map<String, Object>>>();

	private final TagAttribute typeTagAttribute;
	private final TagAttribute varTagAttribute;
	private final TagAttribute allSuffixTagAttribute;

	/**
	 * Default constructor.
	 * 
	 * @param config The {@link TagConfig}.
	 */
	public ImportEnumTagHandler(final TagConfig config) {
		super(config);

		typeTagAttribute = super.getRequiredAttribute("type");
		varTagAttribute = super.getAttribute("var");
		allSuffixTagAttribute = super.getAttribute("allSuffix");
	}

	/**
	 * {@inheritDoc}
	 */
	public void apply(final FaceletContext ctx, final UIComponent parent) throws IOException {
		final Class<?> clazz = getClassFromAttribute(typeTagAttribute, ctx);
		final Map<String, Object> enumValues = getEnumValues(clazz,
				allSuffixTagAttribute == null ? DEFAULT_ALL_SUFFIX : allSuffixTagAttribute.getValue(ctx));

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

		// Assign enum values to alias/var expression
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		final ELContext elContext = facesContext.getELContext();
		final ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();

		final ValueExpression aliasValueExpression = expressionFactory.createValueExpression(elContext, var, Map.class);
		aliasValueExpression.setValue(elContext, enumValues);
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
	 * Get all enum values of the given {@link Class}.
	 * 
	 * @param clazz The enum class.
	 * @return A {@link Map} with the enum values.
	 */
	protected Map<String, Object> getEnumValues(final Class<?> clazz, final String allPrefix) {
		if (clazz.isEnum()) {
			final ClassLoader classLoader = ClassUtils.getClassLoader(clazz);

			if (!CACHE.containsKey(classLoader)) {
				CACHE.put(classLoader, new ConcurrentHashMap<Class<?>, Map<String, Object>>());
			}

			final Map<Class<?>, Map<String, Object>> cache = CACHE.get(classLoader);

			final Map<String, Object> enums;

			if (cache.containsKey(clazz)) {
				enums = cache.get(clazz);
			} else {
				enums = new EnumHashMap<String, Object>(clazz);

				for (Object value : clazz.getEnumConstants()) {
					Enum<?> currentEnum = (Enum<?>) value;
					enums.put(currentEnum.name(), currentEnum);
				}

				enums.put(allPrefix, clazz.getEnumConstants());

				cache.put(clazz, enums);
			}

			return enums;
		} else {
			throw new FacesException("Class '" + clazz + "' is not an enum.");
		}
	}
}
