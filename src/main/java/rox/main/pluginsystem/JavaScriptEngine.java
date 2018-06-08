package rox.main.pluginsystem;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.InputStreamReader;

public class JavaScriptEngine {

    private ScriptEngine scriptEngine = null;

    public void init() {

        try {
            ScriptEngineManager factory = new ScriptEngineManager();
            factory.getEngineFactories().forEach(scriptEngineFactory -> System.out.println(scriptEngineFactory.getNames()));
            this.scriptEngine = factory.getEngineByName("JavaScript");
            if (this.scriptEngine == null) {
                System.out.println("JavaScript Engine not found!");
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
