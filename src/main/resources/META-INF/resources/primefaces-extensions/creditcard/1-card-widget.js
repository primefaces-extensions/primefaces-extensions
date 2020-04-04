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
    init : function(cfg) {
        this._super(cfg);
        this.id = cfg.id;
        this.cfg = cfg;
        this.cfg.container = '.ui-credit-card';
        
        var closestForm = this.jq.closest('form');
        this.cfg.form = PrimeFaces.escapeClientId(closestForm.attr('id'))
        
        this.card = new Card(this.cfg);
    }
});


