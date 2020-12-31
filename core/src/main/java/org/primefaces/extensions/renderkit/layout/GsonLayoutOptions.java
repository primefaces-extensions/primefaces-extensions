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
package org.primefaces.extensions.renderkit.layout;

import org.primefaces.extensions.model.layout.LayoutOptions;

/**
 * Singleton instance of Gson to convert layout options.
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 */
public class GsonLayoutOptions {

    private static final GsonLayoutOptions INSTANCE = new GsonLayoutOptions();
    private final com.google.gson.Gson gson;

    private GsonLayoutOptions() {
        final com.google.gson.GsonBuilder gsonBilder = new com.google.gson.GsonBuilder();
        gsonBilder.registerTypeAdapter(LayoutOptions.class, new LayoutOptionsSerializer());
        gson = gsonBilder.create();
    }

    public static com.google.gson.Gson getGson() {
        return INSTANCE.gson;
    }
}
