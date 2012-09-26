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
    init: function (cfg) {
        this._super(cfg);
        this.cfg = cfg;
        this.id = cfg.id;

        var jqId = PrimeFaces.escapeClientId(this.id);

        this.jqTarget = $(cfg.forTarget);
        this.stateHiddenField = null;

        if (cfg.clientState) {
            this.cfg.options.stateManagement = {
                enabled:true,
                cookie:{
                    name:encodeURIComponent('pfext.layout.' + this.id)
                }
            };
        } else if (cfg.serverState) {
            this.stateHiddenField = $(jqId + "_state");
        }

        var _self = this;

        if (this.jqTarget.is(':visible')) {
            this.createLayout();
        } else {
            var hiddenParent = this.jqTarget.parents('.ui-hidden-container:first');
            var hiddenParentWidget = hiddenParent.data('widget');

            if (hiddenParentWidget) {
                hiddenParentWidget.addOnshowHandler(function () {
                    return _self.createLayout();
                });
            }
        }
    },

    createLayout: function () {
        // create layout
        var layoutObj = this.jqTarget.layout(this.cfg.options);

        if (this.cfg.serverState) {
            layoutObj.loadState(this.cfg.state);
        }

        var _self = this;
        var panesSelector = ".ui-layout-center,.ui-layout-north,.ui-layout-south,.ui-layout-west,.ui-layout-east";

        // bind events
        this.jqTarget.find(panesSelector).bind("layoutpaneonopen", function () {
            var behavior = _self.cfg.behaviors ? _self.cfg.behaviors['open'] : null;
            if (behavior) {
                var combinedPosition = $(this).data('combinedPosition');
                var ext = {
                    params:[
                        {name:_self.id + '_pane', value:combinedPosition}
                    ]
                };

                behavior.call(_self, combinedPosition, ext);
            }

            if (_self.cfg.serverState) {
                _self.stateHiddenField.val(layoutObj.encodeJSON(layoutObj.readState()));
            }
        }).bind("layoutpaneonclose", function () {
            var behavior = _self.cfg.behaviors ? _self.cfg.behaviors['close'] : null;
            if (behavior) {
                var combinedPosition = $(this).data('combinedPosition');
                var ext = {
                    params:[
                        {name:_self.id + '_pane', value:combinedPosition}
                    ]
                };
    
                behavior.call(_self, combinedPosition, ext);
            }
    
            if (_self.cfg.serverState) {
                _self.stateHiddenField.val(layoutObj.encodeJSON(layoutObj.readState()));
            }
        }).bind("layoutpaneonresize", function () {
            var layoutPane = $(this).data("layoutPane");

            if (!layoutPane.state.isClosed && !layoutPane.state.isHidden) {
                var behavior = _self.cfg.behaviors ? _self.cfg.behaviors['resize'] : null;
                if (behavior) {
                    var combinedPosition = $(this).data('combinedPosition');
                    var ext = {
                        params:[
                            {name:_self.id + '_pane', value:combinedPosition},
                            {name:_self.id + '_width', value:layoutPane.state.innerWidth},
                            {name:_self.id + '_height', value:layoutPane.state.innerHeight}
                        ]
                    };
        
                    behavior.call(_self, combinedPosition, ext);
                }
        
                if (_self.cfg.serverState) {
                    _self.stateHiddenField.val(layoutObj.encodeJSON(layoutObj.readState()));
                }
            }
        });
    }
});
