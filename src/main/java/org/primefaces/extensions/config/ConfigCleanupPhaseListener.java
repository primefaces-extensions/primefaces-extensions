package org.primefaces.extensions.config;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 * Cleanup the {@link ThreadLocal} cache for our {@link ConfigContainer} because
 * threads can be reused and therefore it could contain the wrong {@link ConfigContainer}.
 */
@SuppressWarnings("serial")
public class ConfigCleanupPhaseListener implements PhaseListener {

	public void afterPhase(final PhaseEvent event) {
		ConfigProvider.cleanupThreadLocalCache();
	}

	public void beforePhase(final PhaseEvent event) {
		// do nothing
	}

	public PhaseId getPhaseId() {
		return PhaseId.RENDER_RESPONSE;
	}
}
