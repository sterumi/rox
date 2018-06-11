package rox.main.pluginsystem;

import rox.main.Main;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

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
                /*Invocable inv = (Invocable) this.scriptEngine;
                this.scriptEngine.eval(new InputStreamReader(getClass().getResourceAsStream("/js/boot.js")));
                inv.invokeFunction("__boot", this, scriptEngine);*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ScriptEngine getScriptEngine() {
        return scriptEngine;
    }

}
