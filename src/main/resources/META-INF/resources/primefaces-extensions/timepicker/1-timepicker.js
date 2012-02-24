PrimeFacesExt.widget.TimePicker = function(cfg) {
    this.id = cfg.id;
    this.jqId = PrimeFaces.escapeClientId(cfg.id);
    this.jqEl = cfg.modeInline ? this.jqId + '_inline' : this.jqId + '_input';
    this.jq = $(this.jqEl);
    
    // configure localized text
    this.cfg = PrimeFacesExt.configureLocale('TimePicker', cfg);
    
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
            hours = hours + 1;
            minutes = this.cfg.minutes.starts;
        } else if (minutes < this.cfg.minutes.starts) {
            hours = hours - 1;
            minutes = this.cfg.minutes.ends;
        }
    
        if (hours > this.cfg.hours.ends) {
            hours = this.cfg.hours.starts;
        } else if (hours < this.cfg.hours.starts) {
            hours = this.cfg.hours.ends;
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
            hours = hours + 1;
        } else {
            hours = hours - 1;
        }
    
        if (hours > this.cfg.hours.ends) {
            hours = this.cfg.hours.starts;
        } else if (hours < this.cfg.hours.starts) {
            hours = this.cfg.hours.ends;
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

PrimeFacesExt.widget.TimePicker.prototype.adjustAmPm = function(time, prevHours, hours) {
    if (prevHours == 11 && hours == 12) {
        if (time.indexOf('AM') != -1) {
            return time.replace("AM", "PM");
        } else if (time.indexOf('PM') != -1) {
            return time.replace("PM", "AM");
        }
    }
    
    return time;
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
