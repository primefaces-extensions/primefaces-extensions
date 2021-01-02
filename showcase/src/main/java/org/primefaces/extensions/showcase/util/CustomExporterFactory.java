/*
 * Copyright (c) 2011-2021 PrimeFaces Extensions
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
package org.primefaces.extensions.showcase.util;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;

import org.primefaces.extensions.component.exporter.ExcelExporter;
import org.primefaces.extensions.component.exporter.Exporter;
import org.primefaces.extensions.component.exporter.ExporterFactory;
import org.primefaces.extensions.component.exporter.PDFExporter;
import org.primefaces.extensions.showcase.controller.ExporterController;

/**
 * Accessor for objects stored in several scopes via faces context {@link javax.faces.context.FacesContext}.
 *
 * @author Sudheer Jonna / last modified by $Author$
 * @version $Revision$
 */
public class CustomExporterFactory implements ExporterFactory {

    static public enum ExporterType {
        PDF, XLSX
    }

    public Exporter getExporterForType(String type) {

        Exporter exporter = null;

        FacesContext context = FacesContext.getCurrentInstance();
        ExporterController bean = (ExporterController) context.getApplication().evaluateExpressionGet(context, "#{exporterController}",
                    ExporterController.class);
        Boolean customExport = bean.getCustomExporter();

        try {
            ExporterType exporterType = ExporterType.valueOf(type.toUpperCase());

            switch (exporterType) {

                case PDF:
                    if (customExport) {
                        exporter = new PDFCustomExporter();
                    }
                    else {
                        exporter = new PDFExporter();
                    }
                    break;

                case XLSX:
                    if (customExport) {
                        exporter = new ExcelCustomExporter();
                    }
                    else {
                        exporter = new ExcelExporter();
                    }
                    break;

                default: {
                    if (customExport) {
                        exporter = new PDFCustomExporter();
                    }
                    else {
                        exporter = new PDFExporter();
                    }
                    break;
                }

            }
        }
        catch (IllegalArgumentException e) {
            throw new FacesException(e);
        }

        return exporter;
    }

}