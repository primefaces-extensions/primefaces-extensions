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
