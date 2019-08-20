package helpers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class ResponseParser {

    private static Map<String, String> getFileMapping(String filepath) {
        Map<String, String> urlMap = new HashMap<String, String>();
        try{
            BufferedReader reader = new BufferedReader(new FileReader(filepath));
            String line = reader.readLine();
            while(line != null) {
                urlMap.put(line.split(" ")[0], line.split(" ")[1]);
                line = reader.readLine();
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return urlMap;
    }
    public static String parseJsonResponse(String response, String filepath) {
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonResponse = (JSONObject) parser.parse(response);
            JSONObject solrRespones = (JSONObject) jsonResponse.get("response");
            JSONObject parsedResponse = new JSONObject();
            parsedResponse.put("numFound", solrRespones.get("numFound"));
            parsedResponse.put("start", solrRespones.get("start"));
            JSONArray solrDocs = (JSONArray) solrRespones.get("docs");
            JSONArray parsedDocs = new JSONArray();
            Map<String, String> urlMap = getFileMapping(filepath);
            for(Object doc: solrDocs) {
                JSONObject parsedDoc = new JSONObject();
                JSONObject jsonDoc = (JSONObject) doc;
                String url = (String) jsonDoc.get("id");
                String dataUrl = url.substring(url.indexOf("data/"));
                if(urlMap.containsKey(dataUrl)){
                    url = urlMap.get(dataUrl);
                    parsedDoc.put("url", url);
                    JSONArray title = (JSONArray) jsonDoc.get("title");
                    if(title != null)
                        parsedDoc.put("title", title.get(0));
                    JSONArray description = (JSONArray) jsonDoc.get("description");
                    if(description != null)
                        parsedDoc.put("description", description.get(0));
                    parsedDocs.add(parsedDoc);
                }

            }
            parsedResponse.put("docs", parsedDocs);
            return parsedResponse.toString();
        } catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }
}
