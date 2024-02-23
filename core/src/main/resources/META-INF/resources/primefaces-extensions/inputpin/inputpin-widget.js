/**
 * PrimeFaces Extensions InputPin Widget.
 *
 * @since 14.0
 */
PrimeFaces.widget.ExtInputPin = PrimeFaces.widget.BaseWidget.extend({

    /**
     * Initializes the widget.
     *
     * @param {object}
     *            cfg The widget configuration.
     */
    init: function (cfg) {
        this._super(cfg);
        this.id = cfg.id;
        this.cfg = cfg;
        this.disabled = cfg.disabled;

        // JQuery inputs
        this.inputsJq = $(this.jqId + ' > input');

        // pfs metadata
        // this.inputJq.data(PrimeFaces.CLIENT_ID_DATA, this.id);

        // style disabled if necessary
        if (this.disabled) {
            $.each(this.inputsJq, function (elem) {
                elem.attr("disabled", "disabled");
                elem.inputJq.addClass("ui-state-disabled");
            });
        }

        // visual effects
        $.each(this.inputsJq, PrimeFaces.skinInput);

        this.bindEvents();
    },

    /**
     * Focus the component by focusing on the correct input box.
     */
    focus: function () {
        this.inputsJq[0].focus();
    },

    /**
     * Enable the input
     */
    enable: function () {
        $.each(this.inputsJq, PrimeFaces.utils.enableInputWidget);
        this.disabled = false;
    },

    /**
     * Disable the input
     */
    disable: function () {
        $.each(this.inputsJq, PrimeFaces.utils.disableInputWidget);
        this.disabled = true;
    }

});
