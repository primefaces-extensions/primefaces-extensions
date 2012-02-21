/*
 * Primeface Extensions Timeline widget
 * 
 * @constructor
 */
PrimeFacesExt.widget.Timeline = function(cfg) {
    this.id = cfg.id;
    this.cfg = cfg;     
    this.jqId = PrimeFaces.escapeClientId(this.id);    
    this.jq = $(this.jqId);    
    this.jq.append('<div id="'+ this.id + '_main" class="ui-timeline ui-widget-content ui-corner-all"><div id="'+ this.id + '_scroll"></div></div>');
    this.container = $('#' + this.id + '_scroll');    
    if(this.cfg.dataSource) {
        this.bindTimelines();
    }
    this.postConstruct();
}

PrimeFaces.extend(PrimeFacesExt.widget.Timeline, PrimeFaces.widget.BaseWidget);

PrimeFacesExt.widget.Timeline.prototype.bindTimelines = function() {
    
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
}

PrimeFacesExt.widget.Timeline.prototype.createTimeline = function(tl) {
    var dom = '<div id="' + tl.id + '" class="ui-timeline-menu">';
    dom += '<div class="ui-timeline-menu-header ui-widget-header ui-corner-all">';
    dom += tl.title + '</div><ul id="'+ tl.id +'_ul" class="ui-timeline-event-list"></ul></div>';    
    this.container.append(dom);
    var eventContainer = $('#' + tl.id + '_ul');
    var size = tl.events.length;
    for(var index = 0; index < size; index++){
        this.createTimelineEvent(tl.events[index], eventContainer);        
    }        
}

PrimeFacesExt.widget.Timeline.prototype.createTimelineEvent = function(tlEvent, container) {
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
            source: '#' + eventId,
            target: '#' + targetId,
            content: '#' + eventId + '_content'
        });
        blockUI.block();
        $('.blockOverlay').attr("title","Click to unblock").click(blockUI.unblock);
    });
}