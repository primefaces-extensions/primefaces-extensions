PrimeFacesExt.widget.TimePicker = function(cfg) {
    this.id = cfg.id;
    this.cfg = cfg;
    this.jqId = PrimeFaces.escapeClientId(id);
    this.jqEl = this.cfg.modeInline ? this.jqId + '_inline' : this.jqId + '_input';
    this.jq = $(this.jqEl);
    var _self = this;

    // extend configuration
    if (this.cfg.modeInline) {
        this.cfg.altField = this.jqId + '_input';
    }

    var msg = PrimeFaces.widget.TimepickerResourceBundle[this.cfg.locale];
    if (!msg) {
        // fallback
        msg = PrimeFaces.widget.TimepickerResourceBundle['en'];
    }

    this.cfg.hourText = msg.hourText;
    this.cfg.minuteText = msg.minuteText;
    if (this.cfg.showPeriod) {
        this.cfg.amPmText = msg.amPmText;
    }

    if (this.cfg.disabled && this.cfg.modeInline) {
        this.cfg.onHourShow = function() {
            return false;
        };
        this.cfg.onMinuteShow = function() {
            return false;
        };
    }

    // create timepicker
    this.jq.timepicker(this.cfg);

    if (this.cfg.disabled) {
        return;
    }

    if (this.cfg.modeSpinner) {
        $(this.jqId).children('.ui-timepicker-button').mouseover(function() {
            $(this).addClass('ui-state-hover');
        }).mouseout(function() {
            $(this).removeClass('ui-state-hover');
        }).mouseup(function() {
            $(this).removeClass('ui-state-active');
        }).mousedown(function() {
            var el = $(this);
            el.addClass('ui-state-active');

            var dir = el.hasClass('ui-timepicker-up') ? 1 : -1;
            _self.spin(dir);
        });
    }

    // client behaviors
    if (this.cfg.behaviors) {
        PrimeFaces.attachBehaviors(this.jq, this.cfg.behaviors);
    }

    // skin input
    if (!this.cfg.modeInline) {
        PrimeFaces.skinInput(this.jq);
    }
    
    this.postConstruct();
}

PrimeFaces.extend(PrimeFacesExt.widget.TimePicker, PrimeFaces.widget.BaseWidget);

PrimeFacesExt.widget.TimePicker.prototype.spin = function(dir) {
    var time = this.jq.val();
    if (!time) {
        // empty
        return;
    }

    var newTime = null;
    var hours = null;
    var minutes = null;

    // extract hours and minutes
    var result = time.match(/^(\d{2}):(\d{2})((?:\s*)|(?:\s{1}.*))$/);
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
    var strHours = (hours < 10 ? "0" + hours : "" + hours);
    var strMinutes = (minutes < 10 ? "0" + minutes : "" + minutes);
    newTime = time.replace(/^(\d{2}):(\d{2})/, strHours + ":" + strMinutes);

    if (newTime != null) {
        this.jq.val(newTime);
    }
}

PrimeFacesExt.widget.TimePickerResourceBundle = {
    en : {
        hourText: 'Hours',
        minuteText: 'Minutes',
        amPmText: ['AM', 'PM']
    },
    de : {
        hourText: 'Stunden',
        minuteText: 'Minuten',
        amPmText: ['AM', 'PM']
    },
    fr : {
        hourText: 'Heures',
        minuteText: 'Minutes',
        amPmText: ['AM', 'PM']
    }
}

PrimeFacesExt.widget.TimePickerResourceBundle.en_US = PrimeFacesExt.widget.TimePickerResourceBundle.en;
PrimeFacesExt.widget.TimePickerResourceBundle.en_UK = PrimeFacesExt.widget.TimePickerResourceBundle.en;
PrimeFacesExt.widget.TimePickerResourceBundle.de_DE = PrimeFacesExt.widget.TimePickerResourceBundle.de;
PrimeFacesExt.widget.TimePickerResourceBundle.de_CH = PrimeFacesExt.widget.TimePickerResourceBundle.de;
PrimeFacesExt.widget.TimePickerResourceBundle.fr_CH = PrimeFacesExt.widget.TimePickerResourceBundle.fr;