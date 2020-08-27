package org.primefaces.extensions.showcase.model.sheet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Model object to represent a row in sheet component.
 */
public class DynaSheetRow implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private List<DynaSheetCell> cells = new ArrayList<>();
    private boolean readOnly;

    /**
     * Gets the {@link #id}.
     *
     * @return Returns the {@link #id}.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the {@link #id}.
     *
     * @param id The {@link #id} to set.
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * Gets the {@link #cells}.
     *
     * @return Returns the {@link #cells}.
     */
    public List<DynaSheetCell> getCells() {
        return  cells;
    }

    /**
     * Sets the {@link #cells}.
     *
     * @param cells The {@link #cells} to set.
     */
    public void setCells(final List<DynaSheetCell> cells) {
        this.cells = cells;
    }

    /**
     * Gets the {@link #readOnly}.
     *
     * @return Returns the {@link #readOnly}.
     */
    public boolean isReadOnly() {
        return readOnly;
    }

    /**
     * Sets the {@link #readOnly}.
     *
     * @param readOnly The {@link #readOnly} to set.
     */
    public void setReadOnly(final boolean readOnly) {
        this.readOnly = readOnly;
    }

}
