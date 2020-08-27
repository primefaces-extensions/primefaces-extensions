package org.primefaces.extensions.showcase.controller.documentviewer;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class AdvancedDocumentViewerController implements Serializable {

	private static final long serialVersionUID = 1L;

	private int page = 2;
	private String locale = "en";

	public int getPage() {
		return page;
	}

	public void setPage(final int page) {
		this.page = page;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(final String locale) {
		this.locale = locale;
	}
}
