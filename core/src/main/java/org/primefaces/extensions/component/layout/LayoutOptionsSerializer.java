/*
 * Copyright (c) 2011-2022 PrimeFaces Extensions
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
package org.primefaces.extensions.component.layout;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

import org.primefaces.extensions.model.layout.LayoutOptions;

import com.google.gson.*;

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
