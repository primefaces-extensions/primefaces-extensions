PrimeFacesExt.widget.AjaxStatus = function(cfg) {
    this.id = cfg.id;
    this.jqId = PrimeFaces.escapeClientId(this.id);
    
    this.postConstruct();
}

PrimeFaces.extend(PrimeFacesExt.widget.AjaxStatus, PrimeFaces.widget.BaseWidget);

PrimeFacesExt.widget.AjaxStatus.prototype.bindFacet = function(eventName, facetToShow) {
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
}

PrimeFacesExt.widget.AjaxStatus.prototype.bindCallback = function(eventName, fn) {
    // jQuery
    jQuery(document).bind(eventName, fn);

    // JSF standard
    if (eventName == "ajaxError") {
        jsf.ajax.addOnError(this.processAjaxOnErrorCallback(fn));
    } else {
        jsf.ajax.addOnEvent(this.processAjaxOnEventCallback(eventName, fn));
    }
}

PrimeFacesExt.widget.AjaxStatus.prototype.processAjaxOnEventFacet = function(eventName, facetToShow) {
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
}

PrimeFacesExt.widget.AjaxStatus.prototype.processAjaxOnErrorFacet = function(facetToShow) {
    var _self = this;

    function processEvent() {
        _self.showFacet(facetToShow);
    }

    return processEvent;
}

PrimeFacesExt.widget.AjaxStatus.prototype.processAjaxOnEventCallback = function(eventName, fn) {
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
}

PrimeFacesExt.widget.AjaxStatus.prototype.processAjaxOnErrorCallback = function(fn) {
    function processEvent() {
        fn();
    }

    return processEvent;
}

PrimeFacesExt.widget.AjaxStatus.prototype.showFacet = function(facetToShow) {
    jQuery(this.jqId).children().hide();
    jQuery(this.jqId + '_' + facetToShow).show();
}