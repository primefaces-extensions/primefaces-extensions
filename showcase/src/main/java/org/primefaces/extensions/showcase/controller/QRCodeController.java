/*
 * Copyright 2011-2020 PrimeFaces Extensions
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.primefaces.extensions.showcase.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 * InputNumberController
 *
 * @author Mauricio Fenoglio / last modified by $Author$
 * @version $Revision$
 * @since 0.3
 */
@Named("qrCodeController")
@ViewScoped
public class QRCodeController implements Serializable {

    private static final long serialVersionUID = 20120316L;
    private String renderMethod;
    private String text;
    private String label;
    private int mode;
    private int size;
    private String fillColor;

    public QRCodeController() {
        renderMethod = "canvas";
        text = "http://primefaces-extensions.github.io/";
        label = "PF-Extensions";
        mode = 2;
        fillColor = "7d767d";
        size = 200;
    }

    public String getRenderMethod() {
        return renderMethod;
    }

    public void setRenderMethod(final String renderMethod) {
        this.renderMethod = renderMethod;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(final int mode) {
        this.mode = mode;
    }

    public String getFillColor() {
        return fillColor;
    }

    public void setFillColor(final String fillColor) {
        this.fillColor = fillColor;
    }

    public int getSize() {
        return size;
    }

    public void setSize(final int size) {
        this.size = size;
    }

}
