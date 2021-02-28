// @ts-check

window.monacoModule = window.monacoModule || {};

PrimeFaces.widget.ExtMonacoEditorFramed = (function () {

  const {
    FramedEditorDefaults,
    getFacesResourceUri,
    getMonacoResource,
    resolveLocaleUrl,
  } = window.monacoModule.helper;
  const ExtMonacoEditorBase = window.monacoModule.ExtMonacoEditorBase;

  let InstanceId = 0;
  let MessageId = 0;

  /**
   * @extends {ExtMonacoEditorBase<PrimeFaces.widget.ExtMonacoEditorFramedCfg>}
   */
  class FramedImpl extends ExtMonacoEditorBase {
    /**
     * @param  {...any[]} args Arguments as passed by PrimeFaces.
     */
    constructor(...args) {
      super(...args);
    }

    /**
     * @param {Partial<typeof FramedEditorDefaults>} cfg
     */
    init(cfg) {
      super._init(cfg, FramedEditorDefaults);

      this.addRefreshListener(() => this._onRefresh());
      this.addDestroyListener(() => this._onDestroy());

      this._instanceId = ++InstanceId;
      /** @type {Map<number, {resolve: (value: any) => void, reject: (error: string) => void}>} */
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
     * @return {Promise<void>}
     */
    async layout() {
      if (this.isReady()) {
        return this.invokeMonaco("layout");
      }
      else {
        return undefined;
      }
    }

    /**
     * @template {keyof import("monaco-editor").editor.IStandaloneCodeEditor} K
     * @param {K} method
     * @param {Parameters<import("monaco-editor").editor.IStandaloneCodeEditor[K]>} args
     * @return {Promise<PrimeFaces.UnwrapPromise<ReturnType<import("monaco-editor").editor.IStandaloneCodeEditor[K]>>>}
     */
    async invokeMonaco(method, ...args) {
      if (!this.isReady()) {
        throw new Error(`Cannot invoke monaco as the editor is not ready yey. Use isReady / whenReady to check.`);
      }
      return this._postPromise({
        kind: "invokeMonaco",
        data: {
          method,
          args,
        },
      });
    }

    /**
     * @template TRetVal
     * @template {any[]} TArgs
     * @param {((editor: import("monaco-editor").editor.IStandaloneCodeEditor, ...args: TArgs) => TRetVal) | string} script
     * @param {TArgs} args
     * @return {Promise<PrimeFaces.UnwrapPromise<TRetVal>>}
     */
    async invokeMonacoScript(script, ...args) {
      if (!this.isReady()) {
        throw new Error(`Cannot invoke monaco as the editor is not ready yey. Use isReady / whenReady to check.`);
      }
      return this._postPromise({
        kind: "invokeMonacoScript",
        data: {
          script: script.toString(),
          args,
        },
      });
    }

    /**
     * @return {Promise<string>}
     */
    async getValue() {
      if (this.isReady()) {
        return this.invokeMonaco("getValue");
      }
      else {
        return this._editorValue;
      }
    }

    /**
     * @param {string} value
     * @return {Promise<void>}
     */
    async setValue(value) {
      if (this.isReady()) {
        return this.invokeMonaco("setValue", value);
      }
      else {
        this._editorValue = value;
        return undefined;
      }
    }

    // === INTERNAL

    _bindEvents() {
      this._postMessage({
        kind: "bindEvents",
        data: {},
      });
    }

    _onRefresh() {
      this._onDestroy();
    }

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
     * @return {HTMLIFrameElement}
     */
    _getIframe() {
      const iframe = this.getEditorContainer().get(0);
      // @ts-ignore
      return iframe;
    }

    _render() {
      this._postMessage({
        kind: "init",
        data: {
          facesResourceUri: getFacesResourceUri(),
          id: this.id,
          nonce: PrimeFaces.csp.NONCE_VALUE,
          options: this._createCloneableOptions(),
          resolvedLocaleUrl: this._resolvedLocaleUrl,
          resourceUrlExtension: PrimeFaces.resources.getResourceUrlExtension(),
          scrollTop: this._scrollTop,
          supportedEvents: this._listSupportedEvents(),
          value: this._editorValue,
          version: PrimeFacesExt.VERSION,
        },
      });
    }

    /**
     * A subset of the widget configuration that can be sent to the iframe.
     * @returns {Partial<PrimeFaces.widget.ExtMonacoEditorFramedCfgBase>}
     */
    _createCloneableOptions() {
      /** @type {Partial<PrimeFaces.widget.ExtMonacoEditorFramedCfgBase>} */
      const options = {};
      for (const key of Object.keys(this.cfg)) {
        const value = this.cfg[key];
        if (key !== "behaviors" && typeof value !== "function") {
          options[key] = value;
        }
      }
      return options;
    }

    // === MESSAGING

    /**
     * Post a message to the monaco editor iframe via `postMessage`, and get a
     * promise that resolves with the response.
     * @param {MonacoMessage} message
     * @return {Promise<any>}
     */
    _postPromise(message) {
      const messageId = this._postMessage(message);
      return new Promise((resolve, reject) => {
        this._responseMap.set(messageId, { resolve, reject });
      });
    }

    /**
     * Post a message to the monaco editor iframe via `postMessage`.
     * @param {MonacoMessage} message
     */
    _postMessage(message) {
      const iframe = this._getIframe();
      if (iframe && iframe.contentWindow) {
        const messageId = ++MessageId;
        iframe.contentWindow.postMessage({
          instanceId: this._instanceId,
          messageId,
          ...message,
        }, window.location.href);
        return messageId;
      }
      else {
        return -1;
      }
    }

    /**
     * Callback when the iframe sends a message via `postMessage`
     * @param {MessageEvent} event
     */
    _onMessage(event) {
      if (typeof event.data === "object" && typeof event.data.kind === "string") {
        /** @type {MonacoMessage} */
        const message = event.data;
        if (this._instanceId >= 0 && message.instanceId >= 0 && this._instanceId !== message.instanceId) {
          // this is normal when there are multiple editors all sending messages
          return;
        }
        switch (message.kind) {
          case "load":
            this._onMessageLoad();
            break;
          case "response":
            this._onMessageResponse(message.messageId, message.data);
            break;
          case "valueChange":
            this._onMessageValueChange(message.data);
            break;
          case "scrollChange":
            this._onMessageScrollChange(message.data);
            break;
          case "domEvent":
            this._onMessageDomEvent(message.data);
            break;
          case "afterInit":
            this._onMessageAfterInit(message.data);
            break;
          default:
            console.warn("[MonacoEditor] Unhandled message", event.data);
        }
      }
    }

    /**
     * Called when the iframe document is ready.
     */
    _onMessageLoad() {
      this.renderDeferred();
    }

    /**
     * Called when the iframe sends a response to a message.
     * @param {number} messageId
     * @param {ResponseMessageData} data
     */
    _onMessageResponse(messageId, data) {
      if (this._responseMap.has(messageId)) {
        const { resolve, reject } = this._responseMap.get(messageId);
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
     * @param {ValueChangeMessageData} data
     */
    _onMessageValueChange(data) {
      this.getInput().val(data.value || "");
      this._fireEvent("change", data.changes);
    }

    /**
     * Called when the scroll position of the monaco editor in the iframe has changed.
     * @param {ScrollChangeMessageData} data
     */
    _onMessageScrollChange(data) {
      this._scrollTop = data.scrollTop;
    }

    /**
     * Called when a DOM even such as `blur` or `keyup` was triggered on the
     * monaco editor in the iframe.
     * @param {DomEventMessageData} data
     */
    _onMessageDomEvent(data) {
      const args = data.data ? JSON.parse(data.data) : [];
      this._fireEvent(data.name, ...args);
    }

    /**
     * Called after the monaco editor in the iframe was initialized, and also
     * in case an error occurred.
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

  return FramedImpl;
})();
