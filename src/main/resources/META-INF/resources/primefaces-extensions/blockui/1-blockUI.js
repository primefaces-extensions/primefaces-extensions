
PrimeFacesExt.behavior.BlockUI = function(id, source, target, regExp) {
	var clientId = id;
	var sourceId = source;
	var targetId = target;
	var eventRegExp = regExp;
	
	// global settings
	jQuery.blockUI.defaults.theme = true;
	jQuery.blockUI.defaults.fadeIn = 0;
	jQuery.blockUI.defaults.fadeOut = 0;
	jQuery.blockUI.defaults.applyPlatformOpacityRules = false;
	
	/* public access */
	
	this.setupAjaxSend = function () {
		jQuery(sourceId).ajaxSend(function(event, xhr, ajaxOptions) {
			// first, check if event should be handled 
	        if (isAppropriateEvent(ajaxOptions)) {
	        	var targetEl = jQuery(targetId);
	        	
	        	// second, check if the target element has been found
	        	if (targetEl.length > 0) {
	        		// block the target element
		        	targetEl.block({message: jQuery(clientId + "_content").html()});

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
		jQuery(sourceId).ajaxComplete(function(event, xhr, ajaxOptions) {
			// first, check if event should be handled
			if (isAppropriateEvent(ajaxOptions)) {
				var targetEl = jQuery(targetId);
				
				// second, check if the target element has been found
				if (targetEl.length > 0) {
					// get the current counter
		        	var blocksCount = targetEl.data("blockUI.blocksCount");
		        	
		        	// check the counter
		        	if (typeof blocksCount !== 'undefined') {
			        	if (blocksCount == 1) {
			        		// unblock the target element and reset the counter
			        		jQuery(targetId).unblock();
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
