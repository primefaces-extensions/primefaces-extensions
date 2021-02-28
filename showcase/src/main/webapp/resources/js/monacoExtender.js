// Create a Monaco extender with the given settings.
// settings.declarations: A list of TypeScript declaration files that will be added to the editor
// settings.language    : The code language for which to set up the extender.
// settings.tscheck     : if true, enable type checking for JavaScript files.
function createExtender(settings) {
  return {
    beforeCreate: function beforeCreate(widget, options, wasLibLoaded) {
      // Since the configuration is global, we must add the TypeScript
      // declaration files only if the library was loaded or reloaded.
      if (!wasLibLoaded) {
        return;
      }
      
      // Enable JavaScript type checking
      if (settings.tsCheck && settings.language === "javascript") {
        monaco.languages.typescript.javascriptDefaults.setCompilerOptions(Object.assign(
          {},
          monaco.languages.typescript.javascriptDefaults.getCompilerOptions(),
          { checkJs: true }
        ));
        monaco.languages.typescript.javascriptDefaults.setDiagnosticsOptions(Object.assign(
          {},
          monaco.languages.typescript.javascriptDefaults.getDiagnosticsOptions(),
          { noSemanticValidation: false }
        ));
      }

      // Load all requested TypeScript declaration files from the network
      var promiseDeclarationsSingle = settings.declarations.map(function (file) {
        var path = Array.isArray(file) ? file[0] : file
        var name = Array.isArray(file) ? file[1] : file;
        return fetch(path)
          .then(function (response) { return response.text() })
          .then(function (text) { return { text: text, file: name } });
      });
      
      // Loop over all fetched declaration files and add them to the editor
      var promiseDeclarationsAll = Promise.all(promiseDeclarationsSingle)
        .then(function (declarations) {
          return declarations.forEach(function (declaration) {
            if (settings.language === "javascript") {
              monaco.languages.typescript.javascriptDefaults.addExtraLib(declaration.text, declaration.file);
            }
            if (settings.language === "typescript") {
              monaco.languages.typescript.typescriptDefaults.addExtraLib(declaration.text, declaration.file);
            }
          });
        });

      return Promise.all([
          promiseDeclarationsAll
        ])
        .then(function () {
          return options;
        });
    }
  };
}

// Creates an extender with some basic TypeScript declaration files for JQuery / JQuery UI
function createExtenderJQuery(params) {
  return createExtender(Object.assing({}, {
    declarations: [
      "https://raw.githubusercontent.com/DefinitelyTyped/DefinitelyTyped/master/types/sizzle/index.d.ts",
      "https://raw.githubusercontent.com/DefinitelyTyped/DefinitelyTyped/master/types/jquery/JQuery.d.ts",
      "https://raw.githubusercontent.com/DefinitelyTyped/DefinitelyTyped/master/types/jquery/JQueryStatic.d.ts",
      "https://raw.githubusercontent.com/DefinitelyTyped/DefinitelyTyped/master/types/jquery/misc.d.ts",
      "https://raw.githubusercontent.com/DefinitelyTyped/DefinitelyTyped/master/types/jquery/legacy.d.ts",
      "https://raw.githubusercontent.com/DefinitelyTyped/DefinitelyTyped/master/types/jqueryui/index.d.ts"
    ]
  }, params));
}

// Creates an extender with the TypeScript declaration files for the Monaco editor API
function createExtenderMonaco(params) {
  return createExtender(Object.assign({}, {
    declarations: [
      [
        "https://unpkg.com/monaco-editor@0.22.3/esm/vs/editor/editor.api.d.ts",
        "file:///node_modules/@types/monaco-editor/index.d.ts"
      ],
      [
        "https://raw.githubusercontent.com/primefaces-extensions/primefaces-extensions/master/core/src/main/resources/META-INF/resources/primefaces-extensions/monacoeditor/primefaces-monaco-global.d.ts",
        "file:///src/primefaces-monaco-global.d.ts"
      ],
      [
        "https://raw.githubusercontent.com/primefaces-extensions/primefaces-extensions/master/core/src/main/resources/META-INF/resources/primefaces-extensions/monacoeditor/primefaces-monaco-module.d.ts",
        "file:///src/primefaces-monaco-module.d.ts"
      ]
    ]
  }, params));
}

// Parses the URL parameters of the current URL
function parseUrlParams() {
  var search = window.location.search;
  search = search.charAt(0) === "?" ? search.substring(1) : search;
  return search.split("&")
    .map(function(pair){ return pair.split("=").map(decodeURIComponent) })
    .reduce( function(obj, pair) { obj[pair[0]] = pair[1]; return obj }, {});
}

// This is set when the extender is used in the framed editor
// Then we must create the extender and set it on the MonacoEnvironemnt
if (window.MonacoEnvironment) {
  var params = parseUrlParams();
  switch (params.extender) {
  case "jquery":
    window.MonacoEnvironment.Extender = createExtenderJQuery(params);
    break;
  case "monaco":
    window.MonacoEnvironment.Extender = createExtenderMonaco(params);
    break;
  }
}
