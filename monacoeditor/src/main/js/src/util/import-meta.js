// @ts-check

import { dirname } from "node:path"
import { fileURLToPath } from 'node:url';

/**
 * @param {ImportMeta} meta 
 * @return {string}
 */
export function getDirname(meta) {
    const filePath = fileURLToPath(meta.url);
    return dirname(filePath);
}