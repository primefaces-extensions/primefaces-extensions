/**
 * PrimeFaces Extensions Floating Action Button (FAB) Widget.
 *
 * @author Jasper de Vries jepsar@gmail.com
 * @since 7.0.1
 */
PrimeFaces.widget.ExtFAB = PrimeFaces.widget.BaseWidget.extend({

    /**
     * Initializes the widget.
     *
     * @param {object}
     *            cfg The widget configuration.
     */
    init : function (cfg) {
        this._super(cfg);
        this.id = cfg.id;
        this.cfg = cfg;
        var that = this;
        $(this.jqId + ' .ui-fab-main').click(function(){
            that.toggleClass('ui-state-active');
        });
    }

});