/**
 * PrimeFaces Extensions TimeAgo Widget.
 *
 * @author Jasper de Vries jepsar@gmail.com
 * @since 7.0.1
 */
PrimeFaces.widget.ExtTimeAgo = class extends PrimeFaces.widget.BaseWidget {

    /**
     * Initializes the widget.
     *
     * @param {object}
     *            cfg The widget configuration.
     */
    init(cfg) {
        super.init(cfg);
        this.id = cfg.id;
        this.cfg = cfg;
        this.selector = this.jqId + ' time';

        if (typeof this.cfg.locale !== 'undefined'
                && typeof PrimeFacesExt.locales.TimeAgo[this.cfg.locale] !== 'undefined') {
            $.timeago.settings.strings = PrimeFacesExt.locales.TimeAgo[this.cfg.locale];
        }
        this.timeAgo = $(this.selector).timeago();
    }

    // @override
    refresh(cfg) {
        $(this.selector).timeago('dispose');
        super.refresh(cfg);
    }

    // @override
    destroy() {
        super.destroy();
        $(this.selector).timeago('dispose');
    }

};

PrimeFacesExt.locales.TimeAgo['en'] = {
    prefixAgo: null,
    prefixFromNow: null,
    suffixAgo: "ago",
    suffixFromNow: "from now",
    inPast: 'any moment now',
    seconds: "less than a minute",
    minute: "about a minute",
    minutes: "%d minutes",
    hour: "about an hour",
    hours: "about %d hours",
    day: "a day",
    days: "%d days",
    month: "about a month",
    months: "%d months",
    year: "about a year",
    years: "%d years",
    wordSeparator: " ",
    numbers: []
};

PrimeFacesExt.locales.TimeAgo['en_US'] = PrimeFacesExt.locales.TimeAgo['en'];
PrimeFacesExt.locales.TimeAgo['en_UK'] = PrimeFacesExt.locales.TimeAgo['en'];
