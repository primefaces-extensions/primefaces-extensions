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
package org.primefaces.extensions.event;

import org.primefaces.event.AbstractAjaxBehaviorEvent;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.Behavior;

/**
 * Event which is triggered by the {@link org.primefaces.extensions.component.speedtest.Speedtest} component has the test's finished.
 *
 * @author ssibitz ssibitz@me.com
 * @since 6.1
 */
@SuppressWarnings("serial")
public class SpeedTestEvent extends AbstractAjaxBehaviorEvent {

    public static final String NAME = "speedtest";

    private final Double pingTimeMS;

    private final Double jitterTimeMS;

    private final Double speedMbpsDownload;

    private final Double speedMbpsUpload;

    public SpeedTestEvent(final UIComponent component, final Behavior behavior,
                          final Double _PingTimeMS, final Double _JitterTimeMS, final Double _SpeedMbpsDownload, final Double _SpeedMbpsUpload) {
        super(component, behavior);
        this.pingTimeMS         = _PingTimeMS;
        this.jitterTimeMS       = _JitterTimeMS;
        this.speedMbpsDownload  = _SpeedMbpsDownload;
        this.speedMbpsUpload    = _SpeedMbpsUpload;
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
