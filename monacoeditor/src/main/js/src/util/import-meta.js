// @ts-check

import { dirname } from "node:path"

/**
 * @param {ImportMeta} meta 
 * @return {string}
 */
export function getDirname(meta) {
    const url = new URL(meta.url);
    return dirname(url.pathname);
}