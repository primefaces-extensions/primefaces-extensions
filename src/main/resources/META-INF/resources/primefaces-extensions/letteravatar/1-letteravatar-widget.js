/**
 * PrimeFaces Extensions LetterAvatar Widget.
 * 
 * @author Melloware mellowaredev@gmail.com
 * @since 7.0
 */
PrimeFaces.widget.ExtLetterAvatar = PrimeFaces.widget.BaseWidget.extend({

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
        
        LetterAvatar.transform(PrimeFaces.escapeClientId(this.id));
    }

});
