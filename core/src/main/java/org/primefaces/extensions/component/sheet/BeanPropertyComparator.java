/*
 * Copyright 2011-2021 PrimeFaces Extensions
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
package org.primefaces.extensions.component.sheet;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;

import org.primefaces.model.SortMeta;

/**
 * Generic comparator for column sorting.
 */
public class BeanPropertyComparator implements Comparator<Object> {

    private String var;
    private Locale locale;
    private Collator collator;
    private SortMeta sortMeta;

    public BeanPropertyComparator(String var, SortMeta sortMeta, Locale locale) {
        this.sortMeta = sortMeta;
        this.var = var;
        this.locale = locale;
        collator = Collator.getInstance(locale);
    }

    @Override
    public int compare(Object obj1, Object obj2) {
        FacesContext context = FacesContext.getCurrentInstance();
        ValueExpression sortBy = sortMeta.getSortBy();

        try {
            context.getExternalContext().getRequestMap().put(var, obj1);
            Object value1 = sortBy.getValue(context.getELContext());

            context.getExternalContext().getRequestMap().put(var, obj2);
            Object value2 = sortBy.getValue(context.getELContext());

            int result;

            if (sortMeta.getFunction() == null) {
                // Empty check
                if (value1 == null && value2 == null) {
                    return 0;
                }
                else if (value1 == null) {
                    result = sortMeta.getNullSortOrder();
                }
                else if (value2 == null) {
                    result = -1 * sortMeta.getNullSortOrder();
                }
                else if (value1 instanceof String && value2 instanceof String) {
                    if (sortMeta.isCaseSensitiveSort()) {
                        result = collator.compare(value1, value2);
                    }
                    else {
                        String str1 = (((String) value1).toLowerCase(locale));
                        String str2 = (((String) value2).toLowerCase(locale));

                        result = collator.compare(str1, str2);
                    }
                }
                else {
                    result = ((Comparable<Object>) value1).compareTo(value2);
                }
            }
            else {
                result = (Integer) sortMeta.getFunction().invoke(context.getELContext(), new Object[] {value1, value2});
            }

            return sortMeta.getOrder().isAscending() ? result : -1 * result;

        }
        catch (Exception e) {
            throw new FacesException(e);
        }
    }
}
