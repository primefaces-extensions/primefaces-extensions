/**
 * PrimeFaces Extensions BlockUI Widget.
 *
 * @author Oleg Varaksin
 */
PrimeFacesExt.widget.BlockUI = PrimeFaces.widget.BaseWidget.extend({

	/**
	 * Initializes the widget.
	 * 
	 * @param {object} cfg The widget configuration.
	 */
	init : function(cfg) {
		this.source = cfg.source;
		this.target = cfg.target;
	    this.contentId = cfg.content;
	    this.contentExtern = cfg.contentExtern;
		this.eventRegEx = cfg.regEx;
        
        if (cfg.autoShow) {
            this.setupAjaxHandlers();
        }
		
		// global settings
		$.blockUI.defaults.theme = true;
		$.blockUI.defaults.fadeIn = 0;
		$.blockUI.defaults.fadeOut = 0;
        
        PrimeFacesExt.removeWidgetScript(cfg.id);
    },
    
	/* public access */
    setupAjaxHandlers : function () {
        var $this = this;
        var $document = $(document);
        
        $document.ajaxSend(function(event, jqxhr, settings) {
            // get IDs of the sources
            var source = PrimeFaces.Expressions.resolveComponents($this.source);
            
            // first, check if event should be handled 
            if ($this.isAppropriateEvent(source, settings)) {
                $this.block();
            }
        });
        
        $document.ajaxComplete(function(event, jqxhr, settings) {
            // get IDs of the sources
            var source = PrimeFaces.Expressions.resolveComponents($this.source);
            
            // first, check if event should be handled
            if ($this.isAppropriateEvent(source, settings)) {
                $this.unblock();
            }
        });
	},
	    
	block : function () {
        var targetEl = PrimeFaces.Expressions.resolveComponentsAsSelector(this.target);
        
        // second, check if the target element has been found
        if (targetEl.length > 0) {
            // block the target element
            if (this.contentId != null) {
                if (this.contentExtern) {
                    targetEl.block({message: $(this.contentId).clone().show().wrap('<div>').parent().html()});
                } else {
                    targetEl.block({message: $(this.contentId).html()});
                }
            } else {
                targetEl.block();
            }
    
            // get the current counter
            var blocksCount = targetEl.data("blockUI.blocksCount");
            if (typeof blocksCount === 'undefined') {
                blocksCount = 0;
            }
            
            // increase the counter
            targetEl.data("blockUI.blocksCount", blocksCount+1);
        }        
    },
	    
	unblock : function () {
        var targetEl = PrimeFaces.Expressions.resolveComponentsAsSelector(this.target);
        
        // second, check if the target element has been found
        if (targetEl.length > 0) {
            // get the current counter
            var blocksCount = targetEl.data("blockUI.blocksCount");
            
            // check the counter
            if (typeof blocksCount !== 'undefined') {
                if (blocksCount == 1) {
                    // unblock the target element and reset the counter
                    targetEl.unblock();
                    targetEl.data("blockUI.blocksCount", 0);
                } else if (blocksCount > 1) {
                    // only decrease the counter
                    targetEl.data("blockUI.blocksCount", blocksCount-1);
                }
            }
        }
    },
		
	/* private access */
		
	isAppropriateEvent : function (source, ajaxOptions) {
        if (typeof ajaxOptions === 'undefined' || ajaxOptions == null ||
            typeof ajaxOptions.data === 'undefined' || ajaxOptions.data == null) {
            return false;
        }
        
        if($.inArray(ajaxOptions.source, source) !== -1) {
        	return false;
        }
        
        // split options around ampersands
        var params = ajaxOptions.data.split(/&/g);
        
        // loop over the ajax options and try to match events
        for (var i = 0; i < params.length; i++) {
            if (this.eventRegEx.test(params[i])) {
                return true;
            }
        }
        
        return false;
    }
});
