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
package org.primefaces.extensions.showcase.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Person model class.
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 */
public class Person implements Serializable {

    private static final long serialVersionUID = 20111128L;

    private String id;
    private String name;
    private int taxClass;
    private Date birthDate;
    private Set<String> languageSkills = new HashSet<String>();

    public Person(String id, String name, int taxClass, Date birthDate) {
        this.id = id;
        this.name = name;
        this.taxClass = taxClass;
        this.birthDate = birthDate;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTaxClass() {
        return taxClass;
    }

    public void setTaxClass(int taxClass) {
        this.taxClass = taxClass;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String[] getLanguageSkills() {
        return languageSkills.toArray(new String[languageSkills.size()]);
    }

    public void addLanguageSkill(String languageSkill) {
        languageSkills.add(languageSkill);
    }
}
