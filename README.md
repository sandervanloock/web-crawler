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

#Elasticsearch

Elasticsearch runs as a single-node cluster in Docker.
To disable index replicas, you can add an index-template.

This commands creates an index template and will apply these settings for indices starting with crawl*:
```aidl
curl -XPUT "http://localhost:9200/_index_template/crawl-template" -H 'Content-Type: application/json' -d'{  "index_patterns": ["crawl*"],  "template": {    "settings": {      "number_of_shards": 1    },    "mappings": {      "properties": {        "crawl_date": {          "type": "date",          "format": "yyyyMMdd'\''T'\''HHmmss.SSSZ"        }      }    }  },  "priority": 200,  "version": 1,  "_meta": {    "description": "Default crawl index template"  }}'
```