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

package org.primefaces.extensions.converter;

import java.util.Date;
import java.util.TimeZone;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;

import org.primefaces.extensions.util.json.DateTypeAdapter;

import com.google.gson.GsonBuilder;

/**
 * JSON converter for timeline events. Can be used with client-side API of the
 * {@link org.primefaces.extensions.component.timeline.Timeline} component.
 *
 * @author  Oleg Varaksin / last modified by $Author: $
 * @version $Revision: 1.0 $
 * @since   0.7
 */
@FacesConverter(value = "org.primefaces.extensions.converter.TimelineEventsConverter")
public class TimelineEventsConverter extends JsonConverter {

	private static final long serialVersionUID = 20130417L;

	private static final String TYPE_FOR_EVENTS = "java.util.List<org.primefaces.extensions.model.timeline.TimelineEvent>";

	private Object timeZone;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		// register a time zone aware date adapter
		GsonBuilder gsonBilder = new GsonBuilder();
		gsonBilder.registerTypeAdapter(Date.class, new DateTypeAdapter(timeZone == null ? TimeZone.getDefault() : timeZone));
		gsonBilder.serializeNulls();

		return gsonBilder.create().fromJson(value, getObjectType(TYPE_FOR_EVENTS, false));
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		// register a time zone aware date adapter
		GsonBuilder gsonBilder = new GsonBuilder();
		gsonBilder.registerTypeAdapter(Date.class, new DateTypeAdapter(timeZone == null ? TimeZone.getDefault() : timeZone));
		gsonBilder.serializeNulls();

		return gsonBilder.create().toJson(value, getObjectType(TYPE_FOR_EVENTS, false));
	}

	public Object getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(Object timeZone) {
		this.timeZone = timeZone;
	}
}
