package rox.main.lua;

import org.luaj.vm2.script.LuaScriptEngine;
import rox.main.Main;
import rox.main.util.Script;

import javax.script.ScriptEngineManager;
import java.io.*;

public class LuaLoader implements Script {

    private File rootPath = new File("scripts/lua/");

    private ScriptEngineManager engineManager;

    private LuaScriptEngine scriptEngine;

    private File mainFile;

    public LuaLoader() {
        load();
    }

    public void load(){
        if (rootPath.mkdirs()) Main.getLogger().log("ROX", "Created lua scripts folder.");
        engineManager = new ScriptEngineManager();
        scriptEngine = (LuaScriptEngine) engineManager.getEngineByExtension(".lua");

        if((Boolean)Main.getFileConfiguration().getLuaValues().get("loadExternal")){
            if (new File(rootPath, Main.getFileConfiguration().getLuaValues().get("mainFile").toString()).exists()) {
                mainFile = new File(rootPath, Main.getFileConfiguration().getLuaValues().get("mainFile").toString());
                try {
                    StringBuilder content = new StringBuilder();
                    new BufferedReader(new InputStreamReader(new FileInputStream(mainFile))).lines().forEach(content::append);
                    scriptEngine.eval(content.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Main.getLogger().err("ROX", "Could not find lua file: " + Main.getFileConfiguration().getLuaValues().get("mainFile").toString());
                return;
            }
        }else{
            try {
                scriptEngine.eval(new InputStreamReader(getClass().getResourceAsStream("/lua/boot.lua")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void executeFile(String filename){
        if(!filename.endsWith(".lua")) return;
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
            Main.getLogger().err("ROX", "Could not find lua file: " + filename);
        }
    }

    public LuaScriptEngine getScriptEngine() {
        return scriptEngine;
    }

    public ScriptEngineManager getEngineManager() {
        return engineManager;
    }

    public File getRootPath() {
        return rootPath;
    }

    public File getBootFile() {
        return mainFile;
    }
}
