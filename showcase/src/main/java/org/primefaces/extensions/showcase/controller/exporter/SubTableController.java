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
 * SubTableController
 *
 * @author Sudheer Jonna / last modified by $Author$
 * @since 0.7.0
 * @version $Revision: 1.0 $
 */
@Named
@ViewScoped
public class SubTableController implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Player> players;

	public SubTableController() {
		populatePlayers();
	}

	public void populatePlayers() {
		players = new ArrayList<Player>();

		final Player sachin = new Player("Sachin", 10, "India");
		sachin.getDetails().add(new PlayerDetails("2001", 800, 7, 12));
		sachin.getDetails().add(new PlayerDetails("2002", 933, 13, 11));
		sachin.getDetails().add(new PlayerDetails("2003", 900, 10, 33));
		sachin.getDetails().add(new PlayerDetails("2004", 600, 15, 8));
		sachin.getDetails().add(new PlayerDetails("2005", 1000, 22, 3));
		sachin.getDetails().add(new PlayerDetails("2006", 700, 5, 9));
		players.add(sachin);

		final Player ponting = new Player("Ponting", 6, "Australia");
		ponting.getDetails().add(new PlayerDetails("2001", 900, 5, 11));
		ponting.getDetails().add(new PlayerDetails("2002", 600, 2, 13));
		ponting.getDetails().add(new PlayerDetails("2003", 700, 13, 9));
		ponting.getDetails().add(new PlayerDetails("2004", 907, 9, 8));
		ponting.getDetails().add(new PlayerDetails("2005", 988, 3, 20));
		ponting.getDetails().add(new PlayerDetails("2006", 400, 6, 16));
		players.add(ponting);

		final Player lara = new Player("Lara", 10, "Trinidad");
		lara.getDetails().add(new PlayerDetails("2001", 600, 12, 22));
		lara.getDetails().add(new PlayerDetails("2002", 700, 9, 17));
		lara.getDetails().add(new PlayerDetails("2003", 999, 14, 9));
		lara.getDetails().add(new PlayerDetails("2004", 600, 7, 2));
		lara.getDetails().add(new PlayerDetails("2005", 888, 6, 5));
		lara.getDetails().add(new PlayerDetails("2006", 700, 22, 9));
		players.add(lara);
	}

	public List<Player> getPlayers() {
		return players;
	}

	public class Player implements Serializable {

		private static final long serialVersionUID = 1L;

		private String name;

		private int number;

		private String country;

		private List<PlayerDetails> details = new ArrayList<PlayerDetails>();

		public Player() {
		}

		public Player(final String name) {
			this.name = name;
		}

		public Player(final String name, final int number, final String country) {
			this.name = name;
			this.number = number;
			this.country = country;
		}

		public Player(final String name, final int number) {
			this.name = name;
			this.number = number;
		}

		public String getName() {
			return name;
		}

		public void setName(final String name) {
			this.name = name;
		}

		public int getNumber() {
			return number;
		}

		public void setNumber(final int number) {
			this.number = number;
		}

		public String getCountry() {
			return country;
		}

		public void setCountry(final String country) {
			this.country = country;
		}

		public List<PlayerDetails> getDetails() {
			return details;
		}

		public void setDetails(final List<PlayerDetails> details) {
			this.details = details;
		}

		public int getTotalRuns() {
			int sum = 0;

			for (final PlayerDetails d : details) {
				sum += d.getRuns();
			}

			return sum;
		}

		public int getTotalWickets() {
			int sum = 0;

			for (final PlayerDetails d : details) {
				sum += d.getWickets();
			}

			return sum;
		}

		public int getTotalCatches() {
			int sum = 0;

			for (final PlayerDetails d : details) {
				sum += d.getCatches();
			}

			return sum;
		}

		@Override
		public boolean equals(final Object obj) {
			if (obj == null) {
				return false;
			}

			if (!(obj instanceof Player)) {
				return false;
			}

			return ((Player) obj).getNumber() == this.number;
		}

		@Override
		public int hashCode() {
			final int hash = 1;

			return hash * 31 + name.hashCode();
		}

		@Override
		public String toString() {
			return name;
		}
	}

	public class PlayerDetails implements Serializable {

		private static final long serialVersionUID = 1L;

		private String year;

		private int runs;

		private int wickets;

		private int catches;

		public PlayerDetails() {
		}

		public PlayerDetails(final String year, final int runs, final int wickets, final int catches) {
			this.year = year;
			this.runs = runs;
			this.wickets = wickets;
			this.catches = catches;
		}

		public String getYear() {
			return year;
		}

		public void setYear(final String year) {
			this.year = year;
		}

		public int getRuns() {
			return runs;
		}

		public void setRuns(final int runs) {
			this.runs = runs;
		}

		public int getWickets() {
			return wickets;
		}

		public void setWickets(final int wickets) {
			this.wickets = wickets;
		}

		public int getCatches() {
			return catches;
		}

		public void setCatches(final int catches) {
			this.catches = catches;
		}
	}
}
