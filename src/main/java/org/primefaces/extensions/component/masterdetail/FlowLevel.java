/*
* @author  Oleg Varaksin (ovaraksin@googlemail.com)
* $$Id$$
*/

package org.primefaces.extensions.component.masterdetail;

import java.io.Serializable;

public class FlowLevel implements Serializable
{
    private int level;

    private Object contextValue;

    public FlowLevel() {
    }

    public FlowLevel(final int level, final Object contextValue) {
        this.level = level;
        this.contextValue = contextValue;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(final int level) {
        this.level = level;
    }

    public Object getContextValue() {
        return contextValue;
    }

    public void setContextValue(final Object contextValue) {
        this.contextValue = contextValue;
    }
}
