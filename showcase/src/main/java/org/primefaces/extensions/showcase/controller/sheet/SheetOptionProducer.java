/*
 * Copyright (c) 2011-2022 PrimeFaces Extensions
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

import java.util.*;

import javax.annotation.*;
import javax.enterprise.context.*;
import javax.faces.model.*;
import javax.inject.*;

import org.primefaces.extensions.showcase.model.sheet.*;

@Named
@ApplicationScoped
public class SheetOptionProducer {

    private List<SelectItem> assetTypes;
    private List<SelectItem> platformTypes;
    private List<SelectItem> archTypes;

    @PostConstruct
    private void init() {
        assetTypes = createEnumList(AssetType.values());
        platformTypes = createEnumList(PlatformType.values());
        archTypes = createEnumList(PlatformArchType.values());
    }

    private static <T extends Enum<?>> List<SelectItem> createEnumList(final T[] values) {
        final List<SelectItem> result = new ArrayList<>();
        result.add(new SelectItem("", "All"));
        for (final T value : values) {
            result.add(new SelectItem(value, value.name()));
        }
        return result;
    }

    public List<SelectItem> getAssetTypes() {
        return assetTypes;
    }

    public List<SelectItem> getPlatformTypes() {
        return platformTypes;
    }

    public List<SelectItem> getArchTypes() {
        return archTypes;
    }

}
