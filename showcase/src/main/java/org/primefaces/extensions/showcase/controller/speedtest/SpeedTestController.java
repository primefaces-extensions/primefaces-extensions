/*
 * Copyright (c) 2011-2022 PrimeFaces Extensions
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */
package org.primefaces.extensions.showcase.controller.speedtest;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;
import org.primefaces.extensions.component.speedtest.Speedtest;
import org.primefaces.extensions.event.SpeedTestEvent;

/**
 * {@link Speedtest} Controller.
 *
 * @author Stefan Sibitz ssibitz@me.com
 */
@Named
@ViewScoped
public class SpeedTestController implements Serializable {

    private static final long serialVersionUID = -5297177555931103300L;
    private static final Integer ONEMB = 1024 * 1024;
    // Size of Download-File (Default=5)
    private Integer sizeInMB = 5;

    public static void saveResults(final SpeedTestEvent speedTestEvent) {
        final String msg = "PingTimeMS       ='" + String.valueOf(speedTestEvent.getPingTimeMS()) + "', \n"
                    + "Jitter           ='" + String.valueOf(speedTestEvent.getJitterTimeMS()) + "', \n"
                    + "SpeedMbpsDownload='" + String.valueOf(speedTestEvent.getSpeedMbpsDownload()) + "', \n"
                    + "SpeedMbpsUpload='" + String.valueOf(speedTestEvent.getSpeedMbpsUpload()) + "'";
        final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Saved your speed results:", msg);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    private String generatePayloadFile() {
        // Generate random test string
        final Integer size2Generate = ONEMB * sizeInMB;
        return RandomStringUtils.randomAlphanumeric(size2Generate);
    }

    public void downloadPayloadFile() {
        final String payloadString = generatePayloadFile();
        try {
            final FacesContext facesContext = FacesContext.getCurrentInstance();
            final HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
            response.reset();
            response.setHeader("Content-Type", "application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=dummy.payload");
            response.getWriter().write(payloadString);
            response.getWriter().flush();
            facesContext.responseComplete();
        }
        catch (final Exception e) {
            // ignore
        }
    }

    public void updateSizeInMB() {
        // Dummy for updating the size on the server...
    }

    public Integer getSizeInMB() {
        return sizeInMB;
    }

    public void setSizeInMB(final Integer sizeInMB) {
        this.sizeInMB = sizeInMB;
    }
}
