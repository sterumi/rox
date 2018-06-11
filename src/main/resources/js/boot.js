var __boot = null;
(function () {
    var File = java.io.File, FileReader = java.io.FileReader, FileOutputStream = java.io.FileOutputStream,
        ZipInputStream = java.util.zip.ZipInputStream,
        jsPlugins = new File('plugins/ROX'),
        initScript = 'js/ROX.js';


    __boot = function (plugin, engine, classLoader) {
        var logger = plugin.logger, initScriptFile = new File(jsPlugins, initScript);

        if (!jsPlugins.exists()) {
            logger.info('Directory ' + jsPlugins.canonicalPath + ' does not exist.');
            logger.info('Initializing ' + jsPlugins.canonicalPath + ' directory with contents from plugin archive.');
            jsPlugins.mkdirs();
        }
        try {
            engine.eval(new FileReader(initScriptFile));
            __onEnable(engine, plugin, initScriptFile);
        } catch (e) {
            throw e;
        }
    };
})();
