export default function waitResolvable(ms = 0) {
    const ret = {};
    ret.promise = new Promise(res => {ret.resolve = (x) => res(x);setTimeout(res, ms);});
    return ret;
}