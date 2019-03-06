/**
 * PrimeFaces Extensions InputPhone Widget.
 * 
 * @author Jasper de Vries jepsar@gmail.com
 */
PrimeFaces.widget.ExtInputPhone = PrimeFaces.widget.BaseWidget.extend({

    /**
     * Initializes the widget.
     * 
     * @param {object}
     *            cfg The widget configuration.
     */
    init : function (cfg) {
        this._super(cfg);
        this.id = cfg.id;
        this.cfg = cfg;
        this.input = document.querySelector(this.jqId + "_input");
        this.iti = intlTelInput(this.input, cfg);

        this.inputJq = $(this.jqId + "_input");
        this.inputJq.data(PrimeFaces.CLIENT_ID_DATA, this.id);
        PrimeFaces.skinInput(this.inputJq);

        this.bindEvents();
    },

    bindEvents : function () {
        var $this = this;

        this.input.addEventListener('countrychange', function () {
            if ($this.hasBehavior('countrySelect')) {
                var country = $this.iti.getSelectedCountryData();
                var ext = {
                    params : [ {
                        name : $this.id + '_name',
                        value : country.name
                    }, {
                        name : $this.id + '_iso2',
                        value : country.iso2
                    }, {
                        name : $this.id + '_dialCode',
                        value : country.dialCode
                    } ]
                };
                $this.callBehavior('countrySelect', ext);
            }
        });
    },

    /**
     * Get the current number in the given format.
     */
    getNumber : function () {
        if (this.iti) {
            return this.iti.getNumber();
        }
        return '';
    },

    /**
     * Insert a number, and update the selected flag accordingly. Note that if
     * formatOnDisplay is enabled, this will attempt to format the number
     * according to the nationalMode option.
     * 
     * @param number
     *            The value to set
     */
    setNumber : function (number) {
        if (this.iti) {
            this.iti.setNumber(number);
        }
    },

    /**
     * Get the country data for the currently selected flag.
     */
    getCountry : function () {
        if (this.iti) {
            return this.iti.getSelectedCountryData();
        }
        return '';
    },

    /**
     * Change the country selection (e.g. when the user is entering their
     * address).
     * 
     * @param country
     *            The country code
     */
    setCountry : function (country) {
        if (this.iti) {
            this.iti.setCountry(country);
        }
    },

    /**
     * Change the placeholderNumberType option.
     * 
     * @param type
     *            the new type like "FIXED_LINE"
     */
    setPlaceholderNumberType : function (type) {
        if (this.iti) {
            this.iti.setPlaceholderNumberType(type);
        }
    },

    /**
     * Validate the current number
     * 
     * @return true if valid, false if not
     */
    isValidNumber : function () {
        if (this.iti) {
            return this.iti.isValidNumber();
        }
    },

    /**
     * Get more information about a validation error. Can look up the error code
     * in utils.js.
     */
    getValidationError : function () {
        if (this.iti) {
            return this.iti.getValidationError();
        }
        return 0;
    },

    // @override
    refresh : function (cfg) {
        if (this.iti) {
            this.iti.destroy();
        }
        this._super(cfg);
    },

    // @override
    destroy : function () {
        this._super();
        if (this.iti) {
            this.iti.destroy();
        }
    }

});
