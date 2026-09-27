/**
 * PrimeFaces Extensions Gantt Widget.
 */
PrimeFaces.widget.ExtGantt = class extends PrimeFaces.widget.BaseWidget {

    /**
     * Initializes the widget.
     *
     * @param {object}
     *        cfg The widget configuration.
     */
    init(cfg) {
        super.init(cfg);
        this.id = cfg.id;

        var extender = this.cfg.extender;
        if (extender) {
            if (typeof extender === "function") {
                extender.call(this);
            } else {
                PrimeFaces.error("Extender value is not a javascript function!");
            }
        }

        this.tasks = JSON.parse(cfg.tasks || '[]');

        var options = {
            view_mode: cfg.viewMode || 'Day',
            column_width: parseInt(cfg.columnWidth) || 45,
            bar_height: parseInt(cfg.barHeight) || 30,
            date_format: cfg.dateFormat || 'YYYY-MM-DD',
            language: cfg.lang || 'en',
            today_button: cfg.todayButton !== false,
            view_mode_select: cfg.viewModeSelect === true,
            readonly: cfg.readonly === true,
            show_progress: cfg.showProgress !== false,
            show_expected_progress: cfg.showExpectedProgress === true,
            arrow_curve: 5,
            bar_corner_radius: 3,
            container_height: "auto",
            upper_header_height: 45,
            lower_header_height: 30,
            snap_at: cfg.snapAt || '1d',
            infinite_padding: cfg.infinitePadding !== false,
            lines: "both",
            move_dependencies: true,
            padding: 18,
            popup_on: "click",
            scroll_to: "today",
            on_click: (task) => this._onTaskClick(task),
            on_double_click: (task) => this._onTaskDoubleClick(task),
            on_date_change: (task, start, end) => this._onDateChange(task, start, end),
            on_progress_change: (task, progress) => this._onProgressChange(task, progress),
            on_view_change: (mode) => this._onViewChange(mode),
            on_date_click: (date) => this._onDateClick(date)
        };

        if (cfg.weekendColor) {
            options.holidays = {};
            options.holidays[cfg.weekendColor] = 'weekend';
        }

        this.gantt = new Gantt(this.jq[0], this.tasks, options);

        if (this.gantt && this.gantt.set_scroll_position) {
            setTimeout(() => {
                this.gantt.set_scroll_position();
            }, 0);
        }
    }

    _onTaskClick(task) {
        if (this.cfg.behaviors && this.cfg.behaviors.click) {
            this.callBehavior('click', {
                params: [
                    { name: this.id + '_taskId', value: task.id }
                ]
            });
        }
    }

    _onTaskDoubleClick(task) {
        if (this.cfg.behaviors && this.cfg.behaviors.dblclick) {
            this.callBehavior('dblclick', {
                params: [
                    { name: this.id + '_taskId', value: task.id }
                ]
            });
        }
    }

    _onDateChange(task, start, end) {
        if (this.cfg.behaviors && this.cfg.behaviors.dateChange) {
            var startDate = start instanceof Date ? start.toISOString() : String(start);
            var endDate = end instanceof Date ? end.toISOString() : String(end);
            this.callBehavior('dateChange', {
                params: [
                    { name: this.id + '_taskId', value: task.id },
                    { name: this.id + '_start', value: startDate },
                    { name: this.id + '_end', value: endDate }
                ]
            });
        }
    }

    _onProgressChange(task, progress) {
        if (this.cfg.behaviors && this.cfg.behaviors.progressChange) {
            this.callBehavior('progressChange', {
                params: [
                    { name: this.id + '_taskId', value: task.id },
                    { name: this.id + '_progress', value: progress }
                ]
            });
        }
    }

    _onViewChange(mode) {
        if (this.cfg.behaviors && this.cfg.behaviors.viewChange) {
            this.callBehavior('viewChange', {
                params: [
                    { name: this.id + '_viewMode', value: mode }
                ]
            });
        }
    }

    _onDateClick(date) {
        if (this.cfg.behaviors && this.cfg.behaviors.dateClick) {
            var dateValue = date instanceof Date ? date.toISOString() : String(date);
            this.callBehavior('dateClick', {
                params: [
                    { name: this.id + '_date', value: dateValue }
                ]
            });
        }
    }

    /**
     * Refreshes the Gantt chart with new tasks.
     *
     * @param {object}
     *        cfg The widget configuration.
     */
    refresh(cfg) {
        this.tasks = JSON.parse(cfg.tasks || '[]');
        this.gantt.refresh(this.tasks);
    }
};
