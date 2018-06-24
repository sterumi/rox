package rox.main.pluginsystem;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import rox.main.Main;
import rox.main.event.events.PluginLoadEvent;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginManager {

    private ConcurrentHashMap<String, ROXPlugin> plugins = new ConcurrentHashMap<>();

    public PluginManager() {
        try {
            loadPlugins();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void loadPlugins() throws Exception {
        long startTime = System.currentTimeMillis();
        File[] files = new File("plugins/").listFiles();
        if (files != null) {
            for (final File file : files) {
                if (file.getPath().toLowerCase().endsWith(".jar")) {

                    JarFile jarFile = new JarFile(file.getCanonicalPath());

                    final Enumeration<JarEntry> entries = jarFile.entries();
                    while (entries.hasMoreElements()) {
                        final JarEntry entry = entries.nextElement();
                        if (entry.getName().equals("plugin.json")) {

                            StringBuilder content = new StringBuilder();
                            new BufferedReader(new InputStreamReader(jarFile.getInputStream(jarFile.getJarEntry(entry.getName())))).lines().forEach(content::append);

                            JSONObject object = (JSONObject) new JSONParser().parse(content.toString());
                            Class cl = new URLClassLoader(new URL[]{new File(file.getPath()).toURL()}).loadClass(String.valueOf(object.get("mainName")));
                            for (Class anInterface : cl.getInterfaces()) {
                                if (anInterface.getName().equals("rox.main.pluginsystem.ROXPlugin")) {
                                    ROXPlugin roxPlugin = (ROXPlugin) cl.newInstance();

                                    PluginLoadEvent event = new PluginLoadEvent(roxPlugin, String.valueOf(object.get("name")));
                                    Main.getEventManager().callEvent(event);
                                    if (event.isCancelled()) return;

                                    plugins.put(String.valueOf(object.get("name")), roxPlugin);
                                    roxPlugin.onLoad();
                                    Main.getLogger().log("PluginSystem", "Loaded: " + String.valueOf(object.get("name")));
                                }
                            }
                        }
                    }
                }
            }
        } else {
            Main.getLogger().log("PluginSystem", "No plugins found.");
        }
        Main.getLogger().time("PluginLoad", startTime);
    }

    public void stop() {
        plugins.forEach((s, roxPlugin) -> roxPlugin.onStop());
    }

    public ConcurrentHashMap<String, ROXPlugin> getPlugins() {
        return plugins;
    }

    public void setPlugins(ConcurrentHashMap<String, ROXPlugin> plugins) {
        this.plugins = plugins;
    }
}
