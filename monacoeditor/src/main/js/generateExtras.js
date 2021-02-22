const fs = require("fs");
const path = require("path");
const recursive = require("recursive-readdir");

const {
    monacoModEsmDir,
} = require("./paths");

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
 */
function toJsIdentifier(name) {
    return name.replace(/[.-]/g, "_").replace(/[^_$a-zA-Z0-9]/g, "");
}

/**
 * @param {string} word 
 */
function capitalize(word) {
    if (!word) return "";
    return word.charAt(0).toUpperCase() + word.substr(1);
}

/**
 * @param {string} word 
 */
function lowerize(word) {
    if (!word) return "";
    return word.charAt(0).toLowerCase() + word.substr(1);
}

/**
 * @param {object} object 
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
        default: throw new Error("Unknown data:", data);
    }
    return buffer;
}

function toUnixPath(path) {
    return path ? path.replace(/\\/g, "/") : "";
}

function buildExtra(callback) {
    recursive(monacoModEsmDir, (err, files) => {
        if (err) {
            callback(err);
            return;
        }
        const lines = [];
        const exportObject = {};
        files.forEach(file => {
            const extname = path.extname(file);
            if (extname === ".js") {
                const relativeFile = toUnixPath(path.relative(monacoModEsmDir, file));
                const basename = path.basename(relativeFile, extname);
                const relativeDirname = path.dirname(relativeFile);

                if (Excludes.some(exclude => relativeFile.startsWith(exclude))) {
                    console.log("Skipping excluded file " + relativeFile);
                    return;
                }

                const parts = relativeDirname.split("/");
                const normalizedPath = parts.map(lowerize).concat(capitalize(basename)).map(toJsIdentifier);
                const importName = normalizedPath.join("$");
                putAtPath(exportObject, normalizedPath, importName);
                lines.push(`import * as ${importName} from "./${relativeFile}"`);
            }
        });
        lines.push(`const monacoExtras = ${buildJsObject(exportObject).join("")};`);
        lines.push("window.monacoExtras = monacoExtras;");
        lines.push("export {monacoExtras};");
        callback(undefined, lines);
    });
}

/**
 * Assembles all modules found in the ESM version of the Monaco editor library
 * and exposes them in the global scope.
 */
function main() {
    buildExtra((err, lines) => {
        if (err) throw err;
            fs.writeFile(path.join(monacoModEsmDir, "monacoExtras.js"), lines.join("\n"), {encoding: "UTF-8"}, err => {
                if (err) throw err;
                console.log("Extras built successfully.")
            });
    });
}

main();