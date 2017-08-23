/**
 * Copyright 2011-2017 PrimeFaces Extensions
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

import java.util.Iterator;
import java.util.ServiceLoader;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;

/**
 * <code>Exporter</code> component.
 *
 * @author Sudheer Jonna / last modified by $Author$
 * @since 0.7.0
 */
public class ExporterFactoryProvider {

    private static final String KEY = ExporterFactoryProvider.class.getName();

    public static ExporterFactory getExporterFactory(FacesContext context) {

        ExporterFactory factory = (ExporterFactory) context.getExternalContext().getApplicationMap().get(KEY);

        if (factory == null) {
            ServiceLoader<ExporterFactory> loader = ServiceLoader.load(ExporterFactory.class);
            if (loader != null) {
               Iterator<ExporterFactory> iterator = loader.iterator();
               if (iterator.hasNext()) {
                  factory = iterator.next();
               }
            }

            if (factory == null) {
                factory = new DefaultExporterFactory();
            }

            context.getExternalContext().getApplicationMap().put(KEY, factory);
        }

        return factory;
    }
}

class DefaultExporterFactory implements ExporterFactory {

    static public enum ExporterType {
        PDF,
        XLSX
    }

    public Exporter getExporterForType(String type) {

        Exporter exporter = null;

        try {
            ExporterType exporterType = ExporterType.valueOf(type.toUpperCase());

            switch (exporterType) {

                case PDF:
                    exporter = new PDFExporter();
                    break;

                case XLSX:
                    exporter = new ExcelExporter();
                    break;

                default: {
                    exporter = new PDFExporter();
                    break;
                }

            }
        } catch (IllegalArgumentException e) {
            throw new FacesException(e);
        }

        return exporter;
    }

}