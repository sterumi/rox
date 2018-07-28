(function () {
    'use strict';

    var dateformat = require('dateformat'), DATE_FORMAT_ISO8601_FOR_OUTPUT = "yyyy-mm-dd'T'HH:MM:ss'Z'", DATE_FORMAT_RFC1123 = "ddd, dd mmm yyyy HH:MM:ss 'GMT'", GeneralUtil = {};

    function _mixin(to, from, fnName) {
        to[fnName] = function () {return from[fnName].apply(from, arguments);};
    }

    GeneralUtil.urlConcat = function (url, suffix) {
        var left = url;
        if (url.lastIndexOf("/") === (url.length - 1)) left = url.slice(0, url.length - 1);
        if (suffix.indexOf("/") === 0) return left + suffix;

        return left + "/" + suffix;
    };

    GeneralUtil.toJson = function (o) {
        return JSON.stringify(o);
    };

    GeneralUtil.mixin = function (to, from) {
        var index, protos = [], proto = from;

        while (!!proto) {
            protos.push(Object.getOwnPropertyNames(proto));
            proto = Object.getPrototypeOf(proto);
        }

        for (index = protos.length - 2; index >= 0; index--) protos[index].forEach(function (method) {
                if (!to[method] && typeof from[method] === 'function' && method !== 'constructor') _mixin(to, from, method);});
    };

    GeneralUtil.guid = function () {
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {var r = (Math.random() * 16) | 0, v = (c === 'x') ? r : ((r & 0x3) | 0x8);return v.toString(16);});
    };

    GeneralUtil.clone = function (obj) {
        var copy;
        if (null == obj || "object" !== typeof obj) return obj;

        if (obj instanceof Date) {
            copy = new Date();
            copy.setTime(obj.getTime());
            return copy;
        }

        if (obj instanceof Array) {
            copy = [];
            for (var i = 0, len = obj.length; i < len; i++) copy[i] = GeneralUtil.clone(obj[i]);
            return copy;
        }

        if (obj instanceof Object) {
            copy = obj.constructor();
            for (var attr in obj) copy[attr] = GeneralUtil.clone(obj[attr]);
            return copy;
        }

        throw new Error("Unable to copy object.");
    };

    GeneralUtil.objectAssign = function (target, source) {
        if (typeof Object.assign === 'function') return Object.assign.apply(Object, [target].concat(Array.prototype.slice.call(arguments, 1)));
        if (target === undefined || target === null) throw new TypeError('Cannot convert first argument to object');


        var to = Object(target);
        for (var i = 1; i < arguments.length; i++) {
            if (arguments[i] === undefined || arguments[i] === null) continue;

            arguments[i] = Object(arguments[i]);

            var keysArray = Object.keys(Object(arguments[i]));
            for (var nextIndex = 0, len = keysArray.length; nextIndex < len; nextIndex++)
                if (Object.getOwnPropertyDescriptor(arguments[i], keysArray[nextIndex]) !== undefined &&
                    Object.getOwnPropertyDescriptor(arguments[i], keysArray[nextIndex]).enumerable) to[keysArray[nextIndex]] = arguments[i][keysArray[nextIndex]];
        }
        return to;
    };

    GeneralUtil.definePropertyWithDefaultConfig = function (obj, name, getFunc, setFunc) {
        Object.defineProperty(obj, name, {enumerable: true, configurable: true, get: getFunc, set: setFunc});
    };

    GeneralUtil.defineStandardProperty = function (obj, name) {
        var getFunc = function () {
            return this["_" + name];
        }, setFunc = function (v) {
            this["_" + name] = v;
        };
        GeneralUtil.definePropertyWithDefaultConfig(obj, name, getFunc, setFunc);
    };

    GeneralUtil.sleep = function (ms, promiseFactory) {
        return promiseFactory.makePromise(function (resolve) {
            setTimeout(function () {
                resolve();
            }, ms);
        });
    };

    GeneralUtil.getRfc1123Date = function (date) {
        return GeneralUtil.toRfc1123DateTime(date);
    };

    GeneralUtil.toRfc1123DateTime = function (date) {
        date = date || new Date();
        return dateformat(date, DATE_FORMAT_RFC1123, true);
    };

    GeneralUtil.toISO8601DateTime = function (date) {
        date = date || new Date();
        return dateformat(date, DATE_FORMAT_ISO8601_FOR_OUTPUT, true);
    };

    exports.GeneralUtil = GeneralUtil;
}());