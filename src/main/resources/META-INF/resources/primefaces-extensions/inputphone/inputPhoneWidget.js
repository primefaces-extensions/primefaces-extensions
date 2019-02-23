/**
 * PrimeFaces Extensions InputPhone Widget.
 * 
 * @author Jasper de Vries jepsar@gmail.com
 */
PrimeFaces.widget.ExtInputPhone = PrimeFaces.widget.BaseWidget.extend({

    /**
     * Initializes the widget.
     * 
     * @param {object}
     *        cfg The widget configuration.
     */
    init : function(cfg) {
        this._super(cfg);
        this.id = cfg.id;
        this.cfg = cfg;
        this.target = document.querySelector(PrimeFaces.escapeClientId(cfg.target));
        intlTelInput(this.target, cfg);
    }

});
