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
package org.primefaces.extensions.showcase.util.taglib;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import jakarta.faces.application.Application;
import jakarta.faces.application.ApplicationWrapper;
import jakarta.faces.component.FacesComponent;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.ExternalContextWrapper;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.FacesContextWrapper;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.faces.render.RenderKit;
import jakarta.faces.render.RenderKitWrapper;
import jakarta.faces.render.Renderer;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TagLibProviderTest {

    private static final Map<String, Class<?>> COMPONENTS = new HashMap<>();
    private static final Map<String, Class<?>> CONVERTERS = new HashMap<>();
    private static TagLibProvider provider;
    private static final Map<String, Object> appMap = new HashMap<>();

    /** Minimal FacesContext: resolves component/converter types by scanning the PFE core jar for annotations. */
    private static final class StubContext extends FacesContextWrapper {

        StubContext() {
            super(null);
            FacesContext.setCurrentInstance(this);
        }

        static void unset() {
            FacesContext.setCurrentInstance(null);
        }

        @Override
        public FacesContext getWrapped() {
            return null;
        }

        @Override
        public Application getApplication() {
            return new ApplicationWrapper() {
                @Override
                public Application getWrapped() {
                    return null;
                }

                @Override
                public UIComponent createComponent(final String componentType) {
                    return (UIComponent) newInstance(COMPONENTS.get(componentType));
                }

                @Override
                public Converter<?> createConverter(final String converterId) {
                    return (Converter<?>) newInstance(CONVERTERS.getOrDefault(converterId, classForName(converterId)));
                }
            };
        }

        @Override
        public ExternalContext getExternalContext() {
            return new ExternalContextWrapper() {
                @Override
                public ExternalContext getWrapped() {
                    return null;
                }

                @Override
                public Map<String, Object> getApplicationMap() {
                    return appMap;
                }
            };
        }

        @Override
        public RenderKit getRenderKit() {
            return new RenderKitWrapper() {
                @Override
                public RenderKit getWrapped() {
                    return null;
                }

                @Override
                public Renderer<?> getRenderer(final String family, final String rendererType) {
                    return null;
                }
            };
        }
    }

    private static Class<?> classForName(final String name) {
        try {
            return Class.forName(name);
        }
        catch (final ClassNotFoundException e) {
            return null;
        }
    }

    private static Object newInstance(final Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        }
        catch (final ReflectiveOperationException e) {
            throw new IllegalStateException(clazz.getName(), e);
        }
    }

    private static void scanCoreJar() throws Exception {
        final Path jar = Path.of(org.primefaces.extensions.component.sheet.Sheet.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI());
        final File f = jar.toFile();
        if (f.isDirectory()) {
            try (var walk = java.nio.file.Files.walk(jar)) {
                for (final Path p : (Iterable<Path>) walk.filter(x -> x.toString().endsWith(".class"))::iterator) {
                    register(jar.relativize(p).toString().replace(File.separatorChar, '/'));
                }
            }
            return;
        }
        try (JarFile jf = new JarFile(f)) {
            final Enumeration<JarEntry> en = jf.entries();
            while (en.hasMoreElements()) {
                register(en.nextElement().getName());
            }
        }
    }

    private static void register(final String entryName) {
        if (!entryName.endsWith(".class") || entryName.contains("$")) {
            return;
        }
        try {
            final Class<?> c = Class.forName(entryName.substring(0, entryName.length() - 6).replace('/', '.'), false,
                        TagLibProviderTest.class.getClassLoader());
            final FacesComponent fc = c.getAnnotation(FacesComponent.class);
            if (fc != null && !fc.value().isEmpty()) {
                COMPONENTS.put(fc.value(), c);
            }
            final FacesConverter conv = c.getAnnotation(FacesConverter.class);
            if (conv != null && !conv.value().isEmpty()) {
                CONVERTERS.put(conv.value(), c);
            }
        }
        catch (final Throwable t) {
            // not loadable standalone, not a component
        }
    }

    @BeforeAll
    static void init() throws Exception {
        scanCoreJar();
        new StubContext();
        try {
            provider = new TagLibProvider();
            provider.init();
        }
        finally {
            StubContext.unset();
        }
    }

    @AfterAll
    static void cleanup() {
        StubContext.unset();
    }

    @Test
    void parsesPrimeFacesExtensionsTags() {
        final Map<String, TagInfo> tags = provider.getTags();
        for (final String name : List.of("sheet", "sheetcolumn", "monacoEditor", "markText", "layout", "layoutPane", "timePicker")) {
            assertTrue(tags.containsKey(name), "missing tag " + name);
        }
        assertTrue(tags.size() >= 60, "tags: " + tags.size());
    }

    @Test
    void sheetHasClientBehaviorEvents() {
        assertFalse(provider.getTags().get("sheet").getClientBehaviorsEvents().isEmpty());
    }

    @Test
    void timePickerHasValueProperty() {
        assertTrue(provider.getTags().get("timePicker").getProperties().stream().anyMatch(k -> "value".equals(k.getName())));
    }

    @Test
    void noFunctionsSoSectionIsHidden() {
        assertTrue(provider.getFunctionsSorted().isEmpty());
    }
}
