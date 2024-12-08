/**
 * PrimeFaces Extensions FluidGrid Widget.
 * 
 * @author Oleg Varaksin
 */
PrimeFaces.widget.ExtFluidGrid = class extends PrimeFaces.widget.DeferredWidget {

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

        this.cfg.opts.itemSelector = ".pe-fluidgrid-item";
        this.cfg.opts.isInitLayout = false;

        if (this.cfg.opts.stamp) {
            this.cfg.opts.stamp = PrimeFaces.expressions.SearchExpressionFacade.resolveComponentsAsSelector(
                    this.jq, this.cfg.opts.stamp).get();
        }

        this.renderDeferred();
    }

    /**
     * Creates this widget with all initialization steps.
     */
    _render() {
        this.$container = $(PrimeFaces.escapeClientId(this.id));

        if (this.cfg.opts.hasImages) {
            // initialize plugin after all images have been loaded
            this.$container.imagesLoaded($.proxy(function() {
                this.$container.masonry(this.cfg.opts);

                // bind events
                this.bindEvents();

                // trigger layout manually
                this.$container.masonry();
            }, this));
        } else {
            this.$container.masonry(this.cfg.opts);

            // bind events
            this.bindEvents();

            // trigger layout manually
            this.$container.masonry();
        }

        // check if the fluid grid is within a layout pane / unit
        var layoutPane = this.$container.closest(".ui-layout-pane");
        if (layoutPane.length > 0) {
            layoutPane.on('layoutpaneonresize', $.proxy(function() {
                // re-layout the grid
                this.$container.masonry();
            }, this));
        }
    }

    /**
     * Binds events.
     */
    bindEvents() {
        if (this.getBehavior("layoutComplete")) {
            this.$container.masonry('off', 'layoutComplete');
            this.$container.masonry('on', 'layoutComplete', $.proxy(function() {
                var behavior = this.getBehavior("layoutComplete");
                var options = {
                    params : [ {
                        name : this.id + '_layoutComplete',
                        value : true
                    } ]
                };

                behavior.call(this, options);
            }, this));
        }
    }

    /**
     * Gets behavior callback by name or null.
     * 
     * @param name
     *        behavior name
     * @return {Function}
     */
    getBehavior(name) {
        return this.cfg.behaviors ? this.cfg.behaviors[name] : null;
    }

    addItems(elements) {
        this.$container.masonry('addItems', elements);
    }

    appended(elements) {
        this.$container.masonry('appended', elements);
    }

    prepended(elements) {
        this.$container.masonry('prepended', elements);
    }

    bindResize() {
        this.$container.masonry('bindResize');
    }

    unbindResize() {
        this.$container.masonry('unbindResize');
    }

    destroy() {
        this.$container.masonry('destroy');
    }

    getItemElements() {
        return this.$container.masonry('getItemElements');
    }

    hide() {
        this.$container.masonry('hide');
    }

    layout() {
        this.$container.masonry();
    }

    layoutItems(items, isStill) {
        this.$container.masonry('layoutItems', items, isStill);
    }

    reloadItems() {
        this.$container.masonry('reloadItems');
    }

    remove(elements) {
        this.$container.masonry('remove', elements);
    }

    reveal(items) {
        this.$container.masonry('reveal', items);
    }

    stamp(elements) {
        this.$container.masonry('stamp', elements);
    }

    unstamp(elements) {
        this.$container.masonry('unstamp', elements);
    }
};
