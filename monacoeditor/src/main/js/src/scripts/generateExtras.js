// @ts-check

import { promises as fs } from "node:fs";
import { basename, dirname, extname, join, relative } from "node:path";
import recursive from "recursive-readdir";

import { monacoModEsmDir } from "../util/paths.js";

const Excludes = [
    "monacoExtras.js",

    "vs/editor/edcore.main.js",
    "vs/editor/editor.all.js",
    "vs/editor/editor.api.js",
    "vs/editor/editor.main.js",

    "vs/editor/editor.worker.js",

    "vs/language/json/_deps",
    "vs/language/json/json.worker.js",
    "vs/language/json/jsonWorker.js",

    "vs/language/css/_deps",
    "vs/language/css/css.worker.js",
    "vs/language/css/cssWorker.js",

    "vs/language/html/_deps",
    "vs/language/html/html.worker.js",
    "vs/language/html/htmlWorker.js",

    "vs/language/typescript/lib",
    "vs/language/typescript/ts.worker.js",
    "vs/language/typescript/tsWorker.js",
];

/**
 * @param {string} name 
 * @returns {string}
 */
function toJsIdentifier(name) {
    return name.replace(/[.-]/g, "_").replace(/[^_$a-zA-Z0-9]/g, "");
}

/**
 * @param {string} word 
 * @returns {string}
 */
function capitalize(word) {
    if (!word) return "";
    return word.charAt(0).toUpperCase() + word.substr(1);
}

/**
 * @param {string} word 
 * @returns {string}
 */
function lowerize(word) {
    if (!word) return "";
    return word.charAt(0).toLowerCase() + word.substr(1);
}

/**
 * @param {Record<string, any>} object 
 * @param {string[]} path 
 * @param {any} value 
 */
function putAtPath(object, path, value) {
    for (const part of path.slice(0, -1)) {
        if (!(part in object)) {
            object[part] = {};
        }
        object = object[part];
    }
    object[path[path.length - 1]] = value;
}

/**
 * 
 * @param {Record<string, any> | string} data 
 * @param {string[]} [buffer] 
 * @param {number} [indent] 
 * @returns {string[]}
 */
function buildJsObject(data, buffer = [], indent = 0) {
    switch (typeof data) {
        case "object": {
            buffer.push("{");
            buffer.push("\n");
            buffer.push(" ".repeat(indent));
            for (const key of Object.keys(data)) {
                buffer.push("\"");
                buffer.push(key);
                buffer.push("\"");
                buffer.push(": ");
                buffer = buildJsObject(data[key], buffer, indent + 4);
                buffer.push(",");
                buffer.push("\n");
                buffer.push(" ".repeat(indent));
            }
            buffer.push("}");
            break;
        }
        case "string": {
            buffer.push(data);
            break;
        }
        default: throw new Error("Unknown data: " + data);
    }
    return buffer;
}

/**
 * @param {string} path 
 * @returns {string}
 */
function toUnixPath(path) {
    return path ? path.replace(/\\/g, "/") : "";
}

/**
 * @return {Promise<string[]>}
 */
async function buildExtra() {
    const files = await recursive(monacoModEsmDir);
    /** @type {string[]} */
    const lines = [];
    /** @type {Record<string, unknown>} */
    const exportObject = {};
    for (const file of files) {
        const extension = extname(file);
        if (extension === ".js") {
            const relativeFile = toUnixPath(relative(monacoModEsmDir, file));
            const fileName = basename(relativeFile, extension);
            const relativeDirName = dirname(relativeFile);

            if (Excludes.some(exclude => relativeFile.startsWith(exclude))) {
                console.log("Skipping excluded file " + relativeFile);
                continue;
            }

            const parts = relativeDirName.split("/");
            const normalizedPath = parts.map(lowerize).concat(capitalize(fileName)).map(toJsIdentifier);
            const importName = normalizedPath.join("$");
            putAtPath(exportObject, normalizedPath, importName);
            lines.push(`import * as ${importName} from "./${relativeFile}"`);
        }
    };
    lines.push(`const monacoExtras = ${buildJsObject(exportObject).join("")};`);
    lines.push("window.monacoExtras = monacoExtras;");
    lines.push("export { monacoExtras };");
    return lines;
}

/**
 * Assembles all modules found in the ESM version of the Monaco editor library
 * and exposes them in the global scope.
 */
async function main() {
    const lines = await buildExtra();
    const extrasPath = join(monacoModEsmDir, "monacoExtras.js");
    const content = lines.join("\n");
    await fs.writeFile(extrasPath, content, { encoding: "utf-8" });
    console.log("Extras built successfully.")
}

main();