/*
 * imgAreaSelect jQuery plugin
 * version 1.0.0-rc.1
 *
 * Copyright (c) 2008-2013 Michal Wojciechowski (odyniec.net)
 *
 * Dual licensed under the MIT (http://opensource.org/licenses/MIT)
 * and GPL (http://opensource.org/licenses/GPL-2.0) licenses.
 *
 * http://odyniec.net/projects/imgareaselect/
 *
 */

(function($) {

/*
 * Math functions will be used extensively, so it's convenient to make a few
 * shortcuts
 */    
var abs = Math.abs,
    max = Math.max,
    min = Math.min,
    round = Math.round;

/**
 * Create a new HTML div element
 * 
 * @return A jQuery object representing the new element
 */
function div() {
    return $('<div/>');
}

/**
 * imgAreaSelect initialization
 * 
 * @param img
 *            A HTML image element to attach the plugin to
 * @param options
 *            An options object
 */
$.imgAreaSelect = function (img, options) {
    var 
        /* jQuery object representing the image */ 
        $img = $(img),
        
        /* Has the image finished loading? */
        imgLoaded,
        
        /* Plugin elements */
        
        /* Container box */
        $box = div(),
        /* Selection area */
        $area = div(),
        /* Border (four divs) */
        $border = div().add(div()).add(div()).add(div()),
        /* Outer area */
        $outer = div(),
        /* Handles (empty by default, initialized in setOptions()) */
        $handles = $([]),
        
        /* Image position (relative to viewport) */
        left, top,
        
        /* Image offset (as returned by .offset()) */
        imgOfs = { left: 0, top: 0 },
        
        /* Image dimensions (as returned by .width() and .height()) */
        imgWidth, imgHeight,
        
        /*
         * jQuery object representing the parent element that the plugin
         * elements are appended to
         */
        $parent,
        
        /* Parent element offset (as returned by .offset()) */
        parOfs = { left: 0, top: 0 },
        
        /* Base z-index for plugin elements */
        zIndex = 0,
                
        /* Plugin elements position */
        position = 'absolute',
        
        /* X/Y coordinates of the starting point for move/resize operations */ 
        startX, startY,

        /*
         * Distance between the mouse cursor (or touch point) and selection area
         * edges (when resizing)
         */
        edgeX, edgeY,
        
        /* Horizontal and vertical scaling factors */
        scaleX, scaleY,

        /* Current resize mode ("nw", "se", etc.) */
        resize,

        /* Selection area constraints */
        minWidth, minHeight, maxWidth, maxHeight,

        /* Aspect ratio to maintain (floating point number) */
        aspectRatio,

        /* Are the plugin elements currently displayed? */
        shown,

        /* Current selection (relative to parent element) */
        x1, y1, x2, y2,

        /* Current selection (relative to scaled image) */
        selection = { x1: 0, y1: 0, x2: 0, y2: 0, width: 0, height: 0 },

        /* User agent */
        ua = navigator.userAgent,
        
        /* Is the user performing a touch action? */
        touch,

        /* Various helper variables used throughout the code */ 
        $p, d, i, o, w, h, adjusted;

    /*
     * Translate selection coordinates (relative to scaled image) to viewport
     * coordinates (relative to parent element)
     */
    
    /**
     * Translate selection X to viewport X
     * 
     * @param x
     *            Selection X
     * @return Viewport X
     */
    function viewX(x) {
        return x + imgOfs.left - parOfs.left;
    }

    /**
     * Translate selection Y to viewport Y
     * 
     * @param y
     *            Selection Y
     * @return Viewport Y
     */
    function viewY(y) {
        return y + imgOfs.top - parOfs.top;
    }

    /*
     * Translate viewport coordinates to selection coordinates
     */
    
    /**
     * Translate viewport X to selection X
     * 
     * @param x
     *            Viewport X
     * @return Selection X
     */
    function selX(x) {
        return x - imgOfs.left + parOfs.left;
    }

    /**
     * Translate viewport Y to selection Y
     * 
     * @param y
     *            Viewport Y
     * @return Selection Y
     */
    function selY(y) {
        return y - imgOfs.top + parOfs.top;
    }
    
    /*
     * Translate event coordinates (relative to document) to viewport
     * coordinates
     */
    
    /**
     * Get event X and translate it to viewport X
     * 
     * @param event
     *            The event object
     * @return Viewport X
     */
    function evX(event) {
        var coords = touchCoords(event) || event, x;

        if (x = parseInt(coords.pageX))
            return x - parOfs.left;
    }

    /**
     * Get event Y and translate it to viewport Y
     * 
     * @param event
     *            The event object
     * @return Viewport Y
     */
    function evY(event) {
        var coords = touchCoords(event) || event, y;

        if (y = parseInt(coords.pageY))
            return y - parOfs.top;
    }
    
    /**
     * Get the first touch object in an event
     *
     * @param event
     *            The event object
     * @return The first touch object found in the event object, or false if
     *         none are found
     */
    function touchCoords(event) {
        var oev = event.originalEvent || {};
        
        return oev.touches && oev.touches.length ? oev.touches[0] : false;
    }

    /**
     * Get the current selection
     * 
     * @param noScale
     *            If set to <code>true</code>, scaling is not applied to the
     *            returned selection
     * @return Selection object
     */
    function getSelection(noScale) {
        var sx = noScale || scaleX, sy = noScale || scaleY;
        
        return { x1: round(selection.x1 * sx),
            y1: round(selection.y1 * sy),
            x2: round(selection.x2 * sx) - 1,
            y2: round(selection.y2 * sy) - 1,
            width: round(selection.x2 * sx) - round(selection.x1 * sx),
            height: round(selection.y2 * sy) - round(selection.y1 * sy) };
    }
    
    /**
     * Set the current selection
     * 
     * @param x1
     *            X coordinate of the upper left corner of the selection area
     * @param y1
     *            Y coordinate of the upper left corner of the selection area
     * @param x2
     *            X coordinate of the lower right corner of the selection area
     * @param y2
     *            Y coordinate of the lower right corner of the selection area
     * @param noScale
     *            If set to <code>true</code>, scaling is not applied to the
     *            new selection
     */
    function setSelection(x1, y1, x2, y2, noScale) {
        var sx = noScale || scaleX, sy = noScale || scaleY;
        
        selection = {
            x1: round(x1 / sx || 0),
            y1: round(y1 / sy || 0),
            x2: round(++x2 / sx || 0),
            y2: round(++y2 / sy || 0)
        };
        
        selection.width = selection.x2 - selection.x1;
        selection.height = selection.y2 - selection.y1;
    }

    /**
     * Recalculate image and parent offsets
     */
    function adjust() {
        /*
         * Do not adjust if image has not yet loaded or if width is not a
         * positive number. The latter might happen when imgAreaSelect is put
         * on a parent element which is then hidden.
         */
        if (!imgLoaded || !$img.width())
            return;
        
        /*
         * Get image offset. The .offset() method returns float values, so they
         * need to be rounded.
         */
        imgOfs = { left: round($img.offset().left), top: round($img.offset().top) };
        
        /* Get image dimensions */
        imgWidth = $img.innerWidth();
        imgHeight = $img.innerHeight();
        
        imgOfs.top += ($img.outerHeight() - imgHeight) >> 1;
        imgOfs.left += ($img.outerWidth() - imgWidth) >> 1;

        /* Set minimum and maximum selection area dimensions */
        minWidth = round(options.minWidth / scaleX) || 0;
        minHeight = round(options.minHeight / scaleY) || 0;
        maxWidth = round(min(options.maxWidth / scaleX || 1<<24, imgWidth));
        maxHeight = round(min(options.maxHeight / scaleY || 1<<24, imgHeight));
        
        /* Determine parent element offset */ 
        parOfs = position == 'fixed' ?
            /* Plugin elements position set to fixed */
            { left: $(document).scrollLeft(), top: $(document).scrollTop() } :
            /* Check parent element position */
            /static|^$/.test($parent.css('position')) ?
                /* Static */
                { left: 0, top: 0 } :
                /* Absolute or relative */
                { left: round($parent.offset().left) - $parent.scrollLeft(),
                    top: round($parent.offset().top) - $parent.scrollTop() };

        left = viewX(0);
        top = viewY(0);
        
        /*
         * Check if selection area is within image boundaries, adjust if
         * necessary
         */
        if (selection.x2 > imgWidth || selection.y2 > imgHeight)
            fixAreaCoords();
    }

    /**
     * Update plugin elements
     * 
     * @param resetKeyPress
     *            If set to <code>false</code>, this instance's keypress
     *            event handler is not activated
     */
    function update(resetKeyPress) {
        /* If plugin elements are hidden, do nothing */
        if (!shown) return;

        /*
         * Set the position and size of the container box and the selection area
         * inside it
         */
        $box.css({ left: viewX(selection.x1), top: viewY(selection.y1) })
            .add($area).width(w = selection.width).height(h = selection.height);

        /*
         * Reset the position of selection area, borders, and handles (IE6/IE7
         * position them incorrectly if we don't do this)
         */ 
        $area.add($border).add($handles).css({ left: 0, top: 0 });

        /* Set border dimensions */
        $border.add($outer)
            .width(max(w - $border.outerWidth() + $border.innerWidth(), 0))
            .height(max(h - $border.outerHeight() + $border.innerHeight(), 0));

        /* Set the dimensions and border styles of the outer area */
        $outer.css({
            left: left,
            top: top,
            width: w,
            height: h,
            borderStyle: 'solid',
            borderWidth: selection.y1 + 'px ' +
                (imgWidth - selection.x2) + 'px ' + (imgHeight - selection.y2) +
                'px ' + selection.x1 + 'px'
        });

        w -= $handles.outerWidth();
        h -= $handles.outerHeight();
        
        /* Arrange handles */
        switch ($handles.length) {
        case 8:
            $($handles[4]).css({ left: w >> 1 });
            $($handles[5]).css({ left: w, top: h >> 1 });
            $($handles[6]).css({ left: w >> 1, top: h });
            $($handles[7]).css({ top: h >> 1 });
        case 4:
            $handles.slice(1,3).css({ left: w });
            $handles.slice(2,4).css({ top: h });
        }

        if (resetKeyPress !== false) {
            /*
             * Need to reset the document keypress event handler -- unbind the
             * current handler
             */
            if ($.imgAreaSelect.keyPress != docKeyPress)
                $(document).off($.imgAreaSelect.keyPress,
                    $.imgAreaSelect.onKeyPress);

            if (options.keys)
                /*
                 * Set the document keypress event handler to this instance's
                 * docKeyPress() function
                 */
                $(document)[$.imgAreaSelect.keyPress](
                    $.imgAreaSelect.onKeyPress = docKeyPress);
        }
    }
    
    /**
     * Do the complete update sequence: recalculate offsets, update the
     * elements, and set the correct values of x1, y1, x2, and y2.
     * 
     * @param resetKeyPress
     *            If set to <code>false</code>, this instance's keypress
     *            event handler is not activated
     */
    function doUpdate(resetKeyPress) {
        adjust();
        update(resetKeyPress);
        x1 = viewX(selection.x1); y1 = viewY(selection.y1);
        x2 = viewX(selection.x2); y2 = viewY(selection.y2);
    }
    
    /**
     * Hide or fade out an element (or multiple elements)
     * 
     * @param $elem
     *            A jQuery object containing the element(s) to hide/fade out
     * @param fn
     *            Callback function to be called when fadeOut() completes
     */
    function hide($elem, fn) {
        options.fadeDuration ? $elem.fadeOut(options.fadeDuration, fn) : $elem.hide();
    }

    /**
     * Check if a touch event is expected and if the passed event object really
     * is a touch event
     *
     * @param event
     *            The event object
     * @return True if the event handler should be interrupted
     */
    function breakWhenNoTouch(event) {
        return touch && !/^touch/.test(event.type);
    }

    /**
     * Check event coordinates to determine if the selection area should be
     * resized or moved
     *
     * @param event
     *            The event object
     */
    function checkResize(event) {
        var x = selX(evX(event)) - selection.x1,
            y = selY(evY(event)) - selection.y1;

        /* Clear the resize mode */
        resize = '';

        if (options.resizable) {
            /*
             * Check if the mouse pointer is over the resize margin area and set
             * the resize mode accordingly
             */
            if (y <= options.resizeMargin)
                resize = 'n';
            else if (y >= selection.height - options.resizeMargin)
                resize = 's';
            if (x <= options.resizeMargin)
                resize += 'w';
            else if (x >= selection.width - options.resizeMargin)
                resize += 'e';
        }

        $box.css('cursor', resize ? resize + '-resize' :
            options.movable ? 'move' : '');
    }

    /**
     * Selection area mousemove event handler
     * 
     * @param event
     *            The event object
     */
    function areaMouseMove(event) {
        if (breakWhenNoTouch(event))
            return;

        if (!adjusted) {
            adjust();
            adjusted = true;

            $box.one('mouseout', function () { adjusted = false; });
        }

        checkResize(event);
    }

    /**
     * Document mouseup event handler
     * 
     * @param event
     *            The event object
     */
    function docMouseUp(event) {
        /* Reset touch action flag */
        touch = false;
        /* Set back the default cursor */
        $('body').css('cursor', '');
        /*
         * If autoHide is enabled, or if the selection has zero width/height,
         * hide the selection and the outer area
         */
        if (options.autoHide || selection.width * selection.height == 0)
            hide($box.add($outer), function () { $(this).hide(); });

        $(document).off('mousemove touchmove', selectingMouseMove);
        $box.on('mousemove touchmove', areaMouseMove);
        
        /*
         * If docMouseUp() is called by areaMouseDown() to work around the issue
         * with Android Chrome, there is no event object, and we don't want to
         * run the onSelectEnd callback function.
         */
        if (event)
            options.onSelectEnd(img, getSelection());
    }

    /**
     * Selection area mousedown event handler
     * 
     * @param event
     *            The event object
     * @return false
     */
    function areaMouseDown(event) {
        if (event.type == 'mousedown' && event.which != 1) return false;
        
        if (event.type == 'touchstart') {
            /*
             * Android Chrome often does not produce a touchend event
             * (https://code.google.com/p/chromium/issues/detail?id=152913), so
             * if it appears that the touch flag is still set, we call the
             * mouseup/touchend event handler to clean up after the previous
             * touch action.
             */
            if (touch)
                docMouseUp();

            /* This is a start of a touch action */
            touch = true;

            /* 
             * Normally, checkResize() is called by the mousemove event handler
             * triggered just before mousedown, but with a touch action there
             * is no mousemove, so we need to call it explicitly.
             */
            checkResize(event);
        }
        else
            adjust();

        if (resize) {
            /* Resize mode is in effect */

            /*
             * Set (x1, y1) to the fixed corner of the selection area, and (x2,
             * y2) to the corner that's being moved.
             */
            x1 = viewX(selection['x' + (1 + /w/.test(resize))]);
            y1 = viewY(selection['y' + (1 + /n/.test(resize))]);
            x2 = viewX(selection['x' + (1 + !/w/.test(resize))]);
            y2 = viewY(selection['y' + (1 + !/n/.test(resize))]);

            edgeX = x2 - evX(event);
            edgeY = y2 - evY(event);

            $(document).on('mousemove touchmove', selectingMouseMove)
                .one('mouseup touchend', docMouseUp);
            $box.off('mousemove touchmove', areaMouseMove);
        }
        else if (options.movable) {
            startX = left + selection.x1 - evX(event);
            startY = top + selection.y1 - evY(event);

            $box.off('mousemove touchmove', areaMouseMove);

            $(document).on('mousemove touchmove', movingMouseMove)
                .one('mouseup touchend', function () {
                    touch = false;
                    options.onSelectEnd(img, getSelection());

                    $(document).off('mousemove touchmove', movingMouseMove);
                    $box.on('mousemove touchmove', areaMouseMove);
                });
        }
        else
            $img.mousedown(event);

        return false;
    }

    /**
     * Adjust the x2/y2 coordinates to maintain aspect ratio (if defined)
     * 
     * @param xFirst
     *            If set to <code>true</code>, calculate x2 first. Otherwise,
     *            calculate y2 first.
     */
    function fixAspectRatio(xFirst) {
        if (aspectRatio)
            if (xFirst) {
                x2 = max(left, min(left + imgWidth,
                    x1 + abs(y2 - y1) * aspectRatio * (x2 > x1 || -1)));    
                y2 = round(max(top, min(top + imgHeight,
                    y1 + abs(x2 - x1) / aspectRatio * (y2 > y1 || -1))));
                x2 = round(x2);
            }
            else {
                y2 = max(top, min(top + imgHeight,
                    y1 + abs(x2 - x1) / aspectRatio * (y2 > y1 || -1)));
                x2 = round(max(left, min(left + imgWidth,
                    x1 + abs(y2 - y1) * aspectRatio * (x2 > x1 || -1))));
                y2 = round(y2);
            }
    }

    /**
     * Check if the coordinates of the selection area are within the required
     * limits and conform to the aspect ratio; adjust if necessary
     */
    function fixAreaCoords() {
        /*
         * Make sure the top left corner of the selection area stays within
         * image boundaries (it might not if the image source was dynamically
         * changed).
         */
        x1 = min(x1, left + imgWidth);
        y1 = min(y1, top + imgHeight);
        
        if (abs(x2 - x1) < minWidth) {
            /* Selection width is smaller than minWidth */
            x2 = x1 - minWidth * (x2 < x1 || -1);

            if (x2 < left)
                x1 = left + minWidth;
            else if (x2 > left + imgWidth)
                x1 = left + imgWidth - minWidth;
        }

        if (abs(y2 - y1) < minHeight) {
            /* Selection height is smaller than minHeight */
            y2 = y1 - minHeight * (y2 < y1 || -1);

            if (y2 < top)
                y1 = top + minHeight;
            else if (y2 > top + imgHeight)
                y1 = top + imgHeight - minHeight;
        }

        x2 = max(left, min(x2, left + imgWidth));
        y2 = max(top, min(y2, top + imgHeight));
        
        fixAspectRatio(abs(x2 - x1) < abs(y2 - y1) * aspectRatio);

        if (abs(x2 - x1) > maxWidth) {
            /* Selection width is greater than maxWidth */
            x2 = x1 - maxWidth * (x2 < x1 || -1);
            fixAspectRatio();
        }

        if (abs(y2 - y1) > maxHeight) {
            /* Selection height is greater than maxHeight */
            y2 = y1 - maxHeight * (y2 < y1 || -1);
            fixAspectRatio(true);
        }

        selection = { x1: selX(min(x1, x2)), x2: selX(max(x1, x2)),
            y1: selY(min(y1, y2)), y2: selY(max(y1, y2)),
            width: abs(x2 - x1), height: abs(y2 - y1) };
    }

    /**
     * Resize the selection area respecting the minimum/maximum dimensions and
     * aspect ratio
     */
    function doResize() {
        fixAreaCoords();

        update();

        options.onSelectChange(img, getSelection());
    }

    /**
     * Mousemove event handler triggered when the user is selecting an area
     * 
     * @param event
     *            The event object
     * @return false
     */
    function selectingMouseMove(event) {
        if (breakWhenNoTouch(event))
            return;
        
        fixAreaCoords();

        x2 = /w|e|^$/.test(resize) || aspectRatio ? evX(event) + edgeX : viewX(selection.x2);
        y2 = /n|s|^$/.test(resize) || aspectRatio ? evY(event) + edgeY : viewY(selection.y2);

        doResize();

        return false;        
    }

    /**
     * Move the selection area
     * 
     * @param newX1
     *            New viewport X1
     * @param newY1
     *            New viewport Y1
     */
    function doMove(newX1, newY1) {
        x2 = (x1 = newX1) + selection.width;
        y2 = (y1 = newY1) + selection.height;

        $.extend(selection, { x1: selX(x1), y1: selY(y1), x2: selX(x2),
            y2: selY(y2) });

        update();

        options.onSelectChange(img, getSelection());
    }

    /**
     * Mousemove event handler triggered when the selection area is being moved
     * 
     * @param event
     *            The event object
     * @return false
     */
    function movingMouseMove(event) {
        if (breakWhenNoTouch(event))
            return;

        x1 = max(left, min(startX + evX(event), left + imgWidth - selection.width));
        y1 = max(top, min(startY + evY(event), top + imgHeight - selection.height));

        doMove(x1, y1);

        event.preventDefault();     
        return false;
    }

    /**
     * Start selection
     */
    function startSelection() {
        $(document).off('mousemove touchmove', startSelection);
        adjust();

        x2 = x1;
        y2 = y1;       
        doResize();

        resize = '';

        if (!$outer.is(':visible'))
            /* Show the plugin elements */
            $box.add($outer).hide().fadeIn(options.fadeDuration||0)

        shown = true;

        $(document).off('mouseup touchend', cancelSelection)
            .on('mousemove touchmove', selectingMouseMove)
            .one('mouseup touchend', docMouseUp);
        $box.off('mousemove touchmove', areaMouseMove);

        options.onSelectStart(img, getSelection());
    }

    /**
     * Cancel selection
     */
    function cancelSelection() {
        $(document).off('mousemove touchmove', startSelection)
            .off('mouseup touchend', cancelSelection);
        hide($box.add($outer));
        
        setSelection(selX(x1), selY(y1), selX(x1), selY(y1));
        
        /* If this is an API call, callback functions should not be triggered */
        if (!(this instanceof $.imgAreaSelect)) {
            options.onSelectChange(img, getSelection());
            options.onSelectEnd(img, getSelection());
        }
    }

    /**
     * Image mousedown event handler
     * 
     * @param event
     *            The event object
     * @return false
     */
    function imgMouseDown(event) {
        /* Ignore the event if animation is in progress */
        if (event.type == 'mousedown' && event.which != 1 ||
                $outer.is(':animated'))
            return false;

        /* If it's a touch action, set the touch flag */
        touch = event.type == 'touchstart';

        adjust();
        startX = x1 = evX(event);
        startY = y1 = evY(event);
        edgeX = edgeY = 0;

        /* Selection will start when the mouse is moved */
        $(document).on({ 'mousemove touchmove': startSelection,
            'mouseup touchend': cancelSelection });

        return false;
    }
    
    /**
     * Window resize event handler
     */
    function windowResize() {
        doUpdate(false);
    }

    /**
     * Image load event handler. This is the final part of the initialization
     * process.
     */
    function imgLoad() {
        imgLoaded = true;

        /* Set options */
        setOptions(options = $.extend({
            classPrefix: 'imgareaselect',
            movable: true,
            parent: 'body',
            resizable: true,
            resizeMargin: 10,
            onInit: function () {},
            onSelectStart: function () {},
            onSelectChange: function () {},
            onSelectEnd: function () {}
        }, options));

        if (options.show) {
            shown = true;
            adjust();
            update();
            $box.add($outer).hide().fadeIn(options.fadeDuration||0)
        }

        /*
         * Call the onInit callback. The setTimeout() call is used to ensure
         * that the plugin has been fully initialized and the object instance is
         * available (so that it can be obtained in the callback).
         */
        setTimeout(function () { options.onInit(img, getSelection()); }, 0);
    }

    /**
     * Document keypress event handler
     * 
     * @param event
     *            The event object
     * @return false
     */
    var docKeyPress = function(event) {
        var k = options.keys, d, t, key = event.keyCode;
        
        d = !isNaN(k.alt) && (event.altKey || event.originalEvent.altKey) ? k.alt :
            !isNaN(k.ctrl) && event.ctrlKey ? k.ctrl :
            !isNaN(k.shift) && event.shiftKey ? k.shift :
            !isNaN(k.arrows) ? k.arrows : 10;

        if (k.arrows == 'resize' || (k.shift == 'resize' && event.shiftKey) ||
            (k.ctrl == 'resize' && event.ctrlKey) ||
            (k.alt == 'resize' && (event.altKey || event.originalEvent.altKey)))
        {
            /* Resize selection */
            
            switch (key) {
            case 37:
                /* Left */
                d = -d;
            case 39:
                /* Right */
                t = max(x1, x2);
                x1 = min(x1, x2);
                x2 = max(t + d, x1);
                fixAspectRatio();
                break;
            case 38:
                /* Up */
                d = -d;
            case 40:
                /* Down */
                t = max(y1, y2);
                y1 = min(y1, y2);
                y2 = max(t + d, y1);
                fixAspectRatio(true);
                break;
            default:
                return;
            }

            doResize();
        }
        else {
            /* Move selection */
            
            x1 = min(x1, x2);
            y1 = min(y1, y2);

            switch (key) {
            case 37:
                /* Left */
                doMove(max(x1 - d, left), y1);
                break;
            case 38:
                /* Up */
                doMove(x1, max(y1 - d, top));
                break;
            case 39:
                /* Right */
                doMove(x1 + min(d, imgWidth - selX(x2)), y1);
                break;
            case 40:
                /* Down */
                doMove(x1, y1 + min(d, imgHeight - selY(y2)));
                break;
            default:
                return;
            }
        }

        return false;
    };

    /**
     * Set plugin options
     * 
     * @param newOptions
     *            The new options object
     */
    function setOptions(newOptions) {
        if (newOptions.parent)
            ($parent = $(newOptions.parent)).append($box).append($outer);
        
        /* Merge the new options with the existing ones */
        $.extend(options, newOptions);

        adjust();

        if (newOptions.handles != null) {
            /* Recreate selection area handles */
            $handles.remove();
            $handles = $([]);

            i = newOptions.handles ? newOptions.handles == 'corners' ? 4 : 8 : 0;

            while (i--)
                $handles = $handles.add(div());
            
            /* Add a class to handles and set the CSS properties */
            $handles.addClass(options.classPrefix + '-handle').css({
                position: 'absolute',
                /*
                 * The font-size property needs to be set to zero, otherwise
                 * Internet Explorer makes the handles too large
                 */
                fontSize: 0,
                zIndex: zIndex + 1 || 1
            });
            
            /*
             * If handle width/height has not been set with CSS rules, set the
             * default 5px
             */
            if (!parseInt($handles.css('width')) >= 0)
                $handles.width(5).height(5);
        }

        /* Calculate scale factors */
        scaleX = options.imageWidth / imgWidth || 1;
        scaleY = options.imageHeight / imgHeight || 1;

        /* Set selection */
        if (newOptions.x1 != null) {
            setSelection(newOptions.x1, newOptions.y1, newOptions.x2,
                newOptions.y2);
            newOptions.show = !newOptions.hide;
        }

        if (newOptions.keys)
            /* Enable keyboard support */
            options.keys = $.extend({ shift: 1, ctrl: 'resize' },
                newOptions.keys);

        /* Add classes to plugin elements */
        $outer.addClass(options.classPrefix + '-outer');
        $area.addClass(options.classPrefix + '-selection');
        for (i = 0; i++ < 4;)
            $($border[i-1]).addClass(options.classPrefix + '-border' + i);

        /* Append all the selection area elements to the container box */
        $box.append($area.add($border)).append($handles);

        if (msie) {
            if (o = ($outer.css('filter')||'').match(/opacity=(\d+)/))
                $outer.css('opacity', o[1]/100);
            if (o = ($border.css('filter')||'').match(/opacity=(\d+)/))
                $border.css('opacity', o[1]/100);
        }
        
        if (newOptions.hide)
            hide($box.add($outer));
        else if (newOptions.show && imgLoaded) {
            shown = true;
            $box.add($outer).fadeIn(options.fadeDuration||0)
            doUpdate();
        }

        /* Calculate the aspect ratio factor */
        aspectRatio = (d = (options.aspectRatio || '').split(/:/))[0] / d[1];

        $img.add($outer).off('mousedown touchstart', imgMouseDown);
        
        if (options.disable || options.enable === false) {
            /* Disable the plugin */
            $box.off({ 'mousemove touchmove': areaMouseMove,
                'mousedown touchstart': areaMouseDown });
            $(window).off('resize', windowResize);
        }
        else {
            if (options.enable || options.disable === false) {
                /* Enable the plugin */
                if (options.resizable || options.movable)
                    $box.on({ 'mousemove touchmove': areaMouseMove,
                        'mousedown touchstart': areaMouseDown });
    
                $(window).resize(windowResize);
            }

            if (!options.persistent)
                $img.add($outer).on('mousedown touchstart', imgMouseDown);
        }
        
        options.enable = options.disable = undefined;
    }
    
    /**
     * Remove plugin completely
     */
    this.remove = function () {
        /*
         * Call setOptions with { disable: true } to unbind the event handlers
         */
        setOptions({ disable: true });
        $box.add($outer).remove();
    };
    
    /*
     * Public API
     */
    
    /**
     * Get current options
     * 
     * @return An object containing the set of options currently in use
     */
    this.getOptions = function () { return options; };
    
    /**
     * Set plugin options
     * 
     * @param newOptions
     *            The new options object
     */
    this.setOptions = setOptions;
    
    /**
     * Get the current selection
     * 
     * @param noScale
     *            If set to <code>true</code>, scaling is not applied to the
     *            returned selection
     * @return Selection object
     */
    this.getSelection = getSelection;
    
    /**
     * Set the current selection
     * 
     * @param x1
     *            X coordinate of the upper left corner of the selection area
     * @param y1
     *            Y coordinate of the upper left corner of the selection area
     * @param x2
     *            X coordinate of the lower right corner of the selection area
     * @param y2
     *            Y coordinate of the lower right corner of the selection area
     * @param noScale
     *            If set to <code>true</code>, scaling is not applied to the
     *            new selection
     */
    this.setSelection = setSelection;
    
    /**
     * Cancel selection
     */
    this.cancelSelection = cancelSelection;
    
    /**
     * Update plugin elements
     * 
     * @param resetKeyPress
     *            If set to <code>false</code>, this instance's keypress
     *            event handler is not activated
     */
    this.update = doUpdate;

    /* Do the dreaded browser detection */
    var msie = (/msie ([\w.]+)/i.exec(ua)||[])[1],
        safari = /webkit/i.test(ua);

    /* 
     * Traverse the image's parent elements (up to <body>) and find the
     * highest z-index
     */
    $p = $img;

    while ($p.length) {
        zIndex = max(zIndex,
            !isNaN($p.css('z-index')) ? $p.css('z-index') : zIndex);
        /*
         * If the parent element is not set explicitly, check if any of the
         * ancestor elements has fixed position
         */ 
        if (!options.parent && $p.css('position') == 'fixed')
            position = 'fixed';

        $p = $p.parent(':not(body)');
    }

    /*
     * If z-index is given as an option, it overrides the one found by the
     * above loop
     */
    zIndex = options.zIndex || zIndex;

    /*
     * In MSIE and WebKit, we need to use the keydown event instead of keypress
     */
    $.imgAreaSelect.keyPress = msie || safari ? 'keydown' : 'keypress';

    $box.add($outer).hide().css({ position: position, overflow: 'hidden',
        zIndex: zIndex || '0' });
    $box.css({ zIndex: zIndex + 2 || 2 });
    $area.add($border).css({ position: 'absolute', fontSize: 0 });
    
    /*
     * If the image has been fully loaded, or if it is not really an image (eg.
     * a div), call imgLoad() immediately; otherwise, bind it to be called once
     * on image load event.
     */
    img.complete || img.readyState == 'complete' || !$img.is('img') ?
        imgLoad() : $img.one('load', imgLoad);

    /* 
     * MSIE 9.0 doesn't always fire the image load event -- resetting the src
     * attribute seems to trigger it. The check is for version 7 and above to
     * accommodate for MSIE 9 running in compatibility mode.
     */
    if (!imgLoaded && msie && msie >= 7)
        img.src = img.src;
};

/**
 * Invoke imgAreaSelect on a jQuery object containing the image(s)
 * 
 * @param options
 *            Options object
 * @return The jQuery object or a reference to imgAreaSelect instance (if the
 *         <code>instance</code> option was specified)
 */
$.fn.imgAreaSelect = function (options) {
    options = options || {};

    this.each(function () {
        /* Is there already an imgAreaSelect instance bound to this element? */
        if ($(this).data('imgAreaSelect')) {
            /* Yes there is -- is it supposed to be removed? */
            if (options.remove) {
                /* Remove the plugin */
                $(this).data('imgAreaSelect').remove();
                $(this).removeData('imgAreaSelect');
            }
            else
                /* Reset options */
                $(this).data('imgAreaSelect').setOptions(options);
        }
        else if (!options.remove) {
            /* No exising instance -- create a new one */
            
            /*
             * If neither the "enable" nor the "disable" option is present, add
             * "enable" as the default
             */ 
            if (options.enable === undefined && options.disable === undefined)
                options.enable = true;

            $(this).data('imgAreaSelect', new $.imgAreaSelect(this, options));
        }
    });
    
    if (options.instance)
        /*
         * Return the imgAreaSelect instance bound to the first element in the
         * set
         */
        return $(this).data('imgAreaSelect');

    return this;
};

})(jQuery);
;/**
 * PrimeFaces Extensions ImageAreaSelect Widget.
 *
 * @author Thomas Andraschko
 */
PrimeFaces.widget.ExtImageAreaSelect = PrimeFaces.widget.BaseWidget.extend({

    /**
     * Initializes the widget.
     *
     * @param {object} cfg The widget configuration.
     */
    init : function(cfg) {
        this.id = cfg.id;
        this.cfg = cfg;

        this.options = {};
        this.options.instance = true,
        this.options.classPrefix = 'pe-imgageareaselect';

        if (this.cfg.aspectRatio) {
                this.options.aspectRatio = this.cfg.aspectRatio;
        }
        if (this.cfg.autoHide) {
                this.options.autoHide = this.cfg.autoHide;
        }
        if (this.cfg.fadeSpeed) {
                this.options.fadeSpeed = this.cfg.fadeSpeed;
        }
        if (this.cfg.handles) {
                this.options.handles = this.cfg.handles;
        }
        if (this.cfg.hide) {
                this.options.hide = this.cfg.hide;
        }
        if (this.cfg.imageHeight) {
                this.options.imageHeight = this.cfg.imageHeight;
        }
        if (this.cfg.imageWidth) {
                this.options.imageWidth = this.cfg.imageWidth;
        }
        if (this.cfg.maxHeight) {
                this.options.maxHeight = this.cfg.maxHeight;
        }
        if (this.cfg.maxWidth) {
                this.options.maxWidth = this.cfg.maxWidth;
        }
        if (this.cfg.movable) {
                this.options.movable = this.cfg.movable;
        }
        if (this.cfg.persistent) {
                this.options.persistent = this.cfg.persistent;
        }
        if (this.cfg.resizable) {
                this.options.resizable = this.cfg.resizable;
        }
        if (this.cfg.show) {
                this.options.show = this.cfg.show;
        }
        if (this.cfg.parentSelector) {
                this.options.parent = this.cfg.parentSelector;
        }
        if (this.cfg.keyboardSupport) {
                this.options.keys = this.cfg.keyboardSupport;
        }

        this.bindSelectStartCallback();
        this.bindSelectChangeCallback();
        this.bindSelectEndCallback();

        this.instance = PrimeFaces.expressions.SearchExpressionFacade.resolveComponentsAsSelector(this.cfg.target).imgAreaSelect(this.options);

        this.removeScriptElement(this.id);
    },

    /**
     * Binds the selectEnd callback.
     *
     * @private
     */
    bindSelectEndCallback : function() {
        if (this.cfg.behaviors) {
            var selectEndCallback = this.cfg.behaviors['selectEnd'];
            if (selectEndCallback) {
                this.options.onSelectEnd = $.proxy(function(img, selection) {
                    var options = {};

                    this.fillSelectEventsParameter(img, selection, options);

                    selectEndCallback.call(this, options);
                }, this);
            }
        }
    },

    /**
     * Binds the selectStart callback.
     *
     * @private
     */
    bindSelectStartCallback : function() {
        if (this.cfg.behaviors) {
            var selectStartCallback = this.cfg.behaviors['selectStart'];
            if (selectStartCallback) {
                this.options.onSelectStart = $.proxy(function(img, selection) {
                var options = {};

                this.fillSelectEventsParameter(img, selection, options);

                    selectStartCallback.call(this, options);
                }, this);
            }
        }
    },

    /**
     * Binds the selectChange callback.
     *
     * @private
     */
    bindSelectChangeCallback : function() {
        if (this.cfg.behaviors) {
            var selectChangeCallback = this.cfg.behaviors['selectChange'];
            if (selectChangeCallback) {
                this.options.onSelectChange = $.proxy(function(img, selection) {
                    var options = {};

                    this.fillSelectEventsParameter(img, selection, options);

                    selectChangeCallback.call(this, options);
                }, this);
            }
        }
    },

    /**
     * Fills the required parameters.
     *
     * @param {object} img The img object from the imgareaselect plugin.
     * @param {object} selection The selection object from the imgareaselect plugin.
     * @param {object} ext The AJAX extensions object.
     * @private
     */
    fillSelectEventsParameter : function(img, selection, options) {
        options.params = [
            { name: this.id + '_x1', value: selection.x1 },
            { name: this.id + '_x2', value: selection.x2 },
            { name: this.id + '_y1', value: selection.y1 },
            { name: this.id + '_y2', value: selection.y2 },
            { name: this.id + '_width', value: selection.width },
            { name: this.id + '_height', value: selection.height },
            { name: this.id + '_imgSrc', value: img.src },
            { name: this.id + '_imgHeight', value: img.height },
            { name: this.id + '_imgWidth', value: img.width }
        ];
    },

    /**
     * Updates the widget.
     */
    update : function() {
        this.instance.update();
    },

    /**
     * Reloads the widget.
     */
    reload : function() {
        this.setOptions({remove: true});
        this.update();
        this.instance = PrimeFaces.expressions.SearchExpressionFacade.resolveComponentsAsSelector(this.cfg.target).imgAreaSelect(this.options);
    },

    destroy : function() {
        this.cancelSelection();
        this.instance = null;
        PrimeFaces.expressions.SearchExpressionFacade.resolveComponentsAsSelector(this.cfg.target).imgAreaSelect({remove:true});
    },

    refresh : function(cfg) {
        this.destroy();
        this.init(cfg);
    },

    /**
     * Cancel the current selection.
     * This method hides the selection/outer area,
     * so no call to update() is necessary (as opposed to setSelection()).
     */
    cancelSelection : function() {
        if (this.instance) {
            this.instance.cancelSelection();
        }
    },

    /**
     * Get the current selection.
     *
     * @returns {object} An object containing the selection.
     */
    getSelection : function() {
        return this.instance.getSelection();
    },

    /**
     * Set the current selection.
     * This method only sets the internal representation of selection in the plugin instance,
     * it does not update the visual interface.
     * If you want the new selection to be shown, call update() right after setSelection().
     * Also make sure that the show option is set to true.
     *
     * @param {int} x1 X coordinate of the top left corner of the selection area.
     * @param {int} y1 Y coordinate of the top left corner of the selection area.
     * @param {int} x2 X coordinate of the bottom right corner of the selection area.
     * @param {int} y2 Y coordinate of the bottom right corner of the selection area.
     */
    setSelection : function(x1, y1, x2, y2) {
        this.instance.setSelection(x1, y1, x2, y2);
    },

    /**
     * Get current options.
     *
     * @returns {object} An object containing the set of options currently in use.
     */
    getOptions : function() {
        return this.instance.getOptions();
    },

    /**
     * Set the plugin options.
     * This method only sets the internal representation of selection in the plugin instance,
     * it does not update the visual interface.
     * If you want the new selection to be shown, call update() right after setSelection().
     * Also make sure that the show option is set to true.
     *
     * @param {object} options The options for the widget.
     */
    setOptions : function(options) {
        this.instance.setOptions(options);
    }
});
