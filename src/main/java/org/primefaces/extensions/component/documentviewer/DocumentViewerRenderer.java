package org.primefaces.extensions.component.documentviewer;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.DynamicResourceBuilder;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DocumentViewerRenderer extends CoreRenderer {
	public static final String RENDERER_TYPE = "org.primefaces.extensions.component.DocumentViewerRenderer";

	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		DocumentViewer documentViewer = (DocumentViewer) component;
		encodeMarkup(context, documentViewer);
	}

	private void encodeMarkup(FacesContext context,DocumentViewer documentViewer) throws IOException {

		ResponseWriter writer = context.getResponseWriter();

		writer.startElement("iframe", documentViewer);
		writer.writeAttribute("id", documentViewer.getClientId(), null);
		writer.writeAttribute("style", documentViewer.getStyle(), null);
		writer.writeAttribute("width",documentViewer.getWidth() != null ? documentViewer.getWidth() : "100%", null);
		writer.writeAttribute("height", documentViewer.getHeight(), null);
		writer.writeAttribute("allowfullscreen", "", null);
		writer.writeAttribute("webkitallowfullscreen", "", null);
		writer.writeAttribute("src",generateSrc(context, documentViewer), null);
		writer.endElement("iframe");

	}

	private String generateSrc(FacesContext context,DocumentViewer documentViewer)	throws IOException {
		String imageSrc;
		try {
			imageSrc = URLEncoder.encode(getDocumentSource(context, documentViewer), "UTF-8");
		} catch (Exception ex) {
			throw new IOException(ex);
		}
		
		StringBuilder srcBuilder = new StringBuilder();
		srcBuilder.append(context.getExternalContext().getRequestContextPath());
		srcBuilder.append("/javax.faces.resource/");
		srcBuilder.append(getResourceURL(documentViewer,context));
		srcBuilder.append("file=");
		srcBuilder.append(imageSrc);
		srcBuilder.append(generateHashString(documentViewer,context));
		
		return srcBuilder.toString();
	}

	private String generateHashString(DocumentViewer documentViewer,FacesContext context) {
		
		List<String> params = new ArrayList<String>(1);
		params.add("locale=" + getCalculatedLocale(documentViewer, context));
		if(documentViewer.getPage() != null){
			params.add("page="+documentViewer.getPage());
		}
		
		if(!params.isEmpty()){
			return "#" + StringUtils.join(params, "&");
		}else{
			return "";
		}
	}

	private String getResourceURL(DocumentViewer documentViewer, FacesContext context) {
        //TODO: manage compressed & uncopressed resouceces
        return "documentviewer/viewer.html.jsf?ln=primefaces-extensions-uncompressed&v=" + this.getClass().getPackage().getSpecificationVersion() + "&";
	}
	
	private Locale getCalculatedLocale(DocumentViewer documentViewer,FacesContext context){
		Object locale = documentViewer.getLocale();
		if(locale == null){
			return context.getViewRoot().getLocale();
		}else{
			if(locale instanceof Locale){
				return (Locale) locale;
			}else if(locale instanceof String){
				return ComponentUtils.toLocale((String) locale);
			}else{
				throw new IllegalArgumentException("Type:" + locale.getClass() + " is not a valid locale type for calendar:" + documentViewer.getClientId(context));
			}
		}
	}

	protected String getDocumentSource(FacesContext context,DocumentViewer documentViewer) throws UnsupportedEncodingException {

		String name = documentViewer.getName();

		if (name != null) {
			String libName = documentViewer.getLibrary();
			ResourceHandler handler = context.getApplication().getResourceHandler();
			Resource res = handler.createResource(name, libName);

			if (res == null) {
				return "RES_NOT_FOUND";
			} else {
				String requestPath = res.getRequestPath();
				return context.getExternalContext().encodeResourceURL(requestPath);
			}
		} else {
			return DynamicResourceBuilder.build(context,documentViewer.getValue(), documentViewer,documentViewer.isCache());
		}
	}
}
