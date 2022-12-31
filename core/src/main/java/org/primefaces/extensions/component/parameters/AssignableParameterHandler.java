/*
 * Copyright (c) 2011-2023 PrimeFaces Extensions
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
package org.primefaces.extensions.component.parameters;

import javax.el.ValueExpression;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.MetaRule;
import javax.faces.view.facelets.MetaRuleset;
import javax.faces.view.facelets.Metadata;
import javax.faces.view.facelets.MetadataTarget;
import javax.faces.view.facelets.TagAttribute;

/**
 * {@link ComponentHandler} for the {@link AssignableParameter} component.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.5
 */
public class AssignableParameterHandler extends ComponentHandler {

    private static final AssignToMetaRule META_RULE = new AssignToMetaRule();

    /**
     * {@link MetaRule} for the <code>assignTo</code> of the {@link AssignableParameter}.
     *
     * @author Thomas Andraschko / last modified by $Author$
     * @version $Revision$
     */
    private static final class AssignToMetaRule extends MetaRule {

        @Override
        public Metadata applyRule(final String name, final TagAttribute attribute, final MetadataTarget meta) {
            if (meta.isTargetInstanceOf(AssignableParameter.class) && AssignableParameter.PropertyKeys.assignTo.toString().equals(name)) {
                return new AssignToValueExpressionMetadata(attribute);
            }

            return null;
        }
    }

    /**
     * {@link Metadata} for the <code>assignTo</code> of the {@link AssignableParameter}.
     *
     * @author Thomas Andraschko / last modified by $Author$
     * @version $Revision$
     */
    private static final class AssignToValueExpressionMetadata extends Metadata {

        private final TagAttribute attribute;

        public AssignToValueExpressionMetadata(final TagAttribute attribute) {
            this.attribute = attribute;
        }

        @Override
        public void applyMetadata(final FaceletContext context, final Object instance) {
            final AssignableParameter param = (AssignableParameter) instance;
            final ValueExpression valueExpression = attribute.getValueExpression(context, Object.class);

            param.setAssignTo(valueExpression);
        }
    }

    public AssignableParameterHandler(final ComponentConfig config) {
        super(config);
    }

    @Override
    protected MetaRuleset createMetaRuleset(final Class type) {
        final MetaRuleset metaRuleset = super.createMetaRuleset(type);
        metaRuleset.addRule(META_RULE);

        return metaRuleset;
    }
}
