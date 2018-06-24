package rox.main.util;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.File;

public interface Script {

    void executeFile(String filename);

    void init();

    ScriptEngine getScriptEngine();

    ScriptEngineManager getScriptEngineManager();

    File getRootPath();

}
