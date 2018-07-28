import isPromise from './is-promise';

export default function promisify(value) {
    if (isPromise(value)) return value;
    else return Promise.resolve(value);
}