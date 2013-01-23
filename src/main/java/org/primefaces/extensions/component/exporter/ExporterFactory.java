/*
 * Copyright 2011-2013 PrimeFaces Extensions.
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
package org.primefaces.extensions.component.exporter;

import javax.faces.FacesException;
/**
 * <code>Exporter</code> component.
 *
 * @author  Sudheer Jonna / last modified by $Author$
 * @since   0.7.0
 */
public class ExporterFactory {

    static public enum ExporterType {
        PDF,
        XLS
    }

    public static Exporter getExporterForType(String type) {
        Exporter exporter = null;
        
        try {
            ExporterType exporterType = ExporterType.valueOf(type.toUpperCase());

            switch(exporterType) {

                case PDF:
                    exporter = new PDFExporter();
                    break;

//                case XLS:
//                    exporter = new ExcelExporter();
//                break;

                default:
                {
                    exporter = new PDFExporter();
                    break;
                }

            }
        }
        catch(IllegalArgumentException e) {
            throw new FacesException(e);
        } 
        
        return exporter;
	}
}
