package org.primefaces.extensions.util;

import java.awt.Color;

/**
 * Util class to manipulate {@link Color} objects
 * 
 * @author f.strazzullo
 *
 */
public class ColorUtils {

	private ColorUtils() {
	}

	/**
	 * <p>Convert a {@link Color} instance in a HEX format (#FFFFFF) String</p> 
	 *
	 */
	public static String colorToHex(Color color) {

		String red = Integer.toHexString(color.getRed());
		if (red.length() < 2) {
			red = "0" + red;
		}

		String blue = Integer.toHexString(color.getBlue());
		if (blue.length() < 2) {
			blue = "0" + blue;
		}

		String green = Integer.toHexString(color.getGreen());
		if (green.length() < 2) {
			green = "0" + green;
		}

		return "#" + red + green + blue;
	}
	
}
