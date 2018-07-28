import wait from './wait';
import promisify from './promisify';

export default function runForever(predicate, interval = 100) {
    let t = 1;
    return new Promise(() => {const runLoop = () => {
            t++;
            const val = promisify(predicate());
            val.then(() => wait(interval)).then(() => runLoop());
        };
        runLoop();
    });
}