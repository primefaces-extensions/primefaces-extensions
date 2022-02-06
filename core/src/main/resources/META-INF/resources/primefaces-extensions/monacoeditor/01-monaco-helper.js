// @ts-check

window.monacoModule = window.monacoModule || {};

window.monacoModule.helper = (function () {

  /**
   * Map between the language label and the worker that should be used.
   * @type {Record<string, string>}
   */
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
    :
    /**
     * @param {object} object 
     * @param  {...any[]} moreObjects 
     * @returns {any} 
     */
    (object, ...moreObjects) => {
      /** @type {Record<string, unknown>} */
      const result = {};
      for (const obj of [object, ...moreObjects]) {
        for (const key of Object.keys(obj)) {
          result[key] = (/** @type {Record<string, unknown>}*/(obj))[key];
        }
      }
      return result;
    }

  /**
   * The `??` operator.
   * @template T
   * @template S
   * @param {T | undefined | null} lhs 
   * @param {S} rhs 
   * @returns {T | S}
   */
  function defaultIfNullish(lhs, rhs) {
    return lhs !== undefined && lhs !== null ? lhs : rhs;
  }

  /**
   * @param {unknown} x 
   * @returns {x is null | undefined}
   */
  function isNullOrUndefined(x) {
    return x === null || x === undefined;
  }

  /**
   * @template T
   * @param {T | null | undefined} x 
   * @returns {x is T}
   */
  function isNotNullOrUndefined(x) {
    return x !== null && x !== undefined;
  }
  /**
   * @param {string} moduleId 
   * @param {string} label 
   * @returns {string}
   */
  function getScriptName(moduleId, label) {
    return defaultIfNullish(workerMap[label], "editor.worker.js");
  }

  /**
   * Checks whether a string ends with a certain suffix.
   * @param {string} string 
   * @param {string} suffix 
   * @returns {boolean}
   */
  function endsWith(string, suffix) {
    var this_len = string.length;
    return string.substring(this_len - suffix.length, this_len) === suffix;
  }

  /**
   * @returns {string} Base URL to the Faces resource servlet.
   */
  function getFacesResourceUri() {
    const res = PrimeFaces.resources.getFacesResource("", "", "0");
    const idx = res.lastIndexOf(`.${PrimeFaces.resources.getResourceUrlExtension()}`);
    return idx >= 0 ? res.substring(0, idx) : res;
  }

  /**
   * @param {string} url URL from which to load the script.
   * @returns {Promise<void>} The loaded JavaScript.
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
   * @template {import("monaco-editor").editor.IEditor} TEditor
   * @template {import("monaco-editor").editor.IEditorOptions} TEditorOpts
   * @template {PrimeFaces.widget.ExtMonacoEditor.SyncMonacoBaseEditorContext<TEditor, TEditorOpts>} TContext
   * @template {PrimeFaces.widget.ExtMonacoEditor.ExtenderBaseEditor<TEditor, TEditorOpts, TContext>} TExtender
   * @param {TEditor | undefined} editor
   * @param {Partial<PrimeFaces.widget.ExtMonacoBaseEditorCfgBase<TEditorOpts>>} options The extender string as
   * specified on the component.
   * @param {TContext | undefined} context
   * @returns {Partial<TExtender>}
   */
  function loadExtender(editor, options, context) {
    const extenderString = options.extender;

    // No extender given
    if (typeof extenderString === "undefined" || extenderString === "") {
      return {};
    }

    // Extender was evaluated already
    if (typeof extenderString === "object") {
      return /** @type {Partial<TExtender>} */(extenderString);
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
   * @param {Partial<PrimeFaces.widget.ExtMonacoBaseEditorCfgBase<import("monaco-editor").editor.IEditorOptions>>} options
   * @returns {string}
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
   * @param {Partial<PrimeFaces.widget.ExtMonacoBaseEditorCfgBase<import("monaco-editor").editor.IEditorOptions>>} options
   * @returns {Promise<{forceLibReload: boolean, localeUrl: string}>}
   */
  async function loadLanguage(options) {
    // Load language file, if requested.
    const localeUrl = resolveLocaleUrl(options);
    const language = typeof MonacoEnvironment === "object" && typeof MonacoEnvironment.Locale === "object"
      ? MonacoEnvironment.Locale.language
      : undefined;

    if (localeUrl && language !== options.locale) {
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
          forceLibReload: language !== options.locale,
          localeUrl: ""
        };
      }
    }
    else {
      return {
        forceLibReload: language !== options.locale,
        localeUrl: ""
      };
    }
  }

  /**
   * @param {string} resource
   * @param {Record<string, number | string | string[]>} queryParams
   * @returns {string}
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
   * @param {Partial<PrimeFaces.widget.ExtMonacoBaseEditorCfgBase<import("monaco-editor").editor.IEditorOptions>>} options
   * @param {boolean} forceLibReload If true, loads the monaco editor again, even if it is loaded already. This is
   * necessary in case the language changes.
   * @returns {Promise<boolean>} Whether the monaco library was (re)loaded.
   */
  async function loadEditorLib(options, forceLibReload) {
    if (!("monaco" in window) || forceLibReload) {
      if (!options.locale) {
        MonacoEnvironment = typeof MonacoEnvironment === "object" ? MonacoEnvironment : {};
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
   * Finds the default dir name for the Monaco text model for a widget component by using the ID of the component.
   * @template {import("monaco-editor").editor.IEditor} TEditor
   * @template {import("monaco-editor").editor.IEditorOptions} TEditorOpts
   * @param {PrimeFaces.widget.ExtMonacoEditor.AsyncMonacoBaseEditorContext<TEditor, TEditorOpts>} context 
   * @returns {string}
   */
  function deriveDirForComponent(context) {
    const id = context.getWidgetId();
    return String(id).replace(/[^a-zA-Z0-9_-]/g, "/");
  }

  /**
   * Evaluate the `beforeCreate` callback of the extender. It is passed the current options and may
   *
   * - not return anything, in which case the passed options are used (which may have been modified inplace)
   * - return an options object, in which case these options are used
   * - return a promise, in which case the editor is initialized only once that promise resolves. The promise
   *   may resolve to a new options object to be used.
   *
   * @template {PrimeFaces.widget.ExtMonacoEditor.SyncMonacoDiffEditorContext} TContext
   * @template {PrimeFaces.widget.ExtMonacoEditor.ExtenderDiffEditor<TContext>} TExtender
   * @param {TContext} context
   * @param {TExtender} extender
   * @param {string} originalValue
   * @param {string} modifiedValue
   * @param {boolean} wasLibLoaded
   * @returns {Promise<EditorInitData<import("monaco-editor").editor.IStandaloneDiffEditorConstructionOptions, DiffEditorCustomInitData>>}
   */
  async function createDiffEditorInitData(context, extender, originalValue, modifiedValue, wasLibLoaded) {
    const opts = context.getWidgetOptions();

    /** @type {import("monaco-editor").editor.IStandaloneDiffEditorConstructionOptions} */
    const incomingEditorOptions = typeof opts.editorOptions === "object" ?
      opts.editorOptions :
      typeof opts.editorOptions === "string" ?
        JSON.parse(opts.editorOptions) :
        {};

    const originalModel = createModel(context, incomingEditorOptions, extender, originalValue, {
      basename: opts.originalBasename,
      directory: opts.originalDirectory,
      extension: opts.originalExtension,
      language: opts.originalLanguage || opts.language,
      scheme: opts.originalScheme,
      createDefaultDir: context => deriveDirForComponent(context) + "/original",
      createExtenderModel: (context, extender, options) => typeof extender.createOriginalModel === "function"
        ? extender.createOriginalModel(context, options)
        : undefined,
    });

    const modifiedModel = createModel(context, incomingEditorOptions, extender, modifiedValue, {
      basename: opts.basename,
      directory: opts.directory,
      extension: opts.extension,
      language: opts.language,
      scheme: opts.scheme,
      createDefaultDir: context => deriveDirForComponent(context) + "/modified",
      createExtenderModel: (context, extender, options) => typeof extender.createModel === "function"
        ? extender.createModel(context, options)
        : undefined
    });


    // XHTML attribute takes precedence over the editor options
    const originalDisabled = opts.originalDisabled !== undefined
      ? opts.originalDisabled
      : !defaultIfNullish(incomingEditorOptions.originalEditable, false);
    const originalReadonly = defaultIfNullish(opts.originalReadonly, false);

    /** @type {import("monaco-editor").editor.IStandaloneDiffEditorConstructionOptions} */
    const baseEditorOptions = {
      domReadOnly: opts.readonly || opts.disabled,
      readOnly: opts.readonly || opts.disabled,
      originalEditable: !originalDisabled && !originalReadonly,
    };

    let editorOptions = assign(baseEditorOptions, incomingEditorOptions);
    if (incomingEditorOptions.tabIndex) {
      editorOptions.tabIndex = typeof incomingEditorOptions.tabIndex === "number"
        ? incomingEditorOptions.tabIndex
        : parseInt(String(incomingEditorOptions.tabIndex));
    }

    // Allow extender to modify / update the options
    if (typeof extender.beforeCreate === "function") {
      const result = extender.beforeCreate(context, editorOptions, wasLibLoaded);
      if (typeof result === "object" && result !== null) {
        editorOptions = "then" in result ? defaultIfNullish(await result, editorOptions) : result;
      }
    }

    return {
      custom: {
        modifiedModel,
        originalModel,
      },
      options: editorOptions,
    };
  }

  /**
   * Evaluate the `beforeCreate` callback of the extender. It is passed the current options and may
   *
   * - not return anything, in which case the passed options are used (which may have been modified inplace)
   * - return an options object, in which case these options are used
   * - return a promise, in which case the editor is initialized only once that promise resolves. The promise
   *   may resolve to a new options object to be used.
   *
   * @template {PrimeFaces.widget.ExtMonacoEditor.SyncMonacoCodeEditorContext} TContext
   * @template {PrimeFaces.widget.ExtMonacoEditor.ExtenderCodeEditor<TContext>} TExtender
   * @param {TContext} context
   * @param {TExtender} extender
   * @param {string} editorValue
   * @param {boolean} wasLibLoaded
   * @returns {Promise<import("monaco-editor").editor.IStandaloneEditorConstructionOptions>}
   */
  async function createEditorConstructionOptions(context, extender, editorValue, wasLibLoaded) {
    const opts = context.getWidgetOptions();
    /** @type {import("monaco-editor").editor.IStandaloneEditorConstructionOptions} */
    const incomingEditorOptions = typeof opts.editorOptions === "object" ?
      opts.editorOptions :
      typeof opts.editorOptions === "string" ?
        JSON.parse(opts.editorOptions) :
        {};

    const model = createModel(context, incomingEditorOptions, extender, editorValue, {
      basename: opts.basename,
      directory: opts.directory,
      extension: opts.extension,
      language: opts.language,
      scheme: opts.scheme,
      createDefaultDir: context => deriveDirForComponent(context),
      createExtenderModel: (context, extender, options) => typeof extender.createModel === "function"
        ? extender.createModel(context, options)
        : undefined,
    });
    /** @type {import("monaco-editor").editor.IStandaloneEditorConstructionOptions} */
    const baseEditorOptions = {
      domReadOnly: defaultIfNullish(opts.readonly, false) || defaultIfNullish(opts.disabled, false),
      model: model,
      readOnly: defaultIfNullish(opts.readonly, false) || defaultIfNullish(opts.disabled, false),
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
        return "then" in result ? defaultIfNullish(await result, editorOptions) : result;
      }
    }

    return editorOptions;
  }

  /**
   * @template {import("monaco-editor").editor.IEditor} TEditor
   * @template {import("monaco-editor").editor.IEditorOptions} TEditorOpts
   * @template {PrimeFaces.widget.ExtMonacoEditor.SyncMonacoBaseEditorContext<TEditor, TEditorOpts>} TContext
   * @template {PrimeFaces.widget.ExtMonacoEditor.ExtenderBaseEditor<TEditor, TEditorOpts, TContext>} TExtender
   * @param {TContext} context
   * @param {TEditorOpts} editorOptions
   * @param {TExtender} extender
   * @param {string} value
   * @param {ModelConfig<TEditor, TEditorOpts, TContext, TExtender>} modelConfig
   * @returns {import("monaco-editor").editor.ITextModel}
   */
  function createModel(context, editorOptions, extender, value, modelConfig) {
    const language = modelConfig.language || "plaintext";
    // Scheme, path, basename and extension
    /** @type {string} */
    let scheme;
    /** @type {string} */
    let dir;
    /** @type {string} */
    let basename;
    /** @type {string} */
    let extension;

    if (modelConfig.directory) {
      dir = modelConfig.directory;
    }
    else {
      dir = modelConfig.createDefaultDir(context, extender);
      dir = dir + "/" + language.replace(/[^a-zA-Z0-9_-]/g, "/");
    }
    if (modelConfig.basename) {
      basename = modelConfig.basename;
    }
    else {
      basename = "file";
    }
    if (modelConfig.extension) {
      extension = modelConfig.extension;
    }
    else {
      const langInfo = monaco.languages.getLanguages().filter(lang => lang.id === language)[0];
      extension = langInfo !== undefined && langInfo.extensions !== undefined && langInfo.extensions.length > 0
        ? langInfo.extensions[0]
        : "";
    }

    if (modelConfig.scheme) {
      scheme = modelConfig.scheme;
    }
    else {
      scheme = "inmemory";
    }

    // Build path and uri
    if (dir.length > 0 && dir[dir.length - 1] !== "/") {
      dir = dir + "/";
    }
    // Replace multiple consecutive slashes with a single slash
    dir = dir.replace(/\/\/+/g, "/");
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

    const modelFromExtender = modelConfig.createExtenderModel(context, extender, { editorOptions, language, uri, value });
    if (typeof modelFromExtender === "object" && modelFromExtender !== null) {
      return modelFromExtender;
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
   * @template {import("monaco-editor").editor.IEditor} TEditor
   * @template TRet
   * @template {any[]} TArgs
   * @param {TEditor} editor
   * @param {((editor: TEditor, ...args: TArgs) => TRet) | string} script
   * @param {TArgs} args
   * @param {(script: string) => void} evalFn 
   * @returns {Promise<Awaited<TRet>>}
   */
  async function invokeMonacoScript(editor, script, args, evalFn) {
    // Make sure we always call the editor asynchronously
    await Promise.resolve(undefined);
    MonacoEnvironment = typeof MonacoEnvironment === "object" ? MonacoEnvironment : {};
    const wrappedScript = `try{MonacoEnvironment._INVOKE.return=(${script}).apply(window,MonacoEnvironment._INVOKE.args)}catch(e){MonacoEnvironment._INVOKE.error=e}`;
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
   * @template {import("monaco-editor").editor.IEditor} TEditor
   * @template {PrimeFaces.MatchingKeys<TEditor, (...args: never[]) => unknown>} K
   * @param {K} method
   * @param {TEditor} editor
   * @param {PrimeFaces.widget.ExtMonacoEditor.ParametersIfFn<TEditor[K]>} args
   * @returns {Promise<Awaited<PrimeFaces.widget.ExtMonacoEditor.ReturnTypeIfFn<TEditor[K]>>>}
   */
  async function invokeMonaco(editor, method, args) {
    // Make sure we always call the editor asynchronously
    await Promise.resolve(undefined);
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
   * Register all given custom themes with the editor.
   * @param {Record<string, import("monaco-editor").editor.IStandaloneThemeData> | undefined} customThemes
   */
  function defineCustomThemes(customThemes) {
    /** @type {Record<string, import("monaco-editor").editor.IStandaloneThemeData>} */
    const customThemesData = customThemes !== undefined ? customThemes : {};
    for (const themeName of Object.keys(customThemesData || {})) {
      const themeData = assign({}, DefaultThemeData, customThemesData[themeName]);
      if (themeData !== undefined) {
        monaco.editor.defineTheme(themeName, themeData);
      }
    }
  }

  /**
   * @param {unknown} value 
   * @returns {value is MonacoMessage}
   */
  function isMonacoMessage(value) {
    if (value === null || value === undefined) {
      return false;
    }
    if (typeof value !== "object") {
      return false;
    }
    const message = /** @type {Record<string, unknown>} */(value);
    if (typeof message.instanceId !== "number") {
      return false;
    }
    if (typeof message.payload !== "object" || message.payload === null) {
      return false;
    }
    const payload = /** @type {Record<string, unknown>} */(message.payload);
    if (typeof payload.kind !== "string") {
      return false;
    }
    if (!("data" in payload)) {
      return false;
    }
    return true;
  }

  /**
   * Splits the given string at the first occurrence of the given separator. Returns the substring before and after
   * the separator.
   * @param {string} str String to split.
   * @param {string} separator Separator at which to split.
   * @return {[string, string | undefined]} The string before the separator and the string after the separator. When
   * the string does not contain the separator, the first element is the entire string and the second element is
   * `undefined`.
   */
  function splitFirst(str, separator) {
    const index = str.indexOf(separator);
    return index !== -1 ? [str.slice(0, index), str.slice(index + 1)] : [str, undefined];
  }

  /**
   * @param {string} str 
   * @return {Record<string, string | undefined>}
   */
  function parseSimpleQuery(str) {
    str = str.trim().replace(/^[?#&]/, '');
    if (str.length === 0) {
      return {};
    }
    /** @type {Record<string, string | undefined>} */
    const ret = {};
    for (const param of str.split('&')) {
      const [key, val] = splitFirst(param.replace(/\+/g, ' '), "=");
      ret[key] = val !== undefined ? decodeURIComponent(val) : val;
    };
    return ret;
  }

  /**
   * Default options for the monaco editor widget configuration.
   * @type {PrimeFaces.widget.ExtMonacoBaseEditorCfgBase<import("monaco-editor").editor.IEditorOptions>}
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

  /** @type {PrimeFaces.widget.ExtMonacoDiffEditorCfgBase} */
  const DiffEditorDefaults = {
    originalDisabled: true,
    originalReadonly: false,
    originalRequired: false,
    originalLanguage: "",
    originalBasename: "",
    originalDirectory: "",
    originalExtension: "",
    originalScheme: "inmemory",
  };

  /** @type {PrimeFaces.widget.ExtMonacoDiffEditorFramedCfgBase} */
  const FramedDiffEditorDefaults = assign({}, BaseEditorDefaults, DiffEditorDefaults, {
    extender: "",
    iframeUrlParams: {},
  });

  /** @type {PrimeFaces.widget.ExtMonacoCodeEditorFramedCfgBase} */
  const FramedEditorDefaults = assign({}, BaseEditorDefaults, {
    extender: "",
    iframeUrlParams: {},
  });

  /** @type {PrimeFaces.widget.ExtMonacoDiffEditorInlineCfgBase} */
  const InlineDiffEditorDefaults = assign({}, BaseEditorDefaults, DiffEditorDefaults, {
    extender: () => ({}),
    overflowWidgetsDomNode: "",
  });

  /** @type {PrimeFaces.widget.ExtMonacoCodeEditorInlineCfgBase} */
  const InlineEditorDefaults = assign({}, BaseEditorDefaults, {
    extender: () => ({}),
    overflowWidgetsDomNode: "",
  });

  return {
    assign,
    createDiffEditorInitData,
    createEditorConstructionOptions,
    createModel,
    defineCustomThemes,
    endsWith,
    getFacesResourceUri,
    getMonacoResource,
    getScript,
    getScriptName,
    globalEval,
    invokeMonaco,
    invokeMonacoScript,
    isMonacoMessage,
    isNotNullOrUndefined,
    isNullOrUndefined,
    parseSimpleQuery,
    loadEditorLib,
    loadExtender,
    loadLanguage,
    resolveLocaleUrl,
    BaseEditorDefaults,
    DefaultThemeData,
    FramedDiffEditorDefaults,
    FramedEditorDefaults,
    InlineDiffEditorDefaults,
    InlineEditorDefaults,
  };
})();