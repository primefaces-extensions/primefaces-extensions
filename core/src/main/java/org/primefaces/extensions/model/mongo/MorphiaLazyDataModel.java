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
package org.primefaces.extensions.model.mongo;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.faces.FacesException;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.util.Lazy;

import dev.morphia.Datastore;
import dev.morphia.query.FindOptions;
import dev.morphia.query.Query;
import dev.morphia.query.Sort;
import dev.morphia.query.experimental.filters.Filters;
import dev.morphia.query.experimental.filters.RegexFilter;

/**
 * Basic {@link LazyDataModel} implementation for MongoDB using Morphia.
 *
 * @param <T> The model class.
 */
public class MorphiaLazyDataModel<T> extends LazyDataModel<T> implements Serializable {

    protected transient Datastore ds;
    protected Class<T> entityClass;
    protected String rowKeyField;

    // usually will be getId() but can be a user specified method as well when using the 2nd constructor
    private transient Lazy<Method> rowKeyGetter;

    /*
     * if the default match mode queries in applyFilters() dont work for a specific field, overridden field queries with the overrideFieldQuery method will add
     * BiConsumers to this map, where the key is the field name specified in <p:column filterBy="">
     */
    private final Map<String, BiConsumer<Query<T>, FilterMeta>> overrides = new HashMap<>();
    // consumer to be executed before the query is built, useful to modify the original query
    private transient Consumer<Query<T>> prependConsumer;

    /**
     * For serialization only
     */
    public MorphiaLazyDataModel() {
        // for serialization only
    }

    /**
     * Constructs a Morphia lazy data model with selection support.
     *
     * @param ds the {@link Datastore}
     * @param entityClass The entity class
     * @param rowKeyField The name of the rowKey property (e.g. "id")
     */
    public MorphiaLazyDataModel(final Datastore ds, final Class<T> entityClass, final String rowKeyField) {
        this();
        this.ds = ds;
        this.entityClass = entityClass;
        this.rowKeyField = rowKeyField;
    }

    /**
     * Constructs a Morphia lazy data model with selection support with the default "id" field being the row key.
     *
     * @param ds the {@link Datastore}
     * @param entityClass The entity class
     */
    public MorphiaLazyDataModel(final Datastore ds, final Class<T> entityClass) {
        this(ds, entityClass, "id");
    }

    @Override
    public T getRowData(final String rowKey) {
        if (rowKeyField != null) {
            for (final T object : getWrappedData()) {
                try {
                    final Object rko = getRowKeyGetter().invoke(object);
                    final String rk = rko == null ? null : rko.toString();
                    if (Objects.equals(rk, rowKey)) {
                        return object;
                    }
                }
                catch (final Exception ex) {
                    throw new FacesException(
                                "Could not invoke getter for " + rowKeyField + " on " + entityClass.getName(), ex);
                }
            }
            return null;
        }
        throw new UnsupportedOperationException(
                    getMessage("Provide a Converter or rowKeyField via constructor or implement getRowData(String rowKey) in %s"
                                + ", when basic rowKey algorithm is not used [component=%s,view=%s]."));
    }

    @Override
    public String getRowKey(final T object) {
        if (rowKeyField != null) {
            try {
                final Object rowKey = getRowKeyGetter().invoke(object);
                return rowKey == null ? null : rowKey.toString();
            }
            catch (final InvocationTargetException | IllegalAccessException e) {
                throw new FacesException("Could not invoke getter for " + rowKeyField + " on " + entityClass.getName(),
                            e);
            }
        }

        throw new UnsupportedOperationException(
                    getMessage("Provide a Converter or rowKeyField via constructor or implement getRowKey(T object) in %s"
                                + ", when basic rowKey algorithm is not used [component=%s,view=%s]."));
    }

    @Override
    public int count(final Map<String, FilterMeta> map) {
        final Query<T> q = this.buildQuery();
        final long count = applyFilters(q, map).count();
        return (int) count;
    }

    @Override
    public List<T> load(final int first, final int pageSize, final Map<String, SortMeta> sort,
                final Map<String, FilterMeta> filters) {
        final Query<T> q = this.buildQuery();
        final FindOptions opt = new FindOptions();
        sort.forEach((field, sortData) -> opt.sort(sortData.getOrder().name().equalsIgnoreCase("DESCENDING") ? Sort.descending(field) : Sort.ascending(field)));

        this.applyFilters(q, filters);
        opt.skip(first).limit(pageSize);
        return q.iterator(opt).toList();
    }

    public Query<T> applyFilters(final Query<T> q, final Map<String, FilterMeta> filters) {
        filters.forEach((field, metadata) -> {

            if (metadata.getFilterValue() != null) {
                final BiConsumer<Query<T>, FilterMeta> override = overrides.get(field);
                if (override != null) {
                    override.accept(q, metadata);
                }
                else {
                    final Object val = metadata.getFilterValue();
                    if (metadata.getMatchMode() != null) {

                        switch (metadata.getMatchMode()) {
                            case STARTS_WITH:
                                final RegexFilter regStartsWith = Filters.regex(field);
                                regStartsWith.pattern("^" + val).caseInsensitive();
                                q.filter(regStartsWith);
                                break;
                            case ENDS_WITH:
                                final RegexFilter regEndsWith = Filters.regex(field);
                                regEndsWith.pattern(val + "$").caseInsensitive();
                                q.filter(regEndsWith);
                                break;
                            case CONTAINS:
                                q.filter(Filters.regex(field).pattern(val + "").caseInsensitive());
                                break;
                            case EXACT:
                                final Object castedValueEx = castedValue(field, val);
                                if (castedValueEx != null) {
                                    q.filter(Filters.eq(field, castedValueEx));
                                }
                                else {
                                    q.filter(Filters.eq(field, val));
                                }

                                break;
                            case LESS_THAN:
                                final Object castedValueLt = castedValue(field, val);
                                if (castedValueLt != null) {
                                    q.filter(Filters.lt(field, castedValueLt));
                                }
                                else {
                                    q.filter(Filters.lt(field, val));
                                }

                                break;
                            case LESS_THAN_EQUALS:
                                final Object castedValueLte = castedValue(field, val);
                                if (castedValueLte != null) {
                                    q.filter(Filters.lte(field, castedValueLte));
                                }
                                else {
                                    q.filter(Filters.lte(field, val));
                                }
                                break;
                            case GREATER_THAN:
                                final Object castedValueGt = castedValue(field, val);
                                if (castedValueGt != null) {
                                    q.filter(Filters.gt(field, castedValueGt));
                                }
                                else {
                                    q.filter(Filters.gt(field, val));
                                }
                                break;
                            case GREATER_THAN_EQUALS:

                                final Object castedValueGte = castedValue(field, val);
                                if (castedValueGte != null) {
                                    q.filter(Filters.gte(field, castedValueGte));
                                }
                                else {
                                    q.filter(Filters.gte(field, val));
                                }
                                break;

                            case EQUALS:
                                q.filter(Filters.eq(field, val));
                                break;

                            case IN:
                                if (metadata.getFilterValue().getClass() == Object[].class) {
                                    final Object[] parts = (Object[]) metadata.getFilterValue();
                                    q.filter(Filters.in(field, Arrays.asList(parts)));
                                }

                                break;

                            default:
                                throw new UnsupportedOperationException(
                                            "MatchMode " + metadata.getMatchMode() + " not supported");
                        }
                    }
                }
            }
        });
        return q;
    }

    public MorphiaLazyDataModel<T> prependQuery(final Consumer<Query<T>> consumer) {
        prependConsumer = consumer;
        return this;
    }

    public MorphiaLazyDataModel<T> overrideFieldQuery(final String field,
                final BiConsumer<Query<T>, FilterMeta> consumer) {
        this.overrides.put(field, consumer);
        return this;
    }

    protected Method getRowKeyGetter() {
        if (rowKeyGetter == null) {
            rowKeyGetter = new Lazy<>(() -> {
                try {
                    return new PropertyDescriptor(rowKeyField, entityClass).getReadMethod();
                }
                catch (final IntrospectionException e) {
                    throw new FacesException("Could not access " + rowKeyField + " on " + entityClass.getName(), e);
                }
            });
        }
        return rowKeyGetter.get();
    }

    /**
     * checks the data type of the field on the corresponding class and tries to convert the string value to its data type (only handles basic primitive types,
     * for more complex data types the field query should be overridden with the overrideFieldQuery method) for example this can be useful when filtering for
     * fields which are numbers like ints,floats,doubles and longs where the filterValue will be a string and thus the query wont find any matches since it's
     * comparing a string with a number
     *
     * @param field the field on the entity to cast
     * @param value the value to cast based on the field
     * @return the newly cast object
     */
    private Object castedValue(final String field, final Object value) {
        try {
            final Field f = entityClass.getDeclaredField(field);
            if (f == null) {
                return null;
            }
            if (f.getType().isAssignableFrom(Integer.class) || f.getType().isAssignableFrom(int.class)) {
                return Integer.valueOf(value + "");
            }
            else if (f.getType().isAssignableFrom(Float.class) || f.getType().isAssignableFrom(float.class)) {
                return Float.valueOf(value + "");
            }
            else if (f.getType().isAssignableFrom(Double.class) || f.getType().isAssignableFrom(double.class)) {
                return Double.valueOf(value + "");
            }
            else if (f.getType().isAssignableFrom(Long.class) || f.getType().isAssignableFrom(long.class)) {
                return Long.valueOf(value + "");
            }
            else if (f.getType().isAssignableFrom(Boolean.class) || f.getType().isAssignableFrom(boolean.class)) {
                return Boolean.valueOf(value + "");
            }
            else if (f.getType().isAssignableFrom(String.class)) {
                return value + "";
            }
        }
        catch (final Exception e) {
            throw new FacesException("Failed to convert " + field + " to its corresponding data type", e);
        }
        return null;
    }

    private Query<T> buildQuery() {
        final Query<T> q = ds.find(entityClass).disableValidation();
        if (prependConsumer != null) {
            prependConsumer.accept(q);
        }
        return q;
    }

}