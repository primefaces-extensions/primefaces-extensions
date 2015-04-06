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
 * $Id: $
 */

package org.primefaces.extensions.util;

import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * Component utils for this project.
 *
 * @author Thomas Andraschko / last modified by $Author: $
 * @version $Revision: $
 * @since 0.6
 */
public class ClassUtils {

	/**
	 * Prevent instantiation.
	 */
	private ClassUtils() {
		// prevent instantiation
	}

	/**
	 * Detect the right {@link ClassLoader}.
	 * The lookup order is determined by:
	 * <ol>
	 * <li>{@link ClassLoader} of the current Thread</li>
	 * <li>{@link ClassLoader} of the given Object 'obj'</li>
	 * <li>{@link ClassLoader} of this very ClassUtils class</li>
	 * </ol>
	 *
	 * @param obj If not <code>null</code> it may get used to detect the {@link ClassLoader}.
	 * @return The {@link ClassLoader} which should get used to create new instances
	 */
	public static ClassLoader getClassLoader(final Object obj) {
		if (System.getSecurityManager() != null) {
			return AccessController.doPrivileged(new GetClassLoaderAction(obj));
		} else {
			return getClassLoaderInternal(obj);
		}
	}

	/**
	 * Detect the right {@link ClassLoader}.
	 * The lookup order is determined by:
	 * <ol>
	 * <li>{@link ClassLoader} of the current Thread</li>
	 * <li>{@link ClassLoader} of the given Object 'obj'</li>
	 * <li>{@link ClassLoader} of this very ClassUtils class</li>
	 * </ol>
	 *
	 * @param clazz If not <code>null</code> it may get used to detect the {@link ClassLoader}.
	 * @return The {@link ClassLoader} which should get used to create new instances
	 */
	public static ClassLoader getClassLoader(final Class<?> clazz) {
		if (System.getSecurityManager() != null) {
			return AccessController.doPrivileged(new GetClassLoaderAction(clazz));
		} else {
			return getClassLoaderInternal(clazz);
		}
	}

	static class GetClassLoaderAction implements PrivilegedAction<ClassLoader> {

		private Object obj;
		private Class<?> clazz;

		private GetClassLoaderAction(final Object obj) {
			this.obj = obj;
		}

		private GetClassLoaderAction(final Class<?> clazz) {
			this.clazz = clazz;
		}

		public ClassLoader run() {
			try {
				if (obj == null) {
					return getClassLoaderInternal(clazz);
				} else {
					return getClassLoaderInternal(obj);
				}
			} catch (Exception e) {
				return null;
			}
		}
	}

	private static ClassLoader getClassLoaderInternal(final Object obj) {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();

		if (loader == null && obj != null) {
			loader = obj.getClass().getClassLoader();
		}

		if (loader == null) {
			loader = ClassUtils.class.getClassLoader();
		}

		return loader;
	}

	private static ClassLoader getClassLoaderInternal(final Class<?> clazz) {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();

		if (loader == null && clazz != null) {
			loader = clazz.getClassLoader();
		}

		if (loader == null) {
			loader = ClassUtils.class.getClassLoader();
		}

		return loader;
	}
}
