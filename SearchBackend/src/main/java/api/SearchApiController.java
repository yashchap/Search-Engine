package api;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import helpers.ResponseParser;

@RestController
public class SearchApiController {
    private static final String SEARCHBASEURL = "http://localhost:8983/solr/reuters/select";
    private static final String IMAGESEARCHBASEURL = "http://localhost:8983/solr/images/select";
    private static final String FILEPATH = "../SearchWebCrawler/data/reuters/pagerank/url_map.txt";
    private static final String IMAGEFILEPATH = "../SearchWebCrawler/data/reuters/image/imageurlmap.txt";
    private static String get(String url) throws Exception{
        Log logger = LogFactory.getLog(SearchApiController.class);
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        logger.info("\nSending 'GET' request to URL : " + url);
        logger.info("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

    @CrossOrigin(origins = "*")
    @RequestMapping("/search")
    public String getSearchResult(@RequestParam(value = "q") String query, @RequestParam(value = "page", defaultValue = "0") String page) throws Exception{
        Log logger = LogFactory.getLog(SearchApiController.class);
        String url = SEARCHBASEURL + "?q=" + query.replace(" ", "%20") + "&start=" + page;
        String response = "";
        try{
            response = get(url);
        } catch (Exception e){
            e.printStackTrace();
        }
        return ResponseParser.parseJsonResponse(response, FILEPATH);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping("/image")
    public String getImageResult(@RequestParam(value = "q") String query) throws Exception {
       String url = IMAGESEARCHBASEURL + "?q=" + query.replace(" ", "%20");
       String response = "";
       try {
           response = get(url);
       } catch (Exception e){
           e.printStackTrace();
       }
       return ResponseParser.parseJsonResponse(response, IMAGEFILEPATH);
    }

}
