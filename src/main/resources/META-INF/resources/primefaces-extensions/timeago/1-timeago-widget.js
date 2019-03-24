/**
 * PrimeFaces Extensions TimeAgo Widget.
 *
 * @author Jasper de Vries jepsar@gmail.com
 * @since 7.0.1
 */
PrimeFaces.widget.ExtTimeAgo = PrimeFaces.widget.BaseWidget.extend({

    /**
     * Initializes the widget.
     *
     * @param {object}
     *            cfg The widget configuration.
     */
    init : function (cfg) {
        this._super(cfg);
        this.id = cfg.id;
        this.cfg = cfg;
        this.selector = this.jqId + ' time';

        var $this = this;
        if (typeof this.cfg.locale !== 'undefined') {
            var languageJsURI = PrimeFaces.resources.getFacesResource('/timeago/i18n/jquery.timeago.' + this.cfg.locale + '.js',
                    PrimeFacesExt.RESOURCE_LIBRARY, PrimeFacesExt.VERSION);
            PrimeFacesExt.getScript(languageJsURI, $.proxy(function (data, textStatus) {
                $this.timeAgo = $($this.selector).timeago();
            }, this));
        } else {
            this.timeAgo = $(this.selector).timeago();
        }
    },

    // @override
    refresh : function (cfg) {
        $(this.selector).timeago('dispose');
        this._super(cfg);
    },

    // @override
    destroy : function () {
        this._super();
        $(this.selector).timeago('dispose');
    }

});
