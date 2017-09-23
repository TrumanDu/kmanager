package com.chickling.kmonitor.test.offset;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kafka.admin.ConsumerGroupCommand.ConsumerGroupCommandOptions;
import kafka.admin.ConsumerGroupCommand.KafkaConsumerGroupService;
import kafka.api.TopicMetadata;
import kafka.client.ClientUtils;
import scala.collection.JavaConversions;
import scala.collection.Seq;

/**
 * @author Hulva Luva.H from ECBD
 * @date 2017年9月22日
 * @description 
 *
 */
public class GetOffset {

  /**
   * @param args
   */
  public static void main(String[] args) {
    String[] cmd = {"--bootstrap-server", "10.16.238.101:8092,10.16.238.102:8092,10.16.238.103:8092,10.16.238.104:8092", "--list"};
    ConsumerGroupCommandOptions opts = new ConsumerGroupCommandOptions(cmd);
    KafkaConsumerGroupService kafkaConsumerGroupService = new KafkaConsumerGroupService(opts);
    
    List<String> groups = JavaConversions.seqAsJavaList(kafkaConsumerGroupService.listGroups());
    System.out.println(groups);
  }
  
  
  public static void topicsMetadata() {
    Set<String> topic = new HashSet<String>();
    topic.add("test");
    Seq<TopicMetadata> topicsMetadata = ClientUtils.fetchTopicMetadata(JavaConversions.asScalaSet(topic), ClientUtils.parseBrokerList("10.16.238.101:8092,10.16.238.102:8092,10.16.238.103:8092,10.16.238.104:8092"), "GetOffset", 1000, 0).topicsMetadata();
    System.out.println(topicsMetadata);
//    if(topicsMetadata.size() != 1 || !topicsMetadata.head().topic.equals(topic)) {
//      System.err.println(String.format("Error: no valid topic metadata for topic: %s, " + " probably the topic does not exist, run "+
//          "kafka-list-topic.sh to verify", topic) );
//      
//    }
  }

}
