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
    private static final String OUTPUTFILEADJ = "pagerank_adj.txt";
    private static final String URLPATHMAPFILE = "url_map.txt";


    public static void main(String[] args) {
        File dir = new File(DATADIR);
        File[] listOfFiles = dir.listFiles();
        try {
            FileWriter outputWriter = new FileWriter(OUTPUTDIR + OUTPUTFILE);
            FileWriter outputWriterAdj = new FileWriter(OUTPUTDIR + OUTPUTFILEADJ);
            FileWriter outputWriterUrlMap = new FileWriter(OUTPUTDIR + URLPATHMAPFILE);
            Map<String, List<String>> graph = new HashMap<>();
            Map<String, String> urlPathMap = new HashMap<>();
            Map<String, String> urlFileMap = new HashMap<>();
            List<List<String>> graph_helper = new ArrayList<>();
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
                            urlPathMap.put(childPage, url);
                            urlFileMap.put(childPage, url);
                            List<String> link = new ArrayList<>();
                            link.add(parentUrl);
                            link.add(url);
                            graph_helper.add(link);
                        }
                        reader.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            for(List<String> nodes: graph_helper) {
                String startNode = urlPathMap.get(nodes.get(0));
                String endNode = urlPathMap.get(nodes.get(1));
                if (startNode != null && endNode != null) {
                    if(graph.containsKey(startNode)){
                        graph.get(startNode).add(endNode);
                    }
                    else{
                        List<String> node = new ArrayList<>();
                        node.add(endNode);
                        graph.put(startNode, node);
                    }
                    outputWriter.write(startNode + " " + endNode + "\n");
                }
            }
            for(String path: urlFileMap.keySet()) {
                outputWriterUrlMap.write(path + " " + urlFileMap.get(path) + "\n");
            }
            for(String start: graph.keySet()){
                outputWriterAdj.write(start);
                for(String child: graph.get(start)){
                    outputWriterAdj.write(" "+child);
                }
                outputWriterAdj.write("\n");
            }
            outputWriter.close();
            outputWriterAdj.close();
            outputWriterUrlMap.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
