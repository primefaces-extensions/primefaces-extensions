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

package org.primefaces.extensions.component.codemirror;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIInput;
import javax.faces.component.UINamingContainer;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;

import org.primefaces.component.api.Widget;
import org.primefaces.extensions.event.CompleteEvent;
import org.primefaces.extensions.renderkit.widget.Option;

/**
 * Component class for the <code>CodeMirror</code> component.
 *
 * @author  Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since   0.3
 */
@ResourceDependencies({
	@ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
	@ResourceDependency(library = "primefaces", name = "primefaces.js"),
	@ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js"),
	@ResourceDependency(library = "primefaces-extensions", name = "codemirror/codemirror.js"),
	@ResourceDependency(library = "primefaces-extensions", name = "codemirror/codemirror.css"),
	@ResourceDependency(library = "primefaces-extensions", name = "codemirror/mode/modes.js")
})
public class CodeMirror extends UIInput implements ClientBehaviorHolder, Widget {

	public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.CodeMirror";
	public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
	private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.CodeMirrorRenderer";
	private static final String OPTIMIZED_PACKAGE = "org.primefaces.extensions.component.";

	public static final String EVENT_CHANGE = "change";
	public static final String EVENT_HIGHLIGHT_COMPLETE = "highlightComplete";
	public static final String EVENT_BLUR = "blur";
	public static final String EVENT_FOCUS = "focus";

	private static final Collection<String> EVENT_NAMES =
			Collections.unmodifiableCollection(Arrays.asList(EVENT_CHANGE, EVENT_HIGHLIGHT_COMPLETE, EVENT_BLUR, EVENT_FOCUS,
					EVENT_CHANGE));

	/**
	 * Properties that are tracked by state saving.
	 *
	 * @author  Thomas Andraschko / last modified by $Author$
	 * @version $Revision$
	 */
	protected enum PropertyKeys {

		widgetVar,
		@Option theme,
		@Option mode,
		@Option indentUnit,
		@Option smartIndent,
		@Option tabSize,
		@Option indentWithTabs,
		@Option electricChars,
		@Option keyMap,
		@Option lineWrapping,
		@Option lineNumbers,
		@Option firstLineNumber,
		@Option gutter,
		@Option fixedGutter,
		@Option readOnly,
		@Option matchBrackets,
		@Option workTime,
		@Option workDelay,
		@Option pollInterval,
		@Option undoDepth,
		@Option tabindex,
		@Option extraKeys,
		completeMethod,
		process,
		onstart,
		oncomplete,
		onerror,
		onsuccess,
		global,
		async,
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

	public CodeMirror() {
		setRendererType(DEFAULT_RENDERER);
	}

	@Override
	public Collection<String> getEventNames() {
		return EVENT_NAMES;
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	public String getTheme() {
		return (String) getStateHelper().eval(PropertyKeys.theme, null);
	}

	public void setTheme(final String theme) {
		setAttribute(PropertyKeys.theme, theme);
	}

	public String getMode() {
		return (String) getStateHelper().eval(PropertyKeys.mode, null);
	}

	public void setMode(final String mode) {
		setAttribute(PropertyKeys.mode, mode);
	}

	public String getKeyMap() {
		return (String) getStateHelper().eval(PropertyKeys.keyMap, null);
	}

	public void setKeyMap(final String keyMap) {
		setAttribute(PropertyKeys.keyMap, keyMap);
	}

	public String getWidgetVar() {
		return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
	}

	public void setWidgetVar(final String widgetVar) {
		setAttribute(PropertyKeys.widgetVar, widgetVar);
	}

	public Integer getIndentUnit() {
		return (Integer) getStateHelper().eval(PropertyKeys.indentUnit, null);
	}

	public void setIndentUnit(final Integer indentUnit) {
		setAttribute(PropertyKeys.indentUnit, indentUnit);
	}

	public Integer getTabSize() {
		return (Integer) getStateHelper().eval(PropertyKeys.tabSize, null);
	}

	public void setFirstLineNumber(final Integer firstLineNumber) {
		setAttribute(PropertyKeys.firstLineNumber, firstLineNumber);
	}

	public Integer getFirstLineNumber() {
		return (Integer) getStateHelper().eval(PropertyKeys.firstLineNumber, null);
	}

	public void setTabSize(final Integer tabSize) {
		setAttribute(PropertyKeys.tabSize, tabSize);
	}

	public Boolean isLineNumbers() {
		return (Boolean) getStateHelper().eval(PropertyKeys.lineNumbers, null);
	}

	public void setLineNumbers(final Boolean lineNumbers) {
		setAttribute(PropertyKeys.lineNumbers, lineNumbers);
	}

	public Boolean isSmartIndent() {
		return (Boolean) getStateHelper().eval(PropertyKeys.smartIndent, null);
	}

	public void setSmartIndent(final Boolean smartIndent) {
		setAttribute(PropertyKeys.smartIndent, smartIndent);
	}

	public Boolean isReadOnly() {
		return (Boolean) getStateHelper().eval(PropertyKeys.readOnly, false);
	}

	public void setReadOnly(final Boolean readOnly) {
		setAttribute(PropertyKeys.readOnly, readOnly);
	}

	public Boolean isIndentWithTabs() {
		return (Boolean) getStateHelper().eval(PropertyKeys.indentWithTabs, null);
	}

	public void setIndentWithTabs(final Boolean indentWithTabs) {
		setAttribute(PropertyKeys.indentWithTabs, indentWithTabs);
	}

	public Boolean isElectricChars() {
		return (Boolean) getStateHelper().eval(PropertyKeys.electricChars, null);
	}

	public void setElectricChars(final Boolean electricChars) {
		setAttribute(PropertyKeys.electricChars, electricChars);
	}

	public Boolean isLineWrapping() {
		return (Boolean) getStateHelper().eval(PropertyKeys.lineWrapping, null);
	}

	public void setLineWrapping(final Boolean lineWrapping) {
		setAttribute(PropertyKeys.lineWrapping, lineWrapping);
	}

	public Boolean isGutter() {
		return (Boolean) getStateHelper().eval(PropertyKeys.gutter, null);
	}

	public void setGutter(final Boolean gutter) {
		setAttribute(PropertyKeys.gutter, gutter);
	}

	public Boolean isFixedGutter() {
		return (Boolean) getStateHelper().eval(PropertyKeys.fixedGutter, null);
	}

	public void setFixedGutter(final Boolean fixedGutter) {
		setAttribute(PropertyKeys.fixedGutter, fixedGutter);
	}

	public Boolean isMatchBrackets() {
		return (Boolean) getStateHelper().eval(PropertyKeys.matchBrackets, null);
	}

	public void setMatchBrackets(final Boolean matchBrackets) {
		setAttribute(PropertyKeys.matchBrackets, matchBrackets);
	}

	public Integer getWorkTime() {
		return (Integer) getStateHelper().eval(PropertyKeys.workTime, null);
	}

	public void setWorkTime(final Integer workTime) {
		setAttribute(PropertyKeys.workTime, workTime);
	}

	public Integer getWorkDelay() {
		return (Integer) getStateHelper().eval(PropertyKeys.workDelay, null);
	}

	public void setWorkDelay(final Integer workDelay) {
		setAttribute(PropertyKeys.workDelay, workDelay);
	}

	public Integer getPollInterval() {
		return (Integer) getStateHelper().eval(PropertyKeys.pollInterval, null);
	}

	public void setPollInterval(final Integer pollInterval) {
		setAttribute(PropertyKeys.pollInterval, pollInterval);
	}

	public Integer getUndoDepth() {
		return (Integer) getStateHelper().eval(PropertyKeys.undoDepth, null);
	}

	public void setUndoDepth(final Integer undoDepth) {
		setAttribute(PropertyKeys.undoDepth, undoDepth);
	}

	public Integer getTabindex() {
		return (Integer) getStateHelper().eval(PropertyKeys.tabindex, null);
	}

	public void setTabindex(final Integer tabindex) {
		setAttribute(PropertyKeys.tabindex, tabindex);
	}

	public String getExtraKeys() {
		return (String) getStateHelper().eval(PropertyKeys.extraKeys, null);
	}

	public void setExtraKeys(final String extraKeys) {
		setAttribute(PropertyKeys.extraKeys, extraKeys);
	}

	public MethodExpression getCompleteMethod() {
		return (MethodExpression) getStateHelper().eval(PropertyKeys.completeMethod, null);
	}

	public void setCompleteMethod(final MethodExpression completeMethod) {
		setAttribute(PropertyKeys.completeMethod, completeMethod);
	}

	public String getProcess() {
		return (String) getStateHelper().eval(PropertyKeys.process, null);
	}

	public void setProcess(final String process) {
		setAttribute(PropertyKeys.process, process);
	}

	public String getOnstart() {
		return (String) getStateHelper().eval(PropertyKeys.onstart, null);
	}

	public void setOnstart(final String onstart) {
		setAttribute(PropertyKeys.onstart, onstart);
	}

	public String getOncomplete() {
		return (String) getStateHelper().eval(PropertyKeys.oncomplete, null);
	}

	public void setOncomplete(final String oncomplete) {
		setAttribute(PropertyKeys.oncomplete, oncomplete);
	}

	public String getOnerror() {
		return (String) getStateHelper().eval(PropertyKeys.onerror, null);
	}

	public void setOnerror(final String onerror) {
		setAttribute(PropertyKeys.onerror, onerror);
	}

	public String getOnsuccess() {
		return (String) getStateHelper().eval(PropertyKeys.onsuccess, null);
	}

	public void setOnsuccess(final String onsuccess) {
		setAttribute(PropertyKeys.onsuccess, onsuccess);
	}

	public boolean isGlobal() {
		return (Boolean) getStateHelper().eval(PropertyKeys.global, true);
	}

	public void setGlobal(final boolean global) {
		setAttribute(PropertyKeys.global, global);
	}

	public boolean isAsync() {
		return (Boolean) getStateHelper().eval(PropertyKeys.async, false);
	}

	public void setAsync(final boolean async) {
		setAttribute(PropertyKeys.async, async);
	}

	public boolean isEscape() {
		return (Boolean) getStateHelper().eval(PropertyKeys.escape, true);
	}

	public void setEscape(final boolean escape) {
		setAttribute(PropertyKeys.escape, escape);
	}

	public String resolveWidgetVar() {
		final FacesContext context = FacesContext.getCurrentInstance();
		final String userWidgetVar = (String) getAttributes().get(PropertyKeys.widgetVar.toString());

		if (userWidgetVar != null) {
			return userWidgetVar;
		}

		return "widget_" + getClientId(context).replaceAll("-|" + UINamingContainer.getSeparatorChar(context), "_");
	}

	@SuppressWarnings("unchecked")
	public void setAttribute(final PropertyKeys property, final Object value) {
		getStateHelper().put(property, value);

		List<String> setAttributes =
				(List<String>) this.getAttributes().get("javax.faces.component.UIComponentBase.attributesThatAreSet");
		if (setAttributes == null) {
			final String cname = this.getClass().getName();
			if (cname != null && cname.startsWith(OPTIMIZED_PACKAGE)) {
				setAttributes = new ArrayList<String>(6);
				this.getAttributes().put("javax.faces.component.UIComponentBase.attributesThatAreSet", setAttributes);
			}
		}

		if (setAttributes != null && value == null) {
			final String attributeName = property.toString();
			final ValueExpression ve = getValueExpression(attributeName);
			if (ve == null) {
				setAttributes.remove(attributeName);
			} else if (!setAttributes.contains(attributeName)) {
				setAttributes.add(attributeName);
			}
		}
	}

	private List<String> suggestions = null;

	@SuppressWarnings("unchecked")
	@Override
	public void broadcast(final FacesEvent event) throws AbortProcessingException {
		super.broadcast(event);

		final FacesContext facesContext = FacesContext.getCurrentInstance();
		final MethodExpression completeMethod = getCompleteMethod();

		if (completeMethod != null && event instanceof CompleteEvent) {
			final CompleteEvent completeEvent = (CompleteEvent) event;

			suggestions = (List<String>) completeMethod.invoke(
					facesContext.getELContext(),
					new Object[] { completeEvent.getToken(), completeEvent.getContext()});

			if (suggestions == null) {
				suggestions = new ArrayList<String>();
			}

			facesContext.renderResponse();
		}
	}

	public List<String> getSuggestions() {
		return this.suggestions;
	}
}
