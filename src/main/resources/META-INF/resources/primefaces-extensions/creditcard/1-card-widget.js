/**
 * PrimeFaces Extensions CreditCard Widget.
 *
 * @author Melloware mellowaredev@gmail.com
 * @since 8.0.1
 */
PrimeFaces.widget.ExtCreditCard = PrimeFaces.widget.BaseWidget.extend({

    /**
     * Initializes the widget.
     *
     * @param {object}
     *        cfg The widget configuration.
     */
    init: function (cfg) {
        this._super(cfg);
        this.id = cfg.id;
        this.cfg = cfg;
        this.cfg.container = '.ui-credit-card';

        var closestForm = this.jq.closest('form');
        this.cfg.form = PrimeFaces.escapeClientId(closestForm.attr('id'))

        this.card = new Card(this.cfg)

        this.bindListeners();
    },

    /**
     * GitHub #791: If any input box values have changed dynamically then
     * trigger blur/change for Card.js to pick up the changes. Especially
     * after AJAX postback.
     */
    bindListeners: function () {
        this.addRefreshListener(function () {
            $(document).ready(function () {
                const blur = new Event('blur');
                const change = new Event('change');
                $('.ui-state-filled').each(function (index) {
                    this.dispatchEvent(blur);
                    this.dispatchEvent(change);
                })
            });
        });
    }
});


