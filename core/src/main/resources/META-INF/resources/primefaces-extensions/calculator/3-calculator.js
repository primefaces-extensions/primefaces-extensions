/**
 * PrimeFaces Extensions Calculator Widget.
 *
 * @author Melloware info@melloware.com
 * @since 6.1
 */
PrimeFaces.widget.ExtCalculator = PrimeFaces.widget.BaseWidget.extend({

    /**
     * Initializes the widget.
     *
     * @param {object}
     *        cfg The widget configuration.
     */
    init: function (cfg) {
        this._super(cfg);
        this.id = cfg.id;
        this.cfg = cfg;
        this.target = PrimeFaces.expressions.SearchExpressionFacade.resolveComponentsAsSelector(this.cfg.target);

        var input = this.target;
        if (this.target.is(':input')) {
            input = this.target;
        } else {
            input = $(':not(:submit):not(:button):input:enabled:first', this.target);
            var inputId = input.attr('id');
            // PF InputNumber must use hidden input
            if (inputId.endsWith("_input")) {
                inputId = inputId.replace("_input", "_hinput");
                input = $(PrimeFaces.escapeClientId(inputId));
            }
        }

        if (input.length === 0) {
            alert('Calculator must use for="target" or be nested inside an input!');
            console.error('Calculator must use for="target" or be nested inside an input!');
        } else {
            this._applyCalculator(input, cfg);
        }

        // GitHub #723: Fix for PrimeIcon themes
        if (PrimeFacesExt.isPrimeIconTheme()) {
            var button = $('.ui-icon-calculator');
            if (button.length) {
                button.parent().addClass('ui-prime-icons');
            }
        }
    },

    /**
     * Applies the calculator to the given jQuery selector object. Delete
     * previous calculator to support ajax updates and create a new one.
     *
     * @param {object}
     *        input A jQuery selector object.
     * @param {object}
     *        cfg The widget configuration.
     * @private
     */
    _applyCalculator: function (input, cfg) {
        this.input = input;

        // do not attach calculator if field is readonly
        if (this.input[0].readOnly) {
            return;
        }

        var layout = cfg.layout;

        if (layout) {
            if (layout === 'standard') {
                // Standard calculator layout with simple operations
                cfg.layout = $.calculator.standardLayout;
            } else if (layout === 'scientific') {
                // Extended calculator with common scientific operations
                cfg.layout = $.calculator.scientificLayout;
            } else {
                // custom layout of buttons
                cfg.layout = layout.split(',');
            }
        }

        // make a copy of the configuration
        var opts = $.extend(true, {}, cfg);

        // set language
        if (opts.locale) {
            // using jQuery combine cfg + local into one JSON object
            $.extend(opts, $.calculator.regionalOptions[cfg.locale], cfg);
        }

        // register callback for onUse so InputNumber can be set correctly
        opts.onUse = function (value, inst) {
            var id = inst._input.attr('id');
            if (id) {
                // PF InputNumber must call custom setValue() function
                if (id.endsWith("_hinput")) {
                    id = id.replace("_hinput", "");
                    var widget = PrimeFaces.getWidgetById(id);
                    if (widget && widget instanceof PrimeFaces.widget.InputNumber) {
                        widget.setValue(value);
                    } else {
                        PrimeFaces.error("Widget not found for id: " + id);
                    }
                } else {
                    // just set the value in a regular input
                    inst._input.val(value);
                }

            }
        }

        // create the calculator
        input.calculator('destroy').calculator(opts);

        // bind "open", "close" events
        this._bindEvents();
    },

    /**
     * Binds all events to p:ajax events
     *
     * @private
     */
    _bindEvents: function () {
        var $this = this;

        this.input.on("calculatoropen", function () {
            $this.callBehavior('open');
        }).on("calculatorclose", function () {
            $this.callBehavior('close');
        }).on("calculatorbutton", function (event, buttonName, calculatorValue) {
            var options = {
                params: [{
                    name: $this.id + '_button',
                    value: buttonName
                }, {
                    name: $this.id + '_value',
                    value: calculatorValue
                }]
            };
            $this.callBehavior('button', options);
        });
    },

    show: function () {
        if (this.input) {
            this.input.calculator('show') // Show the calculator
        }
    },

    hide: function () {
        if (this.input) {
            this.input.calculator('hide') // Hide the calculator
        }
    },

    enable: function () {
        if (this.input) {
            this.input.calculator('enable') // Enable the calculator
        }
    },

    disable: function () {
        if (this.input) {
            this.input.calculator('disable') // Disable the calculator
        }
    },

    isDisabled: function () {
        // Is the calculator disabled?
        var disabled = false;
        if (this.input) {
            disabled = this.input.calculator('isDisabled')
        }
        return disabled;
    },

    destroy: function () {
        if (this.input) {
            this.input.calculator('destroy');
            this.input = null;
        }
    }
});

/**
 * Global calculator defaults
 */
// set language to US English
$.calculator.setDefaults($.calculator.regionalOptions['']);
$.calculator.setDefaults({
    useThemeRoller: true, // True to add ThemeRoller classes
    buttonText: '.', // Display text for trigger button
    duration: 'fast', // Duration of display/closure
    isRTL: false
// True if right-to-left language, false if left-to-right
});
