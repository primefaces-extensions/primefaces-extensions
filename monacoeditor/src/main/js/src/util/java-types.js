// @ts-check

import { promises as fs } from "node:fs";
import { join } from "node:path";
import rimraf from "rimraf";
import mkdirp from "mkdirp";

import { javaDescriptorPackage, javaDescriptorPath } from "./paths.js";

/** {@type string} */
const HeaderComment = `
/*
 * Copyright (c) 2011-2022 PrimeFaces Extensions
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */`;

const GeneratedComment = `
// ============================================================
// THIS FILE WAS GENERATED AUTOMATICALLY. DO NOT EDIT DIRECTLY.
// ============================================================`;

/** @type {number} */
let docId = 0;

/**
 * @typedef {[string]} AnnotationAsParameter
 */
undefined;

/**
 * @typedef {string} AnnotationAsPropertyValue
 */
undefined;

/**
 * @typedef {{
 *   description: string | undefined; 
 *   deprecatedNotice: string | undefined;
 * }} DocComment
 */
undefined;

/**
 * @typedef {(clazz: string, name: string, docComment: DocComment, type: JavaTypeDescriptor) => string} JavaMethod
 */
undefined;

/**
 * @typedef {(name: string, docComment: DocComment, type: JavaTypeDescriptor) => string} JavaPropertyGetter
 */
undefined;


/**
 * @typedef {(clazz: string, name: string, docComment: DocComment, type: JavaTypeDescriptor) => string} JavaPropertySetter
 */
undefined;

/**
 * @typedef {{
 * type: string;
 * value: string;
 * getter: JavaPropertyGetter;
 * setter: JavaPropertySetter;
 * generics?: JavaTypeDescriptor[];
 * methods?: Record<string, JavaMethod>;
 * }} JavaTypeDescriptor
 */
undefined;

/**
 * @param {string} [data] 
 * @returns {string}
 */
export function Doc(data) {
  if (data) {
    return `@Doc${data}`;
  }
  else {
    return `@Doc${++docId}`;
  }
}

/**
 * @param {string} [data]
 * @returns {string}
 */
export function Deprecated(data) {
  if (data) {
    return `@Deprecated${data}`;
  }
  else {
    return `@Deprecated${++docId}`;
  }
}

/**
 * @param {string} unsafe 
 * @returns {string}
 */
function escapeHtml(unsafe) {
  return unsafe
    .replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;")
    .replace(/"/g, "&quot;")
    .replace(/'/g, "&#039;");
}

/**
 * @param {string} comment 
 * @param {number} maxLength 
 * @param {number} firstLineMaxLength 
 * @returns {string[]}
 */
function breakText(comment, maxLength, firstLineMaxLength) {
  const words = comment.split(/\s+/);
  const lines = [];
  let line = "";
  for (const word of words) {
    if (line.length + 1 + word.length <= (lines.length === 0 ? firstLineMaxLength : maxLength)) {
      if (line.length > 0) {
        line += " ";
      }
      line += word;
    }
    else {
      lines.push(line);
      line = word;
    }
  }
  if (line.length > 0) {
    lines.push(line);
  }
  return lines;
}

/**
 * @param {string} docString 
 * @returns {string}
 */
function createEnumDoc(docString) {
  if (docString) {
    return [
      `    /**`,
      `     * ${escapeHtml(breakText(docString, 120 - 7, 120 - 7).join("\n     * "))}`,
      `     */`,
    ].join("\n");
  }
  else {
    return "";
  }
}

/**
 * @param {string} docString 
 * @returns {string} 
 */
function createTypeDoc(docString) {
  if (docString) {
    /** @type {string[]} */
    const lines = [];
    lines.push(`/**`);
    lines.push(` * ${escapeHtml(breakText(docString, 120 - 3, 120 - 3).join("\n * "))}`);
    lines.push(` */`);
    return lines.join("\n");
  }
  else {
    return "";
  }
}

/**
 * @param {string} docString 
 * @param {string | undefined} deprecationNotice 
 * @returns {string} 
 */
function createGetterDoc(docString, deprecationNotice) {
  if (docString) {
    const lines = [];
    lines.push(`    /**`);
    if (deprecationNotice !== undefined) {
      lines.push(`     * @deprecated ${escapeHtml(breakText(deprecationNotice, 120 - 7, 120 - 7 - 12).join("\n     * "))}`);
    }
    lines.push(`     * @return ${escapeHtml(breakText(docString, 120 - 7, 120 - 7 - 8).join("\n     * "))}`);
    lines.push(`     */`);
    return lines.join("\n");
  }
  else {
    return "";
  }
}

/**
 * @param {string} name 
 * @param {string} docString 
 * @param {string | undefined} deprecationNotice 
 * @returns {string}
 */
function createSetterDoc(name, docString, deprecationNotice) {
  if (docString) {
    /** @type {string[]} */
    const lines = [];
    lines.push(`    /**`);
    if (deprecationNotice !== undefined) {
      lines.push(`     * @deprecated ${escapeHtml(breakText(deprecationNotice, 120 - 7, 120 - 7 - 12).join("\n     * "))}`);
    }
    lines.push(`     * @param ${fieldName(name)} ${escapeHtml(breakText(docString, 120 - 7, 120 - 7 - 8 - fieldName(name).length).join("\n     * "))}`);
    lines.push(`     * @return This same instance, useful for chaining multiple setter methods in one call.`);
    lines.push(`     */`);
    return lines.join("\n");
  }
  else {
    return "";
  }
}

/**
 * @param {string} docString 
 * @param {string | undefined} deprecationNotice 
 * @returns {string} 
 */
function createAddMapEntryDoc(docString, deprecationNotice) {
  if (docString) {
    const lines = [];
    lines.push(`    /**`);
    if (deprecationNotice !== undefined) {
      lines.push(`     * @deprecated ${escapeHtml(breakText(deprecationNotice, 120 - 7, 120 - 7 - 12).join("\n     * "))}`);
    }
    lines.push(`     * ${escapeHtml(breakText(docString, 120 - 7, 120 - 7).join("\n     * "))}`);
    lines.push(`     * @param key ${escapeHtml(breakText("The key of the map entry to add.", 120 - 7, 120 - 7 - 11).join("\n     * "))}`);
    lines.push(`     * @param value ${escapeHtml(breakText("The value to associate with the key.", 120 - 7, 120 - 7 - 13).join("\n     * "))}`);
    lines.push(`     * @return This same instance, useful for chaining multiple setter methods in one call.`);
    lines.push(`     */`);
    return lines.join("\n");
  }
  else {
    return "";
  }
}

/**
 * @returns {Promise<void>}
 */
export function cleanJavaDescriptors() {
  return new Promise((resolve, reject) => {
    rimraf(javaDescriptorPath, (/** @type {unknown} */ err) => {
      if (err) {
        reject(err);
        return;
      }
      mkdirp(javaDescriptorPath)
        .then(() => resolve())
        .catch((/** @type {unknown} */ err) => reject(err));
    });
  });
}

/**
 * @param {string} str 
 * @returns {string}
 */
function capitalize(str) {
  if (!str) return "";
  return str.charAt(0).toUpperCase() + str.substring(1);
}

/**
 * @param {string} str 
 * @returns {string}
 */
function lower(str) {
  if (!str) return "";
  return str.charAt(0).toLowerCase() + str.substring(1);
}

/**
 * @param {string} char 
 * @returns {boolean}
 */
function isUpperCase(char) {
  return char !== char.toLowerCase();
}

/**
 * @param {string} char 
 * @returns {boolean}
 */
function isIdentifier(char) {
  return char.match(/[a-zA-Z0-9]/) !== null;
}

/**
 * @param {string} char 
 * @returns {boolean}
 */
function isStartIdentifier(char) {
  return char.match(/[a-zA-Z]/) !== null;
}

/**
 * @param {string} str 
 * @returns {string}
 */
function enumCase(str) {
  const words = [];
  let word = [];
  for (let i = 0; i < str.length; ++i) {
    const char = str.charAt(i);
    const identifier = isIdentifier(char);
    if (isUpperCase(char) || !identifier) {
      if (word.length > 0) {
        words.push(word.join(""));
        word = [];
      }
    }
    if (identifier) word.push(char.toUpperCase());
  }
  if (word.length > 0) {
    words.push(word.join(""));
  }
  const converted = words.join("_");
  if (!isStartIdentifier(converted.charAt(0))) {
    return "_" + converted;
  }
  return converted;
}

/**
 * @param {string} name
 * @return {string}
 */
function escapeString(name) {
  return name.replace(/(["'\\])/g, "\\$1");
}

/**
 * @param {string} name
 * @return {string}
 */
function toJavaName(name) {
  return name.split(/[^a-zA-Z0-9_]/).map((x, i) => i > 0 ? capitalize(x) : x).join("");
}

/**
 * 
 * @param {boolean} asField 
 * @param {string} name 
 * @param {DocComment} docComment
 * @param {JavaTypeDescriptor} type 
 * @returns {string}
 */
function createSimpleGetter(asField, name, docComment, type) {
  const { description, deprecatedNotice } = docComment;
  /** @type {string[]} */
  const lines = [];
  if (asField) {
    lines.push(`    private ${type.value} ${fieldName(name)};`);
    lines.push("");
  }
  if (description) {
    lines.push(createGetterDoc(description, deprecatedNotice));
  }
  if (deprecatedNotice !== undefined) {
    lines.push("    @Deprecated");
  }
  if (asField) {
    lines.push(`    public ${type.value} ${getterName(name, type)}() {`);
    lines.push(`        return ${name};`);
    lines.push(`    }`);
  }
  else {
    lines.push(`    public ${type.value} ${getterName(name, type)}() {`);
    lines.push(`        return (${type.value}) (has("${escapeString(name)}") ? get("${escapeString(name)}") : null);`);
    lines.push(`    }`);
  }
  return lines.join("\n");
}

/**
 * @param {boolean} asField 
 * @param {string} className 
 * @param {string} name 
 * @param {DocComment} docComment
 * @param {JavaTypeDescriptor} type 
 * @param {(name: string) => string} [converter] 
 * @returns {string}
 */
function createSimpleSetter(asField, className, name, docComment, type, converter) {
  const { description, deprecatedNotice } = docComment;
  /** @type {string[]} */
  const lines = [];
  if (description) {
    lines.push(createSetterDoc(name, description, deprecatedNotice));
  }
  if (deprecatedNotice !== undefined) {
    lines.push("    @Deprecated");
  }
  if (asField) {
    lines.push(`    public ${className} ${setterName(name)}(final ${type.value} ${varName(name)}) {`);
    lines.push(`        this.${fieldName(name)} = ${converter ? converter(name) : varName(name)};`)
    lines.push(`        return this;`);
    lines.push(`    }`);
  }
  else {
    lines.push(`    public ${className} ${setterName(name)}(final ${type.value} ${varName(name)}) {`);
    lines.push(`        put("${escapeString(name)}", ${converter ? converter(name) : varName(name)});`);
    lines.push(`        return this;`);
    lines.push(`    }`);
  }
  return lines.join("\n");
}

/**
 * @param {boolean} asField 
 * @param {string} name 
 * @param {DocComment} docComment
 * @param {JavaTypeDescriptor} type 
 * @returns {string}
 */
function createEnumGetter(asField, name, docComment, type) {
  return createSimpleGetter(asField, name, docComment, T_String(asField));
}

/**
 * 
 * @param {boolean} asField 
 * @param {string} className 
 * @param {string} name 
 * @param {DocComment} docComment
 * @param {JavaTypeDescriptor} type 
 * @returns 
 */
function createEnumSetter(asField, className, name, docComment, type) {
  const lines = [];
  lines.push(createSimpleSetter(asField, className, name, docComment, type, val => `${varName(val)} != null ? ${varName(val)}.toString() : null`));
  lines.push("");
  lines.push(createSimpleSetter(asField, className, name, docComment, T_String(asField)));
  return lines.join("\n");
}

/**
 * @param {string} className 
 * @param {string} docString 
 * @returns {string}
 */
function createHead(className, docString) {
  return [
    HeaderComment,
    ``,
    `package ${javaDescriptorPackage};`,
    ``,
    `import org.primefaces.shaded.json.*;`,
    `import java.io.ObjectStreamException;`,
    `import java.io.Serializable;`,
    GeneratedComment,
    ``,
    createTypeDoc(docString),
    `@SuppressWarnings("serial")`,
    `public class ${className} extends JSONObject implements Serializable {`,
    `    private Object writeReplace() throws ObjectStreamException {`,
    `        return new Serialized${className}(this);`,
    `    }`,
    ``,
    `    private static class Serialized${className} implements Serializable {`,
    `        private String json;`,
    ``,
    `        public Serialized${className}(${className} ${lower(className)}) {`,
    `            this.json = ${lower(className)}.toString();`,
    `        }`,
    ``,
    `        private Object readResolve() throws ObjectStreamException {`,
    `            final ${className} ${lower(className)} = new ${className}();`,
    `            final JSONObject data = new JSONObject(json);`,
    `            for (final String key : data.keySet()) {`,
    `                final Object value = data.get(key);`,
    `                ${lower(className)}.put(key, value);`,
    `            }`,
    `            return ${lower(className)};`,
    `        }`,
    `    }`,
  ].filter(x => x !== undefined).join("\n");
}

/**
 * @returns {string}
 */
function createFooter() {
  return [
    `    /**`,
    `     * @return This options object as a serializable JSON object`,
    `     */`,
    `    JSONObject getJSONObject() {`,
    `        return this;`,
    `    }`,
    `}`,
  ].join("\n");
}

/**
 * @param {string} className 
 * @param {Record<string, AnnotationAsPropertyValue | JavaTypeDescriptor>} fields 
 * @param {string} classDoc 
 * @returns {string}
 */
function createClass(className, fields, classDoc) {
  const lines = [];
  lines.push(createHead(className, classDoc));
  let currentDoc = undefined;
  let currentDeprecation = undefined;
  for (const name of Object.keys(fields)) {
    const type = fields[name];
    if (typeof type === "string") {
      if (name.startsWith("@Doc")) {
        currentDoc = String(type);
      }
      else if (name.startsWith("@Deprecated")) {
        currentDeprecation = String(type);
      }
    }
    else {
      /** @type {DocComment} */
      const docComment = {
        deprecatedNotice: currentDeprecation,
        description: currentDoc,
      };
      lines.push("");
      lines.push(type.getter(name, docComment, type));
      lines.push("");
      lines.push(type.setter(className, name, docComment, type));
      if (type.methods) {
        for (const [_, method] of Object.entries(type.methods)) {
          lines.push("");
          lines.push(method(className, name, docComment, type));
        }
      }
      currentDeprecation = undefined;
      currentDoc = undefined;
    }
  }
  lines.push("");
  lines.push(createFooter());
  return lines.join("\n");
}

/**
 * @param {string} className 
 * @param {string} typeDoc 
 * @param  {(AnnotationAsParameter | string)[]} data 
 * @returns {string}
 */
function createEnum(className, typeDoc, ...data) {
  const lines = data.map(dataItem => {
    const item = Array.isArray(dataItem) ? dataItem[0] : dataItem;
    if (item.startsWith("@Deprecated")) {
      return "@Deprecated";
    }
    else if (item.startsWith("@Doc")) {
      return createEnumDoc(item.substring(4));
    }
    else {
      return `    ${enumCase(item)}("${item}"),\n`;
    }
  });
  return `${HeaderComment}

package ${javaDescriptorPackage};

${createTypeDoc(typeDoc?.substring(4))}
public enum ${className} {
${lines.join("\n")}    ;

    private final String toString;

    ${className}(final String toString) {
        this.toString = toString;
    }

    @Override
    public String toString() {
        return toString;
    }

    public static ${className} parseString(String name) {
        for (final ${className} enumConst : values()) {
            if (enumConst.toString.equals(name)) {
                return enumConst;
            }
        }
        throw new IllegalArgumentException("No such enum constant with name '" + name + "'");
    }
}
`.trim();
}

/**
 * @param {string} name 
 * @param {JavaTypeDescriptor} type
 * @returns {string}
 */
function getterName(name, type) {
  return `${type.value.toLowerCase() === "boolean" ? "is" : "get"}${capitalize(toJavaName(name))}`;
}

/**
 * @param {string} name 
 * @returns {string}
 */
function setterName(name) {
  return `set${capitalize(toJavaName(name))}`;
}

/**
 * @param {string} name 
 * @returns {string}
 */
function fieldName(name) {
  return lower(toJavaName(name));
}

/**
 * @param {string} name 
 * @returns {string}
 */
function varName(name) {
  return lower(toJavaName(name));
}

/**
 * @param {string} name 
 * @returns {string}
 */
function stripPlural(name) {
  if (name.endsWith("s")) {
    return name.substring(0, name.length - 1);
  }
  return name;
}

/**
 * @param {JavaTypeDescriptor} itemType 
 * @param {boolean} [asField] 
 * @returns {JavaTypeDescriptor}
 */
export function T_Array(itemType, asField = false) {
  return {
    type: "JSONArray",
    value: `JSONArray`,
    getter: createSimpleGetter.bind(null, asField),
    setter: createSimpleSetter.bind(null, asField),
    generics: [itemType],
    methods: {
      add(clazz, name, docComment, type) {
        const { description, deprecatedNotice } = docComment;
        /** @type {string[]} */
        const lines = [];
        if (description) {
          lines.push(createGetterDoc(description, deprecatedNotice));
        }
        if (deprecatedNotice !== undefined) {
          lines.push("    @Deprecated");
        }
        lines.push(`    public ${clazz} add${stripPlural(capitalize(name))}(final ${type?.generics?.[0].value} ...items) {`);
        lines.push(`        ${type.value} x = ${getterName(name, type)}();`);
        lines.push(`        if (x == null) {`);
        lines.push(`            x = new JSONArray();`);
        lines.push(`            ${setterName(name)}(x);`);
        lines.push(`        }`);
        lines.push(`        for (${type?.generics?.[0].value} item : items) x.put(item);`);
        lines.push(`        return this;`);
        lines.push(`    }`);
        return lines.join("\n");
      },
      set(clazz, name, docComment, type) {
        const { description, deprecatedNotice } = docComment;
        /** @type {string[]} */
        const lines = [];
        if (description) {
          lines.push(createSetterDoc(name, description, deprecatedNotice));
        }
        if (deprecatedNotice !== undefined) {
          lines.push("    @Deprecated");
        }
        lines.push(`    public ${clazz} set${capitalize(name)}(final java.util.List<${type?.generics?.[0].value}> ${name}) {`);
        lines.push(`        return set${capitalize(name)}(new JSONArray(${name}));`);
        lines.push(`    }`);
        return lines.join("\n");
      },
    },
  };
}

/**
 * @param {JavaTypeDescriptor} keyType 
 * @param {JavaTypeDescriptor} valueType 
 * @param {boolean} [asField] 
 * @returns {JavaTypeDescriptor}
 */
export function T_Map(keyType, valueType, asField = false) {
  return {
    type: "JSONObject",
    value: `JSONObject`,
    getter: createSimpleGetter.bind(null, asField),
    setter: createSimpleSetter.bind(null, asField),
    generics: [keyType, valueType],
    methods: {
      add(clazz, name, docComment, type) {
        const { description, deprecatedNotice } = docComment;
        /** @type {string[]} */
        const lines = [];
        if (description) {
          lines.push(createAddMapEntryDoc(description, deprecatedNotice));
        }
        if (deprecatedNotice !== undefined) {
          lines.push("    @Deprecated");
        }
        lines.push(`    public ${clazz} add${stripPlural(capitalize(name))}(final ${type?.generics?.[0].value} key, final ${type?.generics?.[1].value} value) {`);
        lines.push(`        ${type.value} x = ${getterName(name, type)}();`);
        lines.push(`        if (x == null) {`);
        lines.push(`            x = new JSONObject();`);
        lines.push(`            ${setterName(name)}(x);`);
        lines.push(`        }`);
        lines.push(`        x.put(key, value);`);
        lines.push(`        return this;`);
        lines.push(`    }`);
        return lines.join("\n");
      },
      set(clazz, name, docComment, type) {
        const { description, deprecatedNotice } = docComment;
        /** @type {string[]} */
        const lines = [];
        if (description) {
          lines.push(createSetterDoc(name, description, deprecatedNotice));
        }
        if (deprecatedNotice !== undefined) {
          lines.push("    @Deprecated");
        }
        lines.push(`    public ${clazz} set${capitalize(name)}(final java.util.Map<${type?.generics?.[0].value}, ${type?.generics?.[1].value}> ${name}) {`);
        lines.push(`        return set${capitalize(name)}(new JSONObject(${name}));`);
        lines.push(`    }`);
        return lines.join("\n");
      }
    },
  };
}

/**
 * @param {string} className 
 * @param {string} typeDoc
 * @param {boolean} asField
 * @param {(string | AnnotationAsParameter)[]} constants
 * @returns {JavaTypeDescriptor} 
 */
export function T_Enum(className, typeDoc, asField, ...constants) {
  const code = createEnum(className, typeDoc, ...constants);
  const file = join(javaDescriptorPath, className + ".java");
  fs.writeFile(file, code, { encoding: "latin1" }).then(() => console.log("Wrote", file));
  return {
    type: "String",
    value: className,
    getter: createEnumGetter.bind(null, asField),
    setter: createEnumSetter.bind(null, asField),
  };
}

/**
 * @param {string} clazz 
 * @param {Record<string, AnnotationAsPropertyValue | JavaTypeDescriptor>} fields 
 * @param {string} classDoc 
 * @param {boolean} [asField] 
 * @returns {JavaTypeDescriptor} 
 */
export function T_Class(clazz, fields, classDoc, asField = false) {
  const code = createClass(clazz, fields, classDoc);
  const file = join(javaDescriptorPath, clazz + ".java");
  fs.writeFile(file, code, { encoding: "latin1" }).then(() => console.log("Wrote", file));
  return {
    type: "class",
    value: clazz,
    getter: createSimpleGetter.bind(null, asField),
    setter: createSimpleSetter.bind(null, asField),
  };
}

/**
 * @param {boolean} [asField] 
 * @returns {JavaTypeDescriptor}
 */
export function T_Boolean(asField = false) {
  return {
    type: "class",
    value: "Boolean",
    getter: createSimpleGetter.bind(null, asField),
    setter: createSimpleSetter.bind(null, asField),
  };
};

/**
 * @param {boolean} [asField] 
 * @returns {JavaTypeDescriptor}
 */
export function T_String(asField = false) {
  return {
    type: "class",
    value: "String",
    getter: createSimpleGetter.bind(null, asField),
    setter: createSimpleSetter.bind(null, asField),
  };
}

/**
 * @param {boolean} [asField] 
 * @returns {JavaTypeDescriptor}
 */
export function T_Number(asField = false) {
  return {
    type: "class",
    value: "Number",
    getter: createSimpleGetter.bind(null, asField),
    setter: createSimpleSetter.bind(null, asField),
  };
}

/**
 * 
 * @param {boolean} [asField] 
 * @returns {JavaTypeDescriptor}
 */
export function T_CssSize(asField = false) {
  /** @type {DocComment} */
  const docComment = {
    deprecatedNotice: undefined,
    description: undefined,
  };
  return {
    type: "Union<Number, String>",
    value: "Union<Number, String>",
    getter: (name, type) => {
      return createSimpleGetter(asField, name, docComment, T_String(asField));
    },
    setter: (clazz, name, type) => {
      const setterString = createSimpleSetter(asField, clazz, name, docComment, T_String(asField));
      const setterNumber = createSimpleSetter(asField, clazz, name, docComment, T_Number(asField), value => `${varName(value)} != null ? ${varName(value)}.toString() + "px" : null`);
      return setterNumber + "\n\n" + setterString;
    },
  };
};
