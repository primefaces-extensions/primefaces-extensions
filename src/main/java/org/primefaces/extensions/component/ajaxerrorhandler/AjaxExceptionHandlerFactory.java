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
package org.primefaces.extensions.component.ajaxerrorhandler;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

/**
 * AjaxExceptionHandlerFactory
 *
 * @author Pavol Slany / last modified by $Author$
 * @version $Revision$
 * @since 0.5
 */
public class AjaxExceptionHandlerFactory extends ExceptionHandlerFactory {

	private ExceptionHandlerFactory wrapped;

	/**
	 * Construct a new full {@link AjaxExceptionHandlerFactory} around the given wrapped factory.
	 * 
	 * @param wrapped The wrapped factory.
	 */
	public AjaxExceptionHandlerFactory(final ExceptionHandlerFactory wrapped) {
		this.wrapped = wrapped;
	}

	/**
	 * Returns a new instance {@link AjaxExceptionHandler}.
	 */
	@Override
	public ExceptionHandler getExceptionHandler() {
		//TODO can this instance be cached?
		return new AjaxExceptionHandler(wrapped.getExceptionHandler());
	}

	/**
	 * Returns the wrapped factory.
	 */
	@Override
	public ExceptionHandlerFactory getWrapped() {
		return wrapped;
	}
}
