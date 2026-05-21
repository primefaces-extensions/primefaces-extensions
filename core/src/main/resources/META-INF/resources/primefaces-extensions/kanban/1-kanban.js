/**
 * PrimeFaces Extensions Kanban Widget
 * Copyright (c) 2011-2026 PrimeFaces Extensions
 */
if (PrimeFaces.widget) {
    PrimeFaces.widget.ExtKanban = class extends PrimeFaces.widget.DeferredWidget {

        init(cfg) {
            super.init(cfg);
            this.cfg = cfg;
            this.boards = typeof cfg.boards === 'string' ? JSON.parse(cfg.boards) : (cfg.boards || []);

            if (this.cfg.extender && typeof this.cfg.extender === 'function') {
                this.cfg.extender.call(this);
            }

            this.renderDeferred();
        }

        _render() {
            var $this = this;

            var options = {
                element: PrimeFaces.escapeClientId(this.id),
                boards: this.boards,
                dragItems: this.cfg.draggable !== false,
                dragBoards: false,
                gutter: this.cfg.gutter || '15px',
                widthBoard: this.cfg.widthBoard || '250px',
                responsivePercentage: this.cfg.responsivePercentage === true,
                itemAddOptions: {
                    enabled: this.cfg.addItemButton || false,
                    content: '+ Add Item',
                    footer: false
                },
                dropEl: function(el, target, source, sibling) {
                    var itemId = el.getAttribute('data-eid');
                    var sourceBoard = source.closest('.kanban-board');
                    var targetBoard = target.closest('.kanban-board');
                    var sourceId = sourceBoard ? sourceBoard.getAttribute('data-id') : null;
                    var targetId = targetBoard ? targetBoard.getAttribute('data-id') : null;
                    var targetIndex = Array.prototype.slice.call(target.children).indexOf(el);
                    $this.onDrop(itemId, sourceId, targetId, targetIndex);
                },
                buttonClick: function(el, boardId) {
                    $this.onAddItem(boardId);
                },
                click: function(el) {
                    var itemId = el.getAttribute('data-eid');
                    var board = el.closest('.kanban-board');
                    var columnId = board ? board.getAttribute('data-id') : null;
                    $this.onItemClick(itemId, columnId);
                }
            };

            this.instance = new jKanban(options);
        }

        onDrop(itemId, sourceColumnId, targetColumnId, newPosition) {
            if (this.hasBehavior('drop')) {
                this.callBehavior('drop', {
                    source: this.id,
                    process: this.id,
                    params: [
                        {name: this.id + '_itemId', value: itemId},
                        {name: this.id + '_sourceColumnId', value: sourceColumnId},
                        {name: this.id + '_targetColumnId', value: targetColumnId},
                        {name: this.id + '_newPosition', value: newPosition}
                    ]
                });
            }
        }

        onItemClick(itemId, columnId) {
            if (this.hasBehavior('itemClick')) {
                this.callBehavior('itemClick', {
                    source: this.id,
                    process: this.id,
                    params: [
                        {name: this.id + '_itemId', value: itemId},
                        {name: this.id + '_columnId', value: columnId}
                    ]
                });
            }
        }

        onAddItem(columnId) {
            if (this.hasBehavior('itemAdd')) {
                this.callBehavior('itemAdd', {
                    source: this.id,
                    process: this.id,
                    params: [
                        {name: this.id + '_columnId', value: columnId}
                    ]
                });
            }
        }

        refresh(cfg) {
            this._cleanup();
            super.refresh(cfg);
        }

        destroy() {
            this._cleanup();
            super.destroy();
        }

        _cleanup() {
            if (this.instance) {
                if (this.instance.drake && this.instance.drake.destroy) {
                    this.instance.drake.destroy();
                }
                if (this.instance.drakeBoard && this.instance.drakeBoard.destroy) {
                    this.instance.drakeBoard.destroy();
                }
                this.instance = null;
                var container = document.getElementById(this.id);
                if (container) {
                    container.innerHTML = '';
                }
            }
        }
    };
}
