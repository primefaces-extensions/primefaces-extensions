package org.primefaces.extensions.event;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

@SuppressWarnings("serial")
public class CompleteEvent extends FacesEvent {

    private String token;
    private String context;
    private int line;
    private int column;

    public CompleteEvent(final UIComponent component, final String token, final String context, final int line, final int column) {
        super(component);
        this.token = token;
        this.context = context;
        this.line = line;
        this.column = column;
    }

    @Override
    public boolean isAppropriateListener(final FacesListener facesListener) {
        return false;
    }

    @Override
    public void processListener(final FacesListener facesListener) {

    }

    public String getToken() {
        return token;
    }

    public String getContext() {
        return context;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }
}
