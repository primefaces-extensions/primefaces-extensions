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
 * @deprecated use core Primefaces DataExporter
 */
@Deprecated
public class ExporterFactoryProvider {

    private static final String KEY = ExporterFactoryProvider.class.getName();

    private ExporterFactoryProvider() {
        // hide constructor
    }

    @Deprecated
    public static ExporterFactory getExporterFactory(final FacesContext context) {

        ExporterFactory factory = (ExporterFactory) context.getExternalContext().getApplicationMap().get(KEY);

        if (factory == null) {
            final ServiceLoader<ExporterFactory> loader = ServiceLoader.load(ExporterFactory.class);
            final Iterator<ExporterFactory> iterator = loader.iterator();
            if (iterator.hasNext()) {
                factory = iterator.next();
            }

            if (factory == null) {
                factory = new DefaultExporterFactory();
            }

            context.getExternalContext().getApplicationMap().put(KEY, factory);
        }

        return factory;
    }
}

@Deprecated
class DefaultExporterFactory implements ExporterFactory {

    public enum ExporterType {
        PDF, XLSX
    }

    @Override
    public Exporter getExporterForType(final String type) {

        final Exporter exporter;

        try {
            final ExporterType exporterType = ExporterType.valueOf(type.toUpperCase());

            switch (exporterType) {
                case PDF:
                    exporter = new PDFExporter();
                    break;
                case XLSX:
                    exporter = new ExcelExporter();
                    break;
                default:
                    throw new IllegalStateException("Exporter type not supported.");
            }
        }
        catch (final IllegalArgumentException e) {
            throw new FacesException(e);
        }

        return exporter;
    }

}