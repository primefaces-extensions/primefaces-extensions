// @ts-check

window.monacoModule = window.monacoModule || {};

window.monacoModule.helper = (function () {

  /** Map between the language label and the worker that should be used. */
  const workerMap = {
    css: "css.worker.js",
    handlebars: "html.worker.js",
    html: "html.worker.js",
    javascript: "ts.worker.js",
    json: "json.worker.js",
    less: "css.worker.js",
    razor: "html.worker.js",
    scss: "css.worker.js",
    typescript: "ts.worker.js",
  }

  /**
   * Polyfill for older browsers.
   * @type {typeof Object.assign}
   */
  const assign = typeof Object.assign === "function"
    ? Object.assign
    : (object, ...moreObjects) => {
      const result = {};
      for (const obj of [object, ...moreObjects]) {
        for (const key of Object.keys(obj)) {
          result[key] = obj[key];
        }
      }
      return result;
    }

  /**
   * @param {moduleId} label 
   * @param {string} label 
   * @return {string}
   */
  function getScriptName(moduleId, label) {
    return workerMap[label] || "editor.worker.js";
  }

  function endsWith(string, suffix) {
    var this_len = string.length;
    return string.substring(this_len - suffix.length, this_len) === suffix;
  }

  /**
   * @return {string} Base URL to the Faces resource servlet.
   */
  function getFacesResourceUri() {
    const res = PrimeFaces.resources.getFacesResource("", "", "0");
    const idx = res.lastIndexOf(`.${PrimeFaces.resources.getResourceUrlExtension()}`);
    return idx >= 0 ? res.substring(0, idx) : res;
  }

  /**
   * @param {string} url URL from which to load the script.
   * @return {Promise<void>} The loaded JavaScript.
   */
  async function getScript(url) {
    const script = await jQuery.ajax({
      type: "GET",
      url: url,
      dataType: "text",
      cache: true,
      async: true
    });
    PrimeFaces.csp.eval(script);
  }

  /**
   * Loads the extender for this monaco editor. It may be either
   *
   * - an empty string or undefined, in which case no extender is loaded
   * - a JavaScript expression that evaluates to an extender object
   * - a path to a resource (eg. `extender.js.xhtml?ln=mylib`) that evaluates to an extender object
   *
   * @param {Partial<PrimeFaces.widget.ExtMonacoEditorBaseCfgBase>} options The extender string as specified on the component.
   * @return {Partial<PrimeFaces.widget.ExtMonacoEditorBase.ExtenderBase>}
   */
  function loadExtender(options) {
    const extenderString = options.extender;

    // No extender given
    if (typeof extenderString === "undefined" || extenderString === "") {
      return {};
    }

    // Extender was evaluated already
    if (typeof extenderString === "object") {
      return extenderString;
    }

    // Try creating an extender from the factory
    if (typeof extenderString === "function") {
      try {
        const extender = extenderString();
        if (typeof extender === "object") {
          return extender;
        }
      }
      catch (e) {
        console.warn("[MonacoEditor] Extender must be an expression that evaluates to MonacoExtender", e);
      }
    }

    // Could not create extender.

    console.warn("[MonacoEditor] Extender was specified, but could not be loaded: ", extenderString);
    return {};
  }

  /**
   * @param {Partial<PrimeFaces.widget.ExtMonacoEditorBaseCfgBase>} options
   * @return {string}
   */
  function resolveLocaleUrl(options) {
    // Load language file, if requested.
    if (options.locale) {
      // Determine URI for loading the locale.
      return options.localeUrl
        ? options.localeUrl
        : getMonacoResource(`locale/${options.locale}.js`);
    }
    else {
      return "";
    }
  }

  /**
   * @param {Partial<PrimeFaces.widget.ExtMonacoEditorBaseCfgBase>} options
   * @return {Promise<{forceLibReload: boolean, localeUrl: string}>}
   */
  async function loadLanguage(options) {
    // Load language file, if requested.
    const localeUrl = resolveLocaleUrl(options);
    if (localeUrl && MonacoEnvironment.Locale.language !== options.locale) {
      try {
        await getScript(localeUrl);
        return {
          forceLibReload: true,
          localeUrl: localeUrl,
        };
      }
      catch (e) {
        console.error(`[MonacoEditor] Could not fetch localization file ${localeUrl}. Falling back to default English locale.`);
        options.locale = "en";
        options.localeUrl = undefined;
        return {
          forceLibReload: MonacoEnvironment.Locale.language !== options.locale,
          localeUrl: ""
        };
      }
    }
    else {
      return {
        forceLibReload: MonacoEnvironment.Locale.language !== options.locale,
        localeUrl: ""
      };
    }
  }

  /**
   * @param {string} resource
   * @param {Record<string, number | string | string[]>} queryParams
   * @return {string}
   */
  function getMonacoResource(resource, queryParams = {}) {
    // WARNING: Do not use the variable name "url", this will be replaced via maven during the build process!!!
    const resolvedUrl = PrimeFaces.resources.getFacesResource(`/monacoeditor/${resource}`, "primefaces-extensions", PrimeFacesExt.VERSION);
    const params = [];
    for (const key of Object.keys(queryParams)) {
      const queryParam = queryParams[key];
      const values = Array.isArray(queryParam) ? queryParam : [queryParam];
      for (const value of values) {
        if (value !== undefined && (typeof value !== "string" || value.length > 0)) {
          params.push(`${key}=${encodeURIComponent(value)}`);
        }
      }
    }
    // faces resources URL already contains at least one URL parameter "ln"
    // so no ? needed, we can use &
    return params.length > 0 ? `${resolvedUrl}&${params.join("&")}` : resolvedUrl;
  }

  /**
   * @param {Partial<PrimeFaces.widget.ExtMonacoEditorBaseCfgBase>} options
   * @param {boolean} forceLibReload If true, loads the monaco editor again, even if it is loaded already. This is necessary in case the language changes.
   * @return {Promise<boolean>} Whether the monaco library was (re)loaded.
   */
  async function loadEditorLib(options, forceLibReload) {
    if (!("monaco" in window) || forceLibReload) {
      if (!options.locale) {
        MonacoEnvironment.Locale = {
          language: "",
          data: {},
        };
      }
      const uriEditor = getMonacoResource("editor.js");
      await getScript(uriEditor);
      return true;
    }
    else {
      return false;
    }
  }

  /**
   * Evaluate the `beforeCreate` callback of the extender. It is passed the current options and may
   *
   * - not return anything, in which case the passed options are used (which may have been modified inplace)
   * - return an options object, in which case these options are used
   * - return a promise, in which case the editor is initialized only once that promise resolves. The promise
   *   may resolve to a new options object to be used.
   *
   * @param {PrimeFaces.widget.ExtMonacoEditorBase.SyncMonacoContext} context
   * @param {PrimeFaces.widget.ExtMonacoEditorBase.ExtenderBase} extender
   * @param {string} editorValue
   * @param {boolean} wasLibLoaded
   * @return {Promise<import("monaco-editor").editor.IEditorConstructionOptions>}
   */
  async function createEditorConstructionOptions(context, extender, editorValue, wasLibLoaded) {
    const opts = context.getWidgetOptions();
    /** @type {import("monaco-editor").editor.IStandaloneEditorConstructionOptions} */
    const incomingEditorOptions = typeof opts.editorOptions === "object" ?
      opts.editorOptions :
      typeof opts.editorOptions === "string" ?
        JSON.parse(opts.editorOptions) :
        {};

    const model = createModel(context, incomingEditorOptions, extender, editorValue);
    /** @type {import("monaco-editor").editor.IStandaloneEditorConstructionOptions} */
    const baseEditorOptions = {
      domReadOnly: opts.readonly || opts.disabled,
      model: model,
      readOnly: opts.readonly || opts.disabled,
    };
    const editorOptions = assign(baseEditorOptions, incomingEditorOptions);
    if (incomingEditorOptions.tabIndex) {
      editorOptions.tabIndex = typeof incomingEditorOptions.tabIndex === "number"
        ? incomingEditorOptions.tabIndex
        : parseInt(String(incomingEditorOptions.tabIndex));
    }

    if (typeof extender.beforeCreate === "function") {
      const result = extender.beforeCreate(context, editorOptions, wasLibLoaded);
      if (typeof result === "object" && result !== null) {
        return typeof result["then"] === "function"
          ? (await result) || editorOptions
          : result;
      }
    }

    return editorOptions;
  }

  /**
   * @param {PrimeFaces.widget.ExtMonacoEditorBase.SyncMonacoContext} context
   * @param {import("monaco-editor").editor.IEditorConstructionOptions} editorOptions
   * @param {PrimeFaces.widget.ExtMonacoEditorBase.ExtenderBase} extender
   * @param {string} value
   * @returns {import("monaco-editor").editor.ITextModel}
   */
  function createModel(context, editorOptions, extender, value = "") {
    const options = context.getWidgetOptions();
    const id = context.getWidgetId();

    const language = options.language || "plaintext";
    // Scheme, path, basename and extension
    /** @type {string} */
    let scheme;
    /** @type {string} */
    let dir;
    /** @type {string} */
    let basename;
    /** @type {string} */
    let extension;

    if (options.directory) {
      dir = options.directory;
    }
    else {
      dir = String(id).replace(/[^a-zA-Z0-9_-]/g, "/");
    }
    if (basename) {
      basename = options.basename;
    }
    else {
      basename = "file";
    }
    if (options.extension) {
      extension = options.extension;
    }
    else {
      const langInfo = monaco.languages.getLanguages().filter(lang => lang.id === language)[0];
      extension = langInfo && langInfo.extensions.length > 0 ? langInfo.extensions[0] : "";
    }

    if (options.scheme) {
      scheme = options.scheme;
    }
    else {
      scheme = "inmemory";
    }

    // Build path and uri
    if (dir.length > 0 && dir[dir.length - 1] !== "/") {
      dir = dir + "/";
    }
    if (extension.length > 0 && extension[0] !== ".") {
      extension = "." + extension;
    }
    if (endsWith(basename, extension)) {
      extension = "";
    }

    const uri = monaco.Uri.from({
      scheme: scheme,
      path: dir + basename + extension
    });

    // Allow extender to fetch model
    if (typeof extender.createModel === "function") {
      const modelFromExtender = extender.createModel(context, { editorOptions, language, uri, value });
      if (typeof modelFromExtender === "object" && modelFromExtender !== null) {
        return modelFromExtender;
      }
    }
    // Get or create model, and set previous value
    let model = monaco.editor.getModel(uri);
    if (!model) {
      model = monaco.editor.createModel(value, language, uri);
    }
    model.setValue(value);
    return model;
  }

  /**
   * @param {unknown} x 
   * @return {x is null | undefined}
   */
  function isNullOrUndefined(x) {
    return x === null || x === undefined;
  }

  /**
   * @template T
   * @param {T | null | undefined} x 
   * @return {x is T}
   */
  function isNotNullOrUndefined(x) {
    return x !== null && x !== undefined;
  }

  /**
   * @template TRet
   * @template {any[]} TArgs
   * @param {import("monaco-editor").editor.IStandaloneCodeEditor} editor
   * @param {((editor: import("monaco-editor").editor.IStandaloneCodeEditor, ...args: TArgs) => TRet) | string} script
   * @param {TArgs} args
   * @param {(script: string) => void} evalFn 
   * @return {Promise<PrimeFaces.UnwrapPromise<TRet>>}
   */
  async function invokeMonacoScript(editor, script, args, evalFn) {
    const wrappedScript = `try {MonacoEnvironment._INVOKE.return = (${script}).apply(window,MonacoEnvironment._INVOKE.args)}catch(e){MonacoEnvironment._INVOKE.error=e}`;
    try {
      MonacoEnvironment._INVOKE = {
        args: [editor, ...args],
        error: undefined,
        return: undefined,
      }
      evalFn(wrappedScript);
      if (MonacoEnvironment._INVOKE.error) {
        throw MonacoEnvironment._INVOKE.error;
      }
      else {
        return await MonacoEnvironment._INVOKE.return;
      }
    }
    finally {
      delete MonacoEnvironment._INVOKE;
    }
  }

  /**
   * @param {string} script 
   * @param {string} [nonce] 
   */
  function globalEval(script, nonce) {
    const scriptNode = document.createElement("script");
    scriptNode.type = "text/javascript";
    scriptNode.textContent = script;
    if (nonce !== undefined && nonce.length > 0) {
      scriptNode.nonce = nonce;
    }
    try {
      document.body.appendChild(scriptNode);
    }
    finally {
      document.body.removeChild(scriptNode);
    }
  }

  /**
   * @template {keyof import("monaco-editor").editor.IStandaloneCodeEditor} K
   * @param {K} method
   * @param {import("monaco-editor").editor.IStandaloneCodeEditor} editor
   * @param {Parameters<import("monaco-editor").editor.IStandaloneCodeEditor[K]>} args
   * @return {Promise<PrimeFaces.UnwrapPromise<ReturnType<import("monaco-editor").editor.IStandaloneCodeEditor[K]>>>}
   */
  async function invokeMonaco(editor, method, args) {
    if (editor) {
      const fn = editor[method];
      if (typeof fn === "function") {
        // @ts-ignore
        return await fn.apply(editor, args);
      }
      else {
        throw new Error(`Method ${method} does not exist on the editor.`);
      }
    }
    else {
      throw new Error("Method called on non-existing monaco editor.");
    }
  }

  /**
   * Default options for the monaco editor widget configuration.
   * @type {PrimeFaces.widget.ExtMonacoEditorBaseCfgBase}
   */
  const BaseEditorDefaults = {
    availableEvents: [],
    autoResize: false,
    basename: "",
    customThemes: {},
    directory: "",
    disabled: false,
    editorOptions: {},
    extender: undefined,
    extension: "",
    language: "plaintext",
    scheme: "inmemory",
    tabIndex: undefined,
    readonly: false,
    locale: "",
    localeUrl: "",
  };

  /** @type {import("monaco-editor").editor.IStandaloneThemeData} */
  const DefaultThemeData = {
    base: "vs",
    colors: {},
    inherit: true,
    rules: [],
  };


  /** @type {PrimeFaces.widget.ExtMonacoEditorFramedCfgBase} */
  const FramedEditorDefaults = assign({}, BaseEditorDefaults, {
    extender: "",
    iframeUrlParams: {},
  });

  /** @type {PrimeFaces.widget.ExtMonacoEditorInlineCfgBase} */
  const InlineEditorDefaults = assign({}, BaseEditorDefaults, {
    extender: () => ({}),
    overflowWidgetsDomNode: "",
  });

  return {
    assign,
    createEditorConstructionOptions,
    createModel,
    endsWith,
    getFacesResourceUri,
    getMonacoResource,
    getScript,
    getScriptName,
    globalEval,
    invokeMonaco,
    invokeMonacoScript,
    isNotNullOrUndefined,
    isNullOrUndefined,
    loadEditorLib,
    loadExtender,
    loadLanguage,
    resolveLocaleUrl,
    BaseEditorDefaults,
    DefaultThemeData,
    FramedEditorDefaults,
    InlineEditorDefaults,
  };
})();