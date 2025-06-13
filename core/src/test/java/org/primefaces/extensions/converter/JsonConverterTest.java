/*
 * Copyright (c) 2011-2025 PrimeFaces Extensions
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
package org.primefaces.extensions.converter;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.*;

import javax.faces.convert.ConverterException;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * JsonConverterTest
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 */
class JsonConverterTest {

    private JsonConverter jsonConverter;

    @BeforeEach
    void createConverter() {
        jsonConverter = new JsonConverter();
    }

    @Test
    void classNotFoundSimple() {
        assertThrows(ConverterException.class, () -> {
            jsonConverter.setType("java.lang.WrongClasDates");
            jsonConverter.getAsString(null, null, "WrongClass");
        });
    }

    @Test
    void classNotFoundComplex() {
        assertThrows(ConverterException.class, () -> {
            jsonConverter.setType("java.lang.wrong.package.String");
            jsonConverter.getAsString(null, null, "WrongPackage");
        });
    }

    @Test
    void classNotFoundGenericSimple() {
        assertThrows(ConverterException.class, () -> {
            jsonConverter.setType("java.util.CrazyCollection<java.lang.Integer>");
            jsonConverter.getAsString(null, null, new ArrayList<Integer>());
        });
    }

    @Test
    void classNotFoundGenericComplex() {
        assertThrows(ConverterException.class, () -> {
            jsonConverter.setType("java.util.Map<java.lang.Integer, java.toolang.Date>");
            jsonConverter.getAsString(null, null, new HashMap<Integer, Date>());
        });
    }

    @Test
    void primitiveTypeArgument() {
        assertThrows(ConverterException.class, () -> {
            jsonConverter.setType("java.util.Map<java.lang.Integer, char>");
            jsonConverter.getAsString(null, null, new HashMap<Integer, Character>());
        });
    }

    @Test
    void classNotFoundArray() {
        assertThrows(ConverterException.class, () -> {
            jsonConverter.setType("java.lang.Abc[]");
            jsonConverter.getAsString(null, null, new Integer[] {1, 2, 3});
        });
    }

    @Test
    void testBoolean() {
        jsonConverter.setType("boolean");

        final String json = jsonConverter.getAsString(null, null, true);
        assertEquals("true", json);

        final Object obj = jsonConverter.getAsObject(null, null, json);
        assertEquals(Boolean.TRUE, obj);
    }

    @Test
    void testInt() {
        jsonConverter.setType("int");

        final String json = jsonConverter.getAsString(null, null, 5);
        assertEquals("5", json);

        final Object obj = jsonConverter.getAsObject(null, null, json);
        assertEquals(5, obj);
    }

    @Test
    void testDouble() {
        jsonConverter.setType("double");

        final String json = jsonConverter.getAsString(null, null, 10.99);
        assertEquals("10.99", json);

        final Object obj = jsonConverter.getAsObject(null, null, json);
        assertEquals(10.99, obj);
    }

    @Test
    void intArray() {
        jsonConverter.setType("int[]");

        final int[] ints = {1, 2, 3, 4, 5};
        final String json = jsonConverter.getAsString(null, null, ints);
        assertEquals("[1,2,3,4,5]", json);

        final Object obj = jsonConverter.getAsObject(null, null, json);
        assertArrayEquals(ints, (int[]) obj);
    }

    @Test
    void longArray() {
        jsonConverter.setType("long[]");

        final long[] longs = {100, 255, 399, 401, 59999};
        final String json = jsonConverter.getAsString(null, null, longs);
        assertEquals("[100,255,399,401,59999]", json);

        final Object obj = jsonConverter.getAsObject(null, null, json);
        assertArrayEquals(longs, (long[]) obj);
    }

    @Test
    void stringArray() {
        jsonConverter.setType("java.lang.String[]");

        final String[] strings = {"abc", "def", "ghi"};
        final String json = jsonConverter.getAsString(null, null, strings);

        final Object obj = jsonConverter.getAsObject(null, null, json);
        assertArrayEquals(strings, (String[]) obj);
    }

    @Test
    void string() {
        jsonConverter.setType("java.lang.String");

        final String string = "Hello World";
        final String json = jsonConverter.getAsString(null, null, string);
        assertEquals("\"Hello World\"", json);

        final Object obj = jsonConverter.getAsObject(null, null, json);
        assertEquals(string, obj);
    }

    @Test
    void integer() {
        jsonConverter.setType("java.lang.Integer");

        final Integer integer = 60;
        final String json = jsonConverter.getAsString(null, null, integer);
        assertEquals("60", json);

        final Object obj = jsonConverter.getAsObject(null, null, json);
        assertEquals(integer, obj);
    }

    @Test
    void date() {
        jsonConverter.setType("java.util.Date");

        final Date now = new Date();
        final String json = jsonConverter.getAsString(null, null, now);
        assertEquals(now.getTime() + "", json);

        final Object obj = jsonConverter.getAsObject(null, null, json);
        assertEquals(now, obj);
    }

    @Test
    void collection() {
        jsonConverter.setType("java.util.Collection<java.lang.Integer>");

        final Collection<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        final String json = jsonConverter.getAsString(null, null, list);
        assertEquals("[1,2,3]", json);

        final Object obj = jsonConverter.getAsObject(null, null, json);
        assertTrue(CollectionUtils.isEqualCollection(list, (Collection<Integer>) obj));
    }

    @Test
    void complexMap() {
        jsonConverter.setType(
                    "java.util.Map<java.lang.String, org.apache.commons.lang3.tuple.ImmutablePair<java.lang.Integer, java.util.Date>>");

        final Map<String, ImmutablePair<Integer, Date>> map = new HashMap<>();
        GregorianCalendar calendar = new GregorianCalendar(2012, Calendar.FEBRUARY, 20);
        map.put("cat", new ImmutablePair<>(1, calendar.getTime()));
        calendar = new GregorianCalendar(2011, Calendar.JULY, 1);
        map.put("dog", new ImmutablePair<>(2, calendar.getTime()));
        calendar = new GregorianCalendar(1999, Calendar.NOVEMBER, 15);
        map.put("unknow", new ImmutablePair<>(3, calendar.getTime()));

        final String json = jsonConverter.getAsString(null, null, map);

        final Object obj = jsonConverter.getAsObject(null, null, json);
        assertTrue(CollectionUtils.isEqualCollection(map.entrySet(),
                    ((Map<String, ImmutablePair<Integer, Date>>) obj).entrySet()));
    }

    @Test
    void pojoNonGeneric() {
        jsonConverter.setType("org.primefaces.extensions.converter.FooNonGeneric");

        final FooNonGeneric fooNonGeneric = new FooNonGeneric();
        final String json = jsonConverter.getAsString(null, null, fooNonGeneric);

        final Object obj = jsonConverter.getAsObject(null, null, json);
        assertEquals(fooNonGeneric, obj);
    }

    @Test
    void pojoGenericSimple() {
        jsonConverter.setType("org.primefaces.extensions.converter.FooGeneric<java.lang.String, java.lang.Integer>");

        final FooGeneric<String, Integer> fooGeneric = new FooGeneric<>();
        fooGeneric.setA("test");
        fooGeneric.setB(25);

        final String json = jsonConverter.getAsString(null, null, fooGeneric);

        final Object obj = jsonConverter.getAsObject(null, null, json);
        assertEquals(fooGeneric, obj);
    }

    @Test
    void pojoGenericComplex() {
        jsonConverter.setType(
                    "org.primefaces.extensions.converter.FooGeneric<int[], "
                                + "org.primefaces.extensions.converter.FooGeneric<org.primefaces.extensions.converter.FooNonGeneric, java.lang.Boolean>>");

        final FooNonGeneric fooNonGeneric = new FooNonGeneric();
        final FooGeneric<FooNonGeneric, Boolean> fooGenericInnner = new FooGeneric<>();
        fooGenericInnner.setA(fooNonGeneric);
        fooGenericInnner.setB(false);

        final FooGeneric<int[], FooGeneric<FooNonGeneric, Boolean>> fooGenericOuter = new FooGeneric<>();
        final int[] ints = {1, 2, 3, 4, 5};
        fooGenericOuter.setA(ints);
        fooGenericOuter.setB(fooGenericInnner);

        final String json = jsonConverter.getAsString(null, null, fooGenericOuter);

        final Object obj = jsonConverter.getAsObject(null, null, json);
        assertEquals(fooGenericOuter, obj);
    }
}