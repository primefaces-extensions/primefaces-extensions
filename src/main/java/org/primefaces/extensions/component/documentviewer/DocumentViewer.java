package org.primefaces.extensions.component.documentviewer;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIGraphic;

@ResourceDependencies({
        @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
        @ResourceDependency(library = "primefaces", name = "primefaces.js"),
        @ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js")
})
public class DocumentViewer extends UIGraphic {

	public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.DocumentViewer";
	public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";

    protected static enum PropertyKeys {
        width,
        height,
        style,
        name,
        library,
        cache,
        page,
        locale;
    }

    public DocumentViewer(){
        setRendererType(DocumentViewerRenderer.RENDERER_TYPE);
    }

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	public Integer getWidth() {
		return (Integer) this.getStateHelper().eval(PropertyKeys.width, null);
	}

	public void setWidth(Integer width) {
		this.getStateHelper().put(PropertyKeys.width, width);
	}

	public Integer getHeight() {
		return (Integer) this.getStateHelper().eval(PropertyKeys.height, null);
	}

	public void setHeight(Integer width) {
		this.getStateHelper().put(PropertyKeys.height, width);
	}

	public String getStyle() {
		return (String) this.getStateHelper().eval(PropertyKeys.style, null);
	}

	public void setStyle(String style) {
		this.getStateHelper().put(PropertyKeys.style, style);
	}

	public String getName() {
		return (String) getStateHelper().eval(PropertyKeys.name, null);
	}

	public void setName(String _name) {
		getStateHelper().put(PropertyKeys.name, _name);
	}

	public String getLibrary() {
		return (String) getStateHelper().eval(PropertyKeys.library, null);
	}

	public void setLibrary(String _library) {
		getStateHelper().put(PropertyKeys.library, _library);
	}

	public boolean isCache() {
		return (Boolean) getStateHelper().eval(PropertyKeys.cache, false);
	}

	public void setCache(boolean _cache) {
		getStateHelper().put(PropertyKeys.cache, _cache);
	}

	public Integer getPage(){
		return (Integer) getStateHelper().eval(PropertyKeys.page);
	}
	
	public void setPage(Integer page){
		this.getStateHelper().put(PropertyKeys.page, page);
	}
	
	public Object getLocale() {
		return (Object) getStateHelper().eval(PropertyKeys.locale, null);
	}
	public void setLocale(Object _locale) {
		getStateHelper().put(PropertyKeys.locale, _locale);
	}

}
