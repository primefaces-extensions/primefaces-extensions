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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.FacesException;
import javax.faces.application.ProjectStage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.primefaces.extensions.component.base.Attachable;
import org.primefaces.extensions.component.base.EnhancedAttachable;

/**
 * Component utils for this project.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since   0.2
 */
public class ComponentUtils extends org.primefaces.util.ComponentUtils {

	private static final Logger LOGGER = Logger.getLogger(ComponentUtils.class.getName());

	public static String escapeComponentId(final String id) {
		return id.replaceAll(":", "\\\\\\\\:");
	}

	public static List<UIComponent> findComponents(final FacesContext context, final UIComponent source, final String list) {
		final List<UIComponent> foundComponents = new ArrayList<UIComponent>();

		final String formattedList = formatKeywords(context, source, list);
		final String[] ids = formattedList.split("[,\\s]+");

		for (int i = 0; i < ids.length; i++) {
			final String id = ids[i].trim();

			if (id.equals("@all") || id.equals("@none")) {
				LOGGER.log(Level.WARNING, "Components @all and @none are not supported.");
			} else {
				final UIComponent foundComponent = source.findComponent(id);
				if (foundComponent != null) {
					foundComponents.add(foundComponent);
                } else {
                    if (context.isProjectStage(ProjectStage.Development)) {
                        LOGGER.log(Level.WARNING, "Cannot find component with identifier \"{0}\" in view.", id);
                    }
                }
			}
		}

		return foundComponents;
	}

	public static String findTarget(final Attachable attachable, final FacesContext context) {
		if (!(attachable instanceof UIComponent)) {
			throw new FacesException("A attachable component must extend from UIComponent.");
		}

		UIComponent component = (UIComponent) attachable;

		String forValue = attachable.getFor();
		if (forValue != null) {
			UIComponent forComponent = component.findComponent(forValue);
			if (forComponent == null) {
				throw new FacesException("Cannot find component \"" + forValue + "\" in view.");
			}

			return ComponentUtils.escapeJQueryId(forComponent.getClientId(context));
		}

		if (attachable instanceof EnhancedAttachable) {
			EnhancedAttachable enhancedAttachable = (EnhancedAttachable) attachable;
			String forSelector = enhancedAttachable.getForSelector();

			if (forSelector != null) {
				if (forSelector.startsWith("#")) {
					return ComponentUtils.escapeComponentId(forSelector);
				}

				return forSelector;
			}
		}

		return ComponentUtils.escapeJQueryId(component.getParent().getClientId(context));
	}
}
