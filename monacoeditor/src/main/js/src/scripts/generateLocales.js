// @ts-check

import { promises as fs } from "node:fs";
import { basename, dirname, join, relative } from "node:path";

import mkdirp from "mkdirp";
import ncp from "ncp";
import recursive from "recursive-readdir";
import rimraf from "rimraf";

import { gitUpdateToTheirs } from "../util/git-clone-or-pull.js";

import {
    monacoDir,
    monacoModDir,
    monacoModEsmDir,
    gitDir,
    vsCodeLocDir,
    vsCodeLocI18nDir,
    generatedSourceLocaleDir,
} from "../util/paths.js";

const langDirPrefix = "vscode-language-pack-";
const vsCodeRepository = "https://github.com/Microsoft/vscode-loc";

const fileExistsCache = new Map();

/**
 * @type {Record<string, string>}
 */
const LocaleMapping = {
    "zh_HANS": "zh_CN",
    "zh_HANT": "zh_TW",
}

/**
 * @param {string} dir 
 */
async function isExistingFile(dir) {
    try {
        const stat = await fs.lstat(dir);
        return stat.isFile();
    }
    catch (e) {
        return false;
    }
}

/**
 * @param {string} dir 
 */
async function isExistingDir(dir) {
    try {
        const stat = await fs.lstat(dir);
        return stat.isDirectory();
    }
    catch (e) {
        return false;
    }
}

/**
 * The microsoft/vscode-loc contains many more i18n keys that are used by monaco editor.
 * Keys are grouped by source files, so we include only those keys whose source files
 * exists in the monaco editor repository.
 * @param {string} key
 * @return {Promise<boolean>}
 */
async function sourceFileExists(key) {
    if (fileExistsCache.has(key)) {
        return fileExistsCache.get(key);
    }
    const filePath = join(monacoModEsmDir, key + ".js");
    const exists = await isExistingFile(filePath);
    fileExistsCache.set(key, exists);
    return exists;
}

/**
 * @param {string} key 
 * @returns {Promise<string | undefined>}
 */
async function resolveKeyToSourceFile(key) {
    if (await sourceFileExists(key)) {
        return key;
    }
    const withoutBrowser = key.replace(/\/browser\//g, "/");
    if (await sourceFileExists(withoutBrowser)) {
        return withoutBrowser;
    }
    return undefined;
}

/**
 * @param {string} file 
 * @param {[RegExp, string][]} replacements 
 * @returns {Promise<void>}
 */
async function replaceInFile(file, ...replacements) {
    let content = await fs.readFile(file, { encoding: "utf-8" });
    let replaced = false;
    for (const replacement of replacements) {
        replaced = replaced || replacement[0].test(content);
        content = content.replace(replacement[0], replacement[1]);
    }
    if (replaced) {
        await fs.writeFile(file, content, { encoding: "utf-8" });
    }
}

/**
 * @param {string} source 
 * @param {string} target 
 * @returns {Promise<void>}
 */
function copyDir(source, target) {
    return new Promise((resolve, reject) => {
        ncp.ncp(source, target, (/** @type {unknown} */ error) => {
            if (error) {
                reject(error);
            }
            else {
                resolve();
            }
        })
    });
}

/**
 * The call to the `localize` function only include the i18 key, but translations
 * in the microsoft/vscode-loc repository are first grouped by source file name,
 * then i18n key. So we modify the each call to `localize` so that it includes
 * the source file name.
 *
 * > nls.localize("my.key", args)
 *
 * becomes
 *
 * > nls.localize("source/file.js", "my.key", args)
 */
async function injectSourcePath() {
    await rimraf(monacoModDir);
    await copyDir(monacoDir, monacoModDir);
    const files = await recursive(monacoModEsmDir);
    for (const file of files) {
        if (file.endsWith(".js")) {
            const vsPath = relative(monacoModEsmDir, dirname(file)).replace(/\\/g, "/");
            const transPath = vsPath + "/" + basename(file, ".js");
            await replaceInFile(file,
                [
                    /(?<!function\s+)localize\(/g,
                    `localize('${transPath}', `,
                ],
                [
                    /localize\.apply\(\s*([^,]+)\s*,\s*\[/g,
                    `localize.apply($1, ['${transPath}', `,
                ],
            );
        }
    }
}

/**
 * Reads all files from the microsoft/vscode-loc repository for the given language
 * and creates one object with all i18n keys.
 * @param {string} lang Language, eg. `en` or `de`.
 * @param {string} langPath Full path to the directory with the language files.
 * @returns {Promise<Record<string, unknown>>} The created locale object.
 */
async function createLocale(lang, langPath) {
    /** @type {Record<string, unknown>} */
    const locale = {};
    /** @type {Record<string, unknown>} */
    const allTranslations = {};
    const files = await recursive(langPath);
    for (const file of files) {
        if (file.endsWith(".i18n.json")) {
            const data = await fs.readFile(file, { encoding: "utf-8" });
            let json;
            try {
                json = JSON.parse(data);
            }
            catch (e1) {
                try {
                    json = eval("(" + data + ")");
                }
                catch (e2) {
                    const newErr = new Error("Error while parsing i18n file " + file);
                    if (e2 instanceof Error) {
                        newErr.stack += "\n\ncaused by: " + e2.stack;
                    }
                    throw newErr;
                }
            }
            if (typeof json.contents !== "object") {
                console.warn("no translations found", file);
                continue;
            }
            delete json.contents["package"];
            for (const originalKey of Object.keys(json.contents)) {
                if (originalKey) {
                    const sourceFileKey = await resolveKeyToSourceFile(originalKey);
                    if (sourceFileKey !== undefined) {
                        locale[sourceFileKey] = json.contents[originalKey];
                        allTranslations[sourceFileKey] = json.contents[originalKey];
                    }
                }
            }
        }
    }
    return locale;
}

/**
 * @param {string} lang 
 * @param {Record<string, unknown>} locale 
 * @returns {string}
 */
function createScript(lang, locale) {
    const sortedKeys = Object.keys(locale).sort((lhs, rhs) => {
        const l = lhs.toLowerCase();
        const r = rhs.toLowerCase();
        return l < r ? -1 : l > r ? 1 : 0;
    });
    /** @type {Record<string, unknown>} */
    const sortedLocale = {};
    for (const key of sortedKeys) {
        sortedLocale[key] = locale[key];
    }
    return "this.MonacoEnvironment = this.MonacoEnvironment || {}; this.MonacoEnvironment.Locale = {language: '" + lang + "', data: " + JSON.stringify(sortedLocale, null, 4) + "};";
}

/**
 * Takes the name of the directory with the localization from github, and
 * extracts the locale code. Returns a Java locale name, e.g. `pt_BR` instead of `pt-br`.
 * @param {string} dir 
 * @return {string}
 */
function extractLocaleCode(dir) {
    const normalized = dir
        .substring(langDirPrefix.length)
        .split("-")
        .map((part, index) => index === 0 ? part.toLowerCase() : part.toUpperCase())
        .join("_");
    return LocaleMapping[normalized] ?? normalized;
}

async function main() {
    await mkdirp.native(gitDir);
    await injectSourcePath();

    // Fetch the locale data from the github repo
    console.log("Checking out", vsCodeRepository, "to", vsCodeLocDir);
    await gitUpdateToTheirs(vsCodeRepository, vsCodeLocDir);

    // Each available languages is in its own directory
    const langDirs = await fs.readdir(vsCodeLocI18nDir);

    // For each available language...
    for (const langDir of langDirs) {
        if (!langDir.startsWith(langDirPrefix)) {
            continue;
        }
        const lang = extractLocaleCode(langDir);
        const transPath = join(vsCodeLocI18nDir, langDir, "translations");
        if (!await isExistingDir(transPath)) {
            continue;
        }
        const locale = await createLocale(lang, transPath);
        await mkdirp.native(generatedSourceLocaleDir)
        const mappedLang = lang;
        const path = join(generatedSourceLocaleDir, mappedLang + ".js");
        const content = createScript(mappedLang, locale);
        await fs.writeFile(path, content, { encoding: "utf-8" });
        console.log("generated locale " + mappedLang + ".js (" + path + ")");
    }
}

main();
