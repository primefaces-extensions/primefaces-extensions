/*
 * Copyright 2011-2012 PrimeFaces Extensions.
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

package org.primefaces.extensions.component.ckeditor;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UINamingContainer;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.component.html.HtmlInputTextarea;
import javax.faces.context.FacesContext;

import org.primefaces.component.api.Widget;

/**
 * Component class for the <code>CKEditor</code> component.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.2
 */
@ResourceDependencies({
	@ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
	@ResourceDependency(library = "primefaces", name = "primefaces.js"),
	@ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js")
})
public class CKEditor extends HtmlInputTextarea implements ClientBehaviorHolder, Widget {

	public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.CKEditor";
	public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
	private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.CKEditorRenderer";
	private static final String OPTIMIZED_PACKAGE = "org.primefaces.extensions.component.";

	public static final String EVENT_SAVE = "save";
	public static final String EVENT_INITIALIZE = "initialize";
	public static final String EVENT_BLUR = "blur";
	public static final String EVENT_FOCUS = "focus";
	public static final String EVENT_WYSIWYG_MODE = "wysiwygMode";
	public static final String EVENT_SOURCE_MODE = "sourceMode";

	private static final Collection<String> EVENT_NAMES =
			Collections.unmodifiableCollection(Arrays.asList(EVENT_SAVE, EVENT_INITIALIZE, EVENT_BLUR, EVENT_FOCUS,
					EVENT_WYSIWYG_MODE, EVENT_SOURCE_MODE));

	/**
	 * Properties that are tracked by state saving.
	 *
	 * @author  Thomas Andraschko / last modified by $Author$
	 * @version $Revision$
	 */
	protected enum PropertyKeys {

		widgetVar,
		height,
		width,
		theme,
		skin,
		toolbar,
		readOnly,
		interfaceColor,
		language,
		defaultLanguage,
		contentsCss,
		customConfig,
		tabindex,
		escape;

		private String toString;

		PropertyKeys(final String toString) {
			this.toString = toString;
		}

		PropertyKeys() {
		}

		@Override
		public String toString() {
			return ((this.toString != null) ? this.toString : super.toString());
		}
	}

	public CKEditor() {
		setRendererType(DEFAULT_RENDERER);
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	@Override
	public Collection<String> getEventNames() {
		return EVENT_NAMES;
	}

	@Override
	public String getDefaultEventName() {
		return EVENT_SAVE;
	}

	public String getHeight() {
		return (String) getStateHelper().eval(PropertyKeys.height, "200px");
	}

	public void setHeight(final String height) {
		getStateHelper().put(PropertyKeys.height, height);
	}

	public String getWidth() {
		return (String) getStateHelper().eval(PropertyKeys.width, "600px");
	}

	public void setWidth(final String width) {
		getStateHelper().put(PropertyKeys.width, width);
	}

	public String getTheme() {
		return (String) getStateHelper().eval(PropertyKeys.theme, null);
	}

	public void setTheme(final String theme) {
		getStateHelper().put(PropertyKeys.theme, theme);
	}

	public String getSkin() {
		return (String) getStateHelper().eval(PropertyKeys.skin, null);
	}

	public void setSkin(final String skin) {
		getStateHelper().put(PropertyKeys.skin, skin);
	}

	public String getInterfaceColor() {
		return (String) getStateHelper().eval(PropertyKeys.interfaceColor, null);
	}

	public void setInterfaceColor(final String interfaceColor) {
		getStateHelper().put(PropertyKeys.interfaceColor, interfaceColor);
	}

	public boolean isReadOnly() {
		return (Boolean) getStateHelper().eval(PropertyKeys.readOnly, false);
	}

	public void setReadOnly(final boolean readOnly) {
		getStateHelper().put(PropertyKeys.readOnly, readOnly);
	}

	public String getToolbar() {
		return (String) getStateHelper().eval(PropertyKeys.toolbar, null);
	}

	public void setToolbar(final String toolbar) {
		getStateHelper().put(PropertyKeys.toolbar, toolbar);
	}

	public String getDefaultLanguage() {
		return (String) getStateHelper().eval(PropertyKeys.defaultLanguage, null);
	}

	public void setDefaultLanguage(final String defaultLanguage) {
		getStateHelper().put(PropertyKeys.defaultLanguage, defaultLanguage);
	}

	public String getLanguage() {
		return (String) getStateHelper().eval(PropertyKeys.language, null);
	}

	public void setLanguage(final String language) {
		getStateHelper().put(PropertyKeys.language, language);
	}

	public String getContentsCss() {
		return (String) getStateHelper().eval(PropertyKeys.contentsCss, null);
	}

	public void setContentsCss(final String contentsCss) {
		getStateHelper().put(PropertyKeys.contentsCss, contentsCss);
	}

	public String getWidgetVar() {
		return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
	}

	public void setWidgetVar(final String widgetVar) {
		getStateHelper().put(PropertyKeys.widgetVar, widgetVar);
	}

	public String getCustomConfig() {
		return (String) getStateHelper().eval(PropertyKeys.customConfig, null);
	}

	public void setCustomConfig(final String customConfig) {
		getStateHelper().put(PropertyKeys.customConfig, customConfig);
	}

	@Override
	public String getTabindex() {
		return (String) getStateHelper().eval(PropertyKeys.tabindex, null);
	}

	@Override
	public void setTabindex(final String tabindex) {
		getStateHelper().put(PropertyKeys.tabindex, tabindex);
	}

	public boolean isEscape() {
		return (Boolean) getStateHelper().eval(PropertyKeys.escape, true);
	}

	public void setEscape(final boolean escape) {
		getStateHelper().put(PropertyKeys.escape, escape);
	}

	public String resolveWidgetVar() {
		final FacesContext context = FacesContext.getCurrentInstance();
		final String userWidgetVar = (String) getAttributes().get(PropertyKeys.widgetVar.toString());

		if (userWidgetVar != null) {
			return userWidgetVar;
		}

		return "widget_" + getClientId(context).replaceAll("-|" + UINamingContainer.getSeparatorChar(context), "_");
	}
}
