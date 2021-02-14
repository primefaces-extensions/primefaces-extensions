// Create an extender that loads the the given TypeScript definition files into the editor
// This only works when language is set to "javascript"
// use monaco.languages.typescript.typescriptDefaults for language="typescript"
function createExtender() {
  for (var _len = arguments.length, typescriptDefinitionFiles = new Array(_len), _key = 0; _key < _len; _key++) {
    typescriptDefinitionFiles[_key] = arguments[_key];
  }

  return {
    beforeCreate: function beforeCreate(widget, options, wasLibLoaded) {
      // Since the configuration is global, we must add the TypeScript definitions files only
      // if the library was loaded or reloaded.
      if (!wasLibLoaded) return;
      
      // Load all typescript definitions files from the network
      var fetched = typescriptDefinitionFiles.map(function (file) {
        return fetch(file).then(function (response) {
          return response.text();
        }).then(function (text) {
          return {
            text: text,
            file: file
          };
        });
      });
      
      // Loop over all loaded definition files and add them to the editor
      return Promise.all(fetched).then(function (defs) {
        return defs.forEach(function (def) {
          monaco.languages.typescript.javascriptDefaults.addExtraLib(def.text, def.file);
        });
      }).then(function () {
        return options;
      });
    }
  };
}

// Creates an extender with some basic TypeScript definitions files for JQuery
function createExtenderBasic() {
  return createExtender("https://raw.githubusercontent.com/DefinitelyTyped/DefinitelyTyped/master/types/sizzle/index.d.ts", "https://raw.githubusercontent.com/DefinitelyTyped/DefinitelyTyped/master/types/jquery/JQuery.d.ts", "https://raw.githubusercontent.com/DefinitelyTyped/DefinitelyTyped/master/types/jquery/JQueryStatic.d.ts", "https://raw.githubusercontent.com/DefinitelyTyped/DefinitelyTyped/master/types/jquery/misc.d.ts", "https://raw.githubusercontent.com/DefinitelyTyped/DefinitelyTyped/master/types/jquery/legacy.d.ts", "https://raw.githubusercontent.com/DefinitelyTyped/DefinitelyTyped/master/types/jqueryui/index.d.ts");
}

// This is set when the extender is used in the framed editor
// Then we must create the extender and set it on the MonacoEnvironemnt
if (window.MonacoEnvironment) {
  window.MonacoEnvironment.Extender = createExtenderBasic();
}
