/**
 * @namespace The CKeditor Extensions root namespace.
 */
CKEditorExtension = {

    /**
     * Checks if the FacesServlet is mapped with extension mapping. For example:
     * .jsf/.xhtml.
     *
     * @author Thomas Andraschko
     * @returns {boolean} If mapped with extension mapping.
     */
    isExtensionMapping: function () {
        if (!CKEditorExtension.IS_EXTENSION_MAPPING) {
            var scriptURI = PrimeFacesExt.getResourceScriptURI();
            var scriptName = PrimeFacesExt.getResourceScriptName(scriptURI);

            CKEditorExtension.IS_EXTENSION_MAPPING = scriptURI.charAt(scriptURI.indexOf(scriptName) + scriptName.length) === '.';
        }

        return CKEditorExtension.IS_EXTENSION_MAPPING;
    },

    getScript: function (url, callback) {
        $.ajax({
            type: "GET",
            url: url,
            success: callback,
            dataType: "script",
            cache: true,
            async: true,
            scriptAttrs: {
                nonce: PrimeFaces.csp.NONCE_VALUE
            }
        });
    }
};

/**
 * Resolves the resources for the CKEditor.
 *
 * @param {string} resource The requested resource from CKEditor.
 * @returns {string} The faces resource URL.
 */
CKEDITOR_GETURL = function (resource) {
    var facesResource;

    // GitHub #545 IE11 support
    if (PrimeFaces.env.isIE()) {
        if (!String.prototype.startsWith) {
            String.prototype.startsWith = function (searchString, position) {
                position = position || 0;
                return this.indexOf(searchString, position) === position;
            };
        }
    }

    //do not resolve
    if (resource.indexOf('?resolve=false') !== -1) {
        facesResource = resource.replace('?resolve=false', '');
    } else {
        //already wrapped? check extensions "e="
        var libraryVersion = 'e=' + PrimeFacesExt.VERSION;
        var libraryVersionIndex = resource.indexOf(libraryVersion);
        if (libraryVersionIndex === -1) {
            // Omnifaces https://github.com/primefaces-extensions/primefaces-extensions/issues/294
            libraryVersion = 'v=' + PrimeFacesExt.VERSION;
            libraryVersionIndex = resource.indexOf(libraryVersion);
        }
        if (libraryVersionIndex !== -1) {
            //look for appended resource
            var appendedResource = resource.substring(libraryVersionIndex + (libraryVersion).length);

            if (appendedResource.length > 0) {
                //remove append resource from url
                facesResource = resource.substring(0, resource.length - appendedResource.length);

                // GitHub ##509 check for URL param
                if (appendedResource.startsWith('&')) {
                    // example: replace &conversationContext=33
                    appendedResource = appendedResource.replace(/&\w+=\d+(?:\.\d+)*/, '');
                }

                var resourceIdentiferPosition = facesResource.indexOf(PrimeFaces.RESOURCE_IDENTIFIER);

                if (CKEditorExtension.isExtensionMapping()) {
                    var extension = '.' + PrimeFacesExt.getResourceUrlExtension();
                    var extensionMappingPosition = facesResource.lastIndexOf(extension);
                    if (extensionMappingPosition === -1) {
                        extensionMappingPosition = facesResource.lastIndexOf('.xhtml');
                        if (extensionMappingPosition === -1) {
                            extensionMappingPosition = facesResource.lastIndexOf('.jsf');
                        }
                    }

                    if (extensionMappingPosition === -1) {
                        console.error('Could not find .jsf or .xhtml extension!');
                    }

                    //extract resource
                    var extractedResource = facesResource.substring(resourceIdentiferPosition + PrimeFaces.RESOURCE_IDENTIFIER.length, extensionMappingPosition);

                    facesResource = PrimeFaces.resources.getFacesResource(extractedResource + appendedResource, PrimeFacesExt.RESOURCE_LIBRARY, PrimeFacesExt.VERSION);
                } else {
                    var questionMarkPosition = facesResource.indexOf('?');

                    //extract resource
                    var extractedResource = facesResource.substring(resourceIdentiferPosition + PrimeFaces.RESOURCE_IDENTIFIER.length, questionMarkPosition);

                    facesResource = PrimeFaces.resources.getFacesResource(extractedResource + appendedResource, PrimeFacesExt.RESOURCE_LIBRARY, PrimeFacesExt.VERSION);
                }
            } else {
                facesResource = resource;
            }
        } else {
            if (resource.indexOf(PrimeFaces.RESOURCE_IDENTIFIER) === -1) {
                facesResource = PrimeFaces.resources.getFacesResource('ckeditor/' + resource, PrimeFacesExt.RESOURCE_LIBRARY, PrimeFacesExt.VERSION);
            } else {
                facesResource = resource;
            }
        }
    }

    return facesResource;
};