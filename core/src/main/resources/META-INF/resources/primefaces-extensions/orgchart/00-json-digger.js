/*
 * JSONDigger
 * https://github.com/dabeng/json-digger
 *
 * Licensed under the MIT license:
 * http://www.opensource.org/licenses/MIT
 */
'use strict';

(function (global, factory) {
  typeof exports === 'object' && typeof module !== 'undefined' ? module.exports = factory() :
  typeof define === 'function' && define.amd ? define(factory) :
  (global = typeof globalThis !== 'undefined' ? globalThis : global || self, global.JSONDigger = factory());
}(this, (function () { 'use strict';
  function JSONDigger(datasource, idProp, childrenProp) {
    this.datasource = datasource;
    this.idProp = idProp;
    this.childrenProp = childrenProp;
  }
  JSONDigger.prototype.findNodeById = function (id) {
    var that = this;
    var traverse = function (nodes) {
      for (var i = 0; i < nodes.length; i++) {
        var node = nodes[i];
        if (node[that.idProp] == id) {
          return node;
        }
        if (node[that.childrenProp] && node[that.childrenProp].length) {
          var found = traverse(node[that.childrenProp]);
          if (found) {
            return found;
          }
        }
      }
      return null;
    };
    return traverse(Array.isArray(this.datasource) ? this.datasource : [this.datasource]);
  };
  JSONDigger.prototype.findOneNode = function (criteria) {
    var that = this;
    var keys = Object.keys(criteria);
    var traverse = function (nodes) {
      for (var i = 0; i < nodes.length; i++) {
        var node = nodes[i];
        var match = keys.every(function (key) {
          return node[key] === criteria[key];
        });
        if (match) {
          return node;
        }
        if (node[that.childrenProp] && node[that.childrenProp].length) {
          var found = traverse(node[that.childrenProp]);
          if (found) {
            return found;
          }
        }
      }
      return null;
    };
    return traverse(Array.isArray(this.datasource) ? this.datasource : [this.datasource]);
  };
  JSONDigger.prototype.removeNode = function (id) {
    var that = this;
    var traverse = function (nodes) {
      for (var i = 0; i < nodes.length; i++) {
        if (nodes[i][that.idProp] == id) {
          nodes.splice(i, 1);
          return true;
        }
        if (nodes[i][that.childrenProp] && nodes[i][that.childrenProp].length) {
          if (traverse(nodes[i][that.childrenProp])) {
            return true;
          }
        }
      }
      return false;
    };
    return traverse(Array.isArray(this.datasource) ? this.datasource : [this.datasource]);
  };
  return JSONDigger;
})));
