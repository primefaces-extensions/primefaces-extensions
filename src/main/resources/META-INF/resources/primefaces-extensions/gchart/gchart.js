/**
 * Primefaces Extension GChart Widget
 *
 * @author f.strazzullo
 */
PrimeFacesExt.widget.GChart = PrimeFaces.widget.BaseWidget.extend({
	init : function(cfg) {

		var that = this;

		this._super(cfg);

		this.data = cfg.data ? JSON.parse(cfg.data) : [];
		this.type = cfg.type;
		this.height = cfg.height;
		this.width = cfg.width;
		this.title = cfg.title;
		this.options = cfg.options ? JSON.parse(cfg.options) : {};
		this.input = jQuery(this.jqId+"_hidden");
		
		google.load('visualization', '1.0', {
			'packages' : [ PrimeFacesExt.widget.GChart.packages[this.type] || 'corechart' ]
		});
		
		jQuery(document).ready(function(){
			if (google.visualization) {
				that.draw();
			}else{
				google.setOnLoadCallback(function() {
					that.draw();
				});
			}
		});
		
	},

	draw : function() {

		var dataTable = google.visualization.arrayToDataTable(this.data);

		var that = this;
		
		this.options.title = this.title;
		this.options.width = parseInt(this.width,10);
		this.options.height = parseInt(this.height,10);
		
		this.wrapper = new google.visualization.ChartWrapper({
			chartType : this.type,
			dataTable : dataTable,
			options : this.options,
			containerId : this.id
		});
		
		if(this.cfg.behaviors && this.cfg.behaviors.select) {
			google.visualization.events.addListener(this.wrapper, 'select', function(e){
				console.log(that.wrapper.getChart().getSelection());
				jQuery(that.jqId+"_hidden").val(JSON.stringify(that.wrapper.getChart().getSelection()));
				that.cfg.behaviors.select.call(jQuery(that.jqId+"_hidden"));
			});
		}

		this.wrapper.draw();

	}

});

PrimeFacesExt.widget.GChart.packages = {
		GeoChart: 'geochart',
		OrgChart: 'orgchart'
}