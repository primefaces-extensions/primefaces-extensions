/**
 * PrimeFaces Extensions Waypoint Widget.
 *
 * @author Oleg Varaksin
 */
PrimeFaces.widget.ExtWaypoint = class extends PrimeFaces.widget.BaseWidget {

    /**
     * Initializes the widget.
     *
     * @param {object}
     *        cfg The widget configuration.
     */
    init(cfg) {
        super.init(cfg);
        this.cfg = cfg;
        this.id = cfg.id;
        this.target = PrimeFaces.expressions.SearchExpressionFacade.resolveComponentsAsSelector(this.jq, this.cfg.target);

        // remove the target to prevent circular ref
        delete this.cfg.target;

        if (this.cfg.context) {
            this.cfg.context = PrimeFaces.expressions.SearchExpressionFacade.resolveComponentsAsSelector(this.jq, this.cfg.context);
        }

        this.destroy().create();
    }

    /**
     * Unbinds all event handlers and unregisters the waypoint(s) associated
     * with these elements.
     */
    destroy() {
        this.target.waypoint('destroy');
        return this;
    }

    /**
     * Temporarily disables the waypoint callback function from firing. The
     * waypoint can be re-enabled by calling enable.
     */
    disable() {
        this.target.waypoint('disable');
        return this;
    }

    /**
     * Re-enables a previously disabled waypoint.
     */
    enable() {
        this.target.waypoint('enable');
        return this;
    }

    /**
     * Creates the waypoint(s) again with the same handlers as for init. This
     * method can be called after .destroy().
     */
    create() {
        var _self = this;
        this.target.waypoint(function (direction) {
            _self.reached(direction, this);
        }, this.cfg);
    }

    /**
     * Recalculates all waypoint positions.
     */
    refresh() {
        $.waypoints('refresh');
    }

    reached(dir, way) {
        if (this.hasBehavior('reached')) {
            var options = {
                params: [{
                    name: this.id + '_direction',
                    value: dir
                }, {
                    name: this.id + '_waypointId',
                    value: $(way).attr('id')
                }],
                direction: dir,
                waypoint: way
            };
            this.callBehavior('reached', options);
        }
    }
};