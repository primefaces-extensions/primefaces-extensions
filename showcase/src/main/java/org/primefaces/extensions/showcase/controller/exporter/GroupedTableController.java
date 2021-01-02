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
package org.primefaces.extensions.showcase.controller.exporter;

import java.io.*;
import java.util.*;

import javax.faces.view.*;
import javax.inject.*;

/**
 * GroupedTableController
 *
 * @author Sudheer Jonna / last modified by $Author$
 * @version $Revision: 1.0 $
 * @since 0.7.0
 */
@Named
@ViewScoped
public class GroupedTableController implements Serializable {

    private static final long serialVersionUID = 1L;
    private final List<Mobile> mobileList;

    public GroupedTableController() {
        mobileList = new ArrayList<>();
        populateRandomSales();
    }

    private void populateRandomSales() {
        for (int i = 0; i < 10; i++) {
            mobileList.add(new Mobile("Model" + i, getQ1Sales(), getQ1Profits(), getQ2Sales(), getQ2Profits(),
                        getQ3Sales(), getQ3Profits(), getQ4Sales(), getQ4Profits()));
        }
    }

    private static long getQ1Sales() {
        return (long) (Math.random() * 10000);
    }

    private static long getQ1Profits() {
        return (long) (Math.random() * 100);
    }

    private static long getQ2Sales() {
        return (long) (Math.random() * 10000);
    }

    private static long getQ2Profits() {
        return (long) (Math.random() * 100);
    }

    private static long getQ3Sales() {
        return (long) (Math.random() * 10000);
    }

    private static long getQ3Profits() {
        return (long) (Math.random() * 100);
    }

    private static long getQ4Sales() {
        return (long) (Math.random() * 10000);
    }

    private static long getQ4Profits() {
        return (long) (Math.random() * 100);
    }

    public long getQ1TotSales() {
        long total = 0;

        for (final Mobile sale : mobileList) {
            total += sale.getQ1Sale();
        }

        return total;
    }

    public long getQ2TotSales() {
        long total = 0;

        for (final Mobile sale : mobileList) {
            total += sale.getQ2Sale();
        }

        return total;
    }

    public long getQ3TotSales() {
        long total = 0;

        for (final Mobile sale : mobileList) {
            total += sale.getQ3Sale();
        }

        return total;
    }

    public long getQ4TotSales() {
        long total = 0;

        for (final Mobile sale : mobileList) {
            total += sale.getQ4Sale();
        }

        return total;
    }

    public long getQ1TotProfit() {
        long total = 0;

        for (final Mobile sale : mobileList) {
            total += sale.getQ1Profit();
        }

        return total;
    }

    public long getQ2TotProfit() {
        long total = 0;

        for (final Mobile sale : mobileList) {
            total += sale.getQ2Profit();
        }

        return total;
    }

    public long getQ3TotProfit() {
        long total = 0;

        for (final Mobile sale : mobileList) {
            total += sale.getQ3Profit();
        }

        return total;
    }

    public long getQ4TotProfit() {
        long total = 0;

        for (final Mobile sale : mobileList) {
            total += sale.getQ4Profit();
        }

        return total;
    }

    public class Mobile implements Serializable {

        private static final long serialVersionUID = 1L;
        private String manufacturer;
        private long q1Sale;
        private long q2Sale;
        private long q3Sale;
        private long q4Sale;

        private long q1Profit;
        private long q2Profit;
        private long q3Profit;
        private long q4Profit;

        public Mobile(final String manufacturer, final long q1Sale, final long q2Sale, final long q3Sale,
                    final long q4Sale, final long q1Profit, final long q2Profit, final long q3Profit, final long q4Profit) {
            this.manufacturer = manufacturer;
            this.q1Sale = q1Sale;
            this.q2Sale = q2Sale;
            this.q3Sale = q3Sale;
            this.q4Sale = q4Sale;
            this.q1Profit = q1Profit;
            this.q2Profit = q2Profit;
            this.q3Profit = q3Profit;
            this.q4Profit = q4Profit;
        }

        public String getManufacturer() {
            return manufacturer;
        }

        public void setManufacturer(final String manufacturer) {
            this.manufacturer = manufacturer;
        }

        public long getQ1Sale() {
            return q1Sale;
        }

        public void setQ1Sale(final long q1Sale) {
            this.q1Sale = q1Sale;
        }

        public long getQ2Sale() {
            return q2Sale;
        }

        public void setQ2Sale(final long q2Sale) {
            this.q2Sale = q2Sale;
        }

        public long getQ3Sale() {
            return q3Sale;
        }

        public void setQ3Sale(final long q3Sale) {
            this.q3Sale = q3Sale;
        }

        public long getQ4Sale() {
            return q4Sale;
        }

        public void setQ4Sale(final long q4Sale) {
            this.q4Sale = q4Sale;
        }

        public long getQ1Profit() {
            return q1Profit;
        }

        public void setQ1Profit(final long q1Profit) {
            this.q1Profit = q1Profit;
        }

        public long getQ2Profit() {
            return q2Profit;
        }

        public void setQ2Profit(final long q2Profit) {
            this.q2Profit = q2Profit;
        }

        public long getQ3Profit() {
            return q3Profit;
        }

        public void setQ3Profit(final long q3Profit) {
            this.q3Profit = q3Profit;
        }

        public long getQ4Profit() {
            return q4Profit;
        }

        public void setQ4Profit(final long q4Profit) {
            this.q4Profit = q4Profit;
        }
    }

    public List<Mobile> getMobileList() {
        return mobileList;
    }
}
