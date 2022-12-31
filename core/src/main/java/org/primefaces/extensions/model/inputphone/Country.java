/*
 * Copyright (c) 2011-2023 PrimeFaces Extensions
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
package org.primefaces.extensions.model.inputphone;

import java.io.Serializable;
import java.util.Objects;

/**
 * Model object for country handling of InputPhone.
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 * @since 7.0
 */
public class Country implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String name;
    private final String iso2;
    private final String dialCode;

    public Country(String name, String iso2, String dialCode) {
        this.name = name;
        this.iso2 = iso2;
        this.dialCode = dialCode;
    }

    public String getName() {
        return name;
    }

    public String getIso2() {
        return iso2;
    }

    public String getDialCode() {
        return dialCode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, iso2, dialCode);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Country other = (Country) obj;
        if (!Objects.equals(name, other.getName())) {
            return false;
        }
        if (!Objects.equals(iso2, other.getIso2())) {
            return false;
        }
        return Objects.equals(dialCode, other.getDialCode());
    }

    @Override
    public String toString() {
        return "Country{" + "name=" + name + ", iso2=" + iso2 + ", dialCode=" + dialCode + '}';
    }

}
