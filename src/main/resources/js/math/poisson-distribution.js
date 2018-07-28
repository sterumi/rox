function poisson(expect) {
    var n = 0, x = Math.random();
    while (x > Math.exp(-expect)) { n++; x *= Math.random(); }
    return n;
}