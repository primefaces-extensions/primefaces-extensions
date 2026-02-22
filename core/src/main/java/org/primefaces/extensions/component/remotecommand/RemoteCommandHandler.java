/*
 * Copyright (c) 2011-2026 PrimeFaces Extensions
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */
package org.primefaces.extensions.component.remotecommand;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import jakarta.el.MethodExpression;
import jakarta.faces.view.facelets.ComponentConfig;
import jakarta.faces.view.facelets.ComponentHandler;
import jakarta.faces.view.facelets.CompositeFaceletHandler;
import jakarta.faces.view.facelets.FaceletContext;
import jakarta.faces.view.facelets.MetaRule;
import jakarta.faces.view.facelets.MetaRuleset;
import jakarta.faces.view.facelets.Metadata;
import jakarta.faces.view.facelets.MetadataTarget;
import jakarta.faces.view.facelets.TagAttribute;
import jakarta.faces.view.facelets.TagAttributeException;

import org.primefaces.extensions.component.parameters.MethodSignatureTagHandler;

/**
 * ComponentHandler for RemoteCommand; supports actionListener with MethodParameter signature.
 *
 * @since 0.5
 */
public class RemoteCommandHandler extends ComponentHandler {

    private static final String ACTION_LISTENER_PROPERTY = "actionListener";

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

    private static final class RemoteCommandMetaRule extends MetaRule {

        private final Class<?>[] parameters;

        RemoteCommandMetaRule(final Class<?>[] parameters) {
            this.parameters = parameters;
        }

        @Override
        public Metadata applyRule(final String name, final TagAttribute attribute, final MetadataTarget meta) {
            if (meta.isTargetInstanceOf(RemoteCommand.class) && ACTION_LISTENER_PROPERTY.equals(name)) {
                final Method method = meta.getWriteMethod("actionListenerMethodExpression");
                return new ActionListenerMetadata(attribute, method, parameters);
            }
            return null;
        }
    }

    private static final class ActionListenerMetadata extends Metadata {

        private final TagAttribute attribute;
        private final Method method;
        private final Class<?>[] parameters;

        ActionListenerMetadata(final TagAttribute attribute, final Method method, final Class<?>[] parameters) {
            this.attribute = attribute;
            this.method = method;
            this.parameters = parameters;
        }

        @Override
        public void applyMetadata(final FaceletContext context, final Object instance) {
            final MethodExpression expression = attribute.getMethodExpression(context, null, parameters);
            try {
                method.invoke(instance, expression);
            }
            catch (final InvocationTargetException e) {
                throw new TagAttributeException(attribute, e.getCause());
            }
            catch (final Exception e) {
                throw new TagAttributeException(attribute, e);
            }
        }
    }
}
