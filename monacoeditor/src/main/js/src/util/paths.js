// @ts-check

import { join, resolve } from "node:path";
import { getDirname } from "./import-meta.js";

export const projectDir = join(getDirname(import.meta), "..", "..", "..", "..", "..");
export const srcDir = join(projectDir, "src");
export const targetDir = join(projectDir, "target");
export const npmDir = join(srcDir, "main", "js");
export const nodeModulesDir = join(npmDir, "node_modules");

export const javaDescriptorPackage = "org.primefaces.extensions.model.monacoeditor";
export const javaDescriptorPath = join(targetDir, "generated-sources", "java", "org", "primefaces", "extensions", "model", "monacoeditor");

export const monacoDir = join(nodeModulesDir, "monaco-editor");
export const monacoModDir = join(nodeModulesDir, "monaco-editor-mod");
export const monacoModEsmDir = join(monacoModDir, "esm");
export const webpackOutputDir = resolve(targetDir, "generated-sources", "js");

export const gitDir = join(targetDir, "git");
export const vsCodeLocDir = join(gitDir, "vscode-loc");
export const vsCodeLocI18nDir = join(vsCodeLocDir, "i18n");
export const generatedSourceLocaleDir = join(targetDir, "generated-sources", "js", "locale");

export const typedocInputFile = join(npmDir, "src", "primefaces-monaco.d.ts");
export const typedocTempFile = join(npmDir, "src", "tmp.d.ts");
export const typedocOutputDir = join(projectDir, "docs", "typedoc");
export const typedocReadme = join(npmDir, "typedoc-readme.md");