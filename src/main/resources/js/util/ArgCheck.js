(function () {
    'use strict';

    var ArgCheck = {};

    ArgCheck.notEqual = function (param, value, paramName) {
        if (param === value) throw new Error("IllegalArgument: " + paramName + " == " + value);
    };

    ArgCheck.notNull = function (param, paramName) {
        if (null === param || undefined === param) throw new Error("IllegalArgument: " + paramName + " is null");
    };

    ArgCheck.isNull = function (param, paramName) {
        if (null !== param && undefined !== param) throw new Error("IllegalArgument: " + paramName + " is not null");
    };

    ArgCheck.notNullOrEmpty = function (param, paramName) {
        if (!param) throw new Error("IllegalArgument: " + paramName + " is empty");
    };

    ArgCheck.greaterThanOrEqualToZero = function (param, paramName) {
        if (0 > param) throw new Error("IllegalArgument: " + paramName + " < 0");
    };

    ArgCheck.greaterThanZero = function (param, paramName) {
        if (0 >= param) throw new Error("IllegalArgument: " + paramName + " < 1");
    };

    ArgCheck.notZero = function (param, paramName) {
        if (0 === param) throw new Error("IllegalArgument: " + paramName + " == 0");
    };

    ArgCheck.isValidState = function (isValid, errMsg) {
        if (!isValid) throw new Error("IllegalState: " + errMsg);
    };

    ArgCheck.isValidType = function (param, type) {
        if (!(param instanceof type)) throw new Error("IllegalType: " + param + " is not instance of " + type);
    };

    exports.ArgCheck = ArgCheck;
}());