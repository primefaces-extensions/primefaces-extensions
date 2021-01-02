/*
 * Copyright (c) 2011-2021 PrimeFaces Extensions
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
package org.primefaces.extensions.event;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.Behavior;

import org.primefaces.event.AbstractAjaxBehaviorEvent;

/**
 * Event which is triggered by the {@link org.primefaces.extensions.component.speedtest.Speedtest} component has the test's finished.
 *
 * @author ssibitz ssibitz@me.com
 * @since 6.1
 */
public class SpeedTestEvent extends AbstractAjaxBehaviorEvent {

    public static final String NAME = "speedtest";
    private static final long serialVersionUID = 1L;

    private final Double pingTimeMS;

    private final Double jitterTimeMS;

    private final Double speedMbpsDownload;

    private final Double speedMbpsUpload;

    public SpeedTestEvent(final UIComponent component, final Behavior behavior,
                final Double _PingTimeMS, final Double _JitterTimeMS, final Double _SpeedMbpsDownload, final Double _SpeedMbpsUpload) {
        super(component, behavior);
        pingTimeMS = _PingTimeMS;
        jitterTimeMS = _JitterTimeMS;
        speedMbpsDownload = _SpeedMbpsDownload;
        speedMbpsUpload = _SpeedMbpsUpload;
    }

    public final Double getPingTimeMS() {
        return pingTimeMS;
    }

    public final Double getJitterTimeMS() {
        return jitterTimeMS;
    }

    public final Double getSpeedMbpsDownload() {
        return speedMbpsDownload;
    }

    public final Double getSpeedMbpsUpload() {
        return speedMbpsUpload;
    }
}
