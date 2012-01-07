PrimeFacesExt.widget.Layout = function(id, cfg) {
    var config = cfg;
    var clientId = id;
    var jqId = PrimeFaces.escapeClientId(id);
    var indexTab = cfg.indexTab;
    var northSize = cfg.northSize;
    var tabLayoutOpt = cfg.tabLayoutOpt;
    var centerLayoutOpt = cfg.centerLayoutOpt;
    var westLayoutOpt = cfg.westLayoutOpt;
    var eastLayoutOpt = cfg.eastLayoutOpt;
    var jqTarget = $(cfg.forTarget);

    var _self = this;

    var defaultLayoutSettings = {
        onopen: function(panename, pane, state, options) {
            _self.onopen(options.paneposition);
        }
        , onclose: function(panename, pane, state, options) {
            _self.onclose(options.paneposition);
        }
        , onresize: function(panename, pane, state, options) {
            _self.onresize(options.paneposition, state);
        }
        , slidable: false
        , spacing: 6
        , contentSelector: '.ui-layout-pane-content'
        , togglerTip_open: cfg.togglerTipOpen
        , togglerTip_closed: cfg.togglerTipClose
        , resizerTip: cfg.resizerTip
    };

    var jtOuterLayout;
    var jtTabsContainerLayout;
    var jtTabLayout;
    var jtLayoutTabsLoading = true;

    /* public access */

    this.buildOuterTabsLayout = function() {
        jtOuterLayout = jqTarget.layout({
            resizeWithWindowDelay: 250
            , resizable: false
            , slidable: false
            , closable: false
            , north__paneSelector: jqId + "-layout-outer-north"
            , center__paneSelector: jqId + "-layout-outer-center"
            , north__size: northSize
            , north__spacing: 0
        });

        jtTabsContainerLayout = $(jqId + "-layout-outer-center").layout({
            resizable: false
            , slidable: false
            , closable: false
            , north__paneSelector: jqId + "-layout-tabbuttons"
            , center__paneSelector: jqId + "-layout-tabpanels"
            , spacing_open: 0
            , center__onresize: resizeTabPanelLayout
        });

        if (indexTab < 0) {
            // no tabs
            resizeOrCreateLayout();
            return;
        }

        jtOuterLayout.panes.center.tabs({
            create: function (evt, ui) {
                $(jqId + "-layout-tabbuttons").find(".ui-tab > a[href^='#']").attr('href', function(i, oldHref) {
                    return oldHref.substring(1);
                });
            },
            show: function (evt, ui) {
                resizeOrCreateLayout();
            },
            select: function(evt, ui) {
                location.href = ui.tab.href;
                return false;
            },
            selected: indexTab
        });
    }

    this.onopen = function(paneposition) {
        var behavior = config.behaviors['open'];
        if (behavior) {
            var ext = {
                params: {}
            };
            ext.params[clientId + '_pane'] = paneposition;
            behavior.call(this, paneposition, ext);
        }
    }

    this.onclose = function(paneposition) {
        var behavior = config.behaviors['close'];
        if (behavior) {
            var ext = {
                params: {}
            };
            ext.params[clientId + '_pane'] = paneposition;
            behavior.call(this, paneposition, ext);
        }
    }

    this.onresize = function(paneposition, state) {
        if (!state.isClosed && !state.isHidden) {
            var behavior = config.behaviors['resize'];
            if (behavior) {
                var ext = {
                    params : {}
                };
                ext.params[clientId + '_pane'] = paneposition;
                ext.params[clientId + '_width'] = state.innerWidth;
                ext.params[clientId + '_height'] = state.innerHeight;
                behavior.call(this, paneposition, ext);
            }
        }
    }

    this.allowOverflow = function(pane) {
        if (pane === 'north') {
            jtOuterLayout.allowOverflow('north');
        } else if (pane === 'center' && jtTabLayout) {
            jtTabLayout.panes.center.allowOverflow('center');
        } else if (pane === 'west' && jtTabLayout) {
            jtTabLayout.panes.west.allowOverflow('west');
        } else if (pane === 'east' && jtTabLayout) {
            jtTabLayout.panes.east.allowOverflow('east');
        } else if (pane === 'south' && jtTabLayout) {
            jtTabLayout.panes.south.allowOverflow('south');
        }
    }

    this.resetOverflow = function(pane) {
        if (pane === 'north') {
            jtOuterLayout.resetOverflow('north');
        } else if (pane === 'center' && jtTabLayout) {
            jtTabLayout.panes.center.resetOverflow('center');
        } else if (pane === 'west' && jtTabLayout) {
            jtTabLayout.panes.west.resetOverflow('west');
        } else if (pane === 'east' && jtTabLayout) {
            jtTabLayout.panes.east.resetOverflow('east');
        } else if (pane === 'south' && jtTabLayout) {
            jtTabLayout.panes.south.resetOverflow('south');
        }
    }

    /* private access */

    var resizeOrCreateLayout = function() {
        if (jtLayoutTabsLoading) {
            jtLayoutTabsLoading = false;
            jtTabsContainerLayout.resizeAll();
        }
        // resize OR create the tab-layout
        resizeTabPanelLayout();
        jtOuterLayout.panes.center.show();
    }

    var resizeTabPanelLayout = function() {
        var jtLayoutTabPanel = $(jqId + "-layout-tabpanels").find(".ui-layout-tab");

        if (jtLayoutTabPanel.data("layoutContainer")) {
            jtLayoutTabPanel.layout().resizeAll();
        } else {
            jtTabLayout = jtLayoutTabPanel.layout($.extend({}, defaultLayoutSettings, tabLayoutOpt));
            if (jtTabLayout == null) {
                return;
            }

            if (jtTabLayout.panes.west && westLayoutOpt != null) {
                jtTabLayout.panes.west.layout($.extend({}, defaultLayoutSettings, westLayoutOpt));
            }
            if (jtTabLayout.panes.east && eastLayoutOpt != null) {
                jtTabLayout.panes.east.layout($.extend({}, defaultLayoutSettings, eastLayoutOpt));
            }
            if (centerLayoutOpt != null) {
                jtTabLayout.panes.center.layout($.extend({}, defaultLayoutSettings, centerLayoutOpt));
            }
        }
    }

    if (jqTarget.is(':visible')) {
        this.buildOuterTabsLayout();
    } else {
        var hiddenParent = jqTarget.parents('.ui-hidden-container:first'),
                hiddenParentWidget = hiddenParent.data('widget');

        if (hiddenParentWidget) {
            hiddenParentWidget.addOnshowHandler(function() {
                return _self.buildOuterTabsLayout();
            });
        }
    }

    this.postConstruct();
}

PrimeFaces.extend(PrimeFacesExt.widget.Layout, PrimeFaces.widget.BaseWidget);
