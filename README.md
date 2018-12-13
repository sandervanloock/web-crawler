# web-crawler

to start the needed Docker containers and the JEF monitor, run: 
```$xslt
 ./dev.sh 
```

*Make sure port 80, 8080, 9200 and 9300 are free to use* 

To run a crawl as configured in [crawler-config.xml](src/main/resources/crawler-config.xml), run:
```$xslt
cd norconex-crawler
mvn spring-boot:run
```

The progress of the crawl can be monitored on [http://localhost:8080]().  
The result of the crawl can be found in Kibana on [http://localhost]()