/*
 * Copyright (c) 2011-2026 PrimeFaces Extensions
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
package org.primefaces.extensions.showcase.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.primefaces.cache.CacheProvider;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

/**
 * {@link CacheProvider} backed by Caffeine, used by {@link ShowcaseCacheProvider}. Each region is its own size-bounded Caffeine cache.
 */
public class CaffeineCacheProvider implements CacheProvider {

    private static final long MAX_ENTRIES_PER_REGION = 1_000;

    private final Map<String, Cache<String, Object>> regions = new ConcurrentHashMap<>();

    @Override
    public Object get(String region, String key) {
        Cache<String, Object> cache = regions.get(region);
        return cache == null ? null : cache.getIfPresent(key);
    }

    @Override
    public void put(String region, String key, Object object) {
        regions.computeIfAbsent(region, r -> Caffeine.newBuilder().maximumSize(MAX_ENTRIES_PER_REGION).build()).put(key, object);
    }

    @Override
    public void remove(String region, String key) {
        Cache<String, Object> cache = regions.get(region);
        if (cache != null) {
            cache.invalidate(key);
        }
    }

    @Override
    public void clear() {
        regions.values().forEach(Cache::invalidateAll);
        regions.clear();
    }
}
