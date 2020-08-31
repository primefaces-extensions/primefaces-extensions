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
package org.primefaces.extensions.showcase.controller.exporter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

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

    private final static String[] manufacturers;
    private final static String[] colors;

    private final List<Vehicle> vehicles;
    private List<Customer> customersDetails = new ArrayList<Customer>();

    private final List<Customer> customersDetailsList1 = new ArrayList<Customer>();
    private final List<Customer> customersDetailsList2 = new ArrayList<Customer>();

    static {
        manufacturers = new String[7];
        manufacturers[0] = "Mercedes";
        manufacturers[1] = "BMW";
        manufacturers[2] = "Volvo";
        manufacturers[3] = "Audi";
        manufacturers[4] = "Volkswagen";
        manufacturers[5] = "Ferrari";
        manufacturers[6] = "Ford";

        colors = new String[7];
        colors[0] = "Red";
        colors[1] = "White";
        colors[2] = "Yellow";
        colors[3] = "Green";
        colors[4] = "Blue";
        colors[5] = "Orange";
        colors[6] = "Black";
    }

    public VehicleTableController() {
        vehicles = new ArrayList<Vehicle>();

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
            customersDetails = new ArrayList<Customer>();
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

    private String populateRandomContactNumbers() {
        final Random generator = new Random();
        String strippedNum;
        int num1 = 0;
        int num2 = 0;
        int num3 = 0;
        num1 = generator.nextInt(600) + 100;
        num2 = generator.nextInt(641) + 100;
        num3 = generator.nextInt(8999) + 1000;

        strippedNum = Integer.toOctalString(num1);
        final String contactNumber = strippedNum + "-" + num2 + "-" + num3;
        return contactNumber;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    private String getRandomManufacturer() {
        return manufacturers[(int) (Math.random() * 6)];
    }

    public List<Customer> getCustomersDetails() {
        return customersDetails;
    }

    private int getRandomSpeed() {
        return (int) (Math.random() * 10 + 150);
    }

    private int getRandomYear() {
        return (int) (Math.random() * 50 + 1960);
    }

    private String getRandomColor() {
        return colors[(int) (Math.random() * 6)];
    }

    private int getRandomPrice() {
        return (int) (Math.random() * 10 + 10000);
    }

    public void onRowToggle(final ToggleEvent event) {
        final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected Vehicle",
                    "Manufacturer:" + ((Vehicle) event.getData()).getManufacturer());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}