/**
 * PrimeFaces Extensions Org Chart Widget.
 *
 * @author jxmai
 * @since 6.3
 */
const PFE_ORGCHART_FILTER_DEFAULTS = Object.freeze({
    placeholder: 'Search by name or title',
    applyLabel: 'Filter',
    clearLabel: 'Clear',
    emptyAlert: 'Please type key word firstly.'
});

const PFE_ORGCHART_FILTER_ROLES = Object.freeze({
    input: 'filter-input',
    apply: 'filter-apply',
    clear: 'filter-clear'
});

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
        this.filterEventNamespace = '.pfeOrgChartFilter_' + this.id.replace(/[^A-Za-z0-9_]/g, '_');

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

        // Map parentNodeSymbol to icons, preserving all required icon classes.
        // For Font Awesome parent symbols, keep OCI as base theme class and provide FA classes per icon.
        // This preserves OCI compact corner toggles while rendering the rest with FA.
        // Otherwise, OCI (built-in) icons are used as the default theme.
        if (opts.parentNodeSymbol) {
            var isFa = opts.parentNodeSymbol.startsWith('fa-');
            var defaults = isFa ? {
                'theme': 'oci',
                'parentNode': 'fa ' + opts.parentNodeSymbol,
                'expandToUp': 'fa fa-chevron-up',
                'collapseToDown': 'fa fa-chevron-down',
                'collapseToLeft': 'fa fa-chevron-left',
                'expandToRight': 'fa fa-chevron-right',
                'backToCompact': 'oci-corner-top-left',
                'backToLoose': 'oci-corner-bottom-right',
                'collapsed': 'fa fa-plus-square',
                'expanded': 'fa fa-minus-square',
                'spinner': 'fa fa-spinner'
            } : {
                'theme': 'oci',
                'parentNode': opts.parentNodeSymbol,
                'expandToUp': 'oci-chevron-up',
                'collapseToDown': 'oci-chevron-down',
                'collapseToLeft': 'oci-chevron-left',
                'expandToRight': 'oci-chevron-right',
                'backToCompact': 'oci-corner-top-left',
                'backToLoose': 'oci-corner-bottom-right',
                'collapsed': 'oci-plus-square',
                'expanded': 'oci-minus-square',
                'spinner': 'oci-spinner'
            };
            opts.icons = $.extend({}, defaults, opts.icons);
        }

        this.orgchart = this.jq.orgchart(opts);

        this._bindEvents();
        this._bindFilterControls();
    }

    /**
     * Filters chart nodes by keyword using the same behavior as the official OrgChart filter-node demo.
     * Matching nodes and their ancestor chain are kept visible.
     *
     * @param {string} keyWord The keyword used to match node text.
     * @param {boolean} [showAlert=true] Whether to show an alert for empty keyword.
     * @returns {boolean} `true` if a filter was applied, otherwise `false`.
     */
    filterNodes(keyWord, showAlert = true) {
        var normalizedKeyword = (keyWord || '').trim().toLowerCase();
        if (!normalizedKeyword.length) {
            if (showAlert) {
                window.alert(PFE_ORGCHART_FILTER_DEFAULTS.emptyAlert);
            }
            return false;
        }

        this.clearFilter();

        var $chart = this.getOrgChartRoot();
        $chart.addClass('noncollapsable');

        $chart.find('.node').filter(function(index, node) {
            return $(node).text().toLowerCase().indexOf(normalizedKeyword) > -1;
        }).addClass('matched')
            .closest('.hierarchy').parents('.hierarchy').children('.node').addClass('retained');

        $chart.find('.matched,.retained').each(function(index, node) {
            $(node).removeClass('slide-up')
                .closest('.nodes').removeClass('hidden')
                .siblings('.hierarchy').removeClass('isChildrenCollapsed');

            var $unmatched = $(node).closest('.hierarchy').siblings().find('.node:first:not(.matched,.retained)')
                .closest('.hierarchy').addClass('hidden');

            $unmatched.parent('.nodes').siblings('.node').addClass('isSiblingsCollapsed');
        });

        $chart.find('.matched').each(function(index, node) {
            if (!$(node).siblings('.nodes').find('.matched').length) {
                $(node).siblings('.nodes').addClass('hidden')
                    .parent().addClass('isChildrenCollapsed');
            }
        });

        this._loopVisibleHierarchies($chart.find('.hierarchy:first'));
        return true;
    }

    /**
     * Clears node filtering results and restores the chart to its normal collapsible state.
     */
    clearFilter() {
        var $chart = this.getOrgChartRoot();
        $chart.removeClass('noncollapsable')
            .find('.node').removeClass('matched retained isSiblingsCollapsed')
            .end().find('.hidden, .isChildrenCollapsed, .first-shown, .last-shown')
            .removeClass('hidden isChildrenCollapsed first-shown last-shown')
            .end().find('.slide-up, .slide-left, .slide-right').removeClass('slide-up slide-right slide-left');
    }

    // @override
    refresh(cfg) {
        this._unbindFilterControls();
        super.refresh(cfg);
    }

    // @override
    destroy() {
        this._unbindFilterControls();
        super.destroy();
    }

    /**
     * Returns the inner chart root (`.orgchart`) used by the underlying plugin.
     *
     * @returns {JQuery} The chart root element.
     */
    getOrgChartRoot() {
        return this.jq.children('.orgchart').first();
    }

    _ensureFilterControls() {
        if (this.cfg.filterable !== true) {
            return null;
        }

        var inputId = this.id + '_filterInput';
        var filterButtonId = this.id + '_filterButton';
        var clearFilterButtonId = this.id + '_clearFilterButton';

        var $panel = this.jq.children('.pfe-orgchart-filter-controls');
        if (!$panel.length) {
            $panel = $('<div>', {
                'class': 'pfe-orgchart-filter-controls'
            }).prependTo(this.jq);
        }

        var inputRoleSelector = '[data-pfe-role="' + PFE_ORGCHART_FILTER_ROLES.input + '"]';
        var applyRoleSelector = '[data-pfe-role="' + PFE_ORGCHART_FILTER_ROLES.apply + '"]';
        var clearRoleSelector = '[data-pfe-role="' + PFE_ORGCHART_FILTER_ROLES.clear + '"]';

        var $input = $panel.children(inputRoleSelector);
        if (!$input.length) {
            $input = $('<input>', {
                id: inputId,
                type: 'text',
                'class': 'pfe-orgchart-filter-input',
                'data-pfe-role': PFE_ORGCHART_FILTER_ROLES.input,
                placeholder: PFE_ORGCHART_FILTER_DEFAULTS.placeholder
            }).appendTo($panel);
        }

        var $filterButton = $panel.children(applyRoleSelector);
        if (!$filterButton.length) {
            $filterButton = $('<button>', {
                id: filterButtonId,
                type: 'button',
                'class': 'pfe-orgchart-filter-button',
                'data-pfe-role': PFE_ORGCHART_FILTER_ROLES.apply,
                text: PFE_ORGCHART_FILTER_DEFAULTS.applyLabel
            }).appendTo($panel);
        }

        var $clearButton = $panel.children(clearRoleSelector);
        if (!$clearButton.length) {
            $clearButton = $('<button>', {
                id: clearFilterButtonId,
                type: 'button',
                'class': 'pfe-orgchart-filter-button',
                'data-pfe-role': PFE_ORGCHART_FILTER_ROLES.clear,
                text: PFE_ORGCHART_FILTER_DEFAULTS.clearLabel
            }).appendTo($panel);
        }

        return {
            filterInput: $input,
            filterButton: $filterButton,
            clearFilterButton: $clearButton
        };
    }

    _bindFilterControls() {
        var controls = this._ensureFilterControls();
        if (!controls) {
            return;
        }

        var $this = this;
        var filterInput = controls.filterInput;
        var filterButton = controls.filterButton;
        var clearFilterButton = controls.clearFilterButton;
        var filterOnEnter = true;
        var clearOnBackspace = true;

        if (!filterInput.length && !filterButton.length && !clearFilterButton.length) {
            return;
        }

        if (filterButton.length) {
            filterButton.off('click' + this.filterEventNamespace).on('click' + this.filterEventNamespace, function(event) {
                event.preventDefault();
                $this.filterNodes(filterInput.val(), true);
                return false;
            });
        }

        if (clearFilterButton.length) {
            clearFilterButton.off('click' + this.filterEventNamespace).on('click' + this.filterEventNamespace, function(event) {
                event.preventDefault();
                $this.clearFilter();
                if (filterInput.length) {
                    filterInput.val('');
                }
                return false;
            });
        }

        if (filterInput.length && (filterOnEnter || clearOnBackspace)) {
            filterInput.off('keyup' + this.filterEventNamespace).on('keyup' + this.filterEventNamespace, function(event) {
                if (filterOnEnter && event.which === 13) {
                    $this.filterNodes(this.value, false);
                    event.preventDefault();
                    return false;
                }

                if (clearOnBackspace && event.which === 8 && this.value.length === 0) {
                    $this.clearFilter();
                }

                return true;
            });
        }
    }

    _unbindFilterControls() {
        var $panel = this.jq.children('.pfe-orgchart-filter-controls');
        if (!$panel.length) {
            return;
        }

        $panel.children('[data-pfe-role]').off(this.filterEventNamespace);
    }

    _loopVisibleHierarchies($hierarchy) {
        if (!$hierarchy.length) {
            return;
        }

        var $siblings = $hierarchy.children('.nodes').children('.hierarchy');
        if ($siblings.length) {
            $siblings.filter(':not(.hidden)').first().addClass('first-shown')
                .end().last().addClass('last-shown');
        }

        var $this = this;
        $siblings.each(function(index, sibling) {
            $this._loopVisibleHierarchies($(sibling));
        });
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