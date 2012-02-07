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
 * $Id$
 */

package org.primefaces.extensions.renderkit.widget;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines if the property should be used as JavaScript Widget option.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.3
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Option {

	/**
	 * Defines the name of the option.
	 * Only required if the default name (name of the property) should not be used.
	 *
	 * @return The name of the option.
	 */
	String name() default "";

	/**
	 * Defines if for this option, HTML should be escaped.
	 *
	 * @return If for this option, HTML should be escaped.
	 */
	boolean escapeHTML() default false;

	/**
	 * Defines if for this option, the text should be escaped for JSON.
	 *
	 * @return If for this option, the text should be escaped for JSON.
	 */
	boolean escapeText() default false;

	/**
	 * Defines if double quotes instead of a normal quote should be used for rendering {@link String}s.
	 *
	 * @return If double quotes should be used for {@link String}s.
	 */
	boolean useDoubleQuotes() default false;
}
