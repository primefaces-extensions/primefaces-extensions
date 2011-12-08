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

package org.primefaces.extensions.component.remotecommand;

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
 * {@link ComponentHandler} for the {@link RemoteCommandParameter} component.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.2
 */
public class RemoteCommandParameterHandler extends ComponentHandler {

	private static final ApplyToMetaRule META_RULE = new ApplyToMetaRule();

	/**
	 * {@link MetaRule} for the <code>applyTo</code> of the {@link RemoteCommandParameter}.
	 *
	 * @author Thomas Andraschko / last modified by $Author$
	 * @version $Revision$
	 */
	private static final class ApplyToMetaRule extends MetaRule {

        @Override
		public Metadata applyRule(final String name, final TagAttribute attribute, final MetadataTarget meta) {
            if (meta.isTargetInstanceOf(RemoteCommandParameter.class)) {
                if (RemoteCommandParameter.PropertyKeys.applyTo.toString().equals(name)) {
                    return new ApplyToValueExpressionMetadata(attribute);
                }
            }

            return null;
        }
    }

	/**
	 * {@link Metadata} for the <code>applyTo</code> of the {@link RemoteCommandParameter}.
	 *
	 * @author Thomas Andraschko / last modified by $Author$
	 * @version $Revision$
	 */
	private static final class ApplyToValueExpressionMetadata extends Metadata {

		private final TagAttribute attribute;

		public ApplyToValueExpressionMetadata(final TagAttribute attribute) {
			this.attribute = attribute;
		}

		@Override
		public void applyMetadata(final FaceletContext context, final Object instance) {
			final RemoteCommandParameter param = (RemoteCommandParameter) instance;
			final ValueExpression valueExpression = attribute.getValueExpression(context, Object.class);

			param.setApplyTo(valueExpression);
		}
	}

	public RemoteCommandParameterHandler(final ComponentConfig config) {
		super(config);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected MetaRuleset createMetaRuleset(final Class type) {
		MetaRuleset metaRuleset = super.createMetaRuleset(type);
		metaRuleset.addRule(META_RULE);

		return metaRuleset;
	}
}
