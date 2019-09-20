/**
 * PrimeFaces Extensions Badge Widget.
 * 
 * @author Melloware info@melloware.com
 * @since 7.1
 */
PrimeFaces.widget.ExtBadge = PrimeFaces.widget.BaseWidget.extend({

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
        this.target = PrimeFaces.expressions.SearchExpressionFacade.resolveComponentsAsSelector(this.cfg.target);

        var button = this.target;
        if (this.target.is('.ui-button')) {
            button = this.target;
        } else {
            button = $(':button:enabled:first', this.target);
        }

        if (button.length === 0) {
            var message = 'Badge must use for="target" or be nested inside a button!';
            alert(message);
            console.error(message);
        } else {
            this._applyBadge(button, cfg);
        }
    },

    /**
     * Applies the badge to the given jQuery selector object. 
     * 
     * @param {object}
     *        button A jQuery selector object.
     * @param {object}
     *        cfg The widget configuration.
     * @private
     */
    _applyBadge : function(button, cfg) {
        this.button = button;

        // add badge content
        this.setContent(this.cfg.content);
        
        // add color
        this.setColor(this.cfg.color);
        
        // apply the CSS class
        this.setPosition(this.cfg.position);
    },

    /**
     * Updates the content of the badge.
     * 
     * @param {String} the badge content
     */
    setContent : function(content) {
        this.button.attr("data-badge-content", PrimeFaces.escapeHTML(content));
    },
    
    /**
     * Updates the color of the badge.
     * 
     * @param {String} the badge color
     */
    setColor : function(color) {
        this.button.css("--badge-color", color);
    },
    
    /**
     * Updates the position of the badge.
     * 
     * @param {String} the badge position
     */
    setPosition : function(position) {
        this.button.removeClass("ui-badge-" + this.cfg.position).addClass("ui-badge-" + position);
        this.cfg.position = position;
    },

    /**
     * Show the badge.
     */
    show : function() {
        this.button.addClass("ui-badge-" + this.cfg.position);
    },

    /**
     * Hide the badge.
     */
    hide : function() {
        this.button.removeClass("ui-badge-" + this.cfg.position);
    }
});


