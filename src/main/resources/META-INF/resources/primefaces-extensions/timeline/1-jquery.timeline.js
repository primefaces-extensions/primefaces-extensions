/**
 * @file jquery.timeline.js
 *
 * @brief
 * This is JQuery plugin wrapper for CHAP Link's Timeline Library
 * @license
 * Copyright 2012 Nilesh N. Mali (nileshmali86@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author  Nilesh N. Mali
 * @date    2012-07-24
 * @version 0.0.1
 */
; // For safety if anybody has missed ';'
(function ($) {
    var timeline = undefined;
    /* Public Methods */
    var methods = {
        /**
         *
         * @param options
         * @return {*}
         */
        init:function (data, options) {
            /* Override default settings with user provided options */
            var settings = $.extend({}, $.fn.timeline.defaults, options);

            return this.each(function () {
                $(this).addClass("ui-widget ui-widget-content ui-corner-all ui-timeline-container");
                timeline = new links.Timeline($(this).get(0));
                timeline.draw(data, options);
                $(window).resize(function () {
                    timeline.checkResize();
                });
            });
        },
        checkResize:function () {
            return this.each(function () {
                timeline.checkResize();
            });
        },
        addEvent:function (type, callback) {
            var _self = this;
            return this.each(function () {
                if (type == 'select') {
                    links.events.addListener(timeline, 'select', function () {
                        var selection = timeline.getSelection();
                        if (selection.length) {
                            if (selection[0].row != undefined) {
                                callback(timeline.getItem(selection[0].row));
                            }
                        }
                    });
                }
            });
        }
    };

    /**
     * Timeline Initialization
     * @param method
     * @return {Object}
     */
    $.fn.timeline = function (method) {
        if (methods[method]) {
            return methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
        } else if (typeof method === 'object' || !method) {
            return methods.init.apply(this, arguments);
        } else {
            $.error('Method ' + method + ' does not exist');
        }
    };

    /* Default Options */
    $.fn.timeline.defaults = {
        'width':"100%",
        'height':"auto",
        'minHeight':0, // minimal height in pixels
        'autoHeight':true,

        'eventMargin':10, // minimal margin between events
        'eventMarginAxis':20, // minimal margin between events and the axis
        'dragAreaWidth':10, // pixels

        'min':undefined,
        'max':undefined,
        'intervalMin':10, // milliseconds
        'intervalMax':1000 * 60 * 60 * 24 * 365 * 10000, // milliseconds

        'moveable':true,
        'zoomable':true,
        'selectable':true,
        'editable':false,
        'snapEvents':true,
        'groupChangeable':true,

        'showCurrentTime':true, // show a red bar displaying the current time
        'showCustomTime':false, // show a blue, draggable bar displaying a custom time
        'showMajorLabels':true,
        'showNavigation':false,
        'showButtonAdd':true,
        'groupsOnRight':false,
        'axisOnTop':false,
        'stackEvents':true,
        'animate':true,
        'animateZoom':true,
        'style':'box'
    };
})(jQuery);