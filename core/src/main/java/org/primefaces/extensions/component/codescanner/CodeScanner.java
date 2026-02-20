/*
 * Copyright (c) 2011-2026 PrimeFaces Extensions
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
package org.primefaces.extensions.component.codescanner;

import java.util.Map;

import jakarta.faces.application.ResourceDependency;
import jakarta.faces.component.FacesComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.event.FacesEvent;

import org.primefaces.cdk.api.FacesComponentInfo;
import org.primefaces.event.SelectEvent;
import org.primefaces.extensions.model.codescanner.Code;
import org.primefaces.extensions.model.codescanner.Code.Format;
import org.primefaces.extensions.util.Constants;

/**
 * <code>CodeScanner</code> component.
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 * @since 10.0
 */
@FacesComponent(value = CodeScanner.COMPONENT_TYPE, namespace = CodeScanner.COMPONENT_FAMILY)
@FacesComponentInfo(description = "CodeScanner scans barcode and QR codes using the device camera.")
@ResourceDependency(library = "primefaces", name = "components.css")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = Constants.LIBRARY, name = "codescanner/codescanner.js")
public class CodeScanner extends CodeScannerBaseImpl {

    public static final String STYLE_CLASS = "ui-codescanner ui-widget";
    public static final String EVENT_CODE_SCANNED = "codeScanned";

    public CodeScannerBase.ReaderType getTypeEnum() {
        return CodeScannerBase.ReaderType.valueOf(getType());
    }

    @Override
    public void queueEvent(final FacesEvent event) {
        if (isAjaxBehaviorEventSource(event)) {
            final FacesContext context = event.getFacesContext();
            final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
            final AjaxBehaviorEvent ajaxBehaviorEvent = (AjaxBehaviorEvent) event;

            if (isAjaxBehaviorEvent(event, ClientBehaviorEventKeys.codeScanned)) {
                final Code code = getCode(getClientId(context), params);
                final SelectEvent<Code> selectEvent = new SelectEvent<>(this, ajaxBehaviorEvent.getBehavior(), code);
                selectEvent.setPhaseId(ajaxBehaviorEvent.getPhaseId());
                super.queueEvent(selectEvent);
                return;
            }
        }
        super.queueEvent(event);
    }

    protected static Code getCode(final String clientId, final Map<String, String> params) {
        return new Code(params.get(clientId + "_value"),
                    Format.values()[Integer.parseInt(params.get(clientId + "_format"))]);
    }
}
