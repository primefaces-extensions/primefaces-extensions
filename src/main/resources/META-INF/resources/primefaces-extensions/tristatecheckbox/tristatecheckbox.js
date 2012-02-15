/**
 * PrimeFaces Extensions TriStateCheckbox Widget
 */
PrimeFacesExt.widget.TriStateCheckbox = function(cfg) {
    this.cfg = cfg;
    this.id = this.cfg.id;
    this.jqId = PrimeFaces.escapeClientId(this.id);
    this.jq = $(this.jqId);
    this.input = $(this.jqId + '_input');
    this.box = this.jq.find('.ui-chkbox-box');
    this.icon = this.box.children('.ui-chkbox-icon');
    this.itemLabel = this.jq.find('.ui-chkbox-label');
    this.disabled = this.input.is(':disabled');
    
    var _self = this;

    //bind events if not disabled
    if(!this.disabled) {
        this.box.mouseover(function() {
            _self.box.addClass('ui-state-hover');
        }).mouseout(function() {
            _self.box.removeClass('ui-state-hover');
        }).click(function() {
            _self.toggle();
        });
        
        //toggle state on label click
        this.itemLabel.click(function() {
            _self.toggle();
        });
        
        //Client Behaviors
        if(this.cfg.behaviors) {
            PrimeFaces.attachBehaviors(this.input, this.cfg.behaviors);
        }
    }
    
    this.postConstruct();
}

PrimeFaces.extend(PrimeFacesExt.widget.TriStateCheckbox, PrimeFaces.widget.BaseWidget);

PrimeFacesExt.widget.TriStateCheckbox.prototype.toggle = function() {   
    
    if(!this.disabled) {
        var value = (this.input.attr('value')+1) % 3;        
        this.input.attr('value', value); 
        if(value==0){
            this.uncheck();
        }else if (value ==1){
            this.check();
        }else{
            this.tristate();
        }
    }
}

PrimeFacesExt.widget.TriStateCheckbox.prototype.check = function() {
    if(!this.disabled) {        
        this.box.removeClass('ui-state-active').children('.ui-chkbox-icon').removeClass('ui-icon ui-icon-closethick');
        this.box.addClass('ui-state-active').children('.ui-chkbox-icon').addClass('ui-icon ui-icon-check');
        this.input.change();
    }
}

PrimeFacesExt.widget.TriStateCheckbox.prototype.uncheck = function() {
    if(!this.disabled) {       
        this.box.removeClass('ui-state-active').children('.ui-chkbox-icon').removeClass('ui-icon ui-icon-check');
        this.box.removeClass('ui-state-active').children('.ui-chkbox-icon').removeClass('ui-icon ui-icon-closethick');        
        this.input.change();
    }
}

PrimeFacesExt.widget.TriStateCheckbox.prototype.tristate = function() {
    if(!this.disabled) {      
        this.box.removeClass('ui-state-active').children('.ui-chkbox-icon').removeClass('ui-icon ui-icon-check');   
        this.box.addClass('ui-state-active').children('.ui-chkbox-icon').addClass('ui-icon ui-icon-closethick');
        this.input.change();
    }
}


