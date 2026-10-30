/*
 * Copyright (c) 2011-2026 PrimeFaces Extensions
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
package org.primefaces.extensions.integrationtests.blockui;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.primefaces.extensions.integrationtests.AbstractPrimeExtensionsPageTest;
import org.primefaces.selenium.AbstractPrimePage;
import org.primefaces.selenium.PrimeSelenium;

public class BlockUI001Test extends AbstractPrimeExtensionsPageTest {

    @Test
    @Order(1)
    @DisplayName("BlockUI: block and unblock with the draggable message of the jQuery UI shim")
    void blockAndUnblock(Page page) {
        PrimeSelenium.executeScript("PF('blockWidget').block();");
        PrimeSelenium.waitGui().until(d -> (Boolean) PrimeSelenium.executeScript("return $('#panel .blockMsg').length === 1;"));

        // the themed message is made draggable by $.fn.draggable of the shim
        assertEquals(true, PrimeSelenium.executeScript("return !!PrimeFaces.interactions.instance($('#panel .blockMsg'), 'draggable');"));

        PrimeSelenium.executeScript("PF('blockWidget').unblock();");
        PrimeSelenium.waitGui().until(d -> (Boolean) PrimeSelenium.executeScript("return $('#panel .blockMsg').length === 0;"));
        assertNoJavascriptErrors();
    }

    public static class Page extends AbstractPrimePage {

        @Override
        public String getLocation() {
            return "blockui/blockUI001.xhtml";
        }
    }
}
