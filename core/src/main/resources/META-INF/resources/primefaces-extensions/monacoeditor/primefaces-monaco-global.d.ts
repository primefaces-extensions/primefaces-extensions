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
   * __THIS IS UNSUPPORTED AND MAY BE CHANGED WITHOUT NOTICE. MAKE SURE YOU KNOW WHAT YOU ARE DOING AND USE AT YOUR OWN
   * RISK.__
   */
  monacoExtras: any;

  /**
   * Monaco editor environment, which contains the locale and some factory functions.
   */
  MonacoEnvironment: import("monaco-editor").Environment;
}

// === Monaco editor widget

declare namespace PrimeFaces {
  namespace widget {
    namespace ExtMonacoEditor {
      // === General helper types

      type ParametersIfFn<T> = T extends (...args: never[]) => unknown ? Parameters<T> : never[];
      type ReturnTypeIfFn<T> = T extends (...args: never[]) => unknown ? ReturnType<T> : never;

      /**
       * Type signature for the factory method that creates the web worker for the Monaco editor. 
       */
      type WorkerFactory = (moduleId: string, label: string) => Worker;

      /**
       * Data passed to the extender in the {@link MonacoExtenderBase.createModel} method.
       * @template TEditorOpts Type of the editor construction options, either for the code editor or the diff editor.
       */
      interface CreateModelOptions<TEditorOpts extends import("monaco-editor").editor.IEditorOptions> {
        /** Resolved options for the monaco editor. */
        editorOptions: Readonly<TEditorOpts>;

        /** Code language for the model. */
        language: string;

        /** Default URI for the model. */
        uri: import("monaco-editor").Uri;

        /** Initial value for the model. */
        value: string;
      }

      // === Monaco context

      /**
       * Interface for objects that provide asynchronous access to a Monaco code editor or diff editor instances, such
       * as the widget instance for the Monaco editor in the iframe that can communicate with the editor in the iframe
       * only via `postMessage`. Note that objects that provide synchronous access also implement this interface for
       * convenience when you wish to write generic code that works with both types of editors.  
       * @template TEditor Type of the editor instance, either the code editor or the diff editor.
       * @template TEditorOpts Type of the editor construction options, either for the code editor or the diff editor.
       */
      interface AsyncMonacoBaseEditorContext<
        TEditor extends import("monaco-editor").editor.IEditor,
        TEditorOpts extends import("monaco-editor").editor.IEditorOptions
        > {
        /** @returns The ID of the Monaco editor widget. */
        getWidgetId(): string;

        /** @returns The current options for the Monaco editor widget. */
        getWidgetOptions(): Partial<PrimeFaces.widget.ExtMonacoBaseEditorCfgBase<TEditorOpts>>;

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
         * @returns A promise that resolves with the value returned by the given method. The promise is rejected when
         * the editor is not ready yet.
         */
        invokeMonaco<K extends PrimeFaces.MatchingKeys<TEditor, (...args: never[]) => unknown>>(
          method: K, ...args: PrimeFaces.widget.ExtMonacoEditor.ParametersIfFn<TEditor[K]>
        ): Promise<Awaited<PrimeFaces.widget.ExtMonacoEditor.ReturnTypeIfFn<TEditor[K]>>>;

        /**
         * Invokes the given script on the Monaco editor instance in the iframe, and returns the result. As the
         * communication with the iframes is done via `postMessage`, the result is returned asynchronously.
         * 
         * Note that if a function is given for the script, it is converted to a string, sent to the framed editor
         * and executed. Closing over variables in the lambda is therefore NOT supported. Explicitly specify those
         * variables as the arguments, they will be passed on to the iframe.
         * @template TRetVal Type of the value returned by the script.
         * @template TArgs Type of the arguments required by the script. 
         * @param method A method of the monaco editor instance to invoke. This method receives the Monaco editor
         * instance as the first argument, and the given `args` as additional arguments.
         * @param args Arguments that are passed to the method.
         * @returns A promise that resolves with the value returned by the given method.
         */
        invokeMonacoScript<TRetVal, TArgs extends any[]>(
          script: string | ((editor: TEditor, ...args: TArgs) => TRetVal),
          ...args: TArgs
        ): Promise<Awaited<TRetVal>>;

        /**
         * @returns `true` when the editor was already loaded and initialized and can be interacted with via
         * `getMonaco()`, `false` otherwise.
         */
        isReady(): boolean;

        /**
         * Performs a layout on the monaco editor. Useful when the surrounding container has changed its dimensions
         * etc. Does nothing when the editor was not rendered yet.
         * 
         * This method will always initiate the layout asynchronously, even when called on the inline editor.
         * 
         * @returns A promise that resolves immediately when the editor is not ready, or when the layout command
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
         * @returns A promise that resolves when the value was set.
         */
        setValue(newValue: string): Promise<void>;

        /**
         * @returns A promise that is resolved once the editor has finished loading and was created successfully.
         * When the editor is already initialized, this promise will resolve as soon as possible.
         */
        whenReady(): Promise<this>;
      }

      /**
       * Interface for objects that provide asynchronous access to a Monaco code editor instances, such as the widget
       * instance for the Monaco editor in the iframe that can communicate with the editor in the iframe only via
       * `postMessage`. Note that objects that provide synchronous access also implement this interface for convenience
       * when you wish to write generic code that works with both types of editors.  
       */
      interface AsyncMonacoCodeEditorContext extends AsyncMonacoBaseEditorContext<
        import("monaco-editor").editor.IStandaloneCodeEditor,
        import("monaco-editor").editor.IStandaloneEditorConstructionOptions
      > {
        // override with more specific types
        getWidgetOptions(): Partial<
          PrimeFaces.widget.ExtMonacoBaseEditorCfgBase<
            import("monaco-editor").editor.IStandaloneEditorConstructionOptions
          > & PrimeFaces.widget.ExtMonacoCodeEditorCfgBase
        >;
      }

      /**
       * Interface for objects that provide asynchronous access to a Monaco diff editor instances, such as the widget
       * instance for the Monaco editor in the iframe that can communicate with the editor in the iframe only via
       * `postMessage`. Note that objects that provide synchronous access also implement this interface for convenience
       * when you wish to write generic code that works with both types of editors.  
       */
      interface AsyncMonacoDiffEditorContext extends AsyncMonacoBaseEditorContext<
        import("monaco-editor").editor.IStandaloneDiffEditor,
        import("monaco-editor").editor.IStandaloneDiffEditorConstructionOptions
      > {
        /**
         * Gets the current value (code) of the original editor. May be called as soon as this widget is
         * accessible, even when the Monaco editor was not loaded or initialized yet. In that case, either the
         * initial value or the latest value set by {@link setValue} will be returned.
         * 
         * This method will always fetch the value asynchronously, even when called on the inline Monaco editor.
         * 
         * Note that for the inline editor, you can also get the value synchronously via `getValueNow`. This method
         * provides interoperability with the framed monaco editor and is available for both the inline and framed
         * monaco editor.
         */
        getOriginalValue(): Promise<string>;

        /**
         * Sets the value of the original editor. May be called as soon as this widget is accessible, even when
         * the Monaco editor was not loaded or initialized yet. The value will be set on the editor once it
         * becomes ready.
         * 
         * This method will always set the value asynchronously, even when called on the inline Monaco editor.
         * 
         * Note that for the inline editor, you can also set the value synchronously via `setValueNow`. This method
         * provides interoperability with the framed Monaco editor and is available for both the inline and framed
         * monaco editor.
         * @param newValue The new value to set.
         * @returns A promise that resolves when the value was set.
         */
        setOriginalValue(newValue: string): Promise<void>;

        // override with more specific types
        getWidgetOptions(): Partial<
          PrimeFaces.widget.ExtMonacoBaseEditorCfgBase<
            import("monaco-editor").editor.IStandaloneDiffEditorConstructionOptions
          > & PrimeFaces.widget.ExtMonacoDiffEditorCfgBase
        >;
      }

      /**
       * Interface for objects that provide synchronous access to a Monaco code editor or diff editor instance, such as
       * the widget instance for the inline monaco editor. Note that objects that provide synchronous access also
       * implement the asynchronous interface for convenience when you wish to write generic code that works with both
       * types of editors.
       * @template TEditor Type of the editor instance, either the code editor or the diff editor.
       * @template TEditorOpts Type of the editor construction options, either for the code editor or the diff editor.
       */
      interface SyncMonacoBaseEditorContext<
        TEditor extends import("monaco-editor").editor.IEditor,
        TEditorOpts extends import("monaco-editor").editor.IEditorOptions
        > extends AsyncMonacoBaseEditorContext<TEditor, TEditorOpts> {
        /**
         * @returns The extender that was set for this monaco editor widget, if any. It can be used to customize
         * the editor via JavaScript.
         */
        getExtender(): ExtenderBaseEditor<
          TEditor,
          TEditorOpts,
          never
        > | undefined;

        /**
         * Finds the current instance of the monaco editor, if it was created already. Use this to interact with the
         * editor via JavaScript. See also the
         * [monaco editor API docs](https://microsoft.github.io/monaco-editor/api/index.html).
         * @returns The current monaco editor instance. `undefined` in case the editor was not created yet.
         */
        getMonaco(): TEditor | undefined;

        /**
         * Finds the initial options that were used for creating the Monaco editor.
         * @returns {TEditorOpts | undefined} The original editor options that were used when the editor was created.
         * When you updated the options manually at a later point in time, these changes will not be reflected in the
         * value returned by this method. When the editor options were not created yet, returns `undefined`.
         */
        getMonacoOptions(): Readonly<TEditorOpts> | undefined;

        /**
         * Gets the value of this editor immediately, without a promise. This is possible only for the
         * inline editor, hence why this method only exists on the inline editor. May be called as soon
         * as this widget is accessible, even when the monaco editor was not loaded or initialized yet.
         * @returns The current value of this editor.
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
         * Calls the given handler with the current Monaco editor instance if it exists.
         * @template TReturn Type of the return value of the given handler.
         * @param handler Handler that is invoked with the current monaco editor instance.
         * @param defaultReturnValue Default value that is returned when no editor exists currently.
         * @returns The return value of the handler, or `undefined` if no editor exists.
         * @throws When the handler throws an error.
         */
        withMonaco<TReturn>(handler: (editor: TEditor) => TReturn, defaultReturnValue: TReturn): TReturn;

        /**
         * Calls the given handler with the current Monaco editor instance if it exists and catches possible errors.
         * @template TReturn Type of the return value of the given handler.
         * @param handler Handler that is invoked with the current monaco editor instance.
         * @param defaultReturnValue Default value that is returned when no editor exists currently, or when the
         * handler throws.
         * @returns The return value of the handler, or the default return value if either no editor exists or the
         * handler throws an error.
         */
        tryWithMonaco<TReturn>(handler: (editor: TEditor) => TReturn, defaultReturnValue: TReturn): TReturn;
      }

      /**
       * Interface for objects that provide synchronous access to a Monaco code editor instance, such as the widget
       * instance for the inline Monaco editor. Note that objects that provide synchronous access also implement the
       * asynchronous interface for convenience when you wish to write generic code that works with both types of
       * editors.  
       */
      interface SyncMonacoCodeEditorContext extends
        SyncMonacoBaseEditorContext<
        import("monaco-editor").editor.IStandaloneCodeEditor,
        import("monaco-editor").editor.IStandaloneEditorConstructionOptions
        >,
        AsyncMonacoCodeEditorContext {
        // override with more specific types
        getWidgetOptions(): Partial<
          PrimeFaces.widget.ExtMonacoBaseEditorCfgBase<
            import("monaco-editor").editor.IStandaloneEditorConstructionOptions
          > & PrimeFaces.widget.ExtMonacoCodeEditorCfgBase
        >;
      }

      /**
       * Interface for objects that provide synchronous access to a Monaco diff editor instance, such as the widget
       * instance for the inline Monaco diff editor. Note that objects that provide synchronous access also implement
       * the asynchronous interface for convenience when you wish to write generic code that works with both types of
       * editors.  
       */
      interface SyncMonacoDiffEditorContext extends
        SyncMonacoBaseEditorContext<
        import("monaco-editor").editor.IStandaloneDiffEditor,
        import("monaco-editor").editor.IStandaloneDiffEditorConstructionOptions
        >,
        AsyncMonacoDiffEditorContext {
        /**
         * Gets the value of the original  editor immediately, without a promise. This is possible only for the
         * inline editor, hence why this method only exists on the inline editor. May be called as soon
         * as this widget is accessible, even when the monaco editor was not loaded or initialized yet.
         * @returns The current value of this editor.
         */
        getOriginalValueNow(): string;

        /**
         * Sets the value on the original editor now, synchronously. This is possible only for the inline editor,
         * hence why this method only exists on the inline editor. May be called as soon as this widget
         * is accessible, even when the monaco editor was not loaded or initialized yet.
         * @param value The new value for the editor.
         */
        setOriginalValueNow(value: string): void;

        // override with more specific types
        getWidgetOptions(): Partial<
          PrimeFaces.widget.ExtMonacoBaseEditorCfgBase<
            import("monaco-editor").editor.IStandaloneDiffEditorConstructionOptions
          > & PrimeFaces.widget.ExtMonacoDiffEditorCfgBase
        >;
      }

      // === Monaco extender

      /**
       * An extender object to further customize the Monaco code or diff editor via JavaScript. Specified via the
       * `extender` attribute on the `<p:monacoEditor />`, `<p:monacoDiffEditor />`, `<p:monacoEditorFramed />`, or
       * `<p:monacoDiffEditorFramed />` tag.
       *
       * All callback methods in the extender are optional, if not specified, corresponding defaults are used.
       *
       * @template TEditor Type of the editor instance, either the code editor or the diff editor.
       * @template TEditorOpts Type of the editor construction options, either for the code editor or the diff editor.
       * @template TContext Type of the context object that is passed to all callback methods.
       */
      interface ExtenderBaseEditor<
        TEditor extends import("monaco-editor").editor.IEditor,
        TEditorOpts extends Readonly<import("monaco-editor").editor.IEditorOptions>,
        TContext extends AsyncMonacoBaseEditorContext<TEditor, TEditorOpts>
        > {
        /**
         * This method is called after the editor was created.
         * @param context The current context object.
         * @param wasLibLoaded `true` if the monaco editor library was reloaded, `false` otherwise. In case it was
         * reloaded, you may want to setup some language defaults again.
         */
        afterCreate?: (context: TContext, wasLibLoaded: boolean) => void;

        /**
         * Called after the editor was destroyed; and also when updating a component via AJAX.
         * @param context The current context object. Note that you should not use it to retrieve the monaco editor
         * instance, as the editor has already been destroyed.
         */
        afterDestroy?: (context: TContext) => void;

        /**
         * Called before monaco editor is created. This method is passed the current options object that would be used
         * to initialize the monaco editor.
         *
         * If this callback does not return a value, the options that were passed are used. You may modify the
         * options in-place.
         *
         * If it returns a new options object, that options object is used.
         *
         * If it returns a Thenable or Promise, the monaco editor is created only once the Promise resolves
         * (successfully).
         * If the Promise returns a new options object, these options are used to create the editor.
         *
         * See
         * [IStandaloneEditorConstructionOptions](https://microsoft.github.io/monaco-editor/api/interfaces/monaco.editor.istandaloneeditorconstructionoptions.html)
         * or
         * [IStandaloneDiffEditorConstructionOptions](https://microsoft.github.io/monaco-editor/api/interfaces/monaco.editor.istandalonediffeditorconstructionoptions.html)
         * for all editor options.
         *
         * @param context The current context object. Note that you should not use it to retrieve the monaco editor
         * instance, as the editor was not created yet.
         * @param options The current options that would be used to create the editor.
         * @param wasLibLoaded `true` if the monaco editor library was reloaded, `false` otherwise. In case it was
         * reloaded, you may want to setup some language defaults again.
         * @returns Either `undefined` to use the options as passed; a new options object to be used for creating the
         * editor; or a Promise that may return the new options.
         */
        beforeCreate?: (
          context: TContext,
          options: TEditorOpts,
          wasLibLoaded: boolean
        ) => import("monaco-editor").languages.ProviderResult<TEditorOpts>;

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
        * @returns The worker to be used for the given code language.
        */
        createWorker?: (context: TContext, moduleId: string, label: string) => Worker;

        /**
         * Called when monaco editor is created. May return an object with services that should be overridden. See
         * [here on github](https://github.com/Microsoft/monaco-editor/issues/935#issuecomment-402174095) for details
         * on the available services.
         * @param context The current context object. Note that you should not use it to retrieve the monaco editor
         * instance, as the editor was not created yet.
         * @param options The options that will be used to create the editor. and must not be changed.
         * @returns The override options to be used. If `undefined` is returned, no editor override services are used.
         */
        createEditorOverrideServices?: (
          context: TContext,
          options: Readonly<TEditorOpts>
        ) => import("monaco-editor").editor.IEditorOverrideServices | undefined;

        /**
         * Called when the model needs to be fetched. The default implementation attempts to find an existing model for
         * the given URI in the monaco store (`import("monaco-editor").editor.getModel`). If it cannot be found, it
         * creates a new model (`import("monaco-editor").editor.createModel`). Finally it sets the default value on the
         * model. This method can be used to create a custom when necessary, with possibly a different URI.
         *
         * If you implement this callback, you SHOULD set the initial value (`options.value`) on the model, it will NOT
         * be set automatically.
         * @param context The current context object. Note that you should not use it to retrieve the monaco editor
         * instance, as the editor was not created yet.
         * @param options Options with the default URI, the current value, and the editor configuration.
         * @returns The retrieved or created model. When `undefined`, the default mechanism to create the model is used.
         */
        createModel?: (
          context: TContext,
          options: CreateModelOptions<TEditorOpts>
        ) => import("monaco-editor").editor.ITextModel | undefined
      }

      /**
       * An extender object to further customize the Monaco code editor via JavaScript. Specified via the `extender`
       * attribute on the `<p:monacoEditor />` or `<p:monacoEditorFramed />` tag.
       *
       * All callback methods in the extender are optional, if not specified, corresponding defaults are used.
       *
       * @template TContext Type of the context object that is passed to all callback methods.
       */
      interface ExtenderCodeEditor<TContext extends AsyncMonacoCodeEditorContext> extends ExtenderBaseEditor<
        import("monaco-editor").editor.IStandaloneCodeEditor,
        import("monaco-editor").editor.IStandaloneEditorConstructionOptions,
        TContext
      > {
      }

      /**
       * An extender object to further customize the Monaco diff editor via JavaScript. Specified via the `extender`
       * attribute on the `<p:monacoDiffEditor />` or `<p:monacoDiffEditorFramed />` tag.
       *
       * All callback methods in the extender are optional, if not specified, corresponding defaults are used.
       *
       * @template TContext Type of the context object that is passed to all callback methods.
       */
      interface ExtenderDiffEditor<TContext extends AsyncMonacoDiffEditorContext> extends ExtenderBaseEditor<
        import("monaco-editor").editor.IStandaloneDiffEditor,
        import("monaco-editor").editor.IStandaloneDiffEditorConstructionOptions,
        TContext
      > {
        /**
         * Called when the model needs to be created for the original editor. The default implementation
         * attempts to find an existing model for the given URI in the monaco store
         * (`import("monaco-editor").editor.getModel`). If it cannot be found, it creates a new model
         * (`import("monaco-editor").editor.createModel`). Finally it sets the default value on the model. This
         * method can be used to create a custom when necessary, with possibly a different URI.
         *
         * If you implement this callback, you SHOULD set the initial value (`options.value`) on the model, it will
         * NOT be set automatically.
         * @param context The current context object. Note that you should not use it to retrieve the monaco
         * editor instance, as the editor was not created yet.
         * @param options Options with the default URI, the current value, and the editor configuration.
         * @returns The retrieved or created model. When `undefined`, the default mechanism to create the model
         * is used.
         */
        createOriginalModel?: (
          context: TContext,
          options: CreateModelOptions<import("monaco-editor").editor.IStandaloneDiffEditorConstructionOptions>
        ) => import("monaco-editor").editor.ITextModel | undefined
      }

      /**
       * Extender for the inline Monaco code editor that has direct access to the editor.
       */
      interface ExtenderCodeEditorInline extends ExtenderCodeEditor<widget.ExtMonacoCodeEditorInline> {
      }

      /**
       * Extender for the inline Monaco diff editor that has direct access to the editor.
       */
      interface ExtenderDiffEditorInline extends ExtenderDiffEditor<widget.ExtMonacoDiffEditorInline> {
      }

      /**
       * Extender for the framed Monaco code editor that must communicate via postMessage with the editor.
       */
      interface ExtenderCodeEditorFramed extends ExtenderCodeEditor<IframeCodeEditorContext> {
      }

      /**
       * Extender for the framed Monaco diff editor that must communicate via postMessage with the editor.
       */
      interface ExtenderDiffEditorFramed extends ExtenderDiffEditor<IframeDiffEditorContext> {
      }

      /**
       * Interface for objects that provide access to the controller for the Monaco code editor running in an iframe.
       */
      interface IframeCodeEditorContext extends SyncMonacoCodeEditorContext {
        /**
         * The extender that was set for this monaco editor widget. It can be used to customize the editor via
         * JavaScript.
         */
        getExtender(): ExtenderCodeEditorFramed | undefined;
      }

      /**
       * Interface for objects that provide access to the controller for the Monaco diff editor running in an iframe.
       */
      interface IframeDiffEditorContext extends SyncMonacoDiffEditorContext {
        /**
         * The extender that was set for this monaco diff editor widget. It can be used to customize the editor via
         * JavaScript.
         */
        getExtender(): ExtenderDiffEditorFramed | undefined;
      }
    }
  }
}


// === PrimeFaces integration

declare namespace PrimeFaces {
  namespace widget {

    // === Our widget integration without BaseWidgetCfg

    /**
     * Base configuration for all Monaco editor and diff editor widgets, without the properties from the
     * {@link BaseWidgetCfg}. These properties mostly correspond to the values set on the widget in your XHTML view.
     * @template TEditorOpts Type of the editor construction options, either for the code editor or the diff editor.
     */
    interface ExtMonacoBaseEditorCfgBase<TEditorOpts extends import("monaco-editor").editor.IEditorOptions> {
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
      editorOptions: Readonly<TEditorOpts>;

      /**
       * Extension for the URI used for the model.
       */
      extension: string;

      /**
       * The extender for enhancing the editor with client-side functionality. Exact type depends on whether the inlined
       * or framed editor is used.
       */
      extender: unknown;

      /**
       * Whether the editor is  disabled.
       */
      disabled: boolean;

      /**
       * The code language tag, eg. `css` or `javascript`. See  also `monaco.language.getLanguages`.
       */
      language: string;

      /**
       * The code of the current UI language of the monaco editor, eg. `en` or `de`.
       */
      locale: string;

      /**
       * Whether the editor is read-only.
       */
      readonly: boolean;

      /**
       * Scheme (protocol) for the URI used for the model.
       */
      scheme: string;

      /**
       * Tab index for the editor.
       */
      tabIndex: number | undefined;

      /**
       * The Uri to the locale file with the translations for the current language.
       */
      localeUrl: string;
    }

    // Individual configurations for code editor, diff editor, inline editor, framed editor

    /**
     * Base configuration for both Monaco editor widgets, without the properties from the {@link BaseWidgetCfg}.
     * These properties mostly correspond to the values set on the widget in your XHTML view.
     */
    interface ExtMonacoCodeEditorCfgBase {
    }

    /**
     * Base configuration for both Monaco diff editor widgets, without the properties from the
     * {@link BaseWidgetCfg}. These properties mostly correspond to the values set on the widget in your XHTML
     * view.
     */
    interface ExtMonacoDiffEditorCfgBase {
      /**
       * Whether the original editor is disabled.
       */
      originalDisabled: boolean;
      /**
       * Whether the original editor is read-only.
       */
      originalReadonly: boolean;
      /**
       * Whether the original editor is required.
       */
      originalRequired: boolean;
      /**
       * Code language of original editor.
       */
      originalLanguage: string;
      /**
      * Scheme (protocol) for the URI used for the original model.
      */
      originalScheme: string;
      /**
       * Directory (path) for the URI used for the original model.
       */
      originalDirectory: string;
      /**
       * Basename for the URI used for the original model.
       */
      originalBasename: string;
      /**
       * Extension for the URI used for the original model.
       */
      originalExtension: string;
    }

    /**
     * Base configuration for inline widgets, without the properties from the {@link BaseWidgetCfg}.
     * @template TEditorOpts Type of the editor construction options, either for the code editor or the diff editor.
     */
    interface ExtMonacoInlineEditorCfgBase<
      TEditorOpts extends import("monaco-editor").editor.IEditorOptions
      > extends ExtMonacoBaseEditorCfgBase<TEditorOpts> {
      /**
       * Factory function that creates the extender for this monaco editor widget. Either `undefined` or JavaScript code
       * that evaluates to the extender.
       */
      extender: () => ExtMonacoEditor.ExtenderCodeEditorInline | ExtMonacoEditor.ExtenderDiffEditorInline;

      /**
       * ID or PrimeFaces search expression for a DOM node. Places overflow widgets inside an external DOM node.
       * Defaults to an internal DOM node. This is resolved to a DOM node and passed on to the monaco editor editor
       * constructor option with the same name.
       */
      overflowWidgetsDomNode: string;
    }

    /**
     * Base configuration for framed widgets, without the properties from the {@link BaseWidgetCfg}.
     * @template TEditorOpts Type of the editor construction options, either for the code editor or the diff editor.
     */
    interface ExtMonacoFramedEditorCfgBase<
      TEditorOpts extends import("monaco-editor").editor.IEditorOptions
      > extends ExtMonacoBaseEditorCfgBase<TEditorOpts> {
      /**
       * URL to the extender for this monaco editor widget. Either `undefined` or an URL that points to a script file
       * that sets `window.MonacoEnvironment` to the extender to be used.
       */
      extender: string;

      /**
       * Additional URL params that are added to the iframe URL. May be useful to pass additional parameters to the
       * extender script.
       */
      iframeUrlParams: Record<string, string[]>;
    }

    // Combined configurations for code+inline, code+framed, diff+inline, diff+framed 

    /**
     * Base configuration for {@link ExtMonacoCodeEditorInline} widget, without the properties from the {@link BaseWidgetCfg}.
     */
    interface ExtMonacoCodeEditorInlineCfgBase extends
      ExtMonacoInlineEditorCfgBase<import("monaco-editor").editor.IStandaloneEditorConstructionOptions>,
      ExtMonacoCodeEditorCfgBase {
      /**
       * Factory function that creates the extender for this monaco editor widget.
       * Either `undefined` or JavaScript code that evaluates to the extender.
       */
      extender: () => ExtMonacoEditor.ExtenderCodeEditorInline;
    }

    /**
     * Base configuration for {@link ExtMonacoDiffEditorInline} widget, without the properties from the {@link BaseWidgetCfg}.
     */
    interface ExtMonacoDiffEditorInlineCfgBase extends
      ExtMonacoInlineEditorCfgBase<import("monaco-editor").editor.IStandaloneDiffEditorConstructionOptions>,
      ExtMonacoDiffEditorCfgBase {
      /**
       * Factory function that creates the extender for this monaco editor widget.
       * Either `undefined` or JavaScript code that evaluates to the extender.
       */
      extender: () => ExtMonacoEditor.ExtenderDiffEditorInline;
    }

    /**
     * Base configuration for {@link ExtMonacoCodeEditorFramed} widget, without the properties from the {@link BaseWidgetCfg}.
     */
    interface ExtMonacoCodeEditorFramedCfgBase extends
      ExtMonacoFramedEditorCfgBase<import("monaco-editor").editor.IStandaloneEditorConstructionOptions>,
      ExtMonacoCodeEditorCfgBase {
    }

    /**
     * Base configuration for {@link ExtMonacoDiffEditorFramed} widget, without the properties from the {@link BaseWidgetCfg}.
     */
    interface ExtMonacoDiffEditorFramedCfgBase extends
      ExtMonacoFramedEditorCfgBase<import("monaco-editor").editor.IStandaloneDiffEditorConstructionOptions>,
      ExtMonacoDiffEditorCfgBase {
    }

    // === Complete widget integration with BaseWidgetCfg

    /**
     * Base configuration for both Monaco editor widgets, with the properties from the {@link BaseWidgetCfg}.
     * These properties mostly correspond to the values set on the widget in your XHTML view.
     * @template TEditorOpts Type of the editor construction options, either for the code editor or the diff editor.
     */
    interface ExtMonacoBaseEditorCfg<
      TEditorOpts extends import("monaco-editor").editor.IEditorOptions
      > extends ExtMonacoBaseEditorCfgBase<TEditorOpts>, BaseWidgetCfg { }


    /**
     * Base configuration for the Monaco editor and diff editor inline widgets, with the properties from the
     * {@link BaseWidgetCfg}.
     * These properties mostly correspond to the values set on the widget in your XHTML view.
     * @template TEditorOpts Type of the editor construction options, either for the code editor or the diff editor.
     */
    interface ExtMonacoBaseEditorInlineCfg<
      TEditorOpts extends import("monaco-editor").editor.IEditorOptions
      > extends ExtMonacoInlineEditorCfgBase<TEditorOpts>, BaseWidgetCfg { }

    /**
     * Base configuration for {@link ExtMonacoCodeEditorInline} widget, with the properties from the {@link BaseWidgetCfg}.
     */
    interface ExtMonacoCodeEditorInlineCfg extends ExtMonacoCodeEditorInlineCfgBase, BaseWidgetCfg { }

    /**
     * Base configuration for {@link ExtMonacoDiffEditorInline} widget, with the properties from the {@link BaseWidgetCfg}.
     */
    interface ExtMonacoDiffEditorInlineCfg extends ExtMonacoDiffEditorInlineCfgBase, BaseWidgetCfg { }

    /**
     * Base configuration for the Monaco editor and diff editor framed widgets, with the properties from the
     * {@link BaseWidgetCfg}.
     * These properties mostly correspond to the values set on the widget in your XHTML view.
     * @template TEditorOpts Type of the editor construction options, either for the code editor or the diff editor.
     */
    interface ExtMonacoBaseEditorFramedCfg<
      TEditorOpts extends import("monaco-editor").editor.IEditorOptions
      > extends ExtMonacoFramedEditorCfgBase<TEditorOpts>, BaseWidgetCfg { }

    /**
     * Base configuration for {@link ExtMonacoCodeEditorFramed} widget, with the properties from the {@link BaseWidgetCfg}.
     */
    interface ExtMonacoCodeEditorFramedCfg extends ExtMonacoCodeEditorFramedCfgBase, BaseWidgetCfg { }

    /**
     * Base configuration for {@link ExtMonacoDiffEditorFramed} widget, with the properties from the {@link BaseWidgetCfg}.
     */
    interface ExtMonacoDiffEditorFramedCfg extends ExtMonacoDiffEditorFramedCfgBase, BaseWidgetCfg { }

    // === Widget classes

    /**
     * __PrimeFacesExtensions MonacoEditorBase Widget__
     * 
     * The MonacoEditorBase widget is the base class for both the inline and the framed Monaco editor widget.
     * The Monaco code editor is an advanced code editor with support vor IntelliSense.
     * @template TEditor Type of the editor instance, either the code editor or the diff editor.
     * @template TEditorOpts Type of the editor construction options, either for the code editor or the diff editor.
     * @template TCfg Type of the widget configuration.
     */
    abstract class ExtMonacoBaseEditor<
      TEditor extends import("monaco-editor").editor.IEditor,
      TEditorOpts extends import("monaco-editor").editor.IEditorOptions,
      TCfg extends widget.ExtMonacoBaseEditorCfg<TEditorOpts>
      >
      extends DeferredWidget<TCfg>
      implements ExtMonacoEditor.AsyncMonacoBaseEditorContext<TEditor, TEditorOpts> {

      /**
       * @returns The HTML container element holding the editor. It exists even if the editor was not created yet.
       */
      getEditorContainer(): JQuery;

      /**
       * @returns The hidden textarea holding the value of the editor (eg. the code being edited, this is also the
       * value that is sent when the form is submitted).
       */
      getInput(): JQuery;

      // copied from the interface, see there for docs
      getWidgetId(): string;
      getWidgetOptions(): PartialWidgetCfg<TCfg>;
      getValue(): Promise<string>;
      invokeMonaco<K extends PrimeFaces.MatchingKeys<TEditor, (...args: never[]) => unknown>>(
        method: K, ...args: PrimeFaces.widget.ExtMonacoEditor.ParametersIfFn<TEditor[K]>
      ): Promise<Awaited<PrimeFaces.widget.ExtMonacoEditor.ReturnTypeIfFn<TEditor[K]>>>;
      invokeMonacoScript<TRetVal, TArgs extends any[]>(script: string | ((editor: TEditor, ...args: TArgs) => TRetVal), ...args: TArgs): Promise<Awaited<TRetVal>>;
      isReady(): boolean;
      layout(): Promise<void>;
      setValue(newValue: string): Promise<void>;
      whenReady(): Promise<this>;
    }

    /**
     * __PrimeFacesExtensions ExtMonacoEditorInlineBase Widget__
     * 
     * The ExtMonacoEditorInlineBase widget is the base for the inline variant of the Monaco editor widget and
     * diff editor widget. It renders the editor directly in the page where the widget is used.
     * 
     * The Monaco code editor is an advanced code editor with support vor IntelliSense.
     * @template TEditor Type of the editor instance, either the code editor or the diff editor.
     * @template TEditorOpts Type of the editor construction options, either for the code editor or the diff editor.
     * @template TContext Type of the context that provides access to Monaco related functionality.
     * @template TExtender Type of the extender for customizing the Monaco instance.
     * @template TCfg Type of the widget configuration.
     */
    abstract class ExtMonacoBaseEditorInline<
      TEditor extends import("monaco-editor").editor.IEditor,
      TEditorOpts extends import("monaco-editor").editor.IEditorOptions,
      TContext extends ExtMonacoEditor.SyncMonacoBaseEditorContext<TEditor, TEditorOpts>,
      TExtender extends ExtMonacoEditor.ExtenderBaseEditor<TEditor, TEditorOpts, TContext>,
      TCfg extends widget.ExtMonacoBaseEditorInlineCfg<TEditorOpts>
      >
      extends ExtMonacoBaseEditor<TEditor, TEditorOpts, TCfg>
      implements ExtMonacoEditor.SyncMonacoBaseEditorContext<TEditor, TEditorOpts> {

      /**
       * @returns The Monaco editor context which lets you interact with the Monaco editor.  
       */
      abstract getContext(): TContext;

      // copied from the interface, see there for docs
      getExtender(): Partial<TExtender> | undefined;
      getMonaco(): TEditor | undefined;
      getMonacoOptions(): Readonly<TEditorOpts> | undefined;
      getValueNow(): string;
      setValueNow(value: string): void;
      withMonaco<TReturn>(handler: (editor: TEditor) => TReturn, defaultReturnValue: TReturn): TReturn;
      tryWithMonaco<TReturn>(handler: (editor: TEditor) => TReturn, defaultReturnValue: TReturn): TReturn;
    }

    /**
     * __PrimeFacesExtensions MonacoEditorInline Widget__
     * 
     * The MonacoEditorInline widget is the inline variant of the Monaco editor widget. It renders
     * the editor directly in the page where the widget is used.
     * 
     * The Monaco code editor is an advanced code editor with support vor IntelliSense.
     */
    class ExtMonacoCodeEditorInline
      extends ExtMonacoBaseEditorInline<
      import("monaco-editor").editor.IStandaloneCodeEditor,
      import("monaco-editor").editor.IStandaloneEditorConstructionOptions,
      ExtMonacoCodeEditorInline,
      ExtMonacoEditor.ExtenderCodeEditorInline,
      widget.ExtMonacoCodeEditorInlineCfg
      >
      implements ExtMonacoEditor.SyncMonacoCodeEditorContext {
      // copied from the abstract superclass, see there for docs
      getContext(): ExtMonacoCodeEditorInline;
    }

    /**
     * __PrimeFacesExtensions MonacoEditorInline Widget__
     * 
     * The MonacoEditorInline widget is the inline variant of the Monaco diff editor widget. It renders
     * the editor directly in the page where the widget is used.
     * 
     * The Monaco code editor is an advanced code editor with support vor IntelliSense.
     */
    class ExtMonacoDiffEditorInline
      extends ExtMonacoBaseEditorInline<
      import("monaco-editor").editor.IStandaloneDiffEditor,
      import("monaco-editor").editor.IStandaloneDiffEditorConstructionOptions,
      ExtMonacoDiffEditorInline,
      ExtMonacoEditor.ExtenderDiffEditorInline,
      widget.ExtMonacoDiffEditorInlineCfg
      >
      implements ExtMonacoEditor.SyncMonacoDiffEditorContext {
      /**
       * @returns The hidden textarea holding the value of the original editor.
       */
      getOriginalInput(): JQuery;

      // copied from the abstract superclass, see there for docs
      getContext(): ExtMonacoDiffEditorInline;

      // copied from the interface, see there for docs
      getOriginalValue(): Promise<string>;
      getOriginalValueNow(): string;
      setOriginalValue(newValue: string): Promise<void>;
      setOriginalValueNow(value: string): void;
    }

    /**
     * __PrimeFacesExtensions MonacoEditorFramedBase Widget__
     * 
     * The MonacoEditorInline widget is base for both framed variants of the Monaco editor widget. It renders
     * the editor inside an iframe for improved encapsulation.
     * 
     * The Monaco code editor is an advanced code editor with support vor IntelliSense.
     * @template TEditor Type of the editor instance, either the code editor or the diff editor.
     * @template TEditorOpts Type of the editor construction options, either for the code editor or the diff editor.
     * @template TContext Type of the context that provides access to Monaco related functionality.
     * @template TExtender Type of the extender for customizing the Monaco instance.
     * @template TCfg Type of the widget configuration.
     */
    abstract class ExtMonacoBaseEditorFramed<
      TEditor extends import("monaco-editor").editor.IEditor,
      TEditorOpts extends import("monaco-editor").editor.IEditorOptions,
      TContext extends ExtMonacoEditor.AsyncMonacoBaseEditorContext<TEditor, TEditorOpts>,
      TExtender extends ExtMonacoEditor.ExtenderBaseEditor<TEditor, TEditorOpts, TContext>,
      TCfg extends widget.ExtMonacoBaseEditorFramedCfg<TEditorOpts>
      > extends ExtMonacoBaseEditor<TEditor, TEditorOpts, TCfg> {
    }

    /**
     * __PrimeFacesExtensions MonacoEditorFramed Widget__
     * 
     * The MonacoEditorInline widget is the framed variant of the Monaco editor widget. It renders
     * the editor inside an iframe for improved encapsulation.
     * 
     * The Monaco code editor is an advanced code editor with support vor IntelliSense.
     */
    class ExtMonacoCodeEditorFramed extends ExtMonacoBaseEditorFramed<
      import("monaco-editor").editor.IStandaloneCodeEditor,
      import("monaco-editor").editor.IStandaloneEditorConstructionOptions,
      ExtMonacoEditor.IframeCodeEditorContext,
      ExtMonacoEditor.ExtenderCodeEditorFramed,
      widget.ExtMonacoCodeEditorFramedCfg
    > {
    }

    /**
     * __PrimeFacesExtensions MonacoDiffEditorFramed Widget__
     * 
     * The MonacoEditorInline widget is the framed variant of the Monaco diff editor widget. It renders
     * the editor inside an iframe for improved encapsulation.
     * 
     * The Monaco code editor is an advanced code editor with support vor IntelliSense.
     */
    class ExtMonacoDiffEditorFramed
      extends ExtMonacoBaseEditorFramed<
      import("monaco-editor").editor.IStandaloneDiffEditor,
      import("monaco-editor").editor.IStandaloneDiffEditorConstructionOptions,
      ExtMonacoEditor.IframeDiffEditorContext,
      ExtMonacoEditor.ExtenderDiffEditorFramed,
      widget.ExtMonacoDiffEditorFramedCfg
      >
      implements ExtMonacoEditor.AsyncMonacoDiffEditorContext {
      /**
       * @returns The hidden textarea holding the value of the original editor.
       */
      getOriginalInput(): JQuery;

      // copied from the interface, see there for docs
      getOriginalValue(): Promise<string>;
      setOriginalValue(newValue: string): Promise<void>;
    }
  }
}
