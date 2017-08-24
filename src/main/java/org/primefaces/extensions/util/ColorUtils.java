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
package org.primefaces.extensions.util;

import java.awt.Color;

/**
 * Util class to manipulate {@link Color} objects
 * 
 * @author f.strazzullo
 */
public class ColorUtils {

    private ColorUtils() {
    }

    /**
     * <p>
     * Convert a {@link Color} instance in a HEX format (#FFFFFF) String
     * </p>
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
