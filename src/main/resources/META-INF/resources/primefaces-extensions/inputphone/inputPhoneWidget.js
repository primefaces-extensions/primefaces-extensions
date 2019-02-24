/**
 * PrimeFaces Extensions InputPhone Widget.
 * 
 * @author Jasper de Vries jepsar@gmail.com
 */
PrimeFaces.widget.ExtInputPhone = PrimeFaces.widget.BaseWidget.extend({

    /**
     * Initializes the widget.
     * 
     * @param {object}
     *        cfg The widget configuration.
     */
    init : function(cfg) {
        this._super(cfg);
        this.id = cfg.id;
        this.cfg = cfg;
        this.input = document.querySelector(this.jqId);
        this.iti = intlTelInput(this.input, cfg);

        PrimeFaces.skinInput($(this.jqId));

        this.bindEvents();
    },
    
    bindEvents: function() {
        var $this = this;
        
        this.input.addEventListener('countrychange', function(){
          if ($this.hasBehavior('countrySelect')) {
              var country = $this.iti.getSelectedCountryData();
              var ext = {
                  params: [
                      {name: $this.id + '_name', value: country.name},
                      {name: $this.id + '_iso2', value: country.iso2},
                      {name: $this.id + '_dialCode', value: country.dialCode}
                  ]
              };
              $this.callBehavior('countrySelect', ext);
          }          
        });
    },

    refresh: function(cfg) {
        if (this.iti) {
            this.iti.destroy();
        }
        this._super(cfg);
    },

    destroy: function() {
        this._super();
        if (this.iti) {
            this.iti.destroy();
        }
    }

});
