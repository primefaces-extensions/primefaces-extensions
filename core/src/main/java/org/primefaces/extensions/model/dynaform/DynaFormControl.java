/*
 * Copyright (c) 2011-2022 PrimeFaces Extensions
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
package org.primefaces.extensions.model.dynaform;

import java.io.Serializable;
import java.util.Objects;

import org.primefaces.extensions.model.common.KeyData;

/**
 * Class representing a control inside of <code>DynaForm</code>.
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since 0.5
 */
public class DynaFormControl extends AbstractDynaFormElement implements KeyData {

    public static final String DEFAULT_TYPE = "default";
    private static final long serialVersionUID = 1L;
    private static final String KEY_PREFIX_ROW = "r";
    private static final String KEY_PREFIX_COLUMN = "c";
    private static final String KEY_SUFFIX_REGULAR = "reg";
    private static final String KEY_SUFFIX_EXTENDED = "ext";
    private static final String KEY_SUFFIX_POSITION = "p";

    private String key;
    private Serializable data;
    private final String type;
    private int position;

    public DynaFormControl(Serializable data, String type, int colspan, int rowspan, int row, int column, int position,
                boolean extended) {
        super(colspan, rowspan, row, column, extended);
        this.position = position;

        this.data = data;
        if (type != null) {
            this.type = type;
        }
        else {
            this.type = DEFAULT_TYPE;
        }

        generateKey();
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public Serializable getData() {
        return data;
    }

    @Override
    public void setData(Serializable data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public int getPosition() {
        return position;
    }

    void setPosition(int position) {
        this.position = position;
    }

    void generateKey() {
        final StringBuilder sb = new StringBuilder();
        sb.append(KEY_PREFIX_ROW).append(getRow()).append(KEY_PREFIX_COLUMN).append(getColumn()).append(KEY_SUFFIX_POSITION).append(getPosition());
        if (isExtended()) {
            sb.append(KEY_SUFFIX_EXTENDED);
        }
        else {
            sb.append(KEY_SUFFIX_REGULAR);
        }
        setKey(sb.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DynaFormControl)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        DynaFormControl that = (DynaFormControl) o;
        return getPosition() == that.getPosition();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getPosition());
    }

    @Override
    public String toString() {
        return "DynaFormControl{" + "key=" + key + ", data=" + data + ", type=" + type + ", position=" + position + '}';
    }
}
