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
 * $Id: $
 */

package org.primefaces.extensions.behavior.javascript;

import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.view.AttachedObjectHandler;
import javax.faces.view.AttachedObjectTarget;
import javax.faces.view.BehaviorHolderAttachedObjectHandler;
import javax.faces.view.BehaviorHolderAttachedObjectTarget;
import javax.faces.view.facelets.BehaviorConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagException;
import javax.faces.view.facelets.TagHandler;

/**
 * {@link BehaviorHolderAttachedObjectHandler} and {@link TagHandler} implementation for the {@link JavascriptBehavior}.
 *
 * @author Thomas Andraschko / last modified by $Author: $
 * @version $Revision: $
 * @since 0.2
 */
public class JavascriptBehaviorHandler extends TagHandler implements BehaviorHolderAttachedObjectHandler {

	private final TagAttribute event;
	private final TagAttribute execute;
	private final TagAttribute disabled;

	public JavascriptBehaviorHandler(final BehaviorConfig config) {
		super(config);
		this.execute = this.getAttribute("execute");
		this.disabled = this.getAttribute("disabled");
		this.event = this.getAttribute("event");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void apply(final FaceletContext faceletContext, final UIComponent parent) throws IOException {
		if (!ComponentHandler.isNew(parent)) {
			return;
		}

		final String eventName = getEventName();

		if (UIComponent.isCompositeComponent(parent)) {
			boolean tagApplied = false;
			if (parent instanceof ClientBehaviorHolder) {
				applyAttachedObject(faceletContext, parent, eventName);
				tagApplied = true;
			}

			final BeanInfo componentBeanInfo = (BeanInfo) parent.getAttributes().get(UIComponent.BEANINFO_KEY);
			if (null == componentBeanInfo) {
				throw new TagException(tag, "Composite component does not have BeanInfo attribute");
			}

			final BeanDescriptor componentDescriptor = componentBeanInfo.getBeanDescriptor();
			if (null == componentDescriptor) {
				throw new TagException(tag, "Composite component BeanInfo does not have BeanDescriptor");
			}

			final List<AttachedObjectTarget> targetList = (List<AttachedObjectTarget>)
					componentDescriptor.getValue(AttachedObjectTarget.ATTACHED_OBJECT_TARGETS_KEY);
			if (targetList == null && !tagApplied) {
				throw new TagException(tag, "Composite component does not support behavior events");
			}

			boolean supportedEvent = false;
			if (targetList != null) {
				for (AttachedObjectTarget target : targetList) {
					if (target instanceof BehaviorHolderAttachedObjectTarget) {
						final BehaviorHolderAttachedObjectTarget behaviorTarget = (BehaviorHolderAttachedObjectTarget) target;
						if ((null != eventName && eventName.equals(behaviorTarget.getName()))
								|| (null == eventName && behaviorTarget.isDefaultEvent())) {
							supportedEvent = true;
							break;
						}
					}
				}
			}

			if (supportedEvent) {
				getAttachedObjectHandlers(parent).add(this);
			} else {
				if (!tagApplied) {
					throw new TagException(tag, "Composite component does not support event " + eventName);
				}
			}
		} else if (parent instanceof ClientBehaviorHolder) {
			applyAttachedObject(faceletContext, parent, eventName);
		} else {
			throw new TagException(this.tag, "Unable to attach <p:ajax> to non-ClientBehaviorHolder parent");
		}
	}

	@Override
	public String getEventName() {
		return (this.event != null) ? this.event.getValue() : null;
	}

	public void applyAttachedObject(FaceletContext context, UIComponent component, String eventName) {
		final ClientBehaviorHolder holder = (ClientBehaviorHolder) component;

		if (null == eventName) {
			eventName = holder.getDefaultEventName();
			if (null == eventName) {
				throw new TagException(this.tag, "Event attribute could not be determined: " + eventName);
			}
		} else {
			final Collection<String> eventNames = holder.getEventNames();
			if (!eventNames.contains(eventName)) {
				throw new TagException(this.tag, "Event:" + eventName + " is not supported.");
			}
		}

		final JavascriptBehavior javascriptBehavior = createJavascriptBehavior(context, eventName);
		holder.addClientBehavior(eventName, javascriptBehavior);
	}

	private JavascriptBehavior createJavascriptBehavior(final FaceletContext faceletContext, final String eventName) {
		final Application application = faceletContext.getFacesContext().getApplication();
		final JavascriptBehavior behavior = (JavascriptBehavior) application.createBehavior(JavascriptBehavior.BEHAVIOR_ID);

		setBehaviorAttribute(faceletContext, behavior, this.disabled, Boolean.class);
		setBehaviorAttribute(faceletContext, behavior, this.execute, String.class);

		return behavior;
	}

	@Override
	public String getFor() {
		return null;
	}

	@Override
	public void applyAttachedObject(final FacesContext context, final UIComponent parent) {
		final FaceletContext faceletContext = (FaceletContext) context.getAttributes().get(FaceletContext.FACELET_CONTEXT_KEY);

		applyAttachedObject(faceletContext, parent, getEventName());
	}

	private void setBehaviorAttribute(final FaceletContext faceletContext, final JavascriptBehavior behavior,
			final TagAttribute attribute, final Class<?> type) {

		if (attribute != null) {
			behavior.setValueExpression(attribute.getLocalName(), attribute.getValueExpression(faceletContext, type));
		}
	}

	public List<AttachedObjectHandler> getAttachedObjectHandlers(final UIComponent component) {
		return getAttachedObjectHandlers(component, true);
	}

	@SuppressWarnings("unchecked")
	public List<AttachedObjectHandler> getAttachedObjectHandlers(final UIComponent component, final boolean create) {
		final Map<String, Object> attributes = component.getAttributes();

		List<AttachedObjectHandler> result = (List<AttachedObjectHandler>) attributes.get("javax.faces.RetargetableHandlers");

		if (result == null) {
			if (create) {
				result = new ArrayList<AttachedObjectHandler>();
				attributes.put("javax.faces.RetargetableHandlers", result);
			} else {
				result = Collections.EMPTY_LIST;
			}
		}
		return result;
	}
}
