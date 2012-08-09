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
     * Unregisters the waypoint(s) and wipes any custom options, but leaves the waypoint(s) event bound.
     * Calling .waypoint() again in the future would reregister the waypoint(s) and the old handlers would continue to work.
     */
    remove:function () {
        this.target.waypoint('remove');
    },

    /**
     * Registers the waypoint(s) again with all old handlers. This method can be called after .remove().
     */
    register:function () {
        this.target.waypoint(this.cfg);
    },

    /**
     * Creates the waypoint(s) again with the same handlers as for init. This method can be called after .destory().
     */
    create:function () {
        var _self = this;
        this.target.waypoint(function (event, direction) {
            _self.scroll(event, direction, $(this));
        }, this.cfg);    
    },

    /**
     * Recalculates all waypoint positions.
     */
    refresh:function () {
        $.waypoints('refresh');
    },

    scroll:function (event, direction, jqThis) {
        var behavior = this.cfg.behaviors ? this.cfg.behaviors["scroll"] : null;
        if (behavior) {
            var ext = {
                params:[
                    {name:this.id + '_direction', value:direction}
                ],
                direction:direction,
                jqThis:jqThis,
                target:this.target,
                cfg:this.cfg
            };

            behavior.call(this, event, ext);
        }

        event.stopPropagation();
    }
});