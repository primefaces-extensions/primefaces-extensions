/**
 * PrimeFaces Extensions Speedtest Widget.
 *
 * @author ssibitz ssibitz@me.com
 * @since 6.2
 */
PrimeFaces.widget.ExtSpeedtest = PrimeFaces.widget.BaseWidget.extend({

    /**
     * Initializes the widget.
     *
     * @param {object}
     *        cfg The widget configuration.
     */
    init: function(cfg) {
        this._super(cfg);
        this.id = cfg.id;
        this.cfg = cfg;

        this.render();
    },

    start: function() {
        // Start the speedtest
        var $this = this;
        $this.testPing();
        $this.testDown();
        $this.testUp();

        // Call listener function to 'save' results when it's set:
        var behavior = $this.cfg.behaviors ? $this.cfg.behaviors['speedtest'] : null;
        if (behavior) {
            var options = {
                params: [
                    { name: $this.id + '_PingTimeMS', value: $this.pingTimeMS },
                    { name: $this.id + '_JitterTimeMS', value: $this.jitterTimeMS },
                    { name: $this.id + '_SpeedMbpsDownload', value: $this.downSpeed },
                    { name: $this.id + '_SpeedMbpsUpload', value: $this.upSpeed }
                ]
            };
            behavior.call($this, options);
        }
    },

    render: function() {
        var $this = this;

        // Reset values
        $this.pingTimeMS = 0;
        $this.lastPingTimeMS = 0;
        $this.jitterTimeMS = 0;
        $this.downSpeed = 0;
        $this.upSpeed = 0;

        // Generate Gauge's
        $(document).ready(function () {
            $this.gaugeDown = $this.createGage($this.cfg.idDown, $this.cfg.captionDownload, 'Mbps', '#999999', $this.cfg.colorDownload);
            $this.gaugeUp = $this.createGage($this.cfg.idUp, $this.cfg.captionUpload, 'Mbps', '#999999', $this.cfg.colorUpload);
            $this.gaugePing = $this.createGage($this.cfg.idPing, $this.cfg.captionPing, 'ms', '#999999', $this.cfg.colorPing);
            $this.gaugeJitter = $this.createGage($this.cfg.idJitter, $this.cfg.captionJitter, 'ms', '#999999', $this.cfg.colorJitter);
        });
    },

    updateGauge: function(gauge, value, maxfactor) {
        var Max = (Math.round(value / maxfactor) + 1) * maxfactor;
        gauge.refresh(value, Max);
    },

    createGage: function(id, title, label, color1, color2) {
        return new JustGage({
            id: id,
            title: title,
            label: label,
            titleFontFamily: 'Comic Sans MS',
            valueFontFamily: 'Comic Sans MS',
            refreshAnimationTime: 300,
            value: 0,
            min: 0,
            max: 100,
            decimals: 2,
            formatNumber: true,
            humanFriendly: false,
            levelColors: [
                color1,
                color2
            ]
        });
    },

    genBytes: function(len) {
        var pC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        var ret = "";
        for (var i = 0; i < len; i++) {
            ret += pC.charAt(Math.floor(Math.random() * pC.length));
        }
        return ret;
    },

    startMS: function() {
        return performance.now();
    },

    diffMS: function(startTime) {
        var $this = this;
        return ($this.startMS() - startTime);
    },

    C2MBps: function(bytes) {
        return Math.round(100 * bytes * 8 / 1024 / 1024) / 100;
    },

    getFileName: function() {
        var $this = this;
        var ret = "";
        ret = ret + String($this.cfg.file);
        return ret;
    },

    singlePing: function(cnt, upLoadDatas) {
        var $this = this;
        var pMSC = 0;
        var start = $this.startMS();
        $.ajax({
            async: false,
            type: 'POST',
            url: PrimeFacesExt.getFacesResource('speedtest/dummy.html', PrimeFacesExt.RESOURCE_LIBRARY, PrimeFacesExt.VERSION) + '?id=' + start,
            data: upLoadDatas,
            success: function(msg) {
                pMSC = $this.diffMS(start);
                $this.pingTimeMS += pMSC;
            },
            complete: function(xhr, textStatus) {
                if (cnt > 1) {
                    $this.jitterTimeMS += Math.abs($this.lastPingTimeMS - pMSC);
                }
                $this.lastPingTimeMS = pMSC;
            }
        });
    },

    testPing: function() {
        var $this = this;
        // Do the ping test 10 times:
        for (var cnt = 1; cnt <= 10; cnt++) {
            $this.singlePing(cnt, $this.genBytes(32));
        }
        $this.pingTimeMS = Math.round(100 * $this.pingTimeMS / cnt) / 100;
        $this.jitterTimeMS = Math.round(100 * $this.jitterTimeMS / cnt) / 100;
        // Finished - Show result for ping, jitter:
        $this.updateGauge($this.gaugePing, $this.pingTimeMS, 10);
        $this.updateGauge($this.gaugeJitter, $this.jitterTimeMS, 10);
    },

    testDown: function() {
        var $this = this;
        var start = $this.startMS();
        $.ajax({
            async: false,
            cache: false,
            type: 'GET',
            url: $this.getFileName() + '?id=' + start,
            success: function(msg) {
                $this.downSpeed = $this.C2MBps(msg.length / ($this.diffMS(start) / 1000));
            },
            complete: function(xhr, textStatus) {
                $this.updateGauge($this.gaugeDown, $this.downSpeed, 50);
            }
        });
    },

    testUp: function() {
        var $this = this;
        var upLoadDatas = $this.genBytes(200000);
        var start = $this.startMS();
        $.ajax({
            async: false,
            type: 'POST',
            url: PrimeFacesExt.getFacesResource('speedtest/dummy.html', PrimeFacesExt.RESOURCE_LIBRARY, PrimeFacesExt.VERSION) + '?id=' + start,
            data: upLoadDatas,
            success: function(msg) {
                $this.upSpeed = $this.C2MBps(upLoadDatas.length / ($this.diffMS(start) / 1000));
            },
            complete: function(xhr, textStatus) {
                $this.updateGauge($this.gaugeUp, $this.upSpeed, 50);
            }
        });
    }
});
