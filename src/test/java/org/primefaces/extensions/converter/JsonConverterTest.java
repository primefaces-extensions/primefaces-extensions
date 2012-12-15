/*
 * Copyright 2011-2012 PrimeFaces Extensions.
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.faces.convert.ConverterException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 * JsonConverterTest
 *
 * @author  Oleg Varaksin / last modified by $Author$
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

	@Test
	public void testBoolean() {
		jsonConverter.setType("boolean");

		String json = jsonConverter.getAsString(null, null, true);
		assertEquals("true", json);

		Object obj = jsonConverter.getAsObject(null, null, json);
		assertEquals(true, obj);
	}

	@Test
	public void testInt() {
		jsonConverter.setType("int");

		String json = jsonConverter.getAsString(null, null, 5);
		assertEquals("5", json);

		Object obj = jsonConverter.getAsObject(null, null, json);
		assertEquals(5, obj);
	}

	@Test
	public void testDouble() {
		jsonConverter.setType("double");

		String json = jsonConverter.getAsString(null, null, 10.99);
		assertEquals("10.99", json);

		Object obj = jsonConverter.getAsObject(null, null, json);
		assertEquals(10.99, obj);
	}

	@Test
	public void testIntArray() {
		jsonConverter.setType("int[]");

		int[] ints = {1, 2, 3, 4, 5};
		String json = jsonConverter.getAsString(null, null, ints);
		assertEquals("[1,2,3,4,5]", json);

		Object obj = jsonConverter.getAsObject(null, null, json);
		assertTrue(Arrays.equals(ints, (int[]) obj));
	}

	@Test
	public void testLongArray() {
		jsonConverter.setType("long[]");

		long[] longs = {100, 255, 399, 401, 59999};
		String json = jsonConverter.getAsString(null, null, longs);
		assertEquals("[100,255,399,401,59999]", json);

		Object obj = jsonConverter.getAsObject(null, null, json);
		assertTrue(Arrays.equals(longs, (long[]) obj));
	}

	@Test
	public void testString() {
		jsonConverter.setType("java.lang.String");

		String string = "Hello World";
		String json = jsonConverter.getAsString(null, null, string);
		assertEquals("\"Hello World\"", json);

		Object obj = jsonConverter.getAsObject(null, null, json);
		assertEquals(string, obj);
	}

	@Test
	public void testInteger() {
		jsonConverter.setType("java.lang.Integer");

		Integer integer = 60;
		String json = jsonConverter.getAsString(null, null, integer);
		assertEquals("60", json);

		Object obj = jsonConverter.getAsObject(null, null, json);
		assertEquals(integer, obj);
	}

	@Test
	public void testDate() {
		jsonConverter.setType("java.util.Date");

		Date now = new Date();
		String json = jsonConverter.getAsString(null, null, now);
		assertEquals(now.getTime() + "", json);

		Object obj = jsonConverter.getAsObject(null, null, json);
		assertEquals(now, obj);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testCollection() {
		jsonConverter.setType("java.util.Collection<java.lang.Integer>");

		Collection<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);

		String json = jsonConverter.getAsString(null, null, list);
		assertEquals("[1,2,3]", json);

		Object obj = jsonConverter.getAsObject(null, null, json);
		assertTrue(CollectionUtils.isEqualCollection(list, (Collection<Integer>) obj));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testComplexMap() {
		jsonConverter.setType(
		    "java.util.Map<java.lang.String, org.apache.commons.lang3.tuple.ImmutablePair<java.lang.Integer, java.util.Date>>");

		Map<String, ImmutablePair<Integer, Date>> map = new HashMap<String, ImmutablePair<Integer, Date>>();
		GregorianCalendar calendar = new GregorianCalendar(2012, 1, 20);
		map.put("cat", new ImmutablePair<Integer, Date>(1, calendar.getTime()));
		calendar = new GregorianCalendar(2011, 6, 1);
		map.put("dog", new ImmutablePair<Integer, Date>(2, calendar.getTime()));
		calendar = new GregorianCalendar(1999, 10, 15);
		map.put("unknow", new ImmutablePair<Integer, Date>(3, calendar.getTime()));

		String json = jsonConverter.getAsString(null, null, map);

		Object obj = jsonConverter.getAsObject(null, null, json);
		assertTrue(CollectionUtils.isEqualCollection(map.entrySet(),
		                                             ((Map<String, ImmutablePair<Integer, Date>>) obj).entrySet()));
	}

	@Test
	public void testPojoNonGeneric() {
		jsonConverter.setType("org.primefaces.extensions.converter.FooNonGeneric");

		FooNonGeneric fooNonGeneric = new FooNonGeneric();
		String json = jsonConverter.getAsString(null, null, fooNonGeneric);

		Object obj = jsonConverter.getAsObject(null, null, json);
		assertTrue(fooNonGeneric.equals(obj));
	}

	@Test
	public void testPojoGenericSimple() {
		jsonConverter.setType("org.primefaces.extensions.converter.FooGeneric<java.lang.String, java.lang.Integer>");

		FooGeneric<String, Integer> fooGeneric = new FooGeneric<String, Integer>();
		fooGeneric.setA("test");
		fooGeneric.setB(25);

		String json = jsonConverter.getAsString(null, null, fooGeneric);

		Object obj = jsonConverter.getAsObject(null, null, json);
		assertTrue(fooGeneric.equals(obj));
	}

	@Test
	public void testPojoGenericComplex() {
		jsonConverter.setType(
		    "org.primefaces.extensions.converter.FooGeneric<int[], org.primefaces.extensions.converter.FooGeneric<org.primefaces.extensions.converter.FooNonGeneric, java.lang.Boolean>>");

		FooNonGeneric fooNonGeneric = new FooNonGeneric();
		FooGeneric<FooNonGeneric, Boolean> fooGenericInnner = new FooGeneric<FooNonGeneric, Boolean>();
		fooGenericInnner.setA(fooNonGeneric);
		fooGenericInnner.setB(false);

		FooGeneric<int[], FooGeneric<FooNonGeneric, Boolean>> fooGenericOuter =
		    new FooGeneric<int[], FooGeneric<FooNonGeneric, Boolean>>();
		int[] ints = {1, 2, 3, 4, 5};
		fooGenericOuter.setA(ints);
		fooGenericOuter.setB(fooGenericInnner);

		String json = jsonConverter.getAsString(null, null, fooGenericOuter);

		Object obj = jsonConverter.getAsObject(null, null, json);
		assertTrue(fooGenericOuter.equals(obj));
	}
}
