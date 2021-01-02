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
package org.primefaces.extensions.showcase.controller.sheet;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.primefaces.extensions.component.sheet.Sheet;
import org.primefaces.extensions.event.SheetEvent;
import org.primefaces.extensions.model.sheet.SheetUpdate;
import org.primefaces.extensions.showcase.model.sheet.Asset;
import org.primefaces.extensions.showcase.model.sheet.AssetType;
import org.primefaces.extensions.showcase.model.sheet.PlatformArchType;
import org.primefaces.extensions.showcase.model.sheet.PlatformType;

/**
 * {@link Sheet} Controller.
 *
 * @author Melloware mellowaredev@gmail.com
 */
@Named
@ViewScoped
public class SheetController implements Serializable {

    private static final long serialVersionUID = 20120224L;

    private List<Asset> assets = new ArrayList<>();
    private List<Asset> filteredAssets = new ArrayList<>();

    public SheetController() {
        addAssets(40, PlatformType.Linux, PlatformArchType.AMD64, AssetType.SERVER);
        addAssets(50, PlatformType.Windows, PlatformArchType.I386, AssetType.DESKTOP);
        addAssets(60, PlatformType.Mac, PlatformArchType.OTHER, AssetType.LAPTOP);
        addAssets(5, null, null, AssetType.PRINTER);
    }

    public void cellChangeEvent(final SheetEvent event) {
        final Sheet sheet = event.getSheet();
        final List<SheetUpdate> updates = sheet.getUpdates();
        // A SheetUpdate exists for each column updated, the same row may
        // appear more than once. For this reason we will track those we already
        // persisted
        final HashSet<Asset> processed = new HashSet<Asset>();
        int rowUpdates = 0;
        for (final SheetUpdate update : updates) {
            final Asset asset = (Asset) update.getRowData();
            if (processed.contains(asset)) {
                continue;
            }
            System.out.println("Asset " + asset.getAssetId() + " updated.");
            rowUpdates++;
        }
        sheet.commitUpdates();
        FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Update Success", Integer.toString(rowUpdates) + " rows updated"));
    }

    private void addAssets(final int count, final PlatformType platform, final PlatformArchType arch,
                final AssetType type) {
        for (int i = 0; i < count; i++) {
            final Asset asset = new Asset();
            asset.setAssetId(RandomUtils.nextLong());
            asset.setActive(RandomUtils.nextBoolean());
            asset.setPlatform(platform);
            asset.setPlatformArch(arch);
            asset.setHostName(type.toString().toLowerCase() + i + ".example.lan");
            asset.setAssetType(type);
            asset.setPurchaseDate(new Date());
            asset.setPurchaseTime(new Date());
            asset.setValue1(RandomUtils.nextInt(1, 1000));
            asset.setPassword(RandomStringUtils.randomAlphabetic(6));
            asset.setPurchasePrice(
                        BigDecimal.valueOf(RandomUtils.nextDouble(1.11, 999.99) * (RandomUtils.nextBoolean() ? -1 : 1))
                                    .setScale(2, RoundingMode.HALF_UP));
            getAssets().add(asset);
        }
    }

    public List<Asset> getAssets() {
        return assets;
    }

    public void setAssets(final List<Asset> assets) {
        this.assets = assets;
    }

    public List<Asset> getFilteredAssets() {
        return filteredAssets;
    }

    public void setFilteredAssets(final List<Asset> filteredAssets) {
        this.filteredAssets = filteredAssets;
    }

}
