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

        this.clockpicker = this.jq.clockpicker(this.cfg);
        // pfs metadata
        $(this.jqId + '_input').data(PrimeFaces.CLIENT_ID_DATA, this.id);
        this.originalValue = this.jq.val();
        
    },
    
    // @override
    refresh: function(cfg) {
        // Destroy previous instance
        if (this.clockpicker) {
			this.clockpicker.remove()
		}
        // Reinitialize with new configuration
        this._super(cfg);
    },

    // @override
    destroy: function() {
		this._super();
        // Destroy clockpicker instance
        this.remove()
    },
    
    /**
	 * Hides the clockpicker if it exists.
	 * @function hide
	 */
	hide: function() {
        if (this.clockpicker) {
			this.clockpicker.hide()
		}
    },
    
    /**
	 * Shows the clockpicker if it exists.
	 * @function show
	 */
    show: function() {
        if (this.clockpicker) {
			this.clockpicker.show()
		}
    },
    /**
	 * Removes the clockpicker and its event listeners if it exists.
	 * @function remove
	 */
    remove: function() {
        if (this.clockpicker) {
			this.clockpicker.remove()
		}
    }
});