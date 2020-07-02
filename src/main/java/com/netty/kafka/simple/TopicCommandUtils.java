package com.netty.kafka.simple;

/**
 * @author 86136
 */
public class TopicCommandUtils {

    public static void main(String[] args) {
//        createTopic();
//        describeTopic();
        listTopic();
    }

    /**
     * 代码清单4-1
     */
    public static void createTopic(){
        String[] options = new String[]{
                "--zookeeper", "120.79.201.200:2181",
                "--create",
                "--replication-factor", "1",
                "--partitions", "1",
                "--topic", "topic-create-api"
        };
        kafka.admin.TopicCommand.main(options);
    }

    /**
     * 代码清单4-2
     */
    public static void describeTopic(){
        String[] options = new String[]{
                "--zookeeper", "120.79.201.200:2181",
                "--describe",
                "--topic", "topic-create"
        };
        kafka.admin.TopicCommand.main(options);
    }

    public static void listTopic(){
        String[] options = new String[]{
                "--zookeeper", "120.79.201.200:2181",
                "--list"
        };
        kafka.admin.TopicCommand.main(options);
    }
}
