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
                enabled: true,
                cookie: {
                    name: encodeURIComponent('pfext.layout.' + this.id)
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

        /*
         var eventCallbacks = {
         onopen: function(panename, pane, state, options) {
         if (options.paneposition) {
         _self.onopen(options.paneposition);
         }
         }
         , onclose: function(panename, pane, state, options) {
         if (options.paneposition) {
         _self.onclose(options.paneposition);
         }
         }
         , onresize: function(panename, pane, state, options) {
         if (options.paneposition) {
         _self.onresize(options.paneposition, state);
         }
         }        
         };*/

        /*
         var northOpt = $.extend({}, {
         onopen: eventCallbacks.onopen
         , onclose: eventCallbacks.onclose
         , onresize: eventCallbacks.onresize
         , resizeWithWindowDelay: 250
         , slidable: false
         , north__paneSelector: jqId + "-layout-outer-north"
         , center__paneSelector: jqId + "-layout-outer-center"
         }, cfg.northOptions);    

         var defaultLayoutSettings = {
         onopen: eventCallbacks.onopen
         , onclose: eventCallbacks.onclose
         , onresize: eventCallbacks.onresize
         , slidable: false
         , contentSelector: '.pe-layout-pane-content'
         , togglerTip_open: cfg.togglerTipClose
         , togglerTip_closed: cfg.togglerTipOpen
         , resizerTip: cfg.resizerTip
         , spacing_open: 0
         };*/

        /*
         this.onopen = function(paneposition) {
         var behavior = config.behaviors ? config.behaviors['open'] : null;
         if (behavior) {
         var ext = {
         params: [
         { name: clientId + '_pane', value: paneposition }
         ]
         };

         behavior.call(this, paneposition, ext);
         }

         if (stateHiddenField && stateHiddenField.length > 0) {
         stateHiddenField.val(getLayoutState());
         }        
         }

         this.onclose = function(paneposition) {
         var behavior = config.behaviors ? config.behaviors['close'] : null;
         if (behavior) {
         var ext = {
         params: [
         { name: clientId + '_pane', value: paneposition }
         ]
         };

         behavior.call(this, paneposition, ext);
         }

         if (stateHiddenField && stateHiddenField.length > 0) {
         stateHiddenField.val(getLayoutState());
         }        
         }

         this.onresize = function(paneposition, state) {
         if (!state.isClosed && !state.isHidden) {
         var behavior = config.behaviors ? config.behaviors['resize'] : null;
         if (behavior) {
         var ext = {
         params: [
         { name: clientId + '_pane', value: paneposition },
         { name: clientId + '_width', value: state.innerWidth },
         { name: clientId + '_height', value: state.innerHeight }
         ]
         };

         behavior.call(this, paneposition, ext);
         }

         if (stateHiddenField && stateHiddenField.length > 0) {
         stateHiddenField.val(getLayoutState());
         }            
         }
         }*/
    },

    createLayout:function () {
        // create layout
        var layoutObj = this.jqTarget.layout(this.cfg.options);

        if (this.cfg.serverState) {
            layoutObj.loadState($.parseJSON(this.cfg.state));
        }
    }
});
