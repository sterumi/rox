export default function isPromise(value) {
    try {
        if (typeof value.then === 'function') return true;
    } catch (err) {err.print()}
    return false;
}