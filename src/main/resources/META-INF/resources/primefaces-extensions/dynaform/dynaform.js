/**
 * PrimeFaces Extensions DynaForm Widget.
 *
 * @author Oleg Varaksin
 */
PrimeFacesExt.widget.DynaForm = PrimeFaces.widget.BaseWidget.extend({
	
	/**
	 * Initializes the widget.
	 * 
	 * @param {object} cfg The widget configuration.
	 */
	init : function(cfg) {
	    var id = cfg.id;
	    this.cfg = cfg;
        
        // TODO
	    
	    PrimeFacesExt.removeWidgetScript(cfg.id)
	}
});