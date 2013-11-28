/**
 * PrimeFaces Extensions QRCode Widget.
 *
 * @author Mauricio Fenoglio
 */
PrimeFacesExt.widget.QRCode = PrimeFaces.widget.BaseWidget.extend({

    /**
	 * Initializes the widget.
	 * 
	 * @param {object} cfg The widget configuration.
	 */
    init : function(cfg) {
        this._super(cfg);
        this.container = $(this.jqId);        
        //modes > 2 are only allowed for image render 
        if(cfg.mode > 2 && cfg.render!=='image'){
            cfg.mode = 1;
        }   
        this.container.empty().qrcode(cfg);
    }
});
