/**
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
package org.primefaces.extensions.util.json;

import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Singleton instance of Gson.
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since 0.2
 */
public final class GsonConverter {

    private static final GsonConverter INSTANCE = new GsonConverter();
    private final Gson gson;

    private GsonConverter() {
        final GsonBuilder gsonBilder = new GsonBuilder();

        gsonBilder.registerTypeAdapter(Date.class, new DateTypeAdapter());
        gsonBilder.serializeNulls();

        gson = gsonBilder.create();
    }

    public static Gson getGson() {
        return INSTANCE.gson;
    }
}
