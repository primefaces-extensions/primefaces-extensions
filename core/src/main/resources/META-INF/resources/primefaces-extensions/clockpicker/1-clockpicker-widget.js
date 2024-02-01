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
        this.id = cfg.id;
        this.jqId = PrimeFaces.escapeClientId(cfg.id);
        this.container = $(this.jqId);
        this.jqEl = this.jqId + '_input';
        this.jq = $(this.jqEl);
        this.cfg.donetext = PrimeFaces.getAriaLabel('close') || 'Close';
        this.cfg.appendTo = PrimeFaces.utils.resolveDynamicOverlayContainer(this);

        // setup input
        this.input = $(this.jqEl);
        PrimeFaces.skinInput(this.input);

        // check if being used in dialog and set the parent
        this.setupDialogSupport();

        // create the clock picker
        this.clockpicker = this.createClockPicker();

        // pfs metadata
        this.jq.data(PrimeFaces.CLIENT_ID_DATA, this.id);
        this.originalValue = this.input.val();
    },

    // @override
    refresh: function (cfg) {
        // Destroy previous instance
        this.remove();
        // Reinitialize with new configuration
        this._super(cfg);
    },

    // @override
    destroy: function () {
        this._super();
        this.remove();
    },

    /**
     * Sets up support for using the overlay color picker within an overlay dialog.
     * @private
     */
    setupDialogSupport: function() {
        var dialog = this.input[0].closest('.ui-dialog');
        if (dialog) {
            this.cfg.appendTo = $(PrimeFaces.escapeClientId(dialog.id));
        }
    },

    /**
     * Creates the clock picker and sets up events.
     * @returns {JQuery} the clock picker
     * @private
     */
    createClockPicker: function () {
        this.clockpicker = this.container.clockpicker(this.cfg);
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
        if (this.container) {
            this.container.clockpicker("hide");
        }
    },

    /**
     * Shows the clockpicker if it exists.
     */
    show: function () {
        if (this.container) {
            this.container.clockpicker("show");
        }
    },
    /**
     * Removes the clockpicker and its event listeners if it exists.
     */
    remove: function () {
        if (this.container) {
            this.container.clockpicker("remove");
        }
        this.clockpicker = null;
    },

    /**
     * Disables this input so that the user cannot enter a value anymore.
     */
    disable: function() {
        this.remove();
        PrimeFaces.utils.disableInputWidget(this.container, this.input);
    },

    /**
     * Enables this input so that the user can enter a value.
     */
    enable: function() {
        PrimeFaces.utils.enableInputWidget(this.container, this.input);
        this.clockpicker = this.createClockPicker();
    }
});