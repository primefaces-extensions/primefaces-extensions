/**
 * PrimeFaces Extensions Speedtest Widget.
 *
 * @author @author ssibitz ssibitz@me.com
 * @since 6.1
 */
PrimeFaces.widget.ExtSpeedtest = PrimeFaces.widget.BaseWidget.extend({
    /**
     * Initializes the widget.
     *
     * @param {object}
     *        cfg The widget configuration.
     */
    init : function(cfg) {
        this._super(cfg);
        this.id = cfg.id;
        this.cfg = cfg;
        this._render();
    },
    start : function() {
        // Start the speedtest
        var $this = this;
        $this._TestPing();
        $this._TestDown();
        $this._TestUp();
        // Call listener function to 'save' results when it's set:
        var behavior = $this.cfg.behaviors ? $this.cfg.behaviors['speedtest'] : null;
        if (behavior) {
            var options = {
                params : [ {
                    name : $this.id + '_PingTimeMS',
                    value : $this.pingTimeMS
                }, {
                    name : $this.id + '_JitterTimeMS',
                    value : $this.jitterTimeMS
                }, {
                    name : $this.id + '_SpeedMbpsDownload',
                    value : $this.downSpeed
                }, {
                    name : $this.id + '_SpeedMbpsUpload',
                    value : $this.upSpeed
                } ]
            };
            behavior.call($this, options);
        }
    },
    _render : function() {
        var $this = this;
        // Reset values
        $this.pingTimeMS = 0;
        $this.lastPingTimeMS = 0;
        $this.jitterTimeMS = 0;
        $this.downSpeed = 0;
        $this.upSpeed = 0;
        // Generate Gauge's
        $this.gaugeDown = $this._createGage($this.cfg.IdDown, $this.cfg.CaptionDownload, 'Mbps', '#999999', $this.cfg.ColorDownload);
        $this.gaugeUp = $this._createGage($this.cfg.IdUp, $this.cfg.CaptionUpload, 'Mbps', '#999999', $this.cfg.ColorUpload);
        $this.gaugePing = $this._createGage($this.cfg.IdPing, $this.cfg.CaptionPing, 'ms', '#999999', $this.cfg.ColorPing);
        $this.gaugeJitter = $this._createGage($this.cfg.IdJitter, $this.cfg.CaptionJitter, 'ms', '#999999', $this.cfg.ColorJitter);
    },
    _updateGauge : function(gauge, value, maxfactor) {
        var Max = (Math.round(value / maxfactor) + 1) * maxfactor;
        gauge.refresh(value, Max);
    },
    _createGage : function(id, title, label, color1, color2) {
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
    _genBytes : function(len) {
        var pC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        var ret = "";
        for (var i = 0; i < len; i++) {
            ret += pC.charAt(Math.floor(Math.random() * pC.length));
        }
        return ret;
    },
    _StartMS : function() {
        return performance.now();
    },
    _DiffMS : function(startTime) {
        var $this = this;
        return ($this._StartMS() - startTime);
    },
    _C2MBps : function(bytes) {
        return Math.round(100 * bytes * 8 / 1024 / 1024) / 100;
    },
    _GetFileName : function() {
        var $this = this;
        var ret = "";
        ret = ret + String($this.cfg.File);
        return ret;
    },
    _SinglePing : function(cnt, upLoadDatas) {
        var $this = this;
        var pMSC = 0;
        var start = $this._StartMS();
        $.ajax({
            async: false,
            type: 'POST',
            url: PrimeFaces.getFacesResource('speedtest/dummy.post', PrimeFacesExt.RESOURCE_LIBRARY, PrimeFacesExt.VERSION) + '?id=' + start,
            data: upLoadDatas,
            success: function (msg) {
                pMSC = $this._DiffMS(start);
                $this.pingTimeMS += pMSC;
            },
            complete: function (xhr, textStatus) {
                if (cnt > 1) {
                    $this.jitterTimeMS += Math.abs($this.lastPingTimeMS - pMSC);
                }
                $this.lastPingTimeMS = pMSC;
            }
        });
    },
    _TestPing : function() {
        var $this = this;
        // Do the ping test 10 times:
        for (var cnt = 1; cnt <= 10; cnt++) {
            $this._SinglePing(cnt, $this._genBytes(32));
        }
        $this.pingTimeMS = Math.round(100 * $this.pingTimeMS / cnt) / 100;
        $this.jitterTimeMS = Math.round(100 * $this.jitterTimeMS / cnt) / 100;
        // Finished - Show result for ping, jitter:
        $this._updateGauge($this.gaugePing, $this.pingTimeMS, 10);
        $this._updateGauge($this.gaugeJitter, $this.jitterTimeMS, 10);
    },
    _TestDown : function () {
        var $this = this;
        var start = $this._StartMS();
        $.ajax({
            async: false,
            cache: false,
            type: 'GET',
            url: $this._GetFileName() + '?id=' + start,
            success: function (msg) {
                $this.downSpeed = $this._C2MBps(msg.length / ($this._DiffMS(start) / 1000));
            },
            complete: function (xhr, textStatus) {
                $this._updateGauge($this.gaugeDown, $this.downSpeed, 50);
            }
        });
    },
    _TestUp : function () {
        var $this = this;
        var upLoadDatas = $this._genBytes(200000);
        var start = $this._StartMS();
        $.ajax({
            async: false,
            type: 'POST',
            url: PrimeFaces.getFacesResource('speedtest/dummy.post', PrimeFacesExt.RESOURCE_LIBRARY, PrimeFacesExt.VERSION) + '?id=' + start,
            data: upLoadDatas,
            success: function (msg) {
                $this.upSpeed = $this._C2MBps(upLoadDatas.length / ($this._DiffMS(start) / 1000));
            },
            complete: function (xhr, textStatus) {
                $this._updateGauge($this.gaugeUp, $this.upSpeed, 50);
            }
        });
    }
});
