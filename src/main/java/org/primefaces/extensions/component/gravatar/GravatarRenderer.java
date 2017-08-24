/**
 * Copyright 2011-2017 PrimeFaces Extensions
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
package org.primefaces.extensions.component.gravatar;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.renderkit.CoreRenderer;

public class GravatarRenderer extends CoreRenderer {

    private static final String BASE_URL = "www.gravatar.com";

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        this.encodeMarkup(context, (Gravatar) component);
    }

    private void encodeMarkup(FacesContext context, Gravatar gravatar) throws IOException {

        ResponseWriter writer = context.getResponseWriter();

        writer.startElement("img", gravatar);
        writer.writeAttribute("id", gravatar.getClientId(), null);
        writer.writeAttribute("style", gravatar.getStyle(), null);

        String url;
        try {
            url = this.generateURL(gravatar);
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        writer.writeAttribute("src", url, null);
        writer.endElement("img");

    }

    private String generateURL(Gravatar gravatar) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        boolean qrCode = gravatar.isQrCode();
        Integer size = gravatar.getSize();
        String notFound = gravatar.getNotFound();

        String url;

        // check if the request must be made over a secure layer or not
        if (gravatar.isSecure()) {
            url = "https://" + BASE_URL + "/";
        }
        else {
            url = "http://" + BASE_URL + "/";
        }

        if (!qrCode) {
            url += "avatar/";
        }

        url += generateMailHash(gravatar);

        url += qrCode ? ".qr" : ".jpg";

        List<String> params = new ArrayList<String>();

        if (size != null) {
            params.add("s=" + size);
        }

        if (StringUtils.isNotEmpty(notFound) && !notFound.equals("default")
                    && Gravatar.NOT_FOUND_VALUES.contains(notFound)) {
            params.add("d=" + notFound);
        }

        if (params.size() > 0) {
            url += "?" + StringUtils.join(params, "&");
        }

        return url;
    }

    private String generateMailHash(Gravatar gravatar) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(String.valueOf(gravatar.getValue()).getBytes("UTF-8"));
        byte[] digest = md.digest();
        StringBuffer sb = new StringBuffer();
        for (byte b : digest) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }

}
