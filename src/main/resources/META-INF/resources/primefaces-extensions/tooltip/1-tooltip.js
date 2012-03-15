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
	    }
	    
	    PrimeFacesExt.removeWidgetScript(cfg.id)
	}
});

$.fn.qtip.defaults.style.widget = true;
$.fn.qtip.defaults.style.classes = "ui-tooltip-rounded ui-tooltip-shadow";
