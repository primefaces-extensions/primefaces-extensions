// @ts-check

window.monacoModule = window.monacoModule || {};

PrimeFaces.widget.ExtMonacoEditorInline = (function () {

  const {
    createEditorConstructionOptions,
    getMonacoResource,
    getScriptName,
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
  /** @type {IPromiseQueue<{extender: any, options: import("monaco-editor").editor.IEditorConstructionOptions, wasLibLoaded: boolean}>} */
  const GenericPromiseQueue = new PromiseQueue();

  /**
   * @extends {ExtMonacoEditorBase<PrimeFaces.widget.ExtMonacoEditorInlineCfg>}
   */
  class InlineImpl extends ExtMonacoEditorBase {
    /**
     * @param  {...any[]} args Arguments as passed by PrimeFaces.
     */
    constructor(...args) {
      super(...args);
    }

    /**
     * @param {Partial<typeof InlineEditorDefaults>} cfg
     */
    init(cfg) {
      super._init(cfg, InlineEditorDefaults);

      // Set monaco environment
      if (!("getWorker" in MonacoEnvironment)) {
        MonacoEnvironment.getWorker = this._createWorkerFactory();
      }
      if (!("Locale" in MonacoEnvironment)) {
        MonacoEnvironment.Locale = {
          data: {},
          language: ""
        };
      }

      /** @type {PrimeFaces.widget.ExtMonacoEditorBase.MonacoExtenderInline | undefined} */
      this._extenderInstance = undefined;

      /** @type {{args: RenderArgs, resolve: (args: RenderedArgs) => void, reject: (error: unknown) => void} | undefined} */
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
     * @return {Promise<void>}
     */
    async layout() {
      if (this.isReady()) {
        this._editor.layout();
      }
    }

    /**
     * @return {import("monaco-editor").editor.IStandaloneCodeEditor | undefined}
     */
    getMonaco() {
      return this._editor;
    }

    /**
     * @return {PrimeFaces.widget.ExtMonacoEditorBase.MonacoExtenderInline | undefined}
     */
    getExtender() {
      return this._extenderInstance;
    }

    /**
     * @template TReturn
     * @param {(editor: import("monaco-editor").editor.IStandaloneCodeEditor) => TReturn} handler
     * @param {TReturn} defaultReturnValue
     * @return {TReturn}
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
     * @param {(editor: import("monaco-editor").editor.IStandaloneCodeEditor) => TReturn} handler
     * @param {TReturn} defaultReturnValue
     * @return {TReturn}
     */
    tryWithMonaco(handler, defaultReturnValue) {
      try {
        return this.withMonaco(handler, defaultReturnValue);
      }
      catch (e) {
        console.error("[MonacoEditor] Handler failed to process monaco editor", e);
        return defaultReturnValue;
      }
    }

    /**
     * @return {Promise<string>}
     */
    async getValue() {
      return this.getValueNow();
    }

    /**
     * @return {string}
     */
    getValueNow() {
      if (this.isReady()) {
        return this._editor.getValue();
      }
      else {
        return this._editorValue;
      }
    }

    /**
     * @param {string} value
     */
    setValueNow(value) {
      if (this.isReady()) {
        this._editor.setValue(value);
      }
      else {
        this._editorValue = value;
      }
    }

    /**
     * @param {string} value
     * @return {Promise<void>}
     */
    async setValue(value) {
      // defer setting the value so it's always set asynchronously
      await Promise.resolve();
      this.setValueNow(value);
    }

    /**
     * @template {keyof import("monaco-editor").editor.IStandaloneCodeEditor} K
     * @param {K} method
     * @param {Parameters<import("monaco-editor").editor.IStandaloneCodeEditor[K]>} args
     * @return {Promise<PrimeFaces.UnwrapPromise<ReturnType<import("monaco-editor").editor.IStandaloneCodeEditor[K]>>>}
     */
    invokeMonaco(method, ...args) {
      if (!this.isReady()) {
        throw new Error(`Cannot invoke monaco as the editor is not ready yet. Use isReady / whenReady to check.`);
      }
      return invokeMonaco(this._editor, method, args);
    }

    /**
     * @template TRetVal
     * @template {any[]} TArgs
     * @param {((editor: import("monaco-editor").editor.IStandaloneCodeEditor, ...args: TArgs) => TRetVal) | string} script
     * @param {TArgs} args
     * @return {Promise<PrimeFaces.UnwrapPromise<TRetVal>>}
     */
    invokeMonacoScript(script, ...args) {
      return invokeMonacoScript(this._editor, script, args, s => PrimeFaces.csp.eval(s));
    }

    // === PRIVATE

    /**
     * Callback invoked when the widgets was refreshed after an AJAX call. Saves the current
     * scroll position so that it can be restored later, then destroys the widgets, so
     * that it can be created again. 
     */
    _onRefresh() {
      this._scrollTop = this.tryWithMonaco(monaco => monaco.getScrollTop(), 0);
      this._onDestroy();
    }

    /**
     * Callback when the widget was destroyed. Invokes the extender, if available and disconnects the
     * resize observer, if enabled.
     */
    _onDestroy() {
      this._rejectOnDone();
      const extender = this._extenderInstance;
      if (extender && typeof extender.beforeDestroy === "function") {
        extender.beforeDestroy(this);
      }
      if (this._resizeObserver !== undefined) {
        this._resizeObserver.disconnect();
      }
      this.tryWithMonaco(monaco => monaco.dispose(), undefined);
      if (extender && typeof extender.afterDestroy === "function") {
        extender.afterDestroy(this);
      }
    }

    /**
     * Loads. in this order, the extender script, then localization file, and then monaco editor script.
     * The monaco editor script must be loaded after the localization file or localization will not
     * work. This is why we cannot add the monaco editor script as a `@ResourceDependency` on the JSF
     * component.
     * @return {Promise<{extender: any, options: import("monaco-editor").editor.IEditorConstructionOptions, wasLibLoaded: boolean}>}
     */
    async _setup() {
      const extender = loadExtender(this.cfg);
      this._extenderInstance = extender;
      const { forceLibReload, localeUrl: localeUrl } = await loadLanguage(this.cfg);
      this._resolvedLocaleUrl = localeUrl;
      const wasLibLoaded = await loadEditorLib(this.cfg, forceLibReload);
      this.getEditorContainer().empty();
      const options = await createEditorConstructionOptions(this, extender, this._editorValue, wasLibLoaded);
      return { extender, options, wasLibLoaded };
    }

    /**
     * Utility method that promisifies the `renderDeferred` method of the parent deferred widget so it can
     * be used in a promise-based style.
     * @param {RenderArgs} args Options to be passed on to the render method
     * @return {Promise<RenderedArgs>} A promise that resolves when the render method was called.
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
     * @param {{extender: PrimeFaces.widget.ExtMonacoEditorBase.MonacoExtenderInline, options: import("monaco-editor").editor.IEditorConstructionOptions, wasLibLoaded: boolean}} args Options to be passed on to the render method
     * @return {import("monaco-editor").editor.IStandaloneCodeEditor} The newly created monaco code editor.
     */
    _doRender(args) {
      const { extender, options, wasLibLoaded } = args;

      /** @type {import("monaco-editor").editor.IEditorOverrideServices | undefined} */
      const override = extender && typeof extender.createEditorOverrideServices === "function" ? extender.createEditorOverrideServices(this, options) : undefined;

      // Register all custom themes that we were given. 
      for (const themeName of Object.keys(this.cfg.customThemes || {})) {
        const themeData = this.cfg.customThemes[themeName];
        if (themeData !== undefined) {
          monaco.editor.defineTheme(themeName, themeData);
        }
      }

      // Create a new editor instance.
      this._editor = monaco.editor.create(this.getEditorContainer().get(0), options, override);

      // Restore scroll position (when ajax updating the editor)
      if (typeof this._scrollTop === "number" && this._scrollTop > 0) {
        this.tryWithMonaco(monaco => monaco.setScrollTop(this._scrollTop), undefined);
      }

      // Auto resize
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
        extender.afterCreate(this, wasLibLoaded);
      }

      return this._editor;
    }

    /**
     * Adds all event listeners to the Monaco editor that are required for forwarding them as AJAX
     * behavior events.
     */
    _bindEvents() {
      if (this._editor) {
        // Change event.
        // Set the value of the editor on the hidden textarea.
        this._editor.onDidChangeModelContent(event => {
          this.tryWithMonaco(monaco => this.getInput().val(monaco.getModel().getValue()), undefined);
          this._fireEvent("change", event);
        });

        // Focus / blur
        this._editor.onDidFocusEditorWidget(() => this._fireEvent("focus"));
        this._editor.onDidBlurEditorWidget(() => this._fireEvent("blur"));

        // Paste
        this._editor.onDidPaste(pasteEvent => this._fireEvent("paste", pasteEvent));

        // Mouse / Key
        // These are potentially computationally intensive, so register
        // only when there are server-side or client-side listeners
        if (this._supportsEvent("mousedown")) {
          this._editor.onMouseDown(mouseEvent => this._fireEvent("mousedown", mouseEvent));
        }

        if (this._supportsEvent("mousemove")) {
          this._editor.onMouseMove(mouseEvent => this._fireEvent("mousemove", mouseEvent));
        }

        if (this._supportsEvent("mouseup")) {
          this._editor.onMouseUp(mouseEvent => this._fireEvent("mouseup", mouseEvent));
        }

        if (this._supportsEvent("keydown")) {
          this._editor.onKeyDown(keyboardEvent => this._fireEvent("keydown", keyboardEvent));
        }

        if (this._supportsEvent("keyup")) {
          this._editor.onKeyUp(keyboardEvent => this._fireEvent("keyup", keyboardEvent));
        }
      }
    }

    /**
     * Internal callback invoked when the editor was resized. Performs a new layout on the monaco editor
     * so that it can adapt to the new container size.
     */
    _onResize() {
      this.tryWithMonaco(monaco => (window.requestAnimationFrame || setTimeout)(() => monaco.layout()), undefined);
    }

    /**
     * Create a web worker for the language services. This uses a proxied worker that imports the actual worker script.
     * The proxy imports the translated strings for the current locale to support localization even in the web workers
     *
     * @return {PrimeFaces.widget.ExtMonacoEditorBase.WorkerFactory} The factory that creates the workers for JSON, CSS and other languages.
     */
    _createWorkerFactory() {
      return (moduleId, label) => {
        const extender = this._extenderInstance;
        if (typeof extender === "object" && typeof extender.createWorker === "function") {
          return extender.createWorker(this, moduleId, label);
        }
        else {
          const workerUrl = getMonacoResource(getScriptName(moduleId, label));
          const interceptWorkerUrl = getMonacoResource("worker.js");
          return new Worker(interceptWorkerUrl + "&worker=" + encodeURIComponent(workerUrl) + "&locale=" + encodeURIComponent(this._resolvedLocaleUrl || ""));
        }
      };
    }
  }

  return InlineImpl;
})();
