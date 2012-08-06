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
        var _self = this;

        // create waypoint(s)
        this.target.waypoint('destroy').waypoint(function(event, direction) {
            _self.scroll(event, direction);
        }, this.cfg);

        PrimeFacesExt.removeWidgetScript(this.id);
    },

    /**
     * Unbinds all event handlers and unregisters this waypoint.
     */
    destroy:function () {
        this.target.waypoint('destroy');
    },

    /**
     * Unregisters this waypoint and wipes any custom options, but leaves the waypoint event bound.
     * Calling .waypoint() again in the future would reregister the waypoint and the old handlers would continue to work.
     */
    remove:function () {
        this.target.waypoint('remove');
    },

    /**
     * Registers this waypoint again with all old handlers. This method can be called after .remove().
     */
    register:function () {
        this.target.waypoint(this.cfg);
    },

    /**
     * Recalculates all waypoint positions.
     */
    refresh:function () {
        $.waypoints('refresh');
    },

    scroll:function (event, direction) {
        var behavior = this.cfg.behaviors ? this.cfg.behaviors["scroll"] : null;
        if (behavior) {
            var ext = {
                params:[
                    {name:this.id + '_direction', value:direction}
                ],
                direction:direction,
                target:this.target,
                cfg:this.cfg
            };

            behavior.call(this, event, ext);
        }

        event.stopPropagation();
    }
});