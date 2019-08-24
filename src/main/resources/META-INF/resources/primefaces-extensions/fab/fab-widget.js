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
        this.classActive = 'ui-state-active';
        this.button = $(this.jqId + ' .ui-fab-main');
        this.unselect = function () {
            if (window.getSelection) {
                window.getSelection().removeAllRanges();
            } else if (document.selection) {
                document.selection.empty();
            }
        };
        var obj = this;
        this.button.click(function(){ obj.toggle(); });
        if (!cfg.keepOpen) {
            $(this.jqId + ' .ui-menuitem-link').click(function(){
                obj.close();
            });
        }
    },

    toggle : function () {
        this.jq.toggleClass(this.classActive);
        this.unselect();
    },

    open : function () {
        this.jq.addClass(this.classActive);
        this.unselect();
    },

    close : function () {
        this.jq.removeClass(this.classActive);
        this.unselect();
    }

});