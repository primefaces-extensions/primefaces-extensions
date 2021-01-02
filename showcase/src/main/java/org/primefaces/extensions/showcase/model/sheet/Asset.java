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
package org.primefaces.extensions.showcase.model.sheet;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Asset implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long assetId;

    private Date purchaseDate;

    private Date purchaseTime;

    private String hostName;

    private String description;

    private BigDecimal purchasePrice;

    private PlatformType platform;

    private PlatformArchType platformArch;

    private AssetType assetType;

    private Boolean active;

    private String comment;

    private String password;

    private Date lastUpdated;

    private String patchLevel;

    private String allocatedTo;

    private String location;

    private Integer value1;

    private Integer value2;

    private Integer value3;

    private Integer value4;

    private Integer value5;

    private Integer value6;

    private Integer value7;

    private Integer value8;

    private Integer value9;

    private Integer value10;

    public Long getAssetId() {
        return assetId;
    }

    public void setAssetId(final Long assetId) {
        this.assetId = assetId;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(final Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(final String hostName) {
        this.hostName = hostName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(final BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public PlatformType getPlatform() {
        return platform;
    }

    public void setPlatform(final PlatformType platform) {
        this.platform = platform;
    }

    public PlatformArchType getPlatformArch() {
        return platformArch;
    }

    public void setPlatformArch(final PlatformArchType platformArch) {
        this.platformArch = platformArch;
    }

    public AssetType getAssetType() {
        return assetType;
    }

    public void setAssetType(final AssetType assetType) {
        this.assetType = assetType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(final String comment) {
        this.comment = comment;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(final Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getPatchLevel() {
        return patchLevel;
    }

    public void setPatchLevel(final String patchLevel) {
        this.patchLevel = patchLevel;
    }

    public String getAllocatedTo() {
        return allocatedTo;
    }

    public void setAllocatedTo(final String allocatedTo) {
        this.allocatedTo = allocatedTo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(final String location) {
        this.location = location;
    }

    public Integer getValue1() {
        return value1;
    }

    public void setValue1(final Integer value1) {
        this.value1 = value1;
    }

    public Integer getValue2() {
        return value2;
    }

    public void setValue2(final Integer value2) {
        this.value2 = value2;
    }

    public Integer getValue3() {
        return value3;
    }

    public void setValue3(final Integer value3) {
        this.value3 = value3;
    }

    public Integer getValue4() {
        return value4;
    }

    public void setValue4(final Integer value4) {
        this.value4 = value4;
    }

    public Integer getValue5() {
        return value5;
    }

    public void setValue5(final Integer value5) {
        this.value5 = value5;
    }

    public Integer getValue6() {
        return value6;
    }

    public void setValue6(final Integer value6) {
        this.value6 = value6;
    }

    public Integer getValue7() {
        return value7;
    }

    public void setValue7(final Integer value7) {
        this.value7 = value7;
    }

    public Integer getValue8() {
        return value8;
    }

    public void setValue8(final Integer value8) {
        this.value8 = value8;
    }

    public Integer getValue9() {
        return value9;
    }

    public void setValue9(final Integer value9) {
        this.value9 = value9;
    }

    public Integer getValue10() {
        return value10;
    }

    public void setValue10(final Integer value10) {
        this.value10 = value10;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(final Boolean active) {
        this.active = active;
    }

    public Date getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(final Date purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (assetId == null ? 0 : assetId.hashCode());
        result = prime * result + (hostName == null ? 0 : hostName.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Asset other = (Asset) obj;
        if (assetId == null) {
            if (other.assetId != null) {
                return false;
            }
        }
        else if (!assetId.equals(other.assetId)) {
            return false;
        }
        if (hostName == null) {
            if (other.hostName != null) {
                return false;
            }
        }
        else if (!hostName.equals(other.hostName)) {
            return false;
        }
        return true;
    }

    public Integer getTotal() {
        return (value1 == null ? 0 : value1) +
                    (value2 == null ? 0 : value2) +
                    (value3 == null ? 0 : value3) +
                    (value4 == null ? 0 : value4) +
                    (value5 == null ? 0 : value5) +
                    (value6 == null ? 0 : value6) +
                    (value7 == null ? 0 : value7) +
                    (value8 == null ? 0 : value8) +
                    (value9 == null ? 0 : value9) +
                    (value10 == null ? 0 : value10);
    }

}
