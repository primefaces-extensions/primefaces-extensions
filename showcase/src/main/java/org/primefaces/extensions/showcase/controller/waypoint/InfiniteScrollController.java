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

package org.primefaces.extensions.showcase.controller.waypoint;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 * InfiniteScrollController
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 */
@Named
@ViewScoped
public class InfiniteScrollController implements Serializable {

	private static final long serialVersionUID = 20120810L;

	private static final String CONTENT_PATH_DUMMY = "/sections/waypoint/examples/remoteContentDummy.xhtml";
	private static final String CONTENT_PATH_FIRST = "/sections/waypoint/examples/remoteContentOne.xhtml";
	private static final String CONTENT_PATH_SECOND = "/sections/waypoint/examples/remoteContentSecond.xhtml";

	private String src;

	@PostConstruct
	public void initialize() {
		src = CONTENT_PATH_DUMMY;
	}

	public void toggleSrc(final ActionEvent evt) {
		try {
			// simulate a long running request
			Thread.sleep(1500);
		} catch (final Exception e) {
			// ignore
		}

		if (CONTENT_PATH_DUMMY.equals(src) || CONTENT_PATH_SECOND.equals(src)) {
			src = CONTENT_PATH_FIRST;
		} else if (CONTENT_PATH_FIRST.equals(src)) {
			src = CONTENT_PATH_SECOND;
		}
	}

	public String getSrc() {
		return src;
	}
}
