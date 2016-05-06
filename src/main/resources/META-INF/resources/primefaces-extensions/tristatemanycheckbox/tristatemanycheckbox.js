/**
 * PrimeFaces Extensions TriStateManyCheckbox Widget.
 *
 * @author Mauricio Fenoglio
 */
PrimeFaces.widget.ExtTriStateManyCheckbox = PrimeFaces.widget.BaseWidget.extend({

    /**
     * Initializes the widget.
     *
     * @param {object} cfg The widget configuration.
     */
    init:function (cfg) {
        this._super(cfg);

        this.outputs = this.jq.find('.ui-chkbox-box:not(.ui-state-disabled)');
        this.inputs = this.jq.find(':text:not(:disabled)');
        this.labels = this.jq.find('label:not(.ui-state-disabled)');
        this.fixedMod = function(number,mod){
            return ((number%mod)+mod)%mod;
        }
        var _self = this;

        this.outputs.mouseover(function () {
            $(this).addClass('ui-state-hover');
        }).mouseout(function () {
            $(this).removeClass('ui-state-hover');
        }).click(function (event) {
            _self.toggle($(this),1);
            if (event.preventDefault) { event.preventDefault(); } else { event.returnValue = false; }
        });

        this.labels.click(function (event) {
            var element = $(this), input = $(PrimeFaces.escapeClientId(element.attr('for'))), checkbox = input.parent().next();
            checkbox.click();
            if (event.preventDefault) { event.preventDefault(); } else { event.returnValue = false; }
        });

        //adding accesibility
        this.outputs.bind('keydown', function(event) {
            switch(event.keyCode){
                case 38:
                    _self.toggle($(this),1);
                    if (event.preventDefault) { event.preventDefault(); } else { event.returnValue = false; }
                    break;
                case 40:
                    _self.toggle($(this),-1);
                    if (event.preventDefault) { event.preventDefault(); } else { event.returnValue = false; }
                    break;
                case 39:
                    _self.toggle($(this),1);
                    if (event.preventDefault) { event.preventDefault(); } else { event.returnValue = false; }
                    break;
                case 37:
                    _self.toggle($(this),-1);
                    if (event.preventDefault) { event.preventDefault(); } else { event.returnValue = false; }
                    break;
                case 32:
                    _self.toggle($(this),1);
                    if (event.preventDefault) { event.preventDefault(); } else { event.returnValue = false; }
                    break;
            }
        });

        // client behaviors
        if (this.cfg.behaviors) {
            PrimeFaces.attachBehaviors(this.inputs, this.cfg.behaviors);
        }

        // pfs metadata
        this.inputs.data(PrimeFaces.CLIENT_ID_DATA, this.id);
    },

    toggle:function (checkbox,direction) {
        var inputField = checkbox.prev().find(':input');
        if (!checkbox.hasClass('ui-state-disabled')) {
            var oldValue = parseInt(inputField.val());
            var newValue = this.fixedMod((oldValue + direction),3);
            inputField.val(newValue);

            // remove / add def. icon and active classes
            if (newValue == 0) {
                checkbox.removeClass('ui-state-active');
            } else {
                checkbox.addClass('ui-state-active');
            }

            // remove old icon and add the new one
            var iconsClasses = checkbox.data('iconstates');
            checkbox.children().removeClass(iconsClasses[oldValue]).addClass(iconsClasses[newValue]);

            // change title to the new one
            var iconTitles = checkbox.data('titlestates');
            if(iconTitles!=null && iconTitles.length>0){
                checkbox.attr('title', iconTitles[newValue]);
            }

            // fire change event
            inputField.change();
        }
    }
});
