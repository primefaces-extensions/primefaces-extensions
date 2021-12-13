type PromiseFactory<T> = () => Promise<T>;
type PromiseCallback = (value: any) => void;
interface QueueItem {
  factory: PromiseFactory<unknown>;
  resolve: PromiseCallback;
  reject: PromiseCallback;
}
interface IPromiseQueue<T> {
  add(promiseFactory: PromiseFactory<T>): Promise<T>;
  addAll(...promiseFactory: PromiseFactory<T>[]): Promise<T>[];
}

interface RenderArgs {
  extender: PrimeFaces.widget.ExtMonacoEditorBase.MonacoExtenderInline;
  options: import("monaco-editor").editor.IEditorConstructionOptions;
  wasLibLoaded: boolean;
}

interface RenderedArgs extends RenderArgs {
  editor: import("monaco-editor").editor.IStandaloneCodeEditor;
  extender: PrimeFaces.widget.ExtMonacoEditorBase.MonacoExtenderInline;
  options: import("monaco-editor").editor.IEditorConstructionOptions;
  wasLibLoaded: boolean;
}

interface Helper {
  assign: typeof Object.assign;
  createEditorConstructionOptions: (
    context: PrimeFaces.widget.ExtMonacoEditorBase.SyncMonacoContext,
    extender: PrimeFaces.widget.ExtMonacoEditorBase.ExtenderBase<PrimeFaces.widget.ExtMonacoEditorBase.SyncMonacoContext>,
    editorValue: string, wasLibLoaded: boolean
  ) => Promise<import("monaco-editor").editor.IEditorConstructionOptions>;
  createModel: (
    context: PrimeFaces.widget.ExtMonacoEditorBase.SyncMonacoContext,
    editorOptions: import("monaco-editor").editor.IEditorConstructionOptions,
    extender: PrimeFaces.widget.ExtMonacoEditorBase.ExtenderBase<PrimeFaces.widget.ExtMonacoEditorBase.SyncMonacoContext>,
    value?: string
  ) => import("monaco-editor").editor.ITextModel;
  endsWith: (string: any, suffix: any) => boolean;
  getFacesResourceUri: () => string;
  getMonacoResource: (resource: string, queryParams?: Record<string, number | string | string[]>) => string;
  getScript: (url: string) => Promise<void>;
  getScriptName: (moduleId: string, label: string) => string;
  globalEval: (script: string, nonce?: string) => void;
  invokeMonaco: <K extends keyof import("monaco-editor").editor.IStandaloneCodeEditor>(
    editor: import("monaco-editor").editor.IStandaloneCodeEditor,
    method: K,
    args: Parameters<import("monaco-editor").editor.IStandaloneCodeEditor[K]>
  ) => Promise<PrimeFaces.UnwrapPromise<ReturnType<import("monaco-editor").editor.IStandaloneCodeEditor[K]>>>;
  invokeMonacoScript: <TRet, TArgs extends any[]>(
    editor: import("monaco-editor").editor.IStandaloneCodeEditor,
    script: ((editor: import("monaco-editor").editor.IStandaloneCodeEditor, ...args: TArgs) => TRet) | string,
    args: TArgs,
    evalFn: (script: string) => void
  ) => Promise<PrimeFaces.UnwrapPromise<TRet>>;
  isNotNullOrUndefined: <T>(x: T | null | undefined) => x is T;
  isNullOrUndefined: (x: unknown) => x is null | undefined;
  loadEditorLib: (options: Partial<PrimeFaces.widget.ExtMonacoEditorBaseCfgBase>, forceLibReload: boolean) => Promise<boolean>;
  loadExtender: (
    options: Partial<PrimeFaces.widget.ExtMonacoEditorBaseCfgBase>
  ) => PrimeFaces.widget.ExtMonacoEditorBase.ExtenderBase<PrimeFaces.widget.ExtMonacoEditorBase.SyncMonacoContext>;
  loadLanguage: (options: Partial<PrimeFaces.widget.ExtMonacoEditorBaseCfgBase>) => Promise<{
    forceLibReload: boolean;
    localeUrl: string;
  }>;
  resolveLocaleUrl: (options: Partial<PrimeFaces.widget.ExtMonacoEditorBaseCfgBase>) => string;
  BaseEditorDefaults: PrimeFaces.widget.ExtMonacoEditorBaseCfgBase;
  DefaultThemeData: import("monaco-editor").editor.IStandaloneThemeData;
  FramedEditorDefaults: PrimeFaces.widget.ExtMonacoEditorFramedCfgBase;
  InlineEditorDefaults: PrimeFaces.widget.ExtMonacoEditorInlineCfgBase;
}

interface PromiseHook<T> {
  resolve: (widget: T) => void;
  reject: (reason: any) => void;
}

declare class ExtMonacoEditorBase<
  TCfg extends PrimeFaces.widget.ExtMonacoEditorBaseCfg
  > extends PrimeFaces.widget.ExtMonacoEditorBase<TCfg> {
  _init(cfg: PrimeFaces.PartialWidgetCfg<TCfg>, defaults: Partial<TCfg>): void;
  _bindEvents(): void;
  _editorValue: string;
  _onDone: PromiseHook<this>[];
  _fireEvent(eventName: string, ...params: any[]): void;
  _listSupportedEvents(): string[];
  _rejectOnDone(): void;
  _supportsEvent(eventName: string): boolean;
  _onInitSuccess(): void;
  _onInitError(error: unknown): void;
}

interface Window {
  monacoModule: {
    helper: Helper,
    PromiseQueue: new <T>() => IPromiseQueue<T>;
    ExtMonacoEditorBase: typeof ExtMonacoEditorBase;
  },
}

// == Post message events

declare type BaseMessage<K extends string, T> = {
  kind: K,
  data: T,
  messageId?: number;
  instanceId?: number;
};

type InitMessageData = {
  facesResourceUri: string,
  id: string;
  options: Partial<PrimeFaces.widget.ExtMonacoEditorFramedCfgBase>,
  nonce: string | undefined;
  resolvedLocaleUrl: string;
  resourceUrlExtension: string;
  scrollTop: number;
  supportedEvents: string[];
  value: string;
  version: string;
};

type ResponseMessageData = {
  success: true;
  value: any;
} | {
  success: false;
  error: string;
};

type InvokeMonacoMessageData<K extends keyof import("monaco-editor").editor.IStandaloneCodeEditor = keyof import("monaco-editor").editor.IStandaloneCodeEditor> = {
  method: K;
  args: Parameters<import("monaco-editor").editor.IStandaloneCodeEditor[K]>;
}

type InvokeMonacoScriptData = {
  script: string;
  args: any[];
};

type InvokeMonacoScriptMessageData = {
  script: string;
  args: any[];
}

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

type MonacoMessage =
  | BaseMessage<"load", undefined>

  // When the widget was created and the editor needs to be rendered
  | BaseMessage<"init", InitMessageData>

  // When the widget was destroyed
  | BaseMessage<"destroy", undefined>

  // When the event listeners should be added to the Monaco editor
  | BaseMessage<"bindEvents", BindEventsMessageData>

  // When the iframe controller responds to a message sent by the widget
  | BaseMessage<"response", ResponseMessageData>

  // When the invokeMonaco method was called on the widget
  | BaseMessage<"invokeMonaco", InvokeMonacoMessageData>

  // When the invokeMonacoScript method was called on the widget
  | BaseMessage<"invokeMonacoScript", InvokeMonacoScriptData>

  // When the value of the Monaco editor changed
  | BaseMessage<"valueChange", ValueChangeMessageData>

  // When the scroll position of the Monaco editor changed
  | BaseMessage<"scrollChange", ScrollChangeMessageData>

  // When a DOM event (blur / focus / mousemove etc.) occurred on the Monaco editor
  | BaseMessage<"domEvent", DomEventMessageData>

  // After the editor was rendered
  | BaseMessage<"afterInit", AfterInitMessageData>
  ;
