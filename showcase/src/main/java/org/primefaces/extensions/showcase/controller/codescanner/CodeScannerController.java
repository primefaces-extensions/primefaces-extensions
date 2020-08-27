package org.primefaces.extensions.showcase.controller.codescanner;

import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;
import org.primefaces.extensions.model.codescanner.Code;

/**
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 */
@Named
@ViewScoped
public class CodeScannerController implements Serializable {

    public void onCodeScanned(final SelectEvent<Code> event) {
		final Code code = event.getObject();
		FacesContext.getCurrentInstance().addMessage(
                null,
				new FacesMessage(FacesMessage.SEVERITY_INFO,
                        String.format("Scanned: %s (%s)", code.getValue(), code.getFormat()),
                        null)
        );
	}

}
