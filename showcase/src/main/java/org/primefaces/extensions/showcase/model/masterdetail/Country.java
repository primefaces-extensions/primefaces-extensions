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

package org.primefaces.extensions.showcase.model.masterdetail;

import java.io.Serializable;
import java.util.List;

/**
 * Country model class.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 */
public class Country implements Serializable {

	private static final long serialVersionUID = 20111120L;

	private String name;
	private String code;
	private String sport;
	private List<League> leagues;

	public Country(String name, String code, String sport, List<League> leagues) {
		this.name = name;
		this.code = code;
		this.sport = sport;
		this.leagues = leagues;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

	public String getSport() {
		return sport;
	}

	public List<League> getLeagues() {
		return leagues;
	}
}
