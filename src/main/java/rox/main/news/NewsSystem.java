package rox.main.news;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import rox.main.Main;

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
        fileContent.forEach((integer, jsonArray) -> news.put(Integer.parseInt(String.valueOf(integer)), (JSONArray) jsonArray));
    }

}
