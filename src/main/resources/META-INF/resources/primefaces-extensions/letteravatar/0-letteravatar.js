(function (w, d) {

    function LetterAvatar(name, size) {

        name = name || '';
        size = size || 60;

        var nameSplit = String(name).toUpperCase().split(' '),
                initials, canvas, context, dataURI;


        initials = name.match(/\b(\w)/g).join('').toUpperCase();
        if (nameSplit.length == 1) {
            initials = nameSplit[0] ? initials.charAt(0) : '?';
        } else {
            initials = initials.charAt(0) + initials.substr(initials.length - 1);
        }

        if (w.devicePixelRatio) {
            size = (size * w.devicePixelRatio);
        }

        canvas = d.createElement('canvas');
        canvas.width = size;
        canvas.height = size;
        context = canvas.getContext("2d");

        context.fillStyle = 'hsl(' + LetterAvatar.hue(String(name)) + ', 100%, 50%)';
        context.fillRect(0, 0, canvas.width, canvas.height);
        context.font = Math.round(canvas.width / 2) + "px Arial";
        context.textAlign = "center";
        context.fillStyle = "#FFF";
        context.fillText(initials, size / 2, size / 1.5);

        dataURI = canvas.toDataURL();
        canvas = null;

        return dataURI;
    }

    LetterAvatar.transform = function (id) {
        Array.prototype.forEach.call(d.querySelectorAll(id), function (img, name) {
            name = img.getAttribute('avatar');
            img.src = LetterAvatar(name, img.getAttribute('width'));
            img.removeAttribute('avatar');
            img.setAttribute('alt', name);
            img.setAttribute('title', name);
        });
    };

    LetterAvatar.hash = function (str) {
        for (i = 0, result = 0; i < str.length; i++) {
            result = ((result << 5) - result) + str.charCodeAt(i);
            result |= 0;
        }
        return result;
    };

    LetterAvatar.hue = function (str) {
        return Math.abs(LetterAvatar.hash(str) % 360);
    };

    // AMD support
    if (typeof define === 'function' && define.amd) {

        define(function () {
            return LetterAvatar;
        });

        // CommonJS and Node.js module support.
    } else if (typeof exports !== 'undefined') {

        // Support Node.js specific `module.exports` (which can be a function)
        if (typeof module != 'undefined' && module.exports) {
            exports = module.exports = LetterAvatar;
        }

        // But always support CommonJS module 1.1.1 spec (`exports` cannot be a function)
        exports.LetterAvatar = LetterAvatar;

    } else {

        window.LetterAvatar = LetterAvatar;
    }

})(window, document);