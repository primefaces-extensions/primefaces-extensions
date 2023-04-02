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
package org.primefaces.extensions.showcase.controller.timeago;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 */
@Named
@ViewScoped
public class BasicTimeAgoController implements Serializable {

    private static final long serialVersionUID = 1L;

    private final OffsetDateTime created = OffsetDateTime.now();

    private final LocalDateTime firstRelease = LocalDate.of(2012, Month.JANUARY, 23).atStartOfDay();

    private final LocalDateTime nextMonth = LocalDateTime.now()
                .plusMonths(1)
                .withDayOfMonth(1)
                .truncatedTo(ChronoUnit.DAYS);

    public OffsetDateTime getCreated() {
        return created;
    }

    public LocalDateTime getFirstRelease() {
        return firstRelease;
    }

    public LocalDateTime getNextMonth() {
        return nextMonth;
    }

}
