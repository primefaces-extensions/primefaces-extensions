// @ts-check

window.monacoModule = window.monacoModule || {};

window.monacoModule.ExtMonacoEditorBase = (function () {
  /**
   * @template {PrimeFaces.widget.ExtMonacoEditorBaseCfg} T
   * @extends {PrimeFaces.widget.DeferredWidget<T>}
   */
  class BaseImpl extends PrimeFaces.widget.DeferredWidget {
    /**
     * @param  {PrimeFaces.PartialWidgetCfg<T>} cfg Arguments as passed by PrimeFaces.
     */
    constructor(cfg) {
      super(cfg);
    }

    /**
     * @param {PrimeFaces.PartialWidgetCfg<T>} cfg
     * @param {Partial<T>} editorDefaults
     */
    _init(cfg, editorDefaults) {
      super.init(cfg);

      // Set defaults.
      /** @type {T} */
      this.cfg = jQuery.extend({}, editorDefaults, this.cfg);

      /** @type {PromiseHook<this>[]} */
      this._onDone = [];
      this.jq.data("initialized", false);
      this._scrollTop = this._scrollTop || 0;

      // Get elements
      /** @type {JQuery} */
      this._input = this.jq.find(".ui-helper-hidden-accessible textarea");
      /** @type {JQuery} */
      this._editorContainer = this.jq.children(".ui-monaco-editor-ed");

      // Default to the given value
      /** @type {string} */
      this._editorValue = String(this.getInput().val() || "");

      // English is the default.
      if (this.cfg.locale === "en") {
        this.cfg.locale = "";
      }
    }

    // === PUBLIC API, see monaco-editor.d.ts

    /**
     * @returns {JQuery}
     */
    getInput() {
      return this._input;
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
      return Array.isArray(this.id) ? this.id[0] || "" : this.id;
    }

    /**
     * @returns {JQuery}
     */
    getEditorContainer() {
      return this._editorContainer;
    }

    /**
     * @return {boolean}
     */
    isReady() {
      return this.jq.data("initialized");
    }

    /**
     * @return {Promise<this>}
     */
    whenReady() {
      if (this.isReady()) {
        return Promise.resolve(this);
      }
      else {
        return new Promise((resolve, reject) => {
          this._onDone.push({ resolve, reject });
        });
      }
    }

    /**
     * @return {Promise<string>}
     */
    getValue() { throw new Error("Must override abstract method."); }

    /**
     * @param {string} value
     * @return {Promise<void>}
     */
    setValue(value) { throw new Error("Must override abstract method."); }

    /**
     * @template {keyof import("monaco-editor").editor.IStandaloneCodeEditor} K
     * @param {K} method
     * @param {Parameters<import("monaco-editor").editor.IStandaloneCodeEditor[K]>} args
     * @return {Promise<PrimeFaces.UnwrapPromise<ReturnType<import("monaco-editor").editor.IStandaloneCodeEditor[K]>>>}
     */
    invokeMonaco(method, ...args) { throw new Error("Must override abstract method."); }

    /**
     * @template TRetVal
     * @template {any[]} TArgs
     * @param {((editor: import("monaco-editor").editor.IStandaloneCodeEditor, ...args: TArgs) => TRetVal) | string} script
     * @param {TArgs} args
     * @return {Promise<PrimeFaces.UnwrapPromise<TRetVal>>}
     */
    invokeMonacoScript(script, ...args) { throw new Error("Must override abstract method."); }

    /**
     * @return {Promise<void>}
     */
    layout() { throw new Error("Must override abstract method."); }

    // === INTERNAL

    /**
     * Sets up all event listeners on the Monaco editor that are required.
     */
    _bindEvents() { throw new Error("Must override abstract method."); }

    /**
     * @param {string} eventName
     * @param  {PrimeFaces.ajax.RequestParameter[]} params
     */
    _fireEvent(eventName, ...params) {
      const onName = "on" + eventName;
      this.jq.trigger("monacoEditor:" + eventName, params);
      if (typeof this.cfg[onName] === "function") {
        this.cfg[onName].apply(this, params || []);
      }
      this.callBehavior(eventName, {
        params: params || []
      });
    }

    /**
     * @return {string[]}
     */
    _listSupportedEvents() {
      return this.cfg.availableEvents.filter(event => this._supportsEvent(event));
    }

    /**
     * @param {string} eventName 
     * @return {boolean}
     */
    _supportsEvent(eventName) {
      return this.hasBehavior(eventName) || this.cfg[`on${eventName}`];
    }

    _rejectOnDone() {
      const onDone = this._onDone;
      this._onDone = [];
      if (Array.isArray(onDone) && onDone.length > 0) {
        const error = new Error("Monaco editor was destroyed / refreshed before whenReady could be resolved");
        for (const item of onDone) {
          item.reject(error);
        }
      }
    }

    /**
     * Should be called once the editor was created.
     */
    async _onInitSuccess() {
      this._fireEvent("initialized");
      this.jq.data("initialized", true);
      await this.setValue(this._editorValue);
      for (const { resolve } of this._onDone) {
        resolve(this);
      }
      this._bindEvents();
      this._onDone = [];
    }

    /**
     * Should be called in case initialization fails.
     * @param {unknown} error
     */
    _onInitError(error) {
      if (error === "render_canceled_by_update") {
        error = new Error("Monaco editor was refreshed / destroyed before it could be rendered");
      }
      else {
        console.error("[MonacoEditor] Failed to initialize monaco editor", error);
      }
      for (const { reject } of this._onDone) {
        reject(error);
      }
      this._onDone = [];
    }
  }

  return BaseImpl;
})();