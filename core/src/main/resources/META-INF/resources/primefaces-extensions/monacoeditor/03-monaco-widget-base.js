// @ts-check

window.monacoModule = window.monacoModule || {};

(function () {
  /**
   * @template {import("monaco-editor").editor.IEditor} TEditor
   * @template {import("monaco-editor").editor.IEditorOptions} TEditorOpts
   * @template {PrimeFaces.widget.ExtMonacoBaseEditorCfg<TEditorOpts>} TCfg
   * @extends {PrimeFaces.widget.DeferredWidget<TCfg>}
   */
  class BaseImpl extends PrimeFaces.widget.DeferredWidget {
    /**
     * @param  {PrimeFaces.PartialWidgetCfg<TCfg>} cfg Arguments as passed by PrimeFaces.
     */
    constructor(cfg) {
      super(cfg);

      /** @type {PromiseHook<this>[] | undefined} */
      this._onDone;

      /** @type {JQuery | undefined} */
      this._input;

      /** @type {JQuery | undefined} */
      this._editorContainer;

      /** @type {string | undefined} */
      this._editorValue;
    }

    /**
     * @param {PrimeFaces.PartialWidgetCfg<TCfg>} cfg
     * @param {Partial<TCfg>} editorDefaults
     */
    _init(cfg, editorDefaults) {
      super.init(cfg);

      // Set defaults.
      this.cfg = jQuery.extend({}, editorDefaults, this.cfg);

      this._onDone = [];
      this.jq.data("initialized", false);

      // Get elements
      this._input = this.jq.find(".ui-helper-hidden-accessible textarea").first();

      this._editorContainer = this.jq.children(".ui-monaco-editor-ed");

      // Default to the given value
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
      if (this._input === undefined) {
        throw new Error("IllegalState: Widget was not initialized yet.");
      }
      return this._input;
    }

    /**
     * @returns {PrimeFaces.PartialWidgetCfg<TCfg>}
     */
    getWidgetOptions() {
      if (this.cfg === undefined) {
        throw new Error("IllegalState: Widget was not initialized yet.");
      }
      return this.cfg;
    }

    /**
     * @returns {string}
     */
    getWidgetId() {
      return Array.isArray(this.id) ? this.id[0] || "" : this.id;
    }

    /**
     * @returns {JQuery}
     */
    getEditorContainer() {
      if (this._editorContainer === undefined) {
        throw new Error("IllegalState: Widget was not initialized yet.");
      }
      return this._editorContainer;
    }

    /**
     * @returns {boolean}
     */
    isReady() {
      return this.jq.data("initialized");
    }

    /**
     * @returns {Promise<this>}
     */
    whenReady() {
      if (this.isReady()) {
        return Promise.resolve(this);
      }
      else {
        return new Promise((resolve, reject) => {
          this._onDone = this._onDone || [];
          this._onDone.push({ resolve, reject });
        });
      }
    }

    /**
     * @returns {Promise<string>}
     */
    getValue() { throw new Error("Must override abstract method."); }

    /**
     * @param {string} value
     * @returns {Promise<void>}
     */
    setValue(value) { throw new Error("Must override abstract method."); }

    /**
     * @template {PrimeFaces.MatchingKeys<TEditor, (...args: never[]) => unknown>} K
     * @param {K} method
     * @param {PrimeFaces.widget.ExtMonacoEditor.ParametersIfFn<TEditor[K]>} args
     * @returns {Promise<Awaited<PrimeFaces.widget.ExtMonacoEditor.ReturnTypeIfFn<TEditor[K]>>>}
     */
    invokeMonaco(method, ...args) { throw new Error("Must override abstract method."); }

    /**
     * @template TRetVal
     * @template {any[]} TArgs
     * @param {((editor: TEditor, ...args: TArgs) => TRetVal) | string} script
     * @param {TArgs} args
     * @returns {Promise<Awaited<TRetVal>>}
     */
    invokeMonacoScript(script, ...args) { throw new Error("Must override abstract method."); }

    /**
     * @returns {Promise<void>}
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
      if (this.cfg === undefined) {
        throw new Error("IllegalState: Widget was not initialized yet.");
      }
      const onName = `on${eventName.toLowerCase()}`;
      this.jq.trigger("monacoEditor:" + eventName, params);
      /** @type {Record<string, unknown>} */
      const cfg = this.cfg;
      const callback = cfg[onName];
      if (typeof callback === "function") {
        callback.apply(this, params || []);
      }
      this.callBehavior(eventName, {
        params: params || []
      });
    }

    /**
     * @returns {string[]}
     */
    _listSupportedEvents() {
      if (this.cfg === undefined) {
        throw new Error("IllegalState: Widget was not initialized yet.");
      }
      if (this.cfg.availableEvents === undefined) {
        return [];
      }
      return this.cfg.availableEvents.filter(event => this._supportsEvent(event));
    }

    /**
     * @param {string} eventName 
     * @returns {boolean}
     */
    _supportsEvent(eventName) {
      if (this.cfg === undefined) {
        throw new Error("IllegalState: Widget was not initialized yet.");
      }
      const onName = `on${eventName.toLowerCase()}`;
      /** @type {Record<string, unknown>} */
      const cfg = this.cfg;
      const callback = cfg[onName];
      return this.hasBehavior(eventName) || typeof callback === "function";
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
     * @returns {Promise<void>}
     */
    async _onInitSuccess() {
      this.jq.data("initialized", true);
      await this.setValue(this._editorValue || "");
      this._fireEvent("initialized");
      for (const { resolve } of this._onDone || []) {
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
      for (const { reject } of this._onDone || []) {
        reject(error);
      }
      this._onDone = [];
    }
  }

  window.monacoModule.ExtMonacoEditorBase = BaseImpl;
  PrimeFaces.widget.ExtMonacoBaseEditor = BaseImpl;

  return BaseImpl;
})();