type PromiseFactory<T> = () => Promise<T>;
type PromiseCallback = (value: any) => void;
interface QueueItem {
  factory: PromiseFactory<unknown>;
  resolve: PromiseCallback;
  reject: PromiseCallback;
}
interface IPromiseQueue {
  add<T>(promiseFactory: PromiseFactory<T>): Promise<T>;
  addAll<T>(...promiseFactory: PromiseFactory<T>[]): Promise<T>[];
}

interface ModelConfig<
  TEditor extends import("monaco-editor").editor.IEditor,
  TEditorOpts extends import("monaco-editor").editor.IEditorConstructionOptions,
  TContext extends PrimeFaces.widget.ExtMonacoEditor.SyncMonacoBaseEditorContext<TEditor, TEditorOpts>,
  TExtender extends PrimeFaces.widget.ExtMonacoEditor.ExtenderBaseEditor<TEditor, TEditorOpts, TContext>
  > {
  language: string | undefined;
  scheme: string | undefined;
  directory: string | undefined;
  basename: string | undefined;
  extension: string | undefined;
  createDefaultDir: (
    context: TContext,
    extender: TExtender,
  ) => string;
  createExtenderModel: (
    context: TContext,
    extender: TExtender,
    options: PrimeFaces.widget.ExtMonacoEditor.CreateModelOptions<TEditorOpts>
  ) => import("monaco-editor").editor.ITextModel | undefined;
}

interface DiffEditorCustomInitData {
  originalModel: import("monaco-editor").editor.ITextModel;
  modifiedModel: import("monaco-editor").editor.ITextModel;
}

interface EditorInitData<TEditorOpts extends import("monaco-editor").editor.IEditorOptions, TCustomArgs> {
  custom: TCustomArgs;
  options: TEditorOpts;
}

interface IframeRenderData {
  scrollTop: number;
  value: string;
}

interface IframeDiffRenderData extends IframeRenderData {
  originalScrollTop: number;
  originalValue: string;
}

interface RenderArgs<
  TEditor extends import("monaco-editor").editor.IEditor,
  TEditorOpts extends import("monaco-editor").editor.IEditorConstructionOptions,
  TContext extends PrimeFaces.widget.ExtMonacoEditor.SyncMonacoBaseEditorContext<TEditor, TEditorOpts>,
  TExtender extends PrimeFaces.widget.ExtMonacoEditor.ExtenderBaseEditor<TEditor, TEditorOpts, TContext>,
  TCustomArgs
  > {
  custom: TCustomArgs,
  extender: TExtender;
  options: TEditorOpts;
  wasLibLoaded: boolean;
}

interface RenderedArgs<
  TEditor extends import("monaco-editor").editor.IEditor,
  TEditorOpts extends import("monaco-editor").editor.IEditorConstructionOptions,
  TContext extends PrimeFaces.widget.ExtMonacoEditor.SyncMonacoBaseEditorContext<TEditor, TEditorOpts>,
  TExtender extends PrimeFaces.widget.ExtMonacoEditor.ExtenderBaseEditor<TEditor, TEditorOpts, TContext>,
  TCustomArgs
  > extends RenderArgs<TEditor, TEditorOpts, TContext, TExtender, TCustomArgs> {
  editor: TEditor;
}

interface Helper {
  assign: typeof Object.assign;
  createDiffEditorInitData: <
    TContext extends PrimeFaces.widget.ExtMonacoEditor.SyncMonacoDiffEditorContext,
    TExtender extends PrimeFaces.widget.ExtMonacoEditor.ExtenderDiffEditor<TContext>
    >(
    context: TContext,
    extender: TExtender,
    originalValue: string,
    modifiedValue: string,
    wasLibLoaded: boolean
  ) => Promise<EditorInitData<
    import("monaco-editor").editor.IStandaloneDiffEditorConstructionOptions,
    DiffEditorCustomInitData
  >>;
  createEditorConstructionOptions: <
    TContext extends PrimeFaces.widget.ExtMonacoEditor.SyncMonacoCodeEditorContext,
    TExtender extends PrimeFaces.widget.ExtMonacoEditor.ExtenderCodeEditor<TContext>
    >(
    context: TContext,
    extender: TExtender,
    editorValue: string,
    wasLibLoaded: boolean
  ) => Promise<import("monaco-editor").editor.IStandaloneEditorConstructionOptions>;
  createModel: <
    TEditor extends import("monaco-editor").editor.IEditor,
    TEditorOpts extends import("monaco-editor").editor.IEditorConstructionOptions,
    TContext extends PrimeFaces.widget.ExtMonacoEditor.SyncMonacoBaseEditorContext<TEditor, TEditorOpts>,
    TExtender extends PrimeFaces.widget.ExtMonacoEditor.ExtenderBaseEditor<TEditor, TEditorOpts, TContext>
    >(
    context: TContext,
    editorOptions: TEditorOpts,
    extender: TExtender,
    value: string,
    modelConfig: ModelConfig<TEditor, TEditorOpts, TContext, TExtender>,
  ) => import("monaco-editor").editor.ITextModel;
  defineCustomThemes(customThemes: Record<string, import("monaco-editor").editor.IStandaloneThemeData> | undefined): void;
  endsWith: (string: any, suffix: any) => boolean;
  getFacesResourceUri: () => string;
  getMonacoResource: (resource: string, queryParams?: Record<string, number | string | string[]>) => string;
  getScript: (url: string) => Promise<void>;
  getScriptName: (moduleId: string, label: string) => string;
  globalEval: (script: string, nonce?: string) => void;
  invokeMonaco: <
    TEditor extends import("monaco-editor").editor.IEditor,
    K extends PrimeFaces.MatchingKeys<TEditor, (...args: never[]) => unknown>
    >(
    editor: TEditor,
    method: K,
    args: PrimeFaces.widget.ExtMonacoEditor.ParametersIfFn<TEditor[K]>
  ) => Promise<Awaited<PrimeFaces.widget.ExtMonacoEditor.ReturnTypeIfFn<TEditor[K]>>>;
  invokeMonacoScript: <TEditor extends import("monaco-editor").editor.IEditor, TRet, TArgs extends any[]>(
    editor: TEditor,
    script: ((editor: TEditor, ...args: TArgs) => TRet) | string,
    args: TArgs,
    evalFn: (script: string) => void
  ) => Promise<Awaited<TRet>>;
  isMonacoMessage: (value: unknown) => value is MonacoMessage;
  isNotNullOrUndefined: <T>(x: T | null | undefined) => x is T;
  isNullOrUndefined: (x: unknown) => x is null | undefined;
  loadEditorLib: (
    options: Partial<PrimeFaces.widget.ExtMonacoBaseEditorCfgBase<import("monaco-editor").editor.IEditorOptions>>,
    forceLibReload: boolean
  ) => Promise<boolean>;
  loadExtender: <
    TEditor extends import("monaco-editor").editor.IEditor,
    TEditorOpts extends import("monaco-editor").editor.IEditorOptions,
    TContext extends PrimeFaces.widget.ExtMonacoEditor.SyncMonacoBaseEditorContext<TEditor, TEditorOpts>,
    TExtender extends PrimeFaces.widget.ExtMonacoEditor.ExtenderBaseEditor<TEditor, TEditorOpts, TContext>
    >(
    editor: TEditor | undefined,
    options: Partial<PrimeFaces.widget.ExtMonacoBaseEditorCfgBase<TEditorOpts>>,
    context: TContext | undefined
  ) => Partial<TExtender>;
  loadLanguage: (
    options: Partial<PrimeFaces.widget.ExtMonacoBaseEditorCfgBase<import("monaco-editor").editor.IEditorOptions>>
  ) => Promise<{
    forceLibReload: boolean;
    localeUrl: string;
  }>;
  parseSimpleQuery: (query: string) => Record<string, string | undefined>;
  resolveLocaleUrl: (
    options: Partial<PrimeFaces.widget.ExtMonacoBaseEditorCfgBase<import("monaco-editor").editor.IEditorOptions>>
  ) => string;
  BaseEditorDefaults: PrimeFaces.widget.ExtMonacoBaseEditorCfgBase<import("monaco-editor").editor.IEditorOptions>;
  DefaultThemeData: import("monaco-editor").editor.IStandaloneThemeData;
  FramedDiffEditorDefaults: PrimeFaces.widget.ExtMonacoDiffEditorFramedCfgBase;
  FramedEditorDefaults: PrimeFaces.widget.ExtMonacoCodeEditorFramedCfgBase;
  InlineDiffEditorDefaults: PrimeFaces.widget.ExtMonacoDiffEditorInlineCfgBase;
  InlineEditorDefaults: PrimeFaces.widget.ExtMonacoCodeEditorInlineCfgBase;
}

interface PromiseHook<T> {
  resolve: (widget: T) => void;
  reject: (reason: any) => void;
}

declare class ExtMonacoEditorBase<
  TEditor extends import("monaco-editor").editor.IEditor,
  TEditorOpts extends import("monaco-editor").editor.IEditorOptions,
  TCfg extends PrimeFaces.widget.ExtMonacoBaseEditorCfg<TEditorOpts>
  > extends PrimeFaces.widget.ExtMonacoBaseEditor<TEditor, TEditorOpts, TCfg> {
  _editorValue: string | undefined;
  _onDone: PromiseHook<this>[] | undefined;
  _input: JQuery | undefined;
  _editorContainer: JQuery | undefined;
  _init(cfg: PrimeFaces.PartialWidgetCfg<TCfg>, defaults: Partial<TCfg>): void;
  _bindEvents(): void;
  _fireEvent(eventName: string, ...params: any[]): void;
  _listSupportedEvents(): string[];
  _rejectOnDone(): void;
  _supportsEvent(eventName: string): boolean;
  _onInitSuccess(): Promise<void>;
  _onInitError(error: unknown): void;
}

interface Window {
  monacoModule: {
    helper: Helper,
    PromiseQueue: new () => IPromiseQueue;
    ExtMonacoEditorBase: typeof ExtMonacoEditorBase;
  },
}

// == Post message events

interface MonacoMessage {
  payload: MonacoMessagePayload;
  instanceId: number;
}

interface MonacoMessagePayloadType<K extends string, T> {
  kind: K;
  data: T;
}

interface InitBaseMessageData {
  facesResourceUri: string,
  id: string;
  nonce: string | undefined;
  resolvedLocaleUrl: string;
  resourceUrlExtension: string;
  scrollTop: number;
  supportedEvents: string[];
  value: string;
  version: string;
}
interface InitMessageData extends InitBaseMessageData {
  options: Partial<PrimeFaces.widget.ExtMonacoCodeEditorFramedCfgBase>,
}

interface InitDiffMessageData extends InitMessageData {
  options: Partial<PrimeFaces.widget.ExtMonacoDiffEditorFramedCfgBase>,
  originalScrollTop: number;
  originalValue: string;
}

type ResponseMessageData =
  | {
    success: true;
    value: any;
    messageId: number;
  }
  | {
    success: false;
    error: string;
    messageId: number;
  };

type InvokeMonacoMessageData<
  TEditor extends import("monaco-editor").editor.IEditor,
  K extends PrimeFaces.MatchingKeys<
    TEditor,
    (...args: never[]) => unknown
  > = PrimeFaces.MatchingKeys<TEditor, (...args: never[]) => unknown>
  > = {
    args: PrimeFaces.widget.ExtMonacoEditor.ParametersIfFn<TEditor[K]>;
    messageId: number;
    method: K;
  }

type InvokeMonacoScriptData = {
  script: string;
  args: any[];
  messageId: number;
};

type BindEventsMessageData = {
};

type ValueChangeMessageData = {
  value: string;
  changes: import("monaco-editor").editor.IModelContentChangedEvent;
};

type ScrollChangeMessageData = {
  scrollLeft: number;
  scrollTop: number;
};

type DomEventMessageData = {
  name: string;
  data: string;
}

type AfterInitMessageData = {
  success: true
} | {
  success: false,
  error: string;
};

type MonacoMessagePayload =
  // When the iframe load event was triggered and the iframe is now ready
  | MonacoMessagePayloadType<"load", undefined>

  // When the widget was created and the editor needs to be rendered
  | MonacoMessagePayloadType<"init", InitMessageData>

  // When the widget was created and the diff editor needs to be rendered
  | MonacoMessagePayloadType<"initDiff", InitDiffMessageData>

  // When the widget was destroyed
  | MonacoMessagePayloadType<"destroy", undefined>

  // When the event listeners should be added to the Monaco editor
  | MonacoMessagePayloadType<"bindEvents", BindEventsMessageData>

  // When the iframe controller responds to a message sent by the widget
  | MonacoMessagePayloadType<"response", ResponseMessageData>

  // When the invokeMonaco method was called on the widget
  | MonacoMessagePayloadType<"invokeMonaco", InvokeMonacoMessageData<import("monaco-editor").editor.IStandaloneCodeEditor>>

  // When the invokeMonaco method was called on the widget
  | MonacoMessagePayloadType<"invokeMonacoDiff", InvokeMonacoMessageData<import("monaco-editor").editor.IStandaloneDiffEditor>>

  // When the invokeMonacoScript method was called on the widget
  | MonacoMessagePayloadType<"invokeMonacoScript", InvokeMonacoScriptData>

  // When the value of the Monaco editor changed
  | MonacoMessagePayloadType<"valueChange", ValueChangeMessageData>

  // When the value of the original Monaco editor changed (for the diff editor)
  | MonacoMessagePayloadType<"originalValueChange", ValueChangeMessageData>

  // When the scroll position of the Monaco editor changed
  | MonacoMessagePayloadType<"scrollChange", ScrollChangeMessageData>

  // When the scroll position of the original Monaco editor changed (for the diff editor)
  | MonacoMessagePayloadType<"originalScrollChange", ScrollChangeMessageData>

  // When a DOM event (blur / focus / mousemove etc.) occurred on the Monaco editor
  | MonacoMessagePayloadType<"domEvent", DomEventMessageData>

  // After the editor was rendered
  | MonacoMessagePayloadType<"afterInit", AfterInitMessageData>
  ;