/*
 * Copyright 2011 PrimeFaces Extensions.
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
 *
 * $Id$
 */

package org.primefaces.extensions.event;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.Behavior;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.AjaxBehaviorListener;
import javax.faces.event.FacesListener;

/**
 * Event which is triggered by the
 * {@link org.primefaces.extensions.component.imageareaselect.ImageAreaSelect} component.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.1
 */
@SuppressWarnings("serial")
public class ImageAreaSelectEvent extends AjaxBehaviorEvent {

	private int height;
	private int width;
	private int x1;
	private int x2;
	private int y1;
	private int y2;
	private int imgHeight;
	private int imgWidth;
	private String imgSrc;

	public ImageAreaSelectEvent(final UIComponent component,
			final Behavior behavior,
			final int height,
			final int width,
			final int x1,
			final int x2,
			final int y1,
			final int y2,
			final int imgHeight,
			final int imgWidth,
			final String imgSrc) {
		super(component, behavior);
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		this.height = height;
		this.width = width;
		this.imgHeight = imgHeight;
		this.imgWidth = imgWidth;
		this.imgSrc = imgSrc;
	}

	@Override
	public boolean isAppropriateListener(final FacesListener facesListener) {
		return true;
	}

	@Override
	public void processListener(final FacesListener facesListener) {
		if (facesListener instanceof AjaxBehaviorListener) {
			((AjaxBehaviorListener) facesListener).processAjaxBehavior(this);
		}
	}

	public final int getHeight() {
		return height;
	}

	public final int getWidth() {
		return width;
	}

	public final int getX1() {
		return x1;
	}

	public final int getX2() {
		return x2;
	}

	public final int getY1() {
		return y1;
	}

	public final int getY2() {
		return y2;
	}

	public final int getImgHeight() {
		return imgHeight;
	}

	public final int getImgWidth() {
		return imgWidth;
	}

	public final String getImgSrc() {
		return imgSrc;
	}
}
