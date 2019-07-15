package webcrawler;

public class CrawlerStatistics {
    /**
     * Calculate statics for crawlers
     * Example: Total pages scrapped, Totat link processed
     */

    private int processedPageCount = 0;
    private int totalLinksCount = 0;

    public void incrementProcessedPageCount() {
        processedPageCount++;
    }

    public void updateTotalLinkCount(int linkCount) {
        totalLinksCount += linkCount;
    }

    public int getProcessedPageCount() {
        return processedPageCount;
    }

    public int getTotalLinksCount() {
        return totalLinksCount;
    }
}
