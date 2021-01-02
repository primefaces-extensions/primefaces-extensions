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
package org.primefaces.extensions.showcase.model;

import java.io.*;
import java.util.*;

/**
 * Vehicle
 *
 * @author Sudheer Jonna / last modified by $Author$
 * @version $Revision: 1.0 $
 * @since 0.7.0
 */
public class Vehicle implements Serializable {

    private static final long serialVersionUID = 1L;

    private int model;
    private String manufacturer;
    private String color;
    private int speed;
    private int price;
    private int year;
    private List<Customer> customers = new ArrayList<>();

    public Vehicle(int model, String manufacturer, String color, int speed, int price, int year, List<Customer> customers) {
        this.model = model;
        this.manufacturer = manufacturer;
        this.color = color;
        this.speed = speed;
        this.price = price;
        this.year = year;
        this.customers = customers;
    }

    public int getModel() {
        return model;
    }

    public void setModel(int model) {
        this.model = model;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj); // To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public int hashCode() {
        return super.hashCode(); // To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public String toString() {
        return "Car{" +
                    "model=" + model +
                    ", manufacturer='" + manufacturer + '\'' +
                    ", color='" + color + '\'' +
                    ", speed=" + speed +
                    ", price=" + price +
                    ", year=" + year +
                    '}';
    }
}