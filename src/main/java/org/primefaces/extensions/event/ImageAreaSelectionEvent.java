/*
 * Copyright 2011 Thomas Andraschko.
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

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

@SuppressWarnings("serial")
public class ImageAreaSelectionEvent extends FacesEvent {

	private long height;
	private long width;
	private long x1;
	private long x2;
	private long y1;
	private long y2;
	private long imgHeight;
	private long imgWidth;
	private String imgSrc;

	public ImageAreaSelectionEvent(UIComponent component, 
			long height,
			long width,
			long x1,
			long x2,
			long y1,
			long y2,
			long imgHeight,
			long imgWidth,
			String imgSrc) {
		super(component);
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
	public boolean isAppropriateListener(FacesListener listener) {
		return false;
	}

	@Override
	public void processListener(FacesListener listener) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return the height
	 */
	public long getHeight() {
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(long height) {
		this.height = height;
	}

	/**
	 * @return the width
	 */
	public long getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(long width) {
		this.width = width;
	}

	/**
	 * @return the x1
	 */
	public long getX1() {
		return x1;
	}

	/**
	 * @param x1 the x1 to set
	 */
	public void setX1(long x1) {
		this.x1 = x1;
	}

	/**
	 * @return the x2
	 */
	public long getX2() {
		return x2;
	}

	/**
	 * @param x2 the x2 to set
	 */
	public void setX2(long x2) {
		this.x2 = x2;
	}

	/**
	 * @return the y1
	 */
	public long getY1() {
		return y1;
	}

	/**
	 * @param y1 the y1 to set
	 */
	public void setY1(long y1) {
		this.y1 = y1;
	}

	/**
	 * @return the y2
	 */
	public long getY2() {
		return y2;
	}

	/**
	 * @param y2 the y2 to set
	 */
	public void setY2(long y2) {
		this.y2 = y2;
	}

	/**
	 * @return the imgHeight
	 */
	public long getImgHeight() {
		return imgHeight;
	}

	/**
	 * @param imgHeight the imgHeight to set
	 */
	public void setImgHeight(long imgHeight) {
		this.imgHeight = imgHeight;
	}

	/**
	 * @return the imgWidth
	 */
	public long getImgWidth() {
		return imgWidth;
	}

	/**
	 * @param imgWidth the imgWidth to set
	 */
	public void setImgWidth(long imgWidth) {
		this.imgWidth = imgWidth;
	}

	/**
	 * @return the imgSrc
	 */
	public String getImgSrc() {
		return imgSrc;
	}

	/**
	 * @param imgSrc the imgSrc to set
	 */
	public void setImgSrc(String imgSrc) {
		this.imgSrc = imgSrc;
	}
}
