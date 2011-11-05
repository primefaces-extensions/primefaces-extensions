package org.primefaces.extensions.application;

import javax.faces.application.FacesMessage;

public class TargetableFacesMessage extends FacesMessage {

	public enum NotificationTarget {
		MESSAGE,
		MESSAGES,
		GROWL,
		ALL;
	}

	private NotificationTarget notificationTarget;

	public TargetableFacesMessage(final NotificationTarget target) {
		super();
		setNotificationTarget(target);
	}

	public TargetableFacesMessage(final String summary, final NotificationTarget target) {
		super(summary);
		setNotificationTarget(target);
	}

	public TargetableFacesMessage(final String summary, final String detail, final NotificationTarget target) {
		super(summary, detail);
		setNotificationTarget(target);
	}

	public TargetableFacesMessage(final Severity severity, final String summary, final String detail,
			final NotificationTarget target) {

		super(severity, summary, detail);
		setNotificationTarget(target);
	}

	public final NotificationTarget getNotificationTarget() {
		return notificationTarget;
	}

	public final void setNotificationTarget(final NotificationTarget notificationTarget) {
		this.notificationTarget = notificationTarget;
	}
}
