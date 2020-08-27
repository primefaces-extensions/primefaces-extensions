/**
 * Copyright 2011-2018 PrimeFaces Extensions
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.primefaces.extensions.ckeditor;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.io.FileUtils;

/**
 * Steps to update:
 * <pre>
 * - Delete all files in the src/main/resources/META-INF/resources/primefaces-extensions/ckeditor/ directory
 * - extract new ckeditor files
 * - remove samples dir
 * - Before executing ResourceModifier, modify the short hash (e.g t=K5I7) to match a new version of CKEditor
 * - Execute ResourceModifier
 * - Try to run CKEditor and observe if any error occurs on both browser console and server console
 *    - The most common error is "Resources not found". If that's the case, please check if any new plugins's resources that
 *      need to be modified then add new rules inside ResourceModifier if needed
 *    - Re-run ResourceModifier
 * </pre>
 */
public class ResourceModifier {

    private static final String PROJECT_DIRECTORY = System.getProperty("user.dir");

    private static final String SHORT_HASH = "K5I7";

    private static final int INDEX_NOT_FOUND = -1;

    public static void main(final String[] args) throws IOException {

        final String resourcesDirectory = PROJECT_DIRECTORY + "/src/main/resources/META-INF/resources/primefaces-extensions/";

        System.err.println("######## Modify skin styles...." + resourcesDirectory);

        final File skinsPath = new File(PROJECT_DIRECTORY + "/src/main/resources/META-INF/resources/primefaces-extensions/ckeditor/skins/");

        for (final String skin : Objects.requireNonNull(skinsPath.list())) {
            System.err.println("#### Modify skin '" + skin + "'");

            final File skinDirectory = new File(resourcesDirectory + "ckeditor/skins/" + skin);
            final String relativeSkinPath = replace(
                        replace(skinDirectory.getPath(), resourcesDirectory, ""),
                        "\\",
                        "/");

            for (final File resourceToModify : Objects.requireNonNull(skinDirectory.listFiles())) {

                // modify css only
                if (!resourceToModify.getName().endsWith(".css")) {
                    continue;
                }

                System.err.println("## Modify file '" + resourceToModify.getName() + "'");

                String fileContent = FileUtils.readFileToString(resourceToModify, Charset.defaultCharset());
                final List<File> allSkinResources = getResourcesList(skinDirectory);

                // loop all possible image references
                for (final File resource : allSkinResources) {

                    // we just need to check included images
                    if (resource.getName().endsWith(".css")) {
                        continue;
                    }

                    final String relativeResourcePath = replace(
                                replace(resource.getPath(), resourcesDirectory, ""),
                                "\\",
                                "/");
                    String resourceName = replace(
                                resource.getAbsolutePath().replace(skinDirectory.getAbsolutePath(), ""),
                                "\\",
                                "/");

                    if (resourceName.startsWith("/")) {
                        resourceName = resourceName.substring(1);
                    }

                    fileContent = fileContent.replaceAll("url\\(" + resourceName + "\\)",
                                "url\\(\"#{resource['primefaces-extensions:" + relativeResourcePath + "']}\"\\)");
                }

                // icons.png
                fileContent = fileContent.replaceAll("url\\(icons.png\\?t=" + SHORT_HASH,
                            "url\\(\"#{resource['primefaces-extensions:" + relativeSkinPath + "/icons.png']}&t=" + SHORT_HASH + "\"");

                // icons_hidpi.png
                fileContent = fileContent.replaceAll("url\\(icons_hidpi.png\\?t=" + SHORT_HASH,
                            "url\\(\"#{resource['primefaces-extensions:" + relativeSkinPath + "/icons_hidpi.png']}&t=" + SHORT_HASH + "\"");

                FileUtils.writeStringToFile(resourceToModify, fileContent, Charset.defaultCharset());
            }

            // modify smileys plugin to load the smileys via CKEditor.getUrl
            final File file = new File(PROJECT_DIRECTORY
                        + "/src/main/resources/META-INF/resources/primefaces-extensions/ckeditor/plugins/smiley/dialogs/smiley.js");
            final String fileContent = FileUtils.readFileToString(file, Charset.defaultCharset()).replaceAll(
                        "CKEDITOR.tools.htmlEncode\\(e\\.smiley_path\\+h\\[a\\]\\)",
                        "CKEDITOR.tools.htmlEncode\\(CKEDITOR.getUrl\\(e\\.smiley_path\\+h\\[a\\]\\)\\)");
            FileUtils.writeStringToFile(file, fileContent, Charset.defaultCharset());
        }

        // read the main JS file
        final File file = new File(resourcesDirectory + "ckeditor/ckeditor.js");
        String fileContent = FileUtils.readFileToString(file, Charset.defaultCharset());

        // modify copyFormatting plugin to load copyformatting.css via CKEditor.getUrl
        fileContent = fileContent.replaceAll(
                    "this.path\\+\"styles/copyformatting.css\"",
                    "CKEDITOR.getUrl(this.path\\+\"styles/copyformatting.css\")");

        // modify scayt plugin to load scayt.css via CKEditor.getUrl
        fileContent = fileContent.replaceAll(
                    "this.path\\+\"skins/\"\\+CKEDITOR.skin.name\\+\"/scayt.css\"",
                    "CKEDITOR.getUrl(this.path\\+\"skins/\"\\+CKEDITOR.skin.name\\+\"/scayt.css\")");

        // modify wsc plugin to load wsc.css via CKEditor.getUrl
        fileContent = fileContent.replaceAll(
                    "this.path\\+\"skins/\"\\+CKEDITOR.skin.name\\+\"/wsc.css\"",
                    "CKEDITOR.getUrl(this.path\\+\"skins/\"\\+CKEDITOR.skin.name\\+\"/wsc.css\")");

        fileContent = fileContent.replaceAll(
                    "this.path\\+\"styles/dialog.css\"",
                    "CKEDITOR.getUrl(this.path\\+\"styles/dialog.css\")");

        fileContent = fileContent.replaceAll(
                    "this.path\\+\"styles/tableselection.css\"",
                    "CKEDITOR.getUrl(this.path\\+\"styles/tableselection.css\")");

        // Math JAX plugin images
        fileContent = fileContent.replaceAll(
                    "CKEDITOR.plugins.get\\(\"mathjax\"\\).path\\+\"images/loader.gif\"",
                    "CKEDITOR.getUrl(CKEDITOR.plugins.get\\(\"mathjax\"\\).path\\+\"images/loader.gif\")");
        fileContent = fileContent.replaceAll(
                    "b.plugins.widget.path\\+\"images/handle.png",
                    "CKEDITOR.getUrl(b.plugins.widget.path\\+\"images/handle.png\")\\+\"");

        // write file back out
        FileUtils.writeStringToFile(file, fileContent, Charset.defaultCharset());
    }

    private static List<File> getResourcesList(final File file) {
        final String name = file.getName();
        final List<File> result = new ArrayList<>();
        if (file.isDirectory()) {
            for (final File f : Objects.requireNonNull(file.listFiles())) {
                result.addAll(getResourcesList(f));
            }
        }
        else {
            if (name.endsWith(".png") || name.endsWith(".css")) {
                result.add(file);
            }
        }
        return result;
    }

    public static int indexOf(final CharSequence seq, final CharSequence searchSeq, final int startPos) {
        if (seq == null || searchSeq == null) {
            return INDEX_NOT_FOUND;
        }
        return seq.toString().indexOf(searchSeq.toString(), startPos);
    }

    public static String replace(final String text, final String searchString, final String replacement) {
        if (isEmpty(text) || isEmpty(searchString) || replacement == null) {
            return text;
        }
        int max = INDEX_NOT_FOUND;
        int start = 0;
        int end = indexOf(text, searchString, start);
        if (end == INDEX_NOT_FOUND) {
            return text;
        }
        final int replLength = searchString.length();
        int increase = replacement.length() - replLength;
        increase = Math.max(increase, 0);
        increase *= 16;
        final StringBuilder buf = new StringBuilder(text.length() + increase);
        while (end != INDEX_NOT_FOUND) {
            buf.append(text, start, end).append(replacement);
            start = end + replLength;
            if (--max == 0) {
                break;
            }
            end = indexOf(text, searchString, start);
        }
        buf.append(text, start, text.length());
        return buf.toString();
    }

    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }
}
