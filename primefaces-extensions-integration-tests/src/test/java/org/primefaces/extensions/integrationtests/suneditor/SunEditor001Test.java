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
package org.primefaces.extensions.integrationtests.suneditor;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.primefaces.extensions.integrationtests.AbstractPrimeExtensionsPageTest;
import org.primefaces.selenium.AbstractPrimePage;
import org.primefaces.selenium.PrimeSelenium;
import org.primefaces.selenium.component.CommandButton;
import org.primefaces.selenium.component.Messages;
import org.primefaces.selenium.component.SunEditor;
import org.primefaces.selenium.component.model.Severity;

public class SunEditor001Test extends AbstractPrimeExtensionsPageTest {

    @Test
    @Order(1)
    @DisplayName("SunEditor: renders editor with initial value")
    void render(Page page) {
        SunEditor editor = page.basic;
        assertTrue(editor.isEditorReady());
        assertTrue(editor.getEditorContainer().isDisplayed());
        assertTrue(editor.getToolbar().isDisplayed());
        assertEquals("Hello SunEditor", editor.getText());
        assertTrue(editor.getContent().contains("<strong>SunEditor</strong>"), editor.getContent());
        assertFalse(editor.isDisabled());
        assertFalse(editor.isReadOnly());
        assertNoJavascriptErrors();
    }

    @Test
    @Order(2)
    @DisplayName("SunEditor: typing updates the editor and the underlying textarea")
    void typeText(Page page) {
        SunEditor editor = page.basic;

        editor.setText("Typed by Selenium");

        assertEquals("Typed by Selenium", editor.getText());
        assertTrue(editor.getInputValue().contains("Typed by Selenium"), editor.getInputValue());
    }

    @Test
    @Order(3)
    @DisplayName("SunEditor: toolbar button formats the content")
    void toolbarFormatting(Page page) {
        SunEditor editor = page.basic;
        assertTrue(editor.hasToolbarButton("bold"));

        editor.setText("Format me");
        // select all content and apply underline
        editor.getWysiwyg().click();
        editor.getWysiwyg().sendKeys(org.openqa.selenium.Keys.chord(org.openqa.selenium.Keys.CONTROL, "a"));
        editor.clickToolbarButton("underline");

        PrimeSelenium.waitGui().until(driver -> editor.getContent().contains("<u>"));
        assertTrue(editor.getContent().contains("Format me"));
    }

    @Test
    @Order(4)
    @DisplayName("SunEditor: clear widget method empties editor and textarea")
    void clear(Page page) {
        SunEditor editor = page.basic;
        assertFalse(editor.getText().isEmpty());

        page.clear.click();

        assertTrue(editor.getText().isEmpty(), editor.getContent());
        assertFalse(editor.getInputValue().contains("Hello"), editor.getInputValue());
    }

    @Test
    @Order(5)
    @DisplayName("SunEditor: submit sends typed content to the server")
    void submit(Page page) {
        SunEditor editor = page.basic;
        editor.setText("Saved on server");

        PrimeSelenium.guardAjax(page.submit).click();

        assertEquals("Saved on server", page.output.getText());
        Messages messages = page.messages;
        assertEquals(1, messages.getMessagesBySeverity(Severity.INFO).size());
        assertTrue(messages.getMessage(0).getSummary().contains("Submitted"));
        assertNoJavascriptErrors();
    }

    @Test
    @Order(6)
    @DisplayName("SunEditor: ajax change behavior is fired and updates the model")
    void ajaxChange(Page page) {
        SunEditor editor = page.ajax;
        assertEquals("0", page.changeCount.getText());

        editor.setText("Changed via ajax");

        PrimeSelenium.waitGui().until(driver -> "Changed via ajax".equals(page.ajaxOutput.getText()));
        assertNotEquals("0", page.changeCount.getText());
    }

    @Test
    @Order(7)
    @DisplayName("SunEditor: disable and enable widget methods")
    void disableEnable(Page page) {
        SunEditor editor = page.basic;
        assertFalse(editor.isDisabled());

        page.disable.click();
        assertTrue(editor.isDisabled());
        assertTrue(editor.getRoot().getAttribute("disabled") != null);

        page.enable.click();
        assertFalse(editor.isDisabled());
        editor.setText("Enabled again");
        assertEquals("Enabled again", editor.getText());
    }

    public static class Page extends AbstractPrimePage {

        @FindBy(id = "form:basic")
        SunEditor basic;

        @FindBy(id = "form:ajax")
        SunEditor ajax;

        @FindBy(id = "form:output")
        WebElement output;

        @FindBy(id = "form:ajaxOutput")
        WebElement ajaxOutput;

        @FindBy(id = "form:changeCount")
        WebElement changeCount;

        @FindBy(id = "form:msgs")
        Messages messages;

        @FindBy(id = "form:submitButton")
        CommandButton submit;

        @FindBy(id = "form:clearButton")
        CommandButton clear;

        @FindBy(id = "form:disableButton")
        CommandButton disable;

        @FindBy(id = "form:enableButton")
        CommandButton enable;

        @Override
        public String getLocation() {
            return "suneditor/sunEditor001.xhtml";
        }
    }
}
