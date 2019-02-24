/**
 * Copyright 2011-2018 PrimeFaces Extensions
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
package org.primefaces.extensions.component.inputphone;

/**
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 */
public class Country {

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

}
