package com.es;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;

import java.net.InetAddress;

/**
 *
 * @author Administrator
 */
public class EsDemo {

    @Test
    public void test1() throws Exception {
        //指定集群cluster.name: my-application
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        //创建访问es服务器的客户端
        TransportClient client=new PreBuiltTransportClient(settings)
                                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.30.129"),9300));

        //数据查询
        GetResponse response = client.prepareGet("lib", "user", "1").execute().actionGet();

        //得到查询出的数据
        System.out.println(response.getSourceAsString());

        client.close();
    }

    @Test
    public void testadd() throws Exception {
        //指定集群cluster.name: my-application
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        //创建访问es服务器的客户端
        TransportClient client=new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.30.129"),9300));

//        PUT /index2
//        {
//            "settings":{
//            "number_of_shards": 3,
//                    "number_of_replicas": 0
//        },
//            "mappings":{
//            "blog":{
//                "properties":
//                {
//                    "id":{"type":"long"},
//                    "title":{"type":"text"},
//                    "conrent":{"type":"text"},
//                    "postdate":{"type":"date"},
//                    "url":{"type":"text"}
//                }
//
//            }
//        }
//        }
        XContentBuilder doc = XContentFactory.jsonBuilder().startObject()
                .field("id", 1)
                .field("title", "java client es.........")
                .field("conrent", "content:java client es.........")
                .field("postdate", "2019-08-04")
                .field("url", "www.baidu.com")
                .endObject();

        IndexResponse response = client.prepareIndex("index2", "blog", "10")
                .setSource(doc).get();

        System.out.println(response.status());
        client.close();
    }

    @Test
    public void testdelete() throws Exception {
        //指定集群cluster.name: my-application
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        //创建访问es服务器的客户端
        TransportClient client=new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.30.129"),9300));

        DeleteResponse response = client.prepareDelete("index2", "blog", "10").get();
        System.out.println(response.status());
    }

    @Test
    public void testupdate() throws Exception {
        //指定集群cluster.name: my-application
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        //创建访问es服务器的客户端
        TransportClient client=new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.30.129"),9300));

        UpdateRequest request = new UpdateRequest()
                .index("index2")
                .type("blog")
                .id("10")
                .doc(
                        XContentFactory.jsonBuilder().startObject()
                                .field("title", "title:java client es")
                                .field("conrent", "content:java client es")
                                .endObject()
                );

        UpdateResponse response = client.update(request).get();
        System.out.println(response.status());
        client.close();
    }

    @Test
    public void testupdateset() throws Exception {
        //指定集群cluster.name: my-application
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        //创建访问es服务器的客户端
        TransportClient client=new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.30.129"),9300));

        IndexRequest source = new IndexRequest("index2", "blog", "8")
                .source(
                        XContentFactory.jsonBuilder().startObject()
                                .field("id", 2)
                                .field("title", "java client es.........")
                                .field("conrent", "content:java client es.........")
                                .field("postdate", "2019-08-04")
                                .field("url", "www.baidu.com")
                                .endObject()
                );

        UpdateRequest update = new UpdateRequest()
                .index("index2")
                .type("blog")
                .id("8")
                .doc(
                        XContentFactory.jsonBuilder().startObject()
                                .field("title", "title:es")
                                .field("conrent", "content:es")
                                .endObject()
                ).upsert(source);

        UpdateResponse response = client.update(update).get();
        System.out.println(response.status());
        client.close();
    }
}


