package org.primefaces.extensions.model.timeline;

import java.io.Serializable;

/**
 * Model class representing a timeline group.
 *
 * @author  Oleg Varaksin
 * @since   2.0.0
 */
public class TimelineGroup implements Serializable {

    private static final long serialVersionUID = 20140413L;
    
    /** unique group's id */
    private String id;
   
   	/** any custom data object (required to show content of the group) */
   	private Object data;

    public TimelineGroup() {
    }

    public TimelineGroup(String id, Object data) {
        this.id = id;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TimelineGroup that = (TimelineGroup) o;

        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
