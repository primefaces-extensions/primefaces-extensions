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
package org.primefaces.extensions.showcase.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.primefaces.extensions.showcase.model.system.DocuAttribute;
import org.primefaces.extensions.showcase.model.system.DocuEvent;
import org.primefaces.extensions.showcase.model.system.DocuTag;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Parser of primefaces-extensions.taglib.xml.
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 */
public class TagLibParser {

    private static final String CLIENT_BEHAVIOR_EVENTS = "Client behavior events:";

    public static Map<String, DocuTag> getTags() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        docBuilderFactory.setIgnoringElementContentWhitespace(true);
        docBuilderFactory.setValidating(false);
        docBuilderFactory.setNamespaceAware(true);

        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        if (classloader == null) {
            classloader = TagLibParser.class.getClassLoader();
        }

        InputStream is = classloader.getResourceAsStream("META-INF/primefaces-extensions.taglib.xml");
        Document doc = docBuilder.parse(is);
        try {
            is.close();
        }
        catch (IOException ex) {
            // ignore
        }

        Map<String, DocuTag> tags = new HashMap<String, DocuTag>();
        NodeList nodes = doc.getElementsByTagName("tag");

        for (int i = 0; i < nodes.getLength(); i++) {
            NodeList childs = nodes.item(i).getChildNodes();

            if (childs == null || childs.getLength() < 1) {
                continue;
            }

            DocuTag docuTag = new DocuTag();
            for (int j = 0; j < childs.getLength(); j++) {
                Node child = childs.item(j);
                String nodeName = child.getNodeName();

                if ("description".equals(nodeName)) {
                    String description = child.getTextContent();
                    addEvents(description, docuTag);
                }
                else if ("tag-name".equals(nodeName)) {
                    tags.put(child.getTextContent(), docuTag);
                }
                else if ("attribute".equals(nodeName)) {
                    NodeList childs2 = child.getChildNodes();

                    if (childs2 != null && childs2.getLength() > 0) {
                        DocuAttribute docuAttribute = new DocuAttribute();

                        for (int k = 0; k < childs2.getLength(); k++) {
                            Node child2 = childs2.item(k);
                            String nodeName2 = child2.getNodeName();

                            if ("description".equals(nodeName2)) {
                                docuAttribute.setDescription(child2.getTextContent());
                            }
                            else if ("name".equals(nodeName2)) {
                                docuAttribute.setName(child2.getTextContent());
                            }
                            else if ("type".equals(nodeName2)) {
                                docuAttribute.setType(child2.getTextContent());
                            }
                        }

                        docuTag.addAttribute(docuAttribute);
                    }
                }
            }
        }

        return tags;
    }

    protected static void addEvents(final String description, final DocuTag docuTag) {
        int clientBehaviorEventsIndex = description.indexOf(CLIENT_BEHAVIOR_EVENTS);

        if (clientBehaviorEventsIndex > -1) {
            String extractedEvents = description.substring(clientBehaviorEventsIndex + CLIENT_BEHAVIOR_EVENTS.length());

            for (String extractedEvent : extractedEvents.split(",")) {
                DocuEvent event = new DocuEvent();
                event.setName(
                            extractedEvent.split("-")[0].trim());
                event.setDescription(
                            extractedEvent.split("-")[1].trim().split("\\(")[0]);
                event.setEventClass(
                            extractedEvent.split("-")[1].trim().split("\\(")[1].replace(").", "").replace(")", "").trim());

                docuTag.addEvent(event);
            }
        }
    }
}
