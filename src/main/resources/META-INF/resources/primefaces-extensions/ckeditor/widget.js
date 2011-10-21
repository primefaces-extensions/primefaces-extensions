CKEDITOR_GETURL = function(resource) {
	var facesResource;
	
	// already wrapped?
	var libraryVersionIndex = resource.indexOf('v=' + PrimeFacesExt.getPrimeFacesExtensionsVersion());
	if (libraryVersionIndex !== -1) {
		// look for appended resource
		var appendedResource = resource.substring(libraryVersionIndex + ('v=' + PrimeFacesExt.getPrimeFacesExtensionsVersion()).length);

		if (appendedResource.length > 0) {
			// remove append resource from url
			facesResource = resource.substring(0, resource.length - appendedResource.length);

			var resourceIdentiferPosition = facesResource.indexOf(PrimeFacesExt.RESOURCE_IDENTIFIER);
			
			if (PrimeFacesExt.isExtensionMapping()) {
				var extensionMappingPosition = facesResource.indexOf(PrimeFacesExt.getRequestUrlExtension());

				// extract resource
				var extractedResource = facesResource.substring(resourceIdentiferPosition + PrimeFacesExt.RESOURCE_IDENTIFIER.length, extensionMappingPosition);

				facesResource = PrimeFacesExt.getPrimeFacesExtensionsResource(extractedResource + appendedResource);
			} else {
				var questionMarkPosition = facesResource.indexOf('?');

				// extract resource
				var extractedResource = facesResource.substring(resourceIdentiferPosition + PrimeFacesExt.RESOURCE_IDENTIFIER.length, questionMarkPosition);

				facesResource = PrimeFacesExt.getPrimeFacesExtensionsResource(extractedResource + appendedResource);
			}
		} else {
			facesResource = resource;
		}
	} else {		
		facesResource = PrimeFacesExt.getPrimeFacesExtensionsResource('/ckeditor/' + resource);
	}
	
	return facesResource;
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
		$.getScript(PrimeFacesExt.getPrimeFacesExtensionsResource('/ckeditor/ckeditor.js'), function(data, textStatus) {
			// load jquery adapter
			$.getScript(PrimeFacesExt.getPrimeFacesExtensionsResource('/ckeditor/adapters/jquery.js'), function(data, textStatus) {
				_self.resourcesLoaded();
			});
		});
	} else {
		this.resourcesLoaded();
	}
}

PrimeFaces.extend(PrimeFacesExt.widget.CKEditor, PrimeFaces.widget.BaseWidget);

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
	this.postConstruct();
}
