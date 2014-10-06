package org.primefaces.extensions.component.timer;

import org.primefaces.facelets.MethodRule;

import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.MetaRule;
import javax.faces.view.facelets.MetaRuleset;

/**
 * Handler for {@link Timer}
 */
public class TimerHandler extends ComponentHandler {

	private static final MetaRule LISTENER = new MethodRule("listener", null, new Class[0]);
	
	public TimerHandler(ComponentConfig config) {
		super(config);
	}

	protected MetaRuleset createMetaRuleset(Class type) {
		MetaRuleset metaRuleset = super.createMetaRuleset(type);

		metaRuleset.addRule(LISTENER);

		return metaRuleset;
	}
}