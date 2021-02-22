// @ts-check

/// <reference types="jquery" />
/// <reference types="monaco-editor" />

// === Extensions to the window scope

interface Window {
  /**
   * The official API of the Monaco editor, exposed as a global variable.
   */
  monaco: typeof import("monaco-editor");

  /**
   * Exposes the internal API of monaco editor. May be used to customize the editor even further.
   *
   * __THIS IS UNSUPPORTED AND MAY BE CHANGED WITHOUT NOTICE. MAKE SURE YOU KNOW WHAT YOU ARE DOING AND USE AT YOUR OWN RISK.__
   */
  monacoExtras: any;

  /**
   * Monaco editor environment, which contains the locale and some factory functions.
   */
  MonacoEnvironment: import("monaco-editor").Environment;
}

// === General helper types

declare namespace PrimeFaces {
  /**
   * @param T Type of the newly created instance.
   * @return A newable callable that produces `T`.
   */
  type Constructor<T> = new (...args: any[]) => T;

  /**
   * @param T Type to wrap in a promise.
   * @return When `T` is a promise type, the type `T` itself. Otherwise, the type `T` wrapped in a promise.
   */
  type Promisify<T> = T extends Promise<any> ? T : Promise<T>;

  /**
   * @param T Type to unwrap.
   * @return When `T` is a promise type, the type of the promised value. Otherwise, the type `T` itself.
   */
  type UnwrapPromise<T> = T extends Promise<infer R> ? R : T;
}

// === Monaco editor widget

declare namespace PrimeFaces {
  namespace widget {
    namespace ExtMonacoEditorBase {
      /**
       * Type signature for the factory method that creates the web worker for the Monaco editor. 
       */
      type WorkerFactory = (moduleId: string, label: string) => Worker;

      /**
       * Interface for objects that provide asynchronous access to a monaco editors instance, such as the
       * widget instance for the monaco editor in the iframe that can communicate with the editor in the
       * iframe only via `postMessage`. Note that objects that provide synchronous access usually also
       * implement this interface for convenience when you wish to write generic code that works with both
       * types of editors.  
       */
      interface AsyncMonacoContext {
        /** @return The ID of the Monaco editor widget. */
        getWidgetId(): string;

        /** @return The current options for the Monaco editor widget. */
        getWidgetOptions(): PrimeFaces.widget.ExtMonacoEditorBaseCfgBase;

        /**
         * Gets the current value (code) of the Monaco editor. May be called as soon as this widget is accessible,
         * even when the Monaco editor was not loaded or initialized yet. In that case, either the initial value
         * or the latest value set by {@link setValue} will be returned.
         * 
         * This method will always fetch the value asynchronously, even when called on the inline Monaco editor.
         * 
         * Note that for the inline editor, you can also get the value synchronously via `getValueNow`. This method
         * provides interoperability with the framed monaco editor and is available for both the inline and framed
         * monaco editor.
         */
        getValue(): Promise<string>;

        /**
         * Invokes the given method on the monaco editor instance in the iframe, and returns the result. As the
         * communication with the iframes is done via `postMessage`, the result is returned asynchronously.
         * @template K Type of the method to invoke.
         * @param method A method of the monaco editor instance to invoke.
         * @param args Arguments that are passed to the method.
         * @return A promise that resolves with the value returned by the given method. The promise is rejected when
         * the editor is not ready yet.
         */
        invokeMonaco<K extends keyof import("monaco-editor").editor.IStandaloneCodeEditor>(
          method: K, ...args: Parameters<import("monaco-editor").editor.IStandaloneCodeEditor[K]>
        ): Promise<UnwrapPromise<ReturnType<import("monaco-editor").editor.IStandaloneCodeEditor[K]>>>;

        /**
         * Invokes the given script on the Monaco editor instance in the iframe, and returns the result. As the
         * communication with the iframes is done via `postMessage`, the result is returned asynchronously.
         * 
         * Note that if a function is given for the script, it is converted to a string, sent to the framed editor
         * and executed. Closing over variables in the lambda is therefore NOT supported. Explicitly specify those
         * variables as the arguments, they will be passed on to the iframe.
         * @template TRetVal Type of the value returned by the script.
         * @template TArgs Type of the arguments required by the script. 
         * @param method A method of the monaco editor instance to invoke. This method receives the `IStandaloneCodeEditor`
         * instance as the first argument, and the given `args` as additional arguments.
         * @param args Arguments that are passed to the method.
         * @return A promise that resolves with the value returned by the given method.
         */
        invokeMonacoScript<TRetVal, TArgs extends any[]>(script: string | ((editor: import("monaco-editor").editor.IStandaloneCodeEditor, ...args: TArgs) => TRetVal), ...args: TArgs): Promise<UnwrapPromise<TRetVal>>;

        /**
         * @return `true` when the editor was already loaded and initialized and can be interacted with via
         * `getMonaco()`, `false` otherwise.
         */
        isReady(): boolean;

        /**
         * Performs a layout on the monaco editor. Useful when the surrounding container has changed its dimensions
         * etc. Does nothing when the editor was not rendered yet.
         * 
         * This method will always initiate the layout asynchronously, even when called on the inline editor.
         * 
         * @return A promise that resolves immediately when the editor is not ready, or when the layout command
         * was issued to the editor otherwise.
         */
        layout(): Promise<void>;

        /**
         * Sets the value of this editor. May be called as soon as this widget is accessible, even when the
         * Monaco editor was not loaded or initialized yet. The value will be set on the editor once it
         * becomes ready.
         * 
         * This method will always set the value asynchronously, even when called on the inline Monaco editor.
         * 
         * Note that for the inline editor, you can also set the value synchronously via `setValueNow`. This method
         * provides interoperability with the framed Monaco editor and is available for both the inline and framed
         * monaco editor.
         * @param newValue The new value to set.
         * @return A promise that resolves when the value was set.
         */
        setValue(newValue: string): Promise<void>;

        /**
         * @return A promise that is resolved once the editor has finished loading and was created successfully.
         * When the editor is already initialized, this promise will resolve as soon as possible.
         */
        whenReady(): Promise<this>;
      }

      /**
       * Interface for objects that provide synchronous access to a monaco editor instance, such as the
       * widget instance for the inline monaco editor. Note that objects that provide synchronous access
       * usually also implement the asynchronous interface for convenience when you wish to write generic
       * code that works with both types of editors.  
       */
      interface SyncMonacoContext extends AsyncMonacoContext {
        /**
         * @return The extender that was set for this monaco editor widget, if any. It can be used to customize
         * the editor via JavaScript.
         */
        getExtender(): MonacoExtenderInline | undefined;

        /**
         * Finds the current instance of the monaco editor, if it was created already. Use this to interact with the
         * editor via JavaScript. See also the
         * [monaco editor API docs](https://microsoft.github.io/monaco-editor/api/index.html).
         * @return The current monaco editor instance. `undefined` in case the editor was not created yet.
         */
        getMonaco(): import("monaco-editor").editor.IStandaloneCodeEditor | undefined;

        /**
         * Gets the value of this editor immediately, without a promise. This is possible only for the
         * inline editor, hence why this method only exists on the inline editor. May be called as soon
         * as this widget is accessible, even when the monaco editor was not loaded or initialized yet.
         * @return The current value of this editor.
         */
        getValueNow(): string;

        /**
         * Sets the value on the editor now, synchronously. This is possible only for the inline editor,
         * hence why this method only exists on the inline editor. May be called as soon as this widget
         * is accessible, even when the monaco editor was not loaded or initialized yet.
         * @param value The new value for the editor.
         */
        setValueNow(value: string): void;

        /**
         * Calls the given handler with the current monaco editor instance if it exists.
         * @template TReturn Type of the return value of the given handler.
         * @param handler Handler that is invoked with the current monaco editor instance.
         * @param defaultReturnValue Default value that is returned when no editor exists currently.
         * @return The return value of the handler, or `undefined` if no editor exists.
         * @throws When the handler throws an error.
         */
        withMonaco<TReturn>(handler: (editor: import("monaco-editor").editor.IStandaloneCodeEditor) => TReturn, defaultReturnValue: TReturn): TReturn;

        /**
         * Calls the given handler with the current monaco editor instance if it exists and catches possible errors.
         * @template TReturn Type of the return value of the given handler.
         * @param handler Handler that is invoked with the current monaco editor instance.
         * @param defaultReturnValue Default value that is returned when no editor exists currently, or when the
         * handler throws.
         * @return The return value of the handler, or the default return value if either no editor exists or the
         * handler throws an error.
         */
        tryWithMonaco<TReturn>(handler: (editor: import("monaco-editor").editor.IStandaloneCodeEditor) => TReturn, defaultReturnValue: TReturn): TReturn;
      }

      /**
       * Data passed to the extender in the {@link MonacoExtenderBase.createModel} method.
       */
      interface CreateModelOptions {
        /** Resolved options for the monaco editor. */
        editorOptions: import("monaco-editor").editor.IStandaloneEditorConstructionOptions;

        /** Code language for the model. */
        language: string;

        /** Default URI for the model. */
        uri: import("monaco-editor").Uri;

        /** Initial value for the model. */
        value: string;
      }

      /**
       * An extender object to further customize the monaco editor via JavaScript. Specified via the `extender` attribute
       * on the `<p:monacoEditor />` tag.
       *
       * All callback methods in the extender are optional, if not specified, corresponding defaults are used.
       *
       * @template TContext Type of the context object that is passed to all callback methods.
       */
      interface ExtenderBase<TContext extends SyncMonacoContext> {
        /**
         * This method is called after the editor was created.
         * @param context The current context object.
         * @param wasLibLoaded `true` if the monaco editor library was reloaded, `false` otherwise. In case it was reloaded,
         * you may want to setup some language defaults again.
         */
        afterCreate?: (context: TContext, wasLibLoaded: boolean) => void;

        /**
         * Called after the editor was destroyed; and also when updating a component via AJAX.
         * @param context The current context object. Note that you should not use it to retrieve the monaco editor
         * instance, as the editor has already been destroyed.
         */
        afterDestroy?: (context: TContext) => void;

        /**
         * Called before monaco editor is created. This method is passed the current options object that would be used to
         * initialize the monaco editor.
         *
         * If this callback does not return a value, the options that were passed are used. You may modify the
         * options in-place.
         *
         * If it returns a new options object, that options object is used.
         *
         * If it returns a Thenable or Promise, the monaco editor is created only once the Promise resolves (successfully).
         * If the Promise returns a new options object, these options are used to create the editor.
         *
         * See
         * [IStandaloneEditorConstructionOptions](https://microsoft.github.io/monaco-editor/api/interfaces/import("monaco-editor").editor.istandaloneeditorconstructionoptions.html)
         * for all editor options.
         *
         * @param context The current context object. Note that you should not use it to retrieve the monaco editor
         * instance, as the editor was not created yet.
         * @param options The current options that would be used to create the editor.
         * @param wasLibLoaded `true` if the monaco editor library was reloaded, `false` otherwise. In case it was reloaded, you
         * may want to setup some language defaults again.
         * @return Either `undefined` to use the options as passed; a new options object to be used for creating the editor;
         * or a Promise that may return the new options.
         */
        beforeCreate?: (context: TContext, options: import("monaco-editor").editor.IStandaloneEditorConstructionOptions, wasLibLoaded: boolean) => import("monaco-editor").languages.ProviderResult<import("monaco-editor").editor.IStandaloneEditorConstructionOptions>;

        /**
         * Called before the editor is destroyed, eg. when updating a component via AJAX.
         * @param context The current context object.
         */
        beforeDestroy?: (context: TContext) => void;

        /**
        * Called when a worker for additional language support needs to be created. By default, monaco editor ships with
        * the workers for JSON, CSS, HTML, and TYPESCRIPT. The label is the name of the language, eg. `css` or
        * `javascript`. This method must return the worker to be used for the given language.
        *
        * @param context The current context object. Note that you should not use it to retrieve the monaco editor
        * instance, as the editor was not created yet.
        * @param moduleId Module ID for the worker. Useful only with the AMD version, can be ignored.
        * @param label Label of the language for which to create the worker.
        * @return The worker to be used for the given code language.
        */
        createWorker?: (context: TContext, moduleId: string, label: string) => Worker;

        /**
         * Called when monaco editor is created. May return an object with services that should be overridden. See
         * [here on github](https://github.com/Microsoft/monaco-editor/issues/935#issuecomment-402174095) for details
         * on the available services.
         * @param context The current context object. Note that you should not use it to retrieve the monaco editor
         * instance, as the editor was not created yet.
         * @param options The options that will be used to create the editor. and must not be changed.
         * @return The override options to be used. If `undefined` is returned, no editor override services are used.
         */
        createEditorOverrideServices?: (context: TContext, options: Readonly<import("monaco-editor").editor.IStandaloneEditorConstructionOptions>) => import("monaco-editor").editor.IEditorOverrideServices | undefined;

        /**
         * Called when the model needs to be fetched. The default implementation attempts to find an existing model for the
         * given URI in the monaco store (`import("monaco-editor").editor.getModel`). If it cannot be found, it creates a new model
         * (`import("monaco-editor").editor.createModel`). Finally it sets the default value on the model. This method can be used to create a
         * custom when necessary, with possibly a different URI.
         *
         * If you implement this callback, you SHOULD set the initial value (`data.value`) on the model, it will NOT be set
         * automatically.
         * @param context The current context object. Note that you should not use it to retrieve the monaco editor
         * instance, as the editor was not created yet.
         * @param options Options with the default URI, the current value, and the editor configuration.
         * @return The retrieved or created model. When `undefined`, the default mechanism to create the model ist used.
         */
        createModel?: (context: TContext, options: CreateModelOptions) => import("monaco-editor").editor.ITextModel | undefined
      }
      /**
       * Extender for the inline editor that has direct access to the editor.
       */
      interface MonacoExtenderInline extends widget.ExtMonacoEditorBase.ExtenderBase<widget.ExtMonacoEditorInline> {
      }

      /**
       * Extender for the framed editor that must communicate via postMessage with the editor.
       */
      interface ExtenderFramed extends widget.ExtMonacoEditorBase.ExtenderBase<IframeContext> {
      }

      interface IframeContext extends widget.ExtMonacoEditorBase.SyncMonacoContext {
        /**
         * The extender that was set for this monaco editor widget. It can be used to customize the editor via
         * JavaScript.
         */
        extender: ExtenderFramed;
      }
    }
  }
}


// === PrimeFaces integration

declare namespace PrimeFaces {
  namespace widget {
    /**
     * Base configuration for both Monaco editor widgets, without the properties from the {@link BaseWidgetCfg}.
     * These properties mostly correspond to the values set on the widget in your XHTML view.
     */
    interface ExtMonacoEditorBaseCfgBase {
      /** List of events for which a client or server side listener exists. */
      availableEvents: string[];

      /**
       * Whether the monaco editor is resized automatically. Please not that this requires the browser to
       * support for [ResizeObserver](https://developer.mozilla.org/en-US/docs/Web/API/ResizeObserver)
       */
      autoResize: boolean;

      /**
       * Basename for the URI used for the model.
       */
      basename: string;

      /** Map between a theme name and the data for that theme. */
      customThemes: Record<string, import("monaco-editor").editor.IStandaloneThemeData>;

      /**
       * Directory (path) for the URI used for the model.
       */
      directory: string;

      /**
       * The options that were used to construct the monaco editor instance.
       */
      editorOptions: Readonly<import("monaco-editor").editor.IStandaloneEditorConstructionOptions>;

      /**
       * Extension for the URI used for the model.
       */
      extension: string;

      /**
       * The extender for enhancing the editor with client-side functionality. Exact type
       * depends on whether the inlined or framed editor is used.
       */
      extender: unknown;

      /**
       * Whether the editor is currently disabled.
       */
      disabled: boolean;

      /**
       * The code language tag, eg. `css` or `javascript`. See  also `monaco.language.getLangauges`.
       */
      language: string;

      /**
       * The code of the current UI language of the monaco editor, eg. `en` or `de`.
       */
      locale: string;

      /**
       * Whether the editor is currently read-only.
       */
      readonly: boolean;

      /**
       * Scheme (protocol) for the URI used for the model.
       */
      scheme: string;

      /**
       * Tab index for the editor.
       */
      tabIndex: number;

      /**
       * The Uri to the locale file with the translations for the current language.
       */
      localeUrl: string;
    }

    /**
     * Base configuration for {@link ExtMonacoEditorFramed} widget, without the properties from the {@link BaseWidgetCfg}.
     */
    interface ExtMonacoEditorFramedCfgBase extends ExtMonacoEditorBaseCfgBase {
      /**
       * URL to the extender for this monaco editor widget.
       * Either `undefined` or an URL that points to a script file that sets `window.MonacoEnvironment` to the
       * extender to be used.
       */
      extender: string;
    }

    /**
     * Base configuration for {@link ExtMonacoEditorInline} widget, without the properties from the {@link BaseWidgetCfg}.
     */
    interface ExtMonacoEditorInlineCfgBase extends ExtMonacoEditorBaseCfgBase {
      /**
       * Factory function that creates the extender for this monaco editor widget.
       * Either `undefined` or JavaScript code that evaluates to the extender.
       */
      extender: () => ExtMonacoEditorBase.MonacoExtenderInline;
    }

    /**
     * Base configuration for both Monaco editor widgets, with the properties from the {@link BaseWidgetCfg}.
     * These properties mostly correspond to the values set on the widget in your XHTML view.
     */
    interface ExtMonacoEditorBaseCfg extends ExtMonacoEditorBaseCfgBase, BaseWidgetCfg { }

    /**
     * Base configuration for {@link ExtMonacoEditorFramed} widget, with the properties from the {@link BaseWidgetCfg}.
     */
    interface ExtMonacoEditorFramedCfg extends ExtMonacoEditorFramedCfgBase, BaseWidgetCfg { }

    /**
     * Base configuration for {@link ExtMonacoEditorInline} widget, with the properties from the {@link BaseWidgetCfg}.
     */
    interface ExtMonacoEditorInlineCfg extends ExtMonacoEditorInlineCfgBase, BaseWidgetCfg { }

    /**
     * __PrimeFacesExtensions MonacoEditorBase Widget__
     * 
     * The MonacoEditorBase widget is the base class for both the inline and the framed Monaco editor widget.
     * The Monaco code editor is an advanced code editor with support vor IntelliSense.
     */
    class ExtMonacoEditorBase<TCfg extends widget.ExtMonacoEditorBaseCfg> extends DeferredWidget<TCfg> implements PrimeFaces.widget.ExtMonacoEditorBase.AsyncMonacoContext {
      /**
       * @return The HTML container element holding the editor. It exists even if the editor was not created yet.
       */
      getEditorContainer(): JQuery;

      /**
       * @return The hidden textarea holding the value of the editor (eg. the code being edited, this is also the
       * value that is sent when the form is submitted).
       */
      getInput(): JQuery;

      // copied from the interface, see there for docs
      getWidgetId(): string;
      getWidgetOptions(): PrimeFaces.widget.ExtMonacoEditorBaseCfgBase;
      getValue(): Promise<string>;
      invokeMonaco<K extends keyof import("monaco-editor").editor.IStandaloneCodeEditor>(
        method: K, ...args: Parameters<import("monaco-editor").editor.IStandaloneCodeEditor[K]>
      ): Promise<UnwrapPromise<ReturnType<import("monaco-editor").editor.IStandaloneCodeEditor[K]>>>;
      invokeMonacoScript<TRetVal, TArgs extends any[]>(script: string | ((editor: import("monaco-editor").editor.IStandaloneCodeEditor, ...args: TArgs) => TRetVal), ...args: TArgs): Promise<UnwrapPromise<TRetVal>>;
      isReady(): boolean;
      layout(): Promise<void>;
      setValue(newValue: string): Promise<void>;
      whenReady(): Promise<this>;
    }
    
    /**
     * __PrimeFacesExtensions MonacoEditorFramed Widget__
     * 
     * The MonacoEditorFramed widget is the inline variant of the Monaco editor widget. It renders
     * the editor directly in the page where the widget is used.
     * 
     * The Monaco code editor is an advanced code editor with support vor IntelliSense.
     */
    class ExtMonacoEditorFramed extends ExtMonacoEditorBase<widget.ExtMonacoEditorFramedCfg> {
    }

    /**
     * __PrimeFacesExtensions MonacoEditorFramed Widget__
     * 
     * The MonacoEditorFramed widget is the framed variant of the Monaco editor widget. It renders
     * the editor inside an iframe for improved encapsulation.
     * 
     * The Monaco code editor is an advanced code editor with support vor IntelliSense.
     */
    class ExtMonacoEditorInline extends ExtMonacoEditorBase<widget.ExtMonacoEditorInlineCfg> implements ExtMonacoEditorBase.SyncMonacoContext {
      // copied from the interface, see there for docs
      getWidgetId(): string;
      getWidgetOptions(): PrimeFaces.widget.ExtMonacoEditorBaseCfgBase;
      getValue(): Promise<string>;
      invokeMonaco<K extends keyof import("monaco-editor").editor.IStandaloneCodeEditor>(
        method: K, ...args: Parameters<import("monaco-editor").editor.IStandaloneCodeEditor[K]>
      ): Promise<UnwrapPromise<ReturnType<import("monaco-editor").editor.IStandaloneCodeEditor[K]>>>;
      invokeMonacoScript<TRetVal, TArgs extends any[]>(script: string | ((editor: import("monaco-editor").editor.IStandaloneCodeEditor, ...args: TArgs) => TRetVal), ...args: TArgs): Promise<UnwrapPromise<TRetVal>>;
      isReady(): boolean;
      layout(): Promise<void>;
      setValue(newValue: string): Promise<void>;
      whenReady(): Promise<this>;
      getExtender(): PrimeFaces.widget.ExtMonacoEditorBase.MonacoExtenderInline | undefined;
      getMonaco(): import("monaco-editor").editor.IStandaloneCodeEditor | undefined;
      getValueNow(): string;
      setValueNow(value: string): void;
      withMonaco<TReturn>(handler: (editor: import("monaco-editor").editor.IStandaloneCodeEditor) => TReturn, defaultReturnValue: TReturn): TReturn;
      tryWithMonaco<TReturn>(handler: (editor: import("monaco-editor").editor.IStandaloneCodeEditor) => TReturn, defaultReturnValue: TReturn): TReturn;
    }
  }
}
