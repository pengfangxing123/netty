package com.es;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;

import java.net.InetAddress;
import java.util.Map;

/**
 * @author Administrator
 */
public class EsBatchDemo {
    @Test
    public void testupmget() throws Exception {
        //指定集群cluster.name: my-application
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        //创建访问es服务器的客户端
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.30.129"), 9300));
        MultiGetResponse responses = client.prepareMultiGet()
                .add("index2", "blog", "8", "10")
                .add("lib", "user", "1")
                .get();

        responses.forEach(p->{
            GetResponse response = p.getResponse();
            if(response!=null&&response.isExists()){
                System.out.println(response.getSourceAsString());
            }
        });

    }

    @Test
    public void testbulk() throws Exception {
        //指定集群cluster.name: my-application
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        //创建访问es服务器的客户端
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.30.129"), 9300));

        BulkRequestBuilder bulk = client.prepareBulk();
        bulk.add(client.prepareIndex("lib2","books","15")
                .setSource(
                        XContentFactory.jsonBuilder().startObject()
                                .field("title","python")
                                .field("price",29)
                                .endObject())
                );
        bulk.add(client.prepareIndex("lib2","books","16")
                .setSource(
                        XContentFactory.jsonBuilder().startObject()
                                .field("title","java")
                                .field("price",29)
                                .endObject())
        );

        BulkResponse responses = bulk.get();
        System.out.println(responses.status());
        if(responses.hasFailures()){
            System.out.println(responses.buildFailureMessage());
        }
    }

    @Test
    public void testQueryDelete() throws Exception {
        //指定集群cluster.name: my-application
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        //创建访问es服务器的客户端
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.30.129"), 9300));

        BulkByScrollResponse response = DeleteByQueryAction.INSTANCE
                .newRequestBuilder(client)
                .filter(QueryBuilders.matchQuery("title", "java"))
                .source("index2")
                .get();

        long deleted = response.getDeleted();
        System.out.println(deleted);

    }

    @Test
    public void testmathcAll() throws Exception {
        //指定集群cluster.name: my-application
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        //创建访问es服务器的客户端
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.30.129"), 9300));

        MatchAllQueryBuilder query = QueryBuilders.matchAllQuery();
        SearchResponse response = client.prepareSearch("lib2")
                .setQuery(query)
                .setSize(2)
                .setFrom(1)
                .get();

        SearchHits hits = response.getHits();
        hits.forEach(p->{
            System.out.println(p.getSourceAsString());
//            Map<String, Object> sourceAsMap = p.getSourceAsMap();
//            sourceAsMap.forEach((key,val)->{
//                System.out.println(key);
//                System.out.println(val);
//            });

        });

    }

    @Test
    public void testmathc() throws Exception {
        //指定集群cluster.name: my-application
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        //创建访问es服务器的客户端
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.30.129"), 9300));

        MatchQueryBuilder query = QueryBuilders.matchQuery("title", "java");
        SearchResponse response = client.prepareSearch("lib2")
                .setQuery(query)
                .setSize(10)
                .setFrom(0)
                .get();

        SearchHits hits = response.getHits();
        hits.forEach(p->{
            System.out.println(p.getSourceAsString());
        });

    }

    @Test
    public void testmultimathch() throws Exception {
        //指定集群cluster.name: my-application
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        //创建访问es服务器的客户端
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.30.129"), 9300));

        MultiMatchQueryBuilder query = QueryBuilders.multiMatchQuery("java", "title");

        SearchResponse response = client.prepareSearch("lib2")
                .setQuery(query)
                .setSize(10)
                .setFrom(0)
                .get();

        SearchHits hits = response.getHits();
        hits.forEach(p->{
            System.out.println(p.getSourceAsString());
        });

    }


    @Test
    public void testterm() throws Exception {
        //指定集群cluster.name: my-application
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        //创建访问es服务器的客户端
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.30.129"), 9300));

        TermQueryBuilder query = QueryBuilders.termQuery("title", "java");
        SearchResponse response = client.prepareSearch("lib2")
                .setQuery(query)
                .setSize(10)
                .setFrom(0)
                .get();

        SearchHits hits = response.getHits();
        hits.forEach(p->{
            System.out.println(p.getSourceAsString());
        });
    }

    @Test
    public void testterms() throws Exception {
        //指定集群cluster.name: my-application
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        //创建访问es服务器的客户端
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.30.129"), 9300));

        TermsQueryBuilder query = QueryBuilders.termsQuery("title", "java", "python");
        SearchResponse response = client.prepareSearch("lib2")
                .setQuery(query)
                .setSize(10)
                .setFrom(0)
                .get();

        SearchHits hits = response.getHits();
        hits.forEach(p->{
            System.out.println(p.getSourceAsString());
        });
    }

}
