import isNode from 'is-node';
export default function now() {
    let perform;
    if (isNode) {
        const {performance} = require('perf_hooks');
        perform = performance;
    } else perform = window.performance;
    return perform.now();
}