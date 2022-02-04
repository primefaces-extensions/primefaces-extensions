// @ts-check

// Web worker that first loads the Locale, then loads the actual HTML/CSS/TYPESCRIPT/JSON worker.

(function (workerScope) {
  /**
   * Splits the given string at the first occurrence of the given separator. Returns the substring before and after
   * the separator.
   * @param {string} str String to split.
   * @param {string} separator Separator at which to split.
   * @return {[string, string | undefined]} The string before the separator and the string after the separator. When
   * the string does not contain the separator, the first element is the entire string and the second element is
   * `undefined`.
   */
  function splitFirst(str, separator) {
    const index = str.indexOf(separator);
    return index !== -1 ? [str.slice(0, index), str.slice(index + 1)] : [str, undefined];
  }

  /**
   * @param {string} str 
   * @return {Record<string, string | undefined>}
   */
  function parseQuery(str) {
    str = str.trim().replace(/^[?#&]/, '');
    if (str.length === 0) {
      return {};
    }
    /** @type {Record<string, string | undefined>} */
    const ret = {};
    for (const param of str.split('&')) {
      const [key, val] = splitFirst(param.replace(/\+/g, ' '), "=");
      ret[key] = val !== undefined ? decodeURIComponent(val) : val;
    };
    return ret;
  }

  /**
   * @return {Record<string, string | undefined>}
   */
  function parseLocation() {
    const [x, y] = splitFirst(workerScope.location.href, "?");
    return parseQuery(y !== undefined ? y : x);
  }

  var query = parseLocation();
  var locale = query.locale;
  var worker = query.worker;

  if (!worker) {
    throw new Error("[MonacoEditor] No worker URL was given.");
  }

  if (locale) {
    try {
      workerScope.importScripts(locale);
    }
    catch (e) {
      console.error(`[MonacoEditor] Could not fetch localization file ${locale}. Falling back to default English locale.`);
    }
  }
  workerScope.importScripts(worker);
})(self);