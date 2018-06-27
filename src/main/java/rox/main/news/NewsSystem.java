package rox.main.news;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import rox.main.Main;
import rox.main.event.events.NewsAddEvent;
import rox.main.event.events.NewsGetEvent;
import rox.main.event.events.NewsLoadEvent;
import rox.main.event.events.NewsSaveEvent;

import java.io.*;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

public class NewsSystem {

    private ConcurrentHashMap<Integer, JSONArray> news = new ConcurrentHashMap<>();

    private JSONObject fileContent;


    /*
     *   News Object Array
     *   [0] - News String
     *   [1] - Author
     *   [2] - Date
     */

    public NewsSystem() {
        fileContent = Main.getFileConfiguration().getNewsIndex();
        loadNews();
    }

    public void addNews(String text, String author) {
        try {

            NewsAddEvent event = new NewsAddEvent(text, author);
            Main.getEventManager().callEvent(event);
            if (event.isCancelled()) return;

            JSONObject jsonObject = new JSONObject();

            try {
                getAllNews().forEach(jsonObject::put);
            } catch (Exception e) {
                jsonObject = new JSONObject();
            }

            JSONArray list = new JSONArray();
            list.add(text);
            list.add(author);
            list.add(LocalDateTime.now().toLocalDate().toString());
            jsonObject.put(jsonObject.size() + 1, list);

            news.putAll(jsonObject);
            save();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public String getFileContent() {
        return fileContent.toJSONString();
    }

    private void save() throws Exception {
        NewsSaveEvent event = new NewsSaveEvent();
        Main.getEventManager().callEvent(event);
        if (event.isCancelled()) return;

        Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Main.getFileConfiguration().getNewsFile()), "utf-8"));
        JSONObject jsonObject = new JSONObject();
        getAllNews().forEach(jsonObject::put);
        writer.write(jsonObject.toJSONString());
        writer.flush();
        writer.close();

    }

    public JSONArray getLastNews() {
        return news.get(news.size());
    }

    public JSONArray getNews(int index) {
        NewsGetEvent event = new NewsGetEvent(index, news.get(index));
        Main.getEventManager().callEvent(event);
        if (event.isCancelled()) return event.getReturn();
        return news.get(index);
    }

    public String getAllNewsAsString() {
        JSONObject jsonObject = new JSONObject();
        getAllNews().forEach(jsonObject::put);
        return jsonObject.toJSONString();
    }

    private ConcurrentHashMap<Integer, JSONArray> getAllNews() {
        return news;
    }

    private void loadNews() {
        NewsLoadEvent event = new NewsLoadEvent();
        Main.getEventManager().callEvent(event);
        if (event.isCancelled()) return;
        fileContent.forEach((integer, jsonArray) -> news.put(Integer.parseInt(String.valueOf(integer)), (JSONArray) jsonArray));
    }

    public ConcurrentHashMap<Integer, JSONArray> getNews() {
        return news;
    }

    public void setFileContent(JSONObject fileContent) {
        this.fileContent = fileContent;
    }

    public void setNews(ConcurrentHashMap<Integer, JSONArray> news) {
        this.news = news;
    }
}
