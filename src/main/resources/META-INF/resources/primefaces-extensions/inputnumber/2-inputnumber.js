/**
 * PrimeFaces Extensions InputNumber Widget.
 *
 * @author Mauricio Fenoglio
 */
PrimeFacesExt.widget.InputNumber = PrimeFaces.widget.BaseWidget.extend({

    /**
	 * Initializes the widget.
	 * 
	 * @param {object} cfg The widget configuration.
	 */
    init : function(cfg) {
        this._super(cfg);
        this.inputInternal = $(this.jqId + '_hinput');
        this.inputExternal = $(this.jqId + '_input');
        this.plugOptArray =   cfg.pluginOptions;
        this.valueToRender =  cfg.valueToRender;
        this.disabled = cfg.disabled;      
                
        var $this = this;             

        //bind events if not disabled
        if (this.disabled) {
            this.inputExternal.attr("disabled", "disabled");
            this.inputExternal.addClass("ui-state-disabled");
            this.inputInternal.attr("disabled", "disabled");
        }
        
        //Visual effects
        PrimeFaces.skinInput(this.inputExternal);
        
        //copy to hidden input the cleaned value
        this.inputExternal.on('change', function(event) {
            cleanVal = $this.inputExternal.autoNumeric('get');
            if(cleanVal != ""){
                $this.inputInternal.attr('value', cleanVal);
            }else{
                $this.inputInternal.removeAttr('value');
            }
        });
                
        this.inputExternal.autoNumeric('init', this.plugOptArray);
                       
        if (this.valueToRender != ""){
            //set the value to the external input the plugin will format it.                 
            this.inputExternal.autoNumeric('set',this.valueToRender);                     
            //then copie the value to the internal input
            cleanVal = $this.inputExternal.autoNumeric('get');              
            $this.inputInternal.attr('value', cleanVal);
        }else{
            $this.inputInternal.removeAttr('value');
        }
    },
        
    enable : function() {
        this.inputExternal.removeAttr("disabled");
        this.inputExternal.removeClass("ui-state-disabled");
        this.inputInternal.removeAttr("disabled");
        this.disabled = false;
    },

    disable : function() {
        this.inputExternal.attr("disabled", "disabled");
        this.inputExternal.addClass("ui-state-disabled");
        this.inputInternal.attr("disabled", "disabled");
        this.disabled = true;
    },
            
    setValue : function(value){                
        this.inputExternal.autoNumeric('set', value);                  
        cleanVal = this.inputExternal.autoNumeric('get');              
        this.inputInternal.attr('value', cleanVal);
    },
    
    getValue : function(){
         return  this.inputExternal.autoNumeric('get'); 
    }   
});
