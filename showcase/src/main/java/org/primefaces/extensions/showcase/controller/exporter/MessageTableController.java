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

import java.io.*;
import java.util.*;

import javax.faces.application.*;
import javax.faces.context.*;
import javax.faces.view.*;
import javax.inject.*;

import org.primefaces.component.api.*;

/**
 * MessageTableController
 *
 * @author Sudheer Jonna / last modified by $Author$
 * @since 0.7.0
 */
@Named
@ViewScoped
public class MessageTableController implements Serializable {

    private static final long serialVersionUID = 20111020L;
    private final static String[] STATES;
    private List<Message> messages;
    private List<Message> filteredMessages;
    private String newSubject = "my subject";
    private String newText = "my text";
    private Date currentDate = new Date();
    private boolean checkValue;

    static {
        STATES = new String[10];
        for (int i = 0; i < 10; i++) {
            STATES[i] = "state" + i;
        }
    }

    public MessageTableController() {
        if (messages == null) {
            messages = new ArrayList<>();

            for (int i = 0; i < 10; i++) {
                final Message message = new Message();
                message.setSubject("subject " + i);
                message.setText("text " + i);
                message.setTextLength(i * 10 + 10 + "");
                message.setCountry("country" + i);
                message.setState("state" + i);
                message.setDeliveryStatus("successfull");
                messages.add(message);
            }
        }
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

    public String getNewSubject() {
        return newSubject;
    }

    public void setNewSubject(final String newSubject) {
        this.newSubject = newSubject;
    }

    public String getNewText() {
        return newText;
    }

    public void setNewText(final String newText) {
        this.newText = newText;
    }

    public static String[] getStates() {
        return STATES;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(final Date currentDate) {
        this.currentDate = currentDate;
    }

    public boolean getCheckValue() {
        return checkValue;
    }

    public void setCheckValue(final boolean checkValue) {
        this.checkValue = checkValue;
    }

    public String addMessage() {
        doSomething();

        final Message message = new Message();
        message.setSubject(newSubject);
        message.setText(newText);
        messages.add(0, message);

        newSubject = "";
        newText = "";

        final FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "New message has been added successfully", null));

        return null;
    }

    public static void doSomething() {
        try {
            // simulate a long running request
            Thread.sleep(1500);
        }
        catch (final Exception e) {
            // ignore
        }
    }

    public static String exportColumn(final UIColumn column) {
        return "PFE Rocks!";
    }

    public class Message implements Serializable {

        private static final long serialVersionUID = 1L;
        private String subject;
        private String text;
        private long time;
        private String textLength;
        private String country;
        private String state;
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

        public String getState() {
            return state;
        }

        public void setState(final String state) {
            this.state = state;
        }

        public String getDeliveryStatus() {
            return deliveryStatus;
        }

        public void setDeliveryStatus(final String deliveryStatus) {
            this.deliveryStatus = deliveryStatus;
        }
    }
}
