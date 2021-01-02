/*
 * Copyright (c) 2011-2021 PrimeFaces Extensions
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
package org.primefaces.extensions.showcase.util;

import java.io.*;
import java.util.*;

import javax.xml.*;
import javax.xml.parsers.*;

import org.primefaces.extensions.showcase.model.system.*;
import org.w3c.dom.*;
import org.xml.sax.*;

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
        docBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, ""); // Compliant
        docBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, ""); // compliant
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

        Map<String, DocuTag> tags = new HashMap<>();
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
