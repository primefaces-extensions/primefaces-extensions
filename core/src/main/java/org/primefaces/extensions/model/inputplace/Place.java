/*
 * Copyright (c) 2011-2024 PrimeFaces Extensions
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
package org.primefaces.extensions.model.inputplace;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Model object for InputPlace representing a single Google Place location.
 *
 * @author Melloware &lt;mellowaredev@gmail.com&gt;
 * @since 14.0
 */
public class Place implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String placeId;
    private final String formattedAddress;
    private final String addressLine;
    private final String postalCode;
    private final String city;
    private final String state;
    private final String country;
    private final String administrativeAreaLevel1;
    private final String administrativeAreaLevel2;
    private final String administrativeAreaLevel3;
    private final String types;
    private final double latitude;
    private final double longitude;

    public Place(final String clientId, final Map<String, String> params) {
        this.placeId = params.get(clientId + "_place_id");
        this.formattedAddress = params.get(clientId + "_formatted_address");
        this.addressLine = params.get(clientId + "_address");
        this.postalCode = params.get(clientId + "_postcode");
        this.city = params.get(clientId + "_city");
        this.state = params.get(clientId + "_state");
        this.country = params.get(clientId + "_country");
        this.latitude = Double.parseDouble(params.get(clientId + "_lat"));
        this.longitude = Double.parseDouble(params.get(clientId + "_lng"));
        this.administrativeAreaLevel1 = params.get(clientId + "_administrative_area_level_1");
        this.administrativeAreaLevel2 = params.get(clientId + "_administrative_area_level_2");
        this.administrativeAreaLevel3 = params.get(clientId + "_administrative_area_level_3");
        this.types = params.get(clientId + "_types");
    }

    public Place(String placeId, String formattedAddress, String addressLine, String postalCode, String city, String state, String country,
                String administrativeAreaLevel1, String administrativeAreaLevel2, String administrativeAreaLevel3, String types, double latitude,
                double longitude) {
        this.placeId = placeId;
        this.formattedAddress = formattedAddress;
        this.addressLine = addressLine;
        this.postalCode = postalCode;
        this.city = city;
        this.state = state;
        this.country = country;
        this.administrativeAreaLevel1 = administrativeAreaLevel1;
        this.administrativeAreaLevel2 = administrativeAreaLevel2;
        this.administrativeAreaLevel3 = administrativeAreaLevel3;
        this.types = types;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getAdministrativeAreaLevel1() {
        return administrativeAreaLevel1;
    }

    public String getAdministrativeAreaLevel2() {
        return administrativeAreaLevel2;
    }

    public String getAdministrativeAreaLevel3() {
        return administrativeAreaLevel3;
    }

    public String getTypes() {
        return types;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Place place = (Place) o;
        return Objects.equals(getPlaceId(), place.getPlaceId()) && Objects.equals(getFormattedAddress(), place.getFormattedAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPlaceId(), getFormattedAddress());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Place.class.getSimpleName() + "[", "]")
                    .add("placeId='" + placeId + "'")
                    .add("formattedAddress='" + formattedAddress + "'")
                    .add("addressLine='" + addressLine + "'")
                    .add("postalCode='" + postalCode + "'")
                    .add("city='" + city + "'")
                    .add("state='" + state + "'")
                    .add("country='" + country + "'")
                    .add("administrativeAreaLevel1='" + administrativeAreaLevel1 + "'")
                    .add("administrativeAreaLevel2='" + administrativeAreaLevel2 + "'")
                    .add("administrativeAreaLevel3='" + administrativeAreaLevel3 + "'")
                    .add("types='" + types + "'")
                    .add("latitude=" + latitude)
                    .add("longitude=" + longitude)
                    .toString();
    }
}