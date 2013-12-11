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
    init: function(cfg) {
        this._super(cfg);
        this.container = $(this.jqId);
        this._render();
    },
    _render: function() {
        this.cfg.fontcolor = this.toHexColor(this.cfg.fontcolor);
        this.cfg.fill = this.toHexColor(this.cfg.fill);

        //modes > 2 are only allowed for image render
        if (this.cfg.mode > 2 && this.cfg.render !== 'image') {
            this.cfg.mode = 1;
        }
        console.log(this.cfg);
        this.instance = this.container.qrcode(this.cfg);
    },
    toHexColor: function(value) {
        if (!(/^#/).test(value)) {
            return '#' + value;
        } else {
            return value;
        }
    }

});
