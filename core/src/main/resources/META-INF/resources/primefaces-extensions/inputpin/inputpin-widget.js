/**
 * PrimeFaces Extensions InputPin Widget.
 *
 * @since 14.0
 */
PrimeFaces.widget.ExtInputPin = PrimeFaces.widget.BaseWidget.extend({

    /**
     * Initializes the widget.
     *
     * @param {object}
     *            cfg The widget configuration.
     */
    init: function (cfg) {
        this._super(cfg);
        this.id = cfg.id;
        this.cfg = cfg;
        this.disabled = cfg.disabled;

        // JQuery inputs
        this.inputsJq = $(this.jqId + ' > .ui-inputfield');
        this.hinput = $(this.jqId + '_hidden');

        // pfs metadata
        this.hinput.data(PrimeFaces.CLIENT_ID_DATA, this.id);

        // style disabled if necessary
        if (this.disabled) {
            PrimeFaces.utils.disableInputWidget(this.inputsJq);
            this.hinput.attr("disabled", "disabled");
        }

        // visual effects
        PrimeFaces.skinInput(this.inputsJq);

        this.bindEvents();

        //client Behaviors
        if (this.cfg.behaviors) {
            PrimeFaces.attachBehaviors(this.inputsJq, this.cfg.behaviors);
        }
    },

    bindEvents: function () {
        var $this = this;
        var inputsJq = this.inputsJq;

        // get the current attached events if using CSP
        var events = this.inputsJq[0] ? $._data(this.inputsJq[0], "events") : null;

        // use DOM if non-CSP and JQ event if CSP
        var originalOninput = this.inputsJq.prop('oninput');
        if (!originalOninput && events && events.input) {
            originalOninput = events.input[0].handler;
        }
        var originalOnkeydown = this.inputsJq.prop('onkeydown');
        if (!originalOnkeydown && events && events.keydown) {
            originalOnkeydown = events.keydown[0].handler;
        }

        for (let i = 0; i < inputsJq.length; i++) {

            let inputJq = $(inputsJq[i]);

            inputJq.prop('oninput', null).off('input').on('input', function (e) {
                // handling normal input
                if (inputJq.val().length === 1 && i + 1 < inputsJq.length) {
                    inputsJq[i + 1].focus();
                }

                // if a value is pasted, put each character to each of the next input
                if (inputJq.val().length > 1) {
                    // sanitise input
                    // TODO

                    // split characters to array
                    const chars = inputJq.val().split('');

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
                $this.updateInput();

                if (originalOninput && originalOninput.call(this, e) === false) {
                    return false;
                }
            });

            inputJq.prop('onkeydown', null).off('keydown').on('keydown', function (e) {
                switch (e.keyCode) {
                    case 8: // backspace button
                        if (inputJq.val() === '' && i > 0) {
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
                    case 46: // delete button
                        if (i < inputsJq.length - 1) {
                            // shift next values towards the left
                            for (let pos = i; pos < inputsJq.length - 1; pos++) {
                                inputsJq[pos].value = inputsJq[pos + 1].value;
                            }

                            // clear the last box
                            inputsJq[inputsJq.length - 1].value = '';
                            inputsJq[i].select();
                            e.preventDefault();
                            inputsJq[inputsJq.length - 1].dispatchEvent(new Event("input"));
                            return;
                        }
                        break;
                    case 37: // left button
                        if (i > 0) {
                            e.preventDefault();
                            inputsJq[i - 1].focus();
                            inputsJq[i - 1].select();
                        }
                        return;
                    case 39: // right button
                        if (i + 1 < inputsJq.length) {
                            e.preventDefault();
                            inputsJq[i + 1].focus();
                            inputsJq[i + 1].select();
                        }
                        return;
                }

                if (originalOnkeydown && originalOnkeydown.call(this, e) === false) {
                    return false;
                }
            });
        }
    },

    /**
     * 
     * @returns {string} The original value of the hidden input.
     */
    updateInput: function () {
        var oldValue = this.hinput.val();
        var newValue = '';
        for (var i = 0; i < this.inputsJq.length; i++) {
            newValue += this.inputsJq[i].value;
        }
        this.hinput.val(newValue);
        return oldValue;
    },

    /**
     * Focus the component by focusing on the correct input box.
     */
    focus: function () {
        this.inputsJq[0].trigger('focus');
    },

    /**
     * Enable the input
     */
    enable: function () {
        PrimeFaces.utils.enableInputWidget(this.inputsJq);
        PrimeFaces.utils.enableInputWidget(this.hinput);
        this.disabled = false;
    },

    /**
     * Disable the input
     */
    disable: function () {
        PrimeFaces.utils.disableInputWidget(this.inputsJq);
        PrimeFaces.utils.disableInputWidget(this.inputIso2Jq);
        this.disabled = true;
    }

});
