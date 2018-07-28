(function () {
    'use strict';

    var util = require('util'),
        stream = require('stream');

    var ReadableBufferStream = function (buffer, options) {
        if (!this instanceof ReadableBufferStream) return new ReadableBufferStream(buffer, options);
        stream.Readable.call(this, options);
        this._source = buffer;
    };

    util.inherits(ReadableBufferStream, stream.Readable);

    ReadableBufferStream.prototype._read = function () {
        this.push(this._source);
        this.push(null);
    };

    var WritableBufferStream = function (options) {
        if (!this instanceof WritableBufferStream) return new WritableBufferStream(options);
        stream.Writable.call(this, options);
        this._buffer = new Buffer(0);
    };

    util.inherits(WritableBufferStream, stream.Writable);

    WritableBufferStream.prototype._write = function (chunk, enc, next) {
        this._buffer = Buffer.concat([this._buffer, Buffer.isBuffer(chunk) ? chunk : new Buffer(chunk, enc)]);
        next();
    };

    WritableBufferStream.prototype.writeInt = function (value) {
        return this.write(new Buffer(4).writeInt32BE(value, 0));
    };

    WritableBufferStream.prototype.writeShort = function (value) {
        return this.write(new Buffer(2).writeInt16BE(value, 0));
    };

    WritableBufferStream.prototype.writeByte = function (value) {
        return this.write(new Buffer(1).writeInt8(value, 0));
    };

    WritableBufferStream.prototype.getBuffer = function () {
        return this._buffer;
    };
    WritableBufferStream.prototype.resetBuffer = function () {
        var buffer = this._buffer;
        this._buffer = new Buffer(0);
        return buffer;
    };

    exports.ReadableBufferStream = ReadableBufferStream;
    exports.WritableBufferStream = WritableBufferStream;
}());