/*
    tabSlideOUt v2.4

    By William Paoli: http://wpaoli.building58.com
    Contributions by:
        Michael Fielding
    License: GPL v3.0
    Original location: https://github.com/hawk-ip/jquery.tabSlideOut.js

    To use this you need an element for the tab panel content ('panel'), and inside it an element for the 
    tab which will stick out from the window edge and be clickable ('handle'). By default the selector 
    for handles is '.handle'.

    example HTML:
    
        <div id="my-tab"><span class="handle">Click me</span>Hello World</div>

    example JavaScript (puts the tab on the right, and opens it on hover rather than click):
    
        $('#my-tab').tabSlideOut( {'tabLocation':'right','action':'hover'} );
        
    Style the tab panel and handle using CSS. Add the class ui-slideouttab-handle-rounded to handles to give them 
    rounded outer corners.

    You can use some methods to programmatically interact with tabs. Methods except 'isOpen' are chainable.

        $('#my-tab').tabSlideOut('isOpen'); // return true or false
        $('#my-tab').tabSlideOut('open'); // opens it
        $('#my-tab').tabSlideOut('close'); // closes it
        $('#my-tab').tabSlideOut('toggle'); // toggles it
        $('#my-tab').tabSlideOut('bounce'); // bounces the tab
        
    You can also send JQuery events to initiate actions:
    
        $('#my-tab').trigger('open'); // opens it
        $('#my-tab').trigger('close'); // closes it
        $('#my-tab').trigger('toggle'); // toggles it
        $('#my-tab').trigger('bounce'); // bounces the tab

    Three events are defined and can be caught when tabs open and close:

        $(document).on('slideouttabopen slideouttabclose slideouttabbounce',function(event){
            var $panel = $(event.target);
            var eventType = event.type;
            // your code here
        });

    Features are demonstrated on the related demo page.
*/
(function($){
    $.fn.tabSlideOut = function(callerSettings) {

        /**
         * @param node Element to get the height of.
         * @return string e.g. '123px'
         */
        function heightAsString( node ) {
            return parseInt(node.outerHeight()+1, 10) + 'px';
        }
        /**
         * @param node Element to get the width of.
         * @return string e.g. '123px'
         */
        function widthAsString( node ) {
            return parseInt(node.outerWidth()+1, 10) + 'px';
        }
        
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
      
        /**
         * True if the tab is open.
         * 
         * @returns boolean
         */
        function isOpen() {
            return panel.hasClass('ui-slideouttab-open');
        }
        
        if ( typeof callerSettings == 'string' )
        {
            // param is a string, use command mode
            switch ( callerSettings )
            {
                case 'open':
                    this.trigger('open');
                    return this;
                case 'close':
                    this.trigger('close');
                    return this;
                case 'isOpen':
                    return isOpen();
                case 'toggle':
                    this.trigger('toggle');
                    return this;
                case 'bounce':
                    this.trigger('bounce');
                    return this;
                default:
                    throw "Invalid tabSlideOut command";
            }
        }
        else
        {
            // param is an object, it's initialisation mode
            var settings = $.extend({
                tabLocation: 'left', // left, right, top or bottom
                tabHandle: '.handle', // JQuery selector for the tab, can use any JQuery selector
                action: 'click',  // action which will open the panel, e.g. 'hover'
                hoverTimeout: 5000, // ms to keep tab open after no longer hovered - only if action = 'hover'
                offset: '200px', // panel dist from top or left (bottom or right if offsetReverse is true)
                offsetReverse: false, // if true, panel is offset from  right or bottom of window instead of left or top
                otherOffset: null, // if set, panel size is also set to maintain this dist from bottom or right of view port (top or left if offsetReverse)
                handleOffset: null, // e.g. '10px'. If null, detects panel border to align handle nicely on edge
                handleOffsetReverse: false, // if true, handle is offset from right or bottom of panel instead of left or top
                bounceDistance: '50px', // how far bounce event will move everything
                bounceTimes: 4, // how many bounces when 'bounce' is called
                bounceSpeed: 300, // time to animate bounces
                tabImage: null, // optional image to show in the tab
                tabImageHeight: null, // optional IE8 and lower only, else autodetected size
                tabImageWidth: null, // optional IE8 and lower only, else autodetected size
                onLoadSlideOut: false, // slide out after DOM load
                clickScreenToClose: true, // close tab when somewhere outside the tab is clicked
                clickScreenToCloseFilters: ['.ui-slideouttab-panel'], // if click target or parents match any of these, click won't close this tab
                onOpen: function(){}, // handler called after opening
                onClose: function(){} // handler called after closing
            }, callerSettings||{});

            var edge = settings.tabLocation; 
            var handle = settings.tabHandle = $(settings.tabHandle,panel);
            
            panel.addClass('ui-slideouttab-panel')
                .addClass('ui-slideouttab-'+edge);
            if ( settings.offsetReverse ) 
                panel.addClass('ui-slideouttab-panel-reverse');
            handle.addClass('ui-slideouttab-handle'); // need this to find it later
            if ( settings.handleOffsetReverse ) 
                handle.addClass('ui-slideouttab-handle-reverse');
            settings.toggleButton = $(settings.toggleButton);

            // apply an image to the tab if one is defined
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

            // determine whether panel and handle are positioned from top, bottom, left, or right
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
                    handle.css({'bottom' : '-' + heightAsString(handle)});
                }
                else {
                    handle.css({'top' : '-' + heightAsString(handle)});
                }
            } else {
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
                    handle.css({'right' : '0'});
                } else {
                    handle.css({'left' : '0'});
                }
            }

            handle.click(function(event){
                event.preventDefault();
            });
            settings.toggleButton.click(function(event){
                event.preventDefault();
            });
            
            // now everything is set up, add the class which enables CSS tab animation
            panel.addClass('ui-slideouttab-ready');

            var close = function() {
                panel.removeClass('ui-slideouttab-open').trigger('slideouttabclose');
                settings.onClose();
            };

            var open = function() {
                panel.addClass('ui-slideouttab-open').trigger('slideouttabopen');
                settings.onOpen();
            };
            
            var toggle = function() {
                if (isOpen()) {
                    close();
                } else {
                    open();
                }
            };
          
            // animate the tab in and out when 'bounced'
            var moveIn = [];
            moveIn[edge] = '-=' + settings.bounceDistance;
            var moveOut = [];
            moveOut[edge] = '+=' + settings.bounceDistance;
            
            var bounceIn = function() {
                var temp = panel;
                for ( var i = 0; i < settings.bounceTimes; i++ )
                {
                    temp = temp.animate(moveIn,  settings.bounceSpeed)
                       .animate(moveOut,  settings.bounceSpeed);
                }
                panel.trigger('slideouttabbounce');
            };
            
            var bounceOut = function() {
                var temp = panel;
                for ( var i = 0; i < settings.bounceTimes; i++ )
                {
                    temp = temp.animate(moveOut,  settings.bounceSpeed)
                       .animate(moveIn,  settings.bounceSpeed);
                }
                panel.trigger('slideouttabbounce');
            };

            // handle clicks in rest of document to close tabs if they're open
            if ( settings.clickScreenToClose ) {
                // install a click handler to close tab if anywhere outside the tab is clicked,
                // that isn't filtered out by the configured filters
                $(document).click(function(event){
                    // first check the tab is open and the click isn't inside it
                    if ( isOpen() && !panel[0].contains(event.target) ){
                        // something other than this panel was clicked
                        var clicked = $(event.target);
                        
                        // check to see if any filters return true
                        for ( var i=0; i< settings.clickScreenToCloseFilters.length; i++ ) {
                            var filter = settings.clickScreenToCloseFilters[i];
                            if ( typeof filter === 'string' ) {
                                // checked clicked element itself, and all parents
                                if ( clicked.is(filter) || clicked.parents().is(filter)) {
                                    return; // don't close the tab
                                }
                            } else if ( typeof filter === 'function' ) {
                                // call custom filter
                                if ( filter.call(panel,event) )
                                    return; // don't close the tab
                            }
                        }
                        
                        // we haven't returned true from any filter, so close the tab
                        close();
                    }
                });
            };
            
            //choose which type of action to bind
            if (settings.action === 'click') {
                handle.click(function(event){
                    toggle();
                });
            } else if (settings.action === 'hover') {
                var timer = null;
                panel.hover(
                    function(){
                        if (!isOpen()) {
                            open();
                        }
                        timer = null; // eliminate the timer, ensure we don't close now
                    },
                    function(){
                        if (isOpen() && timer === null) {
                            timer = setTimeout(function(){
                                if ( timer )
                                    close();
                                timer = null;
                            }, settings.hoverTimeout);
                        }
                });

                handle.click(function(event){
                    if (isOpen()) {
                        close();
                    }
                });
            }

            if (settings.onLoadSlideOut) {
                open();
                setTimeout(open, 500);
            }
            
            // custom event handlers -------
            panel.on('open', function(event) {
                if (!isOpen()) {
                    open();
                }
            });
            panel.on('close', function(event) {
                if (isOpen()) {
                    close();
                }
            });
            panel.on('toggle', function(event) {
                toggle();
            });
            panel.on('bounce', function(event){
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