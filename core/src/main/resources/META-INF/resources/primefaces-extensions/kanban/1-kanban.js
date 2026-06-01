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
                dragBoards: this.cfg.dragBoards !== false,
                gutter: this.cfg.gutter || '15px',
                widthBoard: this.cfg.widthBoard || '250px',
                responsivePercentage: this.cfg.responsivePercentage === true,
                itemAddOptions: {
                    enabled: this.cfg.addItemButton || false,
                    content: '+ Add Item',
                    footer: false
                },
                itemHandleOptions: this._buildHandleOptions(),
                dropEl: function(el, target, source, sibling) {
                    if (!target.contains(el)) {
                        return;
                    }
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
                },
                context: function(el, event) {
                    var itemId = el.getAttribute('data-eid');
                    var board = el.closest('.kanban-board');
                    var columnId = board ? board.getAttribute('data-id') : null;
                    event.preventDefault();
                    $this.onItemRightClick(itemId, columnId, el);

                    if ($this.cfg.bindContextMenu) {
                        var menuEvent = $.extend(new $.Event('contextmenu'), {
                            target: el,
                            pageX: event.pageX,
                            pageY: event.pageY,
                            originalEvent: event
                        });
                        PF($this.cfg.bindContextMenu).show(menuEvent);
                    }
                },
                dragBoard: function(el) {
                    var boardId = el.getAttribute('data-id');
                    $this.onDragBoard(boardId);
                },
                dragendBoard: function(el) {
                    var boardId = el.getAttribute('data-id');
                    var kanbanContainer = el.closest('.kanban-container');
                    var boardElements = kanbanContainer ? Array.prototype.slice.call(kanbanContainer.querySelectorAll('.kanban-board')) : [];
                    var newIndex = boardElements.indexOf(el);
                    $this.onDragendBoard(boardId, newIndex);
                }
            };

            this.instance = new jKanban(options);

            if ($this.hasBehavior('itemRightClick') || $this.cfg.bindContextMenu) {
                var container = document.getElementById(this.id);
                if (container) {
                    container.addEventListener('contextmenu', function(event) {
                        var item = event.target.closest('.kanban-item');
                        if (item) {
                            event.preventDefault();
                            event.stopPropagation();
                            options.context(item, event);
                        }
                    });
                }
            }
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

        onItemRightClick(itemId, columnId, element) {
            if (this.hasBehavior('itemRightClick')) {
                this.callBehavior('itemRightClick', {
                    source: this.id,
                    process: this.id,
                    params: [
                        {name: this.id + '_itemId', value: itemId},
                        {name: this.id + '_columnId', value: columnId}
                    ]
                });
            }
        }

        onDragBoard(boardId) {
            if (this.hasBehavior('dragBoard')) {
                this.callBehavior('dragBoard', {
                    source: this.id,
                    process: this.id,
                    params: [
                        {name: this.id + '_boardId', value: boardId}
                    ]
                });
            }
        }

        onDragendBoard(boardId, newPosition) {
            if (this.hasBehavior('dragendBoard')) {
                this.callBehavior('dragendBoard', {
                    source: this.id,
                    process: this.id,
                    params: [
                        {name: this.id + '_boardId', value: boardId},
                        {name: this.id + '_newPosition', value: newPosition}
                    ]
                });
            }
        }

        addElement(boardId, itemConfig, position) {
            return this.instance.addElement(boardId, itemConfig, position);
        }

        addForm(boardId, formElement) {
            if (typeof formElement === 'string') {
                var temp = document.createElement('div');
                temp.innerHTML = formElement;
                formElement = temp.firstElementChild;
            }
            return this.instance.addForm(boardId, formElement);
        }

        findElement(itemId) {
            return this.instance.findElement(itemId);
        }

        findBoard(boardId) {
            return this.instance.findBoard(boardId);
        }

        getParentBoardID(itemId) {
            var el = this.instance.findElement(itemId);
            return el ? this.instance.getParentBoardID(el) : null;
        }

        getBoardElements(boardId) {
            return this.instance.getBoardElements(boardId);
        }

        replaceElement(itemId, itemConfig) {
            return this.instance.replaceElement(itemId, itemConfig);
        }

        removeBoard(boardId) {
            return this.instance.removeBoard(boardId);
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

        _buildHandleOptions() {
            var opts = {
                enabled: this.cfg.dragHandle === true
            };
            if (this.cfg.itemHandleOptions) {
                for (var k in this.cfg.itemHandleOptions) {
                    if (this.cfg.itemHandleOptions.hasOwnProperty(k)) {
                        opts[k] = this.cfg.itemHandleOptions[k];
                    }
                }
            }
            return opts;
        }
    };
}
