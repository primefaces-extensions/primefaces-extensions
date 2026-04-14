/**
 * PrimeFaces Extensions Clipboard Widget.
 * 
 * @author Melloware mellowaredev@gmail.com
 * @since 6.1
 */
PrimeFaces.widget.ExtClipboard = PrimeFaces.widget.BaseWidget.extend({

    /**
     * Initializes the widget.
     * 
     * @param {object}
     *        cfg The widget configuration.
     */
    init : function(cfg) {
        this._super(cfg);
        this.id = cfg.id;
        this.cfg = cfg;
        this.trigger = PrimeFaces.expressions.SearchExpressionFacade.resolveComponentsAsSelector(this.cfg.trigger);

        this._applyClipboard(this.trigger, cfg);
    },

    /**
     * Applies the Clipboard to the given jQuery selector object. Delete
     * previous Clipboard to support ajax updates and create a new one.
     * 
     * @param {object}
     *        _trigger A jQuery selector object.
     * @param {object}
     *        cfg The widget configuration.
     * @private
     */
    _applyClipboard : function(_trigger, cfg) {
        this.trigger = _trigger;

        // destroy the old Clipboard
        this.destroy();

        // start building the options to configure the clipboard
        var opts = {
            action : function(trigger) {
                return cfg.action;
            }
        };

        // either use target input or hard coded text attribute
        if (cfg.target) {
            $.extend(true, opts, {
                target : function(trigger) {
                    var input = PrimeFaces.expressions.SearchExpressionFacade.resolveComponentsAsSelector(cfg.target);
                    var selector = input[0].id;
                    selector = selector.replace(new RegExp(':', 'g'), '\\:');
                    return document.querySelector('#' + selector);
                }
            });
        } else if (cfg.text) {
            $.extend(true, opts, {
                text : function(trigger) {
                    return cfg.text;
                }
            });
        }

        // create the Clipboard
        this.clipboard = new ClipboardJS(this.trigger[0], opts);

        // bind "success", "error" events
        this._bindEvents();
    },

    /**
     * Binds all events to p:ajax events and javascript callbacks
     * 
     * @private
     */
    _bindEvents : function() {
        var $this = this;

        this.clipboard.on('success', function(e) {
            var options = {
                params : [ {
                    name : $this.id + '_action',
                    value : e.action
                }, {
                    name : $this.id + '_text',
                    value : e.text
                }, {
                    name : $this.id + '_trigger',
                    value : e.trigger
                } ]
            };
            $this.callBehavior('success', options);

            if ($this.cfg.onSuccess) {
                $this.cfg.onSuccess.call(e);
            }
        });

        this.clipboard.on('error', function(e) {
            var options = {
                params : [ {
                    name : $this.id + '_action',
                    value : e.action
                }, {
                    name : $this.id + '_trigger',
                    value : e.trigger
                } ]
            };
            $this.callBehavior('error', options);

            if ($this.cfg.onError) {
                $this.cfg.onError.call(e);
            }
        });
    },
    
    destroy : function() {
        if (this.clipboard) {
            this.clipboard.destroy();
        }
    }
});
