export default function wait(ms = 0) {
    return new Promise(res => setTimeout(res, ms));
}