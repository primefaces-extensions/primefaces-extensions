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
package org.primefaces.extensions.component.speedtest;

import jakarta.faces.component.UIComponentBase;

import org.primefaces.cdk.api.FacesBehaviorEvent;
import org.primefaces.cdk.api.FacesBehaviorEvents;
import org.primefaces.cdk.api.FacesComponentBase;
import org.primefaces.cdk.api.Property;
import org.primefaces.component.api.StyleAware;
import org.primefaces.component.api.Widget;
import org.primefaces.extensions.event.SpeedTestEvent;

/**
 * Component base class for the <code>Speedtest</code> component.
 *
 * @author ssibitz ssibitz@me.com
 * @since 6.2
 */
@FacesComponentBase
@FacesBehaviorEvents({
            @FacesBehaviorEvent(name = SpeedTestEvent.NAME, event = SpeedTestEvent.class, description = "Fires when the speedtest is finished.",
                        defaultEvent = true)
})
public abstract class SpeedtestBase extends UIComponentBase implements Widget, StyleAware {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Speedtest";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.SpeedtestRenderer";

    public SpeedtestBase() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Property(defaultValue = "Ping", description = "Caption for Ping gauge.")
    public abstract String getCaptionPing();

    @Property(defaultValue = "Jitter", description = "Caption for Jitter gauge.")
    public abstract String getCaptionJitter();

    @Property(defaultValue = "Download", description = "Caption for Download gauge.")
    public abstract String getCaptionDownload();

    @Property(defaultValue = "Upload", description = "Caption for Upload gauge.")
    public abstract String getCaptionUpload();

    @Property(defaultValue = "#993333", description = "Color for Ping gauge.")
    public abstract String getColorPing();

    @Property(defaultValue = "#d2900a", description = "Color for Jitter gauge.")
    public abstract String getColorJitter();

    @Property(defaultValue = "#339933", description = "Color for Download gauge.")
    public abstract String getColorDownload();

    @Property(defaultValue = "#333399", description = "Color for Upload gauge.")
    public abstract String getColorUpload();

    @Property(description = "URL to the file used for testing (should be some megabytes).", required = true)
    public abstract String getFile();
}
