// @ts-check

declare module "monaco-editor" {
  export interface MonacoLocale {
    /** Map between the localization key and the localized string. */
    data: Record<string, any>;
    /** Language code such as `en` or `de`. */
    language: string;
  }

  export interface Environment {
    /** Locale data to be used by the editor. */
    Locale?: MonacoLocale;

    /** Used to define an extender object */
    Extender?: PrimeFaces.widget.ExtMonacoEditorBase.MonacoExtenderInline;

    /** Used by the monaco editor in the iframe. */
    InstanceId?: number;

    /** Used by the monaco editor in the iframe.  Available only during a call to `invokeMonacoScript`. */
    _INVOKE?: {
      args: any[];
      error: any;
      return: any;
    };
    /** Used by the monaco editor in the iframe. */
    IframeContext?: PrimeFaces.widget.ExtMonacoEditorBase.SyncMonacoContext;
  }
}

declare global {
  /**
   * The official API of the Monaco editor, exposed as a global variable.
   */
  let monaco: typeof import("monaco-editor");

  /**
   * Exposes the internal API of monaco editor. May be used to customize the editor even further.
   *
   * __THIS IS UNSUPPORTED AND MAY BE CHANGED WITHOUT NOTICE. MAKE SURE YOU KNOW WHAT YOU ARE DOING AND USE AT YOUR OWN RISK.__
   */
  let monacoExtras: any;
}

export {}