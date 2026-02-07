/**
 * Kanban Board JavaScript
 * PrimeFaces Extensions Kanban Widget
 * Copyright (c) 2011-2025 PrimeFaces Extensions
 */

// Simple Kanban Board Implementation
(function() {
    'use strict';

    /**
     * Simple Dragula-like drag and drop
     */
    window.SimpleDragula = function(containers, options) {
        var dragulaInstance = {
            containers: containers,
            options: options || {},
            dragging: null,
            source: null,
            item: null,
            copy: null,
            sourceIndex: null,
            initialSibling: null,
            currentSibling: null,
            offsetX: 0,
            offsetY: 0
        };

        function start(item) {
            dragulaInstance.item = item;
            dragulaInstance.source = item.parentNode;
            dragulaInstance.sourceIndex = whichChild(item);
            item.classList.add('gu-transit');
        }

        function end() {
            if (dragulaInstance.copy) {
                dragulaInstance.copy.remove();
            }
            if (dragulaInstance.item) {
                dragulaInstance.item.classList.remove('gu-transit');
            }
            dragulaInstance.dragging = null;
            dragulaInstance.source = null;
            dragulaInstance.item = null;
            dragulaInstance.copy = null;
            dragulaInstance.sourceIndex = null;
        }

        function whichChild(element) {
            var children = Array.prototype.slice.call(element.parentNode.children);
            return children.indexOf(element);
        }

        containers.forEach(function(container) {
            container.addEventListener('mousedown', function(e) {
                var item = e.target.closest('.kanban-item');
                if (!item || !dragulaInstance.options.moves || !dragulaInstance.options.moves(item, container, e.target)) {
                    return;
                }
                e.preventDefault();

                dragulaInstance.dragging = true;
                start(item);

                var rect = item.getBoundingClientRect();
                dragulaInstance.offsetX = e.clientX - rect.left;
                dragulaInstance.offsetY = e.clientY - rect.top;

                dragulaInstance.copy = item.cloneNode(true);
                dragulaInstance.copy.classList.add('gu-mirror');
                dragulaInstance.copy.style.position = 'fixed';
                dragulaInstance.copy.style.zIndex = '9999';
                dragulaInstance.copy.style.width = rect.width + 'px';
                dragulaInstance.copy.style.height = rect.height + 'px';
                dragulaInstance.copy.style.left = (e.clientX - dragulaInstance.offsetX) + 'px';
                dragulaInstance.copy.style.top = (e.clientY - dragulaInstance.offsetY) + 'px';
                dragulaInstance.copy.style.pointerEvents = 'none';
                document.body.appendChild(dragulaInstance.copy);
            });
        });

        document.addEventListener('mousemove', function(e) {
            if (!dragulaInstance.dragging || !dragulaInstance.copy) return;

            dragulaInstance.copy.style.left = (e.clientX - dragulaInstance.offsetX) + 'px';
            dragulaInstance.copy.style.top = (e.clientY - dragulaInstance.offsetY) + 'px';

            var elementBelow = document.elementFromPoint(e.clientX, e.clientY);
            var dropTarget = elementBelow ? elementBelow.closest('.kanban-drag') : null;

            if (dropTarget && containers.indexOf(dropTarget) !== -1) {
                var nextElement = getInsertionPoint(dropTarget, e.clientY);
                if (nextElement && nextElement !== dragulaInstance.item) {
                    dropTarget.insertBefore(dragulaInstance.item, nextElement);
                } else if (!nextElement) {
                    dropTarget.appendChild(dragulaInstance.item);
                }
            }
        });

        document.addEventListener('mouseup', function(e) {
            if (!dragulaInstance.dragging) return;

            var target = dragulaInstance.item.parentNode;
            var targetIndex = whichChild(dragulaInstance.item);

            if (dragulaInstance.options.onDrop && target !== dragulaInstance.source) {
                dragulaInstance.options.onDrop(dragulaInstance.item, target, dragulaInstance.source, dragulaInstance.sourceIndex);
            }

            end();
        });

        function getInsertionPoint(container, mouseY) {
            var items = Array.prototype.slice.call(container.children);
            for (var i = 0; i < items.length; i++) {
                var item = items[i];
                if (item === dragulaInstance.item) continue;
                var rect = item.getBoundingClientRect();
                if (mouseY < rect.top + rect.height / 2) {
                    return item;
                }
            }
            return null;
        }

        return dragulaInstance;
    };

    /**
     * Simple jKanban Implementation
     */
    window.jKanban = function(options) {
        var element = typeof options.element === 'string' ? document.querySelector(options.element) : options.element;
        var boards = options.boards || [];
        var drake = null;
        var self = this;

        this.options = options;
        this.element = element;
        this.boardContainers = [];

        this.init = function() {
            element.classList.add('kanban-container');
            this.addBoards(boards);

            if (options.dragEl) {
                var containers = element.querySelectorAll(options.dragEl);
                this.boardContainers = Array.prototype.slice.call(containers);
                drake = SimpleDragula(this.boardContainers, {
                    moves: function(el, container, handle) {
                        return el.classList.contains('kanban-item');
                    },
                    onDrop: function(item, target, source, sourceIndex) {
                        if (options.dropEl && typeof options.dropEl === 'function') {
                            var itemId = item.getAttribute('data-eid');
                            var sourceBoard = source.closest('.kanban-board');
                            var targetBoard = target.closest('.kanban-board');
                            var sourceId = sourceBoard ? sourceBoard.getAttribute('data-id') : null;
                            var targetId = targetBoard ? targetBoard.getAttribute('data-id') : null;
                            var targetIndex = Array.prototype.slice.call(target.children).indexOf(item);

                            options.dropEl(itemId, source, target, sourceId, targetId, targetIndex);
                        }
                    }
                });
            }
        };

        this.addBoards = function(boards) {
            boards.forEach(function(board) {
                self.addBoard(board);
            });
        };

        this.addBoard = function(board) {
            var boardDiv = document.createElement('div');
            boardDiv.classList.add('kanban-board');
            boardDiv.setAttribute('data-id', board.id);
            if (board.class) {
                board.class.split(' ').forEach(function(cls) {
                    boardDiv.classList.add(cls);
                });
            }

            var headerDiv = document.createElement('div');
            headerDiv.classList.add('kanban-board-header');
            headerDiv.textContent = board.title;
            boardDiv.appendChild(headerDiv);

            var dragDiv = document.createElement('div');
            dragDiv.classList.add('kanban-drag');
            if (this.options.dragEl) {
                this.boardContainers.push(dragDiv);
            }

            if (board.item && board.item.length > 0) {
                board.item.forEach(function(item) {
                    var itemDiv = self.createItem(item);
                    dragDiv.appendChild(itemDiv);
                });
            }

            boardDiv.appendChild(dragDiv);

            if (this.options.addItemButton) {
                var addButton = document.createElement('button');
                addButton.textContent = '+ Add Item';
                addButton.classList.add('kanban-add-item');
                boardDiv.appendChild(addButton);
            }

            element.appendChild(boardDiv);
        };

        this.createItem = function(item) {
            var itemDiv = document.createElement('div');
            itemDiv.classList.add('kanban-item');
            itemDiv.setAttribute('data-eid', item.id);
            if (item.class) {
                item.class.split(' ').forEach(function(cls) {
                    itemDiv.classList.add(cls);
                });
            }

            var titleDiv = document.createElement('div');
            titleDiv.classList.add('kanban-item-title');
            titleDiv.textContent = item.title;
            itemDiv.appendChild(titleDiv);

            if (item.description) {
                var descDiv = document.createElement('div');
                descDiv.classList.add('kanban-item-description');
                descDiv.textContent = item.description;
                itemDiv.appendChild(descDiv);
            }

            return itemDiv;
        };

        this.init();
    };
})();

/**
 * PrimeFaces Extensions Kanban Widget
 */
if (PrimeFaces.widget) {
    PrimeFaces.widget.ExtKanban = PrimeFaces.widget.BaseWidget.extend({

        init: function(cfg) {
            this._super(cfg);
            this.cfg = cfg;
            this.id = cfg.id;
            this.boards = cfg.boards ? JSON.parse(cfg.boards) : [];

            var $this = this;

            var kanbanConfig = {
                element: '#' + this.id,
                boards: this.boards,
                dragEl: this.cfg.draggable ? '.kanban-drag' : null,
                addItemButton: this.cfg.addItemButton || false,
                dropEl: function(itemId, source, target, sourceId, targetId, targetIndex) {
                    $this.onDrop(itemId, sourceId, targetId, targetIndex);
                }
            };

            this.kanban = new jKanban(kanbanConfig);

            if (this.cfg.extender) {
                this.cfg.extender.call(this);
            }
        },

        onDrop: function(itemId, sourceColumnId, targetColumnId, newPosition) {
            var $this = this;
            var options = {
                source: this.id,
                process: this.id,
                params: [
                    {name: this.id + '_itemId', value: itemId},
                    {name: this.id + '_sourceColumnId', value: sourceColumnId},
                    {name: this.id + '_targetColumnId', value: targetColumnId},
                    {name: this.id + '_newPosition', value: newPosition}
                ]
            };

            if (this.hasBehavior('drop')) {
                this.callBehavior('drop', options);
            }
        }
    });
}
