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

package org.primefaces.extensions.component.common;

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
            if (meta.isTargetInstanceOf(AssignableParameter.class)) {
                if (AssignableParameter.PropertyKeys.assignTo.toString().equals(name)) {
                    return new AssignToValueExpressionMetadata(attribute);
                }
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
	@SuppressWarnings("unchecked")
	protected MetaRuleset createMetaRuleset(final Class type) {
		final MetaRuleset metaRuleset = super.createMetaRuleset(type);
		metaRuleset.addRule(META_RULE);

		return metaRuleset;
	}
}
