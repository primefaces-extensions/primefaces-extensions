package org.primefaces.extensions.component.analogclock.model;

import java.awt.Color;

public class DefaultAnalogClockColorModel implements AnalogClockColorModel {

	private static final long serialVersionUID = -2809548631544370757L;

	private Color face;
	private Color border;
	private Color hourSigns;
	private Color hourHand;
	private Color minuteHand;
	private Color secondHand;
	private Color pin;
	
	public DefaultAnalogClockColorModel(){}

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

	
}
