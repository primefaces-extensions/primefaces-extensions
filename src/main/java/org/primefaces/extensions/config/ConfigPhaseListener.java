package org.primefaces.extensions.config;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 * Cleanup the {@link ThreadLocal} cache for our {@link ConfigContainer} because
 * threads can be reused and therefore it could contain the wrong {@link ConfigContainer}.
 */
@SuppressWarnings("serial")
public class ConfigPhaseListener implements PhaseListener {

	public void afterPhase(final PhaseEvent event) {

	}

	public void beforePhase(final PhaseEvent event) {
		ConfigProvider.cleanCache();
	}

	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}
}
