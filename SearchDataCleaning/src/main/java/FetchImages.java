import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;


public class FetchImages {

    private static final String DIRPATH = "../SearchWebCrawler/data/reuters/html/";
    private static final String OUTPUTPATH = "../SearchWebCrawler/data/reuters/image/";
    private static final String OUTPUTFILE = "imagemap.csv";

    private static Map<String, String> fetchData(String path) {
        Map<String, String> imageMap = new HashMap<>();
        try {
            Document doc = Jsoup.parse(new File(path), "utf-8");
            doc.select("script").remove();
            Elements imageTags = doc.getElementsByTag("img");
            for (Element imageTag : imageTags) {
                String alt = imageTag.attr("alt");
                String src = imageTag.attr("src");
                String dataUrl = imageTag.attr("data-lazy");
                if (alt != null && !alt.equals("")) {
                    if (dataUrl != null && !dataUrl.equals("")) {
                        if(imageMap.containsKey(dataUrl)){
                            StringBuilder altText = new StringBuilder(imageMap.get(dataUrl));
                            altText.append(alt);
                            imageMap.put(dataUrl, altText.toString());
                        }else{
                            StringBuilder altText = new StringBuilder(alt);
                            imageMap.put(dataUrl, altText.toString());
                        }
                    } else if(src != null && !src.equals("")){
                        if(imageMap.containsKey(src)){
                            StringBuilder altText = new StringBuilder(imageMap.get(src));
                            altText.append(alt);
                            imageMap.put(src, altText.toString());
                        }else{
                            StringBuilder altText = new StringBuilder(alt);
                            imageMap.put(src, altText.toString());
                        }
                    }
                }
            }
            FileWriter mainFw = new FileWriter(path, false);
            mainFw.write(doc.toString());
            mainFw.close();



        } catch (IOException e) {
            System.out.println("File IO exception");
        }

        return imageMap;
    }


    public static void main(String[] args) {
        File folder = new File(DIRPATH);
        File[] listOfFiles = folder.listFiles();
        try {
            FileWriter fw = new FileWriter(OUTPUTPATH + OUTPUTFILE, false);

            for (File file: listOfFiles) {
                if (file.isFile()) {
                    String filepath = DIRPATH + file.getName();
                    Map<String, String> imageMap = fetchData(filepath);
                    System.out.println("Processing: "+ filepath);
                    for (String url: imageMap.keySet()) {
                        fw.write(url + ";" + imageMap.get(url)+"\n");
                    }
                }
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}