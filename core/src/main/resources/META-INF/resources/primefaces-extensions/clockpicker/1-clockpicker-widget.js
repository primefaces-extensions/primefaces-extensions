/**
 * PrimeFaces Extensions ClockPicker Widget.
 *
 */
PrimeFaces.widget.ExtClockPicker = class extends PrimeFaces.widget.BaseWidget {
    /**
     * Initializes the widget.
     *
     * @param {object} cfg The widget configuration.
     */
    init(cfg) {
        super.init(cfg);
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

        // create the clock picker
        this.clockpicker = this.createClockPicker();

        // pfs metadata
        this.jq.data(PrimeFaces.CLIENT_ID_DATA, this.id);
        this.originalValue = this.input.val();
    }

    // @override
    refresh(cfg) {
        // Destroy previous instance
        this.remove();
        // Reinitialize with new configuration
        super.refresh(cfg);
    }

    // @override
    destroy() {
        super.destroy();
        this.remove();
    }

    /**
     * Creates the clock picker and sets up events.
     * @returns {JQuery} the clock picker
     * @private
     */
    createClockPicker() {
        this.clockpicker = this.container.clockpicker(this.cfg);

        // like PrimeFaces.utils.resolveAppendTo: scroll with a fixed dialog
        const dialog = this.input[0].closest('.p-dialog');
        if (dialog && $(dialog).css('position') === 'fixed') {
            this.container.data('clockpicker').popover.css('position', 'fixed');
        }

        this.bindConstantEvents();
        return this.clockpicker;
    }

    /**
     * Sets up the event listeners that only need to be set up once.
     * @private
     */
    bindConstantEvents() {
        let $this = this;

        PrimeFaces.utils.registerResizeHandler(this, 'resize.' + this.id + '_hide', undefined, function () {
            $this.hide();
        });

        PrimeFaces.utils.registerConnectedOverlayScrollHandler(this, 'scroll.' + this.id + '_hide', this.container, function () {
            $this.hide();
        });
    }

    /**
     * Hides the clockpicker if it exists.
     */
    hide() {
        if (this.container) {
            this.container.clockpicker("hide");
        }
    }

    /**
     * Shows the clockpicker if it exists.
     */
    show() {
        if (this.container) {
            this.container.clockpicker("show");
        }
    }
    /**
     * Removes the clockpicker and its event listeners if it exists.
     */
    remove() {
        if (this.container) {
            this.container.clockpicker("remove");
        }
        this.clockpicker = null;
    }

    /**
     * Disables this input so that the user cannot enter a value anymore.
     */
    disable() {
        this.remove();
        PrimeFaces.utils.disableInputWidget(this.container, this.input);
    }

    /**
     * Enables this input so that the user can enter a value.
     */
    enable() {
        PrimeFaces.utils.enableInputWidget(this.container, this.input);
        this.clockpicker = this.createClockPicker();
    }
};