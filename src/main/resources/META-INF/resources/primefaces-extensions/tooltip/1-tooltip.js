/**
 * PrimeFaces Extensions Tooltip Widget.
 *
 * @author Oleg Varaksin
 */
PrimeFaces.widget.ExtTooltip = PrimeFaces.widget.BaseWidget.extend({

    /**
     * Initializes the widget.
     *
     * @param {object} cfg The widget configuration.
     */
    init : function(cfg) {
        var id = cfg.id;
        this.cfg = cfg;
        var _self = this;
        var targetSelectors = null;

        if (this.cfg.forTarget) {
        	this.targetSelectors = this.resolveComponentsAsSelectorString(this.cfg.forTarget);
        	this.cfg.forTarget = PrimeFaces.expressions.SearchExpressionFacade.resolveComponentsAsSelector(this.cfg.forTarget);
        }

        if (this.cfg.global) {
            this.cfg.position.container = $(document.body);
            var selector = '*[title]';
            if (this.targetSelectors) {
                selector = this.targetSelectors.join(", ");
            }

            $('body').off('.tooltip').on(this.cfg.show.event + '.tooltip', selector, function(event) {
                var el = $(this);
                if (el.is(':disabled')) {
                    return;
                }

                var extCfg = _self.cfg;
                extCfg.content.text = el.attr('title');
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
                var nsevent = "debouncedresize.tooltip" + PrimeFaces.escapeClientId(id);
                $(window).off(nsevent).on(nsevent, function(event) {
                    $(_self.cfg.forTarget).qtip('reposition');
                });
            }
        }

        this.removeScriptElement(id);
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

            if (this.cfg.autoShow) {
                $(window).off("debouncedresize.tooltip" + PrimeFaces.escapeClientId(this.cfg.id));
            }
        }
    },

    reposition : function() {
        if (this.cfg.forTarget) {
            $(this.cfg.forTarget).qtip('reposition');
        }
    },
    
    /**
     * Convertes expressions into an array of Jquery Selectors. 
     * e.g. @(div.mystyle :input) @(.ui-inputtext) to 'div.mystyle :input, .ui-inputtext'
     */
    resolveComponentsAsSelectorString: function(expressions) {
		var splittedExpressions = PrimeFaces.expressions.SearchExpressionFacade.splitExpressions(expressions);
		var elements = [];

		if (splittedExpressions) {
			for (var i = 0; i < splittedExpressions.length; ++i) {
				var expression =  $.trim(splittedExpressions[i]);
				if (expression.length > 0) {

					// skip unresolvable keywords
					if (expression == '@none' || expression == '@all') {
						continue;
					}

					// just a id
					if (expression.indexOf("@") == -1) {
						elements.push(expression);
					}
					// @widget
					else if (expression.indexOf("@widgetVar(") == 0) {
						var widgetVar = expression.substring(11, expression.length - 1);
						var widget = PrimeFaces.widgets[widgetVar];

						if (widget) {
							elements.push(widget.id);
						} else {
							PrimeFaces.error("Widget for widgetVar \"" + widgetVar + "\" not avaiable");
						}
					}
					// PFS
					else if (expression.indexOf("@(") == 0) {
						//converts pfs to jq selector e.g. @(div.mystyle :input) to div.mystyle :input
						elements.push(expression.substring(2, expression.length - 1));
					}
				}
			}
		}

		return elements;
	},
});

$.fn.qtip.defaults.style.widget = true;
$.fn.qtip.defaults.style.classes = "";

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