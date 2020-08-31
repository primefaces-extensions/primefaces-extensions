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
