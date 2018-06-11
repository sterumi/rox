(function (rootDir, modulePaths, hooks, evalHooks) {
    function commonJsWrap(code) {
        return (
            '(function(exports,module,require,__filename,__dirname){ \n' +
            code +
            '\n})'
        );
    }

    var i = 0,
        len = evalHooks.length,
        registered = false;
    for (; i < len; i++) {
        if (evalHooks[i] === commonJsWrap) {
            registered = true;
            break;
        }
    }
    if (!registered) {
        evalHooks.unshift(commonJsWrap);
    }
    var File = java.io.File,
        FileReader = java.io.FileReader,
        BufferedReader = java.io.BufferedReader;

    function fileExists(file) {
        if (file.isDirectory()) {
            return readModuleFromDirectory(file);
        } else {
            return file;
        }
    }

    function _canonize(file) {
        return '' + file.canonicalPath.replaceAll('\\\\', '/');
    }

    function readModuleFromDirectory(dir) {
        var pkgJsonFile = new File(dir, './package.json');
        if (pkgJsonFile.exists()) {
            var pkg = scload(pkgJsonFile);
            var mainFile = new File(dir, pkg.main);
            if (mainFile.exists()) {
                return mainFile;
            } else {
                return null;
            }
        } else {
            var indexJsFile = new File(dir, './index.js');
            if (indexJsFile.exists()) {
                return indexJsFile;
            } else {
                return null;
            }
        }
    }

    function resolveModuleToFile(moduleName, parentDir) {
        var file = new File(moduleName),
            i = 0,
            resolvedFile;
        if (file.exists()) {
            return fileExists(file);
        }
        if (moduleName.match(/^[^\.\/]/)) {

            for (; i < modulePaths.length; i++) {
                resolvedFile = new File(modulePaths[i] + moduleName);
                if (resolvedFile.exists()) {
                    return fileExists(resolvedFile);
                } else {
                    resolvedFile = new File(modulePaths[i] + moduleName + '.js');
                    if (resolvedFile.exists()) {
                        return resolvedFile;
                    }
                }
            }
        } else {
            if ((file = new File(parentDir, moduleName)).exists()) {
                return fileExists(file);
            } else if ((file = new File(parentDir, moduleName + '.js')).exists()) {
                return file;
            } else if ((file = new File(parentDir, moduleName + '.json')).exists()) {
                return file;
            }
        }
        return null;
    }

    function _require(parentFile, path, options) {
        var file,
            canonizedFilename,
            moduleInfo,
            buffered,
            code = '',
            line = null;

        if (typeof options === 'undefined') {
            options = {cache: true};
        } else {
            if (typeof options.cache === 'undefined') {
                options.cache = true;
            }
        }

        file = resolveModuleToFile(path, parentFile);
        if (!file) {
            var errMsg =
                '' +
                _format(
                    "require() failed to find matching file for module '%s' " +
                    "in working directory '%s' ",
                    [path, parentFile.canonicalPath]
                );
            if (!('' + path).match(/^\./)) {
                errMsg =
                    errMsg + ' and not found in paths ' + JSON.stringify(modulePaths);
            }
            var find = _require(parentFile, 'find').exports;
            var allJS = [];
            for (var i = 0; i < modulePaths.length; i++) {
                var js = find(modulePaths[i]);
                for (var j = 0; j < js.length; j++) {
                    if (js[j].match(/\.js$/)) {
                        allJS.push(js[j].replace(modulePaths[i], ''));
                    }
                }
            }
            var pathL = path.toLowerCase();
            var candidates = [];
            for (i = 0; i < allJS.length; i++) {
                var filenameparts = allJS[i];
                var candidate = filenameparts.replace(/\.js/, '');
                var lastpart = candidate.toLowerCase();
                if (pathL.indexOf(lastpart) > -1 || lastpart.indexOf(pathL) > -1) {
                    candidates.push(candidate);
                }
            }
            if (candidates.length > 0) {
                errMsg +=
                    '\nBut found module/s named: ' +
                    candidates.join(',') +
                    ' - is this what you meant?';
            }
            throw new Error(errMsg);
        }
        canonizedFilename = _canonize(file);

        moduleInfo = _loadedModules[canonizedFilename];
        if (moduleInfo) {
            if (options.cache) {
                return moduleInfo;
            }
        }
        if (hooks) {
            hooks.loading(canonizedFilename);
        }
        buffered = new BufferedReader(new FileReader(file));
        while ((line = buffered.readLine()) !== null) {
            code += line + '\n';
        }
        buffered.close();

        if (
            canonizedFilename
                .toLowerCase()
                .substring(canonizedFilename.length - 5) === '.json'
        ) {
            code = 'module.exports = (' + code + ');';
        }

        moduleInfo = {
            loaded: false,
            id: canonizedFilename,
            exports: {},
            require: _requireClosure(file.parentFile)
        };

        if (options.cache) {
            _loadedModules[canonizedFilename] = moduleInfo;
        }
        var codeFn = null;
        try {
            evalHooks.forEach(function (evalHook) {
                var result = evalHook(code);
                if (result.constructor !== String) {
                    codeFn = result;
                } else {
                    code = result;
                }
            });
        } catch (e) {
            throw new Error(
                'Error evaluating module ' +
                path +
                ' line #' +
                e.lineNumber +
                ' : ' +
                e.message,
                canonizedFilename,
                e.lineNumber
            );
        }
        var __dirname = '' + file.parentFile.canonicalPath;
        var parameters = [
            moduleInfo.exports /* exports */,
            moduleInfo /* module */,
            moduleInfo.require /* require */,
            canonizedFilename /* __filename */,
            __dirname /* __dirname */
        ];
        try {
            codeFn.apply(moduleInfo.exports, /* this */ parameters);
        } catch (e) {
            var snippet = '';
            if (('' + e.lineNumber).match(/[0-9]/)) {
                var lines = code.split(/\n/);
                if (e.lineNumber > 1) {
                    snippet = ' ' + lines[e.lineNumber - 2] + '\n';
                }
                snippet += '> ' + lines[e.lineNumber - 1] + '\n';
                if (e.lineNumber < lines.length) {
                    snippet += ' ' + lines[e.lineNumber] + '\n';
                }
            }
            throw new Error(
                'Error executing module ' +
                path +
                ' line #' +
                e.lineNumber +
                ' : ' +
                e.message +
                (snippet ? '\n' + snippet : ''),
                canonizedFilename,
                e.lineNumber
            );
        }
        if (hooks) {
            hooks.loaded(canonizedFilename);
        }
        moduleInfo.loaded = true;
        return moduleInfo;
    }

    function _requireClosure(parentFile) {
        var _boundRequire = function requireBoundToParent(path, options) {
            var module = _require(parentFile, path, options);
            return module.exports;
        };

        _boundRequire.resolve = function resolveBoundToParent(path) {
            return resolveModuleToFile(path, parentFile);
        };
        _boundRequire.cache = _loadedModules;

        return _boundRequire;
    }

    var _loadedModules = {};
    var _format = java.lang.String.format;
    return _requireClosure(new java.io.File(rootDir));
})