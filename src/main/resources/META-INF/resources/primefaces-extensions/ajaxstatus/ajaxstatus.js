/**
 * PrimeFaces Extensions AjaxStatus Widget.
 *
 * @author Oleg Varaksin
 */
PrimeFacesExt.widget.AjaxStatus = PrimeFaces.widget.BaseWidget.extend({
	
	/**
	 * Initializes the widget.
	 * 
	 * @param {object} cfg The widget configuration.
	 */
	init : function(cfg) {
	    this._super(cfg);
	},


	bindFacet : function(eventName, facetToShow) {
	    var _self = this;
	
	    // jQuery
	    jQuery(document).bind(eventName, function() {
	        _self.showFacet(facetToShow);
	    });
	
	    // JSF standard
	    if (eventName == "ajaxError") {
	        jsf.ajax.addOnError(this.processAjaxOnErrorFacet(facetToShow));
	    } else {
	        jsf.ajax.addOnEvent(this.processAjaxOnEventFacet(eventName, facetToShow));
	    }    
	},

	bindCallback : function(eventName, fn) {
	    // jQuery
	    jQuery(document).bind(eventName, fn);
	
	    // JSF standard
	    if (eventName == "ajaxError") {
	        jsf.ajax.addOnError(this.processAjaxOnErrorCallback(fn));
	    } else {
	        jsf.ajax.addOnEvent(this.processAjaxOnEventCallback(eventName, fn));
	    }
	},

	processAjaxOnEventFacet : function(eventName, facetToShow) {
	    var _self = this;
	
	    function processEvent(data) {
	        if (eventName == "ajaxStart" && data.status == "begin") {
	            _self.showFacet(facetToShow);
	        } else if (eventName == "ajaxComplete" && data.status == "complete") {
	            _self.showFacet(facetToShow);
	        } else if (eventName == "ajaxSuccess" && data.status == "success") {
	            _self.showFacet(facetToShow);
	        }
	    }
	
	    return processEvent;
	},

	processAjaxOnErrorFacet : function(facetToShow) {
	    var _self = this;
	
	    function processEvent() {
	        _self.showFacet(facetToShow);
	    }
	
	    return processEvent;
	},

	processAjaxOnEventCallback : function(eventName, fn) {
	    function processEvent(data) {
	        if (eventName == "ajaxStart" && data.status == "begin") {
	            fn();
	        } else if (eventName == "ajaxComplete" && data.status == "complete") {
	            fn();
	        } else if (eventName == "ajaxSuccess" && data.status == "success") {
	            fn();
	        }
	    }
	
	    return processEvent;
	},

	processAjaxOnErrorCallback : function(fn) {
	    function processEvent() {
	        fn();
	    }
	
	    return processEvent;
	},

	showFacet : function(facetToShow) {
	    this.jq.children().hide();
	    jQuery(this.jqId + '_' + facetToShow).show();
	}
});
