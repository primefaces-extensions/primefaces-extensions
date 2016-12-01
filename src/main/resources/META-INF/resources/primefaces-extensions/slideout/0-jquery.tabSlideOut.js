/*
    tabSlideOUt v2.3.1

    By William Paoli: http://wpaoli.building58.com
    Contributions by:
        Michael Fielding / www.hawkip.com
    License: GPL v2.0
    Original location: http://code.google.com/p/tab-slide-out

    To use you must have a div, a, img, span etc. for the handle, inside a div which
    will be the panel. By default the selector for handles is .handle

    example:

        $('#slide-out-div').tabSlideOut({
                tabHandle: '.handle', //selector for the tab
        });

    You can leave out most options and set the properties using css.

    There is an optional setting bottomPos which, when set, fixes the gap between the window
    bottom edge and the panel bottom - the panel is resized with the window. This only
    really makes sense if positioning: 'fixed', and only works if tabLocation is
    left or right.

    You can use some methods too:

        $('#slide-out-div').tabSlideOut('isOpen'); // return true or false
        $('#slide-out-div').tabSlideOut('open'); // opens it
        $('#slide-out-div').tabSlideOut('close'); // closes it
        $('#slide-out-div').tabSlideOut('toggle'); // toggles it
        $('#slide-out-div').tabSlideOut('bounce'); // bounces the tab

    Three events are defined, respond to one or more of them as follows:

        $(document).on('slideouttabopen slideouttabclose slideouttabbounce',function(event){
            var $panel = $(event.target);
            var eventType = event.type;
            // your code here
        });

    Add the class ui-slideouttab-handle-rounded to handles to give them 
    rounded outer corners.
*/
(function($){
    $.fn.tabSlideOut = function(callerSettings) {

        /*
         * Get the width of the given border, in pixels.
         * 
         * @param node element
         * @param string edge
         * @returns int
         */
        function borderWidth(element,edge){
            return parseInt(element.css('border-'+edge+'-width'), 10);
        }

        /**
         * True if the tab is open.
         * 
         * @returns boolean
         */
        function isOpen() {
            return panel.hasClass('ui-slideouttab-open');
        }
        
        /**
         * Return the desired height of the panel to maintain both offsets.
         */
        function calculatePanelSize() {
            var available = $(window).height();
            if ( edge==='top' || edge==='bottom') {
                available = $(window).width();
            }
            return available - parseInt(settings.otherOffset) - parseInt(settings.offset);
        }

        var panel = this;
            
        if ( typeof callerSettings == 'string' )
        {
            // param is a string, use command mode
            switch ( callerSettings )
            {
                case 'open':
                    if ( !isOpen() )
                        this.children('.ui-slideouttab-handle').click();
                    break;
                case 'close':
                    if ( isOpen() )
                        this.children('.ui-slideouttab-handle').click();
                    break;
                case 'isOpen':
                    return isOpen();
                    break;
                case 'toggle':
                    this.children('.ui-slideouttab-handle').click();
                    break;
                case 'bounce':
                    this.children('.ui-slideouttab-handle').trigger('bounce');
                    break;
                default:
                    throw "Invalid tabSlideOut command";
            }
        }
        else
        {
            // param is an object, it's initialisation mode
            var settings = $.extend({
                tabLocation: 'left', // left, right, top or bottom
                tabHandle: '.handle', // JQuery selector for the tab, can use #
                speed: 300, // time to animate
                action: 'click',  // action which will open the panel, e.g. 'hover'
                offset: '200px', // panel dist from top or left (bottom or right if offsetReverse is true)
                offsetReverse: false, // if true, panel is aligned with right or bottom of window
                otherOffset: null, // if set, panel size is set to maintain this dist from bottom or right (top or left if offsetReverse)
                handleOffset: null, // e.g. '10px'. If null, detects panel border to align handle nicely
                handleOffsetReverse: false, // if true, handle aligned with right or bottom of panel 
                bounceDistance: '50px', // how far bounce event will move everything
                bounceTimes: 4, // how many bounces when 'bounce' is called
                positioning: 'fixed', // can also use absolute, so tabs move when window scrolls
                tabImage: null, // optional image to show in the tab
                tabImageHeight: null, // optional IE8 and lower only, else autodetected size
                tabImageWidth: null, // optional IE8 and lower only, else autodetected size
                onLoadSlideOut: false, // slide out after DOM load
                clickScreenToClose: true, // close tab when rest of screen clicked
                toggleButton: '.tab-opener', // not often used
                onOpen: function(){}, // handler called after opening
                onClose: function(){} // handler called after closing
            }, callerSettings||{});

            var edge = settings.tabLocation; 
            var handle = settings.tabHandle = $(settings.tabHandle,panel);
            panel.addClass('ui-slideouttab-panel');
            panel.addClass('ui-slideouttab-'+edge);
            if ( settings.offsetReverse ) panel.addClass('ui-slideouttab-panel-reverse');
            handle.addClass('ui-slideouttab-handle'); // need this to find it later
            if ( settings.handleOffsetReverse ) handle.addClass('ui-slideouttab-handle-reverse');
            settings.toggleButton = $(settings.toggleButton);

            //ie6 doesn't do well with the fixed option
            if (document.all && !window.opera && !window.XMLHttpRequest) {
                settings.positioning = 'absolute';
            }

            // apply an image if one is defined
            if (settings.tabImage !== null) {
                var imageHeight = 0;
                var imageWidth = 0;
                if (settings.tabImageHeight !== null && settings.tabImageWidth !== null) {
                   imageHeight = settings.tabImageHeight;
                   imageWidth = settings.tabImageWidth;
                } else {
                   var img = new Image();
                   img.src = settings.tabImage;
                   imageHeight = img.naturalHeight;
                   imageWidth = img.naturalWidth;
                }

                handle.addClass('ui-slideouttab-handle-image');
                handle.css({
                    'background' : 'url('+settings.tabImage+') no-repeat',
                    'width' : imageWidth,
                    'height': imageHeight
                });
            }

            handle.css({
                'display': 'block',
                'position' : 'absolute'
            });

            panel.css({
                'position' : settings.positioning
            });

            // set up alignment information based on settings
            if ( edge === 'top' || edge === 'bottom' ){
                settings.panelOffsetFrom = 
                        settings.offsetReverse ? 'right' : 'left';
                settings.handleOffsetFrom = 
                        settings.handleOffsetReverse ? 'right' : 'left';
            } else {
                settings.panelOffsetFrom = 
                        settings.offsetReverse ? 'bottom' : 'top';
                settings.handleOffsetFrom = 
                        settings.handleOffsetReverse ? 'bottom' : 'top';
            }
            
            /* autodetect the correct offset for the handle using appropriate panel border*/
            if (settings.handleOffset === null) {
                settings.handleOffset = '-'+borderWidth(panel,settings.handleOffsetFrom)+'px';
            }
            
            var sizes = {
                        panelWidth: parseInt(panel.outerWidth()+1, 10) + 'px',
                        panelHeight: parseInt(panel.outerHeight()+1, 10) + 'px',
                        handleWidth: parseInt(handle.outerWidth(), 10) + 'px',
                        handleHeight: parseInt(handle.outerHeight()+1, 10) + 'px'
                    };

            // 
            if(edge === 'top' || edge === 'bottom') {
                /* set left or right edges */
                panel.css( settings.panelOffsetFrom, settings.offset);
                handle.css( settings.handleOffsetFrom, settings.handleOffset );
                
                // possibly drive the panel size
                if ( settings.otherOffset !== null ) {
                    panel.css( 'width', calculatePanelSize() + 'px' );
                    // install resize handler
                    $(window).resize(function() { 
                        panel.css( 'width', calculatePanelSize() + 'px');
                    });
                }
            
                if(edge === 'top') {
                    panel.css({'top' : '-' + sizes.panelHeight});
                    handle.css({'bottom' : '-' + sizes.handleHeight});
                }
                else {
                    panel.css({'bottom' : '-' + sizes.panelHeight, 'position' : 'fixed'});
                    handle.css({'top' : '-' + sizes.handleHeight});
                }
            }


            if(edge === 'left' || edge === 'right') {
                /* set top or bottom edge */
                panel.css( settings.panelOffsetFrom, settings.offset );
                handle.css( settings.handleOffsetFrom, settings.handleOffset);

                // possibly drive the panel size
                if ( settings.otherOffset !== null ) {
                    panel.css( 'height', calculatePanelSize() + 'px' );
                    // install resize handler
                    $(window).resize(function() { 
                        panel.css( 'height', calculatePanelSize() + 'px');
                    });
                }
            
                if(edge === 'left') {
                    panel.css({ 'left': '-' + sizes.panelWidth});
                    handle.css({'right' : '0'});
                } else {
                    panel.css({ 'right': '-' + sizes.panelWidth});
                    handle.css({'left' : '0'});

                    $('html').css('overflow-x', 'hidden');
                }
            }

            handle.click(function(event){
                event.preventDefault();
            });
            settings.toggleButton.click(function(event){
                event.preventDefault();
            });

            var slideIn = function() {
                var size;
                switch ( edge )
                {
                    case 'top':
                    case 'bottom':
                        size = sizes.panelHeight;
                        break;
                    case 'left':
                    case 'right':
                        size = sizes.panelWidth;
                }
                
                var param = [];
                param[edge] = '-' + size;
                panel.removeClass('ui-slideouttab-open').animate(param, settings.speed, function(){
                    panel.trigger('slideouttabclose');
                    settings.onClose();
                });
            };

            var slideOut = function() {
                var param = [];
                // show everything except the border along the edge we're on
                param[edge] = '-'+borderWidth(panel,edge)+'px';
                panel.animate(param,  settings.speed, function(){
                    panel.addClass('ui-slideouttab-open').trigger('slideouttabopen');
                    settings.onOpen();
                });
            };
            
            // animate the tab in and out
            var moveIn = [];
            moveIn[edge] = '-=' + settings.bounceDistance;
            var moveOut = [];
            moveOut[edge] = '+=' + settings.bounceDistance;
            
            var bounceIn = function() {
                var temp = panel;
                for ( var i = 0; i < settings.bounceTimes; i++ )
                {
                    temp = temp.animate(moveIn,  settings.speed)
                       .animate(moveOut,  settings.speed);
                }
                panel.trigger('slideouttabbounce');
            };
            
            var bounceOut = function() {
                var temp = panel;
                for ( var i = 0; i < settings.bounceTimes; i++ )
                {
                    temp = temp.animate(moveOut,  settings.speed)
                       .animate(moveIn,  settings.speed);
                }
                panel.trigger('slideouttabbounce');
            };

            // handle clicks in rest of document to close tabs if they're open
            var clickScreenToClose = function() {
                panel.click(function(event){
                    event.stopPropagation();
                });

                settings.toggleButton.click(function(event){
                    event.stopPropagation();
                });


                $(document).click(function(){
                    if ( isOpen() ){
                        slideIn();
                    }
                });
            };

            var clickAction = function(){
                handle.click(function(event){
                    if (isOpen()) {
                        slideIn();
                    } else {
                        slideOut();
                    }
                });
                settings.toggleButton.click(function(event){
                    if (isOpen()) {
                        slideIn();
                    } else {
                        slideOut();
                    }
                });
                if ( settings.clickScreenToClose )
                    clickScreenToClose();
            };

            var hoverAction = function(){
                panel.hover(
                    function(){
                        if (!isOpen()) {
                            slideOut();
                        }
                    },

                    function(){
                        if (isOpen()) {
                            setTimeout(slideIn, 1000);
                        }
                    });

                    handle.click(function(event){
                        if (isOpen()) {
                            slideIn();
                        }
                    });

                    settings.toggleButton.click(function(event){
                        if (isOpen()) {
                            slideIn();
                        } else {
                            slideOut();
                        }
                    });

                    if ( settings.clickScreenToClose )
                        clickScreenToClose();

            };

            var slideOutOnLoad = function(){
                slideIn();
                setTimeout(slideOut, 500);
            };

            //choose which type of action to bind
            if (settings.action === 'click') {
                clickAction();
            }

            if (settings.action === 'hover') {
                hoverAction();
            }

            if (settings.onLoadSlideOut) {
                slideOutOnLoad();
            }
            
            handle.on('bounce', function(event){
                if (isOpen()) {
                    bounceIn();
                } else {
                    bounceOut();
                }
            });

        }
        return this;
    };
})(jQuery);