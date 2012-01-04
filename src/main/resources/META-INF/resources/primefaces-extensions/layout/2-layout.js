
PrimeFacesExt.widget.Layout = function(id, cfg) {
	var clientId = PrimeFaces.escapeClientId(id);
	var indexTab = cfg.indexTab;
	var northSize = cfg.northSize;
	var tabLayoutOpt = cfg.tabLayoutOpt;
	var centerLayoutOpt = cfg.centerLayoutOpt;
	var westLayoutOpt = cfg.westLayoutOpt;
	var eastLayoutOpt = cfg.eastLayoutOpt;
    var forTarget = cfg.forTarget; 
	
	var defaultLayoutSettings = {
		  slidable: false
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
	
	this.buildOuterTabsLayout = function () {
    	jtOuterLayout = $(forTarget).layout({
    		  resizeWithWindowDelay: 250
    		, resizable: false
    		, slidable:	false
    		, closable:	false
    		, north__paneSelector: clientId + "-layout-outer-north"
    		, center__paneSelector: clientId + "-layout-outer-center"
    		, north__size: northSize
    		, north__spacing: 0
    	});

    	jtTabsContainerLayout = $(clientId + "-layout-outer-center").layout({
    		  resizable: false
    		, slidable:	false
    		, closable:	false
    		, north__paneSelector: clientId + "-layout-tabbuttons"
    		, center__paneSelector: clientId + "-layout-tabpanels"
    		, spacing_open:	0
    		, center__onresize: resizeTabPanelLayout
    	});
    	
    	if (indexTab < 0) {
    		// no tabs
    		resizeOrCreateLayout();
    		return;
    	}
    	
    	jtOuterLayout.panes.center.tabs({
    		create: function (evt, ui) {
    			$(clientId + "-layout-tabbuttons").find(".ui-tab > a[href^='#']").attr('href',
    				function(i, oldHref) {
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
	
	this.allowOverflow = function (pane) {
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
	
	this.resetOverflow = function (pane) {
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
	
	var resizeOrCreateLayout = function () {
    	if (jtLayoutTabsLoading) {
    		jtLayoutTabsLoading = false;
    		jtTabsContainerLayout.resizeAll();
    	}
    	// resize OR create the tab-layout
    	resizeTabPanelLayout();
    	jtOuterLayout.panes.center.show();    	
    }
	
	var resizeTabPanelLayout = function () {
		var jtLayoutTabPanel = $(clientId + "-layout-tabpanels").find(".ui-layout-tab");
    	
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
    
    this.postConstruct();
}

PrimeFaces.extend(PrimeFacesExt.widget.Layout, PrimeFaces.widget.BaseWidget);
