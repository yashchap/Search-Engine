package server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackageClasses= api.SearchApiController.class)
@EnableAutoConfiguration
public class SearchServer {
    public static void main(String[] args) {
        SpringApplication.run(SearchServer.class, args);
    }
}
