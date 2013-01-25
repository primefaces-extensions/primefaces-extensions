/**
 * PrimeFaces Extensions Layout Widget.
 *
 * @author Oleg Varaksin
 */
PrimeFacesExt.widget.Layout = PrimeFaces.widget.BaseWidget.extend({
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
        
        var $this = this;

        if (this.jqTarget.is(':visible')) {
            this.createLayout();
        } else {
            var hiddenParent = this.jqTarget.parents('.ui-hidden-container:first');
            var hiddenParentWidget = hiddenParent.data('widget');

            if (hiddenParentWidget) {
                hiddenParentWidget.addOnshowHandler(function () {
                    return $this.createLayout();
                });
            }
        }
    },

    createLayout:function () {
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
            .bind("layoutpaneonopen",function () {
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
            }).bind("layoutpaneonclose",function () {
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
            }).bind("layoutpaneonresize", function () {
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
        this.jqTarget.find(".ui-layout-pane").
            each(function() {
                var combinedPosition = $(this).data('combinedposition');
                if (combinedPosition && combinedPosition === pane) {
                    $(this).trigger("layoutpaneclose");
                    return false;
                }
            });
    },

    open:function (pane) {
        this.jqTarget.find(".ui-layout-pane").
            each(function() {
                var combinedPosition = $(this).data('combinedposition');
                if (combinedPosition && combinedPosition === pane) {
                    $(this).trigger("layoutpaneopen");
                    return false;
                }
            });
    },

    sizePane:function (pane, size) {
        this.jqTarget.find(".ui-layout-pane").
            each(function() {
                var combinedPosition = $(this).data('combinedposition');
                if (combinedPosition && combinedPosition === pane) {
                    $(this).trigger("layoutpanesize", [size]);
                    return false;
                }
            });
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
