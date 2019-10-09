/**
 * PrimeFaces Extensions TimePicker Widget.
 *
 * @author Oleg Varaksin
 */
PrimeFaces.widget.ExtTimePicker = PrimeFaces.widget.BaseWidget.extend({

	/**
	 * Initializes the widget.
	 *
	 * @param {object} cfg The widget configuration.
	 */
	init : function(cfg) {
	    this.id = cfg.id;
	    this.jqId = PrimeFaces.escapeClientId(cfg.id);
	    this.container = $(this.jqId);
	    this.jqEl = cfg.modeInline ? this.jqId + '_inline' : this.jqId + '_input';
	    this.jq = $(this.jqEl);

	    // configure localized text
	    this.cfg = PrimeFacesExt.configureLocale('TimePicker', cfg);

	    // GitHub #723: Fix for PrimeIcon themes
	    if (PrimeFacesExt.isPrimeIconTheme()) {
	        this.container.addClass('ui-prime-icons');
	    }

	    if (this.cfg.showPeriod) {
	        this.amHours = {};
	        this.pmHours = {};
	        var i, prev, next;

	        if (this.cfg.hours.starts <= 11) {
	            // fill AM hours
	            var hoursEnds = Math.min(11, this.cfg.hours.ends);
	            for (i=this.cfg.hours.starts; i<=hoursEnds; i++) {
	                prev = null;
                    next = null;
	                if (i == this.cfg.hours.starts) {
	                    prev = null;
	                } else {
	                    prev = i - 1;
	                    if (prev == 0) {
	                        prev = 12;
	                    }
	                }

	                if (i == hoursEnds) {
	                    next = null;
	                } else {
	                    next = i + 1;
	                }

	                this.amHours[i == 0 ? 12 : i] = {'prev': prev, 'next': next};
	            }
	        }

	        if (this.cfg.hours.ends >= 12) {
	            // fill PM hours
	            for (i=12; i<=this.cfg.hours.ends; i++) {
	                prev = null;
                    next = null;
	                if (i == 12) {
	                    prev = null;
	                } else {
	                    prev = i - 1;
	                    if (prev != 12) {
	                        prev = prev - 12;
	                    }
	                }

	                if (i < this.cfg.hours.ends) {
	                    next = i - 11;
	                } else {
	                    next = null;
	                }

	                this.pmHours[i > 12 ? i-12 : i] = {'prev': prev, 'next': next};
	            }
	        }
	    }

	    // for internal use
	    var _self = this;

	    // extend configuration
	    if (this.cfg.modeInline) {
	        this.cfg.altField = this.jqId + '_input';
	    }

        if (!this.cfg.modeInline) {
            this.cfg.beforeShow = function(input, inst) {
                setTimeout(function() {
                    $('#ui-timepicker-div').css('z-index', ++PrimeFaces.zindex);
                }, 250);

       	        _self.onbeforeShow();
       	    };
        } else {
            this.cfg.beforeShow = function(input, inst) {
       	        _self.onbeforeShow();
       	    };
        }

	    this.cfg.onClose = function(time, inst) {
	        _self.onclose();
	    };

	    this.cfg.onSelect = function(time, inst) {
	        if (_self.cfg.modeInline) {
	            $(_self.cfg.altField).val(time);
	        } else {
	            _self.jq.val(time);
	        }
	        _self.ontimeSelect();
	    };

	    // create timepicker
	    this.jq.fgtimepicker(this.cfg);

	    if (this.cfg.disabled) {
	        this.disable();
	    } else {
            this.enable();
        }

	    if (this.cfg.modeSpinner && !this.cfg.disabled) {
	        this.enableSpinner();
	    }

	    // skin input
	    if (!this.cfg.modeInline) {
	        this.enableInput();
	    }

	    // client behaviors
	    if (this.cfg.behaviors) {
	        PrimeFaces.attachBehaviors(this.jq, this.cfg.behaviors);
	    }

        // pfs metadata
        $(this.jqId + '_input').data(PrimeFaces.CLIENT_ID_DATA, this.id);

        this.removeScriptElement(this.id);

        this.originalValue = this.jq.val();
	},

	enableInput : function() {
	    var _self = this;

	    PrimeFaces.skinInput(this.jq);

	    this.jq.on({
	        keydown: function(e){
	            var keyCode = $.ui.keyCode;

	            switch(e.which) {
	                case keyCode.UP:
	                    _self.spin(1);
	                break;

	                case keyCode.DOWN:
	                    _self.spin(-1);
	                break;

	                default:
	                    //do nothing
	                break;
	            }
	        },
	        keyup: function(e){
	            clearInterval(_self.spinnerInterval);
	        },
	        mousewheel: function(event, delta) {
	            if(_self.jq.is(':focus')) {
	                if(delta > 0)
	                    _self.spin(1);
	                else
	                    _self.spin(-1);

	                clearInterval(_self.spinnerInterval);
	                return false;
	            }
	        }
	    });
	},

	enableSpinner : function() {
	    var _self = this;

	    $(this.jqId).children('.ui-spinner-button')
                .removeClass('ui-state-disabled')
                .off('mouseover mouseout mouseup mousedown')
                .on({
                    mouseover: function(){
                        $(this).addClass('ui-state-hover');
                    },
                    mouseout: function(){
                        $(this).removeClass('ui-state-hover');

                        clearInterval(_self.spinnerInterval);
                    },
                    mouseup: function(){
                        $(this).removeClass('ui-state-active');

                        clearInterval(_self.spinnerInterval);
                    },
                    mousedown: function(e){
                        var el = $(this);
                        el.addClass('ui-state-active');

                        var dir = el.hasClass('ui-spinner-up') ? 1 : -1;
                        _self.spin(dir);

                        _self.spinnerInterval = setInterval(function() {
                            _self.spin(dir);
                        }, 200);

                        e.preventDefault();
                    }
                });
	},

	disableSpinner : function() {
	    $(this.jqId).children('.ui-spinner-button').
	    removeClass('ui-state-hover ui-state-active').
	    addClass('ui-state-disabled').
	    off('mouseover mouseout mouseup mousedown');
	},

	spin : function(dir) {
	    var time = this.jq.val();
	    if (!time) {
            // if the value is empty, set 00:00 and process with spinning
	        this.jq.val('00:00');
	    }

	    var newTime = null;
	    var hours = null;
	    var minutes = null;
	    var objHours, strHours, strMinutes, result, prevHours;
	    var isAmPmChanged = false;

	    if (this.cfg.showHours && this.cfg.showMinutes) {
	        // extract hours and minutes
	        result = time.match(new RegExp('^(\\d{2})' + this.cfg.timeSeparator + '(\\d{2})((?:\\s*)|(?:\\s{1}.*))$'));
	        if (result && result.length >= 3) {
	            if (result[1].charAt(0) == '0') {
	                hours = parseInt(result[1].charAt(1));
	            } else {
	                hours = parseInt(result[1]);
	            }

	            if (result[2].charAt(0) == '0') {
	                minutes = parseInt(result[2].charAt(1));
	            } else {
	                minutes = parseInt(result[2]);
	            }
	        }

	        if (minutes == null) {
	            return;
	        }

	        // increment / decrement minutes and hours
	        if (dir == 1) {
	            minutes = minutes + this.cfg.minutes.interval;
	            minutes = Math.floor(minutes / this.cfg.minutes.interval) * this.cfg.minutes.interval;
	        } else {
	            minutes = minutes - this.cfg.minutes.interval;
	            minutes = Math.ceil(minutes / this.cfg.minutes.interval) * this.cfg.minutes.interval;
	        }

	        if (minutes > this.cfg.minutes.ends) {
	            objHours = this.increaseHour(hours, this.isAm(time));
	            hours = objHours['newHour'];
	            isAmPmChanged = objHours['isAmPmChanged'];
	            minutes = this.cfg.minutes.starts;
	        } else if (minutes < this.cfg.minutes.starts) {
	            objHours = this.decreaseHour(hours, this.isAm(time));
	            hours = objHours['newHour'];
	            isAmPmChanged = objHours['isAmPmChanged'];
	            minutes = this.cfg.minutes.ends;
	        }

	        // replace old time by new one
	        strHours = (hours < 10 ? "0" + hours : "" + hours);
	        strMinutes = (minutes < 10 ? "0" + minutes : "" + minutes);
	        newTime = time.replace(new RegExp('^(\\d{2})' + this.cfg.timeSeparator + '(\\d{2})'), strHours + this.cfg.timeSeparator + strMinutes);
	        newTime = this.adjustAmPm(newTime, isAmPmChanged);

	    } else if (this.cfg.showHours && !this.cfg.showMinutes) {
	        // only hours
	        result = time.match(new RegExp('^(\\d{2})((?:\\s*)|(?:\\s{1}.*))$'));
	        if (result && result.length >= 2) {
	            if (result[1].charAt(0) == '0') {
	                hours = parseInt(result[1].charAt(1));
	            } else {
	                hours = parseInt(result[1]);
	            }
	        }

	        if (hours == null) {
	            return;
	        }

	        // increment / decrement hours
	        if (dir == 1) {
	            objHours = this.increaseHour(hours, this.isAm(time));
	            hours = objHours['newHour'];
	            isAmPmChanged = objHours['isAmPmChanged'];
	        } else {
	            objHours = this.decreaseHour(hours, this.isAm(time));
	            hours = objHours['newHour'];
	            isAmPmChanged = objHours['isAmPmChanged'];
	        }

	        // replace old time by new one
	        strHours = (hours < 10 ? "0" + hours : "" + hours);
	        newTime = time.replace(new RegExp('^(\\d{2})'), strHours);
	        newTime = this.adjustAmPm(newTime, isAmPmChanged);

	    } else if (!this.cfg.showHours && this.cfg.showMinutes) {
	        // only minutes
	        result = time.match(new RegExp('^(\\d{2})(?:\\s*)$'));
	        if (result && result.length >= 2) {
	            if (result[1].charAt(0) == '0') {
	                minutes = parseInt(result[1].charAt(1));
	            } else {
	                minutes = parseInt(result[1]);
	            }
	        }

	        if (minutes == null) {
	            return;
	        }

	        // increment / decrement minutes and hours
	        if (dir == 1) {
	            minutes = minutes + this.cfg.minutes.interval;
	            minutes = Math.floor(minutes / this.cfg.minutes.interval) * this.cfg.minutes.interval;
	        } else {
	            minutes = minutes - this.cfg.minutes.interval;
	            minutes = Math.ceil(minutes / this.cfg.minutes.interval) * this.cfg.minutes.interval;
	        }

	        if (minutes > this.cfg.minutes.ends) {
	            minutes = this.cfg.minutes.starts;
	        } else if (minutes < this.cfg.minutes.starts) {
	            minutes = this.cfg.minutes.ends;
	        }

	        // replace old time by new one
	        strMinutes = (minutes < 10 ? "0" + minutes : "" + minutes);
	        newTime = time.replace(new RegExp('^(\\d{2})'), strMinutes);
	    }

	    if (newTime != null) {
	        this.jq.val(newTime);
	        this.jq.attr('aria-valuenow', newTime);
	    }
	},

	onbeforeShow : function() {
	    this.callBehavior('beforeShow');
	},

	onclose : function() {
	    this.callBehavior('close');
	},

	ontimeSelect : function() {
	    this.callBehavior('timeSelect');
	},

	isAm : function(time) {
	    var am = this.cfg.amPmText[0];
	    return (time && time.indexOf(am) != -1);
	},

	adjustAmPm : function(time, isAmPmChanged) {
	    if (isAmPmChanged) {
	        var am = this.cfg.amPmText[0];
	        var pm = this.cfg.amPmText[1];
	        if (time.indexOf(am) != -1) {
	            return time.replace(am, pm);
	        } else if (time.indexOf(pm) != -1) {
	            return time.replace(pm, am);
	        }
	    }

	    return time;
	},

	increaseHour : function(hour, isAm) {
	    var newHour;
	    var isAmPmChanged = false;

	    if (this.cfg.showPeriod) {
	        var timeObj, curTimeObj, curHour;
	        if (isAm) {
	            timeObj = this.amHours[hour];
	            if (timeObj && timeObj.next) {
	                newHour = timeObj.next;
	            } else {
	                for(curHour in this.pmHours) {
	                    curTimeObj = this.pmHours[curHour];
	                    if (curTimeObj.prev == null) {
	                        newHour = curHour;
	                        isAmPmChanged = true;
	                        break;
	                    }
	                }
	            }
	        } else {
	            timeObj = this.pmHours[hour];
	            if (timeObj && timeObj.next) {
	                newHour = timeObj.next;
	            } else {
	                for(curHour in this.amHours) {
	                    curTimeObj = this.amHours[curHour];
	                    if (curTimeObj.prev == null) {
	                        newHour = curHour;
	                        isAmPmChanged = true;
	                        break;
	                    }
	                }
	            }
	        }
	    } else {
	        newHour = hour + 1;
	        if (newHour > this.cfg.hours.ends) {
	            newHour = this.cfg.hours.starts;
	        }
	    }

	    return {'newHour': newHour, 'isAmPmChanged': isAmPmChanged};
	},

	decreaseHour : function(hour, isAm) {
	    var newHour;
	    var isAmPmChanged = false;

	    if (this.cfg.showPeriod) {
	        var timeObj, curTimeObj, curHour;
	        if (isAm) {
	            timeObj = this.amHours[hour];
	            if (timeObj && timeObj.prev) {
	                newHour = timeObj.prev;
	            } else {
	                for(curHour in this.pmHours) {
	                    curTimeObj = this.pmHours[curHour];
	                    if (curTimeObj.next == null) {
	                        newHour = curHour;
	                        isAmPmChanged = true;
	                        break;
	                    }
	                }
	            }
	        } else {
	            timeObj = this.pmHours[hour];
	            if (timeObj && timeObj.prev) {
	                newHour = timeObj.prev;
	            } else {
	                for(curHour in this.amHours) {
	                    curTimeObj = this.amHours[curHour];
	                    if (curTimeObj.next == null) {
	                        newHour = curHour;
	                        isAmPmChanged = true;
	                        break;
	                    }
	                }
	            }
	        }
	    } else {
	        newHour = hour - 1;
	        if (newHour < this.cfg.hours.starts) {
	            newHour = this.cfg.hours.ends;
	        }
	    }

	    return {'newHour': newHour, 'isAmPmChanged': isAmPmChanged};
	},

	// Exposed public methods

	setTime : function(time) {
	    this.jq.fgtimepicker('setTime', time);
	},

    setMinTime : function(hour, minute) {
   	    this.jq.fgtimepicker('option', {minTime: {hour: hour, minute: minute}});
   	},

    setMaxTime : function(hour, minute) {
   	    this.jq.fgtimepicker('option', {maxTime: {hour: hour, minute: minute}});
   	},

	getTime : function() {
	    return this.jq.fgtimepicker('getTime');
	},

	getHours : function() {
	    return this.jq.fgtimepicker('getHour');
	},

	getMinutes : function() {
	    return this.jq.fgtimepicker('getMinute');
	},

	disable : function() {
	    this.jq.fgtimepicker('disable');

        if (!this.cfg.modeInline) {
            this.jq.addClass('ui-state-disabled');
        }

        if (this.cfg.modeSpinner) {
	        this.disableSpinner();
        }
	},

	enable : function() {
	    this.jq.fgtimepicker('enable');

        if (!this.cfg.modeInline) {
            this.jq.removeClass('ui-state-disabled');
        }

        if (this.cfg.modeSpinner) {
	        this.enableSpinner();
        }
	},

    reset : function(cfg) {
        this.jq.val(this.originalValue);
    }
});

// default i18n

PrimeFacesExt.locales.TimePicker['en'] = {
    hourText: 'Hours',
    minuteText: 'Minutes',
    amPmText: ['AM', 'PM'] ,
    closeButtonText: 'Done',
    nowButtonText: 'Now',
    deselectButtonText: 'Deselect'
};

PrimeFacesExt.locales.TimePicker['en_US'] = PrimeFacesExt.locales.TimePicker['en'];
PrimeFacesExt.locales.TimePicker['en_UK'] = PrimeFacesExt.locales.TimePicker['en'];
