package org.primefaces.extensions.showcase.controller.gravatar;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 * Example Bean for Gravatar component
 *
 * @author f.strazzullo
 */
@Named
@ViewScoped
public class GravatarController implements Serializable {

	private static final long serialVersionUID = 1L;

	private int size = 200;
	private String notFound = "default";
	private String username = "iamadummygravataruser@gmail.com";

	public String getNotFound() {
		return notFound;
	}

	public void setNotFound(final String notFound) {
		this.notFound = notFound;
	}

	public int getSize() {
		return size;
	}

	public void setSize(final int size) {
		this.size = size;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}
}
