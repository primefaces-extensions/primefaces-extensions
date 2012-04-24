/**
 * PrimeFaces Extensions AjaxErrorHandler.
 *
 * @author Pavol Slany
 */

PrimeFacesExt.AjaxErrorHandler = function() {
    var defaultHostname = '???unknown???';
    var getDefaultErrorTime = function() {return new Date().toString();};

    PrimeFaces.ajax.AjaxResponse = function() {
        // backup original AjaxResponse function ...
        var backupAjaxResponse = PrimeFaces.ajax.AjaxResponse;

        return function() {
            var docPartialUpdate = arguments[0];
            var nodeErrors = docPartialUpdate.getElementsByTagName('error');
            if (nodeErrors && nodeErrors.length && nodeErrors[0].childNodes && nodeErrors[0].childNodes.length) {
                // XML => JSON
                var error = {};
                for (var i=0; i<nodeErrors[0].childNodes.length; i++) {
                    var node = nodeErrors[0].childNodes[i];
                    var key = node.nodeName;
                    var val = node.nodeValue;
                    if (node.childNodes && node.childNodes.length) {
                        val = node.childNodes[0].nodeValue;
                    }
                    error[key] = val;
                }

                if (error['error-name']) {
                    // findErrorSettings
                    var errorSetting = findErrorSettings(error['error-name']);
                    var retOnError = true;
                    if (errorSetting['onerror']) {
                        try {
                            var fun = errorSetting['onerror'];
                            if (typeof(fun) != 'function') {
                                fun = function(error, response) {
                                    return eval(errorSetting['onerror']);
                                };
                            };

                            fun.call(this, error, arguments[2]);
                        } catch(e) {}
                    }
                    if (retOnError !== false) {
                        // Copy updates to errorSettings ...
                        if (error.updateCustomContent && error.updateCustomContent.substring(-13)=='<exception />') error.updateCustomContent=null;
                        if (error.updateTitle && error.updateTitle.substring(-13)=='<exception />') error.updateTitle=null;
                        if (error.updateBody && error.updateBody.substring(-13)=='<exception />') error.updateBody= null;
                        if (error.updateViewState && error.updateViewState.substring(-13)=='<exception />') error.updateViewState= null;

                        errorSetting.updateCustomContent = error.updateCustomContent;
                        errorSetting.updateTitle = error.updateTitle;
                        errorSetting.updateBody = error.updateBody;
                        errorSetting.updateViewState = error.updateViewState;

                        var errorData = replaceVariables(errorSetting, error);

                        showPopupWindow(errorData);
                        return true;
                    }
                }
            }
            return backupAjaxResponse.apply(window, arguments);
        }
    }();

    var defSettings = {
        'title': '{error-name}',
        'body': '{error-message}',
        'buttonText': 'Reload',
        'buttonOnClick': function() {
            document.location.href = document.location.href;
        },
        'onerror': function(values) {}
    };
    var mainSettings = defSettings;
    var otherSettings = {};

    var findErrorSettings = function(name) {
        if (!name) return jQuery.extend({}, mainSettings);

        if (!otherSettings[name]) jQuery.extend({}, mainSettings);

        return jQuery.extend({}, mainSettings, otherSettings[name]);
    }
    var addErrorSettings = function(conf) {
        if (!conf) return;

        if (!conf.type) {
            mainSettings = jQuery.extend({},mainSettings,conf);
            return;
        }
        var def = otherSettings[conf.type] || {};
        otherSettings[conf.type] = jQuery.extend({}, def, conf);
    }



        var replaceAll = function(str, key, val) {
        var newStr;
        while ((newStr = str.replace(key, val))!=str) {str = newStr};
        return str;
    };


    var replaceVariables = function(obj, variables) {
        if (!obj) return text;

        variables = jQuery.extend({
            'error-hostname': defaultHostname,
            'error-stacktrace': '',
            'error-time': getDefaultErrorTime()
        },variables);

        var ret = {};
        jQuery.each(obj, function(key, val) {
            if (typeof(val) == 'string') {
                jQuery.each(variables, function(iVar, iVal) {
                    val = replaceAll(val, '{'+iVar+'}',iVal);
                });
            }
            ret[key] = val;
        });


        return ret;
    }

    var win =  null;
    var winMask = null;
    var winRecalcHeight = null;
    var showPopupWindow = function(errorData) {
        destroyPopupWindow();

        winMask = $('<div class="ui-widget-overlay"></div>').hide();
        win = $('<div class="ui-dialog ui-widget ui-widget-content ui-corner-all ui-shadow ui-overlay-visible"></div>').hide();
        $('body').append(winMask, win);

        if (errorData.updateCustomContent) {
            var elContent = $('<div></div>');
            win.append(elContent);
            elContent.replaceWith(errorData.updateCustomContent);
        }
        else {
            var htmlContent = '<div class="ui-dialog-content ui-widget-content"></div>';
            var htmlTitle = '<div class="ui-dialog-titlebar ui-widget-header ui-helper-clearfix ui-corner-top"></div>';
            var htmlTitleText = '<span class="ui-dialog-title"></span>';


            var button = $('<button class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only"><span class="ui-button-text">'+errorData['buttonText']+'</span></button>');
            var funOnClick = errorData['buttonOnClick'];
            button.click(function() {
                if (typeof(funOnClick) == 'function') funOnClick.call(this);
                else eval(funOnClick);
            });

            var elTitle = $('<a />');
            var elBody = $('<a />');
            win.append($(htmlTitle).append($(htmlTitleText).append(elTitle)));
            win.append($(htmlContent).append($('<div></div>').append(elBody)), $('<div class="pe-error-buttons"></div>').append(button));

            // setup draggable
            win.draggable({
                cancel: '.ui-dialog-content, .ui-dialog-titlebar-close',
                handle: '.ui-dialog-titlebar',
                containment : 'document'
            });

            elTitle.replaceWith(errorData.updateTitle || errorData.title);
            elBody.replaceWith(errorData.updateBody || errorData.body);
        }



        var zIndex = 999998;
        winMask.css('zIndex', zIndex++);
        win.css('zIndex', zIndex);

        win.css({'position':'fixed', 'margin-left':'auto', 'margin-right':'auto', 'overflow':'hidden'});
        winMask.css({'position':'fixed', 'left':0, 'top':0});
        winRecalcHeight = function() {
            if (winMask == null && win==null) return;

            var height = $(window).outerHeight();
            var width = $(window).outerWidth();
            winMask.css('width',width);
            winMask.css('height', width);

            var winCss = {};
            winCss.left = (width - win.outerWidth()) / 2;
            winCss.top = (height - win.outerHeight()) / 2;
            if (winCss.left<0) winCss.left=0;
            if (winCss.top<0) winCss.top=0;
            win.css(winCss);
        };
//        $(window).resize(winRecalcHeight);
        winRecalcHeight();
        win.show();
        winMask.show();
        winRecalcHeight();
    };
    var destroyPopupWindow = function () {
        if (winMask) winMask.remove();
        if (win) win.remove();
//        if (winRecalcHeight) $(window).unbind('resize', winRecalcHeight);
        winMask = null;
        win = null;
        winRecalcHeight = null;
    };
    var isVisible = function() {
        return win && win.isVisible();
    }

    return {
        addErrorSettings: function(conf) {
            addErrorSettings(conf);
            return PrimeFacesExt.AjaxErrorHandler;
        },
        hide: destroyPopupWindow,
        isVisible : isVisible,
        setHostname: function(hostname) {
            defaultHostname = hostname;
        }
    };
}();
