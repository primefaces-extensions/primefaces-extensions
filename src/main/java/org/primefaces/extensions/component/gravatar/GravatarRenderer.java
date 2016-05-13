package org.primefaces.extensions.component.gravatar;

import java.io.IOException;
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

	private final String BASE_URL = "https://www.gravatar.com/";	
	
	private static final MessageDigest md;
	
	static{
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		this.encodeMarkup(context, (Gravatar) component);
	}
	
	private void encodeMarkup(FacesContext context,	Gravatar gravatar) throws IOException {

		ResponseWriter writer = context.getResponseWriter();


		writer.startElement("img", gravatar);
		writer.writeAttribute("id", gravatar.getClientId(), null);
		writer.writeAttribute("style", gravatar.getStyle(), null);
		
		String url;
		try {
			url = this.generateURL(gravatar);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		
		writer.writeAttribute("src",url, null);
		writer.endElement("img");

	}

	protected String generateURL(Gravatar gravatar) throws NoSuchAlgorithmException {
            
		boolean qrCode = gravatar.isQrCode();
		Integer size = gravatar.getSize();
		String notFound = gravatar.getNotFound();
                
                String url = this.BASE_URL;
		
		if(!qrCode){
			url += "avatar/";
		}
		
		url += generateMailHash(gravatar);
		
		url += qrCode ? ".qr" : ".jpg";
		
		List<String> params = new ArrayList<String>();
		
		if(size != null){
			params.add("s=" + size);
		}
		
		if(StringUtils.isNotEmpty(notFound) && !notFound.equals("default") && Gravatar.NOT_FOUND_VALUES.contains(notFound)){
			params.add("d=" + notFound);
		}
		
		if(params.size() > 0){
			url += "?" + StringUtils.join(params, "&");
		}
		
		return url;
	}

	private String generateMailHash(Gravatar gravatar) throws NoSuchAlgorithmException {
		
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(String.valueOf(gravatar.getValue()).getBytes());
		byte[] digest = md.digest();
		StringBuffer sb = new StringBuffer();
		for (byte b : digest) {
			sb.append(String.format("%02x", b & 0xff));
		}
		return sb.toString();
	}


}
