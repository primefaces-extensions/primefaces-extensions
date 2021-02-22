// === REMOVE once we use main PF types

declare function PF(widgetVar: string): PrimeFaces.widget.BaseWidget;

declare namespace PrimeFacesExt {
  const VERSION: string;
}

declare namespace PrimeFaces {
  function info(message: string): void;
  function warn(message: string): void;
  function error(message: string): void;
  namespace resources {
    function getFacesResource(path: string, lib: string, version?: string): string;
    function getResourceUrlExtension(): string;
  }
  namespace csp {
    let NONCE_VALUE: string | undefined;
    function eval(script: string, nonce?: string): void;
  }
  const widgets: { [widgetVar: string]: widget.BaseWidget };
  namespace widget {
    interface BaseWidgetCfg {
      id: string;
    }
    interface DeferredWidgetCfg extends BaseWidgetCfg {
    }
  }
  namespace widget {
    class BaseWidget<TConf = widget.BaseWidgetCfg> {
      id: string;
      cfg: TConf;
      jq: JQuery;
      jqEl: HTMLElement;
      jqId: string;
      widgetVar: string;
      constructor(...args: any[]);
      addRefreshListener(listener: () => void): void;
      addDestroyListener(listener: () => void): void;
      getJQ(): JQuery;
      hasBehavior(name: string): boolean;
      init(configuration: { [key: string]: any }): void;
      refresh(configuration: { [key: string]: any }): void;
      removeScriptElement(clientId: string): void;
      isDetached(): boolean;
      destroy(): void;
      callBehavior(name: string, ...args: unknown[]): void;
    }
    class DeferredWidget<TConf = widget.DeferredWidgetCfg> extends BaseWidget<TConf> {
      renderDeferred(): void;
    }
  }
}
