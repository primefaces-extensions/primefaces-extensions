if (!PrimeFacesExt.timers) {

    /**
     * Customized timer functions using web-workers which do not get throttled by browsers when inactive.
     * All the following conditions need to be true in order for the 1-minute throttling to happen :
     *
     * Page has been hidden for more than 5 minutes.
     * Chain count while applying setTimeout() / setInterval() methods is greater than 5.
     * Page has not made any sound using any of the sound-making APIs.
     * WebRTC is not in use.
     *
     * @namespace
     */
    PrimeFacesExt.timers = {
        createWorker: function () {
            var containerFunction = function () {
                var idMap = {};

                self.onmessage = function (e) {
                    if (e.data.type === 'setInterval') {
                        idMap[e.data.id] = setInterval(function () {
                            self.postMessage({
                                type: 'fire',
                                id: e.data.id
                            });
                        }, e.data.delay);
                    } else if (e.data.type === 'clearInterval') {
                        clearInterval(idMap[e.data.id]);
                        delete idMap[e.data.id];
                    } else if (e.data.type === 'setTimeout') {
                        idMap[e.data.id] = setTimeout(function () {
                            self.postMessage({
                                type: 'fire',
                                id: e.data.id
                            });
                            // remove reference to this timeout after is finished
                            delete idMap[e.data.id];
                        }, e.data.delay);
                    } else if (e.data.type === 'clearCallback') {
                        clearTimeout(idMap[e.data.id]);
                        delete idMap[e.data.id];
                    }
                };
            };

            return new Worker(
                URL.createObjectURL(
                    new Blob(
                        [
                            '(',
                            containerFunction.toString(),
                            ')();'
                        ],
                        {type: 'application/javascript'}
                    )
                )
            );
        },

        generateId: function () {
            return this.currentId++;
        },

        setInterval: function (callback, delay) {
            var intervalId = this.generateId();

            this.idToCallback[intervalId] = callback;
            this.worker.postMessage({
                type: 'setInterval',
                delay: delay,
                id: intervalId
            });

            return intervalId;
        },

        clearInterval: function (intervalId) {
            this.worker.postMessage({
                type: 'clearInterval',
                id: intervalId
            });

            delete this.idToCallback[intervalId];
        },

        setTimeout: function (callback, delay) {
            var intervalId = this.generateId();
            var $this = this;

            this.idToCallback[intervalId] = function () {
                callback();
                delete $this.idToCallback[intervalId];
            };

            this.worker.postMessage({
                type: 'setTimeout',
                delay: delay,
                id: intervalId
            });

            return intervalId;
        },

        clearTimeout: function (intervalId) {
            this.worker.postMessage({
                type: 'clearInterval',
                id: intervalId
            });

            delete this.idToCallback[intervalId];
        },

        init: function () {
            this.worker = this.createWorker();
            this.idToCallback = {};
            this.currentId = 1;

            this.worker.onmessage = function (e) {
                if (e.data.type === 'fire' && PrimeFacesExt.timers.idToCallback[e.data.id] != null) {
                    PrimeFacesExt.timers.idToCallback[e.data.id]();
                }
            };
        }
    };
    PrimeFacesExt.timers.init();
};