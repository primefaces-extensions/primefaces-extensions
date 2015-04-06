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

package org.primefaces.extensions.component.ajaxerrorhandler;

import java.util.LinkedList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;

import org.apache.commons.lang3.StringUtils;

/**
 * {@link VisitCallback} which collects all {@link AjaxErrorHandler}s.
 * 
 * @author Pavol Slany / last modified by $Author: $
 * @version $Revision: $
 * @since 0.5
 */
public class AjaxErrorHandlerVisitCallback implements VisitCallback {

	private List<AjaxErrorHandler> defaultAjaxErrorHandler = new LinkedList<AjaxErrorHandler>();
	private List<AjaxErrorHandler> typeAjaxErrorHandler = new LinkedList<AjaxErrorHandler>();

	private String type;
	private String timestampFormat;

	private UIComponent facetTitle = null;
	private UIComponent facetBody = null;
	private List<UIComponent> customContent = null;
	private boolean resolved = false;

	public AjaxErrorHandlerVisitCallback(final String type) {
		this.type = type;
	}

	public void resolveFacetsAndCustomContent() {
		if (resolved == true) {
			return;
		}

		resolved = true;
		facetTitle = null;
		facetBody = null;
		customContent = null;
		timestampFormat = null;

		List<AjaxErrorHandler> ajaxErrorHandlers = new LinkedList<AjaxErrorHandler>(defaultAjaxErrorHandler);
		ajaxErrorHandlers.addAll(typeAjaxErrorHandler);

		for (AjaxErrorHandler ajaxErrorHandler : ajaxErrorHandlers) {
			if (ajaxErrorHandler.getChildCount() > 0) {
				facetBody = null;
				facetTitle = null;
				customContent = ajaxErrorHandler.getChildren();
				continue;
			}

			boolean isCustomContentRedefined =
					ajaxErrorHandler.getTitle() != null ||
					ajaxErrorHandler.getFacet(AjaxErrorHandler.PropertyKeys.title.toString()) != null ||
					ajaxErrorHandler.getBody() != null ||
					ajaxErrorHandler.getFacet(AjaxErrorHandler.PropertyKeys.body.toString()) != null ||
					ajaxErrorHandler.getButton() != null ||
					ajaxErrorHandler.getButtonOnclick() != null;

			if (isCustomContentRedefined) {
				customContent = null;
			}

			if (ajaxErrorHandler.getTimestampFormat() != null) {
				timestampFormat = ajaxErrorHandler.getTimestampFormat();
			}


			////// title ...
			// If later ajaxErrorHandler has TITLE attribute => early TITLE in FACET is ignored ...
			if (ajaxErrorHandler.getTitle() != null) {
				facetTitle = null;
			}

			if (ajaxErrorHandler.getFacet(AjaxErrorHandler.PropertyKeys.title.toString()) != null) {
				facetTitle = ajaxErrorHandler.getFacet(AjaxErrorHandler.PropertyKeys.title.toString());
			}

			////// body ...
			// If later ajaxErrorHandler has BODY attribute => early TITLE in FACET is ignored ...
			if (ajaxErrorHandler.getBody() != null) {
				facetBody = null;
			}

			if (ajaxErrorHandler.getFacet(AjaxErrorHandler.PropertyKeys.body.toString()) != null) {
				facetBody = ajaxErrorHandler.getFacet(AjaxErrorHandler.PropertyKeys.body.toString());
			}
		}

	}

	public String getTimestampFormat() {
		resolveFacetsAndCustomContent();
		return timestampFormat;
	}

	public UIComponent findCurrentTitleFacet() {
		resolveFacetsAndCustomContent();

		return facetTitle;
	}

	public AjaxErrorHandlerVisitCallback() {
		super();
	}

	public UIComponent findCurrentBodyFacet() {
		resolveFacetsAndCustomContent();

		return facetBody;
	}

	public List<UIComponent> findCurrentChildren() {
		resolveFacetsAndCustomContent();

		return customContent;
	}

	public VisitResult visit(final VisitContext context, final UIComponent target) {
		if (!target.isRendered()) {
			return VisitResult.REJECT;
		}

		if (!(target instanceof AjaxErrorHandler)) {
			return VisitResult.ACCEPT;
		}

		AjaxErrorHandler handler = (AjaxErrorHandler) target;

		if (handler.getType() == null) {
			resolved = false;
			defaultAjaxErrorHandler.add(handler);
		} else if (StringUtils.equals(handler.getType(), this.type)) {
			resolved = false;
			typeAjaxErrorHandler.add(handler);
		}

		return VisitResult.REJECT;
	}
}
