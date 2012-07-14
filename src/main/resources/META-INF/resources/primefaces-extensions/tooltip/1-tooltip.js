/**
 * PrimeFaces Extensions Tooltip Widget.
 *
 * @author Oleg Varaksin
 */
PrimeFacesExt.widget.Tooltip = PrimeFaces.widget.BaseWidget.extend({

    /**
     * Initializes the widget.
     *
     * @param {object} cfg The widget configuration.
     */
    init : function(cfg) {
        var id = cfg.id;
        this.cfg = cfg;
        var _self = this;

        if (this.cfg.global) {
            this.cfg.position.container = $(document.body);
            var selector;
            if (this.cfg.forTarget == null) {
                selector = '*[title]';
            } else {
                selector = this.cfg.forTarget + '[title]';
            }

            $('body').undelegate('.tooltip').delegate(selector, this.cfg.show.event + '.tooltip', function(event) {
                var el = $(this);
                if (el.is(':disabled')) {
                    return;
                }

                el.attr('oldtitle', el.attr('title')).attr('title', '');

                var extCfg = _self.cfg;
                extCfg.content = {};
                extCfg.content.text = el.attr('oldtitle');
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

            if (this.cfg.autoShow) {
                $(window).on("debouncedresize", function(event) {
                    $(_self.cfg.forTarget).qtip('reposition');
                });
            }
        }

        PrimeFacesExt.removeWidgetScript(cfg.id)
    },

    show : function() {
        if (this.cfg.forTarget) {
            $(this.cfg.forTarget).qtip('show');
        }
    },

    hide : function() {
        if (this.cfg.forTarget) {
            $(this.cfg.forTarget).qtip('hide');
        }
    },

    destroy : function() {
        if (this.cfg.forTarget) {
            $(this.cfg.forTarget).qtip('destroy');
        }
    },

    reposition : function() {
        if (this.cfg.forTarget) {
            $(this.cfg.forTarget).qtip('reposition');
        }
    }
});

$.fn.qtip.defaults.style.widget = true;
$.fn.qtip.defaults.style.classes = "ui-tooltip-rounded ui-tooltip-shadow";

// copied from https://github.com/louisremi/jquery-smartresize/ to hanlde proper window.resize
(function($) {

    var $event = $.event,
            $special,
            resizeTimeout;

    $special = $event.special.debouncedresize = {
        setup: function() {
            $(this).on("resize", $special.handler);
        },
        teardown: function() {
            $(this).off("resize", $special.handler);
        },
        handler: function(event, execAsap) {
            // Save the context
            var context = this,
                    args = arguments,
                    dispatch = function() {
                        // set correct event type
                        event.type = "debouncedresize";
                        $event.dispatch.apply(context, args);
                    };

            if (resizeTimeout) {
                clearTimeout(resizeTimeout);
            }

            execAsap ? dispatch() : resizeTimeout = setTimeout(dispatch, $special.threshold);
        },
        threshold: 250
    };

})(jQuery);