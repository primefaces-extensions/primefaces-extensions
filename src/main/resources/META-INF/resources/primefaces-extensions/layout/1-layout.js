/**
 * PrimeFaces Extensions Layout Widget.
 *
 * @author Oleg Varaksin
 */
PrimeFacesExt.widget.Layout = PrimeFaces.widget.DeferredWidget.extend({
    /**
     * Initializes the widget.
     *
     * @param {object} cfg The widget configuration.
     */
    init:function (cfg) {
        this._super(cfg);
        this.cfg = cfg;
        this.id = cfg.id;

        var jqId = PrimeFaces.escapeClientId(this.id);
        
        this.jqTarget = $(cfg.forTarget);
        this.stateHiddenField = null;

        if (cfg.clientState) {
            this.cfg.options.stateManagement = {
                enabled:true,
                autoSave:true,
                cookie:{
                    name:encodeURIComponent('pfext.layout.' + this.id)
                }
            };
        } else if (cfg.serverState) {
            this.stateHiddenField = $(jqId + "_state");
        }

        if (this.jqTarget.is(':visible')) {
            this._render();
        } else {
            var hiddenParent = this.jqTarget.closest('.ui-hidden-container');
            var hiddenParentWidget = hiddenParent.data('widget');

            if (hiddenParentWidget) {
                var $this = this;
                hiddenParentWidget.addOnshowHandler(function () {
                    return $this._render();
                });
            }
        }
    },

    _render:function () {
        // create layout
        this.layout = this.jqTarget.layout(this.cfg.options);

        if (this.cfg.serverState) {
            this.layout.loadState(this.cfg.state);
        }

        // bind "open", "close" and "resize" events
        this.bindEvents(this.jqTarget);
    },
    
    bindEvents:function(parent) {
        var $this = this;

        // bind events
        parent.find(".ui-layout-pane")
            .on("layoutpaneonopen",function () {
                var behavior = $this.cfg.behaviors ? $this.cfg.behaviors['open'] : null;
                if (behavior) {
                    var combinedPosition = $(this).data('combinedposition');
                    var ext = {
                        params:[
                            {name:$this.id + '_pane', value:combinedPosition}
                        ]
                    };
    
                    behavior.call($this, combinedPosition, ext);
                }
    
                if ($this.cfg.serverState) {
                    $this.stateHiddenField.val($this.layout.encodeJSON($this.layout.readState()));
                }
            }).on("layoutpaneonclose",function () {
                var behavior = $this.cfg.behaviors ? $this.cfg.behaviors['close'] : null;
                if (behavior) {
                    var combinedPosition = $(this).data('combinedposition');
                    var ext = {
                        params:[
                            {name:$this.id + '_pane', value:combinedPosition}
                        ]
                    };
    
                    behavior.call($this, combinedPosition, ext);
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
                        var ext = {
                            params:[
                                {name:$this.id + '_pane', value:combinedPosition},
                                {name:$this.id + '_width', value:layoutPane.state.innerWidth},
                                {name:$this.id + '_height', value:layoutPane.state.innerHeight}
                            ]
                        };
    
                        behavior.call($this, combinedPosition, ext);
                    }
    
                    if ($this.cfg.serverState) {
                        $this.stateHiddenField.val($this.layout.encodeJSON($this.layout.readState()));
                    }
                }
            });        
    },

    toggle:function (pane) {
        this.jqTarget.find(".ui-layout-pane").
            each(function() {
                var combinedPosition = $(this).data('combinedposition');
                if (combinedPosition && combinedPosition === pane) {
                    $(this).trigger("layoutpanetoggle");
                    return false;
                }
            });
    },

    close:function (pane) {
        var panes = this.jqTarget.find(".ui-layout-pane");
        var length = panes.length;
        for (var i=0; i < length; i++) {
            var combinedPosition = $(panes[i]).data('combinedposition');
            if (combinedPosition && combinedPosition === pane) {
                $(panes[i]).trigger("layoutpaneclose");
                break;
            }            
        }
    },

    open:function (pane) {
        var panes = this.jqTarget.find(".ui-layout-pane");
        var length = panes.length;
        for (var i=0; i < length; i++) {
            var combinedPosition = $(panes[i]).data('combinedposition');
            if (combinedPosition && combinedPosition === pane) {
                $(panes[i]).trigger("layoutpaneopen");
                break;
            }            
        }
    },

    sizePane:function (pane, size) {
        var panes = this.jqTarget.find(".ui-layout-pane");
        var length = panes.length;
        for (var i=0; i < length; i++) {
            var combinedPosition = $(panes[i]).data('combinedposition');
            if (combinedPosition && combinedPosition === pane) {
                $(panes[i]).trigger("layoutpanesize", [size]);
                break;
            }            
        }
    },
    
    sizeContent:function (pane) {
        var panes = this.jqTarget.find(".ui-layout-pane");
        var length = panes.length;
        for (var i=0; i < length; i++) {
            var combinedPosition = $(panes[i]).data('combinedposition');
            if (combinedPosition && combinedPosition === pane) {
                $(panes[i]).trigger("layoutpanesizecontent");
                break;
            }            
        }
    }    
    
    /*
    update:function (pane, options) {
        var $this = this;
        
        this.jqTarget.find(".ui-layout-pane").
            each(function() {
                var $this = $(this);
                var combinedPosition = $this.data('combinedposition');
                if (combinedPosition && combinedPosition === pane) {
                    
                    // update child options
                    var $layoutContainer = $this.closest(".ui-layout-container");
                    if ($layoutContainer.length) {
                        var innerLayout = $layoutContainer.data("layout");
                        
                        innerLayout.destroy();
                        $layoutContainer.layout(options != null ? options : {});
                        
                        // bind "open", "close" and "resize" events
                        $this.bindEvents($this);                        
                    }                    
                    
                    return false;
                }
            });
    }*/   
});
