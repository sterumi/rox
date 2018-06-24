package rox.main.javascript;

import rox.main.Main;
import rox.main.event.events.JavaScriptLoadEvent;
import rox.main.util.Script;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class JavaScriptEngine implements Script {

    private ScriptEngineManager scriptEngineManager;

    private ScriptEngine scriptEngine = null;

    private File rootPath = new File("scripts/javascript/");

    public JavaScriptEngine() {
        init();
    }

    public void init() {

        JavaScriptLoadEvent event = new JavaScriptLoadEvent();
        Main.getEventManager().callEvent(event);
        if (event.isCancelled()) return;

        if(rootPath.mkdirs())Main.getLogger().log("ROX", "Created javascript root path.");

        try {
            scriptEngineManager = new ScriptEngineManager();
            this.scriptEngine = scriptEngineManager.getEngineByName("JavaScript");
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

    public void executeFile(String filename){
        if(!filename.endsWith(".js")) return;
        File file = new File(rootPath, filename);
        if(file.exists()){
            StringBuilder content = new StringBuilder();
            try {
                new BufferedReader(new InputStreamReader(new FileInputStream(file))).lines().forEach(content::append);

                scriptEngine.eval(content.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            Main.getLogger().err("ROX", "Could not find javascript file: " + filename);
        }
    }

    public ScriptEngine getScriptEngine() {
        return scriptEngine;
    }

    public File getRootPath() {
        return rootPath;
    }

    public ScriptEngineManager getScriptEngineManager() {
        return scriptEngineManager;
    }
}
