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
        this.inputsJq = $(this.jqId + ' > input');

        // pfs metadata
        // this.inputsJq.data(PrimeFaces.CLIENT_ID_DATA, this.id);

        // style disabled if necessary
        if (this.disabled) {
            PrimeFaces.utils.disableInputWidget(this.inputsJq);
        }

        // visual effects
        PrimeFaces.skinInput(this.inputsJq);

        this.bindEvents();
    },

    bindEvents: function () {
        var inputs = this.inputsJq.get();

        for (let i = 0; i < inputs.length; i++) {
            const input = inputs[i];

            input.addEventListener('input', function () {
                // handling normal input
                if (input.value.length == 1 && i + 1 < inputs.length) {
                    inputs[i + 1].focus();
                }

                // if a value is pasted, put each character to each of the next input
                if (input.value.length > 1) {
                    // sanitise input
                    // TODO

                    // split characters to array
                    const chars = input.value.split('');

                    for (let pos = 0; pos < chars.length; pos++) {
                        // if length exceeded the number of inputs, stop
                        if (pos + i >= inputs.length)
                            break;

                        // paste value
                        inputs[pos + i].value = chars[pos];
                    }

                    // focus the input next to the last pasted character
                    let focus_index = Math.min(inputs.length - 1, i + chars.length);
                    inputs[focus_index].focus();
                }
                updateInput();
            });

            input.addEventListener('keydown', function (e) {
                switch (e.keyCode) {
                    case 8: // backspace button
                        if (input.value === '' && i > 0) {
                            // shift next values towards the left
                            for (let pos = i; pos < inputs.length - 1; pos++) {
                                inputs[pos].value = inputs[pos + 1].value;
                            }

                            // clear previous box and focus on it
                            inputs[i - 1].value = '';
                            inputs[i - 1].focus();
                            updateInput();
                            return;
                        }
                        break;
                    case 46: // delete button
                        if (i < inputs.length - 1) {
                            // shift next values towards the left
                            for (let pos = i; pos < inputs.length - 1; pos++) {
                                inputs[pos].value = inputs[pos + 1].value;
                            }

                            // clear the last box
                            inputs[inputs.length - 1].value = '';
                            input.select();
                            e.preventDefault();
                            updateInput();
                            return;
                        }
                        break;
                    case 37: // left button
                        if (i > 0) {
                            e.preventDefault();
                            inputs[i - 1].focus();
                            inputs[i - 1].select();
                        }
                        return;
                    case 39: // right button
                        if (i + 1 < inputs.length) {
                            e.preventDefault();
                            inputs[i + 1].focus();
                            inputs[i + 1].select();
                        }
                        return;
                }
            });
        }
    },

    updateInput: function () {

    },

    /**
     * Focus the component by focusing on the correct input box.
     */
    focus: function () {
        this.inputsJq[0].focus();
    },

    /**
     * Enable the input
     */
    enable: function () {
        PrimeFaces.utils.enableInputWidget(this.inputsJq);
        this.disabled = false;
    },

    /**
     * Disable the input
     */
    disable: function () {
        PrimeFaces.utils.disableInputWidget(this.inputsJq);
        this.disabled = true;
    }

});
