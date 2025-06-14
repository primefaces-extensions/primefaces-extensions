/**
 * PrimeFaces Extensions TimePicker Widget.
 *
 * @author Oleg Varaksin
 */
PrimeFaces.widget.ExtTimePicker = class extends PrimeFaces.widget.BaseWidget {

    /**
     * Initializes the widget.
     *
     * @param {object} cfg The widget configuration.
     */
    init(cfg) {
        super.init(cfg);
        this.id = cfg.id;
        this.jqId = PrimeFaces.escapeClientId(cfg.id);
        this.container = $(this.jqId);
        this.jqEl = cfg.modeInline ? this.jqId + '_inline' : this.jqId + '_input';
        this.jq = $(this.jqEl);

        // configure localized text
        this.cfg = PrimeFacesExt.configureLocale('TimePicker', cfg);

        // configure for showPeriod
        this.setupPeriod();

        // for internal use
        let $this = this;

        // extend configuration
        if (this.cfg.modeInline) {
            this.cfg.altField = this.jqId + '_input';
        }

        this.cfg.beforeShow = function (input, inst) {
            $this.onbeforeShow();
        };

        this.cfg.onClose = function (time, inst) {
            $this.onclose();
        };

        this.cfg.onSelect = function (time, inst) {
            if ($this.cfg.modeInline) {
                $($this.cfg.altField).val(time);
            } else {
                $this.jq.val(time);
            }
            $this.ontimeSelect();
        };

        // create timepicker
        this.jq.fgtimepicker(this.cfg);

        this.bindConstantEvents();

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
        this.originalValue = this.jq.val();
    }

    setupPeriod() {
        if (this.cfg.showPeriod) {
            this.amHours = {};
            this.pmHours = {};
            let i, prev, next;

            if (this.cfg.hours.starts <= 11) {
                // fill AM hours
                let hoursEnds = Math.min(11, this.cfg.hours.ends);
                for (i = this.cfg.hours.starts; i <= hoursEnds; i++) {
                    prev = null;
                    next = null;
                    if (i === this.cfg.hours.starts) {
                        prev = null;
                    } else {
                        prev = i - 1;
                        if (prev === 0) {
                            prev = 12;
                        }
                    }

                    if (i === hoursEnds) {
                        next = null;
                    } else {
                        next = i + 1;
                    }

                    this.amHours[i === 0 ? 12 : i] = {'prev': prev, 'next': next};
                }
            }

            if (this.cfg.hours.ends >= 12) {
                // fill PM hours
                for (i = 12; i <= this.cfg.hours.ends; i++) {
                    prev = null;
                    next = null;
                    if (i === 12) {
                        prev = null;
                    } else {
                        prev = i - 1;
                        if (prev !== 12) {
                            prev = prev - 12;
                        }
                    }

                    if (i < this.cfg.hours.ends) {
                        next = i - 11;
                    } else {
                        next = null;
                    }

                    this.pmHours[i > 12 ? i - 12 : i] = {'prev': prev, 'next': next};
                }
            }
        }
    }

    /**
     * Sets up the event listeners that only need to be set up once.
     * @private
     */
    bindConstantEvents() {
        let $this = this;

        PrimeFaces.utils.registerResizeHandler(this, 'resize.' + this.id + '_hide', $this.panel, function () {
            $this.hide();
        });

        PrimeFaces.utils.registerScrollHandler(this, 'scroll.' + this.id + '_hide', function () {
            $this.hide();
        });
    }

    enableInput() {
        let $this = this;

        PrimeFaces.skinInput(this.jq);

        this.jq.on({
            keydown: function (e) {
                let keyCode = $.ui.keyCode;

                switch (e.which) {
                    case keyCode.UP:
                        $this.spin(1);
                        break;

                    case keyCode.DOWN:
                        $this.spin(-1);
                        break;

                    default:
                        //do nothing
                        break;
                }
            },
            keyup: function (e) {
                clearInterval($this.spinnerInterval);
            },
            mousewheel: function (event, delta) {
                if ($this.jq.is(':focus')) {
                    if (delta > 0)
                        $this.spin(1);
                    else
                        $this.spin(-1);

                    clearInterval($this.spinnerInterval);
                    return false;
                }
            }
        });
    }

    enableSpinner() {
        let $this = this;

        $(this.jqId).children('.ui-spinner-button')
            .removeClass('ui-state-disabled')
            .off('mouseover mouseout mouseup mousedown')
            .on({
                mouseover: function () {
                    $(this).addClass('ui-state-hover');
                },
                mouseout: function () {
                    $(this).removeClass('ui-state-hover');

                    clearInterval($this.spinnerInterval);
                },
                mouseup: function () {
                    $(this).removeClass('ui-state-active');

                    clearInterval($this.spinnerInterval);
                },
                mousedown: function (e) {
                    // GitHub #423 only respect left click
                    if (e.which !== 1) {
                        return;
                    }
                    let el = $(this);
                    el.addClass('ui-state-active');

                    let dir = el.hasClass('ui-spinner-up') ? 1 : -1;
                    $this.spin(dir);

                    $this.spinnerInterval = setInterval(function () {
                        $this.spin(dir);
                    }, 200);

                    e.preventDefault();
                }
            });
    }

    disableSpinner() {
        $(this.jqId).children('.ui-spinner-button')
            .removeClass('ui-state-hover ui-state-active')
            .addClass('ui-state-disabled')
            .off();
    }

    spin(dir) {
        let time = this.jq.val();
        if (!time) {
            // if the value is empty, set 00:00 and process with spinning
            time = '00' + this.cfg.timeSeparator + '00';
            this.jq.val(time);
        }

        let newTime = null;
        let hours = null;
        let minutes = null;
        let objHours, strHours, strMinutes, result, prevHours;
        let isAmPmChanged = false;

        if (this.cfg.showHours && this.cfg.showMinutes) {
            // extract hours and minutes
            result = time.match(new RegExp('^(\\d{2})' + this.cfg.timeSeparator + '(\\d{2})((?:\\s*)|(?:\\s{1}.*))$'));
            if (result && result.length >= 3) {
                if (result[1].charAt(0) === '0') {
                    hours = parseInt(result[1].charAt(1));
                } else {
                    hours = parseInt(result[1]);
                }

                if (result[2].charAt(0) === '0') {
                    minutes = parseInt(result[2].charAt(1));
                } else {
                    minutes = parseInt(result[2]);
                }
            }

            if (minutes == null) {
                return;
            }

            // increment / decrement minutes and hours
            if (dir === 1) {
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
                if (result[1].charAt(0) === '0') {
                    hours = parseInt(result[1].charAt(1));
                } else {
                    hours = parseInt(result[1]);
                }
            }

            if (hours == null) {
                return;
            }

            // increment / decrement hours
            if (dir === 1) {
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
            result = time.match(new RegExp('^(\\d{2})\\s*$'));
            if (result && result.length >= 2) {
                if (result[1].charAt(0) === '0') {
                    minutes = parseInt(result[1].charAt(1));
                } else {
                    minutes = parseInt(result[1]);
                }
            }

            if (minutes == null) {
                return;
            }

            // increment / decrement minutes and hours
            if (dir === 1) {
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
    }

    onbeforeShow() {
        this.callBehavior('beforeShow');
    }

    onclose() {
        this.callBehavior('close');
    }

    ontimeSelect() {
        this.callBehavior('timeSelect');
    }

    isAm(time) {
        let am = this.cfg.amPmText[0];
        return (time && time.indexOf(am) !== -1);
    }

    adjustAmPm(time, isAmPmChanged) {
        if (isAmPmChanged) {
            let am = this.cfg.amPmText[0];
            let pm = this.cfg.amPmText[1];
            if (time.indexOf(am) !== -1) {
                return time.replace(am, pm);
            } else if (time.indexOf(pm) !== -1) {
                return time.replace(pm, am);
            }
        }

        return time;
    }

    increaseHour(hour, isAm) {
        let newHour;
        let isAmPmChanged = false;

        if (this.cfg.showPeriod) {
            let timeObj, curTimeObj, curHour;
            if (isAm) {
                timeObj = this.amHours[hour];
                if (timeObj && timeObj.next) {
                    newHour = timeObj.next;
                } else {
                    for (curHour in this.pmHours) {
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
                    for (curHour in this.amHours) {
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
    }

    decreaseHour(hour, isAm) {
        let newHour;
        let isAmPmChanged = false;

        if (this.cfg.showPeriod) {
            let timeObj, curTimeObj, curHour;
            if (isAm) {
                timeObj = this.amHours[hour];
                if (timeObj && timeObj.prev) {
                    newHour = timeObj.prev;
                } else {
                    for (curHour in this.pmHours) {
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
                    for (curHour in this.amHours) {
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
    }

    // Exposed public methods

    setTime(time) {
        this.jq.fgtimepicker('setTime', time);
    }

    setMinTime(hour, minute) {
        this.jq.fgtimepicker('option', {minTime: {hour: hour, minute: minute}});
    }

    setMaxTime(hour, minute) {
        this.jq.fgtimepicker('option', {maxTime: {hour: hour, minute: minute}});
    }

    getTime() {
        return this.jq.fgtimepicker('getTime');
    }

    getHours() {
        return this.jq.fgtimepicker('getHour');
    }

    getMinutes() {
        return this.jq.fgtimepicker('getMinute');
    }

    /**
     * Disables this input so that the user cannot enter a value anymore.
     */
    disable() {
        this.jq.fgtimepicker('disable');

        if (!this.cfg.modeInline) {
            this.jq.addClass('ui-state-disabled');
        }

        if (this.cfg.modeSpinner) {
            this.disableSpinner();
        }
    }

    /**
     * Enables this input so that the user can enter a value.
     */
    enable() {
        this.jq.fgtimepicker('enable');

        if (!this.cfg.modeInline) {
            this.jq.removeClass('ui-state-disabled');
        }

        if (this.cfg.modeSpinner) {
            this.enableSpinner();
        }
    }

    /**
     * Brings up the overlay panel with the available selectable options.
     */
    show() {
        this.jq.fgtimepicker('show');
    }

    /**
     * Hides the overlay panel with the available selectable options.
     */
    hide() {
        this.jq.fgtimepicker('hide');
    }

    reset(cfg) {
        this.jq.val(this.originalValue);
    }
};

// default i18n

PrimeFacesExt.locales.TimePicker['en'] = {
    hourText: 'Hours',
    minuteText: 'Minutes',
    amPmText: ['AM', 'PM'],
    closeButtonText: 'Done',
    nowButtonText: 'Now',
    deselectButtonText: 'Deselect'
};

PrimeFacesExt.locales.TimePicker['en_US'] = PrimeFacesExt.locales.TimePicker['en'];
PrimeFacesExt.locales.TimePicker['en_UK'] = PrimeFacesExt.locales.TimePicker['en'];

// GitHub #203 must correct z-index using PF code
(function () {
    $.fgtimepicker._adjustZIndex = function (input) {
        input = input.target || input;
        let inst = $.fgtimepicker._getInst(input);
        inst.tpDiv.css('zIndex', PrimeFaces.nextZindex());
    };
})();