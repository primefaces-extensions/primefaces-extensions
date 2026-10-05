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
package org.primefaces.selenium.component;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.primefaces.selenium.PrimeSelenium;
import org.primefaces.selenium.component.base.AbstractComponent;

/**
 * Component wrapper for the PrimeFaces Extensions {@code pe:sunEditor}.
 */
public abstract class SunEditor extends AbstractComponent {

    private static final String EDITOR_CLASS = "sun-editor";
    private static final String TOOLBAR_CLASS = "se-toolbar";

    public boolean isEditorReady() {
        return Boolean.TRUE.equals(PrimeSelenium.executeScript(
                    "return !!(" + getWidgetByIdScript() + " && " + getWidgetByIdScript() + ".editor);"));
    }

    public void waitForEditor() {
        PrimeSelenium.waitGui().until(driver -> isEditorReady());
    }

    /**
     * Resolved via the widget instead of a global CSS lookup so several editors per page do not clash.
     */
    public WebElement getWysiwyg() {
        waitForEditor();
        return PrimeSelenium.executeScript("return " + getWidgetByIdScript() + ".editor.$.frameContext.get('wysiwyg');");
    }

    public WebElement getEditorContainer() {
        return getWysiwyg().findElement(By.xpath("ancestor::div[contains(@class,'" + EDITOR_CLASS + "')][1]"));
    }

    public WebElement getToolbar() {
        return getEditorContainer().findElement(By.className(TOOLBAR_CLASS));
    }

    public WebElement getToolbarButton(String command) {
        return getToolbar().findElement(By.cssSelector("button[data-command='" + command + "']"));
    }

    public boolean hasToolbarButton(String command) {
        return !getToolbar().findElements(By.cssSelector("button[data-command='" + command + "']")).isEmpty();
    }

    public void clickToolbarButton(String command) {
        getToolbarButton(command).click();
    }

    public String getContent() {
        return getWysiwyg().getAttribute("innerHTML");
    }

    public String getText() {
        return getWysiwyg().getText();
    }

    /**
     * The value of the underlying textarea, i.e. what is submitted to the server.
     */
    public String getInputValue() {
        return PrimeSelenium.executeScript("return " + getWidgetByIdScript() + ".input.val();");
    }

    /**
     * Types like a user (select all, then type) and waits until the text is synced to the textarea.
     */
    public void setText(String text) {
        WebElement wysiwyg = getWysiwyg();
        wysiwyg.click();
        wysiwyg.sendKeys(Keys.chord(Keys.CONTROL, "a"), text);
        PrimeSelenium.waitGui().until(driver -> getInputValue().contains(text));
    }

    public void type(String text) {
        WebElement wysiwyg = getWysiwyg();
        wysiwyg.click();
        wysiwyg.sendKeys(text);
    }

    public void clear() {
        PrimeSelenium.executeScript(getWidgetByIdScript() + ".clear();");
    }

    public void disable() {
        PrimeSelenium.executeScript(getWidgetByIdScript() + ".disable();");
    }

    public void enable() {
        PrimeSelenium.executeScript(getWidgetByIdScript() + ".enable();");
    }

    public boolean isDisabled() {
        return Boolean.TRUE.equals(PrimeSelenium.executeScript("return " + getWidgetByIdScript() + ".disabled;"));
    }

    /**
     * SunEditor marks read-only with a CSS class, while disabled sets contenteditable to false.
     */
    public boolean isReadOnly() {
        WebElement wysiwyg = getWysiwyg();
        String styleClass = wysiwyg.getAttribute("class");
        return (styleClass != null && styleClass.contains("se-read-only")) || !"true".equals(wysiwyg.getAttribute("contenteditable"));
    }
}
