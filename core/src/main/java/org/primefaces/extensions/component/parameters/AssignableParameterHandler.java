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
package org.primefaces.extensions.component.parameters;

import jakarta.el.ValueExpression;
import jakarta.faces.view.facelets.ComponentConfig;
import jakarta.faces.view.facelets.ComponentHandler;
import jakarta.faces.view.facelets.FaceletContext;
import jakarta.faces.view.facelets.MetaRule;
import jakarta.faces.view.facelets.MetaRuleset;
import jakarta.faces.view.facelets.Metadata;
import jakarta.faces.view.facelets.MetadataTarget;
import jakarta.faces.view.facelets.TagAttribute;

import org.primefaces.cdk.api.FacesTagHandler;
import org.primefaces.cdk.api.Property;

/**
 * ComponentHandler for the AssignableParameter component; maps assignTo attribute as ValueExpression.
 *
 * @since 0.5
 */
@FacesTagHandler(description = "Component handler for assignableParam; maps assignTo attribute to the component.")
public class AssignableParameterHandler extends ComponentHandler {

    private static final String ASSIGN_TO_PROPERTY = "assignTo";
    private static final AssignToMetaRule META_RULE = new AssignToMetaRule();

    @Property(description = "ValueExpression where the parameter will be applied.", type = ValueExpression.class, required = true)
    private final TagAttribute assignToAttribute;

    public AssignableParameterHandler(final ComponentConfig config) {
        super(config);
        this.assignToAttribute = getRequiredAttribute(ASSIGN_TO_PROPERTY);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected MetaRuleset createMetaRuleset(final Class type) {
        final MetaRuleset metaRuleset = super.createMetaRuleset(type);
        metaRuleset.addRule(META_RULE);
        return metaRuleset;
    }

    private static final class AssignToMetaRule extends MetaRule {

        @Override
        public Metadata applyRule(final String name, final TagAttribute attribute, final MetadataTarget meta) {
            if (meta.isTargetInstanceOf(AssignableParameter.class) && ASSIGN_TO_PROPERTY.equals(name)) {
                return new AssignToValueExpressionMetadata(attribute);
            }
            return null;
        }
    }

    private static final class AssignToValueExpressionMetadata extends Metadata {

        private final TagAttribute attribute;

        AssignToValueExpressionMetadata(final TagAttribute attribute) {
            this.attribute = attribute;
        }

        @Override
        public void applyMetadata(final FaceletContext context, final Object instance) {
            final AssignableParameter param = (AssignableParameter) instance;
            param.setAssignTo(attribute.getValueExpression(context, Object.class));
        }
    }
}
