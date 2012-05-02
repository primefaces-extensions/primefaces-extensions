/**
 * PrimeFaces Extensions TriStateManyCheckbox Widget.
 *
 * @author Mauricio Fenoglio
 */
PrimeFacesExt.widget.TriStateManyCheckbox = PrimeFaces.widget.BaseWidget.extend({

	/**
	 * Initializes the widget.
	 * 
	 * @param {object} cfg The widget configuration.
	 */
	init : function(cfg) {
	    this._super(cfg);

	    this.outputs = this.jq.find('.ui-chkbox-box:not(.ui-state-disabled)');
	    this.inputs = this.jq.find(':text:not(:disabled)');
	    this.labels = this.jq.find('label:not(.ui-state-disabled)');
	    var _self = this;
	
	    this.outputs.mouseover(function() {
	        $(this).addClass('ui-state-hover');
	    }).mouseout(function() {
	        $(this).removeClass('ui-state-hover');
	    }).click(function() {
	        _self.toggle($(this));
	    });
	
	    this.labels.click(function(e) {
	        var element = $(this),
	        input = $(PrimeFaces.escapeClientId(element.attr('for'))),
	        checkbox = input.parent().next();
	        checkbox.click();
	        
	        e.preventDefault();
	    });
	
	    //Client Behaviors
	    if (this.cfg.behaviors) {
	        PrimeFaces.attachBehaviors(this.inputs, this.cfg.behaviors);
	    }
        
        // pfs metadata
        this.inputs.data(PrimeFaces.CLIENT_ID_DATA, this.id);
	},
	
	toggle : function(checkbox) {
	    var inputField = checkbox.prev().find(':input');   
	    if (!checkbox.hasClass('ui-state-disabled')) {
	        var value = (inputField.attr('value')+1) % 3;        
	        inputField.attr('value', value); 
	        this.changeIcon(checkbox,value);
	        inputField.change();
	    }
	},

	changeIcon : function(checkbox, addIndex) {
        iconsClasses =  checkbox.attr('statesicons').split(';');
        //js bug for mod of negative values. fix with this!!!!!!!
        removeIndex = (((addIndex-1)%3)+3)%3;      
        removeClass = 'ui-icon ' + iconsClasses[removeIndex];
        if (iconsClasses[addIndex]!=" ") {
            addClass = 'ui-icon ' + iconsClasses[addIndex];
        } else{
            addClass = '';
        }

        //if addIndex is 0 , remove active class
        if (addIndex==0) {
            checkbox.removeClass('ui-state-active');
        } else {
            checkbox.addClass('ui-state-active')
        }
        //remove old icon and add the new one        
        checkbox.children('.ui-chkbox-icon').removeClass(removeClass);
        checkbox.children('.ui-chkbox-icon').addClass(addClass);          
	}
});
