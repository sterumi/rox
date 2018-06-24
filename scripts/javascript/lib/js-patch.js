module.exports = function ($) {
    if (typeof String.prototype.trim === 'undefined') {
        String.prototype.trim = function () {
            return this.replace(/^\s+|\s+$/g, '');
        };
    }
    if (typeof Function.prototype.bind === 'undefined') {
        Function.prototype.bind = (function (slice) {
            function bind(context) {
                var self = this;
                if (1 < arguments.length) {
                    var $arguments = slice.call(arguments, 1);
                    return function () {
                        return self.apply(context, arguments.length ? $arguments.concat(slice.call(arguments)) : $arguments);
                    };
                }
                // optimized callback
                return function () {
                    return arguments.length
                        ? self.apply(context, arguments)
                        : self.call(context);
                };
            }

            return bind;
        })(Array.prototype.slice);
    }
    require('task-bukkit')($);

    return function unitTest(console) {
        $.setTimeout(function () {
            console.log('js-patch setTimeout() test complete');
        }, 100);
        var clearMe = $.setTimeout(function () {
            console.error('js-patch clearTimeout() test failed');
        }, 100);
        $.clearTimeout(clearMe);

        var runs = 3;
        var clearAfterRuns = $.setInterval(function () {
            runs--;
            if (runs == 0) {
                $.clearInterval(clearAfterRuns);
            }
            if (runs < 0) {
                console.error('js-patch clearInterval test failed.');
            }
        }, 100);
    };
};