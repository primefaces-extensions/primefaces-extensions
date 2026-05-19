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
            if (this.instance) {
                this.instance = null;
                var container = document.getElementById(this.id);
                if (container) {
                    container.innerHTML = '';
                }
            }
            super.refresh(cfg);
        }

        destroy() {
            if (this.instance) {
                this.instance = null;
                var container = document.getElementById(this.id);
                if (container) {
                    container.innerHTML = '';
                }
            }
            super.destroy();
        }
    };
}
