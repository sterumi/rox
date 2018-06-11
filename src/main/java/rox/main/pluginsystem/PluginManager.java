package rox.main.pluginsystem;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import rox.main.Main;

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
        File[] files = new File("plugins/").listFiles();
        if (files == null) {
            Main.getLogger().log("PluginSystem", "No plugins found.");
            return;
        }
        for (final File file : files) {
            if (file.getPath().toLowerCase().endsWith(".jar")) {

                JarFile jarFile = new JarFile(file.getCanonicalPath());

                final Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    final JarEntry entry = entries.nextElement();
                    if (entry.getName().equals("plugin.json")) {
                        JarEntry fileEntry = jarFile.getJarEntry(entry.getName());
                        InputStream input = jarFile.getInputStream(fileEntry);
                        JSONObject object = (JSONObject) new JSONParser().parse(new BufferedReader(new InputStreamReader(input)).readLine());
                        String name = String.valueOf(object.get("name"));
                        String mainClassName = String.valueOf(object.get("mainName"));
                        Class cl = new URLClassLoader(new URL[]{new File(file.getPath()).toURL()}).loadClass(mainClassName);
                        Class[] interfaces = cl.getInterfaces();
                        for (Class anInterface : interfaces) {
                            if (anInterface.getName().equals("rox.main.pluginsystem.ROXPlugin")) {
                                ROXPlugin roxPlugin = (ROXPlugin) cl.newInstance();
                                plugins.put(name, roxPlugin);
                                roxPlugin.onLoad();
                                Main.getLogger().log("PluginSystem", "Loaded: " + name);
                            }
                        }
                    }
                }
            }
        }
    }

    public void stop() {
        plugins.forEach((s, roxPlugin) -> roxPlugin.onStop());
    }

    public ConcurrentHashMap<String, ROXPlugin> getPlugins() {
        return plugins;
    }

}
