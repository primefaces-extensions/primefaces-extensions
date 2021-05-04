// @ts-check

window.monacoModule = window.monacoModule || {};

window.monacoModule.PromiseQueue = (function () {

  const { isNotNullOrUndefined } = window.monacoModule.helper;

  /**
   * A simple queue to which promise factories can be added. It makes sure the promises
   * are created / started in the order as they were added.
   * @template T
   * @implements IPromiseQueue<T>
   */
  class PromiseQueueImpl {
    constructor() {
      /** @type {QueueItem[]} */
      this._queue = [];
    }

    /**
     * @param {PromiseFactory<T>} promiseFactory
     * @return {Promise<T>}
     */
    add(promiseFactory) {
      return this.addAll(promiseFactory)[0];
    }

    /**
     * @param {PromiseFactory<T>[]} promiseFactory
     * @return {Promise<T>[]}
     */
    addAll(...promiseFactory) {
      return promiseFactory
        .filter(isNotNullOrUndefined)
        .map(factory => new Promise((resolve, reject) => {
          this._addQueueItem(factory, resolve, reject);
        }));
    }

    /**
     *
     * @param {PromiseFactory<unknown>} factory
     * @param {PromiseCallback} resolve
     * @param {PromiseCallback} reject
     */
    _addQueueItem(factory, resolve, reject) {
      const wasEmpty = this._queue.length === 0;
      this._queue.push({
        factory: factory,
        resolve: resolve,
        reject: reject,
      });
      if (wasEmpty) {
        this._startQueue();
      }
    }

    _startQueue() {
      this._processQueue(this._peek());
    }

    _onPromiseDone() {
      this._poll();
      this._processQueue(this._peek());
    }

    /**
     * @param {QueueItem} queueItem
     */
    _processQueue(queueItem) {
      if (queueItem) {
        const promise = PromiseQueueImpl._makePromise(queueItem.factory);
        promise
          .then(queueItem.resolve)
          .catch(queueItem.reject)
          .then(() => this._onPromiseDone());
      }
    }

    /**
     * @return {QueueItem}
     */
    _poll() {
      return this._queue.shift();
    }

    /**
     * @return {QueueItem}
     */
    _peek() {
      return this._queue[0];
    }

    /**
     * @param {PromiseFactory<unknown>} promiseFactory
     * @return {Promise<unknown>}
     */
    static _makePromise(promiseFactory) {
      try {
        const promise = promiseFactory();
        if (promise !== undefined) {
          return promise;
        }
      }
      catch (e) {
        console.error("[MonacoEditor] Could not create promise", e);
      }
      return Promise.resolve();
    }
  }

  return PromiseQueueImpl;
})();