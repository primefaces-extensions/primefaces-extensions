/**
 * PrimeFaces Extensions CommandPalette Widget
 *
 * @since 16.0.0
 */
PrimeFaces.widget.ExtCommandPalette = class extends PrimeFaces.widget.BaseWidget {

    init(cfg) {
        super.init(cfg);
        this.cfg = cfg;
        this.id = cfg.id;
        this.ns = this.id.replace(/[.:]/g, '_');
        this.paletteWidth = parseInt(cfg.width, 10) || 280;
        this.paletteHeight = parseInt(cfg.height, 10) || 400;

        this.filterInput = this.jq.find('.ui-commandpalette-filter-input');
        this.groupsContainer = this.jq.find('.ui-commandpalette-groups');

        this.bindTriggers();
        this.bindEvents();
    }

    bindTriggers() {
        var $this = this;
        var forValue = this.cfg['for'];
        if (!forValue) {
            return;
        }
        var triggerEl = this.resolveTrigger(forValue);

        if (triggerEl && triggerEl.length) {
            var eventName = this.cfg.triggerEvent || 'contextmenu';
            triggerEl.on(eventName + '.commandpalette_' + this.ns, function (e) {
                $this.show(e);
                e.preventDefault();
                e.stopPropagation();
                return false;
            });
        }
    }

    bindEvents() {
        var $this = this;

        this.jq.off('.commandpalette-group').on('click.commandpalette-group', '.ui-commandpalette-group-header', function () {
            $this.toggleGroup($(this));
        });

        this.jq.off('.commandpalette-item').on('click.commandpalette-item', '.ui-commandpalette-item', function () {
            $this.selectItem($(this));
        });

        if (this.filterInput.length) {
            this.filterInput.off('.commandpalette-filter').on('keyup.commandpalette-filter', function () {
                $this.filter($(this).val());
            });
        }

        $(document).off('.commandpalette-close_' + this.ns).on('mousedown.commandpalette-close_' + this.ns, function (e) {
            if (!$this.jq.is(e.target) && !$this.jq.has(e.target).length) {
                $this.hide();
            }
        });

        $(document).off('.commandpalette-escape_' + this.ns).on('keydown.commandpalette-escape_' + this.ns, function (e) {
            if (e.key === 'Escape') {
                $this.hide();
            }
        });
    }

    show(e) {
        var clientX = (e && e.clientX) || 0;
        var clientY = (e && e.clientY) || 0;
        this.showAt(clientX, clientY);
    }

    showCentered() {
        var viewportWidth = $(window).width();
        var viewportHeight = $(window).height();
        var clientX = Math.max(0, (viewportWidth - this.paletteWidth) / 2);
        var clientY = Math.max(0, (viewportHeight - this.paletteHeight) / 2);
        this.showAt(clientX, clientY);
    }

    showAt(clientX, clientY) {
        var viewportWidth = $(window).width();
        var viewportHeight = $(window).height();

        this.jq.show();
        this.jq.css({
            left: Math.min(clientX, viewportWidth - this.paletteWidth - 20) + 'px',
            top: Math.min(clientY, viewportHeight - this.paletteHeight - 20) + 'px',
            width: this.paletteWidth + 'px',
            position: 'fixed',
            zIndex: ++PrimeFaces.zindex
        });

        this.groupsContainer.css('maxHeight', this.paletteHeight + 'px');

        this.collapseAll();

        if (this.filterInput.length) {
            this.filterInput.val('').focus();
        }
    }

    hide() {
        this.jq.hide();
        this.collapseAll();
        if (this.filterInput.length) {
            this.filterInput.val('');
        }
        this.groupsContainer.find('.ui-commandpalette-item').show();
        this.groupsContainer.find('.ui-commandpalette-group').show();
    }

    toggleGroup(header) {
        var items = header.next('.ui-commandpalette-group-items');
        var toggle = header.find('.ui-commandpalette-group-toggle');

        if (items.is(':visible')) {
            items.slideUp();
            toggle.html('\u25B6');
            header.removeClass('ui-state-active');
        } else {
            items.slideDown();
            toggle.html('\u25BC');
            header.addClass('ui-state-active');
        }
    }

    collapseAll() {
        var $this = this;
        this.jq.find('.ui-commandpalette-group-header').each(function () {
            var items = $(this).next('.ui-commandpalette-group-items');
            var toggle = $(this).find('.ui-commandpalette-group-toggle');
            items.hide();
            toggle.html('\u25B6');
            $(this).removeClass('ui-state-active');
        });
    }

    selectItem(item) {
        var option = {
            group: item.data('group'),
            value: item.data('value'),
            label: item.data('label')
        };

        if (this.cfg.onSelect) {
            this.cfg.onSelect(option);
        }

        if (this.hasBehavior('select')) {
            var options = {
                params: [
                    { name: this.id + '_group', value: option.group },
                    { name: this.id + '_value', value: option.value },
                    { name: this.id + '_label', value: option.label }
                ]
            };
            this.callBehavior('select', options);
        }

        this.hide();
    }

    filter(value) {
        var expr = value.toLowerCase().trim();
        var showAll = expr === '';

        this.jq.find('.ui-commandpalette-group').each(function () {
            var group = $(this);
            var items = group.find('.ui-commandpalette-item');
            var header = group.find('.ui-commandpalette-group-header');
            var itemsContainer = group.find('.ui-commandpalette-group-items');
            var found = false;

            items.each(function () {
                var label = $(this).data('label') || '';
                if (showAll || label.toLowerCase().indexOf(expr) !== -1) {
                    $(this).show();
                    found = true;
                } else {
                    $(this).hide();
                }
            });

            if (found || showAll) {
                group.show();
                if (!showAll && itemsContainer.is(':hidden')) {
                    itemsContainer.show();
                    header.find('.ui-commandpalette-group-toggle').html('\u25BC');
                    header.addClass('ui-state-active');
                }
            } else {
                group.hide();
            }
        });
    }

    refresh(cfg) {
        this.removeTriggers();
        super.refresh(cfg);
    }

    removeTriggers() {
        var triggerEl = this.resolveTrigger(this.cfg['for']);
        if (triggerEl && triggerEl.length) {
            triggerEl.off('.commandpalette_' + this.ns);
        }
        $(document).off('.commandpalette-close_' + this.ns);
        $(document).off('.commandpalette-escape_' + this.ns);
    }

    resolveTrigger(forValue) {
        if (!forValue) {
            return $();
        }
        var triggerEl;
        if (PrimeFaces.expressions && PrimeFaces.expressions.SearchExpressionFacade) {
            triggerEl = PrimeFaces.expressions.SearchExpressionFacade.resolveComponentsAsSelector(forValue);
        }
        if ((!triggerEl || !triggerEl.length) && document.getElementById) {
            triggerEl = $(document.getElementById(forValue));
        }
        if ((!triggerEl || !triggerEl.length)) {
            triggerEl = $('#' + forValue);
        }
        return triggerEl;
    }

    destroy() {
        this.removeTriggers();
        super.destroy();
    }

};
