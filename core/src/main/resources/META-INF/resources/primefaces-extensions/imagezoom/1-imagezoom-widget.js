/**
 * PrimeFaces Extensions ImageZoom Widget.
 *
 * @author Melloware
 * @since 11..3
 */
PrimeFaces.widget.ImageZoom = class extends PrimeFaces.widget.BaseWidget {

    init(cfg) {
        super.init(cfg);

        // default background to theme background if not user defined
        if (!this.cfg.background) {
            this.cfg.background = 'var(--surface-a)';
        }

        var selector = PrimeFaces.escapeClientId(this.cfg.target);
        var targetWidget = PrimeFaces.getWidgetById(this.cfg.target);
        if (targetWidget && targetWidget instanceof PrimeFaces.widget.Galleria) {
            selector = selector + " li[class='ui-galleria-item'] > img";
        }

        this.zoom = mediumZoom(selector, this.cfg);
    }

    /**
     * @override
     * @inheritdoc
     * @param {PrimeFaces.PartialWidgetCfg<TCfg>} cfg
     */
    refresh(cfg) {
        this._cleanup();
        super.refresh(cfg);
    }

    /**
     * @override
     * @inheritdoc
     */
    destroy() {
        super.destroy();
        this._cleanup();
    }

    /**
     * Clean up this widget and remove elements from DOM.
     * @private
     */
    _cleanup() {
        this.zoom.detach();// detach all images
        this.zoom = null;
    }

    /**
     * Opens the zoom when closed / dismisses the zoom when opened, and returns a promise resolving with the zoom.
     */
    toggle() {
        this.zoom.toggle();
    }

    /**
     * Opens the zoom and returns a promise resolving with the zoom.
     * Emits an event open on animation start and opened when completed.
     */
    show() {
        this.zoom.open();
    }

    /**
     * Closes the zoom and returns a promise resolving with the zoom.
     * Emits an event close on animation start and closed when completed.
     */
    hide() {
        this.zoom.close();
    }

};