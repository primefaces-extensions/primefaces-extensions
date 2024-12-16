/**
 * PrimeFaces Extensions DynaForm Widget.
 *
 * @author Oleg Varaksin
 */
PrimeFaces.widget.ExtDynaForm = class extends PrimeFaces.widget.BaseWidget {

    /**
     * Initializes the widget.
     *
     * @param {object} cfg The widget configuration.
     */
    init(cfg) {
        super.init(cfg);
        if (!cfg.isPostback) {
            this.toggledExtended = false;
        }

        if (cfg.autoSubmit && !PF(cfg.widgetVar)) {
            // Delay submission until the CSP handler was registered, needed when CSP is enabled
            // See https://github.com/primefaces-extensions/primefaces-extensions/issues/1794#issuecomment-2526318388
            $(() => this.submitForm());
        } else if (cfg.isPostback && this.toggledExtended && this.uuid == cfg.uuid) {
            var rows = this.jq.find("tr.pe-dynaform-extendedrow");
            if (rows.length > 0) {
                if (this.openExtended) {
                    rows.show();
                } else {
                    rows.hide();
                }
            }
        }

        this.uuid = cfg.uuid;
    }

    toggleExtended() {
        var rows = this.jq.find("tr.pe-dynaform-extendedrow");
        if (rows.length > 0) {
            rows.toggle();

            this.toggledExtended = true;
            this.openExtended = $(rows[0]).css("display") != "none";
        }
    }

    submitForm() {
        this.jq.find(":submit").trigger('click');
    }
};