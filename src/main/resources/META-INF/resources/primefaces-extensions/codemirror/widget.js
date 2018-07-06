/**
 * PrimeFaces Extensions CodeMirror Widget
 * 
 * @author Thomas Andraschko
 */
PrimeFaces.widget.ExtCodeMirror = PrimeFaces.widget.DeferredWidget.extend({

    /**
     * Initializes the widget.
     * 
     * @param {object}
     *        cfg The widget configuration.
     */
    init : function(cfg) {
        this._super(cfg);

        // remove old instance if available
        if (this.jq.next().hasClass('CodeMirror')) {
            this.jq.next().remove();
        }

        this.options = this.cfg;

        this.renderDeferred();
    },

    _render : function() {
        var $this = this;
        this.instance = CodeMirror.fromTextArea(this.jq[0], this.options);
        this.instance.widgetInstance = this;
        
        this.instance.on('focus', function(cMirror) {
            $this.fireEvent('focus');
        });
        
        this.instance.on('blur', function(cMirror) {
            $this.fireEvent('blur');
        });
        
        this.instance.on('highlightComplete', function(cMirror) {
            $this.fireEvent('highlightComplete');
        });

        // register change handler
        this.instance.on('change', function(cMirror) {
            // set value to textarea
            cMirror.save();

            // fire event
            $this.fireEvent('change');
        });
    },

    complete : function() {
        this.suggestions = null;
        this.token = null;

        // Find the token at the cursor
        var cursor = this.instance.getCursor();
        var token = this.instance.getTokenAt(cursor);
        var tokenProperty = token;

        // If it's not a 'word-style' token, ignore the token.
        if (!/^[\w$_]*$/.test(token.string)) {
            token = tokenProperty = {
                start : cursor.ch,
                end : cursor.ch,
                string : "",
                state : token.state,
                className : token.string == "." ? "property" : null
            };
        }

        // If it is a property, find out what it is a property of.
        while (tokenProperty.className == "property") {
            tokenProperty = this.instance.getTokenAt({
                line : cursor.line,
                ch : tokenProperty.start
            });

            if (tokenProperty.string != ".") {
                return;
            }

            tokenProperty = this.instance.getTokenAt({
                line : cursor.line,
                ch : tokenProperty.start
            });

            if (!context) {
                var context = [];
            }

            context.push(tokenProperty);
        }

        var contextString = null;
        if (context) {
            contextString = '';

            for (var i = 0; i < context.length; i++) {
                var currentContext = context[i];

                if (i > 0) {
                    contextString = contextString + '.';
                }

                contextString = contextString + currentContext.string;
            }
        }

        this.token = token;
        this.search(token.string, contextString, cursor.line, cursor.ch);
    },

    search : function(value, context, line, column) {
        // lazy get parent form
        if (!this.form) {
            this.form = this.jq.closest("form");
            this.formId = this.form[0].id;
        }

        var $this = this;

        // start callback
        if (this.cfg.onstart) {
            this.cfg.onstart.call(this, value);
        }

        var options = {
            source : this.id,
            update : this.id,
            formId : this.formId,
            onsuccess : function(responseXML, status, xhr) {

                if ($this.cfg.onsuccess) {
                    $this.cfg.onsuccess.call(this, responseXML, status, xhr);
                }

                PrimeFaces.ajax.Response.handle(responseXML, status, xhr, {
                    widget : $this,
                    handle : function(content) {
                        $this.suggestions = [];

                        var parsedSuggestions = $(content).filter(function() {
                            return $(this).is('ul')
                        }).children();

                        parsedSuggestions.each(function() {
                            $this.suggestions.push($(this).html());
                        });

                        CodeMirror.showHint($this.instance, PrimeFaces.widget.ExtCodeMirror.getSuggestions);
                    }
                });

                return true;
            }
        };

        // complete callback
        if (this.cfg.oncomplete) {
            options.oncomplete = this.cfg.oncomplete;
        }

        // error callback
        if (this.cfg.onerror) {
            options.onerror = this.cfg.onerror;
        }

        // process
        options.process = this.cfg.process ? this.id + ' ' + this.cfg.process : this.id;

        if (this.cfg.global === false) {
            options.global = false;
        }

        options.params = [ {
            name : this.id + '_token',
            value : encodeURIComponent(value)
        }, {
            name : this.id + '_context',
            value : encodeURIComponent(context)
        }, {
            name : this.id + '_line',
            value : encodeURIComponent(line)
        }, {
            name : this.id + '_column',
            value : encodeURIComponent(column)
        } ];

        PrimeFaces.ajax.AjaxRequest(options);
    },

    /**
     * This method fires an event if the behavior was defined.
     * 
     * @param {string}
     *        eventName The name of the event.
     * @private
     */
    fireEvent : function(eventName) {
        if (this.cfg.behaviors) {
            var callback = this.cfg.behaviors[eventName];
            if (callback) {
                var options = {
                    params : []
                };

                callback.call(this, options);
            }
        }
    },

    /**
     * Returns the CodeMirror instance.
     */
    getCodeMirrorInstance : function() {
        return this.instance;
    }
});

PrimeFaces.widget.ExtCodeMirror.getSuggestions = function(editor) {
    return {
        list : editor.widgetInstance.suggestions,
        from : {
            line : editor.getCursor().line,
            ch : editor.widgetInstance.token.start
        },
        to : {
            line : editor.getCursor().line,
            ch : editor.widgetInstance.token.end
        }
    };
};
