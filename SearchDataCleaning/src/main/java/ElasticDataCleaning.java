import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class ElasticDataCleaning {
    private static final String DIRPATH = "../SearchWebCrawler/data/reuters/";
    private static final String OUTPUTPATH = "../SearchWebCrawler/data/reuters/elastic/";
    private static final String OUTPUTFILE = "reuters.json";
    private static final String HTMLBASEDIR = "../SearchWebCrawler/";

    public static String readFileAsString(String fileName) {
        String text = "";
        try {
            text = new String(Files.readAllBytes(Paths.get(fileName)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return text;
    }

    public static void main(String[] args) {
        File folder = new File(DIRPATH);
        File[] listOfFiles = folder.listFiles();
        try {
            FileWriter fw = new FileWriter(OUTPUTPATH + OUTPUTFILE, false);
            for (File file: listOfFiles) {
                if (file.isFile()) {
                    String filepath = DIRPATH + file.getName();
                    JSONParser jsonParser = new JSONParser();
                    try (FileReader reader = new FileReader(filepath)) {
                        Object obj = jsonParser.parse(reader);
                        JSONObject scrappedData = (JSONObject) obj;
                        for(Iterator iterator = scrappedData.keySet().iterator(); iterator.hasNext();) {
                            String key = (String) iterator.next();
                            JSONObject htmlData = (JSONObject) scrappedData.get(key);
                            JSONObject index = new JSONObject();
                            JSONObject url = new JSONObject();
                            url.put("url", key);
                            index.put("index",url);
                            JSONObject html = new JSONObject();

                            html.put("content",readFileAsString(HTMLBASEDIR + htmlData.get("html").toString()));
                            fw.write(index.toString()+"\n");
                            fw.write(html.toString()+"\n");
                        }
                        System.out.println(filepath);

                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
