import promisify from './promisify';
export default function resolveValues(obj) {
    const ret = {};
    return Promise.all(Object.keys(obj).map((k) => {const val = promisify(obj[k]);return val.then(v => ret[k] = v);})).then(() => ret);
}