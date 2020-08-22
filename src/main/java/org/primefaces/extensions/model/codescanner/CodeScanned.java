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
package org.primefaces.extensions.model.codescanner;

import org.primefaces.extensions.component.codescanner.CodeScanner;

/**
 * Model object for scanned code handling of {@link CodeScanner} component.
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 * @since 8.0.5
 */
public class CodeScanned {

    // Based on https://github.com/zxing-js/library/blob/master/src/core/BarcodeFormat.ts
    public enum Format {
        AZTEC, CODABAR, CODE_39, CODE_93, CODE_128, DATA_MATRIX, EAN_8, EAN_13, ITF, MAXICODE, PDF_417, QR_CODE, RSS_14, RSS_EXPANDED, UPC_A, UPC_E, UPC_EAN_EXTENSION
    }

    private final String value;

    private final Format format;

    public CodeScanned(String value, Format format) {
        this.value = value;
        this.format = format;
    }

    public String getValue() {
        return value;
    }

    public Format getFormat() {
        return format;
    }

}
