/**
 * PrimeFaces Extensions Layout Widget.
 *
 * @author Oleg Varaksin
 */
PrimeFaces.widget.ExtLayout = PrimeFaces.widget.DeferredWidget.extend({
    /**
     * Initializes the widget.
     *
     * @param {object}
     *        cfg The widget configuration.
     */
    init: function (cfg) {
        this._super(cfg);
        this.cfg = cfg;
        this.id = cfg.id;
        this.jq = $(cfg.forTarget);
        this.stateHiddenField = null;

        this.jqId = PrimeFaces.escapeClientId(this.id);

        if (this.cfg.full) {                                                 //full
            var dialog = $(this.cfg.centerSelector).parents('.ui-dialog-content');
            if (dialog.length == 1) {                                         // full + dialog
                this.jq = dialog;
            } else {
                this.jq = $('body');
            }
        } else if (this.cfg.parent) {                                        //nested
            this.jq = $(PrimeFaces.escapeClientId(this.cfg.parent));
        } else {                                                            //element
            this.jq = $(this.jqId);
        }

        if (cfg.clientState) {
            this.cfg.options.stateManagement = {
                enabled: true,
                autoSave: true, // Save state when page exits?
                autoLoad: true, // load state when page loads?
                storeLocation: "localStorage", //or sessionStorage, globalStorage, openDatabase, userdata, google gears, flash, cookie
                cookie: {
                    name: encodeURIComponent('Layout-' + this.id)
                }
            };
        } else if (cfg.serverState) {
            this.stateHiddenField = $(this.jqId + "_state");
        }

        this.renderDeferred();
    },

    _render: function () {
        // create layout
        this.layout = this.jq.layout(this.cfg.options);

        if (this.cfg.serverState) {
            this.layout.loadState(this.cfg.state);
        }

        if (!this.cfg.full) {
            this.jq.css('overflow', 'visible');
        }

        // bind "open", "close" and "resize" events
        this.bindEvents(this.jq);
    },

    bindEvents: function (parent) {
        var $this = this;

        // bind events
        parent.find(".ui-layout-pane").on("layoutpaneonopen", function () {
            var behavior = $this.cfg.behaviors ? $this.cfg.behaviors['open'] : null;
            if (behavior) {
                var combinedPosition = $(this).data('combinedposition');
                var options = {
                    params: [{
                        name: $this.id + '_pane',
                        value: combinedPosition
                    }]
                };

                behavior.call($this, options);
            }

            if ($this.cfg.serverState) {
                $this.stateHiddenField.val($this.layout.encodeJSON($this.layout.readState()));
            }
        }).on("layoutpaneonclose", function () {
            var behavior = $this.cfg.behaviors ? $this.cfg.behaviors['close'] : null;
            if (behavior) {
                var combinedPosition = $(this).data('combinedposition');
                var options = {
                    params: [{
                        name: $this.id + '_pane',
                        value: combinedPosition
                    }]
                };

                behavior.call($this, options);
            }

            if ($this.cfg.serverState) {
                $this.stateHiddenField.val($this.layout.encodeJSON($this.layout.readState()));
            }
        }).on("layoutpaneonresize", function () {
            var layoutPane = $(this).data("layoutPane");

            if (!layoutPane.state.isClosed && !layoutPane.state.isHidden) {
                var behavior = $this.cfg.behaviors ? $this.cfg.behaviors['resize'] : null;
                if (behavior) {
                    var combinedPosition = $(this).data('combinedposition');
                    var options = {
                        params: [{
                            name: $this.id + '_pane',
                            value: combinedPosition
                        }, {
                            name: $this.id + '_width',
                            value: layoutPane.state.innerWidth
                        }, {
                            name: $this.id + '_height',
                            value: layoutPane.state.innerHeight
                        }]
                    };

                    behavior.call($this, options);
                }

                if ($this.cfg.serverState) {
                    $this.stateHiddenField.val($this.layout.encodeJSON($this.layout.readState()));
                }
            }
        });
    },

    toggle: function (pane) {
        this.jq.find(".ui-layout-pane").each(function () {
            var combinedPosition = $(this).data('combinedposition');
            if (combinedPosition && combinedPosition === pane) {
                $(this).trigger("layoutpanetoggle");
                return false;
            }
        });
    },

    close: function (pane) {
        var panes = this.jq.find(".ui-layout-pane");
        var length = panes.length;
        for (var i = 0; i < length; i++) {
            var combinedPosition = $(panes[i]).data('combinedposition');
            if (combinedPosition && combinedPosition === pane) {
                $(panes[i]).trigger("layoutpaneclose");
                break;
            }
        }
    },

    open: function (pane) {
        var panes = this.jq.find(".ui-layout-pane");
        var length = panes.length;
        for (var i = 0; i < length; i++) {
            var combinedPosition = $(panes[i]).data('combinedposition');
            if (combinedPosition && combinedPosition === pane) {
                $(panes[i]).trigger("layoutpaneopen");
                break;
            }
        }
    },

    sizePane: function (pane, size) {
        var panes = this.jq.find(".ui-layout-pane");
        var length = panes.length;
        for (var i = 0; i < length; i++) {
            var combinedPosition = $(panes[i]).data('combinedposition');
            if (combinedPosition && combinedPosition === pane) {
                $(panes[i]).trigger("layoutpanesize", [size]);
                break;
            }
        }
    },

    sizeContent: function (pane) {
        var panes = this.jq.find(".ui-layout-pane");
        var length = panes.length;
        for (var i = 0; i < length; i++) {
            var combinedPosition = $(panes[i]).data('combinedposition');
            if (combinedPosition && combinedPosition === pane) {
                $(panes[i]).trigger("layoutpanesizecontent");
                break;
            }
        }
    },

    /**
     * Automatically resize the whole layout.
     */
    resizeAll: function () {
        if (this.layout) {
            this.layout.resizeAll();
        }
    }

});
