# Monaco Editor JSF Component

Monaco Editor resources for PrimeFaces Extensions. See https://microsoft.github.io/monaco-editor/

This contains the node JS scripts that download the monaco editor sources and create the minfied files.

## Build steps

The build takes the following steps:

* When not installed yet, node and npm are downloaded and installed via the frontend-maven-plugin
* `npm install` is run on `src/main/web`
* The locale files are downloaded from `"git://github.com/Microsoft/vscode-loc.git"` and processed so that they can be loaded into monaco editor. Monaco editor bundled with webpack normally does not support language files and switching languages on the fly. To work around this, the monaco editor source file `nls.js` gets replaced during the webpack build with our own file (see file `nls-replace.js`) that reads the language translations from a browser global.
* Java POJOs for the editor configuration are generated so basic settings can be set on the component via Java. The available options are documented at [https://microsoft.github.io/monaco-editor/api/interfaces/monaco.editor.istandaloneeditorconstructionoptions.html](https://microsoft.github.io/monaco-editor/api/interfaces/monaco.editor.istandaloneeditorconstructionoptions.html). The file `descriptor/create.js` contains the list of properties with the type and a description and creates Java class files based on that. Maintaining that is easier that having to maintain the Java POJOs itself with redundant getters and setters.
* Finally, webpack is run on the monaco editor source file to produce the minified bundle. There are separate bundles for the editor core, the web workers, and the locale files.

## Updating monaco editor

* First, read the release notes [https://github.com/microsoft/monaco-editor/blob/master/CHANGELOG.md](https://github.com/microsoft/monaco-editor/blob/master/CHANGELOG.md) for possible breaking changes etc.

* Then, update the NPM dependency:

```bash
cd src/main/web
npm outdated
npm install monaco-editor@latest
```

* Next, check `descriptor/create.js` against [https://microsoft.github.io/monaco-editor/api/interfaces/monaco.editor.istandaloneeditorconstructionoptions.html](https://microsoft.github.io/monaco-editor/api/interfaces/monaco.editor.istandaloneeditorconstructionoptions.html) and adjust as necessary.

* Run a normal maven install and use the showcase to check if everything still works.