/**
 * PrimeFaces Extensions Org Chart Widget.
 *
 * @author jxmai
 * @since 6.3
 */
PrimeFaces.widget.ExtOrgChart = class extends PrimeFaces.widget.BaseWidget {

    /**
     * Initializes the widget.
     *
     * @param {object}
     *        cfg The widget configuration.
     */
    init(cfg) {
        super.init(cfg);
        this.id = cfg.id;

        // user extension to configure plugin
        var extender = this.cfg.extender;
        if (extender) {
            if (typeof extender === "function") {
                extender.call(this);
            } else {
                PrimeFaces.error("Extender value is not a javascript function!");
            }
        }

        var opts = $.extend(true, {}, cfg);
        opts['data'] = JSON.parse(opts['data']);

        // Map parentNodeSymbol to icons.parentNode
        if (opts.parentNodeSymbol) {
            opts.icons = opts.icons || {};
            opts.icons.parentNode = opts.parentNodeSymbol;
            // Set theme based on icon prefix
            if (opts.parentNodeSymbol.startsWith('fa-')) {
                opts.icons.theme = 'fa';
            } else if (opts.parentNodeSymbol.startsWith('oci-')) {
                opts.icons.theme = 'oci';
            }
        }

        this.orgchart = this.jq.orgchart(opts);

        this._bindEvents();
    }

    _bindEvents() {
        var $this = this;

        this.jq.on('click', '.node', function () {
            var $thisNode = $(this);

            var options = {
                params: [{
                    name: $this.id + '_nodeId',
                    value: $thisNode[0].id
                }, {
                    name: $this.id + '_hierarchy',
                    value: JSON.stringify($this.orgchart.getHierarchy())
                }]

            };
            $this.callBehavior('click', options);
        });

        this.jq.children('.orgchart').on('nodedropped.orgchart', function (event) {
            var options = {
                params: [{
                    name: $this.id + '_draggedNodeId',
                    value: event.draggedNode['context']['id']
                }, {
                    name: $this.id + '_droppedZoneId',
                    value: event.dropZone['context']['id']
                }, {
                    name: $this.id + '_hierarchy',
                    value: JSON.stringify($this.orgchart.getHierarchy())
                }]

            };
            $this.callBehavior('drop', options);

        });
    }
};