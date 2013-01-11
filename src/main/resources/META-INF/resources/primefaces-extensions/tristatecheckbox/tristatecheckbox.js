/**
 * PrimeFaces Extensions TriStateCheckbox Widget.
 *
 * @author Mauricio Fenoglio
 */
PrimeFacesExt.widget.TriStateCheckbox = PrimeFaces.widget.BaseWidget.extend({

    /**
     * Initializes the widget.
     *
     * @param {object} cfg The widget configuration.
     */
    init:function (cfg) {
        this._super(cfg);

        this.input = $(this.jqId + '_input');
        this.box = this.jq.find('.ui-chkbox-box');
        this.icon = this.box.children('.ui-chkbox-icon');
        this.itemLabel = this.jq.find('.ui-chkbox-label');
        this.disabled = this.input.is(':disabled');

        var _self = this;

        //bind events if not disabled
        if (!this.disabled) {
            this.box.mouseover(function () {
                _self.box.addClass('ui-state-hover');
            }).mouseout(function () {
                        _self.box.removeClass('ui-state-hover');
                    }).click(function () {
                        _self.toggle();
                    });

            //toggle state on label click
            this.itemLabel.click(function () {
                _self.toggle();
            });

            // client behaviors
            if (this.cfg.behaviors) {
                PrimeFaces.attachBehaviors(this.input, this.cfg.behaviors);
            }
        }

        // pfs metadata
        this.input.data(PrimeFaces.CLIENT_ID_DATA, this.id);
    },

    toggle:function () {
        if (!this.disabled) {
            var oldValue = parseInt(this.input.val());
            var newValue = (oldValue + 1) % 3;
            this.input.val(newValue);

            // remove / add def. icon and active classes
            if (newValue == 0) {
                this.box.removeClass('ui-state-active');
            } else {
                this.box.addClass('ui-state-active');
            }

            // remove old icon and add the new one
            var iconsClasses = this.box.data('iconstates');
            this.icon.removeClass(iconsClasses[oldValue]).addClass(iconsClasses[newValue]);
            
            // change title to the new one
            var iconTitles = this.box.data('titlestates');
            this.box.attr('title', iconTitles[newValue]);
           
            // fire change event
            this.input.change();
        }
    }
});
