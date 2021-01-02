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
package org.primefaces.extensions.util.json;

import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Singleton instance of Gson which exludes fields without @Expose annotation.
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since 1.1.0
 */
public final class GsonExposeAwareConverter {

    private static final GsonExposeAwareConverter INSTANCE = new GsonExposeAwareConverter();
    private final Gson gson;

    private GsonExposeAwareConverter() {
        final GsonBuilder gsonBilder = new GsonBuilder();

        gsonBilder.registerTypeAdapter(Date.class, new DateTypeAdapter());
        gsonBilder.serializeNulls();
        gsonBilder.excludeFieldsWithoutExposeAnnotation();

        gson = gsonBilder.create();
    }

    public static Gson getGson() {
        return INSTANCE.gson;
    }
}
