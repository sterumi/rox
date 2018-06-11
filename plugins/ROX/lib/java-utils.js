exports.isJavaObject = function (o) {
    if (o === global) {
        return false;
    }
    if (o !== undefined && o !== null) {
        try {
            if (typeof o.constructor === 'function') {
                return false;
            }
        } catch (e) {
            return true;
        }
        try {
            var result = !!o.getClass;
            if (result === true) {
                return result;
            }
        } catch (e2) {
        }

        if (o instanceof java.lang.Object) {
            return true;
        }
    }
    return o instanceof java.lang.Object;
};