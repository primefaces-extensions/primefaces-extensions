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
 */

package org.primefaces.extensions.showcase.controller.jsonconverter;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.primefaces.extensions.showcase.model.jsonconverter.FooGeneric;
import org.primefaces.extensions.showcase.model.jsonconverter.FooNonGeneric;
import org.primefaces.extensions.util.RequestParameterBuilder;

/**
 * ViewParamJsonController
 *
 * @author Oleg Varaksin / last modified by $Author: $
 * @version $Revision: 1.0 $
 */
@Named
@ViewScoped
public class ViewParamJsonController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private TypesJsonController typesJsonController;

	private boolean b;
	private long l;
	private int[] ints;
	private char[] chars;
	private String s;
	private Integer i;
	private Date d;

	private Collection<Integer> list = new ArrayList<Integer>();
	private Map<String, ImmutablePair<Integer, Date>> map = new HashMap<String, ImmutablePair<Integer, Date>>();

	private FooNonGeneric fooNonGeneric = new FooNonGeneric();
	private FooGeneric<String, Integer> fooGenericSimple = new FooGeneric<String, Integer>();
	private FooGeneric<int[], FooGeneric<FooNonGeneric, Boolean>> fooGenericComplex = new FooGeneric<int[], FooGeneric<FooNonGeneric, Boolean>>();

	private String generatedUrl;

	@PostConstruct
	protected void initialize() {
		// creates a builder instance by the current request URL
		final RequestParameterBuilder rpBuilder = new RequestParameterBuilder(true);

		try {
			rpBuilder.param("b", true);
			rpBuilder.param("l", 5000L);
			rpBuilder.paramJson("ints", new int[] { 1, 2, 3, 4, 5 });
			rpBuilder.paramJson("chars", new char[] { 'x', 'y', 'z' });
			rpBuilder.paramJson("s", "Hallo World");
			rpBuilder.param("i", 99);
			rpBuilder.paramJson("d", new Date());

			final Collection<Integer> list = new ArrayList<Integer>();
			list.add(11);
			list.add(22);
			list.add(33);

			rpBuilder.paramJson("list", list, typesJsonController.getTypeGenericList());

			final Map<String, ImmutablePair<Integer, Date>> map = new HashMap<String, ImmutablePair<Integer, Date>>();
			GregorianCalendar calendar = new GregorianCalendar(2012, 1, 20);
			map.put("cat", new ImmutablePair<Integer, Date>(1, calendar.getTime()));
			calendar = new GregorianCalendar(2011, 6, 1);
			map.put("dog", new ImmutablePair<Integer, Date>(2, calendar.getTime()));
			calendar = new GregorianCalendar(1999, 10, 15);
			map.put("unknow", new ImmutablePair<Integer, Date>(3, calendar.getTime()));

			rpBuilder.paramJson("map", map, typesJsonController.getTypeGenericMap());

			final FooNonGeneric fooNonGeneric = new FooNonGeneric();
			fooNonGeneric.setCount(1001);
			fooNonGeneric.setStartDate(new Date());
			fooNonGeneric.setEndDate(new Date(fooNonGeneric.getStartDate().getTime() + 360000));
			fooNonGeneric.addMessage("Message string 1");
			fooNonGeneric.addMessage("Message string 2");

			rpBuilder.paramJson("fooNonGeneric", fooNonGeneric);

			final FooGeneric<String, Integer> fooGenericSimple = new FooGeneric<String, Integer>();
			fooGenericSimple.setA("test");
			fooGenericSimple.setB(25);

			rpBuilder.paramJson("fooGenericSimple", fooGenericSimple, typesJsonController.getTypeGenericSimple());

			final FooGeneric<int[], FooGeneric<FooNonGeneric, Boolean>> fooGenericComplex = new FooGeneric<int[], FooGeneric<FooNonGeneric, Boolean>>();
			final FooNonGeneric fooNonGeneric2 = new FooNonGeneric();
			fooNonGeneric2.setCount(909);
			fooNonGeneric2.setStartDate(new Date());
			fooNonGeneric2.setEndDate(new Date(fooNonGeneric.getStartDate().getTime() + 7200000));
			fooNonGeneric2.addMessage("Message string 11");
			fooNonGeneric2.addMessage("Message string 22");

			final FooGeneric<FooNonGeneric, Boolean> fooGenericInnner = new FooGeneric<FooNonGeneric, Boolean>();
			fooGenericInnner.setA(fooNonGeneric2);
			fooGenericInnner.setB(false);

			final int[] ints = { 11, 22 };
			fooGenericComplex.setA(ints);
			fooGenericComplex.setB(fooGenericInnner);

			rpBuilder.paramJson("fooGenericComplex", fooGenericComplex, typesJsonController.getTypeGenericComplex());
		} catch (final UnsupportedEncodingException e) {
			// handle exception ...
		}

		// build URL
		generatedUrl = rpBuilder.build();
	}

	public String getGeneratedUrl() {
		return generatedUrl;
	}

	public boolean isB() {
		return b;
	}

	public void setB(final boolean b) {
		this.b = b;
	}

	public long getL() {
		return l;
	}

	public void setL(final long l) {
		this.l = l;
	}

	public int[] getInts() {
		return ints;
	}

	public String getPrettyInts() {
		return Arrays.toString(ints);
	}

	public void setInts(final int[] ints) {
		this.ints = ints;
	}

	public char[] getChars() {
		return chars;
	}

	public String getPrettyChars() {
		return Arrays.toString(chars);
	}

	public void setChars(final char[] chars) {
		this.chars = chars;
	}

	public String getS() {
		return s;
	}

	public void setS(final String s) {
		this.s = s;
	}

	public Integer getI() {
		return i;
	}

	public void setI(final Integer i) {
		this.i = i;
	}

	public Date getD() {
		return d;
	}

	public void setD(final Date d) {
		this.d = d;
	}

	public Collection<Integer> getList() {
		return list;
	}

	public void setList(final Collection<Integer> list) {
		this.list = list;
	}

	public Map<String, ImmutablePair<Integer, Date>> getMap() {
		return map;
	}

	public void setMap(final Map<String, ImmutablePair<Integer, Date>> map) {
		this.map = map;
	}

	public FooNonGeneric getFooNonGeneric() {
		return fooNonGeneric;
	}

	public void setFooNonGeneric(final FooNonGeneric fooNonGeneric) {
		this.fooNonGeneric = fooNonGeneric;
	}

	public FooGeneric<String, Integer> getFooGenericSimple() {
		return fooGenericSimple;
	}

	public void setFooGenericSimple(final FooGeneric<String, Integer> fooGenericSimple) {
		this.fooGenericSimple = fooGenericSimple;
	}

	public FooGeneric<int[], FooGeneric<FooNonGeneric, Boolean>> getFooGenericComplex() {
		return fooGenericComplex;
	}

	public void setFooGenericComplex(final FooGeneric<int[], FooGeneric<FooNonGeneric, Boolean>> fooGenericComplex) {
		this.fooGenericComplex = fooGenericComplex;
	}

	public TypesJsonController getTypesJsonController() {
		return typesJsonController;
	}

	public void setTypesJsonController(final TypesJsonController typesJsonController) {
		this.typesJsonController = typesJsonController;
	}
}
