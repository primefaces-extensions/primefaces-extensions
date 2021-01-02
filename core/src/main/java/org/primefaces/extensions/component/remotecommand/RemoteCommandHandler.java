/*
 * Copyright (c) 2011-2021 PrimeFaces Extensions
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

import javax.el.MethodExpression;
import javax.faces.view.facelets.*;

import org.primefaces.extensions.component.parameters.MethodParameter;
import org.primefaces.extensions.component.parameters.MethodSignatureTagHandler;

/**
 * {@link ComponentHandler} for the {@link RemoteCommand} component.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.5
 */
public class RemoteCommandHandler extends ComponentHandler {

    /**
     * {@link MetaRule} for the <code>actionListener</code> of the {@link RemoteCommand}. This extra {@link MetaRule} is required if {@link MethodParameter}s
     * are used.
     *
     * @author Thomas Andraschko / last modified by $Author$
     * @version $Revision$
     */
    private static final class RemoteCommandMetaRule extends MetaRule {

        private final Class<?>[] parameters;

        public RemoteCommandMetaRule(final Class<?>[] parameters) {
            this.parameters = parameters;
        }

        @Override
        public Metadata applyRule(final String name, final TagAttribute attribute, final MetadataTarget meta) {
            if (meta.isTargetInstanceOf(RemoteCommand.class) && RemoteCommand.PropertyKeys.actionListener.toString().equals(name)) {
                final Method method = meta.getWriteMethod("actionListenerMethodExpression");

                return new ActionListenerMetadata(attribute, method, parameters);
            }

            return null;
        }
    }

    /**
     * {@link Metadata} for the <code>actionListener</code> of the {@link RemoteCommand}. This extra {@link Metadata} is required if {@link MethodParameter}s
     * are used.
     *
     * @author Thomas Andraschko / last modified by $Author$
     * @version $Revision$
     */
    private static final class ActionListenerMetadata extends Metadata {

        private final TagAttribute attribute;
        private final Method method;
        private final Class<?>[] parameters;

        public ActionListenerMetadata(final TagAttribute attribute, final Method method, final Class<?>[] parameters) {
            this.attribute = attribute;
            this.method = method;
            this.parameters = parameters;
        }

        @Override
        public void applyMetadata(final FaceletContext context, final Object instance) {
            final MethodExpression expression = attribute.getMethodExpression(context, null, parameters);

            // invoke setAction with MethodExpression
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

    public RemoteCommandHandler(final ComponentConfig config) {
        super(config);
    }

    @Override
    protected MetaRuleset createMetaRuleset(final Class type) {
        final MetaRuleset metaRuleset = super.createMetaRuleset(type);
        metaRuleset.addRule(new RemoteCommandMetaRule(getParameterTypes()));

        return metaRuleset;
    }

    private Class<?>[] getParameterTypes() {
        // try to get next MethodSignatureTagHandler
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
