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
        this.id = cfg.id;
        this.cfg = cfg;
        this.target = $(this.cfg.target);

        // TODO

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
    waypoint:function () {
        this.target.waypoint(this.cfg);
    },

    /**
     * Recalculates all waypoint positions.
     */
    refresh:function () {
        $.waypoints('refresh');
    }
});