/**
 * __PrimeFaces InputPlace Widget__
 *
 * InputPlace is a component to wrap Google Places AutoComplete functionality.
 *
 *  @typedef PrimeFaces.widget.ExtInputPlace.InputPlaceExtender Name of JavaScript function to extend the options of the
 *  underlying Google Places plugin. Access the widget via this context, and change the Google Places
 *  configuration stored in `this.cfg`. See also {@link PrimeFaces.widget.InputPlaceCfg.extender}.
 *  @this {PrimeFaces.widget.ExtInputPlace} PrimeFaces.widget.ExtInputPlace.InputPlaceExtender
 *
 * @prop {google.maps.places.Autocomplete} autocomplete Google Places Autocomplete controller
 * @prop {google.maps.places.Place}  currentPlace Current place found by the AutoComplete
 *
 * @interface {PrimeFaces.widget.InputPlaceCfg} cfg The configuration for the {@link  InputPlace| InputPlace widget}.
 * You can access this configuration via {@link PrimeFaces.widget.BaseWidget.cfg|BaseWidget.cfg}. Please note that this
 * configuration is usually meant to be read-only and should not be modified.
 * @extends {PrimeFaces.widget.BaseWidgetCfg} cfg
 *
 *  @prop {PrimeFaces.widget.ExtInputPlace.InputPlaceExtender} cfg.extender Name of JavaScript function to extend the
 *  options of the underlying Google Places plugin. Access to this widget via the context, and change the Google Places
 *  configuration stored in `this.cfg`.
 * @prop {string} cfg.restrictCountries The array of countries to constrain results from
 * @prop {string} cfg.restrictTypes The array of types to constrain results from
 */
PrimeFaces.widget.ExtInputPlace = PrimeFaces.widget.BaseWidget.extend({

    /**
     * @override
     * @inheritdoc
     * @param {PrimeFaces.PartialWidgetCfg<TCfg>} cfg
     */
    init: function (cfg) {
        this._super(cfg);

        // add styling
        PrimeFaces.skinInput(this.jq);

        // user extension to configure places
        this.configureExtender();

        if (this.cfg.apiType === "google") {
            // wait for Google script to be loaded then initialize
            this.configureGoogle();
        } else if (this.cfg.apiType === "azure") {
            // load Azure
            this.configureAzure();
        }
        else {
            // leave the field a plain old input text
            PrimeFaces.debug("InputPlace is not configured for Google or Azure");
        }
    },

    /**
     * Configures the input to be a Google Places AutoComplete.
     */
    configureExtender: function () {
        let extender = this.cfg.extender;
        if (extender) {
            if (typeof extender === "function") {
                extender.call(this);
            } else {
                PrimeFaces.error("Extender value is not a javascript function!");
            }
        }
    },

    /**
     * Configure Azure to use jQuery Autocomplete to bind to input element.
     * @private
     */
    configureAzure: function () {
        const $this = this;

        if (!this.cfg.apiKey) {
            PrimeFaces.error('Microsoft Azure API Key is required to use this component!');
            return;
        }

        const searchType = this.cfg.restrictTypes || 'fuzzy';
        const countrySet = this.cfg.restrictCountries === "auto" ? navigator.language.slice(-2).toLowerCase() : this.cfg.restrictCountries;
        const geocodeServiceUrlTemplate = 'https://atlas.microsoft.com/search/{searchType}/json?typeahead=true&api-version=1&subscription-key={apiKey}&query={query}&language={language}&countrySet={countrySet}&view=Auto';

        this.jq.autocomplete({
            minLength: 3,
            source: function (request, response) {
                const requestUrl = geocodeServiceUrlTemplate
                    .replace('{query}', encodeURIComponent(request.term))
                    .replace('{apiKey}', $this.cfg.apiKey)
                    .replace('{searchType}', searchType)
                    .replace('{language}', navigator.language)
                    .replace('{countrySet}', countrySet || '');

                $this.processAzureRequest(requestUrl).then(data => {
                    response(data.results);
                });
            },
            open: function( event, ui ) {
                // #1551 in dialogs must always stay on top
                $('.ui-inputplace.ui-autocomplete-panel').css('z-index', PrimeFaces.nextZindex());
            }
        });

        const autocompleteInstance = this.jq.autocomplete("instance");
        autocompleteInstance._renderMenu = function (ul, items) {
            items.forEach(item => {
                this._renderItemData(ul, item);
            });
            const poweredBy = $("<li>", {
                "class": "ui-inputplace-item ui-autocomplete-item ui-autocomplete-list-item ui-corner-all ui-state-disabled",
                "style": "display:flex;align-items: center;justify-content: end;margin-right: 1rem;opacity: 1 !important;",
                "html": `Powered By <svg xmlns="http://www.w3.org/2000/svg" width="100" height="30" viewBox="0 0 52.917 15.245"><path d="M-259.186 274.575c63.563-11.228 116.063-20.52 116.666-20.648l1.096-.233-60.01-71.38c-33.005-39.259-60.01-71.532-60.01-71.718 0-.353 61.966-170.992 62.314-171.6.116-.202 42.286 72.602 102.221 176.483C-40.796 212.735 5.467 292.922 5.897 293.674l.781 1.366-190.717-.025-190.717-.024 115.57-20.416zm731.379-17.263c-29.031-1.861-45.916-18.392-50.39-49.334-1.19-8.231-1.195-8.445-1.317-61.88l-.118-51.718H446.065l.101 50.024c.091 45.085.149 50.371.58 53.543 1.75 12.872 5.232 21.527 11.16 27.74 4.746 4.973 10.301 7.884 17.991 9.43 3.628.728 13.948.73 17.256.002 7.796-1.715 14.044-5.108 19.556-10.618 6.282-6.28 10.93-15.19 13.179-25.266l.758-3.395.084-50.448.085-50.447h26.234v158.609h-25.964v-12.582c0-8.55-.094-12.552-.292-12.485-.161.053-.828 1.073-1.482 2.266-4.474 8.16-11.927 15.62-20.095 20.116-9.79 5.388-19.609 7.304-33.023 6.444zm294.666-.128c-10.248-.77-21.038-4.293-29.858-9.749-18.589-11.499-29.588-30.452-32.808-56.535-1.114-9.026-1.248-21.15-.318-28.819 2.077-17.137 8.811-33.96 18.4-45.97 2.459-3.079 8.036-8.655 11.114-11.113 8.314-6.638 18.039-11.28 28.364-13.539 6.015-1.316 16.61-1.934 23-1.341 16.056 1.488 30.772 9.047 40.773 20.945 10.162 12.089 15.747 28.98 16.46 49.78.112 3.26.14 8.975.06 12.7l-.142 6.774-56.233.07-56.233.072v2.51c0 7.639 1.857 16.34 5.068 23.742 2.769 6.384 7.535 13.355 11.437 16.728 8.002 6.917 17.794 11.057 28.51 12.054 3.973.369 14.099-.02 18.626-.716 12.918-1.985 25.2-7.052 35.118-14.488 1.167-.874 2.283-1.692 2.481-1.816.29-.182.358 2.104.351 11.712l-.009 11.938-2.657 1.644c-11.22 6.939-24.073 11.395-37.682 13.063-4.062.497-18.965.72-23.822.354zm48.94-100.685c0-12.796-5.393-27.011-13.024-34.33-5.446-5.224-12.03-8.417-19.856-9.629-3.702-.573-11.506-.353-15.41.436-8.252 1.668-15.078 5.296-21.031 11.178-6.266 6.192-10.964 13.712-13.918 22.28-1.062 3.08-2.305 8.139-2.69 10.952l-.184 1.34h86.112zM53.01 253.2c.066-.194 19.161-50.359 42.434-111.477L137.759 30.6l13.596-.001 13.596-.001 1.128 2.893c3.447 8.84 84.71 219.821 84.71 219.931 0 .072-6.508.13-14.464.13l-14.463-.004-11.713-31.183-11.712-31.182-47.159-.001H104.12l-.41 1.058c-.225.582-5.229 14.613-11.12 31.18l-10.71 30.123-14.494.005c-11.473.004-14.469-.069-14.374-.348zm136.818-85.815c0-.05-7.882-21.417-17.515-47.484-18.022-48.766-19.03-51.7-20.455-59.53-.667-3.671-1.005-3.773-1.401-.423-.284 2.394-1.512 7.752-2.46 10.724-.469 1.475-8.585 23.75-18.034 49.5-9.45 25.751-17.181 46.928-17.181 47.061 0 .133 17.335.242 38.523.242 21.188 0 38.523-.04 38.523-.09zm69.71 82.106v-4.061l46.984-64.59 46.984-64.589-42.54-.14-42.539-.142-.074-10.795-.074-10.795h123.056v7.292l-46.99 64.942c-25.845 35.718-46.99 65.001-46.99 65.075 0 .073 20.891.133 46.426.133h46.425v21.731H259.538zm344.78 3.873c-.103-.103-.187-35.917-.187-79.586V94.38H629.814v16.369c0 9.003.11 16.369.243 16.369.134 0 .759-1.474 1.389-3.275 2.887-8.254 7.894-15.966 14.344-22.095 5.811-5.522 12.453-8.824 20.29-10.087 2.2-.355 4.063-.423 8.466-.308 5.53.144 8.42.548 11.783 1.647l1.058.346v26.658l-3.034-1.519c-5.359-2.683-10.647-3.744-17.004-3.414-4.137.215-6.852.747-10.16 1.988-6.805 2.554-12.325 7.062-16.182 13.215-5.584 8.909-9.566 20.36-10.74 30.883-.211 1.889-.336 17.27-.385 47.594l-.073 44.802h-12.65c-6.959 0-12.736-.084-12.84-.188zm-1092.387-.557c0-.1 28.276-49.187 62.836-109.08l62.837-108.897 73.229-61.454c40.276-33.8 73.337-61.495 73.47-61.545.132-.051-.398 1.283-1.178 2.963-.78 1.68-36.562 78.43-79.516 170.554l-78.098 167.499-56.79.071c-31.235.04-56.79-.011-56.79-.111z" style="fill:#0089d6;fill-opacity:1;stroke-width:.28222221" transform="translate(19.416 3.508) scale(.03978)"/></svg>`
            });
            poweredBy.data("ui-autocomplete-item", "")
            $(ul).addClass("ui-inputplace ui-autocomplete-panel ui-autocomplete-items ui-autocomplete-list ui-widget-content ui-widget ui-corner-all ui-helper-reset")
                .append(poweredBy)
        };

        autocompleteInstance._renderItem = function (ul, item) {
            let suggestionLabel = item.address.freeformAddress;
            if (item.poi && item.poi.name) {
                suggestionLabel = `${item.poi.name} (${suggestionLabel})`;
            }
            function highlightWords(text, searchTerms) {
                const searchTermsArray = searchTerms.split(' ');
                // Escape special characters in the search terms
                const escapedSearchTerms = searchTermsArray.map(term =>
                    term.replace(/[.*+?^${}()|[\]\\]/g, '\\$&')
                );

                // Create a regular expression to match the search terms globally and case insensitively
                const regex = new RegExp(`\\b(${escapedSearchTerms.join('|')})\\b`, 'gi');

                // Replace the matched words with span elements
                return text.replace(regex, '<span class="ui-autocomplete-query">$1</span>');
            }

            suggestionLabel = highlightWords(suggestionLabel, $this.jq.val());

            return $("<li>", {
                "class": "ui-inputplace-item ui-autocomplete-item ui-autocomplete-list-item ui-corner-all ui-state-default",
                "html": `<a><svg width="16" height="16" viewBox="0 0 24 24" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns="http://www.w3.org/2000/svg" xmlns:cc="http://creativecommons.org/ns#" xmlns:dc="http://purl.org/dc/elements/1.1/">
                        <g transform="translate(0 -1028.4)">
                            <path d="m12 0c-4.4183 2.3685e-15 -8 3.5817-8 8 0 1.421 0.3816 2.75 1.0312 3.906 0.1079 0.192 0.221 0.381 0.3438 0.563l6.625 11.531 6.625-11.531c0.102-0.151 0.19-0.311 0.281-0.469l0.063-0.094c0.649-1.156 1.031-2.485 1.031-3.906 0-4.4183-3.582-8-8-8zm0 4c2.209 0 4 1.7909 4 4 0 2.209-1.791 4-4 4-2.2091 0-4-1.791-4-4 0-2.2091 1.7909-4 4-4z" transform="translate(0 1028.4)" fill="#e74c3c"/>
                            <path d="m12 3c-2.7614 0-5 2.2386-5 5 0 2.761 2.2386 5 5 5 2.761 0 5-2.239 5-5 0-2.7614-2.239-5-5-5zm0 2c1.657 0 3 1.3431 3 3s-1.343 3-3 3-3-1.3431-3-3 1.343-3 3-3z" transform="translate(0 1028.4)" fill="#c0392b"/>
                        </g>
                    </svg> ${suggestionLabel}</a>`
            }).appendTo(ul);
        };

        this.addDestroyListener(() => {
            $this.jq.autocomplete("destroy");
        });

        this.bindAzureEvents();
    },

    /**
     * Bind the AJAX and client side events for when a place has changed.
     */
    bindAzureEvents: function () {
        let $this = this;

        PrimeFaces.utils.registerResizeHandler(this, 'resize.' + this.id + '_hide', this.jq, function () {
            $this.jq.autocomplete("close");
        });
        PrimeFaces.utils.registerScrollHandler(this, 'scroll.' + this.id + '_hide', function () {
            $this.jq.autocomplete("close");
        });

        // do not allow the Powered By to be accessible as an option
        this.jq.on("autocompleteopen", function (event, ui) {
            $('.ui-autocomplete').find('svg').removeClass('ui-menu-item-wrapper').removeAttr('tabindex');
        });

        this.jq.on("autocompletefocus", function (event, ui) {
            const place = ui.item;
            const address = place.address.freeformAddress;
            PrimeFaces.queueTask(() => $(this).val(address), 0);
        });

        this.jq.on("autocompleteselect", function (event, ui) {
            const place = ui.item;
            if (!place.position || !place.address) {
                // User entered the name of a Place that was not suggested and
                // pressed the Enter key, or the Place Details request failed.
                PrimeFaces.error(`No details available for input! '${place}'`);
                return;
            }

            PrimeFaces.debug(place);
            $this.currentPlace = place;

            // Call user onPlaceChanged client side callback
            if ($this.cfg.onPlaceChanged) {
                $this.cfg.onPlaceChanged.call(this, place);
            }

            let name = place.address.freeformAddress;
            let poiUrl = '';
            let poiPhone = '';
            if (place.poi && place.poi.name) {
                name = place.poi.name;
                poiUrl = place.poi.url;
                poiPhone = place.poi.phone;
            }
            const address = place.address;
            const streetNumber = address.streetNumber;
            let streetName = address.streetName;
            if (streetNumber) {
                streetName = `${streetNumber} ${streetName}`;
            }
            const postcode = address.extendedPostalCode || address.postalCode;
            const city = address.localName;
            const state = address.countrySubdivision;
            const country = address.country;
            const countryCode = address.countryCode;
            const administrative_area_level_1 = address.countrySubdivisionName;
            const administrative_area_level_2 = address.municipality;
            const administrative_area_level_3 = address.countrySecondarySubdivision;

            // Call ajax placeChanged behaviour
            if ($this.hasBehavior('placeChanged')) {
                let ext = {
                    params: [{
                        name: $this.id + '_lat',
                        value: place.position.lat
                    }, {
                        name: $this.id + '_lng',
                        value: place.position.lon
                    }, {
                        name: $this.id + '_formatted_address',
                        value: address.freeformAddress
                    }, {
                        name: $this.id + '_name',
                        value: name
                    }, {
                        name: $this.id + '_address',
                        value: streetName
                    }, {
                        name: $this.id + '_postcode',
                        value: postcode
                    }, {
                        name: $this.id + '_city',
                        value: city
                    }, {
                        name: $this.id + '_state',
                        value: state
                    }, {
                        name: $this.id + '_country',
                        value: country
                    }, {
                        name: $this.id + '_country_code',
                        value: countryCode
                    }, {
                        name: $this.id + '_administrative_area_level_1',
                        value: administrative_area_level_1
                    }, {
                        name: $this.id + '_administrative_area_level_2',
                        value: administrative_area_level_2
                    }, {
                        name: $this.id + '_administrative_area_level_3',
                        value: administrative_area_level_3
                    }, {
                        name: $this.id + '_url',
                        value: poiUrl
                    }, {
                        name: $this.id + '_phone',
                        value: poiPhone
                    }, {
                        name: $this.id + '_types',
                        value: place.type
                    }, {
                        name: $this.id + '_place_id',
                        value: place.id
                    }]
                };

                $this.callBehavior('placeChanged', ext);
            }

            // Update input field value and maintain focus
            PrimeFaces.queueTask(() => $(this).val(address.freeformAddress).focus(), 0);
        });
    },

    /**
     * Because Google prefers you load the script async and deferred we need to wait until it is ready.
     * @private
     * @param {string} url the URL to call Microsoft Azure Maps
     */
    processAzureRequest: function (url) {
        return new Promise((resolve, reject) => {
            fetch(url, {
                method: 'GET',
                mode: 'cors',
            }).then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            }).then(data => {
                resolve(data);
            }).catch(error => {
                reject(error);
            });
        });
    },

    /**
     * Because Google prefers you load the script async and deferred we need to wait until it is ready.
     * @private
     */
    configureGoogle: function () {
        let $this = this;
        let mapWaitCount = 0;
        let mapWaitMax = 5;

        function loadScript() {
            if (typeof google !== 'undefined') {
                $this.initializeGoogleInput();
            } else if (mapWaitCount < mapWaitMax) {
                PrimeFaces.debug('Google API waiting attempt #' + mapWaitCount);
                PrimeFaces.queueTask(loadScript, 500);
                mapWaitCount++;
            } else {
                PrimeFaces.error('Failed to load Google API JS which is required for this component!');
            }
        }

        loadScript();
    },

    /**
     * Configures the input to be a Google Places AutoComplete.
     */
    initializeGoogleInput: function () {
        const input = this.jq[0];
        const options = {
            fields: ["address_components", "geometry", "formatted_address", "place_id", "types", "name"],
            strictBounds: false
        };
        this.autocomplete = new google.maps.places.Autocomplete(input, options);

        if (this.cfg.restrictCountries) {
            let countries = this.cfg.restrictCountries === "auto"
                ? navigator.language.slice(-2).toLowerCase()
                : this.cfg.restrictCountries.split(',', 5);
            this.autocomplete.setComponentRestrictions({
                country: countries
            });
        }

        if (this.cfg.restrictTypes) {
            this.autocomplete.setTypes(this.cfg.restrictTypes.split(',', 5));
        }

        this.bindGoogleEvents();

        this.addDestroyListener(() => {
            PrimeFaces.debug("Google does not have any way to destroy the AutoComplete!");
        });
    },

    /**
     * Bind the AJAX and client side events for when a place has changed.
     */
    bindGoogleEvents: function () {
        let $this = this;
        this.autocomplete.addListener("place_changed", () => {
            const place = $this.autocomplete.getPlace();
            if (!place.geometry || !place.geometry.location) {
                // User entered the name of a Place that was not suggested and
                // pressed the Enter key, or the Place Details request failed.
                PrimeFaces.error(`No details available for input: '${place.name}'`);
                return;
            }
            PrimeFaces.debug(place);
            $this.currentPlace = place;

            // Call user onPlaceChanged client side callback
            if ($this.cfg.onPlaceChanged) {
                $this.cfg.onPlaceChanged.call(this, place);
            }

            let address1 = "";
            let postcode = "";
            let city = "";
            let state = "";
            let country = "";
            let countryCode = "";
            let administrative_area_level_1 = "";
            let administrative_area_level_2 = "";
            let administrative_area_level_3 = "";

            // Get each component of the address from the place details,
            // and then fill-in the corresponding field on the form.
            // place.address_components are google.maps.GeocoderAddressComponent objects
            // which are documented at http://goo.gle/3l5i5Mr
            for (const component of place.address_components) {
                const componentType = component.types[0];

                switch (componentType) {
                    case "street_number": {
                        address1 = `${component.long_name} ${address1}`;
                        break;
                    }
                    case "route": {
                        address1 += component.long_name;
                        break;
                    }
                    case "postal_code": {
                        postcode = `${component.long_name}${postcode}`;
                        break;
                    }
                    case "postal_code_suffix": {
                        postcode = `${postcode}-${component.long_name}`;
                        break;
                    }
                    case "locality":
                        city = component.long_name;
                        break;
                    case "administrative_area_level_1": {
                        state = component.short_name;
                        administrative_area_level_1 = component.long_name;
                        break;
                    }
                    case "administrative_area_level_2": {
                        administrative_area_level_2 = component.long_name;
                        break;
                    }
                    case "administrative_area_level_3": {
                        administrative_area_level_3 = component.long_name;
                        break;
                    }
                    case "country":
                        country = component.long_name;
                        countryCode = component.short_name;
                        break;
                }
            }

            // Call ajax placeChanged behaviour
            if ($this.hasBehavior('placeChanged')) {
                let ext = {
                    params: [{
                        name: $this.id + '_lat',
                        value: place.geometry.location.lat()
                    }, {
                        name: $this.id + '_lng',
                        value: place.geometry.location.lng()
                    }, {
                        name: $this.id + '_formatted_address',
                        value: place.formatted_address
                    }, {
                        name: $this.id + '_name',
                        value: place.name
                    }, {
                        name: $this.id + '_address',
                        value: address1
                    }, {
                        name: $this.id + '_postcode',
                        value: postcode
                    }, {
                        name: $this.id + '_city',
                        value: city
                    }, {
                        name: $this.id + '_state',
                        value: state
                    }, {
                        name: $this.id + '_country',
                        value: country
                    }, {
                        name: $this.id + '_country_code',
                        value: countryCode
                    }, {
                        name: $this.id + '_administrative_area_level_1',
                        value: administrative_area_level_1
                    }, {
                        name: $this.id + '_administrative_area_level_2',
                        value: administrative_area_level_2
                    }, {
                        name: $this.id + '_administrative_area_level_3',
                        value: administrative_area_level_3
                    }, {
                        name: $this.id + '_types',
                        value: place.types && place.types.join(',')
                    }, {
                        name: $this.id + '_place_id',
                        value: place.place_id
                    }]
                };

                $this.callBehavior('placeChanged', ext);
            }
        });
    },

    /**
     * Returns the current Google Places instance.
     * @return {google.maps.places.Place} The current map place.
     */
    getPlace: function () {
        return this.currentPlace;
    },

    /**
     * Disables this input so that the user cannot enter a value anymore.
     */
    disable: function () {
        PrimeFaces.utils.disableInputWidget(this.jq);
    },

    /**
     * Enables this input so that the user can enter a value.
     */
    enable: function () {
        PrimeFaces.utils.enableInputWidget(this.jq);
    }
});