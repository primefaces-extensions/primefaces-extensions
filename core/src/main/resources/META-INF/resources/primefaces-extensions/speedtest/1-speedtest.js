/**
 * PrimeFaces Extensions Speedtest Widget.
 *
 * @author ssibitz ssibitz@me.com
 * @since 6.2
 */
PrimeFaces.widget.ExtSpeedtest = PrimeFaces.widget.BaseWidget.extend({

    init: function(cfg) {
        this._super(cfg);
        this.id = cfg.id;
        this.cfg = cfg;

        this.render();
    },

    start: function() {
        // Start the speedtest
        let $this = this;
        $this.testPing();
        $this.testDown();
        $this.testUp();

        // Call listener function to 'save' results when it's set:
        let options = {
                params: [
                    { name: $this.id + '_PingTimeMS', value: $this.pingTimeMS },
                    { name: $this.id + '_JitterTimeMS', value: $this.jitterTimeMS },
                    { name: $this.id + '_SpeedMbpsDownload', value: $this.downSpeed },
                    { name: $this.id + '_SpeedMbpsUpload', value: $this.upSpeed }
                ]
            };
        $this.callBehavior('speedtest', options);
    },

    render: function() {
        let $this = this;

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

    updateGauge: function(gaugeChart, value, label, maxfactor) {
        // Calculate the new max value based on maxfactor and current value
        let max = (Math.round(value / maxfactor) + 1) * maxfactor;

        // Update the chart options
        gaugeChart.setOption({
            series: [{
                max: max,  // update max dynamically
                data: [{ value: value, name: label }]
            }]
        });
    },

    createGage: function(id, title, label, color1, color2) {
        // Get the DOM element where the gauge will be rendered
        let gaugeElement = document.getElementById(id);

        // Initialize ECharts instance on the specified element
        let gaugeChart = echarts.init(gaugeElement);

        // Configure the gauge options
        let options = {
            title: {
                text: title,
                left: 'center',
                textStyle: {
                    fontFamily: 'Comic Sans MS',
                    fontSize: 16,
                }
            },
            series: [
                {
                    name: label,
                    type: 'gauge',
                    min: 0,
                    max: 100,  // initial max
                    splitNumber: 10,
                    axisLine: {
                        lineStyle: {
                            color: [
                                [1, color2]
                            ],
                            width: 10
                        }
                    },
                    axisTick: {
                        lineStyle: {
                            color: color1,
                            width: 1
                        }
                    },
                    axisLabel: {
                        show: false  // Hides the numbers on the gauge axis
                    },
                    pointer: {
                        width: 5,
                        itemStyle: {
                            color: color1
                        }
                    },
                    title: {
                        fontFamily: 'Comic Sans MS',
                        fontSize: 14,
                        offsetCenter: [0, '60%']
                    },
                    detail: {
                        fontFamily: 'Comic Sans MS',
                        fontSize: 14,
                        formatter: function (value) {
                            return value + '';
                        },
                        valueAnimation: true
                    },
                    data: [{ value: 0, name: label }]
                }
            ]
        };

        // Set initial options on the chart instance
        gaugeChart.setOption(options);

        return gaugeChart; // return the chart instance for later updates
    },

    genBytes: function(len) {
        let pC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        let ret = "";
        for (let i = 0; i < len; i++) {
            ret += pC.charAt(Math.floor(Math.random() * pC.length));
        }
        return ret;
    },

    startMS: function() {
        return performance.now();
    },

    diffMS: function(startTime) {
        let $this = this;
        return ($this.startMS() - startTime);
    },

    C2MBps: function(bytes) {
        return Math.round(100 * bytes * 8 / 1024 / 1024) / 100;
    },

    getFileName: function() {
        let $this = this;
        let ret = "";
        ret = ret + String($this.cfg.file);
        return ret;
    },

    singlePing: function(cnt, upLoadDatas) {
        let $this = this;
        let pMSC = 0;
        let start = $this.startMS();
        $.ajax({
            async: false,
            type: 'POST',
            url: PrimeFaces.resources.getFacesResource('speedtest/dummy.html', PrimeFacesExt.RESOURCE_LIBRARY, PrimeFacesExt.VERSION) + '?id=' + start,
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
        let successfulPings = 0;
        let PING_COUNT = 10;
        let BYTE_SIZE = 32;

        // Initialize cumulative times to 0 before starting the tests
        this.pingTimeMS = 0;
        this.jitterTimeMS = 0;

        // Perform the ping test `PING_COUNT` times
        for (let i = 1; i <= PING_COUNT; i++) {
             this.singlePing(i, this.genBytes(BYTE_SIZE));
             successfulPings++;
        }

        // Calculate the average times
        this.pingTimeMS = successfulPings > 0 ? Math.round((100 * this.pingTimeMS) / successfulPings) / 100 : 0;
        this.jitterTimeMS = successfulPings > 0 ? Math.round((100 * this.jitterTimeMS) / successfulPings) / 100 : 0;

        // Display results by updating the gauges
        this.updateGauge(this.gaugePing, this.pingTimeMS, 'ms',10);
        this.updateGauge(this.gaugeJitter, this.jitterTimeMS, 'ms',10);
    },


    testDown: function() {
        let $this = this;
        let start = $this.startMS();
        $.ajax({
            async: false,
            cache: false,
            type: 'GET',
            url: $this.getFileName() + '?id=' + start,
            success: function(msg) {
                $this.downSpeed = $this.C2MBps(msg.length / ($this.diffMS(start) / 1000));
            },
            complete: function(xhr, textStatus) {
                $this.updateGauge($this.gaugeDown, $this.downSpeed, 'Mbps', 50);
            }
        });
    },

    testUp: function() {
        let $this = this;
        let upLoadDatas = $this.genBytes(200000);
        let start = $this.startMS();
        $.ajax({
            async: false,
            type: 'POST',
            url: PrimeFaces.resources.getFacesResource('speedtest/dummy.html', PrimeFacesExt.RESOURCE_LIBRARY, PrimeFacesExt.VERSION) + '?id=' + start,
            data: upLoadDatas,
            success: function(msg) {
                $this.upSpeed = $this.C2MBps(upLoadDatas.length / ($this.diffMS(start) / 1000));
            },
            complete: function(xhr, textStatus) {
                $this.updateGauge($this.gaugeUp, $this.upSpeed, 'Mbps',50);
            }
        });
    }
});