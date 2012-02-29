PrimeFacesExt.widget.TimePicker = function(cfg) {
    this.id = cfg.id;
    this.jqId = PrimeFaces.escapeClientId(cfg.id);
    this.jqEl = cfg.modeInline ? this.jqId + '_inline' : this.jqId + '_input';
    this.jq = $(this.jqEl);
    
    // configure localized text
    this.cfg = PrimeFacesExt.configureLocale('TimePicker', cfg);
    
    if (this.cfg.showPeriod) {
        this.amHours = {};
        this.pmHours = {};
        var i, prev, next;
        
        if (this.cfg.hours.starts <= 11) {
            // fill AM hours
            var hoursEnds = Math.min(11, this.cfg.hours.ends);
            for (i=this.cfg.hours.starts; i<=hoursEnds; i++) {
                prev, next = null;
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
                prev, next = null;
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
    
    this.cfg.beforeShow = function(input, inst) {
        _self.onbeforeShow();
    };
    
    this.cfg.onClose = function(time, inst) {
        _self.onclose();
    };

    this.cfg.onSelect = function(time, inst) {
        _self.jq.val(time);
        _self.ontimeSelect();
    };   

    // create timepicker
    this.jq.timepicker(this.cfg);

    if (this.cfg.disabled) {
        this.jq.timepicker('disable');
    }

    if (this.cfg.modeSpinner && !this.cfg.disabled) {
        this.enableSpinner();
    }

    // skin input
    if (!this.cfg.modeInline) {
        PrimeFaces.skinInput(this.jq);
    }
    
    // client behaviors
    if (this.cfg.behaviors) {
        PrimeFaces.attachBehaviors(this.jq, this.cfg.behaviors);
    }
    
    this.postConstruct();
}

PrimeFaces.extend(PrimeFacesExt.widget.TimePicker, PrimeFaces.widget.BaseWidget);

PrimeFacesExt.widget.TimePicker.prototype.enableSpinner = function() {
    var _self = this;
    
    $(this.jqId).children('.ui-timepicker-button').
    removeClass('ui-state-disabled').
    on({
        mouseover: function(){
            $(this).addClass('ui-state-hover');
        },
        mouseout: function(){
            $(this).removeClass('ui-state-hover');
        },
        mouseup: function(){
            $(this).removeClass('ui-state-active');
        },
        mousedown: function(){
            var el = $(this);
            el.addClass('ui-state-active');
        
            var dir = el.hasClass('ui-timepicker-up') ? 1 : -1;
            _self.spin(dir);
        }
    });
}

PrimeFacesExt.widget.TimePicker.prototype.disableSpinner = function() {
    $(this.jqId).children('.ui-timepicker-button').
    removeClass('ui-state-hover ui-state-active').
    addClass('ui-state-disabled').
    off('mouseover mouseout mouseup mousedown');    
}

PrimeFacesExt.widget.TimePicker.prototype.spin = function(dir) {
    var time = this.jq.val();
    if (!time) {
        // empty
        return;
    }

    var newTime = null;
    var hours = null;
    var minutes = null;
    var strHours, strMinutes, result, prevHours;

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
    
        prevHours = hours;
        if (minutes > this.cfg.minutes.ends) {
            hours = this.increaseHour(hours, this.isAm(time));
            minutes = this.cfg.minutes.starts;
        } else if (minutes < this.cfg.minutes.starts) {
            hours = this.decreaseHour(hours, this.isAm(time));
            minutes = this.cfg.minutes.ends;
        }
    
        // replace old time by new one
        strHours = (hours < 10 ? "0" + hours : "" + hours);
        strMinutes = (minutes < 10 ? "0" + minutes : "" + minutes);
        newTime = time.replace(new RegExp('^(\\d{2})' + this.cfg.timeSeparator + '(\\d{2})'), strHours + this.cfg.timeSeparator + strMinutes);
        newTime = this.adjustAmPm(newTime, prevHours, hours);
        
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
    
        prevHours = hours;
        // increment / decrement hours
        if (dir == 1) {
            hours = this.increaseHour(hours, this.isAm(time));
        } else {
            hours = this.decreaseHour(hours, this.isAm(time));
        }
    
        // replace old time by new one
        strHours = (hours < 10 ? "0" + hours : "" + hours);
        newTime = time.replace(new RegExp('^(\\d{2})'), strHours);
        newTime = this.adjustAmPm(newTime, prevHours, hours);
        
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
    }
}

PrimeFacesExt.widget.TimePicker.prototype.onbeforeShow = function() {
    var behavior = this.cfg.behaviors ? this.cfg.behaviors['beforeShow'] : null;
    if (behavior) {
        behavior.call(this);
    }       
}

PrimeFacesExt.widget.TimePicker.prototype.onclose = function() {
    var behavior = this.cfg.behaviors ? this.cfg.behaviors['close'] : null;
    if (behavior) {
        behavior.call(this);
    }       
}

PrimeFacesExt.widget.TimePicker.prototype.ontimeSelect = function() {
    var behavior = this.cfg.behaviors ? this.cfg.behaviors['timeSelect'] : null;
    if (behavior) {
        behavior.call(this);
    }       
}

PrimeFacesExt.widget.TimePicker.prototype.isAm = function(time) {
    var am = this.cfg.amPmText[0];
    return (time && time.indexOf(am) != -1);
}

PrimeFacesExt.widget.TimePicker.prototype.adjustAmPm = function(time, prevHours, hours) {
    if (prevHours == 11 && hours == 12) {
        var am = this.cfg.amPmText[0];
        var pm = this.cfg.amPmText[1];
        if (time.indexOf(am) != -1) {
            return time.replace(am, pm);
        } else if (time.indexOf(pm) != -1) {
            return time.replace(pm, am);
        }
    }
    
    return time;
}

PrimeFacesExt.widget.TimePicker.prototype.increaseHour = function(hour, isAm) {
    var newHour;
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
    
    return newHour;
}

PrimeFacesExt.widget.TimePicker.prototype.decreaseHour = function(hour, isAm) {
    var newHour;
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
    
    return newHour;
}

// Exposed public methods

PrimeFacesExt.widget.TimePicker.prototype.setTime = function(time) {
    this.jq.timepicker('setTime', time);     
}

PrimeFacesExt.widget.TimePicker.prototype.getTime = function() {
    return this.jq.timepicker('getTime');     
}

PrimeFacesExt.widget.TimePicker.prototype.getHours = function() {
    return this.jq.timepicker('getHour');     
}

PrimeFacesExt.widget.TimePicker.prototype.getMinutes = function() {
    return this.jq.timepicker('getMinute');     
}

PrimeFacesExt.widget.TimePicker.prototype.disable = function() {
    this.jq.timepicker('disable');
    this.disableSpinner(); 
}

PrimeFacesExt.widget.TimePicker.prototype.enable = function() {
    this.jq.timepicker('enable');
    this.enableSpinner();    
}

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