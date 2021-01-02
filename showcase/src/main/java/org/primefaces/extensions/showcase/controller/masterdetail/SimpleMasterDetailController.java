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
package org.primefaces.extensions.showcase.controller.masterdetail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.extensions.showcase.model.masterdetail.Country;
import org.primefaces.extensions.showcase.model.masterdetail.League;
import org.primefaces.extensions.showcase.model.masterdetail.Sport;

/**
 * SimpleMasterDetailController.
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 */
@Named
@ViewScoped
public class SimpleMasterDetailController implements Serializable {

    private static final long serialVersionUID = 20111120L;

    private List<Sport> sports;
    private int currentLevel = 1;

    public SimpleMasterDetailController() {
        if (sports == null) {
            sports = new ArrayList<Sport>();

            // football
            List<Country> countries = new ArrayList<Country>();
            Country country = new Country("Switzerland", "CH", "Football", getLeagues("Switzerland"));
            countries.add(country);
            country = new Country("England", "UK", "Football", getLeagues("England"));
            countries.add(country);
            country = new Country("Spain", "ES", "Football", getLeagues("Spain"));
            countries.add(country);
            country = new Country("Netherlands", "NL", "Football", getLeagues("Netherlands"));
            countries.add(country);
            sports.add(new Sport("Football", countries));

            // basketball
            countries = new ArrayList<Country>();
            country = new Country("Germany", "DE", "Basketball", getLeagues("Germany"));
            countries.add(country);
            country = new Country("USA", "US", "Basketball", getLeagues("USA"));
            countries.add(country);
            country = new Country("Poland", "PL", "Basketball", getLeagues("Poland"));
            countries.add(country);
            sports.add(new Sport("Basketball", countries));

            // ice hockey
            countries = new ArrayList<Country>();
            country = new Country("Russia", "RU", "Ice Hockey", getLeagues("Russia"));
            countries.add(country);
            country = new Country("Canada", "CA", "Ice Hockey", getLeagues("Canada"));
            countries.add(country);
            sports.add(new Sport("Ice Hockey", countries));
        }
    }

    public List<Sport> getSports() {
        return sports;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(final int currentLevel) {
        this.currentLevel = currentLevel;
    }

    private List<League> getLeagues(final String country) {
        final List<League> leagues = new ArrayList<League>();

        leagues.add(new League(country + " SuperLeague", 20));
        leagues.add(new League(country + " NotBadLeague", 15));
        leagues.add(new League(country + " CrapLeague", 30));

        return leagues;
    }
}
