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

package org.primefaces.extensions.application;

import javax.faces.application.FacesMessage;

/**
 * Custom {@link FacesMessage} which allows a {@link Target} as parameter.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.2
 */
public class TargetableFacesMessage extends FacesMessage {

    private static final long serialVersionUID = 20111109L;
    
	/**
	 * Targets for the {@link TargetableFacesMessage}.
	 *
	 * @author Thomas Andraschko / last modified by $Author$
	 * @version $Revision$
	 * @since 0.2
	 */
	public enum Target {
		MESSAGE,
		MESSAGES,
		GROWL,
		ALL;
	}

	private Target target;

	public TargetableFacesMessage(final Target target) {
		super();
		setTarget(target);
	}

	public TargetableFacesMessage(final String summary, final Target target) {
		super(summary);
		setTarget(target);
	}

	public TargetableFacesMessage(final String summary, final String detail, final Target target) {
		super(summary, detail);
		setTarget(target);
	}

	public TargetableFacesMessage(final Severity severity, final String summary, final String detail, final Target target) {

		super(severity, summary, detail);
		setTarget(target);
	}

	public final Target getTarget() {
		return target;
	}

	public final void setTarget(final Target target) {
		this.target = target;
	}
}
