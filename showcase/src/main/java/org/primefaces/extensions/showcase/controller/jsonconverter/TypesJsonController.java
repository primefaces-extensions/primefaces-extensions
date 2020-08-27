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
 */

package org.primefaces.extensions.showcase.controller.jsonconverter;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 * TypesJsonController
 *
 * @author Oleg Varaksin / last modified by $Author: $
 * @version $Revision: 1.0 $
 */
@Named
@SessionScoped
public class TypesJsonController implements Serializable {

	private static final long serialVersionUID = 1L;

	public String getTypeGenericList() {
		return "java.util.Collection<java.lang.Integer>";
	}

	public String getTypeGenericMap() {
		return "java.util.Map<java.lang.String, org.apache.commons.lang3.tuple.ImmutablePair<java.lang.Integer, java.util.Date>>";
	}

	public String getTypeGenericSimple() {
		return "org.primefaces.extensions.showcase.model.jsonconverter.FooGeneric<java.lang.String, java.lang.Integer>";
	}

	public String getTypeGenericComplex() {
		return "org.primefaces.extensions.showcase.model.jsonconverter.FooGeneric"
				+ "<int[], org.primefaces.extensions.showcase.model.jsonconverter.FooGeneric"
				+ "<org.primefaces.extensions.showcase.model.jsonconverter.FooNonGeneric, java.lang.Boolean>>";
	}
}
