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
