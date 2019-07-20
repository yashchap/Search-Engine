import networkx as nx

INPUTPATH = "../SearchWebCrawler/data/reuters/pagerank/pagerank.txt"
OUTPUTDIR = "../SearchWebCrawler/data/reuters/pagerank/"
OUTPUTFILE = "score.csv"
class PageRanking(object):
    """
        return: page_rank score
        params: file path for graph
    """
    def __init__(self, path):
        self.path = path
    def generate_score(self):
        try:
            input_file = open(self.path, 'rb')
            graph = nx.read_edgelist(input_file)
            input_file.close()
        except Exception as e:
            print(e)
        
        directed_graph = nx.DiGraph(graph)
        page_rank_score = nx.pagerank(directed_graph, alpha=0.9)
        return page_rank_score

if __name__ == "__main__":
    pg_handler = PageRanking(INPUTPATH)
    score_map = pg_handler.generate_score()
    with open(OUTPUTDIR + OUTPUTFILE, "w") as output_file:
        for key, value in score_map.items():
            output_file.write(key +";"+ str(value) + "\n")



