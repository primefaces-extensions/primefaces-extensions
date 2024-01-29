/**
 * PrimeFaces Extensions ClockPicker Widget.
 *
 */
PrimeFaces.widget.ExtClockPicker = PrimeFaces.widget.BaseWidget.extend({
    /**
     * Initializes the widget.
     *
     * @param {object} cfg The widget configuration.
     */
    init: function (cfg) {
        this._super(cfg);
        this.id = PrimeFaces.escapeClientId(cfg.id);
        this.cfg = cfg;
        this.jqEl = this.jqId + '_input';
        this.jq = $(this.jqEl);
        this.cfg.donetext = PrimeFaces.getAriaLabel('close') || 'Close';

        PrimeFaces.skinInput(this.jq);
        this.clockpicker = this.createClockPicker();

        // pfs metadata
        this.jq.data(PrimeFaces.CLIENT_ID_DATA, this.id);
        this.originalValue = this.jq.val();
    },

    // @override
    refresh: function (cfg) {
        // Destroy previous instance
        this.remove();
        // Reinitialize with new configuration
        this._super(cfg);
    },

    /**
     * Creates the clock picker and sets up events.
     * @returns {JQuery} the clock picker
     * @private
     */
    createClockPicker: function () {
        this.clockpicker = this.jq.clockpicker(this.cfg);
        this.bindConstantEvents();
        return this.clockpicker;
    },

    /**
     * Sets up the event listeners that only need to be set up once.
     * @private
     */
    bindConstantEvents: function () {
        let $this = this;

        PrimeFaces.utils.registerResizeHandler(this, 'resize.' + this.id + '_hide', undefined, function () {
            $this.hide();
        });

        PrimeFaces.utils.registerScrollHandler(this, 'scroll.' + this.id + '_hide', function () {
            $this.hide();
        });
    },

    /**
     * Hides the clockpicker if it exists.
     */
    hide: function () {
        if (this.jq) {
            this.jq.clockpicker("hide");
        }
    },

    /**
     * Shows the clockpicker if it exists.
     */
    show: function () {
        if (this.jq) {
            this.jq.clockpicker("show");
        }
    },
    /**
     * Removes the clockpicker and its event listeners if it exists.
     */
    remove: function () {
        if (this.jq) {
            this.jq.clockpicker("remove");
        }
        this.clockpicker = null;
    },

    /**
     * Disables this input so that the user cannot enter a value anymore.
     */
    disable: function() {
        this.remove();
        PrimeFaces.utils.disableInputWidget(this.jq);
    },

    /**
     * Enables this input so that the user can enter a value.
     */
    enable: function() {
        PrimeFaces.utils.enableInputWidget(this.jq);
        this.clockpicker = this.createClockPicker();
    }
});