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

package org.primefaces.extensions.showcase.webapp;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.FacesException;
import javax.inject.Named;

import org.primefaces.extensions.showcase.model.system.DocuAttribute;
import org.primefaces.extensions.showcase.model.system.DocuEvent;
import org.primefaces.extensions.showcase.model.system.DocuTag;
import org.primefaces.extensions.showcase.util.TagLibParser;

/**
 * Prepares the documentation from the tag library.
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 */
@ApplicationScoped
@Named
public class DocuPreparer {

	private Map<String, DocuTag> tags;

	@PostConstruct
	protected void initialize() {
		try {
			tags = TagLibParser.getTags();
		} catch (final Exception e) {
			throw new FacesException("Taglib parsing failed!", e);
		}
	}

	public List<DocuAttribute> getDocuAttributes(final String tagName) {
		if (tagName == null || tags == null) {
			return null;
		}

		final DocuTag docuTag = tags.get(tagName);
		if (docuTag == null) {
			return null;
		}

		return docuTag.getAttributes();
	}

	public List<DocuEvent> getDocuEvents(final String tagName) {
		if (tagName == null || tags == null) {
			return null;
		}

		final DocuTag docuTag = tags.get(tagName);
		if (docuTag == null) {
			return null;
		}

		return docuTag.getEvents();
	}
}
