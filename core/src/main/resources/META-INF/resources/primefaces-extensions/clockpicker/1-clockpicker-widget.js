/**
 * PrimeFaces Extensions ClockPicker Widget.
 * 
 */
PrimeFaces.widget.ExtClockPicker = PrimeFaces.widget.BaseWidget.extend({
	/**
     * Initializes the widget.
     * 
     * @param {object}
     *        cfg The widget configuration.
     */
    init : function(cfg) {
        this._super(cfg);
        this.id = PrimeFaces.escapeClientId(cfg.id);
        this.cfg = cfg;
        this.jqEl = this.jqId + '_input';
        this.jq = $(this.jqEl);

        this.jq.clockpicker(this.cfg);
        // pfs metadata
        $(this.jqId + '_input').data(PrimeFaces.CLIENT_ID_DATA, this.id);
        this.originalValue = this.jq.val();
        
    }
});