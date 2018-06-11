'use strict';
var global = this;
var server;
global.nashorn = typeof Java !== 'undefined';

var __onDisableImpl;

function __onDisable(__engine, __plugin) {
    __onDisableImpl(__engine, __plugin);
}

function __onEnable(__engine, __plugin, __script) {
    function _echo() {
        var sender, msg;
        if (arguments.length === 2) {
            sender = arguments[0];
            msg = arguments[1];
        } else {
            if (typeof self === 'undefined') {
                return;
            }
            sender = self;
            msg = arguments[0];
        }
        sender.sendMessage(msg);
    }

    function _canonize(file) {
        return '' + file.getCanonicalPath().replaceAll('\\\\', '/');
    }

    function _save(objToSave, filename) {
        var objectToStr = null,
            f,
            out;
        try {
            objectToStr = JSON.stringify(objToSave, null, 2);
        } catch (e) {
            console.error('ERROR: ' + e.getMessage() + ' while saving ' + filename);
            return;
        }
        f = filename instanceof File ? filename : new File(filename);
        out = new PrintWriter(new FileWriter(f));
        out.println(objectToStr);
        out.close();
    }

    function _loadJSON(filename) {
        var result = null,
            file = filename,
            r,
            reader,
            br,
            contents;

        if (!(filename instanceof File)) {
            file = new File(filename);
        }
        var canonizedFilename = _canonize(file);

        if (file.exists()) {
            reader = new FileReader(file);
            br = new BufferedReader(reader);
            contents = '';
            try {
                while ((r = br.readLine()) !== null) {
                    contents += r + '\n';
                }
                result = JSON.parse(contents);
            } catch (e) {
                logError('Error evaluating ' + canonizedFilename + ', ' + e);
            } finally {
                try {
                    reader.close();
                } catch (re) {
                    throw re;
                }
            }
        }
        return result;
    }

    function _load(filename, warnOnFileNotFound) {
        var result = null,
            file = filename,
            r,
            reader,
            br,
            code,
            wrappedCode;

        if (!(filename instanceof File)) {
            file = new File(filename);
        }
        var canonizedFilename = _canonize(file);

        if (file.exists()) {
            reader = new FileReader(file);
            br = new BufferedReader(reader);
            code = '';
            try {
                while ((r = br.readLine()) !== null) {
                    code += r + '\n';
                }
                wrappedCode = '(' + code + ')';
                result = __engine.eval(wrappedCode);
            } catch (e) {
                logError('Error evaluating ' + canonizedFilename + ', ' + e);
            } finally {
                try {
                    reader.close();
                } catch (re) {
                }
            }
        } else {
            if (warnOnFileNotFound) {
                logWarn(canonizedFilename + ' not found');
            }
        }
        return result;
    }

    function _isOp(sender) {
        return sender.op;
    }

    function _refresh(skipOpCheck) {
        if (!skipOpCheck && typeof self !== 'undefined') {
            if (!_isOp(self)) return echo(self, 'Only operators can refresh()');
        }

        __plugin.pluginLoader.disablePlugin(__plugin);
        server.scheduler.cancelTasks(__plugin);
        __plugin.pluginLoader.enablePlugin(__plugin);
    }

    function _onDisable(/* evt */) {
        _save(global.config, new File(jsPluginsRootDir, 'data/global-config.json'));
        _runUnloadHandlers();
    }

    function _addUnloadHandler(f) {
        unloadHandlers.push(f);
    }

    function _runUnloadHandlers() {
        for (var i = 0; i < unloadHandlers.length; i++) {
            unloadHandlers[i]();
        }
    }

    function engineEval(code) {
        return __engine.eval(code);
    }

    var moduleHooks = [engineEval];
    var replHooks = [engineEval];

    global._moduleHooks = moduleHooks;
    global._replHooks = replHooks;

    function __onCommand() {
        var jsArgs = [],
            i = 0,
            jsResult,
            result,
            cmdName,
            sender,
            args,
            cmd,
            fnBody;

        sender = arguments[0];
        cmd = arguments[1];
        args = arguments[3];
        cmdName = ('' + cmd.name).toLowerCase();
        for (; i < args.length; i++) {
            jsArgs.push('' + args[i]);

        }
        result = false;

        if (cmdName === 'js') {
            if (!isOp(sender)) {
                echo("You don't have any permissions to do this.");
                return true;
            }
            result = true;
            fnBody = jsArgs.join(' ');
            global.self = sender;
            global.__engine = __engine;
            try {
                var code = fnBody;
                replHooks.forEach(function (xform) {
                    code = xform(code);
                });
                jsResult = code;

                if (typeof jsResult !== 'undefined') {
                    if (jsResult === null) {
                    } else {
                        try {
                            if (isJavaObject(jsResult) || typeof jsResult === 'function') {
                                echo(sender, jsResult);
                            } else {
                                var replacer = function replacer(key, value) {
                                    return this[key] instanceof java.lang.Object
                                        ? '' + this[key]
                                        : value;
                                };
                                echo(sender, JSON.stringify(jsResult, replacer, 2));
                            }
                        } catch (displayError) {
                            logError(
                                'Error while trying to display result: ' +
                                jsResult +
                                ', Error: ' +
                                displayError
                            );
                        }
                    }
                }
            } catch (e) {
                logError(
                    'Error while trying to evaluate javascript: ' +
                    fnBody +
                    ', Error: ' +
                    e
                );
                echo(
                    sender,
                    'Error while trying to evaluate javascript: ' +
                    fnBody +
                    ', Error: ' +
                    e
                );
                throw e;
            } finally {
                if (!nashorn) {
                    delete global.self;
                    delete global.__engine;
                }
            }
        }
        if (cmdName === 'jsp') {
            if (!_isOp(sender)) {
                echo("You don't have any permissions to do this.");
                return true;
            }
            cmdModule.exec(jsArgs, sender);
            result = true;
        }
        return result;
    }

    var logger = null;
    logger = __plugin.logger;

    function logError(msg) {
        logger.severe(msg);
    }

    function logWarn(msg) {
        //logger.warning(msg);
    }

    var File = java.io.File,
        FileReader = java.io.FileReader,
        BufferedReader = java.io.BufferedReader,
        PrintWriter = java.io.PrintWriter,
        FileWriter = java.io.FileWriter,
        jsPluginsRootDir = __script.parentFile.parentFile,
        jsPluginsRootDirName = _canonize(jsPluginsRootDir),
        unloadHandlers = [];

    var configFile = new File(jsPluginsRootDir, 'data/');
    configFile.mkdirs();
    configFile = new File(configFile, 'global-config.json');
    var config = _load(configFile);
    if (!config) {
        config = {verbose: false};
    }
    global.config = config;
    global.__plugin = __plugin;
    (function () {
        var jsonFileReader = new FileReader(
            new File(jsPluginsRootDirName + '/lib/json2.js')
        );
        __engine['eval(java.io.Reader)'](jsonFileReader);
    })();

    global.addUnloadHandler = _addUnloadHandler;
    global.refresh = _refresh;
    global.echo = _echo;
    global.alert = _echo;
    global.scload = _load;
    global.scsave = _save;
    global.scloadJSON = _loadJSON;
    global.isOp = _isOp;
    var configRequire = _load(jsPluginsRootDirName + '/lib/require.js', true);

    var modulePaths = [
        jsPluginsRootDirName + '/lib/',
        jsPluginsRootDirName + '/modules/'
    ];

    if (config.verbose) {
        logger.info(
            'Setting up CommonJS-style module system. Root Directory: ' +
            jsPluginsRootDirName
        );
        logger.info('Module paths: ' + JSON.stringify(modulePaths));
    }
    var requireHooks = {
        loading: function (path) {
            if (config.verbose) {
                logger.info('loading ' + path);
            }
        },
        loaded: function (path) {
            if (config.verbose) {
                logger.info('loaded  ' + path);
            }
        }
    };
    global.require = configRequire(
        jsPluginsRootDirName,
        modulePaths,
        requireHooks,
        moduleHooks
    );

    var testJSPatch = require('js-patch')(global);
    var console = require('console')(logger);
    global.console = console;
    testJSPatch(console);

    require('persistence')(jsPluginsRootDir, global);

    var isJavaObject = require('java-utils').isJavaObject;

    var cmdModule = require('command');
    global.command = cmdModule.command;
    var plugins = require('plugin');
    global.__onTabComplete = require('tabcomplete');
    global.plugin = plugins.plugin;

    var events = require('events');
    global.events = events;

    __onDisableImpl = _onDisable;
    global.__onCommand = __onCommand;
    plugins.autoload(global, new File(jsPluginsRootDir, 'plugins'));
    require('legacy-check')(jsPluginsRootDir);
}