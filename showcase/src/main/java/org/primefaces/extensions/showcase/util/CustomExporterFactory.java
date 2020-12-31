/*
 * Copyright 2011-2021 PrimeFaces Extensions
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