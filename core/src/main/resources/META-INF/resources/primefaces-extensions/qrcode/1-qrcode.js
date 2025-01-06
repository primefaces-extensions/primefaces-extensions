/**
 * PrimeFaces Extensions QRCode Widget.
 *
 * @author Mauricio Fenoglio
 */
PrimeFaces.widget.ExtQRCode = class extends PrimeFaces.widget.BaseWidget {
    /**
     * Initializes the widget.
     *
     * @param {object} cfg The widget configuration.
     */
    init(cfg) {
        super.init(cfg);
        this.container = $(this.jqId);
        this._render();
    }
    _render() {
        this.cfg.fontcolor = this.toHexColor(this.cfg.fontcolor);
        this.cfg.fill = this.toHexColor(this.cfg.fill);

        //modes > 2 are only allowed for image render
        if (this.cfg.mode > 2 && this.cfg.render !== 'image') {
            this.cfg.mode = 1;
        }

        this.instance = this.container.qrcode(this.cfg);
    }
    toHexColor(value) {
        if (!(/^#/).test(value)) {
            return '#' + value;
        } else {
            return value;
        }
    }

};
