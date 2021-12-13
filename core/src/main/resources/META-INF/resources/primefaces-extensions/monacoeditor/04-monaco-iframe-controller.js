// @ts-check

window.monacoModule = window.monacoModule || {};

window.MonacoEnvironment.IframeContext = (function () {

  const {
    assign,
    createEditorConstructionOptions,
    DefaultThemeData,
    FramedEditorDefaults,
    getScriptName,
    globalEval,
    invokeMonaco,
    invokeMonacoScript,
  } = window.monacoModule.helper;

  const ScrollTriggerInterval = 500;

  /**
   * @implements PrimeFaces.widget.ExtMonacoEditorBase.SyncMonacoContext
   */
  class ContextImpl {
    constructor() {
      this.id = String(window.MonacoEnvironment.InstanceId);
      this._instanceId = window.MonacoEnvironment.InstanceId;

      /** @type {PrimeFaces.widget.ExtMonacoEditorFramedCfgBase} */
      this.cfg = assign({ id: this.id }, FramedEditorDefaults);

      /** @type {PrimeFaces.widget.ExtMonacoEditorBase.MonacoExtenderInline} */
      this._extenderInstance = MonacoEnvironment.Extender || {};

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

      /** @type {import("monaco-editor").editor.IStandaloneCodeEditor | undefined} */
      this._editor = undefined;

      /** @type {ResizeObserver | undefined} */
      this._resizeObserver = undefined;

      window.addEventListener("message", event => this._onMessage(event));
      window.addEventListener("load", event => this._onLoad(event));
    }

    // === PUBLIC API, see primefaces-monaco.d.ts for docs

    /**
     * @return Promise<void>
     */
    async layout() {
      if (!this._editor) {
        // Widget should make sure controller methods are not invoked before the editor is ready.
        throw new Error("Method called on monaco iframe controller before the editor was ready.");
      }
      this._editor.layout();
    }

    isReady() {
      return this._editor !== undefined;
    }

    /**
     * @return Promise<this>
     */
    async whenReady() {
      if (!this._editor) {
        // Widget should make sure controller methods are not invoked before the editor is ready.
        throw new Error("Method called on monaco iframe controller before the editor was ready.");
      }
      return this;
    }

    /**
     * @return {import("monaco-editor").editor.IStandaloneCodeEditor}
     */
    getMonaco() {
      return this._editor;
    }

    /**
     * @return {PrimeFaces.widget.ExtMonacoEditorBaseCfgBase}
     */
    getWidgetOptions() {
      return this.cfg;
    }

    /**
     * @return {string}
     */
    getWidgetId() {
      return this.id;
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
      if (!this._editor) {
        // Widget should make sure controller methods are not invoked before the editor is ready.
        throw new Error("Method called on monaco iframe controller before the editor was ready.");
      }
      return this.withMonaco(monaco => monaco.getValue(), "");
    }

    /**
     * @param {string} value
     */
    setValueNow(value) {
      if (!this._editor) {
        // Widget should make sure controller methods are not invoked before the editor is ready.
        throw new Error("Method called on monaco iframe controller before the editor was ready.");
      }
      this._editor.setValue(value);
    }

    /**
     * @param {string} value
     * @return {Promise<void>}
     */
    async setValue(value) {
      // Make sure the value is always set asynchronously
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
      if (!this._editor) {
        // Widget should make sure controller methods are not invoked before the editor is ready.
        throw new Error("Method called on monaco iframe controller before the editor was ready.");
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
      if (!this._editor) {
        // Widget should make sure controller methods are not invoked before the editor is ready.
        throw new Error("Method called on monaco iframe controller before the editor was ready.");
      }
      return invokeMonacoScript(this._editor, script, args, s => globalEval(s, this._nonce));
    }

    /**
     * @template TReturn
     * @param {(editor: import("monaco-editor").editor.IStandaloneCodeEditor) => TReturn} handler
     * @param {TReturn} defaultReturnValue
     * @return {TReturn | undefined}
     */
    withMonaco(handler, defaultReturnValue) {
      if (!this._editor) {
        return defaultReturnValue;
      }
      return handler(this._editor);
    }

    /**
     * @template TReturn
     * @param {(editor: import("monaco-editor").editor.IStandaloneCodeEditor) => TReturn} handler
     * @param {TReturn} defaultReturnValue
     * @return {TReturn | undefined}
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

    /** @return {PrimeFaces.widget.ExtMonacoEditorBase.MonacoExtenderInline | undefined} */
    getExtender() {
      return this._extenderInstance;
    }

    // === PRIVATE

    async _render(value, scrollTop) {
      MonacoEnvironment.getWorker = this._createWorkerFactory();

      // Create editor options
      const editorOptions = await createEditorConstructionOptions(this, this._extenderInstance, value, true);
      const overrideServices = typeof this._extenderInstance.createEditorOverrideServices === "function" ? this._extenderInstance.createEditorOverrideServices(this, editorOptions) : undefined;

      // Register all custom themes that we were given. 
      for (const themeName of Object.keys(this.cfg.customThemes || {})) {
        const themeData = assign({}, DefaultThemeData, this.cfg.customThemes[themeName]);
        if (themeData !== undefined) {
          monaco.editor.defineTheme(themeName, themeData);
        }
      }

      // Create a new editor instance.
      this._editor = monaco.editor.create(this._editorContainer, editorOptions, overrideServices);

      // Restore scroll position
      if (typeof scrollTop === "number" && scrollTop > 0) {
        this.tryWithMonaco(monaco => monaco.setScrollTop(scrollTop), undefined);
      }

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
        this._extenderInstance.afterCreate(this, true);
      }
    }

    /**
     * Adds the event listeners to Monaco editor that are required for AJAX behavior events.
     */
    _bindEvents() {
      if (this._editor) {
        // Change event.
        this._editor.onDidChangeModelContent(event => this._onValueChanged(event));

        // Focus
        this._addDomEventListener("focus", fn => this._editor.onDidFocusEditorWidget(fn));
        this._addDomEventListener("blur", fn => this._editor.onDidBlurEditorWidget(fn));

        // Paste
        this._addDomEventListener("paste", fn => this._editor.onDidPaste(fn));

        // Mouse
        this._addDomEventListener("mousedown", fn => this._editor.onMouseDown(fn));
        this._addDomEventListener("mousemove", fn => this._editor.onMouseMove(fn));
        this._addDomEventListener("mouseup", fn => this._editor.onMouseUp(fn));

        // Key
        this._addDomEventListener("keydown", fn => this._editor.onKeyDown(fn));
        this._addDomEventListener("keyup", fn => this._editor.onKeyUp(fn));

        // Scroll
        this._editor.onDidScrollChange(event => this._onScrollChanged(event));
      }
    }

    /**
     * @param {string[]} events 
     * @return {Record<string, boolean>}
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
     * @param {Event} event
     */
    _onLoad(event) {
      this._editorContainer = document.getElementById("editor");
      this._postMessage({
        kind: "load",
        data: undefined,
      });
    }

    /**
     * Callback invoked when the container of the editor was resized. Performs a layout
     * in the monaco editor so that it can adapt to the new container size.
     */
    _onResize() {
      // Use requestAnimationFrame when available for better performance
      this.tryWithMonaco(monaco => (window.requestAnimationFrame || setTimeout)(() => monaco.layout()), undefined);
    }

    /**
     * Callback invoked when the editor was scrolled. Calls the actual handler with a delay for better performance.
     * Scroll events are potentially triggered on every frame.
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
     */
    _handleScrollChange() {
      this._isScrollChangeQueued = false;
      this.tryWithMonaco(monaco => {
        this._postMessage({
          kind: "scrollChange",
          data: {
            scrollTop: monaco.getScrollTop(),
            scrollLeft: monaco.getScrollLeft(),
          },
        });
      }, undefined);
    }

    /**
     * @param {import("monaco-editor").editor.IModelContentChangedEvent} changes
     */
    _onValueChanged(changes) {
      if (this._editor && this._editor.getModel()) {
        this._postMessage({
          kind: "valueChange",
          data: {
            changes,
            value: this._editor.getModel().getValue(),
          }
        });
      }
    }

    /**
     * @return {PrimeFaces.widget.ExtMonacoEditorBase.WorkerFactory}
     */
    _createWorkerFactory() {
      return (moduleId, label) => {
        if (typeof this._extenderInstance.createWorker === "function") {
          return this._extenderInstance.createWorker(this, moduleId, label);
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
     * @param {string} name
     * @param {(...args: any[]) => void} register
     */
    _addDomEventListener(name, register) {
      if (this._supportedEvents[name]) {
        register((...data) => this._postMessage({
          kind: "domEvent",
          data: {
            name,
            // Remove non-cloneable data
            data: data.length > 0 ? JSON.stringify(data) : "",
          },
        }));
      }
    }

    /**
     * @param {string} resource
     */
    _getMonacoResource(resource) {
      // DO NOT use simple variable names such as "url" or "name"
      // they will be replaced by the maven resource plugin as filtering=true is set in the pom!!
      const resourcePath = encodeURIComponent(resource);
      const resourceExtension = encodeURIComponent(this._resourceUrlExtension);
      const libVersion = encodeURIComponent(this._version);
      const libraryName = encodeURIComponent("primefaces-extensions");
      return `${this._facesResourceUri}monacoeditor/${resourcePath}.${resourceExtension}?ln=${libraryName}&v=${libVersion}`;
    }

    // === MESSAGING between top window and iframe

    /**
     * @param {MonacoMessage} message
     */
    _postMessage(message) {
      window.parent.postMessage({
        instanceId: this._instanceId,
        ...message
      }, window.location.href);
    }

    /**
     * @param {MessageEvent} event
     */
    _onMessage(event) {
      if (typeof event.data === "object" && typeof event.data.kind === "string") {
        /** @type {MonacoMessage} */
        const message = event.data;
        if (this._instanceId >= 0 && message.instanceId >= 0 && this._instanceId !== message.instanceId) {
          console.warn(`[MonacoEditor] Received message ${message.kind} with wrong instance ID (expected: ${this._instanceId}, actual: ${message.instanceId}). This message will be ignored.`);
          return;
        }
        switch (message.kind) {
          case "init":
            this._onMessageInit(message.data);
            break;
          case "destroy":
            this._onMessageDestroy();
            break;
          case "bindEvents":
            this._bindEvents();
            break;
          case "invokeMonaco":
            this._onMessageInvokeMonaco(message.messageId, message.data);
            break;
          case "invokeMonacoScript":
            this._onMessageInvokeMonacoScript(message.messageId, message.data);
            break;
          default:
            console.warn("[MonacoEditor] Unhandled message:", event.data);
        }
      }
    }

    /**
     * @param {number} messageId 
     * @param {() => any} fn 
     * @param {() => string} errorMessageSupplier 
     */
    async _sendResponse(messageId, fn, errorMessageSupplier) {
      let returnValue;
      let error;
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
          messageId,
          data: {
            success: true,
            value: returnValue,
          },
        });
      }
      else {
        console.error(errorMessageSupplier() + ":", error);
        this._postMessage({
          kind: "response",
          messageId,
          data: {
            success: false,
            error: error.message,
          },
        });
        return;
      }
    }

    /**
     * @param {InitMessageData} data
     */
    async _onMessageInit(data) {
      this.cfg = assign({}, FramedEditorDefaults, data.options);
      this.id = data.id;
      this._facesResourceUri = data.facesResourceUri;
      this._resourceUrlExtension = data.resourceUrlExtension;
      this._resolvedLocaleUrl = data.resolvedLocaleUrl;
      this._supportedEvents = this._createSupportedEvents(data.supportedEvents);
      this._nonce = data.nonce;
      this._version = data.version;
      try {
        await this._render(data.value, data.scrollTop);
        this._postMessage({
          kind: "afterInit",
          data: {
            success: true,
          },
        });
      }
      catch (e) {
        console.error("[MonacoEditor] Failed to render monaco editor:", e);
        this._postMessage({
          kind: "afterInit",
          data: {
            success: false,
            error: e.message,
          },
        });
      }
    }

    _onMessageDestroy() {
      if (typeof this._extenderInstance.beforeDestroy === "function") {
        this._extenderInstance.beforeDestroy(this);
      }
      if (this._resizeObserver !== undefined) {
        this._resizeObserver.disconnect();
      }
      this.tryWithMonaco(monaco => monaco.dispose(), undefined);
      if (typeof this._extenderInstance.afterDestroy === "function") {
        this._extenderInstance.afterDestroy(this);
      }
    }

    /**
     * @param {number} messageId
     * @param {InvokeMonacoMessageData} data
     */
    _onMessageInvokeMonaco(messageId, data) {
      this._sendResponse(
        messageId,
        () => this.invokeMonaco(data.method, ...data.args),
        () => `Could not invoke ${data.method} on monaco editor`
      );
    }

    /**
     * @param {number} messageId
     * @param {InvokeMonacoScriptMessageData} data
     */
    _onMessageInvokeMonacoScript(messageId, data) {
      this._sendResponse(
        messageId,
        () => this.invokeMonacoScript(data.script, ...data.args),
        () => "Could not invoke script on monaco editor"
      );
    }
  }

  return new ContextImpl();

})();
