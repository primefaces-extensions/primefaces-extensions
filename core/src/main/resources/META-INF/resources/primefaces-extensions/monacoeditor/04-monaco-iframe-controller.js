// @ts-check

window.monacoModule = window.monacoModule || {};

(function () {

  const {
    assign,
    createDiffEditorInitData,
    createEditorConstructionOptions,
    defineCustomThemes,
    FramedDiffEditorDefaults,
    FramedEditorDefaults,
    getScriptName,
    globalEval,
    invokeMonaco,
    invokeMonacoScript,
    isMonacoMessage,
    parseSimpleQuery,
  } = window.monacoModule.helper;

  const ScrollTriggerInterval = 500;

  /**
   * @returns {PrimeFaces.widget.ExtMonacoEditor.ExtenderCodeEditorFramed}
   */
  function getGlobalCodeEditorExtender() {
    MonacoEnvironment = typeof MonacoEnvironment === "object" ? MonacoEnvironment : {};
    if (MonacoEnvironment.Extender && typeof MonacoEnvironment.Extender.codeEditor === "object" && MonacoEnvironment.Extender.codeEditor !== null) {
      return MonacoEnvironment.Extender.codeEditor;
    }
    // TODO remove this in a later version 
    if (typeof MonacoEnvironment.Extender === "object" && MonacoEnvironment.Extender !== null) {
      // legacy support
      return /** @type {PrimeFaces.widget.ExtMonacoEditor.ExtenderCodeEditorFramed} */ (MonacoEnvironment.Extender);
    }
    return {};
  }

  /**
   * @returns {PrimeFaces.widget.ExtMonacoEditor.ExtenderDiffEditorFramed}
   */
   function getGlobalDiffEditorExtender() {
    MonacoEnvironment = typeof MonacoEnvironment === "object" ? MonacoEnvironment : {};
    if (MonacoEnvironment.Extender && typeof MonacoEnvironment.Extender.diffEditor === "object" && MonacoEnvironment.Extender.diffEditor !== null) {
      return MonacoEnvironment.Extender.diffEditor;
    }
    // TODO remove this in a later version 
    if (typeof MonacoEnvironment.Extender === "object" && MonacoEnvironment.Extender !== null) {
      // legacy support
      return /** @type {PrimeFaces.widget.ExtMonacoEditor.ExtenderDiffEditorFramed} */ (MonacoEnvironment.Extender);
    }
    return {};
  }

  /**
   * @template {import("monaco-editor").editor.IEditor} TEditor
   * @template {import("monaco-editor").editor.IEditorOptions} TEditorOpts
   * @template {PrimeFaces.widget.ExtMonacoEditor.SyncMonacoBaseEditorContext<TEditor, TEditorOpts>} TContext
   * @template {PrimeFaces.widget.ExtMonacoEditor.ExtenderBaseEditor<TEditor, TEditorOpts, TContext>} TExtender
   * @template {PrimeFaces.widget.ExtMonacoBaseEditorCfg<TEditorOpts> & PrimeFaces.widget.ExtMonacoCodeEditorFramedCfgBase} TCfg
   * @template TCustomInitData
   * @template TRenderData
   * @implements {PrimeFaces.widget.ExtMonacoEditor.SyncMonacoBaseEditorContext<TEditor, TEditorOpts>}
   */
  class BaseContextImpl {
    /**
     * @param {Partial<TExtender>} extender 
     */
    constructor(extender) {
      if (window.MonacoEnvironment.InstanceId === undefined) {
        throw new Error("IllegalState: No MonacoEnvironment.InstanceId available");
      }

      /** @type {string} */
      this.id = String(window.MonacoEnvironment.InstanceId);

      /** @type {number} */
      this._instanceId = window.MonacoEnvironment.InstanceId;

      /** @type {Partial<TCfg>} */
      this.cfg = assign({ id: this.id }, this._getConfigDefaults());

      /** @type {Partial<TExtender>} */
      this._extenderInstance = extender;

      /** @type {string} */
      this._version = "1.0";

      /** @type {string} */
      this._facesResourceUri = "";

      /** @type {string} */
      this._resolvedLocaleUrl = "";

      /** @type {HTMLElement} */
      this._editorContainer = document.createElement("div");

      /** @type {boolean} */
      this._isScrollChangeQueued = false;

      /** @type {Record<string, boolean>} */
      this._supportedEvents = {};

      /** @type {string | undefined} */
      this._nonce = undefined;

      /** @tpye {string | undefined} */
      this._resourceUrlExtension = undefined;

      /** @type {TEditor | undefined} */
      this._editor = undefined;

      /** @type {TEditorOpts | undefined} */
      this._editorOptions = undefined;

      /** @type {ResizeObserver | undefined} */
      this._resizeObserver = undefined;

      window.addEventListener("message", event => this._onMessage(event));

      if (document.readyState === "complete") {
        this._onLoad();
      }
      else {
        window.addEventListener("load", () => this._onLoad());
      }
    }

    // === PUBLIC API, see primefaces-monaco.d.ts for docs

    /**
     * @abstract
     * @returns {TContext}
     */
    getContext() {
      throw new Error("Must override abstract method");
    }

    /**
     * @returns Promise<void>
     */
    async layout() {
      if (!this._editor) {
        // Widget should make sure controller methods are not invoked before the editor is ready.
        throw new Error("IllegalState: Method called on monaco iframe controller before the editor was ready.");
      }
      this._editor.layout();
    }

    isReady() {
      return this._editor !== undefined;
    }

    /**
     * @returns Promise<this>
     */
    async whenReady() {
      if (!this._editor) {
        // Widget should make sure controller methods are not invoked before the editor is ready.
        throw new Error("IllegalState: Method called on monaco iframe controller before the editor was ready.");
      }
      return this;
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
     * @returns {Partial<TCfg>}
     */
    getWidgetOptions() {
      return this.cfg;
    }

    /**
     * @returns {string}
     */
    getWidgetId() {
      return this.id;
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
      if (!this._editor) {
        // Widget should make sure controller methods are not invoked before the editor is ready.
        throw new Error("IllegalState: Method called on Monaco iframe controller before the editor was ready.");
      }
      return this.withMonaco(monaco => this._getStandaloneEditor(monaco).getValue(), "");
    }

    /**
     * @param {string} value
     */
    setValueNow(value) {
      if (!this._editor) {
        // Widget should make sure controller methods are not invoked before the editor is ready.
        throw new Error("IllegalState: Method called on Monaco iframe controller before the editor was ready.");
      }
      this._getStandaloneEditor(this._editor).setValue(value);
    }

    /**
     * @param {string} value
     * @returns {Promise<void>}
     */
    async setValue(value) {
      // Make sure the value is always set asynchronously
      await Promise.resolve();
      this.setValueNow(value);
    }

    /**
     * @template {PrimeFaces.MatchingKeys<TEditor, (...args: never[]) => unknown>} K
     * @param {K} method
     * @param {PrimeFaces.widget.ExtMonacoEditor.ParametersIfFn<TEditor[K]>} args
     * @returns {Promise<Awaited<PrimeFaces.widget.ExtMonacoEditor.ReturnTypeIfFn<TEditor[K]>>>}
     */
    invokeMonaco(method, ...args) {
      if (!this._editor) {
        // Widget should make sure controller methods are not invoked before the editor is ready.
        throw new Error("IllegalState: Method called on monaco iframe controller before the editor was ready.");
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
      if (!this._editor) {
        // Widget should make sure controller methods are not invoked before the editor is ready.
        throw new Error("IllegalState: Method called on monaco iframe controller before the editor was ready.");
      }
      return invokeMonacoScript(this._editor, script, args, s => globalEval(s, this._nonce));
    }

    /**
     * @template TReturn
     * @param {(editor: TEditor) => TReturn} handler
     * @param {TReturn} defaultReturnValue
     * @returns {TReturn}
     */
    withMonaco(handler, defaultReturnValue) {
      if (!this._editor) {
        return defaultReturnValue;
      }
      return handler(this._editor);
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
        console.error("[MonacoEditor] Handler failed to process monaco editor", e);
        return defaultReturnValue;
      }
    }

    /**
     * @returns {Partial<TExtender> | undefined}
     */
    getExtender() {
      return this._extenderInstance;
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
     * @param {TRenderData} renderData
     * @param {boolean} wasLibLoaded
     * @returns {Promise<EditorInitData<TEditorOpts, TCustomInitData>>}
     */
    _createInitData(extender, renderData, wasLibLoaded) {
      throw new Error("Must override abstract method");
    }

    /**
     * @abstract
     * @protected
     * @param {TRenderData} renderData
     */
    _restoreScrollPosition(renderData) {
      throw new Error("Must override abstract method");
    }

    /**
     * Calls the given function to register a listener for a DOM event and
     * sends the event data to the top frame via post message when the event
     * occurs.
     * @protected
     * @param {string} domName Name of the DOM event.
     * @param {string} widgetName Name of the correspondng widget event.
     * @param {(listener: (...args: any[]) => any) => any} register Function
     * that registers the given listener for the DOM event.
     */
    _addDomEventListener(domName, widgetName, register) {
      if (this._supportedEvents[domName]) {
        register((...data) => this._postMessage({
          kind: "domEvent",
          data: {
            name: widgetName,
            // Remove non-cloneable data
            data: data.length > 0 ? JSON.stringify(data) : "",
          },
        }));
      }
    }

    /**
     * Adds the event listeners to Monaco editor that are required for AJAX behavior events.
     * @protected
     */
    _bindEvents() {
      const editor = this._editor;
      if (editor !== undefined) {
        // Change event.
        this._getStandaloneEditor(editor).onDidChangeModelContent(event => this._onValueChanged(event));

        // Focus
        this._addDomEventListener("focus", "focus", fn => this._getStandaloneEditor(editor).onDidFocusEditorWidget(fn));
        this._addDomEventListener("blur", "blur", fn => this._getStandaloneEditor(editor).onDidBlurEditorWidget(fn));

        // Paste
        this._addDomEventListener("paste", "paste", fn => this._getStandaloneEditor(editor).onDidPaste(fn));

        // Mouse
        this._addDomEventListener("mousedown", "mousedown", fn => this._getStandaloneEditor(editor).onMouseDown(fn));
        this._addDomEventListener("mousemove", "mousemove", fn => this._getStandaloneEditor(editor).onMouseMove(fn));
        this._addDomEventListener("mouseup", "mouseup", fn => this._getStandaloneEditor(editor).onMouseUp(fn));

        // Key
        this._addDomEventListener("keydown", "keydown", fn => this._getStandaloneEditor(editor).onKeyDown(fn));
        this._addDomEventListener("keyup", "keyup", fn => this._getStandaloneEditor(editor).onKeyUp(fn));

        // Scroll
        this._getStandaloneEditor(editor).onDidScrollChange(event => this._onScrollChanged(event));
      }
    }

    // === PRIVATE

    /**
     * @private
     * @param {TRenderData} renderData 
     */
    async _render(renderData) {
      MonacoEnvironment = typeof MonacoEnvironment === "object" ? MonacoEnvironment : {};
      MonacoEnvironment.getWorker = this._createWorkerFactory();

      // Create editor options
      const { custom, options } = await this._createInitData(this._extenderInstance, renderData, true);
      this._editorOptions = options;

      const override = typeof this._extenderInstance.createEditorOverrideServices === "function"
        ? this._extenderInstance.createEditorOverrideServices(this.getContext(), options)
        : undefined;

      // Register all custom themes that we were given. 
      defineCustomThemes(this.cfg.customThemes);

      // Create a new editor instance.
      this._editor = this._createEditor(this._editorContainer, { custom, options }, override);

      // Restore scroll position
      this._restoreScrollPosition(renderData);

      // Auto resize
      if (this.cfg.autoResize) {
        if (typeof ResizeObserver === "function") {
          this._resizeObserver = new ResizeObserver(this._onResize.bind(this));
          this._resizeObserver.observe(this._editorContainer);
        }
        else {
          console.warn("[MonacoEditor] Browser environment does not support auto resize: window.ResizeObserver is not available.");
        }
      }

      // After create callback
      if (typeof this._extenderInstance.afterCreate === "function") {
        this._extenderInstance.afterCreate(this.getContext(), true);
      }
    }

    /**
     * @private
     * @param {string[]} events 
     * @returns {Record<string, boolean>}
     */
    _createSupportedEvents(events) {
      /** @type {Record<string, boolean>} */
      const record = {};
      for (const event of events) {
        record[event] = true;
      }
      return record;
    }

    /**
     * Callback that is invoked when the `load` event occurred on the iframe.
     * Sends a message to the parent frame to indicate that we are ready. 
     * @protected
     */
    _onLoad() {
      const container = document.getElementById("editor");
      if (container === null) {
        throw new Error("IllegalState: IFrame HTML does not contain editor container (#editor)");
      }
      this._editorContainer = container;
      this._postMessage({
        kind: "load",
        data: undefined,
      });
    }

    /**
     * Callback invoked when the container of the editor was resized. Performs a layout
     * in the monaco editor so that it can adapt to the new container size.
     * @private
     */
    _onResize() {
      // Use requestAnimationFrame when available for better performance
      this.tryWithMonaco(monaco => (window.requestAnimationFrame || setTimeout)(() => monaco.layout()), undefined);
    }

    /**
     * Callback invoked when the editor was scrolled. Calls the actual handler with a delay for better performance.
     * Scroll events are potentially triggered on every frame.
     * @private
     * @param {import("monaco-editor").IScrollEvent} event
     */
    _onScrollChanged(event) {
      if (!this._isScrollChangeQueued) {
        this._isScrollChangeQueued = true;
        setTimeout(() => this._handleScrollChange(), ScrollTriggerInterval);
      }
    }

    /**
     * Delayed callback when the editor was scrolled. Sends the current scroll position to
     * the outside of the iframe so it can be saved and restored when performing an AJAX
     * update.
     * @private
     */
    _handleScrollChange() {
      this._isScrollChangeQueued = false;
      this.tryWithMonaco(monaco => {
        this._postMessage({
          kind: "scrollChange",
          data: {
            scrollTop: this._getStandaloneEditor(monaco).getScrollTop(),
            scrollLeft: this._getStandaloneEditor(monaco).getScrollLeft(),
          },
        });
      }, undefined);
    }

    /**
     * @private
     * @param {import("monaco-editor").editor.IModelContentChangedEvent} changes
     */
    _onValueChanged(changes) {
      if (this._editor && this._editor.getModel()) {
        const model = this._getStandaloneEditor(this._editor).getModel();
        const value = model !== null ? model.getValue() : "";
        this._postMessage({
          kind: "valueChange",
          data: {
            changes,
            value: value,
          }
        });
      }
    }

    /**
     * @private
     * @returns {PrimeFaces.widget.ExtMonacoEditor.WorkerFactory}
     */
    _createWorkerFactory() {
      return (moduleId, label) => {
        if (typeof this._extenderInstance.createWorker === "function") {
          return this._extenderInstance.createWorker(this.getContext(), moduleId, label);
        }
        else {
          const workerUrl = this._getMonacoResource(getScriptName(moduleId, label));
          const proxyWorkerUrl = this._getMonacoResource("worker.js");
          return new Worker(
            `${proxyWorkerUrl}&worker=${encodeURIComponent(workerUrl)}&locale=${encodeURIComponent(this._resolvedLocaleUrl || "")}`
          );
        }
      };
    }

    /**
     * @private
     * @param {string} resource
     */
    _getMonacoResource(resource) {
      // DO NOT use simple variable names such as "url" or "name"
      // they will be replaced by the maven resource plugin as filtering=true is set in the pom!!
      const resourcePath = encodeURIComponent(resource);
      const resourceExtension = this._resourceUrlExtension !== undefined
        ? encodeURIComponent(this._resourceUrlExtension)
        : "";
      const libVersion = encodeURIComponent(this._version);
      const libraryName = encodeURIComponent("primefaces-extensions");
      return `${this._facesResourceUri}monacoeditor/${resourcePath}.${resourceExtension}?ln=${libraryName}&v=${libVersion}`;
    }

    // === MESSAGING between top window and iframe

    /**
     * @protected
     * @param {MonacoMessagePayload} payload
     */
    _postMessage(payload) {
      /** @type {MonacoMessage} */
      const message = {
        instanceId: this._instanceId,
        payload,
      };
      window.parent.postMessage(message, window.location.href);
    }

    /**
     * @protected
     * @param {number} messageId 
     * @param {() => any} fn 
     * @param {() => string} errorMessageSupplier 
     */
    async _sendResponse(messageId, fn, errorMessageSupplier) {
      /** @type {unknown} */
      let returnValue;
      /** @type {unknown} */
      let error;
      /** @type {boolean} */
      let success;
      // make sure not to catch errors in the post message method itself
      try {
        returnValue = await fn(); // monaco method may return a promise
        success = true;
      }
      catch (e) {
        error = e;
        success = false;
      }

      if (success) {
        this._postMessage({
          kind: "response",
          data: {
            messageId,
            success: true,
            value: returnValue,
          },
        });
      }
      else {
        console.error(errorMessageSupplier() + ":", error);
        const errorMessage = error instanceof Error ? error.message : String(error);
        this._postMessage({
          kind: "response",
          data: {
            messageId,
            success: false,
            error: errorMessage,
          },
        });
        return;
      }
    }

    /**
     * @private
     * @param {MessageEvent} event
     */
    _onMessage(event) {
      if (isMonacoMessage(event.data)) {
        const message = event.data;
        if (this._instanceId >= 0 && message.instanceId >= 0 && this._instanceId !== message.instanceId) {
          console.warn(`[MonacoEditor] Received message ${message.payload.kind} with wrong instance ID (expected: ${this._instanceId}, actual: ${message.instanceId}). This message will be ignored.`);
          return;
        }
        switch (message.payload.kind) {
          case "destroy":
            this._onMessageDestroy();
            break;
          case "bindEvents":
            this._bindEvents();
            break;
          case "invokeMonacoScript":
            this._onMessageInvokeMonacoScript(message.payload.data);
            break;
          default:
            this._handleMessage(message);
            break;
        }
      }
    }

    /**
     * @protected
     * @param {MonacoMessage} message
     */
    _handleMessage(message) {
      console.warn("[MonacoEditor] Unhandled message:", message);
    }

    /**
     * @protected
     * @param {InitBaseMessageData} data
     * @param {Partial<TCfg>} cfg
     * @param {TRenderData} renderData
     */
    async _onMessageInit(data, cfg, renderData) {
      this.cfg = assign({}, this._getConfigDefaults(), cfg);
      this.id = data.id;
      this._facesResourceUri = data.facesResourceUri;
      this._resourceUrlExtension = data.resourceUrlExtension;
      this._resolvedLocaleUrl = data.resolvedLocaleUrl;
      this._supportedEvents = this._createSupportedEvents(data.supportedEvents);
      this._nonce = data.nonce;
      this._version = data.version;
      try {
        await this._render(renderData);
        this._postMessage({
          kind: "afterInit",
          data: {
            success: true,
          },
        });
      }
      catch (error) {
        console.error("[MonacoEditor] Failed to render monaco editor:", error);
        this._postMessage({
          kind: "afterInit",
          data: {
            success: false,
            error: error instanceof Error ? error.message : String(error),
          },
        });
      }
    }

    _onMessageDestroy() {
      if (typeof this._extenderInstance.beforeDestroy === "function") {
        try {
          this._extenderInstance.beforeDestroy(this.getContext());
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
      if (typeof this._extenderInstance.afterDestroy === "function") {
        try {
          this._extenderInstance.afterDestroy(this.getContext());
        }
        catch (e) {
          console.error("[MonacoEditor] Error in extender.afterDestroy callback", e);
        }
      }
      this._editor = undefined;
      this._editorOptions = undefined;
    }

    /**
     * @private
     * @param {InvokeMonacoScriptData} data
     */
    _onMessageInvokeMonacoScript(data) {
      this._sendResponse(
        data.messageId,
        () => this.invokeMonacoScript(data.script, ...data.args),
        () => "Could not invoke script on Monaco editor"
      );
    }
  }

  /**
   * @extends {BaseContextImpl<import("monaco-editor").editor.IStandaloneCodeEditor, import("monaco-editor").editor.IStandaloneEditorConstructionOptions, PrimeFaces.widget.ExtMonacoEditor.IframeCodeEditorContext, PrimeFaces.widget.ExtMonacoEditor.ExtenderCodeEditorFramed, PrimeFaces.widget.ExtMonacoCodeEditorFramedCfg, undefined, IframeRenderData>}
   */
  class CodeEditorContextImpl extends BaseContextImpl {
    constructor() {
      super(getGlobalCodeEditorExtender());
    }

    // === PUBLIC API, see primefaces-monaco.d.ts for docs

    /**
     * @returns {PrimeFaces.widget.ExtMonacoEditor.IframeCodeEditorContext}
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
     * @returns {Partial<PrimeFaces.widget.ExtMonacoCodeEditorFramedCfg>}
     */
    _getConfigDefaults() {
      return FramedEditorDefaults;
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
     * @param {PrimeFaces.widget.ExtMonacoEditor.ExtenderCodeEditorFramed} extender
     * @param {IframeRenderData} renderData
     * @param {boolean} wasLibLoaded
     * @returns {Promise<EditorInitData<import("monaco-editor").editor.IStandaloneEditorConstructionOptions, undefined>>}
     */
    async _createInitData(extender, renderData, wasLibLoaded) {
      return {
        custom: undefined,
        options: await createEditorConstructionOptions(this, extender, renderData.value, wasLibLoaded),
      };
    }

    /**
     * @protected
     * @param {IframeRenderData} renderData
     */
    _restoreScrollPosition(renderData) {
      if (typeof renderData.scrollTop === "number" && renderData.scrollTop > 0) {
        this.tryWithMonaco(monaco => monaco.setScrollTop(renderData.scrollTop), undefined);
      }
    }

    /**
     * @protected
     * @param {MonacoMessage} message
     */
    _handleMessage(message) {
      switch (message.payload.kind) {
        case "init":
          this._onMessageInit(message.payload.data, message.payload.data.options, {
            scrollTop: message.payload.data.scrollTop,
            value: message.payload.data.value,
          });
          break;
        case "invokeMonaco":
          this._onMessageInvokeMonaco(message.payload.data);
          break;
        default:
          console.warn("[MonacoEditor] Unhandled message:", message);
          break;
      }
    }

    // === PRIVATE

    /**
     * @private
     * @param {InvokeMonacoMessageData<import("monaco-editor").editor.IStandaloneCodeEditor>} data
     */
    _onMessageInvokeMonaco(data) {
      this._sendResponse(
        data.messageId,
        () => this.invokeMonaco(
          data.method,
          ...data.args
        ),
        () => `Could not invoke ${data.method} on Monaco editor`
      );
    }
  }

  /**
   * @extends {BaseContextImpl<import("monaco-editor").editor.IStandaloneDiffEditor, import("monaco-editor").editor.IStandaloneDiffEditorConstructionOptions, PrimeFaces.widget.ExtMonacoEditor.IframeDiffEditorContext, PrimeFaces.widget.ExtMonacoEditor.ExtenderDiffEditorFramed, PrimeFaces.widget.ExtMonacoDiffEditorFramedCfg, DiffEditorCustomInitData, IframeDiffRenderData>}
   */
  class DiffEditorContextImpl extends BaseContextImpl {
    constructor() {
      super(getGlobalDiffEditorExtender());

      /** @type {boolean} */
      this._isOriginalScrollChangeQueued = false;
    }

    // === PUBLIC API, see primefaces-monaco.d.ts for docs

    /**
     * @returns {PrimeFaces.widget.ExtMonacoEditor.IframeDiffEditorContext}
     */
    getContext() {
      return this;
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
      if (this._editor === undefined) {
        // Widget should make sure controller methods are not invoked before the editor is ready.
        throw new Error("IllegalState: Method called on Monaco iframe controller before the diff editor was ready.");
      }
      return this.withMonaco(monaco => monaco.getOriginalEditor().getValue(), "");
    }

    /**
     * @param {string} value
     */
    setOriginalValueNow(value) {
      if (!this._editor) {
        // Widget should make sure controller methods are not invoked before the editor is ready.
        throw new Error("IllegalState: Method called on Monaco iframe controller before the diff editor was ready.");
      }
      this._editor.getOriginalEditor().setValue(value);
    }

    /**
     * @param {string} value
     * @returns {Promise<void>}
     */
    async setOriginalValue(value) {
      // Make sure the value is always set asynchronously
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
     * @returns {Partial<PrimeFaces.widget.ExtMonacoDiffEditorFramedCfg>}
     */
    _getConfigDefaults() {
      return FramedDiffEditorDefaults;
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
     * @param {PrimeFaces.widget.ExtMonacoEditor.ExtenderDiffEditorFramed} extender
     * @param {IframeDiffRenderData} renderData
     * @param {boolean} wasLibLoaded
     * @returns {Promise<EditorInitData<import("monaco-editor").editor.IStandaloneDiffEditorConstructionOptions, DiffEditorCustomInitData>>}
     */
    async _createInitData(extender, renderData, wasLibLoaded) {
      return createDiffEditorInitData(this, extender, renderData.originalValue, renderData.value, wasLibLoaded);
    }

    /**
     * @protected
     * @param {IframeDiffRenderData} renderData
     */
    _restoreScrollPosition(renderData) {
      if (renderData.scrollTop > 0 || renderData.originalScrollTop > 0) {
        this.tryWithMonaco(monaco => {
          if (renderData.scrollTop > 0) {
            monaco.getModifiedEditor().setScrollTop(renderData.scrollTop);
          }
          if (renderData.originalScrollTop > 0) {
            monaco.getOriginalEditor().setScrollTop(renderData.originalScrollTop);
          }
        }, undefined);
      }
    }

    /**
     * @protected
     */
    _bindEvents() {
      super._bindEvents();

      const editor = this._editor;
      if (editor !== undefined) {
        // Change event.
        editor.getOriginalEditor().onDidChangeModelContent(event => this._onOriginalValueChanged(event));

        // Focus
        this._addDomEventListener("focus", "originalFocus", fn => editor.getOriginalEditor().onDidFocusEditorWidget(fn));
        this._addDomEventListener("blur", "originalBlur", fn => editor.getOriginalEditor().onDidBlurEditorWidget(fn));

        // Paste
        this._addDomEventListener("paste", "originalPaste", fn => editor.getOriginalEditor().onDidPaste(fn));

        // Mouse
        this._addDomEventListener("mousedown", "originalMousedown", fn => editor.getOriginalEditor().onMouseDown(fn));
        this._addDomEventListener("mousemove", "originalMousemove", fn => editor.getOriginalEditor().onMouseMove(fn));
        this._addDomEventListener("mouseup", "originalMouseup", fn => editor.getOriginalEditor().onMouseUp(fn));

        // Key
        this._addDomEventListener("keydown", "originalKeydown", fn => editor.getOriginalEditor().onKeyDown(fn));
        this._addDomEventListener("keyup", "originalKeyup", fn => editor.getOriginalEditor().onKeyUp(fn));

        // Scroll
        editor.getOriginalEditor().onDidScrollChange(event => this._onOriginalScrollChanged(event));
      }
    }

    /**
     * @protected
     * @param {MonacoMessage} message
     */
    _handleMessage(message) {
      switch (message.payload.kind) {
        case "initDiff":
          this._onMessageInit(message.payload.data, message.payload.data.options, {
            originalScrollTop: message.payload.data.originalScrollTop,
            originalValue: message.payload.data.originalValue,
            scrollTop: message.payload.data.scrollTop,
            value: message.payload.data.value,
          });
          break;
        case "invokeMonacoDiff":
          this._onMessageInvokeMonacoDiff(message.payload.data);
          break;
        default:
          console.warn("[MonacoEditor] Unhandled message:", message);
          break;
      }
    }

    // === PRIVATE

    /**
     * Callback invoked when the editor was scrolled. Calls the actual handler with a delay for better performance.
     * Scroll events are potentially triggered on every frame.
     * @private
     * @param {import("monaco-editor").IScrollEvent} event
     */
    _onOriginalScrollChanged(event) {
      if (!this._isOriginalScrollChangeQueued) {
        this._isOriginalScrollChangeQueued = true;
        setTimeout(() => this._handleOriginalScrollChange(), ScrollTriggerInterval);
      }
    }

    /**
     * Send the value of the original editor to the page where the iframe is embedded.
     * @private
     * @param {import("monaco-editor").editor.IModelContentChangedEvent} changes
     */
    _onOriginalValueChanged(changes) {
      if (this._editor && this._editor.getModel()) {
        const model = this._editor.getOriginalEditor().getModel();
        const value = model !== null ? model.getValue() : "";
        this._postMessage({
          kind: "originalValueChange",
          data: {
            changes,
            value: value,
          }
        });
      }
    }

    /**
     * Delayed callback when the editor was scrolled. Sends the current scroll position to
     * the outside of the iframe so it can be saved and restored when performing an AJAX
     * update.
     * @private
     */
    _handleOriginalScrollChange() {
      this._isOriginalScrollChangeQueued = false;
      this.tryWithMonaco(monaco => {
        this._postMessage({
          kind: "originalScrollChange",
          data: {
            scrollTop: monaco.getOriginalEditor().getScrollTop(),
            scrollLeft: monaco.getOriginalEditor().getScrollLeft(),
          },
        });
      }, undefined);
    }

    /**
     * @private
     * @param {InvokeMonacoMessageData<import("monaco-editor").editor.IStandaloneDiffEditor>} data
     */
    _onMessageInvokeMonacoDiff(data) {
      this._sendResponse(
        data.messageId,
        () => this.invokeMonaco(
          data.method,
          ...data.args
        ),
        () => `Could not invoke ${data.method} on Monaco diff editor`
      );
    }
  }

  const query = parseSimpleQuery(window.location.search);
  switch (query.editorType) {
    case "codeEditor":
      new CodeEditorContextImpl();
      break;
    case "diffEditor":
      new DiffEditorContextImpl();
      break;
    default:
      console.error("[MonacoEditor] No controller was found for editor type: ", query.editorType);
  }
})();
