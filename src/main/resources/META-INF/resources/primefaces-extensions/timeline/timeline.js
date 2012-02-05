/*
 * Primeface Extension Timeline widget
 */
PrimeFacesExt.widget.Timeline = function(cfg) {
    this.id = cfg.id;
    this.cfg = cfg;     
    this.jqId = PrimeFaces.escapeClientId(this.id);    
    this.jq = $(this.jqId);
    this.menus = this.jq.find('.ui-timeline-menu');
    
    var totalDates = this.menus.length;
    var timelineWidth = 320 * totalDates;
    $('#' + this.id + '_scroll').width(timelineWidth + 10);
    
    this.jq.mousedown(function (event) {
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
    
    $(".ui-timeline-event-list li").hover(function () {
        $(this).addClass("ui-state-hover");
    }, function () {
        $(this).removeClass("ui-state-hover");
    });    
}