
PrimeFacesExt.behavior.BlockUI = function(source, target, content, regExp) {
	var sourceId = source;
	var targetId = target;
    var contentId = content;
	var eventRegExp = regExp;
	
	// global settings
	$.blockUI.defaults.theme = true;
	$.blockUI.defaults.fadeIn = 0;
	$.blockUI.defaults.fadeOut = 0;
	$.blockUI.defaults.applyPlatformOpacityRules = false;
	
	/* public access */
	
	this.setupAjaxSend = function () {
		$(sourceId).ajaxSend(function(event, xhr, ajaxOptions) {
			// first, check if event should be handled 
	        if (isAppropriateEvent(ajaxOptions)) {
	        	var targetEl = $(targetId);
	        	
	        	// second, check if the target element has been found
	        	if (targetEl.length > 0) {
	        		// block the target element
                    if (contentId != null) {
		        	    targetEl.block({message: $(contentId).html()});
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
	        }
		});
    }
	
	this.setupAjaxComplete = function () {
		$(sourceId).ajaxComplete(function(event, xhr, ajaxOptions) {
			// first, check if event should be handled
			if (isAppropriateEvent(ajaxOptions)) {
				var targetEl = $(targetId);
				
				// second, check if the target element has been found
				if (targetEl.length > 0) {
					// get the current counter
		        	var blocksCount = targetEl.data("blockUI.blocksCount");
		        	
		        	// check the counter
		        	if (typeof blocksCount !== 'undefined') {
			        	if (blocksCount == 1) {
			        		// unblock the target element and reset the counter
			        		$(targetId).unblock();
			        		targetEl.data("blockUI.blocksCount", 0);
			        	} else if (blocksCount > 1) {
			        		// only decrease the counter
			        		targetEl.data("blockUI.blocksCount", blocksCount-1);
			        	}
			        }
				}
			}
		});
    }
	
	/* private access */
	
	var isAppropriateEvent = function (ajaxOptions) {
		if (typeof ajaxOptions === 'undefined' || ajaxOptions == null ||
			typeof ajaxOptions.data === 'undefined' || ajaxOptions.data == null) {
			return false;
		}
		
		// split options around ampersands
		var params = ajaxOptions.data.split(/&/g);
		
		// loop over the ajax options and try to match events
		for (var i = 0; i < params.length; i++) {
			if (eventRegExp.test(params[i])) {
				return true;
			}
		}
		
		return false;
    }
}
