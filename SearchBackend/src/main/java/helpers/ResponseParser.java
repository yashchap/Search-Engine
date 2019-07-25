package helpers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ResponseParser {
    public static String parseJsonResponse(String response) {
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonResponse = (JSONObject) parser.parse(response);
            JSONObject solrRespones = (JSONObject) jsonResponse.get("response");
            JSONObject parsedResponse = new JSONObject();
            parsedResponse.put("numFound", solrRespones.get("numFound"));
            parsedResponse.put("start", solrRespones.get("start"));
            JSONArray solrDocs = (JSONArray) solrRespones.get("docs");
            JSONArray parsedDocs = new JSONArray();
            for(Object doc: solrDocs) {
                JSONObject parsedDoc = new JSONObject();
                JSONObject jsonDoc = (JSONObject) doc;
                parsedDoc.put("url", jsonDoc.get("id"));
                JSONArray title = (JSONArray) jsonDoc.get("title");
                parsedDoc.put("title", title.get(0));
                JSONArray description = (JSONArray) jsonDoc.get("description");
                parsedDoc.put("description", description.get(0));
                parsedDocs.add(parsedDoc);
            }
            parsedResponse.put("docs", parsedDocs);
            return parsedResponse.toString();
        } catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }
}
