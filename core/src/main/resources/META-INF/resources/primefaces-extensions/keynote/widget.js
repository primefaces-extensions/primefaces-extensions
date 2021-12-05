PrimeFaces.widget.ExtKeynote = PrimeFaces.widget.BaseWidget.extend({

    init: function (cfg) {

        this._super(cfg);
        this.id = cfg.id;
        this.cfg = cfg;

        this.options = {
            autoSlide: cfg.autoSlide || 0,
            center: cfg.center || true,
            controls: cfg.controls || true,
            disableLayout: cfg.disableLayout || false,
            embedded: cfg.embedded || false,
            loop: cfg.loop || false,
            navigationMode: cfg.navigationMode || "default",
            progress: cfg.progress || true,
            showNotes: cfg.showNotes || false,
            touch: cfg.touch || true,
            transition: cfg.transition || "slide",
            transitionSpeed: cfg.transitionSpeed || "default",
            backgroundTransition: cfg.backgroundTransition || "fade",

            width: cfg.width || 960,
            height: cfg.height || 700,
            margin: cfg.margin || 0.04,
            minScale: cfg.minScale || 0.2,
            maxScale: cfg.maxScale || 2.0
        };

        let deck = new Reveal(document.querySelector(this.jqId), this.options);
        deck.initialize();

    }


});