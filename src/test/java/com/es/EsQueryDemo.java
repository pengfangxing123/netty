package com.es;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.Filter;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.aggregations.metrics.max.MaxAggregationBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * @author Administrator
 */
public class EsQueryDemo {
    private static TransportClient client;

    static{
        //指定集群cluster.name: my-application
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        //创建访问es服务器的客户端
        try {
            client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.30.129"), 9300));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRange(){
        //范围
//        RangeQueryBuilder dq = QueryBuilders.rangeQuery("price").from(20)
//                .to(30).includeLower(true).includeUpper(true);
        //前缀
        //PrefixQueryBuilder dq = QueryBuilders.prefixQuery("title", "py");

        //通配查询
        //WildcardQueryBuilder dq = QueryBuilders.wildcardQuery("title", "py*");

        //模糊查询
        //FuzzyQueryBuilder dq = QueryBuilders.fuzzyQuery("title", "pythn");

        //type查询
        //TypeQueryBuilder dq = QueryBuilders.typeQuery("books");

        //ids查询
        IdsQueryBuilder dq = QueryBuilders.idsQuery().addIds("1", "2");

        SearchResponse response = client.prepareSearch("lib2").setQuery(dq)
                .get();
        SearchHits hits = response.getHits();
        hits.forEach(p->{
           System.out.println(p.getSourceAsString());
       });
    }

    @Test
    public void testAggre(){
        MaxAggregationBuilder dq = AggregationBuilders.max("hh").field("price");
        SearchResponse response = client.prepareSearch("lib2").addAggregation(dq)
                .get();
        Max max = response.getAggregations().get("hh");
        System.out.println(max.getValue());

    }


    @Test
    public void testCommon(){
        //这种数字类型匹配不到值
        //CommonTermsQueryBuilder dq = QueryBuilders.commonTermsQuery("age", 10);

        //CommonTermsQueryBuilder dq = QueryBuilders.commonTermsQuery("name", "小华");
        QueryStringQueryBuilder dq = QueryBuilders.queryStringQuery("+20");

        SearchResponse response = client.prepareSearch("lib")
                .setQuery(dq)
                .get();
        SearchHits hits = response.getHits();
        hits.forEach(p->{
            System.out.println(p.getSourceAsString());
        });
    }

    @Test
    public void testBollQuery(){
        BoolQueryBuilder dq = QueryBuilders.boolQuery();
        dq.must(QueryBuilders.matchQuery("name","小华"))
                .mustNot(QueryBuilders.matchQuery("age",20))
                .should(QueryBuilders.matchQuery("age",20));

        SearchResponse response = client.prepareSearch("lib")
                .setQuery(dq)
                .get();
        SearchHits hits = response.getHits();
        hits.forEach(p->{
            System.out.println(p.getSourceAsString());
        });
    }

    @Test
    public void testAggre2(){
        TermsAggregationBuilder dq = AggregationBuilders.terms("terms").field("age");

        SearchResponse response = client.prepareSearch("lib").addAggregation(dq)
                .get();
        Terms terms = response.getAggregations().get("terms");
        List<? extends Terms.Bucket> buckets = terms.getBuckets();
        buckets.forEach(p->{
            System.out.println(p.getKey()+":"+p.getDocCount());
        });
    }

    @Test
    public void testfilter(){
        QueryBuilder dq = QueryBuilders.termQuery("age", 20);
        AggregationBuilder filter = AggregationBuilders.filter("filter", dq);
        SearchResponse response = client.prepareSearch("lib").addAggregation(filter)
                .get();

        Filter filter1 = response.getAggregations().get("filter");
        System.out.println(filter1.getDocCount());
    }
}
