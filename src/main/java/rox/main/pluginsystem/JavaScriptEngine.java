package rox.main.pluginsystem;

import rox.main.Main;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.InputStreamReader;

public class JavaScriptEngine {

    private ScriptEngine scriptEngine = null;

    public JavaScriptEngine() {
        init();
    }

    private void init() {

        try {
            ScriptEngineManager factory = new ScriptEngineManager();
            factory.getEngineFactories().forEach(scriptEngineFactory -> System.out.println(scriptEngineFactory.getNames()));
            this.scriptEngine = factory.getEngineByName("JavaScript");
            if (this.scriptEngine == null) {
                Main.getLogger().log("JavaScript", "Engine not found!");
            } else {
                Invocable inv = (Invocable) this.scriptEngine;
                this.scriptEngine.eval(new InputStreamReader(getClass().getResourceAsStream("/js/boot.js")));
                inv.invokeFunction("__boot", this, scriptEngine);
            }
        } catch (Exception e) {
            if (e instanceof ScriptException) {
                ScriptException e1 = (ScriptException) e;
                System.out.println("ScriptException: File: " + e1.getFileName() + ". (" + e1.getLineNumber() + ":" + e1.getColumnNumber() + ") Exception: " + e1.getMessage());
            } else if (e instanceof NoSuchMethodException) {
                NoSuchMethodException e1 = (NoSuchMethodException) e;
                e1.printStackTrace();
            } else {
                e.printStackTrace();
            }
        }

    }

    public ScriptEngine getScriptEngine() {
        return scriptEngine;
    }

}
