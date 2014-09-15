package org.primefaces.extensions.component.countdown;

import org.primefaces.facelets.MethodRule;

import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.MetaRule;
import javax.faces.view.facelets.MetaRuleset;

/**
 * Handler for {@link org.primefaces.extensions.component.countdown.Countdown}
 */
public class CountdownHandler extends ComponentHandler {

	private static final MetaRule LISTENER = new MethodRule("listener", null, new Class[0]);
	
	public CountdownHandler(ComponentConfig config) {
		super(config);
	}

	protected MetaRuleset createMetaRuleset(Class type) {
		MetaRuleset metaRuleset = super.createMetaRuleset(type);

		metaRuleset.addRule(LISTENER);

		return metaRuleset;
	}
}