/**
 * PrimeFaces Extensions TriStateCheckbox Widget.
 *
 * @author Mauricio Fenoglio
 */
PrimeFacesExt.widget.TriStateCheckbox = PrimeFaces.widget.BaseWidget.extend({

	/**
	 * Initializes the widget.
	 * 
	 * @param {object} cfg The widget configuration.
	 */
	init : function(cfg) {
	    this._super(cfg);

	    this.input = $(this.jqId + '_input');
	    this.box = this.jq.find('.ui-chkbox-box');
	    this.icon = this.box.children('.ui-chkbox-icon');
	    this.itemLabel = this.jq.find('.ui-chkbox-label');
	    this.disabled = this.input.is(':disabled');
	    
	    var _self = this;
	
	    //bind events if not disabled
	    if (!this.disabled) {
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
        
        // pfs metadata
        this.inputs.data(PrimeFaces.CLIENT_ID_DATA, this.id);
	},

	toggle : function() {   
	    if (!this.disabled) {
	        var value = (this.input.attr('value')+1) % 3;        
	        this.input.attr('value', value); 
	        this.changeIcon(value);
	        this.input.change();
	    }
	},

	changeIcon : function(addIndex) {
	    if (!this.disabled) {               
	        iconsClasses =  this.box.attr('statesicons').split(';');

	        //js bug for mod of negative values. fix with this!!!!!!!
	        removeIndex = (((addIndex-1)%3)+3)%3;      
	        removeClass = 'ui-icon ' + iconsClasses[removeIndex];
	        if(iconsClasses[addIndex]!=" ") {
	            addClass = 'ui-icon ' + iconsClasses[addIndex];
	        } else {
	            addClass = '';
	        }

	        //if addIndex is 0 , remove active class
	        if (addIndex==0) {
	            this.box.removeClass('ui-state-active');
	        } else{
	            this.box.addClass('ui-state-active')
	        }

	        //remove old icon and add the new one        
	        this.box.children('.ui-chkbox-icon').removeClass(removeClass);
	        this.box.children('.ui-chkbox-icon').addClass(addClass);       
	    }
	}
});
