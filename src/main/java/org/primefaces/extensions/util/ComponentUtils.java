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
import javax.faces.application.Application;
import javax.faces.application.ProjectStage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
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

	private static final Logger LOG = Logger.getLogger(ComponentUtils.class.getName());

	public static String escapeComponentId(final String id) {
		return id.replaceAll(":", "\\\\\\\\:");
	}

	public static String findClientIds(final FacesContext context, final UIComponent component, final String list) {
		if (list == null) {
			return "@none";
		}

		final StringBuilder newList = new StringBuilder();
		final String[] ids = list.split("[\\s,]+");

		for (int i = 0; i < ids.length; i++) {
			String id = ids[i];

			if (id.equals("@this")) {
				id = component.getClientId(context);
			} else if (id.equals("@form")) {
				final UIComponent form = ComponentUtils.findParentForm(context, component);
				if (form != null) {
					id = form.getClientId(context);
				} else if (context.isProjectStage(ProjectStage.Development)) {
					LOG.log(Level.INFO, "Cannot find enclosing form for component \"{0}\".", component.getClientId(context));
					id = "";
				}
			} else if (id.equals("@parent")) {
				id = component.getParent().getClientId(context);
			} else if (!id.equals("@all") && !id.equals("@none")) {
				final UIComponent comp = component.findComponent(id);
				if (comp != null) {
					id = comp.getClientId(context);
				} else if (context.isProjectStage(ProjectStage.Development)) {
					LOG.log(Level.WARNING, "Cannot find component with identifier \"{0}\" in view.", id);
					id = "";
				}
			}

			if (i != 0) {
				newList.append(" ");
			}

			newList.append(id);
		}

		return newList.toString();
	}

	public static List<UIComponent> findComponents(final FacesContext context, final UIComponent source, final String list) {
		final List<UIComponent> foundComponents = new ArrayList<UIComponent>();

		final String[] ids = list.split("[\\s,]+");

		for (int i = 0; i < ids.length; i++) {
			final String id = ids[i];

			if (id.equals("@this")) {
				foundComponents.add(source);
			} else if (id.equals("@form")) {
				final UIComponent form = ComponentUtils.findParentForm(context, source);

				if (form != null) {
					foundComponents.add(form);
				} else if (context.isProjectStage(ProjectStage.Development)) {
					LOG.log(Level.INFO, "Cannot find enclosing form for component \"{0}\".", source.getClientId(context));
				}
			} else if (id.equals("@parent")) {
				foundComponents.add(source.getParent());
			} else if (id.equals("@all") || id.equals("@none")) {
				LOG.log(Level.WARNING, "Components @all and @none are not supported.");
			} else {
				final UIComponent component = source.findComponent(id);

				if (component != null) {
					foundComponents.add(component);
				} else if (context.isProjectStage(ProjectStage.Development)) {
					LOG.log(Level.WARNING, "Cannot find component with identifier \"{0}\" in view.", id);
				}
			}
		}

		return foundComponents;
	}

	public static String findTarget(final FacesContext context, final Attachable attachable) {
		if (!(attachable instanceof UIComponent)) {
			throw new FacesException("An attachable component must extend UIComponent or ClientBehavior.");
		}

		return findTarget(context, attachable, (UIComponent) attachable);
	}

	public static String findTarget(final FacesContext context, final Attachable attachable,
	                                final ClientBehaviorContext cbContext) {
		if (!(attachable instanceof ClientBehavior)) {
			throw new FacesException("An attachable component must extend UIComponent or ClientBehavior.");
		}

		if (cbContext == null) {
			throw new FacesException("ClientBehaviorContext is null.");
		}

		return findTarget(context, attachable, cbContext.getComponent());
	}

	private static String findTarget(final FacesContext context, final Attachable attachable, final UIComponent component) {
		final String forValue = attachable.getFor();
		if (forValue != null) {
			final UIComponent forComponent = component.findComponent(forValue);
			if (forComponent == null) {
				throw new FacesException("Cannot find component '" + forValue + "'.");
			}

			return ComponentUtils.escapeJQueryId(forComponent.getClientId(context));
		}

		if (attachable instanceof EnhancedAttachable) {
			final EnhancedAttachable enhancedAttachable = (EnhancedAttachable) attachable;
			final String forSelector = enhancedAttachable.getForSelector();

			if (forSelector != null) {
				if (forSelector.startsWith("#")) {
					return ComponentUtils.escapeComponentId(forSelector);
				}

				return forSelector;
			}
		}

		return ComponentUtils.escapeJQueryId(component.getParent().getClientId(context));
	}

	public static void addComponentResource(final FacesContext context, final String name) {
		addComponentResource(context, name, Constants.LIBRARY, "head");
	}

    public static void addComponentResource(final FacesContext context, final String name, final String library, final String target) {
        final Application application = context.getApplication();

        final UIComponent componentResource = application.createComponent(UIOutput.COMPONENT_TYPE);
        componentResource.setRendererType(application.getResourceHandler().getRendererTypeForResourceName(name));
        componentResource.setTransient(true);
        componentResource.setId(context.getViewRoot().createUniqueId());
        componentResource.getAttributes().put("name", name);
        componentResource.getAttributes().put("library", library);
        componentResource.getAttributes().put("target", target);

        context.getViewRoot().addComponentResource(context, componentResource, target);
    }
}
