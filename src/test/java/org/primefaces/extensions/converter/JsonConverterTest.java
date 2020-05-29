/*
 * Copyright 2011-2015 PrimeFaces Extensions
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
 *
 * $Id$
 */

package org.primefaces.extensions.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.*;

import javax.faces.convert.ConverterException;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.Before;
import org.junit.Test;

/**
 * JsonConverterTest
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 */
public class JsonConverterTest {

    private JsonConverter jsonConverter;

    @Before
    public void createConverter() {
        jsonConverter = new JsonConverter();
    }

    @Test(expected = ConverterException.class)
    public void testClassNotFoundSimple() {
        jsonConverter.setType("java.lang.WrongClasDates");
        jsonConverter.getAsString(null, null, "WrongClass");
    }

    @Test(expected = ConverterException.class)
    public void testClassNotFoundComplex() {
        jsonConverter.setType("java.lang.wrong.package.String");
        jsonConverter.getAsString(null, null, "WrongPackage");
    }

    @Test(expected = ConverterException.class)
    public void testClassNotFoundGenericSimple() {
        jsonConverter.setType("java.util.CrazyCollection<java.lang.Integer>");
        jsonConverter.getAsString(null, null, new ArrayList<Integer>());
    }

    @Test(expected = ConverterException.class)
    public void testClassNotFoundGenericComplex() {
        jsonConverter.setType("java.util.Map<java.lang.Integer, java.toolang.Date>");
        jsonConverter.getAsString(null, null, new HashMap<Integer, Date>());
    }

    @Test(expected = ConverterException.class)
    public void testPrimitiveTypeArgument() {
        jsonConverter.setType("java.util.Map<java.lang.Integer, char>");
        jsonConverter.getAsString(null, null, new HashMap<Integer, Character>());
    }

    @Test(expected = ConverterException.class)
    public void testClassNotFoundArray() {
        jsonConverter.setType("java.lang.Abc[]");
        jsonConverter.getAsString(null, null, new Integer[] {1, 2, 3});
    }

    @Test
    public void testBoolean() {
        jsonConverter.setType("boolean");

        final String json = jsonConverter.getAsString(null, null, true);
        assertEquals("true", json);

        final Object obj = jsonConverter.getAsObject(null, null, json);
        assertEquals(true, obj);
    }

    @Test
    public void testInt() {
        jsonConverter.setType("int");

        final String json = jsonConverter.getAsString(null, null, 5);
        assertEquals("5", json);

        final Object obj = jsonConverter.getAsObject(null, null, json);
        assertEquals(5, obj);
    }

    @Test
    public void testDouble() {
        jsonConverter.setType("double");

        final String json = jsonConverter.getAsString(null, null, 10.99);
        assertEquals("10.99", json);

        final Object obj = jsonConverter.getAsObject(null, null, json);
        assertEquals(10.99, obj);
    }

    @Test
    public void testIntArray() {
        jsonConverter.setType("int[]");

        final int[] ints = {1, 2, 3, 4, 5};
        final String json = jsonConverter.getAsString(null, null, ints);
        assertEquals("[1,2,3,4,5]", json);

        final Object obj = jsonConverter.getAsObject(null, null, json);
        assertTrue(Arrays.equals(ints, (int[]) obj));
    }

    @Test
    public void testLongArray() {
        jsonConverter.setType("long[]");

        final long[] longs = {100, 255, 399, 401, 59999};
        final String json = jsonConverter.getAsString(null, null, longs);
        assertEquals("[100,255,399,401,59999]", json);

        final Object obj = jsonConverter.getAsObject(null, null, json);
        assertTrue(Arrays.equals(longs, (long[]) obj));
    }

    @Test
    public void testStringArray() {
        jsonConverter.setType("java.lang.String[]");

        final String[] strings = {"abc", "def", "ghi"};
        final String json = jsonConverter.getAsString(null, null, strings);

        final Object obj = jsonConverter.getAsObject(null, null, json);
        assertTrue(Arrays.equals(strings, (String[]) obj));
    }

    @Test
    public void testString() {
        jsonConverter.setType("java.lang.String");

        final String string = "Hello World";
        final String json = jsonConverter.getAsString(null, null, string);
        assertEquals("\"Hello World\"", json);

        final Object obj = jsonConverter.getAsObject(null, null, json);
        assertEquals(string, obj);
    }

    @Test
    public void testInteger() {
        jsonConverter.setType("java.lang.Integer");

        final Integer integer = 60;
        final String json = jsonConverter.getAsString(null, null, integer);
        assertEquals("60", json);

        final Object obj = jsonConverter.getAsObject(null, null, json);
        assertEquals(integer, obj);
    }

    @Test
    public void testDate() {
        jsonConverter.setType("java.util.Date");

        final Date now = new Date();
        final String json = jsonConverter.getAsString(null, null, now);
        assertEquals(now.getTime() + "", json);

        final Object obj = jsonConverter.getAsObject(null, null, json);
        assertEquals(now, obj);
    }

    @Test
    public void testCollection() {
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
    public void testComplexMap() {
        jsonConverter.setType(
                    "java.util.Map<java.lang.String, org.apache.commons.lang3.tuple.ImmutablePair<java.lang.Integer, java.util.Date>>");

        final Map<String, ImmutablePair<Integer, Date>> map = new HashMap<>();
        GregorianCalendar calendar = new GregorianCalendar(2012, 1, 20);
        map.put("cat", new ImmutablePair<>(1, calendar.getTime()));
        calendar = new GregorianCalendar(2011, 6, 1);
        map.put("dog", new ImmutablePair<>(2, calendar.getTime()));
        calendar = new GregorianCalendar(1999, 10, 15);
        map.put("unknow", new ImmutablePair<>(3, calendar.getTime()));

        final String json = jsonConverter.getAsString(null, null, map);

        final Object obj = jsonConverter.getAsObject(null, null, json);
        assertTrue(CollectionUtils.isEqualCollection(map.entrySet(),
                    ((Map<String, ImmutablePair<Integer, Date>>) obj).entrySet()));
    }

    @Test
    public void testPojoNonGeneric() {
        jsonConverter.setType("org.primefaces.extensions.converter.FooNonGeneric");

        final FooNonGeneric fooNonGeneric = new FooNonGeneric();
        final String json = jsonConverter.getAsString(null, null, fooNonGeneric);

        final Object obj = jsonConverter.getAsObject(null, null, json);
        assertEquals(fooNonGeneric, obj);
    }

    @Test
    public void testPojoGenericSimple() {
        jsonConverter.setType("org.primefaces.extensions.converter.FooGeneric<java.lang.String, java.lang.Integer>");

        final FooGeneric<String, Integer> fooGeneric = new FooGeneric<>();
        fooGeneric.setA("test");
        fooGeneric.setB(25);

        final String json = jsonConverter.getAsString(null, null, fooGeneric);

        final Object obj = jsonConverter.getAsObject(null, null, json);
        assertEquals(fooGeneric, obj);
    }

    @Test
    public void testPojoGenericComplex() {
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
