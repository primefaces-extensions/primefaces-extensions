/*
 * Copyright 2011-2015 PrimeFaces Extensions
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
package org.primefaces.extensions.showcase.controller.exporter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 * GroupedTableController
 *
 * @author Sudheer Jonna / last modified by $Author$
 * @since 0.7.0
 * @version $Revision: 1.0 $
 */
@Named
@ViewScoped
public class GroupedTableController implements Serializable {

	private static final long serialVersionUID = 1L;
	private final List<Mobile> mobileList;

	public GroupedTableController() {
		mobileList = new ArrayList<Mobile>();
		populateRandomSales();
	}

	private void populateRandomSales() {
		for (int i = 0; i < 10; i++) {
			mobileList.add(new Mobile("Model" + i, getQ1Sales(), getQ1Profits(), getQ2Sales(), getQ2Profits(),
					getQ3Sales(), getQ3Profits(), getQ4Sales(), getQ4Profits()));
		}
	}

	private long getQ1Sales() {
		return (long) (Math.random() * 10000);
	}

	private long getQ1Profits() {
		return (long) (Math.random() * 100);
	}

	private long getQ2Sales() {
		return (long) (Math.random() * 10000);
	}

	private long getQ2Profits() {
		return (long) (Math.random() * 100);
	}

	private long getQ3Sales() {
		return (long) (Math.random() * 10000);
	}

	private long getQ3Profits() {
		return (long) (Math.random() * 100);
	}

	private long getQ4Sales() {
		return (long) (Math.random() * 10000);
	}

	private long getQ4Profits() {
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
		public String manufacturer;
		public long q1Sale;
		public long q2Sale;
		public long q3Sale;
		public long q4Sale;

		public long q1Profit;
		public long q2Profit;
		public long q3Profit;
		public long q4Profit;

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
