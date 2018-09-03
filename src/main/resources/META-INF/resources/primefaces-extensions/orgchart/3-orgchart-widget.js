PrimeFaces.widget.ExtOrgChart = PrimeFaces.widget.BaseWidget.extend({

	/**
	 * Initializes the widget.
	 * 
	 * @param {object}
	 *            cfg The widget configuration.
	 */
	init : function(cfg) {
		this._super(cfg);
		this.id = cfg.id;
		var opts = $.extend(true, {}, cfg);

		opts['data'] = JSON.parse(opts['data']);

		this.jq.orgchart(opts);

		this._bindEvents();

	},
	_bindEvents : function() {
		var $this = this;

		this.jq.on('click', '.node', function() {
			var $thisNode = $(this);

			var behavior = $this.cfg.behaviors ? $this.cfg.behaviors['click']
					: null;

			if (behavior) {
				var options = {
					params : [ {
						name : $this.id + '_nodeId',
						value : $thisNode[0].id
					}, {
						name : $this.id + '_hierarchy',
						value : JSON.stringify($this.jq.orgchart('getHierarchy'))
					} ]

				};
				behavior.call($this, options);
			}
		});
		
		this.jq.children('.orgchart').on('nodedropped.orgchart', function(event) {
//			console.log('draggedNode:' + event.draggedNode.children('.title').text()
//			        + ', dragZone:' + event.dragZone.children('.title').text()
//			        + ', dropZone:' + event.dropZone.children('.title').text()
//			      );
			var behavior = $this.cfg.behaviors ? $this.cfg.behaviors['drop'] : null;
			
			if (behavior) {
				var options = {
					params : [ {
						name : $this.id + '_draggedNodeId',
						value : event.draggedNode['context']['id']
					}, {
						name : $this.id + '_droppedZoneId',
						value : event.dropZone['context']['id']
					}, {
						name : $this.id + '_hierarchy',
						value : JSON.stringify($this.jq.orgchart('getHierarchy'))
					} ]

				};
				behavior.call($this, options);
			}
			
		});
	}
});
