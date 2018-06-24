function argsToArray(args) {
    var result = [];
    for (var i = 0; i < args.length; i++) {
        result.push(args[i]);
    }
    return result;
}

function consMsg(params) {
    var args = argsToArray(params);
    if (args.length > 1) {
        return java.lang.String.format(args[0], args.slice(1));
    } else {
        return args[0];
    }
}

module.exports = function (logger) {
    function bukkitLog(level, restOfArgs) {
        logger['log(java.util.logging.Level,java.lang.String)'](
            java.util.logging.Level[level],
            consMsg(restOfArgs)
        );
    }

    return {
        log: function () {
            bukkitLog('INFO', arguments);
        },
        info: function () {
            bukkitLog('INFO', arguments);
        },
        warn: function () {
            bukkitLog('WARNING', arguments);
        },
        error: function () {
            bukkitLog('SEVERE', arguments);
        }
    };
};