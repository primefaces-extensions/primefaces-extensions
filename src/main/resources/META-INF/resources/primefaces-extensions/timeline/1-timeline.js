/**
 * PrimeFaces Extensions Timeline Widget.
 *
 * @author Oleg Varaksin, reimplemented since 0.7
 */
PrimeFacesExt.widget.Timeline = PrimeFaces.widget.BaseWidget.extend({
    /**
     * Initializes the widget.
     *
     * @param {Object} cfg widget configuration's object.
     */
    init: function (cfg) {
        this._super(cfg);
        this.cfg = cfg;
        this.id = cfg.id;

        this.jqId = PrimeFaces.escapeClientId(this.id);

        PrimeFacesExt.handleInitialize(this, this.createTimeline);
    },

    /**
     * Creates timeline widget with all initialization steps.
     */
    createTimeline: function () {
        //this.jq.addClass("ui-widget ui-widget-content ui-corner-all ui-timeline-container");

        // instantiate a timeline object
        this.instance = new links.Timeline(document.getElementById(this.id));

        // draw the timeline with created data and options
        this.instance.draw(this.cfg.data, this.cfg.opts);

        // bind events
        this.bindEvents();
    },

    /**
     * Binds timeline's events.
     */
    bindEvents: function () {
        if (this.cfg.opts.responsive) {
            var nsevent = "resize.timeline" + this.jqId;
            $(window).off(nsevent).on(nsevent, $.proxy(function () {
                this.instance.checkResize();
            }, this));
        }

        // "select" event
        var behavior = this.getBehavior("select");
        if (this.cfg.selectable && behavior) {
            links.events.addListener(this.instance, 'select', $.proxy(function () {
                var item = this.getSelectedItem();
                var ext = {
                    params: [
                        {name: this.id + '_eventId', value: item.id}
                    ]
                };

                behavior.call(this, null, ext);
            }, this));
        }

        // "add" event
        behavior = this.getBehavior("add");
        if (this.cfg.selectable && this.cfg.editable && behavior) {
            links.events.addListener(this.instance, 'add', $.proxy(function () {
                var item = this.getSelectedItem();
                var ext = {
                    params: [
                        {name: this.id + '_startDate', value: this.getUTC(item.start)},
                        {name: this.id + '_endDate', value: this.getUTC(item.end)},
                        {name: this.id + '_group', value: item.group}
                    ]
                };

                behavior.call(this, null, ext);
                
                // cancel adding the item because it should be added on the server-side and draw with response
                this.instance.cancelAdd();
            }, this));
        }
    },

    /**
     * Gets behavior callback by name or null.
     * 
     * @param name behavior name
     * @return {Function}
     */
    getBehavior: function (name) {
        return this.cfg.behaviors ? this.cfg.behaviors[name] : null;
    },

    /**
     * Gets index of the currently selected item or -1.
     * 
     * @return {Number}
     */
    getSelectedIndex: function () {
        var selection = this.instance.getSelection();
        if (selection.length) {
            if (selection[0].row != undefined) {
                return selection[0].row;
            }
        }

        return -1;
    },

    /**
     * Gets currently selected item or null.
     * 
     * @return {Object} JSON object
     */
    getSelectedItem: function () {
        var index = this.getSelectedIndex();
        if (index != -1) {
            this.instance.getItem(index);
        }

        return null;
    },

    /**
     * Selects an event by index. The visible range will be moved,
     * so that the selected event is placed in the middle.
     * 
     * @param index
     */
    setSelection: function(index) {
        this.instance.setSelection([{
            'row': index
        }]);    
    },

    /**
     * Converts local date in Browser in UTC.     
     * 
     * @param local local date
     * @return {Number}
     */
    getUTC: function(local) {
        var utc = new Date(Date.UTC(
            local.getFullYear(),
            local.getMonth(),
            local.getDate(),
            local.getHours(),
            local.getMinutes(),
            local.getSeconds(),
            local.getMilliseconds()
        ));
        
        return utc.getTime();
    },

    /**
     * Gets instance of the Timeline object.
     * 
     * @return {Object}
     */
    getInstance: function () {
        return this.instance;
    }
});