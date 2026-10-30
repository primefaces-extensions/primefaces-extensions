/**
 * PrimeFaces 17 compatibility shim for the jQuery UI interaction plugins.
 *
 * PrimeFaces 17 removed jQuery UI and replaced its interactions with PrimeFaces.interactions. This shim restores
 * $.fn.draggable, $.fn.droppable, $.fn.sortable and $.fn.resizable (and $.browser) on top of
 * PrimeFaces.interactions, so existing code keeps working while it is migrated. Every plugin logs a deprecation
 * warning (development project stage only) the first time it is used.
 *
 * It is part of jquery-plugins.js, after the jQuery UI subset, and is also used by Layout and BlockUI. Plugins defined
 * by a full jQuery UI that is loaded anyway are not replaced.
 *
 * Not covered, see the PrimeFaces 17 migration guide: $.fn.position(options) beyond the jQuery UI subset, jQuery UI CSS
 * classes (now prefixed with "p-", e.g. p-sortable-helper) and event.originalEvent, which is now the native
 * PointerEvent.
 *
 * @see https://github.com/primefaces-extensions/primefaces-extensions/issues/2868
 */
(function ($) {
    "use strict";

    if (!$ || !window.PrimeFaces || !PrimeFaces.interactions) {
        return;
    }

    const interactions = PrimeFaces.interactions;
    const warned = {};

    /**
     * Logs the deprecation warning of an API once.
     * @param {string} api the deprecated API, e.g. $.fn.sortable
     * @param {string} replacement the API to use instead, e.g. PrimeFaces.interactions.sortable
     */
    function deprecated(api, replacement) {
        if (!warned[api]) {
            warned[api] = true;
            PrimeFaces.warn(api + " is deprecated, use " + replacement
                + " instead. It is provided by the PrimeFaces Extensions jQuery UI shim.");
        }
    }

    /**
     * The items of a sortable as an array of attribute values, like toArray of jQuery UI.
     * @param {object} sortable the PrimeFaces sortable
     * @param {object} [options] the options attribute (default id) and item
     * @returns {string[]} the values
     */
    function toArray(sortable, options) {
        const o = options || {};
        return sortable.getItems().map(function () {
            return $(o.item || this).attr(o.attribute || "id") || "";
        }).get();
    }

    /**
     * The items of a sortable as a query string, like serialize of jQuery UI.
     * @param {object} sortable the PrimeFaces sortable
     * @param {object} [options] the options attribute (default id), expression, key and item
     * @returns {string} the serialized items, e.g. item[]=1&item[]=2
     */
    function serialize(sortable, options) {
        const o = options || {};
        const str = [];
        sortable.getItems().each(function () {
            const res = ($(o.item || this).attr(o.attribute || "id") || "").match(o.expression || (/(.+)[\-=_](.+)/));
            if (res) {
                str.push((o.key || res[1] + "[]") + "=" + (o.key && o.expression ? res[1] : res[2]));
            }
        });
        if (!str.length && o.key) {
            str.push(o.key + "=");
        }
        return str.join("&");
    }

    /** Methods of the jQuery UI plugins that the PrimeFaces interactions don't have. */
    const extraMethods = {
        sortable: {
            toArray: toArray,
            serialize: serialize,
            refreshPositions: function (sortable) {
                sortable.refresh();
            }
        }
    };

    /**
     * Creates a jQuery plugin with the bridge semantics of jQuery UI: called with options it creates the
     * interaction or updates its options, called with a method name it calls the method.
     * @param {string} type the type of the interaction, e.g. sortable
     * @returns {function} the plugin
     */
    function plugin(type) {
        const dataKey = "ui-" + type;

        return function (options) {
            deprecated("$.fn." + type, "PrimeFaces.interactions." + type);

            if (typeof options !== "string") {
                interactions[type](this, options);
                // jQuery UI stored the instance under ui-<type>, e.g. $(el).data('ui-sortable')
                return this.each(function () {
                    $.data(this, dataKey, interactions.instance(this, type));
                });
            }

            const method = options;
            const args = Array.prototype.slice.call(arguments, 1);
            let result = this;

            this.each(function () {
                const instance = interactions.instance(this, type);
                if (!instance) {
                    $.error("cannot call methods on " + type + " prior to initialization; attempted to call method '" + method + "'");
                }

                let value;
                const extra = extraMethods[type] && extraMethods[type][method];
                if (method === "instance") {
                    value = instance;
                } else if (method === "widget") {
                    value = instance.element;
                } else if (method === "destroy") {
                    instance.destroy();
                    $.removeData(this, dataKey);
                } else if (extra) {
                    value = extra.apply(null, [instance].concat(args));
                } else if (method.charAt(0) !== "_" && typeof instance[method] === "function") {
                    value = instance[method].apply(instance, args);
                } else {
                    $.error("no such method '" + method + "' for " + type + " widget instance");
                }

                // like jQuery UI, a getter returns the value of the first element
                if (value !== instance.element && value !== undefined) {
                    result = value;
                    return false;
                }
            });

            return result;
        };
    }

    // don't replace jQuery UI if it was loaded anyway
    ["draggable", "droppable", "sortable", "resizable"].forEach(function (type) {
        if (!$.fn[type]) {
            $.fn[type] = plugin(type);
        }
    });

    // $.browser of the jQuery Browser plugin, now PrimeFaces.env.browser
    if (!$.browser && PrimeFaces.env && PrimeFaces.env.browser) {
        Object.defineProperty($, "browser", {
            configurable: true,
            get: function () {
                deprecated("$.browser", "PrimeFaces.env.browser");
                return PrimeFaces.env.browser;
            }
        });
    }

})(window.jQuery);
