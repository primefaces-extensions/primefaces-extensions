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

        if (this.target === 0) {
            var message = 'Badge target not found!';
            alert(message);
            console.error(message);
        } else {
            // add badge content
            this.setContent(this.cfg.content);

            // add color
            this.setColor(this.cfg.color);

            // apply the CSS class
            this.setPosition(this.cfg.position);
        }
    },

    /**
     * Updates the content of the badge.
     * 
     * @param {String} the badge content
     */
    setContent : function(content) {
        this.target.attr("data-badge-content", PrimeFaces.escapeHTML(content));
    },
    
    /**
     * Updates the color of the badge.
     * 
     * @param {String} the badge color
     */
    setColor : function(color) {
        this.target.css("--badge-color", color);
    },
    
    /**
     * Updates the position of the badge.
     * 
     * @param {String} the badge position
     */
    setPosition : function(position) {
        this.target.removeClass("ui-badge-" + this.cfg.position).addClass("ui-badge-" + position);
        this.cfg.position = position;
    },

    /**
     * Show the badge.
     */
    show : function() {
        this.target.addClass("ui-badge-" + this.cfg.position);
    },

    /**
     * Hide the badge.
     */
    hide : function() {
        this.target.removeClass("ui-badge-" + this.cfg.position);
    }
});


