import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;

public class CreatePageRankingGraph {
    private static final String DATADIR = "../SearchWebCrawler/data/reuters/";
    private static final String OUTPUTDIR = "../SearchWebCrawler/data/reuters/pagerank/";
    private static final String OUTPUTFILE = "pagerank.txt";


    public static void main(String[] args) {
        File dir = new File(DATADIR);
        File[] listOfFiles = dir.listFiles();
        try {
            FileWriter outputWriter = new FileWriter(OUTPUTDIR + OUTPUTFILE);
            Map<String, String> urlPathMap = new HashMap<>();
            List<List<String>> graph = new ArrayList<>();
            for (File file: listOfFiles) {
                if (file.isFile()) {
                    String filepath = DATADIR + file.getName();
                    JSONParser jsonParser = new JSONParser();
                    try {
                        FileReader reader = new FileReader(filepath);
                        Object obj = jsonParser.parse(reader);
                        JSONObject metaData  = (JSONObject) obj;
                        for (Object key: metaData.keySet()) {
                            String url = (String) key;
                            JSONObject data = (JSONObject) metaData.get(url);
                            String parentUrl = (String) data.get("parentPage");
                            String childPage = (String) data.get("html");
                            urlPathMap.put(url, childPage);
                            List<String> link = new ArrayList<>();
                            link.add(parentUrl);
                            link.add(url);
                            graph.add(link);
                        }
                        reader.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            for(List<String> nodes: graph) {
                String startNode = urlPathMap.get(nodes.get(0));
                String endNode = urlPathMap.get(nodes.get(1));
                if (startNode != null && endNode != null) {
                    outputWriter.write(startNode + " " + endNode + "\n");
                }
            }
            outputWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
