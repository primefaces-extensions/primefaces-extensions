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
            slideNumber: cfg.slideNumber || "false",
            touch: cfg.touch || true,
            transition: cfg.transition || "slide",
            transitionSpeed: cfg.transitionSpeed || "default",
            backgroundTransition: cfg.backgroundTransition || "fade",

            width: cfg.width || 960,
            height: cfg.height || 700,
            margin: cfg.margin || 0.04,
            minScale: cfg.minScale || 0.2,
            maxScale: cfg.maxScale || 2.0,

            plugins: [RevealMarkdown]
        };

        if (this.options.slideNumber === "false") {
            this.options.slideNumber = false;
        }

        this.keynote = new Reveal(document.querySelector(this.jqId), this.options);

        this.bindEvents();

        this.keynote.initialize();
    },

    bindEvents: function () {
        var $this = this;

        this.keynote.on('slidechanged', event => {
            $this.slideChanged(event);
        });

        this.keynote.on('slidetransitionend', event => {
            $this.slideTransitionEnd(event);
        });
    },

    slideChanged: function (event) {
        var $this = this;

        if (this.hasBehavior('slideChanged')) {
            var options = {
                params: [
                    {name: $this.id + '_slideChanged', value: true},
                    {name: $this.id + '_lastSlide', value: $this.keynote.isLastSlide()}
                ]
            };
            this.callBehavior('slideChanged', options);
        }
    },

    slideTransitionEnd: function (event) {
        var $this = this;

        if (this.hasBehavior('slideTransitionEnd')) {
            var options = {
                params: [
                    {name: $this.id + '_slideTransitionEnd', value: true},
                    {name: $this.id + '_lastSlide', value: $this.keynote.isLastSlide()}
                ]
            };
            this.callBehavior('slideTransitionEnd', options);
        }
    }

});