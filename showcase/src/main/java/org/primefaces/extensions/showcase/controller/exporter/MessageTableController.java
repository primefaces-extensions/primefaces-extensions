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
package org.primefaces.extensions.showcase.controller.exporter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.component.api.UIColumn;

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

    private List<Message> messages;
    private List<Message> filteredMessages;
    private String newSubject = "my subject";
    private String newText = "my text";
    private final static String[] states;
    private Date currentDate = new Date();
    private boolean checkValue;

    static {
        states = new String[10];
        for (int i = 0; i < 10; i++) {
            states[i] = "state" + i;
        }
    }

    public MessageTableController() {
        if (messages == null) {
            messages = new ArrayList<Message>();

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

    public String[] getStates() {
        return states;
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

    public void doSomething() {
        try {
            // simulate a long running request
            Thread.sleep(1500);
        }
        catch (final Exception e) {
            // ignore
        }
    }

    public String exportColumn(final UIColumn column) {
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
