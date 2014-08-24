package org.primefaces.extensions.component.analogclock.model;

import java.awt.Color;
import java.io.Serializable;

public interface AnalogClockColorModel extends Serializable {

	public abstract void setPin(Color pin);

	public abstract Color getPin();

	public abstract void setSecondHand(Color secondHand);

	public abstract Color getSecondHand();

	public abstract void setMinuteHand(Color minuteHand);

	public abstract Color getMinuteHand();

	public abstract void setHourHand(Color hourHand);

	public abstract Color getHourHand();

	public abstract void setHourSigns(Color hourSigns);

	public abstract Color getHourSigns();

	public abstract void setBorder(Color border);

	public abstract Color getBorder();

	public abstract void setFace(Color face);

	public abstract Color getFace();

}
