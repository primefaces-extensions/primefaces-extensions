CKEDITOR_GETURL = function(resource) {
	var urlPattern;
	if (FACES_IS_EXTENSION_MAPPING == true) {
		// *.jsf -> jsf
		urlPattern = FACES_URL_PATTERN.substring(2);
	} else {
		// /faces/* -> /faces
		urlPattern = FACES_URL_PATTERN.substring(0, FACES_URL_PATTERN.length - 2);
	}
	
	var jsfResource;
	
	// already wrapped?
	var libraryVersionIndex = resource.indexOf(PrimeFacesExt.LIBRARY_VERSION);
	if (libraryVersionIndex !== -1) {
		// look for appended resource
		var appendedResource = resource.substring(libraryVersionIndex + PrimeFacesExt.LIBRARY_VERSION.length);
		
		// remove append resource from url
		jsfResource = resource.substring(0, resource.length - appendedResource.length);

		if (FACES_IS_EXTENSION_MAPPING == true) {
			// look for extension
			var extensionsPosition = jsfResource.indexOf('.' + urlPattern);
			
			// insert appended resource
			jsfResource = jsfResource.substring(0, extensionsPosition)
				+ appendedResource 
				+ jsfResource.substring(extensionsPosition);
		} else {
			// look for ?
			var questionMarkPosition = jsfResource.indexOf('?');

			// insert appended resource
			jsfResource = jsfResource.substring(0, questionMarkPosition)
				+ appendedResource 
				+ jsfResource.substring(questionMarkPosition);
		}
	} else {
		// build resource URL
		jsfResource = CKEDITOR_BASEPATH;
		
		if (FACES_IS_EXTENSION_MAPPING == false) {
			jsfResource = jsfResource + urlPattern;
		}

		jsfResource = jsfResource + PrimeFacesExt.RESOURCE_IDENTIFIER;
		jsfResource = jsfResource + '/ckeditor/';
		jsfResource = jsfResource + resource;

		if (FACES_IS_EXTENSION_MAPPING == true) {
			jsfResource = jsfResource + '.' + urlPattern;
		}

		jsfResource = jsfResource + '?ln=' + PrimeFacesExt.RESOURCE_LIBRARY;
		jsfResource = jsfResource + '&amp;v=' + PrimeFacesExt.LIBRARY_VERSION;
	}

	return jsfResource;
};

/**
 * PrimeFaces Extensions CKEditor Widget
 */
PrimeFacesExt.widget.CKEditor = function(id, cfg) {
	var _self = this;
	
	this.id = id;
	this.cfg = cfg;
	this.jqId = PrimeFaces.escapeClientId(this.id);
	this.jq = $(this.jqId);

	// set global variables
	window.CKEDITOR_BASEPATH = this.cfg.basePath;
	window.FACES_IS_EXTENSION_MAPPING = this.cfg.isExtensionMapping;
	window.FACES_URL_PATTERN = this.cfg.mappingUrlPattern;

	this.options = {};

	if (this.cfg.skin) {
		this.options.skin = this.cfg.skin;
	}
	if (this.cfg.width) {
		this.options.width = this.cfg.width;
	}
	if (this.cfg.height) {
		this.options.height = this.cfg.height;
	}
	if (this.cfg.theme) {
		this.options.theme = this.cfg.theme;
	}
	if (this.cfg.toolbar) {
		this.options.toolbar = this.cfg.toolbar;
	}
	if (this.cfg.readOnly) {
		this.options.readOnly = this.cfg.readOnly;
	}
	if (this.cfg.interfaceColor) {
		this.options.uiColor = this.cfg.interfaceColor;
	}
	if (this.cfg.language) {
		this.options.language = this.cfg.language;
	}
	if (this.cfg.defaultLanguage) {
		this.options.defaultLanguage = this.cfg.defaultLanguage;
	}
	if (this.cfg.contentsCss) {
		this.options.contentsCss = this.cfg.contentsCss;
	}

	// check if ckeditor is already included
	if (typeof(CKEDITOR) == 'undefined') {
		// load ckeditor
		$.getScript(this.cfg.editorResourceURL, function(data, textStatus) {
			// load jquery adapter
			$.getScript(_self.cfg.jqueryAdapterResourceURL, function(data, textStatus) {
				_self.resourcesLoaded();
			});
		});
	} else {
		this.resourcesLoaded();
	}
}

PrimeFacesExt.widget.CKEditor.prototype.resourcesLoaded = function() {
	var _self = this;
	
	// overwrite save button
	CKEDITOR.plugins.registered['save'] = {
		init : function(editor) {
			var command = editor.addCommand('save', {
				modes : { wysiwyg:1, source:1 },
				exec : function(editor) {
					if (_self.cfg.behaviors) {
						var saveCallback = _self.cfg.behaviors['save'];
					    if (saveCallback) {
					    	var ext = {
					    			params: {}
					    	};

					    	saveCallback.call(_self, null, ext);
					    }
					}
				}
			});
			editor.ui.addButton('Save', {label : editor.lang.save, command : 'save' });
		}
	}

	// remove old instances if required
	var oldInstance = CKEDITOR.instances[this.id];
	if (oldInstance) {
		oldInstance.destroy();
		delete oldInstance;
	}

	// initialize ckeditor after all resources were loaded
	this.jq.ckeditor(function() { _self.initialized(); }, _self.options);
}

PrimeFacesExt.widget.CKEditor.prototype.initialized = function() {
	this.instance = this.jq.ckeditorGet();
}
