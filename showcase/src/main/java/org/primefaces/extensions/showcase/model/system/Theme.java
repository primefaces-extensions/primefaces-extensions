package org.primefaces.extensions.showcase.model.system;

/**
 * Theme
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 */
public class Theme {

	private String name;
	private String image;

	public Theme() {
	}

	public Theme(final String name, final String image) {
		this.name = name;
		this.image = image;
	}

	public final String getName() {
		return name;
	}

	public final void setName(final String name) {
		this.name = name;
	}

	public final String getImage() {
		return image;
	}

	public final void setImage(final String image) {
		this.image = image;
	}
}
