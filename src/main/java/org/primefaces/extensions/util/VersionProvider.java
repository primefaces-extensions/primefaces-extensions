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

package org.primefaces.extensions.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * Provides current version for this project.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since   0.2
 */
public final class VersionProvider {

	private static final Logger LOGGER = Logger.getLogger(VersionProvider.class.getName());

	private static final VersionProvider INSTANCE = new VersionProvider();
	private String version;

	private VersionProvider() {
		ResourceBundle rb;
		try {
			rb = ResourceBundle.getBundle("primefaces-extensions");
			version = rb.getString("application.version");
		} catch (MissingResourceException e) {
			LOGGER.warning("Resource bundle 'primefaces-extensions' was not found or error while reading current version.");
		}
	}

	public static String getVersion() {
		return INSTANCE.version;
	}
}
