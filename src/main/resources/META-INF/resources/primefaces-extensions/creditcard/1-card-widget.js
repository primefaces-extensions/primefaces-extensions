/**
 * PrimeFaces Extensions CreditCard Widget.
 *
 * @author Melloware mellowaredev@gmail.com
 * @since 8.0.1
 */
PrimeFaces.widget.ExtCreditCard = PrimeFaces.widget.BaseWidget.extend({

    /**
     * @override
     * @inheritdoc
     * @param {PrimeFaces.BaseWidget<TCfg>} cfg
     */
    init: function (cfg) {
        this._super(cfg);
        this.cfg = cfg;
        this.cfg.container = '.ui-credit-card';

        this.form = this.jq.closest('form');
        this.cfg.form = PrimeFaces.escapeClientId(this.form.attr('id'))

        this.card = new Card(this.cfg)

        this.bindListeners();
    },

    /**
     * GitHub #791: If any input box values have changed dynamically then
     * trigger blur/change for Card.js to pick up the changes. Especially
     * after AJAX postback.
     */
    bindListeners: function () {
        let $this = this;
        this.addRefreshListener(function () {
            $(document).ready(function () {
                const blur = new Event('blur');
                const change = new Event('change');
                $this.form.find('.ui-state-filled').each(function () {
                    this.dispatchEvent(blur);
                    this.dispatchEvent(change);
                })
            });
        });
    }
});


