/*
 * Copyright 2014 PrimeFaces Extensions.
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
 *
 * $Id$
 */
package org.primefaces.extensions.util;

import java.io.IOException;

import javax.faces.context.FacesContext;
import org.primefaces.util.WidgetBuilder;

public class ExtWidgetBuilder extends WidgetBuilder {

	private static final String KEY = ExtWidgetBuilder.class.getName();

	public static ExtWidgetBuilder get(final FacesContext context) {

		ExtWidgetBuilder wb = (ExtWidgetBuilder) context.getExternalContext().getRequestMap().get(KEY);

		if (wb == null) {
			wb = new ExtWidgetBuilder(context);

			context.getExternalContext().getRequestMap().put(KEY, wb);
		}

		return wb;
	}
    
    public ExtWidgetBuilder(FacesContext context) {
        super(context);
    }

    protected WidgetBuilder init(String widgetClass, String widgetVar, String id, String resourcePath, boolean endFunction) throws IOException {
    	this.resourcePath = resourcePath;
    	this.endFunction = endFunction;
    	
        context.getResponseWriter().write("PrimeFacesExt.cw(\"");
        context.getResponseWriter().write(widgetClass);
        context.getResponseWriter().write("\",\"");
        context.getResponseWriter().write(widgetVar);
        context.getResponseWriter().write("\",{");
        context.getResponseWriter().write("id:\"");
        context.getResponseWriter().write(id);
        if (widgetVar == null) {
        	context.getResponseWriter().write("\"");
        } else {
	        context.getResponseWriter().write("\",widgetVar:\"");
	        context.getResponseWriter().write(widgetVar);
	        context.getResponseWriter().write("\"");
        }

        return this;
    }
}
