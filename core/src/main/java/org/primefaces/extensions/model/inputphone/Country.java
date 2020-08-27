/*
 * Copyright 2011-2020 PrimeFaces Extensions
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
