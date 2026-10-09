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
package org.primefaces.extensions.showcase.controller;

import java.io.Serializable;
import java.time.LocalTime;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named
@ViewScoped
public class ClockPickerAlignController implements Serializable {

    private static final long serialVersionUID = 897540091000342926L;

    private LocalTime timeLeft;
    private LocalTime timeCenter;
    private LocalTime timeRight;
    private LocalTime timeTop;
    private LocalTime timeBottom;

    public ClockPickerAlignController() {
        timeLeft = LocalTime.of(9, 0);
        timeCenter = LocalTime.of(12, 0);
        timeRight = LocalTime.of(15, 30);
        timeTop = LocalTime.of(18, 45);
        timeBottom = LocalTime.of(10, 0);
    }

    public LocalTime getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(LocalTime timeLeft) {
        this.timeLeft = timeLeft;
    }

    public LocalTime getTimeCenter() {
        return timeCenter;
    }

    public void setTimeCenter(LocalTime timeCenter) {
        this.timeCenter = timeCenter;
    }

    public LocalTime getTimeRight() {
        return timeRight;
    }

    public void setTimeRight(LocalTime timeRight) {
        this.timeRight = timeRight;
    }

    public LocalTime getTimeTop() {
        return timeTop;
    }

    public void setTimeTop(LocalTime timeTop) {
        this.timeTop = timeTop;
    }

    public LocalTime getTimeBottom() {
        return timeBottom;
    }

    public void setTimeBottom(LocalTime timeBottom) {
        this.timeBottom = timeBottom;
    }
}
