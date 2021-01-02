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
package org.primefaces.extensions.showcase.controller.exporter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 * DynamicColumnsTableController
 *
 * @author Sudheer Jonna / last modified by $Author$
 * @since 0.7.0
 * @version $Revision: 1.0 $
 */
@Named
@ViewScoped
public class DynamicColumnsTableController implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final List<String> VALID_COLUMN_KEYS = Arrays.asList("subject", "text", "country", "textLength",
                "deliveryStatus");

    private List<Message> messages;
    private List<Message> filteredMessages;

    private String columnTemplate = "subject text country textLength";
    private final List<ColumnModel> columns = new ArrayList<ColumnModel>();

    public DynamicColumnsTableController() {
        if (messages == null) {
            messages = new ArrayList<Message>();

            for (int i = 0; i < 10; i++) {
                final Message message = new Message();
                message.setSubject("subject " + i);
                message.setText("text " + i);
                message.setTextLength(i * 10 + 10 + "");
                message.setCountry("country" + i);
                message.setDeliveryStatus("successfull");
                messages.add(message);
            }
        }

        createDynamicColumns();
    }

    public List<ColumnModel> getColumns() {
        return columns;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(final List<Message> messages) {
        this.messages = messages;
    }

    public List<Message> getFilteredMessages() {
        return filteredMessages;
    }

    public void setFilteredMessages(final List<Message> filteredMessages) {
        this.filteredMessages = filteredMessages;
    }

    public static class ColumnModel implements Serializable {

        private static final long serialVersionUID = 1L;
        private final String header;
        private final String property;

        public ColumnModel(final String header, final String property) {
            this.header = header;
            this.property = property;
        }

        public String getHeader() {
            return header;
        }

        public String getProperty() {
            return property;
        }
    }

    public class Message implements Serializable {

        private static final long serialVersionUID = 1L;
        private String subject;
        private String text;
        private long time;
        private String textLength;
        private String country;
        private String deliveryStatus;

        public Message() {
            time = System.currentTimeMillis() + (long) (Math.random() * 10);
            textLength = Math.random() * 10 + "";
        }

        public final String getSubject() {
            return subject;
        }

        public final void setSubject(final String subject) {
            this.subject = subject;
        }

        public final String getText() {
            return text;
        }

        public final void setText(final String text) {
            this.text = text;
        }

        public long getTime() {
            return time;
        }

        public void setTime(final long time) {
            this.time = time;
        }

        public String getTextLength() {
            return textLength;
        }

        public void setTextLength(final String textLength) {
            this.textLength = textLength;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(final String country) {
            this.country = country;
        }

        public String getDeliveryStatus() {
            return deliveryStatus;
        }

        public void setDeliveryStatus(final String deliveryStatus) {
            this.deliveryStatus = deliveryStatus;
        }
    }

    public String getColumnTemplate() {
        return columnTemplate;
    }

    public void setColumnTemplate(final String columnTemplate) {
        this.columnTemplate = columnTemplate;
    }

    public void updateColumns() {
        createDynamicColumns();
    }

    public void createDynamicColumns() {
        final String[] columnKeys = columnTemplate.split(" ");
        columns.clear();

        for (final String columnKey : columnKeys) {
            final String key = columnKey.trim();

            if (VALID_COLUMN_KEYS.contains(key)) {
                columns.add(new ColumnModel(columnKey.toUpperCase(), columnKey));
            }
        }
    }
}
