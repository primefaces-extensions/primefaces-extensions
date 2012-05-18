/**
 * PrimeFaces Extensions DynaForm Widget.
 *
 * @author Oleg Varaksin
 */
PrimeFacesExt.widget.DynaForm = PrimeFaces.widget.BaseWidget.extend({

    /**
     * Initializes the widget.
     *
     * @param {object} cfg The widget configuration.
     */
    init : function(cfg) {
        this._super(cfg);

        if (cfg.autoSubmit) {
            this.submitForm();
        }
    },

    toggleExtended : function() {
        this.jq.find("tr.pe-dynaform-extendedrow").toggle();
    },

    submitForm : function() {
        this.jq.find(":submit").trigger('click');
    }
});