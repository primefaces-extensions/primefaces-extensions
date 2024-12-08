/**
 * PrimeFaces Extensions InputOtp Widget.
 * Inspired from https://github.com/shuqikhor/Vanilla-OTP-Input
 *
 * @since 14.0.0
 */
PrimeFaces.widget.ExtInputOtp = class extends PrimeFaces.widget.BaseWidget {

    /**
     * Initializes the widget.
     *
     * @param {object}
     *            cfg The widget configuration.
     */
    init(cfg) {
        super.init(cfg);
        this.id = cfg.id;
        this.cfg = cfg;
        this.disabled = cfg.disabled;

        // JQuery inputs
        this.inputsJq = $(this.jqId + ' > .ui-inputotp-input');
        this.inputCount = this.inputsJq.length;
        this.hinput = $(this.jqId + '_hidden');

        // pfs metadata
        this.inputsJq.data(PrimeFaces.CLIENT_ID_DATA, this.id);
        this.hinput.data(PrimeFaces.CLIENT_ID_DATA, this.id);

        // style disabled if necessary
        if (this.disabled) {
            PrimeFaces.utils.disableInputWidget(this.inputsJq);
            this.hinput.attr("disabled", "disabled");
        }

        // ARIA and events
        this.setupARIA();
        this.bindEvents();

        // visual effects
        PrimeFaces.skinInput(this.inputsJq);

        //client Behaviors
        if (this.cfg.behaviors) {
            PrimeFaces.attachBehaviors(this.inputsJq, this.cfg.behaviors);
        }
    }

    /**
     * Set up the ARIA accessibility.
     */
    setupARIA() {
        let $this = this;
        this.ariaLabel = this.cfg.ariaLabel ||
            PrimeFaces.getAriaLabel('otpLabel', 'Please enter OTP/PIN character {0}');
        this.inputsJq.each(function (index, elem) {
            $(elem).attr('aria-label', $this.ariaLabel.replace('{0}', (index + 1)));
        });
    }

    bindEvents() {
        let $this = this;
        let inputsJq = this.inputsJq;

        // get the current attached events if using CSP
        let events = this.inputsJq[0] ? $._data(this.inputsJq[0], "events") : null;

        // use DOM if non-CSP and JQ event if CSP
        let originalOninput = this.inputsJq.prop('oninput');
        if (!originalOninput && events && events.input) {
            originalOninput = events.input[0].handler;
        }
        let originalOnkeydown = this.inputsJq.prop('onkeydown');
        if (!originalOnkeydown && events && events.keydown) {
            originalOnkeydown = events.keydown[0].handler;
        }
        let originalOnfocus = this.inputsJq.prop('onfocus');
        if (!originalOnfocus && events && events.focus) {
            originalOnfocus = events.focus[0].handler;
        }

        for (let i = 0; i < inputsJq.length; i++) {

            let input = inputsJq[i];

            $(input).prop('oninput', null).off('input').on('input.otp', function (e) {
                // sanitise input
                if ($this.cfg.integerOnly && input.value.length > 0 && isNaN(input.value)) {
                    input.value = '';
                    $this.updateInput();
                    return false;
                }
                // handling normal input
                if (input.value.length === 1 && i + 1 < inputsJq.length) {
                    inputsJq[i + 1].focus();
                }

                // if a value is pasted, put each character to each of the next input
                if (input.value.length > 1) {
                    // split characters to array
                    const chars = input.value.split('');

                    for (let pos = 0; pos < chars.length; pos++) {
                        // if length exceeded the number of inputs, stop
                        if (pos + i >= inputsJq.length)
                            break;

                        // paste value
                        inputsJq[pos + i].value = chars[pos];
                    }

                    // focus the input next to the last pasted character
                    let focus_index = Math.min(inputsJq.length - 1, i + chars.length);
                    inputsJq[focus_index].focus();
                }
                let originalValue = $this.updateInput();

                if (originalOninput && originalOninput.call(this, e) === false) {
                    $this.setValue(originalValue);
                    return false;
                }
            });

            $(input).prop('onkeydown', null).off('keydown').on('keydown.otp', function (e) {
                switch (e.code) {
                    case 'Backspace':
                    case 'Delete':
                        if (input.value === '' && i > 0) {
                            // shift next values towards the left
                            for (let pos = i; pos < inputsJq.length - 1; pos++) {
                                inputsJq[pos].value = inputsJq[pos + 1].value;
                            }

                            // clear previous box and focus on it
                            inputsJq[i - 1].value = '';
                            inputsJq[i - 1].focus();
                            inputsJq[i - 1].dispatchEvent(new Event("input"));
                            return;
                        }
                        break;
                    case 'ArrowUp':
                    case 'ArrowDown':
                        e.preventDefault();
                        break;
                    case 'ArrowLeft':
                        if (i > 0) {
                            e.preventDefault();
                            inputsJq[i - 1].focus();
                            inputsJq[i - 1].select();
                        }
                        break;
                    case 'ArrowRight':
                        if (i + 1 < inputsJq.length) {
                            e.preventDefault();
                            inputsJq[i + 1].focus();
                            inputsJq[i + 1].select();
                        }
                        break;
                    default:
                        break;
                }

                if (originalOnkeydown && originalOnkeydown.call(this, e) === false) {
                    return false;
                }
            });
        }

        inputsJq.prop('onfocus', null).off('focus').on('focus.otp', function (e) {
            if (originalOnfocus && originalOnfocus.call(this, e) === false) {
                return false;
            }
            this.select();
        });
    }

    /**
     *
     * @returns {string} The original value of the hidden input.
     */
    updateInput() {
        let oldValue = this.hinput.val();
        let newValue = '';
        for (let i = 0; i < this.inputsJq.length; i++) {
            newValue += this.inputsJq[i].value;
        }
        this.hinput.val(newValue);
        return oldValue;
    }

    getValue() {
        this.hinput.val();
    }

    /**
     * Sets the value of this input number widget to the given value.
     * @param {number | string} value The new value to set.
     */
    setValue(value) {
        if (value.length > this.inputCount || (this.cfg.integerOnly && isNaN(value))) {
            return;
        }
        const chars = value.split('');

        for (let pos = 0; pos < chars.length; pos++) {
            if (pos + i >= this.inputCount) {
                this.inputsJq[pos + i].value = '';
            } else {
                this.inputsJq[pos + i].value = chars[pos];
            }
        }
        this.updateInput();
    }

    /**
     * Focus the component by focusing on the correct input box.
     */
    focus() {
        this.inputsJq[0].trigger('focus');
    }

    /**
     * Enable the input
     */
    enable() {
        PrimeFaces.utils.enableInputWidget(this.inputsJq);
        PrimeFaces.utils.enableInputWidget(this.hinput);
        this.disabled = false;
    }

    /**
     * Disable the input
     */
    disable() {
        PrimeFaces.utils.disableInputWidget(this.inputsJq);
        PrimeFaces.utils.disableInputWidget(this.hinput);
        this.disabled = true;
    }

};