// don't break aggregation
/**
 * PrimeFaces Extensions CodeScanner Widget.
 *
 * @author Jasper de Vries jepsar@gmail.com
 * @since 8.0.5
 */
PrimeFaces.widget.ExtCodeScanner = PrimeFaces.widget.BaseWidget.extend({

    init: function (cfg) {
        this._super(cfg);
        this.id = cfg.id;
        this.cfg = cfg;

        function getReader(type) {
            switch (type) {
                case 'bar':
                    return new ZXing.BrowserBarcodeReader();
                case 'qr':
                    return new ZXing.BrowserQRCodeReader();
                default:
                    return new ZXing.BrowserMultiFormatReader();
            }
        }

        function handleResult(result) {
            if ($this.hasBehavior('codeScanned')) {
                var ext = {
                    params: [{
                            name: $this.id + '_value',
                            value: result.text
                        }, {
                            name: $this.id + '_format',
                            value: result.format
                        }]
                };
                $this.callBehavior('codeScanned', ext);
            }

        }

        var $this = this;
        this.video = $('video', this.jq)[0];
        this.codeReader = getReader(cfg.type);
        this.codeReader.listVideoInputDevices()
                .then((videoInputDevices) => {
                    var deviceId = videoInputDevices[0].deviceId;
                    $this.codeReader.decodeFromVideoDevice(deviceId, $this.video, (result, err) => {
                        if (result) {
                            handleResult(result);
                        }
                        if (err && !(err instanceof ZXing.NotFoundException)) {
                            console.error(err);
                        }
                    })
                })
                .catch((err) => {
                    console.error(err)
                });
    },

    // @override
    refresh: function (cfg) {
        if (this.codeReader) {
            this.codeReader.reset();
        }
        this._super(cfg);
    },

    // @override
    destroy: function () {
        this._super();
        if (this.codeReader) {
            this.codeReader.reset();
        }
    }

});
