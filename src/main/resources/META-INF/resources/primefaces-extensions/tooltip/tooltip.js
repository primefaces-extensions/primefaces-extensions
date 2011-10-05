PrimeFacesExt.widget.Tooltip = function(id, cfg) {
    this.cfg = cfg;
    var _self = this;

    if (this.cfg.global) {
        this.cfg.position.container = $(document.body);

        // bind the qTip within the event handler
        $('*[title]').die(this.cfg.show.event + ".tooltip").live(this.cfg.show.event + ".tooltip", function(event) {
            var el = $(this);
            if ($.browser.msie) {
                el.attr('oldtitle', el.attr('title')).attr('title', '');
            }

            if (el.is(':disabled')) {
                return;
            }

            // show the tooltip as soon as it's bound, vital so it shows up the first time you hover
            var extCfg = _self.cfg;
            extCfg.show.ready = true;
            el.qtip(extCfg, event);
        });
    } else if (this.cfg.shared) {
        var jqId = PrimeFaces.escapeClientId(id);
        
        // remove previous container element to support ajax updates
        $(document.body).children('#ui-tooltip-shared-' + jqId).remove();
        // create a new one
        var sharedDiv = $("<div id='ui-tooltip-shared-" + jqId + "'/>");
        sharedDiv.appendTo(document.body);

        this.cfg.position.container = sharedDiv;
        $('<div/>').qtip(this.cfg);
    } else {
        this.cfg.position.container = $(document.body);

        // delete previous tooltip to support ajax updates and create a new one
        $(this.cfg.forTarget).qtip('destroy').qtip(this.cfg);
    }
}

$.fn.qtip.defaults.style.widget = true;
$.fn.qtip.defaults.style.classes = "ui-tooltip-rounded ui-tooltip-shadow";
