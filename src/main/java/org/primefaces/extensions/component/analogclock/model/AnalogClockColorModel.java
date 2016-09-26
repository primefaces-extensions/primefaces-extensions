package org.primefaces.extensions.component.analogclock.model;

import java.awt.Color;
import java.io.Serializable;

public interface AnalogClockColorModel extends Serializable {

   void setPin(Color pin);

   Color getPin();

   void setSecondHand(Color secondHand);

   Color getSecondHand();

   void setMinuteHand(Color minuteHand);

   Color getMinuteHand();

   void setHourHand(Color hourHand);

   Color getHourHand();

   void setHourSigns(Color hourSigns);

   Color getHourSigns();

   void setBorder(Color border);

   Color getBorder();

   void setFace(Color face);

   Color getFace();

   String toJson();

}
