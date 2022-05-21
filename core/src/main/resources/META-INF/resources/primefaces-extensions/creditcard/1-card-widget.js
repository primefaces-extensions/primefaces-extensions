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
     * @param {PrimeFaces.BaseWidget} cfg
     */
    init: function (cfg) {
        this._super(cfg);
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
        var $this = this;
        this.addRefreshListener(function () {
            $(document).ready(function () {
                var blur = new Event('blur');
                var change = new Event('change');
                $this.form.find('.ui-state-filled').each(function () {
                    if ($this.isInput(this, 'number') ||
                        $this.isInput(this, 'name') ||
                        $this.isInput(this, 'expiry') ||
                        $this.isInput(this, 'cvc')) {
                        this.dispatchEvent(blur);
                        this.dispatchEvent(change);
                    }
                });
            });
        });
    },

    /**
     * Checks if this input has the name we are looking for.
     *
     * @param input the input field
     * @param name the name of the input to look for
     * @returns {boolean|*} true if the field matches the name
     */
    isInput: function (input, name) {
        return input.name === name;
    },

    /**
     * Flips the card to the back of the card.
     */
    flipToBack: function () {
        this.form.find('input[name="name"]').each(function () {
            this.dispatchEvent(new Event('blur'));
        });
        this.form.find('input[name="cvc"]').each(function () {
            this.dispatchEvent(new Event('focus'));
        });
    },

    /**
     * Flips the card to the front of the card.
     */
    flipToFront: function () {
        this.form.find('input[name="cvc"]').each(function () {
            this.dispatchEvent(new Event('blur'));
        });
        this.form.find('input[name="name"]').each(function () {
            this.dispatchEvent(new Event('focus'));
        });
    }
});


