const path = require("path");

exports.projectDir = path.join(__dirname, "..", "..", "..");
exports.srcDir = path.join(exports.projectDir, "src");
exports.targetDir = path.join(exports.projectDir, "target");
exports.npmDir = path.join(exports.srcDir, "main", "js");
exports.nodeModulesDir= path.join(exports.npmDir, "node_modules");

exports.javaDescriptorPackage = "org.primefaces.extensions.model.monacoeditor";
exports.javaDescriptorPath= path.join(exports.targetDir, "generated-sources", "java", "org", "primefaces", "extensions", "model", "monacoeditor");

exports.monacoDir = path.join(exports.nodeModulesDir, "monaco-editor");
exports.monacoModDir = path.join(exports.nodeModulesDir, "monaco-editor-mod");
exports.monacoModEsmDir= path.join(exports.monacoModDir, "esm");
exports.webpackOutputDir = path.resolve(exports.targetDir, "generated-sources", "js");

exports.gitDir = path.join(exports.targetDir, "git");
exports.vsCodeLocDir = path.join(exports.gitDir, "vscode-loc");
exports.vsCodeLocI18nDir = path.join(exports.vsCodeLocDir, "i18n");
exports.generatedSourceLocaleDir = path.join(exports.targetDir, "generated-sources", "js", "locale");

exports.typedocInputFile = path.join(exports.npmDir, "src", "primefaces-monaco.d.ts");
exports.typedocTempFile = path.join(exports.npmDir, "src", "tmp.d.ts");
exports.typedocOutputDir = path.join(exports.projectDir, "docs", "typedoc");
exports.typedocReadme = path.join(exports.npmDir, "typedoc-readme.md");