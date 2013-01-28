/**
 * PrimeFaces Extensions Timeline Widget.
 *
 * @author Nilesh Namdeo Mali
 */
PrimeFacesExt.widget.Timeline = PrimeFaces.widget.BaseWidget.extend({

    /**
     * Initializes the widget.
     *
     * @param {object} cfg The widget configuration.
     */
    init: function (cfg) {
        this._super(cfg);
        this.jq = $(PrimeFaces.escapeClientId(this.id));
        this.jq.width(cfg.width);
        this.jq.height(cfg.height);

        this.timelineOptions = {
            'width': "100%",
            'height': cfg.height,

            'minHeight':0, // minimal height in pixels
            'autoHeight':true,

            'eventMargin':10, // minimal margin between events
            'eventMarginAxis':20, // minimal margin between events and the axis
            'dragAreaWidth':10, // pixels

            'min':undefined,
            'max':undefined,
            'intervalMin':10, // milliseconds
            'intervalMax':1000 * 60 * 60 * 24 * 365 * 10000, // milliseconds

            'moveable':true,
            'zoomable':true,
            'selectable':false,
            'editable':false,
            'snapEvents':true,
            'groupChangeable':true,

            'showCurrentTime':true, // show a red bar displaying the current time
            'showCustomTime':false, // show a blue, draggable bar displaying a custom time
            'showMajorLabels':true,
            'showNavigation':cfg.showNavigation,
            'showButtonAdd':true,
            'groupsOnRight':false,
            'axisOnTop':(cfg.axisPosition == "top") ? true : false,
            'stackEvents':true,
            'animate':true,
            'animateZoom':true,
            'style':cfg.eventStyle
        };

        // Check selectable behaviour
        if (this.cfg.behaviors) {
            var eventSelectBehavior = this.cfg.behaviors['eventSelect'];
            if (eventSelectBehavior) {
                this.timelineOptions.selectable = true;
            }
        }

        PrimeFacesExt.handleInitialize(this, this.initialize);
    },

    initialize: function () {
        this.jq.addClass("ui-widget ui-widget-content ui-corner-all ui-timeline-container");
        this.instance = new links.Timeline(this.jq[0]);
        this.instance.draw(this.cfg.dataSource, this.timelineOptions);

        $(window).resize($.proxy(function () {
            this.instance.checkResize();
        },this));

        // Attach behaviours
        if (this.timelineOptions.selectable) {
            links.events.addListener(this.instance, 'select', $.proxy(function () {
                var selection = this.instance.getSelection();
                if (selection.length) {
                    if (selection[0].row != undefined) {
                        var item = this.instance.getItem(selection[0].row);
                        this.instance.unselectItem();
                        var eventSelectBehavior = this.cfg.behaviors['eventSelect'];
                        if (eventSelectBehavior) {
                            var ext = {
                                params: [
                                    {name: this.id + '_selectedTimelineId', value: item.timelineId},
                                    {name: this.id + '_selectedEventId', value: item.id}
                                ]
                            };
                            eventSelectBehavior.call(this, null, ext);
                        }
                    }
                }
            }, this));
        }
    },

    checkResize: function () {
        this.instance.checkResize();
    }
});
