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
package org.primefaces.extensions.model.mongo;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.logging.Logger;

import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;

import org.primefaces.context.PrimeApplicationContext;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.primefaces.util.Callbacks;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.Constants;
import org.primefaces.util.PropertyDescriptorResolver;

import dev.morphia.Datastore;
import dev.morphia.query.CountOptions;
import dev.morphia.query.FindOptions;
import dev.morphia.query.MorphiaCursor;
import dev.morphia.query.Query;
import dev.morphia.query.Sort;
import dev.morphia.query.filters.Filters;
import dev.morphia.query.filters.RegexFilter;

/**
 * Basic {@link LazyDataModel} implementation for MongoDB using Morphia.
 *
 * @param <T> The model class.
 */
public class MorphiaLazyDataModel<T> extends LazyDataModel<T> implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(MorphiaLazyDataModel.class.getName());

    protected Class<T> entityClass;
    protected Callbacks.SerializableSupplier<Datastore> datastore;
    protected String rowKeyField;
    protected Callbacks.SerializableFunction<T, Object> rowKeyProvider;

    /*
     * if the default match mode queries in applyFilters() dont work for a specific field, overridden field queries with the overrideFieldQuery method will add
     * BiConsumers to this map, where the key is the field name specified in <p:column filterBy="">
     */
    private final Map<String, BiConsumer<Query<T>, FilterMeta>> overrides = new HashMap<>();
    // consumer to be executed before the query is built, useful to modify the original query
    private transient Consumer<Query<T>> prependConsumer;
    // global filter consumer (to be implemented by the user)
    private transient BiConsumer<Query<T>, FilterMeta> globalFilterConsumer;
    // for user supplied FindOptions
    private transient Callbacks.SerializableSupplier<FindOptions> findOptionsSupplier;
    // for user supplied CountOptions
    private transient Callbacks.SerializableSupplier<CountOptions> countOptionsSupplier;

    /**
     * For serialization only
     */
    public MorphiaLazyDataModel() {
        // for serialization only
    }

    @Override
    public T getRowData(final String rowKey) {
        List<T> values = Objects.requireNonNullElseGet(getWrappedData(), Collections::emptyList);
        for (T obj : values) {
            if (Objects.equals(rowKey, getRowKey(obj))) {
                return obj;
            }
        }

        return null;
    }

    @Override
    public String getRowKey(final T object) {
        return String.valueOf(rowKeyProvider.apply(object));
    }

    @Override
    public int count(final Map<String, FilterMeta> map) {
        final Query<T> q = this.buildQuery();
        final CountOptions opts = getCountOptions();
        final long count = applyFilters(q, map).count(opts);
        return (int) count;
    }

    @Override
    public List<T> load(final int first, final int pageSize, final Map<String, SortMeta> sort,
                final Map<String, FilterMeta> filters) {
        final Query<T> q = buildQuery();
        final FindOptions opt = getFindOptions();
        sort.forEach((field, sortData) -> opt.sort(sortData.getOrder() == SortOrder.DESCENDING ? Sort.descending(field) : Sort.ascending(field)));

        applyFilters(q, filters);
        opt.skip(first).limit(pageSize);
        try (MorphiaCursor<T> cursor = q.iterator(opt)) {
            return cursor.toList();
        }
    }

    protected FindOptions getFindOptions() {
        try {
            return findOptionsSupplier != null ? findOptionsSupplier.get() : new FindOptions();
        }
        catch (Exception e) {
            // if we get here, this means the user supplied FindOptions failed to resolve for some reason, so we fall back to the default
            return new FindOptions();
        }
    }

    protected CountOptions getCountOptions() {
        try {
            return countOptionsSupplier != null ? countOptionsSupplier.get() : new CountOptions();
        }
        catch (Exception e) {
            // if we get here, this means the user supplied CountOptions failed to resolve for some reason, so we fall back to the default
            return new CountOptions();
        }
    }

    public Query<T> applyFilters(final Query<T> q, final Map<String, FilterMeta> filters) {
        PrimeApplicationContext primeAppContext = PrimeApplicationContext.getCurrentInstance(FacesContext.getCurrentInstance());

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
                                final RegexFilter regStartsWith = Filters.regex(field, "^" + val).caseInsensitive();
                                q.filter(regStartsWith);
                                break;
                            case ENDS_WITH:
                                final RegexFilter regEndsWith = Filters.regex(field, val + "$").caseInsensitive();
                                q.filter(regEndsWith);
                                break;
                            case CONTAINS:
                                q.filter(Filters.regex(field, val + "").caseInsensitive());
                                break;
                            case EXACT:
                                final Object castedValueEx = convertToDataType(primeAppContext, field, val);
                                if (castedValueEx != null) {
                                    q.filter(Filters.eq(field, castedValueEx));
                                }
                                else {
                                    q.filter(Filters.eq(field, val));
                                }
                                break;
                            case LESS_THAN:
                                final Object castedValueLt = convertToDataType(primeAppContext, field, val);
                                if (castedValueLt != null) {
                                    q.filter(Filters.lt(field, castedValueLt));
                                }
                                else {
                                    q.filter(Filters.lt(field, val));
                                }
                                break;
                            case LESS_THAN_EQUALS:
                                final Object castedValueLte = convertToDataType(primeAppContext, field, val);
                                if (castedValueLte != null) {
                                    q.filter(Filters.lte(field, castedValueLte));
                                }
                                else {
                                    q.filter(Filters.lte(field, val));
                                }
                                break;
                            case GREATER_THAN:
                                final Object castedValueGt = convertToDataType(primeAppContext, field, val);
                                if (castedValueGt != null) {
                                    q.filter(Filters.gt(field, castedValueGt));
                                }
                                else {
                                    q.filter(Filters.gt(field, val));
                                }
                                break;
                            case GREATER_THAN_EQUALS:

                                final Object castedValueGte = convertToDataType(primeAppContext, field, val);
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
                            case BETWEEN:
                                if (metadata.getFilterValue() instanceof List) {
                                    final List<?> dates = (List) metadata.getFilterValue();
                                    if (dates.size() > 1) { // does this ever have less than 2 items?
                                        q.filter(Filters.gte(field, dates.get(0)), Filters.lte(field, dates.get(1)));
                                    }
                                }
                                break;
                            case NOT_CONTAINS:
                                q.filter(Filters.regex(field, val + Constants.EMPTY_STRING).caseInsensitive().not());
                                break;
                            case NOT_EQUALS:
                                final Object castedValueNe = convertToDataType(primeAppContext, field, val);
                                if (castedValueNe != null) {
                                    q.filter(Filters.eq(field, castedValueNe).not());
                                }
                                else {
                                    q.filter(Filters.eq(field, val).not());
                                }
                                break;
                            case NOT_STARTS_WITH:
                                final RegexFilter regStartsWithNot = Filters.regex(field, "^" + val).caseInsensitive();
                                q.filter(regStartsWithNot.not());
                                break;
                            case NOT_IN:
                                if (metadata.getFilterValue() instanceof Object[]) {
                                    final Object[] parts = (Object[]) metadata.getFilterValue();
                                    q.filter(Filters.nin(field, Arrays.asList(parts)));
                                }
                                break;
                            case NOT_ENDS_WITH:
                                final RegexFilter regEndsWithNot = Filters.regex(field, val + "$").caseInsensitive();
                                q.filter(regEndsWithNot.not());
                                break;
                            case GLOBAL:
                                if (globalFilterConsumer != null) {
                                    globalFilterConsumer.accept(q, metadata);
                                }
                                break;

                            default:
                                throw new UnsupportedOperationException("MatchMode " + metadata.getMatchMode() + " not supported");
                        }
                    }
                }
            }
        });
        return q;
    }

    /**
     * use {@link Builder#prependQuery(Consumer)} instead
     */
    @Deprecated
    public MorphiaLazyDataModel<T> prependQuery(final Consumer<Query<T>> consumer) {
        this.prependConsumer = consumer;
        return this;
    }

    /**
     * use {@link Builder#findOptions(Callbacks.SerializableSupplier)} instead
     */
    @Deprecated
    public MorphiaLazyDataModel<T> findOptions(final Callbacks.SerializableSupplier<FindOptions> supplier) {
        this.findOptionsSupplier = supplier;
        return this;
    }

    /**
     * use {@link Builder#findOptions(Callbacks.SerializableSupplier)} instead
     */
    @Deprecated
    public MorphiaLazyDataModel<T> countOptions(final Callbacks.SerializableSupplier<CountOptions> supplier) {
        this.countOptionsSupplier = supplier;
        return this;
    }

    /**
     * use {@link Builder#globalFilter(BiConsumer)} instead
     */
    @Deprecated
    public MorphiaLazyDataModel<T> globalFilter(final BiConsumer<Query<T>, FilterMeta> consumer) {
        this.globalFilterConsumer = consumer;
        return this;
    }

    /**
     * use {@link Builder#overrideFieldQuery(String, BiConsumer)} instead
     */
    @Deprecated
    public MorphiaLazyDataModel<T> overrideFieldQuery(final String field, final BiConsumer<Query<T>, FilterMeta> consumer) {
        this.overrides.put(field, consumer);
        return this;
    }

    private Object convertToDataType(PrimeApplicationContext primeAppContext, final String field, final Object value) {
        PropertyDescriptorResolver propResolver = primeAppContext.getPropertyDescriptorResolver();
        final PropertyDescriptor propertyDescriptor = propResolver.get(entityClass, field);
        return ComponentUtils.convertToType(value, propertyDescriptor.getPropertyType(), LOGGER);
    }

    private Query<T> buildQuery() {
        final Query<T> q = datastore.get().find(entityClass).disableValidation();
        if (prependConsumer != null) {
            prependConsumer.accept(q);
        }
        return q;
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> {
        private final MorphiaLazyDataModel<T> model;

        public Builder() {
            model = new MorphiaLazyDataModel<>();
        }

        public Builder<T> entityClass(Class<T> entityClass) {
            model.entityClass = entityClass;
            return this;
        }

        public Builder<T> datastore(Callbacks.SerializableSupplier<Datastore> datastore) {
            model.datastore = datastore;
            return this;
        }

        public Builder<T> rowKeyConverter(Converter<T> rowKeyConverter) {
            model.rowKeyConverter = rowKeyConverter;
            return this;
        }

        public Builder<T> rowKeyProvider(Callbacks.SerializableFunction<T, Object> rowKeyProvider) {
            model.rowKeyProvider = rowKeyProvider;
            return this;
        }

        public Builder<T> rowKeyField(String rowKey) {
            model.rowKeyField = rowKey;
            return this;
        }

        public Builder<T> findOptions(Callbacks.SerializableSupplier<FindOptions> findOptionsSupplier) {
            model.findOptionsSupplier = findOptionsSupplier;
            return this;
        }

        public Builder<T> countOptions(Callbacks.SerializableSupplier<CountOptions> countOptionsSupplier) {
            model.countOptionsSupplier = countOptionsSupplier;
            return this;
        }

        public Builder<T> prependQuery(final Consumer<Query<T>> consumer) {
            model.prependConsumer = consumer;
            return this;
        }

        public Builder<T> globalFilter(final BiConsumer<Query<T>, FilterMeta> consumer) {
            model.globalFilterConsumer = consumer;
            return this;
        }

        public Builder<T> overrideFieldQuery(final String field, final BiConsumer<Query<T>, FilterMeta> consumer) {
            model.overrides.put(field, consumer);
            return this;
        }

        public MorphiaLazyDataModel<T> build() {
            Objects.requireNonNull(model.entityClass, "entityClass not set");
            Objects.requireNonNull(model.datastore, "datastore not set");

            boolean requiresRowKeyProvider = model.rowKeyProvider == null && (model.rowKeyConverter != null || model.rowKeyField != null);
            if (requiresRowKeyProvider) {
                if (model.rowKeyConverter != null) {
                    model.rowKeyProvider = model::getRowKeyFromConverter;
                }
                else {
                    Objects.requireNonNull(model.rowKeyField, "rowKeyField is mandatory if neither rowKeyProvider nor converter is provided");

                    PropertyDescriptorResolver propResolver = PrimeApplicationContext.getCurrentInstance(FacesContext.getCurrentInstance())
                                .getPropertyDescriptorResolver();
                    model.rowKeyProvider = obj -> propResolver.getValue(obj, model.rowKeyField);
                }
            }
            return model;
        }
    }
}