package webcrawler;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

import java.io.File;
import java.util.logging.*;
import java.util.HashSet;
import java.util.Set;

public class ReutersWebCrawlerController {

    /**
     * Controller to configure crawler.
     */

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static int numCrawlers = 15;
    private static String crawlerMetadataPath = "data/metadata";
    private static String scrappedDataPath = "data/reuters/";
    public static final String HOST = "reuters.com";
    private static int delay = 1000;
    private static int depth = 40;
    private static int maxPages = 4000;
    private static boolean includeBinary = true;
    private static boolean resumable = false;

    static Set<String> seeds = new HashSet<String>(){
        {
            add("https://www.reuters.com");

        }};

    public static void main(String[] args) throws Exception {
        CrawlConfig config = new CrawlConfig();

        // File storage for intermediate metadata for crawler.
        File crawlMetadataStorage = new File(crawlerMetadataPath);
        config.setCrawlStorageFolder(crawlMetadataStorage.getAbsolutePath());

        // Delay between two requests.
        config.setPolitenessDelay(delay);
        // depth of scrapping.
        config.setMaxDepthOfCrawling(depth);
        // maximum limit of pages to scrap.
        config.setMaxPagesToFetch(maxPages);
        // config option for scrap binary file.
        config.setIncludeBinaryContentInCrawling(includeBinary);
        // option to whether to start freshstart or resume.
        config.setResumableCrawling(resumable);


        // Initiate the controller for this crawl.
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

        for(String seed: seeds){
            controller.addSeed(seed);
        }


        CrawlerStatistics stats = new CrawlerStatistics();
        CrawlController.WebCrawlerFactory<ReutersWebCrawler> factory = () -> new ReutersWebCrawler(stats, scrappedDataPath, HOST);

        controller.start(factory, numCrawlers);
        LOGGER.info(String.format("Crawled %d pages. %n", stats.getProcessedPageCount()));
        LOGGER.info(String.format("Total Number of outbound links = %s. %n", stats.getTotalLinksCount()));
    }

}
