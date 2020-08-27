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
package org.primefaces.extensions.component.sheet;

import java.io.Serializable;

/**
 * Class used to represent an invalid row/cell.
 *
 * @author Mark Lassiter / Melloware
 * @since 6.2
 */
public class SheetInvalidUpdate implements Serializable {

    private static final long serialVersionUID = 1L;

    private Object invalidRowKey;

    private int invalidColIndex;

    private transient SheetColumn invalidColumn;

    private Object invalidValue;

    private String invalidMessage;

    public SheetInvalidUpdate(Object invalidRowKey, int invalidColIndex, SheetColumn invalidColumn, Object invalidValue, String invalidMessage) {
        super();
        this.invalidRowKey = invalidRowKey;
        this.invalidColIndex = invalidColIndex;
        this.invalidColumn = invalidColumn;
        this.invalidValue = invalidValue;
        this.invalidMessage = invalidMessage;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + invalidColIndex;
        result = prime * result + (invalidRowKey == null ? 0 : invalidRowKey.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SheetInvalidUpdate other = (SheetInvalidUpdate) obj;
        if (invalidColIndex != other.invalidColIndex) {
            return false;
        }
        if (invalidRowKey == null) {
            if (other.invalidRowKey != null) {
                return false;
            }
        }
        else if (!invalidRowKey.equals(other.invalidRowKey)) {
            return false;
        }
        return true;
    }

    public Object getInvalidRowKey() {
        return invalidRowKey;
    }

    public void setInvalidRowKey(Object invalidRowKey) {
        this.invalidRowKey = invalidRowKey;
    }

    public int getInvalidColIndex() {
        return invalidColIndex;
    }

    public void setInvalidColIndex(int invalidColIndex) {
        this.invalidColIndex = invalidColIndex;
    }

    public SheetColumn getInvalidColumn() {
        return invalidColumn;
    }

    public void setInvalidColumn(SheetColumn invalidColumn) {
        this.invalidColumn = invalidColumn;
    }

    public Object getInvalidValue() {
        return invalidValue;
    }

    public void setInvalidValue(Object invalidValue) {
        this.invalidValue = invalidValue;
    }

    public String getInvalidMessage() {
        return invalidMessage;
    }

    public void setInvalidMessage(String invalidMessage) {
        this.invalidMessage = invalidMessage;
    }

}
