PrimeFacesExt.widget.Tooltip = function(cfg) {
    this.cfg = cfg;
    var _self = this;

    if (this.cfg.global) {
        // Bind the qTip within the event handler
        $('*[title]').die(this.cfg.show.event + ".tooltip").live(this.cfg.show.event + ".tooltip", function(event) {
            var el = $(this);
            if ($.browser.msie) {
                el.attr('oldtitle', el.attr('title')).attr('title', '');
            }

            if (el.is(':disabled')) {
                return;
            }

            // Show the tooltip as soon as it's bound, vital so it shows up the first time you hover
            var extCfg = _self.cfg;
            extCfg.show.ready = true;
            el.qtip(extCfg, event);
        });
    } else {
        // delete previous tooltip to support ajax updates and create a new one
        $(PrimeFaces.escapeClientId(this.cfg.forComponent)).qtip('destroy').qtip(this.cfg);
    }
}

$.fn.qtip.defaults.style.widget = true;
$.fn.qtip.defaults.style.classes = "ui-tooltip-rounded ui-tooltip-shadow";
