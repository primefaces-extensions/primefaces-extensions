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
package org.primefaces.extensions.showcase.controller.timeago;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 */
@Named
@ViewScoped
public class BasicTimeAgoController implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Date now;

    private final Date firstRelease;

    public BasicTimeAgoController() {
        now = new Date();

        final Calendar calendar = GregorianCalendar.getInstance();
        calendar.set(2012, 0, 23);
        firstRelease = calendar.getTime();
    }

    public Date getNow() {
        return now;
    }

    public Date getFirstRelease() {
        return firstRelease;
    }

}
