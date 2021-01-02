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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.commons.lang3.RandomUtils;
import org.primefaces.event.ToggleEvent;
import org.primefaces.extensions.showcase.model.Customer;
import org.primefaces.extensions.showcase.model.Vehicle;

/**
 * VehicleTableController
 *
 * @author Sudheer Jonna / last modified by $Author$
 * @version $Revision: 1.0 $
 * @since 0.7.0
 */
@Named
@ViewScoped
public class VehicleTableController implements Serializable {

    private static final long serialVersionUID = 1L;

    private final static String[] MANUFACTURERS;
    private final static String[] COLORS;

    private final List<Vehicle> vehicles;
    private List<Customer> customersDetails = new ArrayList<>();

    private final List<Customer> customersDetailsList1 = new ArrayList<>();
    private final List<Customer> customersDetailsList2 = new ArrayList<>();

    static {
        MANUFACTURERS = new String[7];
        MANUFACTURERS[0] = "Mercedes";
        MANUFACTURERS[1] = "BMW";
        MANUFACTURERS[2] = "Volvo";
        MANUFACTURERS[3] = "Audi";
        MANUFACTURERS[4] = "Volkswagen";
        MANUFACTURERS[5] = "Ferrari";
        MANUFACTURERS[6] = "Ford";

        COLORS = new String[7];
        COLORS[0] = "Red";
        COLORS[1] = "White";
        COLORS[2] = "Yellow";
        COLORS[3] = "Green";
        COLORS[4] = "Blue";
        COLORS[5] = "Orange";
        COLORS[6] = "Black";
    }

    public VehicleTableController() {
        vehicles = new ArrayList<>();

        customersDetailsList1.add(new Customer("Thomas Andraschko", "Germany", populateRandomContactNumbers()));
        customersDetailsList1.add(new Customer("Oleg Varaksin", "Russia", populateRandomContactNumbers()));
        customersDetailsList1.add(new Customer("Nilesh Mali", "India", populateRandomContactNumbers()));
        customersDetailsList1.add(new Customer("Mauricio Fenoglio", "Uruguay", populateRandomContactNumbers()));
        customersDetailsList1.add(new Customer("Oval Slany", "Slovakia", populateRandomContactNumbers()));
        customersDetailsList1.add(new Customer("Sudheer Jonna", "India", populateRandomContactNumbers()));

        customersDetailsList2.add(new Customer("Narayana VenkataS", "India", populateRandomContactNumbers()));
        customersDetailsList2.add(new Customer("Anu Pallavi", "India", populateRandomContactNumbers()));
        customersDetailsList2.add(new Customer("Uma Vijayakumar", "India", populateRandomContactNumbers()));
        customersDetailsList2.add(new Customer("Surendra", "India", populateRandomContactNumbers()));
        customersDetailsList2.add(new Customer("Vineet Jain", "Canada", populateRandomContactNumbers()));
        customersDetailsList2.add(new Customer("Karthikeyan", "Canada", populateRandomContactNumbers()));
        customersDetailsList2.add(new Customer("Sudheer", "India", populateRandomContactNumbers()));

        populateRandomVehicles(vehicles, 6);
    }

    private void populateRandomVehicles(final List<Vehicle> vehicles, final int size) {
        for (int i = 0; i < size; i++) {
            customersDetails = new ArrayList<>();
            if ((i + 1) % 2 == 0) {
                customersDetails = customersDetailsList2;
            }
            else {
                customersDetails = customersDetailsList1;
            }
            vehicles.add(new Vehicle(i + 1, getRandomManufacturer(), getRandomColor(), getRandomSpeed(),
                        getRandomPrice(), getRandomYear(), customersDetails));
        }
    }

    private static String populateRandomContactNumbers() {
        String strippedNum;
        int num1 = 0;
        int num2 = 0;
        int num3 = 0;
        num1 = RandomUtils.nextInt(1, 600) + 100;
        num2 = RandomUtils.nextInt(1, 641) + 100;
        num3 = RandomUtils.nextInt(1, 8999) + 1000;

        strippedNum = Integer.toOctalString(num1);
        final String contactNumber = strippedNum + "-" + num2 + "-" + num3;
        return contactNumber;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    private static String getRandomManufacturer() {
        return MANUFACTURERS[(int) (Math.random() * 6)];
    }

    public List<Customer> getCustomersDetails() {
        return customersDetails;
    }

    private static int getRandomSpeed() {
        return (int) (Math.random() * 10 + 150);
    }

    private static int getRandomYear() {
        return (int) (Math.random() * 50 + 1960);
    }

    private static String getRandomColor() {
        return COLORS[(int) (Math.random() * 6)];
    }

    private static int getRandomPrice() {
        return (int) (Math.random() * 10 + 10000);
    }

    public static void onRowToggle(final ToggleEvent event) {
        final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected Vehicle",
                    "Manufacturer:" + ((Vehicle) event.getData()).getManufacturer());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}