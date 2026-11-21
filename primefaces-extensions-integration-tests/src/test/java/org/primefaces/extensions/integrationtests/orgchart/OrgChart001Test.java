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
package org.primefaces.extensions.integrationtests.orgchart;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.primefaces.extensions.integrationtests.AbstractPrimeExtensionsPageTest;
import org.primefaces.selenium.AbstractPrimePage;
import org.primefaces.selenium.PrimeSelenium;

public class OrgChart001Test extends AbstractPrimeExtensionsPageTest {

    @Test
    @Order(1)
    @DisplayName("OrgChart: Font Awesome parent node symbol is not given the OCI theme class")
    void fontAwesomeParentNodeSymbol(Page page) {
        WebElement icon = PrimeSelenium.waitGui().until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector(".orgchart .node .title .parentNodeSymbol")));
        assertNotNull(icon);

        String classes = icon.getAttribute("class");
        assertTrue(PrimeSelenium.hasCssClass(icon, "fa"), classes);
        assertTrue(PrimeSelenium.hasCssClass(icon, "fa-folder-open"), classes);
        assertFalse(PrimeSelenium.hasCssClass(icon, "oci"), classes);

        String titleFont = page.title.getCssValue("font-family");
        String iconFont = icon.getCssValue("font-family");
        assertEquals(titleFont, iconFont, "Font Awesome icon must inherit the node title font");

        assertNoJavascriptErrors();
    }

    public static class Page extends AbstractPrimePage {

        @FindBy(css = ".orgchart .node .title")
        WebElement title;

        @Override
        public String getLocation() {
            return "orgchart/orgchart001.xhtml";
        }
    }
}
