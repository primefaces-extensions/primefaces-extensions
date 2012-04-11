/*
 * Copyright 2011-2012 PrimeFaces Extensions.
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

package org.primefaces.extensions.component.remotecommand;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.el.MethodExpression;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.CompositeFaceletHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.MetaRule;
import javax.faces.view.facelets.MetaRuleset;
import javax.faces.view.facelets.Metadata;
import javax.faces.view.facelets.MetadataTarget;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagAttributeException;

import org.primefaces.extensions.component.common.MethodParameter;
import org.primefaces.extensions.component.common.MethodSignatureTagHandler;

/**
 * {@link ComponentHandler} for the {@link RemoteCommand} component.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.5
 */
public class RemoteCommandHandler extends ComponentHandler {

	/**
	 * {@link MetaRule} for the <code>actionListener</code> of the {@link RemoteCommand}.
	 * This extra {@link MetaRule} is required if {@link MethodParameter}s are used.
	 *
	 * @author Thomas Andraschko / last modified by $Author$
	 * @version $Revision$
	 */
	private static final class RemoteCommandMetaRule extends MetaRule {

		private Class<?>[] parameters;

		public RemoteCommandMetaRule(final Class<?>[] parameters) {
			this.parameters = parameters;
		}

        @Override
		public Metadata applyRule(final String name, final TagAttribute attribute, final MetadataTarget meta) {
            if (meta.isTargetInstanceOf(RemoteCommand.class)) {
            	if (RemoteCommand.PropertyKeys.actionListener.toString().equals(name)) {
                	final Method method = meta.getWriteMethod("actionListenerMethodExpression");
                	return new ActionListenerMetadata(attribute, method, parameters);
                }
            }

            return null;
        }
    }

	/**
	 * {@link Metadata} for the <code>actionListener</code> of the {@link RemoteCommand}.
	 * This extra {@link Metadata} is required if {@link MethodParameter}s are used.
	 *
	 * @author Thomas Andraschko / last modified by $Author$
	 * @version $Revision$
	 */
	private static final class ActionListenerMetadata extends Metadata {

		private final TagAttribute attribute;
		private final Method method;
		private Class<?>[] parameters;

		public ActionListenerMetadata(final TagAttribute attribute, final Method method, final Class<?>[] parameters) {
			this.attribute = attribute;
			this.method = method;
			this.parameters = parameters;
		}

		@Override
		public void applyMetadata(final FaceletContext context, final Object instance) {
            final MethodExpression expression = attribute.getMethodExpression(context, null, parameters);

            //invoke setAction with MethodExpression
            try {
            	method.invoke(instance, new Object[] { expression });
            } catch (InvocationTargetException e) {
                throw new TagAttributeException(attribute, e.getCause());
            } catch (Exception e) {
                throw new TagAttributeException(attribute, e);
            }
		}
	}

	public RemoteCommandHandler(final ComponentConfig config) {
		super(config);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected MetaRuleset createMetaRuleset(final Class type) {
		final MetaRuleset metaRuleset = super.createMetaRuleset(type);
		metaRuleset.addRule(new RemoteCommandMetaRule(getParameterTypes()));

		return metaRuleset;
	}

	private Class<?>[] getParameterTypes() {
		//try to get next MethodSignatureTagHandler
		MethodSignatureTagHandler signatureTagHandler = null;

		if (nextHandler instanceof CompositeFaceletHandler) {
			final CompositeFaceletHandler handler = (CompositeFaceletHandler) nextHandler;
			if (handler.getHandlers().length > 0 && handler.getHandlers()[0] instanceof MethodSignatureTagHandler) {
				signatureTagHandler = (MethodSignatureTagHandler) handler.getHandlers()[0];
			}
		}

		if (signatureTagHandler == null) {
			return new Class<?>[] {};
		}

		return signatureTagHandler.getParameterTypes();
	}
}
