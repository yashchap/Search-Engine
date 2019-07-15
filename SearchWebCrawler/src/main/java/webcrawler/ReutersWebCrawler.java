package webcrawler;

import java.io.FileWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import java.io.File;
import java.io.IOException;

import org.json.simple.JSONObject;

public class ReutersWebCrawler extends WebCrawler{

    private static final Pattern EXCLUSIONS = Pattern.compile(".*(\\.(css|js|xml|mp3|bmp|gif|jpg|png|mp4|zip|gz|pdf))$");
    private static String scrappedDataPath;
    private Set visited = new HashSet<String>();
    CrawlerStatistics stats;
    String HOST;


    public ReutersWebCrawler(CrawlerStatistics stats, String scrappedDataPath, String HOST) {
        this.stats = stats;
        this.scrappedDataPath = scrappedDataPath;
        this.HOST = HOST;
    }

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String urlString = url.getURL().toLowerCase();
        if(visited.contains(urlString)){
            return false;
        }
        return !EXCLUSIONS.matcher(urlString).matches() && urlString.contains(HOST);
    }

    @Override
    public void visit(Page page) {

        int docid = page.getWebURL().getDocid();
        String url = page.getWebURL().getURL();
        String domain = page.getWebURL().getDomain();
        String path = page.getWebURL().getPath();
        String subDomain = page.getWebURL().getSubDomain();
        String parentUrl = page.getWebURL().getParentUrl();
        String anchor = page.getWebURL().getAnchor();


        JSONObject data = new JSONObject();
        data.put("docId", docid);
        data.put("url", url);
        data.put("domain", domain);
        data.put("subDomain", subDomain);
        data.put("path", path);
        data.put("parentPage", parentUrl);
        data.put("anchorText", anchor);

        stats.incrementProcessedPageCount();

        if (page.getParseData() instanceof HtmlParseData) {

            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String title = htmlParseData.getTitle();
            String text = htmlParseData.getText();
            String html = htmlParseData.getHtml();
            Set<WebURL> links = htmlParseData.getOutgoingUrls();
            try {
                data.put("title", title);
                FileWriter fwhtml = new FileWriter(scrappedDataPath + "html/" + Integer.toString(docid)+".txt");
                fwhtml.write(html);
                data.put("html", scrappedDataPath + "html/" + Integer.toString(docid)+".txt");
                FileWriter fwtext = new FileWriter(scrappedDataPath + "text/" + Integer.toString(docid)+".txt");
                fwtext.write(text);
                data.put("text", scrappedDataPath + "text/" + Integer.toString(docid)+".txt");

            }catch (Exception e){
                e.printStackTrace();
            }
            stats.updateTotalLinkCount(links.size());
            logger.debug("Text length: {}", text.length());
            logger.debug("Html length: {}", html.length());
            logger.debug("Number of outgoing links: {}", links.size());
        }
        JSONObject pageData = new JSONObject();
        pageData.put(url, data);
        File file = new File(scrappedDataPath, Integer.toString(docid) + ".json");
        try (FileWriter fw = new FileWriter(file)){

            fw.write(pageData.toJSONString());
            fw.flush();
            visited.add(url);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
