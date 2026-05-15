/*
 * Copyright (c) 2011-2026 PrimeFaces Extensions
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
import java.util.List;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import org.apache.commons.lang3.RandomUtils;
import org.primefaces.extensions.showcase.model.sheet.Asset;
import org.primefaces.extensions.showcase.model.sheet.AssetType;
import org.primefaces.extensions.showcase.model.sheet.PlatformType;

@Named
@ViewScoped
public class SheetExportController implements Serializable {

    private static final long serialVersionUID = 20240224L;

    private List<Asset> assets = new ArrayList<>();
    private List<Asset> filteredAssets = new ArrayList<>();

    public SheetExportController() {
        addAssets(40, PlatformType.Linux, AssetType.SERVER);
        addAssets(50, PlatformType.Windows, AssetType.DESKTOP);
        addAssets(60, PlatformType.Mac, AssetType.LAPTOP);
        addAssets(5, null, AssetType.PRINTER);
    }

    private void addAssets(final int count, final PlatformType platform, final AssetType type) {
        for (int i = 0; i < count; i++) {
            final Asset asset = new Asset();
            asset.setAssetId(RandomUtils.secure().randomLong());
            asset.setActive(RandomUtils.secure().randomBoolean());
            asset.setPlatform(platform);
            asset.setAssetType(type);
            asset.setPurchaseDate(new Date());
            asset.setPurchasePrice(
                        BigDecimal.valueOf(RandomUtils.secure().randomDouble(1.11, 999.99) * (RandomUtils.secure().randomBoolean() ? -1 : 1))
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
