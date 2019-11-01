/**
 * Copyright 2011-2019 PrimeFaces Extensions
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
package org.primefaces.extensions.component.github;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponentBase;

import org.primefaces.component.api.Widget;

/**
 * <code>Github</code> component.
 *
 * @author Melloware mellowaredev@gmail.com
 * @since 6.1
 */
@ResourceDependencies({
            @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
            @ResourceDependency(library = "primefaces-extensions", name = "github/github.css"),
            @ResourceDependency(library = "primefaces-extensions", name = "github/github.js")
})
public class Github extends UIComponentBase implements Widget {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Github";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.GithubRenderer";

    protected enum PropertyKeys {

      // @formatter:off
      widgetVar,
      repository,
      iconStars,
      iconForks,
      iconIssues,
      style,
      styleClass;
      // @formatter:on

        private String toString;

        PropertyKeys(final String toString) {
            this.toString = toString;
        }

        PropertyKeys() {
        }

        @Override
        public String toString() {
            return toString != null ? toString : super.toString();
        }
    }

    /**
     * Default constructor
     */
    public Github() {
        setRendererType(DEFAULT_RENDERER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getWidgetVar() {
        return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void setWidgetVar(final String _widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, _widgetVar);
    }

    public String getRepository() {
        return (String) getStateHelper().eval(PropertyKeys.repository, null);
    }

    public void setRepository(final String _repository) {
        getStateHelper().put(PropertyKeys.repository, _repository);
    }

    public void setIconStars(final boolean _iconStars) {
        getStateHelper().put(PropertyKeys.iconStars, _iconStars);
    }

    public boolean isIconStars() {
        return (Boolean) getStateHelper().eval(PropertyKeys.iconStars, true);
    }

    public void setIconForks(final boolean _iconForks) {
        getStateHelper().put(PropertyKeys.iconForks, _iconForks);
    }

    public boolean isIconForks() {
        return (Boolean) getStateHelper().eval(PropertyKeys.iconForks, true);
    }

    public void setIconIssues(final boolean _iconIssues) {
        getStateHelper().put(PropertyKeys.iconIssues, _iconIssues);
    }

    public boolean isIconIssues() {
        return (Boolean) getStateHelper().eval(PropertyKeys.iconIssues, true);
    }

    public String getStyle() {
        return (String) getStateHelper().eval(PropertyKeys.style, null);
    }

    public void setStyle(final String _style) {
        getStateHelper().put(PropertyKeys.style, _style);
    }

    public String getStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.styleClass, null);
    }

    public void setStyleClass(final String _styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, _styleClass);
    }

}
