/**
 * PrimeFaces Extensions Timeline Widget.
 *
 * @author Nilesh Namdeo Mali
 */
PrimeFacesExt.widget.Timeline = PrimeFaces.widget.BaseWidget.extend({
    
        /**
	 * Initializes the widget.
	 * 
	 * @param {object} cfg The widget configuration.
	 */
	init : function(cfg) {
	    this._super(cfg);
	    
	    this.jq.append('<div id="'+ this.id + '_main" class="pe-timeline ui-widget-content ui-corner-all"><div id="'+ this.id + '_scroll"></div></div>');
	    this.container = $('#' + this.id + '_scroll');    
	    
	    if(this.cfg.dataSource) {
	        this.bindTimelines();
	    }
	},

	bindTimelines : function() {
	    var dataSource = this.cfg.dataSource;    
	    var size = dataSource.length;
	    var timelineWidth = 320 * size;     
	    this.container.width(timelineWidth + 10);
	    
	    $('#' + this.id + '_main').mousedown(function (event) {
	        $(this)
	        .data('down', true)
	        .data('x', event.clientX)
	        .data('scrollLeft', this.scrollLeft);
	                
	        return false;
	    }).mouseup(function (event) {
	        $(this).data('down', false);
	    }).mousemove(function (event) {
	        if ($(this).data('down') == true) {
	            this.scrollLeft = $(this).data('scrollLeft') + $(this).data('x') - event.clientX;
	        }
	    }).mousewheel(function (event, delta) {
	        this.scrollLeft -= (delta * 30);
	    }).css({
	        'overflow' : 'hidden',
	        'cursor' : '-moz-grab'
	    });
	    
	    for(var index = 0; index < size; index++){    
	        this.createTimeline(dataSource[index]);
	    }    
	},

	createTimeline : function(tl) {
	    var dom = '<div id="' + tl.id + '" class="pe-timeline-menu">';
	    dom += '<div class="pe-timeline-menu-header ui-widget-header ui-corner-all">';
	    dom += tl.title + '</div><ul id="'+ tl.id +'_ul" class="pe-timeline-event-list"></ul></div>';    
	    this.container.append(dom);
	    var eventContainer = $('#' + tl.id + '_ul');
	    var size = tl.events.length;
	    for(var index = 0; index < size; index++){
	        this.createTimelineEvent(tl.events[index], eventContainer);        
	    }        
	},

	createTimelineEvent : function(tlEvent, container) {
	    var eventId = tlEvent.id;    
	    var dom = '<li id="' + eventId + '_li" class="ui-widget-content ui-corner-all"><span></span>';    
	    dom += tlEvent.title;
	    dom += '<div id="'+ eventId + '_content" class="content">';
	    dom += tlEvent.startDate + ((tlEvent.startDate == '' || tlEvent.endDate == '')?'':' - ') + tlEvent.endDate + '<br/>' + tlEvent.description;
	    dom += '</div><li>';    
	    container.append(dom);
	    
	    // Hover for TimelineEvent
	    $('#' + eventId + '_li').hover(function () {
	        $(this).addClass("ui-state-hover");
	    }, function () {
	        $(this).removeClass("ui-state-hover");
	    });    
	    var targetId = this.id;
	    
	    // BlockUI on click
	    $('#' + eventId + '_li').click(function(){
	        var blockUI = new PrimeFacesExt.widget.BlockUI({
                    id: eventId + '_jsf',    
	            source: '#' + eventId,
	            target: '#' + targetId,
	            content: '#' + eventId + '_content'
	        });
	        blockUI.block();
	        $('.ui-widget-overlay').attr("title","Click to unblock").click(function(){                        
                        blockUI.unblock();
                });
	    });
	}
});
