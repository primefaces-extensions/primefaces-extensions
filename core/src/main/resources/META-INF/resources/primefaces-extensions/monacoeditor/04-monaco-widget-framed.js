// @ts-check

window.monacoModule = window.monacoModule || {};

(function () {

  const {
    FramedDiffEditorDefaults,
    FramedEditorDefaults,
    getFacesResourceUri,
    getMonacoResource,
    isMonacoMessage,
    resolveLocaleUrl,
  } = window.monacoModule.helper;
  const ExtMonacoEditorBase = window.monacoModule.ExtMonacoEditorBase;

  let InstanceId = 0;

  let MessageId = 0;

  /**
   * @template {import("monaco-editor").editor.IEditor} TEditor
   * @template {import("monaco-editor").editor.IEditorOptions} TEditorOpts
   * @template {PrimeFaces.widget.ExtMonacoEditor.AsyncMonacoBaseEditorContext<TEditor, TEditorOpts>} TContext
   * @template {PrimeFaces.widget.ExtMonacoEditor.ExtenderBaseEditor<TEditor, TEditorOpts, TContext>} TExtender
   * @template {PrimeFaces.widget.ExtMonacoBaseEditorFramedCfg<TEditorOpts> & PrimeFaces.widget.ExtMonacoFramedEditorCfgBase<TEditorOpts>} TCfg
   * @extends {ExtMonacoEditorBase<TEditor, TEditorOpts, TCfg>}
   */
  class FramedBaseImpl extends ExtMonacoEditorBase {
    /**
     * @param  {PrimeFaces.PartialWidgetCfg<TCfg>} cfg Arguments as passed by PrimeFaces.
     */
    constructor(cfg) {
      super(cfg);

      /** @type {number | undefined} */
      this._instanceId;

      /** @type {Map<number, {resolve: (value: any) => void, reject: (error: string) => void}> | undefined} */
      this._responseMap;

      /** @type {string | undefined} */
      this._resolvedLocaleUrl;

      /** @type {((event: MessageEvent) => void) | undefined} */
      this._messageListener;
    }

    /**
     * @param {TCfg} cfg
     */
    init(cfg) {
      super._init(cfg, this._getConfigDefaults());

      this.addRefreshListener(() => this._onRefresh());
      this.addDestroyListener(() => this._onDestroy());

      this._instanceId = ++InstanceId;

      this._responseMap = new Map();

      this._resolvedLocaleUrl = resolveLocaleUrl(this.cfg);

      // Initialize iframe
      const iframeUrl = getMonacoResource("iframe.html", {
        bootstrap: [
          this.cfg.extender ? this.cfg.extender : "",
          this._resolvedLocaleUrl,
          getMonacoResource("editor.js"),
          getMonacoResource("iframe-controller.js")
        ],
        instanceId: this._instanceId,
        editorType: this._getEditorType(),
        ...this.cfg.iframeUrlParams,
      });

      this._getIframe().src = iframeUrl;

      // Communicate with iframe
      if (this._messageListener) {
        window.removeEventListener("message", this._messageListener);
      }
      this._messageListener = event => this._onMessage(event);
      window.addEventListener("message", this._messageListener);
    }

    // === PUBLIC API, see monaco-editor.d.ts

    /**
     * @returns {Promise<void>}
     */
    async layout() {
      if (this.isReady()) {
        return this._invokeLayout();
      }
      else {
        return undefined;
      }
    }

    /**
     * @template {PrimeFaces.MatchingKeys<TEditor, (...args: never[]) => unknown>} K
     * @param {K} method
     * @param {PrimeFaces.widget.ExtMonacoEditor.ParametersIfFn<TEditor[K]>} args
     * @returns {Promise<Awaited<PrimeFaces.widget.ExtMonacoEditor.ReturnTypeIfFn<TEditor[K]>>>}
     */
    async invokeMonaco(method, ...args) {
      if (!this.isReady()) {
        throw new Error(`Cannot invoke monaco as the editor is not ready yey. Use isReady / whenReady to check.`);
      }
      return this._invokeMonaco(method, ...args);
    }

    /**
     * @template TRetVal
     * @template {any[]} TArgs
     * @param {((editor: TEditor, ...args: TArgs) => TRetVal) | string} script
     * @param {TArgs} args
     * @returns {Promise<Awaited<TRetVal>>}
     */
    async invokeMonacoScript(script, ...args) {
      if (!this.isReady()) {
        throw new Error(`Cannot invoke monaco as the editor is not ready yey. Use isReady / whenReady to check.`);
      }
      return this._postPromise({
        kind: "invokeMonacoScript",
        data: {
          args,
          messageId: this._createMessageId(),
          script: script.toString(),
        },
      });
    }

    /**
     * @returns {Promise<string>}
     */
    async getValue() {
      if (this.isReady()) {
        return this._invokeGetValue();
      }
      else {
        return this._editorValue || "";
      }
    }

    /**
     * @param {string} value
     * @returns {Promise<void>}
     */
    async setValue(value) {
      if (this.isReady()) {
        return this._invokeSetValue(value);
      }
      else {
        this._editorValue = value;
        return undefined;
      }
    }

    // === PROTECTED

    /**
     * @abstract
     * @protected
     * @returns {string}
     */
    _getEditorType() {
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
     * @returns {Promise<void>}
     */
    _invokeLayout() {
      throw new Error("Must override abstract method");
    }

    /**
     * @abstract
     * @protected
     * @returns {Promise<string>}
     */
    _invokeGetValue() {
      throw new Error("Must override abstract method");
    }

    /**
     * @protected
     * @template {PrimeFaces.MatchingKeys<TEditor, (...args: never[]) => unknown>} K
     * @param {K} method
     * @param {PrimeFaces.widget.ExtMonacoEditor.ParametersIfFn<TEditor[K]>} args
     * @returns {Promise<Awaited<PrimeFaces.widget.ExtMonacoEditor.ReturnTypeIfFn<TEditor[K]>>>}
     */
    _invokeMonaco(method, ...args) {
      throw new Error("Must override abstract method");
    }

    /**
     * @abstract
     * @protected
     * @param {string} value
     * @returns {Promise<void>}
     */
    _invokeSetValue(value) {
      throw new Error("Must override abstract method");
    }

    /**
     * @abstract
     * @protected
     * @param {InitBaseMessageData} initMessageData
     * @param {Partial<PrimeFaces.PartialWidgetCfg<TCfg>>} cloneableCfg
     */
    _postInit(initMessageData, cloneableCfg) {
      throw new Error("Must override abstract method");
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
     * @returns {number}
     */
    _createMessageId() {
      const messageId = ++MessageId;
      return messageId;
    }

    // === PRIVATE

    _bindEvents() {
      this._postMessage({
        kind: "bindEvents",
        data: {},
      });
    }

    _render() {
      const cloneableCfg = this._createCloneableCfg();
      this._postInit({
        facesResourceUri: getFacesResourceUri(),
        id: this.getWidgetId(),
        nonce: PrimeFaces.csp.NONCE_VALUE,
        resolvedLocaleUrl: this._resolvedLocaleUrl || "",
        resourceUrlExtension: PrimeFaces.resources.getResourceUrlExtension(),
        scrollTop: this._scrollTop || 0,
        supportedEvents: this._listSupportedEvents(),
        value: this._editorValue || "",
        version: PrimeFacesExt.VERSION,
      }, cloneableCfg);
    }

    /**
     * @private
     */
    _onRefresh() {
      this._onDestroy();
    }

    /**
     * @private
     */
    _onDestroy() {
      this._rejectOnDone();
      this._postMessage({
        kind: "destroy",
        data: undefined,
      });
      if (this._messageListener) {
        window.removeEventListener("message", this._messageListener);
        this._messageListener = undefined;
      }
    }

    /**
     * @private
     * @returns {HTMLIFrameElement}
     */
    _getIframe() {
      const iframe = this.getEditorContainer().get(0);
      if (!iframe) {
        throw new Error("[MonacoEditor] Editor container does not exist");
      }
      if (!(iframe instanceof HTMLIFrameElement)) {
        throw new Error("[MonacoEditor] Expected editor container to be an iframe, but got: " + iframe.outerHTML);
      }
      return iframe;
    }

    /**
     * A subset of the widget configuration that can be sent to the iframe.
     * @private
     * @returns {Partial<PrimeFaces.PartialWidgetCfg<TCfg>>}
     */
    _createCloneableCfg() {
      /** @type {Record<string, unknown>} */
      const clone = {};
      /** @type {Record<string, unknown>} */
      const original = this.cfg;
      for (const key of Object.keys(original)) {
        const value = original[key];
        if (key !== "behaviors" && typeof value !== "function") {
          clone[key] = value;
        }
      }
      return /** @type {Partial<PrimeFaces.PartialWidgetCfg<TCfg>>} */(clone);
    }

    // === MESSAGING

    /**
     * Post a message to the monaco editor iframe via `postMessage`, and get a
     * promise that resolves with the response.
     * @protected
     * @param {MonacoMessagePayload & {data: {messageId: number}}} message
     * @returns {Promise<any>}
     */
    async _postPromise(message) {
      const error = this._postMessage(message);
      if (error !== undefined) {
        return Promise.reject(new Error(error));
      }
      return new Promise((resolve, reject) => {
        this._responseMap = this._responseMap || new Map();
        this._responseMap.set(message.data.messageId, { resolve, reject });
      });
    }

    /**
     * Post a message to the monaco editor iframe via `postMessage`.
     * @protected
     * @param {MonacoMessagePayload} payload
     * @returns {string | undefined} Error message when message could not be posted.
     */
    _postMessage(payload) {
      if (this._instanceId === undefined) {
        // Cannot happen normally since init is called immediately upon construction
        throw new Error("IllegalState: Framed widget has no instance ID");
      }
      const iframe = this._getIframe();
      if (!iframe || !iframe.contentWindow) {
        return "Cannot not post message, iframe not yet loaded";
      }
      /** @type {MonacoMessage} */
      const message = {
        instanceId: this._instanceId,
        payload,
      };
      iframe.contentWindow.postMessage(message, window.location.href);
      return undefined;
    }

    /**
     * Callback when the iframe sends a message via `postMessage`
     * @private
     * @param {MessageEvent} event
     */
    _onMessage(event) {
      if (isMonacoMessage(event.data)) {
        const message = event.data;
        if (this._instanceId === undefined) {
          throw new Error("IllegalState: Framed widget has no instance ID");
        }
        if (this._instanceId >= 0 && message.instanceId >= 0 && this._instanceId !== message.instanceId) {
          // this is normal when there are multiple editors all sending messages
          return;
        }
        switch (message.payload.kind) {
          case "load":
            this._onMessageLoad();
            break;
          case "response":
            this._onMessageResponse(message.payload.data.messageId, message.payload.data);
            break;
          case "valueChange":
            this._onMessageValueChange(message.payload.data);
            break;
          case "scrollChange":
            this._onMessageScrollChange(message.payload.data);
            break;
          case "domEvent":
            this._onMessageDomEvent(message.payload.data);
            break;
          case "afterInit":
            this._onMessageAfterInit(message.payload.data);
            break;
          default:
            this._handleMessage(message);
            break;
        }
      }
    }

    /**
     * Called when the iframe document is ready.
     * @private
     */
    _onMessageLoad() {
      this.renderDeferred();
    }

    /**
     * Called when the iframe sends a response to a message.
     * @private
     * @param {number} messageId
     * @param {ResponseMessageData} data
     */
    _onMessageResponse(messageId, data) {
      this._responseMap = this._responseMap || new Map();
      const entry = this._responseMap.get(messageId);
      if (entry !== undefined) {
        const { resolve, reject } = entry;
        this._responseMap.delete(messageId);
        if (data.success === true) {
          resolve(data.value);
        }
        else if (data.success === false) {
          reject(data.error);
        }
      }
    }

    /**
     * Called when the value of the monaco editor in the iframe has changed.
     * @private
     * @param {ValueChangeMessageData} data
     */
    _onMessageValueChange(data) {
      this.getInput().val(data.value || "");
      this._fireEvent("change", data.changes);
    }

    /**
     * Called when the scroll position of the monaco editor in the iframe has changed.
     * @private
     * @param {ScrollChangeMessageData} data
     */
    _onMessageScrollChange(data) {
      this._scrollTop = data.scrollTop;
    }

    /**
     * Called when a DOM even such as `blur` or `keyup` was triggered on the
     * monaco editor in the iframe.
     * @private
     * @param {DomEventMessageData} data
     */
    _onMessageDomEvent(data) {
      const args = data.data ? JSON.parse(data.data) : [];
      this._fireEvent(data.name, ...args);
    }

    /**
     * Called after the monaco editor in the iframe was initialized, and also
     * in case an error occurred.
     * @private
     * @param {AfterInitMessageData} data
     */
    _onMessageAfterInit(data) {
      if (data.success === true) {
        this._onInitSuccess();
      }
      else if (data.success === false) {
        this._onInitError(data.error);
      }
    }
  }

  /**
   * @extends {FramedBaseImpl<import("monaco-editor").editor.IStandaloneCodeEditor, import("monaco-editor").editor.IStandaloneEditorConstructionOptions, PrimeFaces.widget.ExtMonacoEditor.IframeCodeEditorContext, PrimeFaces.widget.ExtMonacoEditor.ExtenderCodeEditorFramed, PrimeFaces.widget.ExtMonacoCodeEditorFramedCfg>}
   */
  class FramedEditorImpl extends FramedBaseImpl {
    /**
     * @param  {PrimeFaces.PartialWidgetCfg<PrimeFaces.widget.ExtMonacoCodeEditorFramedCfg>} cfg Arguments as passed by PrimeFaces.
     */
    constructor(cfg) {
      super(cfg);
    }

    // === PUBLIC API, see monaco-editor.d.ts

    // === PROTECTED

    /**
     * @protected
     * @returns {string}
     */
    _getEditorType() {
      return "codeEditor";
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
     * @param {InitBaseMessageData} initMessageData
     * @param {Partial<PrimeFaces.widget.ExtMonacoCodeEditorFramedCfg>} cloneableCfg
     */
    _postInit(initMessageData, cloneableCfg) {
      this._postMessage({
        kind: "init",
        data: {
          ...initMessageData,
          options: cloneableCfg,
        },
      });
    }

    /**
     * @protected
     * @returns {Promise<string>}
     */
    async _invokeGetValue() {
      return this.invokeMonaco("getValue");
    }

    /**
     * @protected
     * @returns {Promise<void>}
     */
    async _invokeLayout() {
      return this.invokeMonaco("layout")
    }

    /**
     * @protected
     * @template {PrimeFaces.MatchingKeys<import("monaco-editor").editor.IStandaloneCodeEditor, (...args: never[]) => unknown>} K
     * @param {K} method
     * @param {PrimeFaces.widget.ExtMonacoEditor.ParametersIfFn<import("monaco-editor").editor.IStandaloneCodeEditor[K]>} args
     * @returns {Promise<Awaited<PrimeFaces.widget.ExtMonacoEditor.ReturnTypeIfFn<import("monaco-editor").editor.IStandaloneCodeEditor[K]>>>}
     */
    async _invokeMonaco(method, ...args) {
      return this._postPromise({
        kind: "invokeMonaco",
        data: {
          args,
          messageId: this._createMessageId(),
          method,
        },
      });
    }

    /**
     * @protected
     * @param {string} value
     * @returns {Promise<void>}
     */
    async _invokeSetValue(value) {
      return this.invokeMonaco("setValue", value);
    }
  }

  /**
   * @extends {FramedBaseImpl<import("monaco-editor").editor.IStandaloneDiffEditor, import("monaco-editor").editor.IStandaloneDiffEditorConstructionOptions, PrimeFaces.widget.ExtMonacoEditor.IframeDiffEditorContext, PrimeFaces.widget.ExtMonacoEditor.ExtenderDiffEditorFramed, PrimeFaces.widget.ExtMonacoDiffEditorFramedCfg>}
   */
  class FramedDiffEditorImpl extends FramedBaseImpl {
    /**
     * @param  {PrimeFaces.PartialWidgetCfg<PrimeFaces.widget.ExtMonacoDiffEditorFramedCfg>} cfg Arguments as passed by PrimeFaces.
     */
    constructor(cfg) {
      super(cfg);

      /** @type {string | undefined} */
      this._originalEditorValue;

      /** @type {JQuery | undefined} */
      this._originalInput;

      /** @type {number | undefined} */
      this._originalScrollTop;
    }

    /**
     * @param {PrimeFaces.widget.ExtMonacoDiffEditorFramedCfg} cfg 
     */
    init(cfg) {
      super.init(cfg);

      // Textarea with the value for the original editor
      this._originalInput = this.jq.find(".ui-helper-hidden-accessible textarea").eq(1);

      // Default to the given value for the original editor
      this._originalEditorValue = String(this._originalInput.val() || "");
    }

    // === PUBLIC API, see monaco-editor.d.ts

    /**
     * @returns {JQuery}
     */
    getOriginalInput() {
      return this._originalInput || $();
    }

    /**
     * @returns {Promise<string>}
     */
    async getOriginalValue() {
      if (this.isReady()) {
        return this.invokeMonacoScript(editor => editor.getOriginalEditor().getValue());
      }
      else {
        return this._originalEditorValue || "";
      }
    }

    /**
     * @param {string} value 
     * @returns {Promise<void>}
     */
    async setOriginalValue(value) {
      if (this.isReady()) {
        return this.invokeMonacoScript((editor, val) => editor.getOriginalEditor().setValue(val), value);
      }
      else {
        this._originalEditorValue = value;
        return undefined;
      }
    }

    // === PROTECTED

    /**
     * @protected
     * @returns {string}
     */
    _getEditorType() {
      return "diffEditor";
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
     * @param {InitBaseMessageData} initMessageData
     * @param {Partial<PrimeFaces.widget.ExtMonacoDiffEditorFramedCfg>} cloneableCfg
     */
    _postInit(initMessageData, cloneableCfg) {
      this._postMessage({
        kind: "initDiff",
        data: {
          ...initMessageData,
          options: cloneableCfg,
          originalScrollTop: this._originalScrollTop || 0,
          originalValue: this._originalEditorValue || "",
        },
      });
    }

    /**
     * @protected
     * @returns {Promise<string>}
     */
    async _invokeGetValue() {
      return this.invokeMonacoScript(editor => editor.getModifiedEditor().getValue());
    }

    /**
     * @protected
     * @returns {Promise<void>}
     */
    async _invokeLayout() {
      return this.invokeMonaco("layout")
    }

    /**
     * @protected
     * @template {PrimeFaces.MatchingKeys<import("monaco-editor").editor.IStandaloneDiffEditor, (...args: never[]) => unknown>} K
     * @param {K} method
     * @param {PrimeFaces.widget.ExtMonacoEditor.ParametersIfFn<import("monaco-editor").editor.IStandaloneDiffEditor[K]>} args
     * @returns {Promise<Awaited<PrimeFaces.widget.ExtMonacoEditor.ReturnTypeIfFn<import("monaco-editor").editor.IStandaloneDiffEditor[K]>>>}
     */
    async _invokeMonaco(method, ...args) {
      return this._postPromise({
        kind: "invokeMonacoDiff",
        data: {
          args,
          messageId: this._createMessageId(),
          method,
        },
      });
    }

    /**
     * @protected
     * @param {string} value
     * @returns {Promise<void>}
     */
    async _invokeSetValue(value) {
      return this.invokeMonacoScript((editor, val) => editor.getModifiedEditor().setValue(val), value);
    }

    // === MESSAGING

    /**
     * @protected
     * @param {MonacoMessage} message
     */
    _handleMessage(message) {
      switch (message.payload.kind) {
        case "originalScrollChange":
          this._onMessageOriginalScrollChange(message.payload.data);
          break;
        case "originalValueChange":
          this._onMessageOriginalValueChange(message.payload.data);
          break;
        default:
          console.warn("[MonacoEditor] Unhandled message:", message);
          break;
      }
    }

    /**
     * Called when the scroll position of the original Monaco editor in the iframe has changed.
     * @private
     * @param {ScrollChangeMessageData} data
     */
    _onMessageOriginalScrollChange(data) {
      this._originalScrollTop = data.scrollTop;
    }

    /**
     * Called when the value of the original Monaco editor in the iframe has changed.
     * @private
     * @param {ValueChangeMessageData} data
     */
    _onMessageOriginalValueChange(data) {
      this.getOriginalInput().val(data.value || "");
      this._fireEvent("originalChange", data.changes);
    }
  }

  PrimeFaces.widget.ExtMonacoBaseEditorFramed = FramedBaseImpl;
  PrimeFaces.widget.ExtMonacoCodeEditorFramed = FramedEditorImpl;
  PrimeFaces.widget.ExtMonacoDiffEditorFramed = FramedDiffEditorImpl;

  // TODO remove in one of the next major releases
  // @ts-expect-error legacy, will be removed soon
  PrimeFaces.widget.ExtMonacoEditorFramed
    = FramedEditorImpl;
})();
