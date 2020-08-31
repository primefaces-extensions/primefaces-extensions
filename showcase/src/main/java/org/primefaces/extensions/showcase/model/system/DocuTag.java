/*
 * Copyright 2011-2020 PrimeFaces Extensions
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
package org.primefaces.extensions.showcase.model.system;

import java.util.ArrayList;
import java.util.List;

/**
 * Corresponds to 'tag' tag in tag library.
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 */
public class DocuTag {

    private List<DocuAttribute> attributes = new ArrayList<DocuAttribute>();
    private List<DocuEvent> events = new ArrayList<DocuEvent>();

    public List<DocuAttribute> getAttributes() {
        return attributes;
    }

    public void addAttribute(final DocuAttribute attribute) {
        attributes.add(attribute);
    }

    public List<DocuEvent> getEvents() {
        return events;
    }

    public void addEvent(final DocuEvent event) {
        events.add(event);
    }
}
