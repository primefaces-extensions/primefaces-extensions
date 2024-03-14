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

        PrimeFaces.skinInput(this.jq);

        // user extension to configure places
        this.configureExtender();

        // wait for Google script to be loaded then initialize
        this.waitForGoogle();
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
     * Configures the input to be a Google Places AutoComplete.
     */
    initializeInput: function () {
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

        this.bindEvents();
    },

    /**
     * Because Google prefers you load the script async and deferred we need to wait until it is ready.
     * @private
     */
    waitForGoogle: function () {
        let $this = this;
        let mapWaitCount = 0;
        let mapWaitMax = 5;

        function loadScript() {
            if (typeof google !== 'undefined') {
                $this.initializeInput();
            } else if (mapWaitCount < mapWaitMax) {
                PrimeFaces.debug('Google API waiting attempt #' + mapWaitCount);
                PrimeFaces.queueTask(loadScript, 1000);
                mapWaitCount++;
            } else {
                PrimeFaces.error('Failed to load Google API.');
            }
        }

        loadScript();
    },

    /**
     * Bind the AJAX and client side events for when a place has changed.
     */
    bindEvents: function () {
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
                        address1 += component.short_name;
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
     * @return {google.maps.places.Place} The current map instance.
     */
    getPlace: function() {
        return this.autocomplete.getPlace();
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