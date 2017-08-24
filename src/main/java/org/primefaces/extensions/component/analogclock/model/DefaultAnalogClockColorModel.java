/**
 * Copyright 2011-2017 PrimeFaces Extensions
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
package org.primefaces.extensions.component.analogclock.model;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import org.primefaces.extensions.util.ColorUtils;
import org.primefaces.extensions.util.json.GsonConverter;

public class DefaultAnalogClockColorModel implements AnalogClockColorModel {

    private static final long serialVersionUID = -2809548631544370757L;

    private Color face;
    private Color border;
    private Color hourSigns;
    private Color hourHand;
    private Color minuteHand;
    private Color secondHand;
    private Color pin;

    public DefaultAnalogClockColorModel() {
    }

    public Color getFace() {
        return face;
    }

    public void setFace(Color face) {
        this.face = face;
    }

    public Color getBorder() {
        return border;
    }

    public void setBorder(Color border) {
        this.border = border;
    }

    public Color getHourSigns() {
        return hourSigns;
    }

    public void setHourSigns(Color hourSigns) {
        this.hourSigns = hourSigns;
    }

    public Color getHourHand() {
        return hourHand;
    }

    public void setHourHand(Color hourHand) {
        this.hourHand = hourHand;
    }

    public Color getMinuteHand() {
        return minuteHand;
    }

    public void setMinuteHand(Color minuteHand) {
        this.minuteHand = minuteHand;
    }

    public Color getSecondHand() {
        return secondHand;
    }

    public void setSecondHand(Color secondHand) {
        this.secondHand = secondHand;
    }

    public Color getPin() {
        return pin;
    }

    public void setPin(Color pin) {
        this.pin = pin;
    }

    public String toJson() {

        Map<String, String> map = new HashMap<String, String>(0);

        map.put("face", ColorUtils.colorToHex(this.getFace()));
        map.put("border", ColorUtils.colorToHex(this.getBorder()));
        map.put("hourHand", ColorUtils.colorToHex(this.getHourHand()));
        map.put("hourSigns", ColorUtils.colorToHex(this.getHourSigns()));
        map.put("minuteHand", ColorUtils.colorToHex(this.getMinuteHand()));
        map.put("secondHand", ColorUtils.colorToHex(this.getSecondHand()));
        map.put("secondSigns", ColorUtils.colorToHex(this.getHourSigns()));
        map.put("pin", ColorUtils.colorToHex(this.getPin()));

        return GsonConverter.getGson().toJson(map, Map.class);
    }

}
