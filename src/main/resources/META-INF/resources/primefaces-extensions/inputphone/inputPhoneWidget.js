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
        this.target = document.querySelector(PrimeFaces.escapeClientId(cfg.target));
        this.iti = intlTelInput(this.target, cfg);
        this.bindEvents();
    },
    
    bindEvents: function() {
        var $this = this;
        
        this.target.addEventListener('countrychange', function(){
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
    }

});
