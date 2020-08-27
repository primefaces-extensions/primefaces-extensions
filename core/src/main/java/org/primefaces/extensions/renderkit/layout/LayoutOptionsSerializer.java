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
package org.primefaces.extensions.renderkit.layout;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

import org.primefaces.extensions.model.layout.LayoutOptions;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * Gson serializer for layout options.
 *
 * @author Oleg Varaksin / last modified by Melloware
 * @since 0.6.0
 */
public class LayoutOptionsSerializer implements JsonSerializer<LayoutOptions> {

    @Override
    public JsonElement serialize(final LayoutOptions src, final Type typeOfSrc, final JsonSerializationContext context) {
        final JsonObject result = new JsonObject();

        final Set<Map.Entry<String, Object>> options = src.getOptions().entrySet();
        for (final Map.Entry<String, Object> entry : options) {
            final Object value = entry.getValue();
            JsonPrimitive jsonPrimitive = null;

            if (value instanceof Boolean) {
                jsonPrimitive = new JsonPrimitive((Boolean) value);
            }
            else if (value instanceof Number) {
                jsonPrimitive = new JsonPrimitive((Number) value);
            }
            else if (value instanceof String) {
                jsonPrimitive = new JsonPrimitive((String) value);
            }

            result.add(entry.getKey(), jsonPrimitive);
        }

        if (src.getPanesOptions() != null) {
            result.add("defaults", context.serialize(src.getPanesOptions(), src.getPanesOptions().getClass()));
        }

        if (src.getTips() != null) {
            result.add("tips", context.serialize(src.getTips(), src.getTips().getClass()));
        }

        if (src.getNorthOptions() != null) {
            result.add("north", context.serialize(src.getNorthOptions(), src.getNorthOptions().getClass()));
        }

        if (src.getSouthOptions() != null) {
            result.add("south", context.serialize(src.getSouthOptions(), src.getSouthOptions().getClass()));
        }

        if (src.getWestOptions() != null) {
            result.add("west", context.serialize(src.getWestOptions(), src.getWestOptions().getClass()));
        }

        if (src.getEastOptions() != null) {
            result.add("east", context.serialize(src.getEastOptions(), src.getEastOptions().getClass()));
        }

        if (src.getCenterOptions() != null) {
            result.add("center", context.serialize(src.getCenterOptions(), src.getCenterOptions().getClass()));
        }

        if (src.getChildOptions() != null) {
            result.add("children", context.serialize(src.getChildOptions(), src.getChildOptions().getClass()));
        }

        return result;
    }

}
