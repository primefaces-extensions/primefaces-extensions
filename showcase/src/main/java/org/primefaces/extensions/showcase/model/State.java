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

package org.primefaces.extensions.showcase.model;

import java.io.Serializable;

/**
 * State model class.
 *
 * @author  Mauricio Fenoglio / last modified by $Author:$
 * @version $Revision:$
 */
public class State implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String stateDesc;
	private String state;

	public State(String state) {
		this.state = state;
		this.stateDesc = "State class value = " + state;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStateDesc() {
		return stateDesc;
	}

	public void setStateDesc(String stateDesc) {
		this.stateDesc = stateDesc;
	}

	@Override
	public String toString() {
		return stateDesc;
	}
}
