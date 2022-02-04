// @ts-check

window.monacoModule = window.monacoModule || {};

(function () {

  const {
    createDiffEditorInitData,
    createEditorConstructionOptions,
    defineCustomThemes,
    getMonacoResource,
    getScriptName,
    InlineDiffEditorDefaults,
    InlineEditorDefaults,
    invokeMonaco,
    invokeMonacoScript,
    loadEditorLib,
    loadExtender,
    loadLanguage,
  } = window.monacoModule.helper;

  const PromiseQueue = window.monacoModule.PromiseQueue;

  const ExtMonacoEditorBase = window.monacoModule.ExtMonacoEditorBase;

  // Make sure the monaco environment is set.
  window.MonacoEnvironment = window.MonacoEnvironment || {};

  // Queue for loading the editor.js only once
  const GenericPromiseQueue = new PromiseQueue();

  /**
   * @template {import("monaco-editor").editor.IEditor} TEditor
   * @template {import("monaco-editor").editor.IEditorOptions} TEditorOpts
   * @template {PrimeFaces.widget.ExtMonacoEditor.SyncMonacoBaseEditorContext<TEditor, TEditorOpts>} TContext
   * @template {PrimeFaces.widget.ExtMonacoEditor.ExtenderBaseEditor<TEditor, TEditorOpts, TContext>} TExtender
   * @template {PrimeFaces.widget.ExtMonacoBaseEditorInlineCfg<TEditorOpts> & PrimeFaces.widget.ExtMonacoInlineEditorCfgBase<TEditorOpts>} TCfg
   * @template TCustomInitData
   * @extends {ExtMonacoEditorBase<TEditor, TEditorOpts, TCfg>}
   */
  class InlineBaseImpl extends ExtMonacoEditorBase {
    /**
     * @param {PrimeFaces.PartialWidgetCfg<TCfg>} cfg
     */
    constructor(cfg) {
      super(cfg);

      /** @type {TEditor | undefined} */
      this._editor;

      /** @type {Partial<TExtender> | undefined} */
      this._extenderInstance;

      /** @type {ResizeObserver | undefined} */
      this._resizeObserver;

      /** @type {{ args: RenderArgs<TEditor, TEditorOpts, TContext, Partial<TExtender>, TCustomInitData>; resolve: (args: RenderedArgs<TEditor, TEditorOpts, TContext, Partial<TExtender>, TCustomInitData>) => void; reject: (error: unknown) => void; } | undefined} */
      this._renderArgs;

      /** @type {string | undefined} */
      this._resolvedLocaleUrl;

      /** @type {TEditorOpts | undefined} */
      this._editorOptions;
    }

    /**
     * @param {TCfg} cfg
     */
    init(cfg) {
      super._init(cfg, this._getConfigDefaults());

      // Set monaco environment
      MonacoEnvironment = typeof MonacoEnvironment === "object" ? MonacoEnvironment : {};
      if (!("getWorker" in MonacoEnvironment)) {
        MonacoEnvironment.getWorker = this._createWorkerFactory();
      }
      if (!("Locale" in MonacoEnvironment)) {
        MonacoEnvironment.Locale = {
          data: {},
          language: ""
        };
      }

      this._extenderInstance = undefined;

      this._renderArgs = undefined;

      this.addRefreshListener(() => this._onRefresh());
      this.addDestroyListener(() => this._onDestroy());

      // Begin loading the editor, but only load one editor at a time
      // when there are multiple editor components in the page
      GenericPromiseQueue.add(() => this._setup())
        .then(args => this._renderDeferredAsync(args))
        .then(() => this._onInitSuccess())
        .catch(error => this._onInitError(error));
    }

    // === PUBLIC API, see primefaces-monaco.d.ts for docs

    /**
     * @returns {TContext}
     */
    getContext() {
      throw new Error("Must override abstract method");
    }

    /**
     * @returns {Partial<TExtender> | undefined}
     */
    getExtender() {
      return this._extenderInstance;
    }

    /**
     * @returns {TEditor | undefined}
     */
    getMonaco() {
      return this._editor;
    }

    /**
     * @returns {Readonly<TEditorOpts> | undefined}
     */
    getMonacoOptions() {
      return this._editorOptions;
    }

    /**
     * @returns {Promise<string>}
     */
    async getValue() {
      return this.getValueNow();
    }

    /**
     * @returns {string}
     */
    getValueNow() {
      if (this._isReady()) {
        return this._getStandaloneEditor(this._editor).getValue();
      }
      else {
        return this._editorValue || "";
      }
    }

    /**
     * @param {string} value
     */
    setValueNow(value) {
      if (this._isReady()) {
        this._getStandaloneEditor(this._editor).setValue(value);
      }
      else {
        this._editorValue = value;
      }
    }

    /**
     * @param {string} value
     * @returns {Promise<void>}
     */
    async setValue(value) {
      // defer setting the value so it's always set asynchronously
      await Promise.resolve();
      this.setValueNow(value);
    }

    /**
     * @template TReturn
     * @param {(editor: TEditor) => TReturn} handler
     * @param {TReturn} defaultReturnValue
     * @returns {TReturn}
     */
    withMonaco(handler, defaultReturnValue) {
      if (this._editor) {
        return handler(this._editor);
      }
      else {
        return defaultReturnValue;
      }
    }

    /**
     * @template TReturn
     * @param {(editor: TEditor) => TReturn} handler
     * @param {TReturn} defaultReturnValue
     * @returns {TReturn}
     */
    tryWithMonaco(handler, defaultReturnValue) {
      try {
        return this.withMonaco(handler, defaultReturnValue);
      }
      catch (e) {
        console.error("[MonacoEditor] Handler failed to process Monaco editor", e);
        return defaultReturnValue;
      }
    }

    /**
     * @returns {Promise<void>}
     */
    async layout() {
      if (this._isReady()) {
        this._editor.layout();
      }
    }

    /**
     * @template {PrimeFaces.MatchingKeys<TEditor, (...args: never[]) => unknown>} K
     * @param {K} method
     * @param {PrimeFaces.widget.ExtMonacoEditor.ParametersIfFn<TEditor[K]>} args
     * @returns {Promise<Awaited<PrimeFaces.widget.ExtMonacoEditor.ReturnTypeIfFn<TEditor[K]>>>}
     */
    invokeMonaco(method, ...args) {
      if (!this._isReady()) {
        throw new Error(`IllegalState: Cannot invoke Monaco as the editor is not ready yet. Use isReady / whenReady to check.`);
      }
      return invokeMonaco(this._editor, method, args);
    }

    /**
     * @template TRetVal
     * @template {any[]} TArgs
     * @param {((editor: TEditor, ...args: TArgs) => TRetVal) | string} script
     * @param {TArgs} args
     * @returns {Promise<Awaited<TRetVal>>}
     */
    invokeMonacoScript(script, ...args) {
      if (!this._isReady()) {
        throw new Error(`IllegalState: Cannot invoke Monaco as the editor is not ready yet. Use isReady / whenReady to check.`);
      }
      return invokeMonacoScript(this._editor, script, args, s => PrimeFaces.csp.eval(s));
    }

    // === PROTECTED

    /**
     * @abstract
     * @protected
     * @param {TEditor} editor
     * @returns {import("monaco-editor").editor.IStandaloneCodeEditor}
     */
    _getStandaloneEditor(editor) {
      throw new Error("Must override abstract method");
    }

    /**
     * @abstract
     * @protected
     * @returns {Partial<TCfg>}
     */
    _getConfigDefaults() {
      throw new Error("Must override abstract method");
    }

    /**
     * @abstract
     * @protected
     * @param {HTMLElement} domElement
     * @param {EditorInitData<TEditorOpts, TCustomInitData>} data
     * @param {import("monaco-editor").editor.IEditorOverrideServices | undefined} override
     * @returns {TEditor}
     */
    _createEditor(domElement, data, override) {
      throw new Error("Must override abstract method");
    }

    /**
     * @abstract
     * @protected
     * @param {Partial<TExtender>} extender
     * @param {boolean} wasLibLoaded
     * @returns {Promise<EditorInitData<TEditorOpts, TCustomInitData>>}
     */
    _createInitData(extender, wasLibLoaded) {
      throw new Error("Must override abstract method");
    }

    /**
     * @abstract
     * @protected
     * @param {TEditorOpts} options
     * @param {HTMLElement} target 
     */
    _setOverflowWidgetsDomNode(options, target) {
      throw new Error("Must override abstract method");
    }

    /**
     * @abstract
     * @protected
     */
    _storeScrollPosition() {
      throw new Error("Must override abstract method");
    }

    /**
     * @abstract
     * @protected
     */
    _restoreScrollPosition() {
      throw new Error("Must override abstract method");
    }

    /**
     * Adds all event listeners to the Monaco editor that are required for forwarding them as AJAX behavior events.
     * @override
     */
    _bindEvents() {
      const editor = this._editor;

      if (editor !== undefined) {
        // Change event.
        // Set the value of the editor on the hidden textarea.
        this._getStandaloneEditor(editor).onDidChangeModelContent(event => {
          this.tryWithMonaco(monaco => {
            const model = this._getStandaloneEditor(monaco).getModel();
            const value = model !== null ? model.getValue() : "";
            this.getInput().val(value);
          }, undefined);
          this._fireEvent("change", event);
        });

        // Focus / blur
        this._getStandaloneEditor(editor).onDidFocusEditorWidget(() => this._fireEvent("focus"));
        this._getStandaloneEditor(editor).onDidBlurEditorWidget(() => this._fireEvent("blur"));

        // Paste
        this._getStandaloneEditor(editor).onDidPaste(pasteEvent => this._fireEvent("paste", pasteEvent));

        // Mouse / Key
        // These are potentially computationally intensive, so register
        // only when there are server-side or client-side listeners
        if (this._supportsEvent("mousedown")) {
          this._getStandaloneEditor(editor).onMouseDown(mouseEvent => this._fireEvent("mousedown", mouseEvent));
        }

        if (this._supportsEvent("mousemove")) {
          this._getStandaloneEditor(editor).onMouseMove(mouseEvent => this._fireEvent("mousemove", mouseEvent));
        }

        if (this._supportsEvent("mouseup")) {
          this._getStandaloneEditor(editor).onMouseUp(mouseEvent => this._fireEvent("mouseup", mouseEvent));
        }

        if (this._supportsEvent("keydown")) {
          this._getStandaloneEditor(editor).onKeyDown(keyboardEvent => this._fireEvent("keydown", keyboardEvent));
        }

        if (this._supportsEvent("keyup")) {
          this._getStandaloneEditor(editor).onKeyUp(keyboardEvent => this._fireEvent("keyup", keyboardEvent));
        }
      }
    }


    // === PRIVATE

    /**
     * @returns {this is {_editor: TEditor}}
     */
    _isReady() {
      const ready = super.isReady();
      if (ready && this._editor === undefined) {
        // can only happen due to programming error
        // we mark it as ready once the editor was created
        throw new Error("IllegalState: Editor marked as ready, but editor instance not set ");
      }
      return ready;
    }

    /**
     * Loads. in this order, the extender script, then localization file, and then monaco editor script.
     * The monaco editor script must be loaded after the localization file or localization will not
     * work. This is why we cannot add the monaco editor script as a `@ResourceDependency` on the JSF
     * component.
     * @private
     * @returns {Promise<RenderArgs<TEditor, TEditorOpts, TContext, Partial<TExtender>, TCustomInitData>>}
     */
    async _setup() {
      const extender = this._extenderInstance = loadExtender(this._editor, this.cfg, this.getContext());
      const { forceLibReload, localeUrl: localeUrl } = await loadLanguage(this.cfg);
      this._resolvedLocaleUrl = localeUrl;
      const wasLibLoaded = await loadEditorLib(this.cfg, forceLibReload);
      this.getEditorContainer().empty();
      const { custom, options } = await this._createInitData(extender, wasLibLoaded);
      this._editorOptions = options;
      if (this.cfg.overflowWidgetsDomNode !== undefined && this.cfg.overflowWidgetsDomNode.length > 0) {
        const target = PrimeFaces.expressions.SearchExpressionFacade.resolveComponentsAsSelector(this.cfg.overflowWidgetsDomNode);
        if (target !== undefined && target.length > 0) {
          this._setOverflowWidgetsDomNode(options, target.get(0));
        }
        else {
          console.warn(`Target '${this.cfg.overflowWidgetsDomNode}' for option overflowWidgetsDomNode was not found in the DOM`);
        }
      }
      return { custom, extender, options, wasLibLoaded };
    }

    /**
     * Utility method that promisifies the `renderDeferred` method of the parent deferred widget so it can
     * be used in a promise-based style.
     * @private
     * @param {RenderArgs<TEditor, TEditorOpts, TContext, Partial<TExtender>, TCustomInitData>} args Options to be passed on to the render method
     * @returns {Promise<RenderedArgs<TEditor, TEditorOpts, TContext, Partial<TExtender>, TCustomInitData>>} A promise that resolves when the render method was called.
     */
    _renderDeferredAsync(args) {
      if (this.jq.closest(".ui-hidden-container").length === 0) {
        return Promise.reject("No hidden container found");
      }
      return new Promise((resolve, reject) => {
        this._renderArgs = { args, reject, resolve };
        this.renderDeferred();
      });
    }

    /**
     * Invokes the actual render method and resolves the promise created by `_renderDeferredAsync`.  Together with that
     * method this makes it possible to promisify the `renderDeferred` method of the parent deferred widget so it can
     * be used in a promise-based style.
     */
    _render() {
      if (this._renderArgs === undefined) {
        throw new Error("IllegalState: _render called, but _renderArgs were not set yet");
      }
      const { args, reject, resolve } = this._renderArgs;
      try {
        const editor = this._doRender(args);
        resolve({ editor, ...args });
      }
      catch (e) {
        reject(e);
      }
    }

    /**
     * Creates a new monaco editor with the configured options and sets up all event handlers.
     * @param {RenderArgs<TEditor, TEditorOpts, TContext, Partial<TExtender>, TCustomInitData>} args Options to be passed on to the render method
     * @returns {TEditor} The newly created monaco code editor.
     */
    _doRender(args) {
      const { custom, extender, options, wasLibLoaded } = args;

      const override = extender && typeof extender.createEditorOverrideServices === "function"
        ? extender.createEditorOverrideServices(this.getContext(), options)
        : undefined;

      // Register all custom themes that we were given. 
      defineCustomThemes(this.cfg.customThemes);

      // Create a new editor instance.
      this._editor = this._createEditor(this.getEditorContainer().get(0), { custom, options }, override);

      // Restore scroll position (when ajax updating the editor)
      this._restoreScrollPosition();

      // Auto resize when browser supports ResizeObserver
      if (this.cfg.autoResize) {
        if (typeof ResizeObserver === "function") {
          this._resizeObserver = new ResizeObserver(this._onResize.bind(this));
          this._resizeObserver.observe(this.jq.get(0));
        }
        else {
          console.warn("[MonacoEditor] Browser environment does not support auto resize: window.ResizeObserver is not available.");
        }
      }

      // After create callback
      if (typeof extender === "object" && typeof extender.afterCreate === "function") {
        extender.afterCreate(this.getContext(), wasLibLoaded);
      }

      return this._editor;
    }

    /**
     * Internal callback invoked when the editor was resized. Performs a new layout on the monaco editor
     * so that it can adapt to the new container size.
     * @private
     */
    _onResize() {
      this.tryWithMonaco(monaco => (window.requestAnimationFrame || setTimeout)(() => monaco.layout()), undefined);
    }

    /**
     * Callback invoked when the widgets was refreshed after an AJAX call. Saves the current
     * scroll position so that it can be restored later, then destroys the widgets, so
     * that it can be created again. 
     * @private
     */
    _onRefresh() {
      this._storeScrollPosition();
      PrimeFaces.removeDeferredRenders(String(this.id));
      this._onDestroy();
    }

    /**
     * Callback when the widget was destroyed. Invokes the extender, if available and disconnects the
     * resize observer, if enabled.
     * @private
     */
    _onDestroy() {
      if (this._renderArgs) {
        this._renderArgs.reject("render_canceled_by_update");
        this._renderArgs = undefined;
      }
      this._rejectOnDone();
      const extender = this._extenderInstance;
      if (extender && typeof extender.beforeDestroy === "function") {
        try {
          extender.beforeDestroy(this.getContext());
        }
        catch (e) {
          console.error("[MonacoEditor] Error in extender.beforeDestroy callback", e);
        }
      }
      if (this._resizeObserver !== undefined) {
        try {
          this._resizeObserver.disconnect();
        }
        catch (e) {
          console.error("[MonacoEditor] Could not disconnect resize observer", e);
        }
      }
      this.tryWithMonaco(monaco => monaco.dispose(), undefined);
      if (extender && typeof extender.afterDestroy === "function") {
        try {
          extender.afterDestroy(this.getContext());
        }
        catch (e) {
          console.error("[MonacoEditor] Error in extender.afterDestroy callback", e);
        }
      }
      this._editor = undefined;
      this._editorOptions = undefined;
    }

    /**
     * Create a web worker for the language services. This uses a proxied worker that imports the actual worker script.
     * The proxy imports the translated strings for the current locale to support localization even in the web workers
     *
     * @private
     * @returns {PrimeFaces.widget.ExtMonacoEditor.WorkerFactory} The factory that creates the workers for JSON, CSS and other languages.
     */
    _createWorkerFactory() {
      return (moduleId, label) => {
        const extender = this._extenderInstance;
        if (typeof extender === "object" && typeof extender.createWorker === "function") {
          return extender.createWorker(this.getContext(), moduleId, label);
        }
        else {
          const workerUrl = getMonacoResource(getScriptName(moduleId, label));
          const interceptWorkerUrl = getMonacoResource("worker.js");
          return new Worker(interceptWorkerUrl + "&worker=" + encodeURIComponent(workerUrl) + "&locale=" + encodeURIComponent(this._resolvedLocaleUrl || ""));
        }
      };
    }
  }

  /**
   * @extends {InlineBaseImpl<import("monaco-editor").editor.IStandaloneCodeEditor, import("monaco-editor").editor.IStandaloneEditorConstructionOptions, PrimeFaces.widget.ExtMonacoCodeEditorInline, PrimeFaces.widget.ExtMonacoEditor.ExtenderCodeEditorInline, PrimeFaces.widget.ExtMonacoCodeEditorInlineCfg, undefined>}
   */
  class InlineEditorImpl extends InlineBaseImpl {
    /**
     * @param {PrimeFaces.PartialWidgetCfg<PrimeFaces.widget.ExtMonacoCodeEditorInlineCfg>} cfg Arguments as passed by PrimeFaces.
     */
    constructor(cfg) {
      super(cfg);

      /** @type {number | undefined} */
      this._scrollTop;
    }

    /**
     * @returns {PrimeFaces.widget.ExtMonacoCodeEditorInline}
     */
    getContext() {
      return this;
    }

    // === PROTECTED

    /**
     * @protected
     * @param {import("monaco-editor").editor.IStandaloneCodeEditor} editor 
     * @returns {import("monaco-editor").editor.IStandaloneCodeEditor}
     */
    _getStandaloneEditor(editor) {
      return editor;
    }

    /**
     * @protected
     * @returns {Partial<PrimeFaces.widget.ExtMonacoCodeEditorInlineCfg>}
     */
    _getConfigDefaults() {
      return InlineEditorDefaults;
    }

    /**
     * @protected
     * @param {HTMLElement} domElement
     * @param {EditorInitData<import("monaco-editor").editor.IStandaloneEditorConstructionOptions, undefined>} data
     * @param {import("monaco-editor").editor.IEditorOverrideServices | undefined} override
     * @returns {import("monaco-editor").editor.IStandaloneCodeEditor}
     */
    _createEditor(domElement, data, override) {
      return monaco.editor.create(domElement, data.options, override);
    }

    /**
     * @protected
     * @param {PrimeFaces.widget.ExtMonacoEditor.ExtenderCodeEditorInline} extender
     * @param {boolean} wasLibLoaded
     * @returns {Promise<EditorInitData<import("monaco-editor").editor.IStandaloneEditorConstructionOptions, undefined>>}
     */
    async _createInitData(extender, wasLibLoaded) {
      return {
        custom: undefined,
        options: await createEditorConstructionOptions(this.getContext(), extender, this._editorValue || "", wasLibLoaded),
      };
    }

    /**
     * @protected
     * @param {import("monaco-editor").editor.IStandaloneEditorConstructionOptions} options
     * @param {HTMLElement} target 
     */
    _setOverflowWidgetsDomNode(options, target) {
      options.overflowWidgetsDomNode = target;
    }

    /**
     * @protected
     */
    _storeScrollPosition() {
      this._scrollTop = this.tryWithMonaco(monaco => monaco.getScrollTop(), 0);
    }

    /**
     * @protected
     */
    _restoreScrollPosition() {
      const scrollTop = this._scrollTop;
      if (typeof scrollTop === "number" && scrollTop > 0) {
        this.tryWithMonaco(monaco => monaco.setScrollTop(scrollTop), undefined);
      }
    }
  }

  /**
   * @extends {InlineBaseImpl<import("monaco-editor").editor.IStandaloneDiffEditor, import("monaco-editor").editor.IStandaloneDiffEditorConstructionOptions, PrimeFaces.widget.ExtMonacoDiffEditorInline, PrimeFaces.widget.ExtMonacoEditor.ExtenderDiffEditorInline, PrimeFaces.widget.ExtMonacoDiffEditorInlineCfg, DiffEditorCustomInitData>}
   */
  class InlineDiffEditorImpl extends InlineBaseImpl {
    /**
     * @param {PrimeFaces.PartialWidgetCfg<PrimeFaces.widget.ExtMonacoDiffEditorInlineCfg>} cfg Arguments as passed by PrimeFaces.
     */
    constructor(cfg) {
      super(cfg);

      /** @type {{original: number, modified: number} | undefined} */
      this._scrollTop;

      /** @type {string | undefined} */
      this._originalEditorValue;

      /** @type {JQuery | undefined} */
      this._originalInput;
    }

    /**
     * @param {PrimeFaces.widget.ExtMonacoDiffEditorInlineCfg} cfg 
     */
    init(cfg) {
      super.init(cfg);

      // Textarea with the value for the original editor
      this._originalInput = this.jq.find(".ui-helper-hidden-accessible textarea").eq(1);

      // Default to the given value for the original editor
      this._originalEditorValue = String(this._originalInput.val() || "");
    }

    // === PUBLIC API, see primefaces-monaco.d.ts for docs

    /**
     * @returns {PrimeFaces.widget.ExtMonacoDiffEditorInline}
     */
    getContext() {
      return this;
    }

    /**
     * @returns {JQuery}
     */
    getOriginalInput() {
      if (this._originalInput === undefined) {
        throw new Error("IllegalState: Widget was not initialized yet.");
      }
      return this._originalInput;
    }

    /**
     * @returns {Promise<string>}
     */
    async getOriginalValue() {
      return this.getOriginalValueNow();
    }

    /**
     * @returns {string}
     */
    getOriginalValueNow() {
      if (this._isReady()) {
        return this._editor.getOriginalEditor().getValue();
      }
      else {
        return this._originalEditorValue || "";
      }
    }

    /**
     * @param {string} value
     */
    setOriginalValueNow(value) {
      if (this._isReady()) {
        this._editor.getOriginalEditor().setValue(value);
      }
      else {
        this._originalEditorValue = value;
      }
    }

    /**
     * @param {string} value
     * @returns {Promise<void>}
     */
    async setOriginalValue(value) {
      // defer setting the value so it's always set asynchronously
      await Promise.resolve();
      this.setOriginalValueNow(value);
    }

    // === PROTECTED

    /**
     * @protected
     * @param {import("monaco-editor").editor.IStandaloneDiffEditor} editor 
     * @returns {import("monaco-editor").editor.IStandaloneCodeEditor}
     */
    _getStandaloneEditor(editor) {
      return editor.getModifiedEditor();
    }

    /**
     * @protected
     * @returns {Partial<PrimeFaces.widget.ExtMonacoDiffEditorInlineCfg>}
     */
    _getConfigDefaults() {
      return InlineDiffEditorDefaults;
    }

    /**
     * @protected
     * @param {HTMLElement} domElement
     * @param {EditorInitData<import("monaco-editor").editor.IStandaloneDiffEditorConstructionOptions, DiffEditorCustomInitData>} data
     * @param {import("monaco-editor").editor.IEditorOverrideServices | undefined} override
     * @returns {import("monaco-editor").editor.IStandaloneDiffEditor}
     */
    _createEditor(domElement, data, override) {
      const editor = monaco.editor.createDiffEditor(domElement, data.options, override);
      editor.setModel({
        modified: data.custom.modifiedModel,
        original: data.custom.originalModel,
      });
      return editor;
    }

    /**
     * @protected
     * @param {PrimeFaces.widget.ExtMonacoEditor.ExtenderDiffEditorInline} extender
     * @param {boolean} wasLibLoaded
     * @returns {Promise<EditorInitData<import("monaco-editor").editor.IStandaloneDiffEditorConstructionOptions, DiffEditorCustomInitData>>}
     */
    async _createInitData(extender, wasLibLoaded) {
      return createDiffEditorInitData(this.getContext(), extender, this._originalEditorValue || "", this._editorValue || "", wasLibLoaded);
    }

    /**
     * @protected
     * @param {import("monaco-editor").editor.IStandaloneDiffEditorConstructionOptions} options
     * @param {HTMLElement} target 
     */
    _setOverflowWidgetsDomNode(options, target) {
      options.overflowWidgetsDomNode = target;
    }

    /**
     * @protected
     */
    _storeScrollPosition() {
      this._scrollTop = this.tryWithMonaco(monaco => ({
        modified: monaco.getModifiedEditor().getScrollTop(),
        original: monaco.getOriginalEditor().getScrollTop(),
      }), { modified: 0, original: 0 });
    }

    /**
     * @protected
     */
    _restoreScrollPosition() {
      if (typeof this._scrollTop === "object" && (this._scrollTop.modified > 0 || this._scrollTop.original > 0)) {
        this.tryWithMonaco(monaco => {
          if (typeof this._scrollTop === "object" && this._scrollTop.modified > 0) {
            monaco.getModifiedEditor().setScrollTop(this._scrollTop.modified);
          }
          if (typeof this._scrollTop === "object" && this._scrollTop.original > 0) {
            monaco.getOriginalEditor().setScrollTop(this._scrollTop.original);
          }
        }, undefined);
      }
    }

    /**
     * Adds all event listeners to the Monaco editor that are required for forwarding them as AJAX behavior events.
     * @override
     */
    _bindEvents() {
      // Bind events on modified editor
      super._bindEvents();

      const editor = this._editor;

      // Bind events on original editor
      if (editor !== undefined) {
        // Change event.
        // Set the value of the editor on the hidden textarea.
        editor.getOriginalEditor().onDidChangeModelContent(event => {
          this.tryWithMonaco(monaco => {
            const model = monaco.getOriginalEditor().getModel();
            const value = model !== null ? model.getValue() : "";
            this.getOriginalInput().val(value);
          }, undefined);
          this._fireEvent("originalChange", event);
        });

        // Focus / blur
        editor.getOriginalEditor().onDidFocusEditorWidget(() => this._fireEvent("originalFocus"));
        editor.getOriginalEditor().onDidBlurEditorWidget(() => this._fireEvent("originalBlur"));

        // Paste
        editor.getOriginalEditor().onDidPaste(pasteEvent => this._fireEvent("originalPaste", pasteEvent));

        // Mouse / Key
        // These are potentially computationally intensive, so register
        // only when there are server-side or client-side listeners
        if (this._supportsEvent("originalMousedown")) {
          editor.getOriginalEditor().onMouseDown(mouseEvent => this._fireEvent("originalMousedown", mouseEvent));
        }

        if (this._supportsEvent("originalMousemove")) {
          editor.getOriginalEditor().onMouseMove(mouseEvent => this._fireEvent("originalMousemove", mouseEvent));
        }

        if (this._supportsEvent("originalMouseup")) {
          editor.getOriginalEditor().onMouseUp(mouseEvent => this._fireEvent("originalMouseup", mouseEvent));
        }

        if (this._supportsEvent("originalKeydown")) {
          editor.getOriginalEditor().onKeyDown(keyboardEvent => this._fireEvent("originalKeydown", keyboardEvent));
        }

        if (this._supportsEvent("originalKeyup")) {
          editor.getOriginalEditor().onKeyUp(keyboardEvent => this._fireEvent("originalKeyup", keyboardEvent));
        }
      }
    }
  }

  PrimeFaces.widget.ExtMonacoBaseEditorInline = InlineBaseImpl;
  PrimeFaces.widget.ExtMonacoCodeEditorInline = InlineEditorImpl;
  PrimeFaces.widget.ExtMonacoDiffEditorInline = InlineDiffEditorImpl;

  // TODO remove in one of the next major releases
  // @ts-expect-error legacy, will be removed soon
  PrimeFaces.widget.ExtMonacoEditorInline
    = InlineEditorImpl;
})();
