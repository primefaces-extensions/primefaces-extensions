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
package org.primefaces.extensions.integrationtests.jqueryuishim;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.primefaces.extensions.integrationtests.AbstractPrimeExtensionsPageTest;
import org.primefaces.selenium.AbstractPrimePage;
import org.primefaces.selenium.PrimeSelenium;

public class JqueryUiShim001Test extends AbstractPrimeExtensionsPageTest {

    @Test
    @Order(1)
    @DisplayName("jQuery UI shim: $.fn plugins exist and create PrimeFaces interactions")
    void pluginsCreateInteractions(Page page) {
        assertEquals(true, PrimeSelenium.executeScript(
                    "return ['draggable', 'droppable', 'sortable', 'resizable'].every(function (t) { return typeof $.fn[t] === 'function'; });"));

        PrimeSelenium.executeScript("$('#drag').draggable({ axis: 'x' }); $('#drop').droppable(); $('#resize').resizable();");

        assertEquals(true, PrimeSelenium.executeScript(
                    "return PrimeFaces.interactions.instance($('#drag'), 'draggable') === $('#drag').draggable('instance');"));
        assertTrue(page.drag.getAttribute("class").contains("p-draggable"));
        assertEquals(true, PrimeSelenium.executeScript(
                    "return $('#drop').data('ui-droppable') === PrimeFaces.interactions.instance($('#drop'), 'droppable');"));
        assertEquals(true, PrimeSelenium.executeScript("return !!PrimeFaces.interactions.instance($('#resize'), 'resizable');"));
        assertNoJavascriptErrors();
    }

    @Test
    @Order(2)
    @DisplayName("jQuery UI shim: option, enable, disable and destroy methods")
    void methods(Page page) {
        PrimeSelenium.executeScript("$('#list').sortable({ items: 'li' });");

        // setter returns the jQuery object for chaining, getter returns the value
        assertEquals(true, PrimeSelenium.executeScript("return $('#list').sortable('option', 'axis', 'y').is('#list');"));
        assertEquals("y", PrimeSelenium.executeScript("return $('#list').sortable('option', 'axis');"));
        // calling with options again updates the options of the existing sortable
        PrimeSelenium.executeScript("$('#list').sortable({ axis: 'x' });");
        assertEquals("x", PrimeSelenium.executeScript("return $('#list').sortable('option').axis;"));

        PrimeSelenium.executeScript("$('#list').sortable('disable');");
        assertTrue(page.list.getAttribute("class").contains("p-sortable-disabled"));
        PrimeSelenium.executeScript("$('#list').sortable('enable');");
        assertFalse(page.list.getAttribute("class").contains("p-sortable-disabled"));

        PrimeSelenium.executeScript("$('#list').sortable('destroy');");
        assertEquals(true, PrimeSelenium.executeScript(
                    "return !PrimeFaces.interactions.instance($('#list'), 'sortable') && $('#list').data('ui-sortable') === undefined;"));

        // calling a method before initialization throws like jQuery UI
        assertEquals(true, PrimeSelenium.executeScript(
                    "try { $('#list').sortable('toArray'); return false; } catch (e) { return /prior to initialization/.test(e.message); }"));
        assertNoJavascriptErrors();
    }

    @Test
    @Order(3)
    @DisplayName("jQuery UI shim: sortable toArray and serialize")
    void toArrayAndSerialize(Page page) {
        PrimeSelenium.executeScript("$('#list').sortable({ items: 'li' });");

        assertEquals(List.of("item_1", "item_2", "item_3"), PrimeSelenium.executeScript("return $('#list').sortable('toArray');"));
        assertEquals("item[]=1&item[]=2&item[]=3", PrimeSelenium.executeScript("return $('#list').sortable('serialize');"));
        assertEquals("pos=1&pos=2&pos=3", PrimeSelenium.executeScript("return $('#list').sortable('serialize', { key: 'pos' });"));
        assertNoJavascriptErrors();
    }

    @Test
    @Order(4)
    @DisplayName("jQuery UI shim: dragging sorts the items and fires the update callback")
    void dragSorts(Page page) {
        PrimeSelenium.executeScript("window.shimUpdated = false;"
                    + "$('#list').sortable({ items: 'li', update: function (event, ui) { window.shimUpdated = ui.item.attr('id'); } });");

        Actions actions = new Actions(page.getWebDriver());
        actions.clickAndHold(page.item1);
        for (int i = 0; i < 12; i++) {
            actions.moveByOffset(0, 10);
        }
        actions.release().perform();

        assertEquals("item_1", PrimeSelenium.executeScript("return window.shimUpdated;"));
        assertNotEquals(List.of("item_1", "item_2", "item_3"), PrimeSelenium.executeScript("return $('#list').sortable('toArray');"));
        assertNoJavascriptErrors();
    }

    @Test
    @Order(5)
    @DisplayName("jQuery UI shim: $.browser is PrimeFaces.env.browser")
    void browser(Page page) {
        assertEquals(true, PrimeSelenium.executeScript("return $.browser === PrimeFaces.env.browser && !!$.browser.name;"));
        assertNoJavascriptErrors();
    }

    public static class Page extends AbstractPrimePage {

        @FindBy(id = "list")
        WebElement list;

        @FindBy(id = "item_1")
        WebElement item1;

        @FindBy(id = "drag")
        WebElement drag;

        @Override
        public String getLocation() {
            return "jqueryuishim/jqueryUiShim001.xhtml";
        }
    }
}
