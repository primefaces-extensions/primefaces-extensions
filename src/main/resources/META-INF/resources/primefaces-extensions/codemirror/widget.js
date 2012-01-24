/**
 * PrimeFaces Extensions CodeMirror Widget
 * 
 * @constructor
 */
PrimeFacesExt.widget.CodeMirror = function(id, cfg) {
	this.id = id;
	this.cfg = cfg;
	this.jqId = PrimeFaces.escapeClientId(this.id);
	this.jq = $(this.jqId);

	this.options = this.cfg;

	this.options.onFocus = $.proxy(function() { this.fireEvent('focus'); }, this);
	this.options.onBlur = $.proxy(function() { this.fireEvent('blur'); }, this);

	this.options.onHighlightComplete =
		$.proxy(function(codeMirror) { this.fireEvent('highlightComplete'); }, this);

	this.options.onChange =
		$.proxy(function(from, to, text, next) {
			//set value to textarea
			this.jq.val(this.instance.getValue());
			
			//fire event
			this.fireEvent('change'); 
		}, this);

	this.instance = CodeMirror.fromTextArea(this.jq[0], this.options);
}

PrimeFaces.extend(PrimeFacesExt.widget.CodeMirror, PrimeFaces.widget.BaseWidget);

/**
 * This method fires an event if the behavior was defined.
 *
 * @param {string} eventName The name of the event.
 * @protected
 */
PrimeFacesExt.widget.CodeMirror.prototype.fireEvent = function(eventName) {
	if (this.cfg.behaviors) {
		var callback = this.cfg.behaviors[eventName];
	    if (callback) {
	    	var ext = {
	    			params: {}
	    	};

	    	callback.call(this, null, ext);
	    }
	}
}
