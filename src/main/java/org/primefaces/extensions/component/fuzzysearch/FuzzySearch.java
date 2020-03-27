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
package org.primefaces.extensions.component.fuzzysearch;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;

@FacesComponent(value = FuzzySearch.COMPONENT_TYPE)
@ResourceDependencies({
    @ResourceDependency(library = "primefaces", name = "components.css"),
    @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
    @ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js"),
    @ResourceDependency(library = "primefaces", name = "core.js"),
    @ResourceDependency(library = "primefaces-extensions", name = "fuzzysearch/0-fuzzysearch.js"),
    @ResourceDependency(library = "primefaces-extensions", name = "fuzzysearch/1-fuzzysearch-widget.js")
})
public class FuzzySearch extends FuzzySearchBase {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.FuzzySearch";

    public static final String CONTAINER_CLASS = "ui-fuzzysearch";
    public static final String ITEM_CLASS = "ui-fuzzysearch-item";

}
