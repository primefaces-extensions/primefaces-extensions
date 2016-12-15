/**
 * PrimeFaces Extensions Tooltip Widget.
 * 
 * @author Oleg Varaksin
 * @author Melloware
 */
PrimeFaces.widget.ExtTooltip = PrimeFaces.widget.BaseWidget.extend({

	/**
	 * Initializes the widget.
	 * 
	 * @param {object}
	 *            cfg The widget configuration.
	 */
	init : function(cfg) {
		this._super(cfg);
		this.cfg = cfg;
		var _self = this;
		var targetSelectors = null;

		if (this.cfg.forTarget) {
			this.targetSelectors = PrimeFaces.expressions.CssSelectorResolver.resolveCssSelectors(this.cfg.forTarget);
			this.cfg.forTarget = PrimeFaces.expressions.SearchExpressionFacade.resolveComponentsAsSelector(this.cfg.forTarget);
		}

		if (this.cfg.global) {
			this.cfg.position.container = $(document.body);

			// now select all tooltips on page or just the for="" selector
			var selector = '[title][title!=""]';
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

				_self.applyTooltip(el, extCfg, event);
			});
		} else if (this.cfg.shared) {
			// remove previous container element to support ajax updates
			$(document.body).children('#ui-tooltip-shared-' + this.jqId).remove();
			// create a new one
			var sharedDiv = $("<div id='ui-tooltip-shared-" + this.jqId + "'/>");
			sharedDiv.appendTo(document.body);

			this.cfg.position.container = sharedDiv;
			$('<div/>').qtip(this.cfg);
		} else {
			this.cfg.position.container = $(document.body);

			this.applyTooltip($(this.cfg.forTarget), this.cfg);

			if (this.cfg.autoShow) {
				var nsevent = "debouncedresize.tooltip" + this.jqId;
				$(window).off(nsevent).on(nsevent, function(event) {
					_self.cfg.forTarget.qtip('reposition');
				});
			}
		}
	},

	/**
	 * Apply the QTip to Jquery object by deleting the old tip first. Delete
	 * previous tooltip to support ajax updates and create a new one
	 * 
	 * @param jq
	 *            the Jquery object to apply to
	 * @param cfg
	 *            the JSON configuation for the tooltip
	 * @param event
	 *            the optional event to attach to
	 */
	applyTooltip : function(jq, cfg, event) {
		if (event) {
			jq.qtip('destroy').qtip(cfg, event);
		} else {
			jq.qtip('destroy').qtip(cfg);
		}
	},

	show : function() {
		if (this.cfg.forTarget) {
			this.cfg.forTarget.qtip('show');
		}
	},

	hide : function() {
		if (this.cfg.forTarget) {
			this.cfg.forTarget.qtip('hide');
		}
	},

	destroy : function() {
		if (this.cfg.forTarget) {
			this.cfg.forTarget.qtip('destroy');

			if (this.cfg.autoShow) {
				$(window).off("debouncedresize.tooltip" + this.jqId);
			}
		}
	},

	reposition : function() {
		if (this.cfg.forTarget) {
			this.cfg.forTarget.qtip('reposition');
		}
	}

});

/**
 * Converts expressions into an array of Jquery Selectors.
 * 
 * e.g. @(div.mystyle :input) @(.ui-inputtext) to 'div.mystyle :input, .ui-inputtext'
 */
PrimeFaces.expressions.CssSelectorResolver = {
		
	resolveCssSelectors : function(expressions) {
		var splittedExpressions = PrimeFaces.expressions.SearchExpressionFacade.splitExpressions(expressions);
		var elements = [];

		if (splittedExpressions) {
			for (var i = 0; i < splittedExpressions.length; ++i) {
				var expression = $.trim(splittedExpressions[i]);
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
						// converts pfs to jq selector e.g. @(div.mystyle) to div.mystyle
						elements.push(expression.substring(2, expression.length - 1));
					}
				}
			}
		}

		return elements;
	}
};

// qTip2 Global Defaults
$.fn.qtip.defaults.style.widget = true;
$.fn.qtip.defaults.style.classes = "";

/**
 * Handle proper window.resize events.
 * 
 * Copied from https://github.com/louisremi/jquery-smartresize/ 
 */
(function($) {

	var $event = $.event, $special, resizeTimeout;

	$special = $event.special.debouncedresize = {
		setup : function() {
			$(this).on("resize", $special.handler);
		},
		teardown : function() {
			$(this).off("resize", $special.handler);
		},
		handler : function(event, execAsap) {
			// Save the context
			var context = this, args = arguments, dispatch = function() {
				// set correct event type
				event.type = "debouncedresize";
				$event.dispatch.apply(context, args);
			};

			if (resizeTimeout) {
				clearTimeout(resizeTimeout);
			}

			execAsap ? dispatch() : resizeTimeout = setTimeout(dispatch, $special.threshold);
		},
		threshold : 250
	};

})(jQuery);