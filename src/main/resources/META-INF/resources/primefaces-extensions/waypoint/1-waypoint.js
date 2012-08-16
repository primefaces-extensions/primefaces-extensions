/**
 * PrimeFaces Extensions Waypoint Widget.
 *
 * @author Oleg Varaksin
 */
PrimeFacesExt.widget.Waypoint = PrimeFaces.widget.BaseWidget.extend({

    /**
     * Initializes the widget.
     *
     * @param {object} cfg The widget configuration.
     */
    init:function (cfg) {
        this.cfg = cfg;
        this.id = cfg.id;
        this.target = $(this.cfg.target);

        delete this.cfg.target;

        PrimeFacesExt.removeWidgetScript(this.id);
    },

    /**
     * Unbinds all event handlers and unregisters the waypoint(s).
     */
    destroy:function () {
        this.target.waypoint('destroy');
        return this;
    },

    /**
     * Unregisters the waypoint(s) and wipes any custom options, but leaves the waypoint's event bound.
     * Calling .register() again in the future would reregister the waypoint(s) and the old handlers would continue to work.
     */
    remove:function () {
        if (this.isMozilla()) {
            // mozilla bug https://github.com/imakewebthings/jquery-waypoints/issues/80
            this.winscrolltop = $(window).scrollTop();
        }
        
        this.target.waypoint('remove');
    },

    /**
     * Registers the waypoint(s) again with all old handlers. This method can be called after .remove().
     */
    register:function () {
        if (this.isMozilla() && this.winscrolltop) {
            $(window).scrollTop(this.winscrolltop);
            delete this.winscrolltop;
        }
        
        this.target.waypoint(this.cfg);
    },

    /**
     * Creates the waypoint(s) again with the same handlers as for init. This method can be called after .destory().
     */
    create:function () {
        var _self = this;
        this.target.waypoint(function (event, direction) {
            _self.reached(event, direction, this);
        }, this.cfg);    
    },

    /**
     * Recalculates all waypoint positions.
     */
    refresh:function () {
        $.waypoints('refresh');
    },

    reached:function (event, dir, way) {
        var behavior = this.cfg.behaviors ? this.cfg.behaviors["reached"] : null;
        if (behavior) {
            var ext = {
                params:[
                    {name:this.id + '_direction', value:dir},
                    {name:this.id + '_waypointId', value:$(way).attr('id')}
                ],
                direction:dir,
                waypoint:way
            };

            behavior.call(this, event, ext);
        }

        event.stopPropagation();
    },
    
    // https://github.com/jquery/jquery-browser
    isMozilla:function() {
        var ua = (navigator.userAgent || "").toLowerCase();
        return ("mozilla" === (ua.indexOf("compatible") < 0 && /(mozilla)(?:.*? rv:([\w.]+))?/.exec(ua) || [])[1]);
    }
});