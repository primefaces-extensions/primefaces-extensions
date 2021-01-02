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
package org.primefaces.extensions.component.gravatar;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.extensions.util.Attrs;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.LangUtils;

public class GravatarRenderer extends CoreRenderer {

    private static final String BASE_URL = "www.gravatar.com";

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        encodeMarkup(context, (Gravatar) component);
    }

    private void encodeMarkup(FacesContext context, Gravatar gravatar) throws IOException {

        final ResponseWriter writer = context.getResponseWriter();

        writer.startElement("img", gravatar);
        writer.writeAttribute("id", gravatar.getClientId(), null);
        writer.writeAttribute(Attrs.STYLE, gravatar.getStyle(), null);

        String url;
        try {
            url = generateURL(gravatar);
        }
        catch (final NoSuchAlgorithmException e) {
            throw new FacesException(e);
        }

        writer.writeAttribute("src", url, null);
        writer.endElement("img");

    }

    private String generateURL(Gravatar gravatar) throws NoSuchAlgorithmException {

        final boolean qrCode = gravatar.isQrCode();
        final Integer size = gravatar.getSize();
        final String notFound = gravatar.getNotFound();

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

        final List<String> params = new ArrayList<>();

        if (size != null) {
            params.add("s=" + size);
        }

        if (!LangUtils.isValueBlank(notFound) && (!"default".equals(notFound)
                    && Gravatar.NOT_FOUND_VALUES.contains(notFound) || notFound.startsWith("http"))) {
            params.add("d=" + notFound);
        }

        if (!params.isEmpty()) {
            url += "?" + String.join("&", params.toArray(new String[params.size()]));
        }

        return url;
    }

    private String generateMailHash(Gravatar gravatar) throws NoSuchAlgorithmException {
        final MessageDigest md = MessageDigest.getInstance("MD5"); // NOSONAR
        md.update(String.valueOf(gravatar.getValue()).getBytes(StandardCharsets.UTF_8));
        final byte[] digest = md.digest();
        final StringBuilder sb = new StringBuilder(1024);
        for (final byte b : digest) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }

}
