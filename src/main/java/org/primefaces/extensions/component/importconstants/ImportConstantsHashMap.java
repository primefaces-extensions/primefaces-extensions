/*
 * Copyright 2011-2012 PrimeFaces Extensions.
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
 * $Id: $
 */

package org.primefaces.extensions.component.importconstants;

import java.util.HashMap;

import javax.faces.FacesException;

/**
 * Custom {@link HashMap} which throws an {@link FacesException} if the key/constant does not exist.
 * 
 * @author Thomas Andraschko / last modified by $Author: $
 * @version $Revision: $
 * @since 0.6
 *
 * @param <TK> The key type.
 * @param <TV> The value type.
 */
@SuppressWarnings("serial")
public class ImportConstantsHashMap<TK, TV> extends HashMap<TK, TV> {

	private Class<?> clazz;

	/**
	 * Default constructor.
	 *
	 * @param clazz The class.
	 */
	public ImportConstantsHashMap(final Class<?> clazz) {
		this.clazz = clazz;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TV get(final Object key) {
		if (!containsKey(key)) {
			throw new FacesException("Class " + clazz.getName() + " does not contain the constant " + key);
		}

		return super.get(key);
	}
}
