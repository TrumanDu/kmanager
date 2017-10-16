package com.chickling.kmanager.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.zookeeper.data.Stat;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chickling.kmanager.kafka.TopicAndPartition;
import com.chickling.kmanager.model.BrokerInfo;
import com.chickling.kmanager.zookeeper.ZkDataAndStat;

/**
 * @author Hulva Luva.H
 * @since 2017-10-16
 *
 */
public class ZKUtils {
	// val AdminPath = "/admin"
	// val BrokersPath = "/brokers"
	// val ClusterPath = "/cluster"
	// val ConfigPath = "/config"
	// val ControllerPath = "/controller"
	// val ControllerEpochPath = "/controller_epoch"
	// val IsrChangeNotificationPath = "/isr_change_notification"
	// val KafkaAclPath = "/kafka-acl"
	// val KafkaAclChangesPath = "/kafka-acl-changes"
	//
	// val ConsumersPath = "/consumers"
	// val ClusterIdPath = s"$ClusterPath/id"
	// val BrokerIdsPath = s"$BrokersPath/ids"
	// val BrokerTopicsPath = s"$BrokersPath/topics"
	// val ReassignPartitionsPath = s"$AdminPath/reassign_partitions"
	// val DeleteTopicsPath = s"$AdminPath/delete_topics"
	// val PreferredReplicaLeaderElectionPath =
	// s"$AdminPath/preferred_replica_election"
	// val BrokerSequenceIdPath = s"$BrokersPath/seqid"
	// val ConfigChangesPath = s"$ConfigPath/changes"
	// val ConfigUsersPath = s"$ConfigPath/users"

	private static final String BrokersPath = "/brokers";
	protected static final String ConsumersPath = "/consumers";
	private static final String BrokerTopicsPath = BrokersPath + "/topics";

	private static final String BrokerIdsPath = BrokersPath + "/ids";

	private static Logger LOG = LoggerFactory.getLogger(ZKUtils.class);

	private static ZkClient zkClient = null;
	private static ZkConnection zkConnection = null;

	public static void init(String zkHosts, int zkSessionTimeout, int zkConnectionTimeout) {
		try {
			LOG.debug("init ZKUtil with " + zkHosts + " - " + zkSessionTimeout + " - " + zkConnectionTimeout);
			if (zkConnection == null) {
				zkConnection = new ZkConnection(zkHosts);
			}
			if (zkClient == null) {
				zkClient = new ZkClient(zkConnection, zkConnectionTimeout, new SerializableSerializer());
			}
		} catch (Exception e) {
			throw new RuntimeException("Init ZKUtils failed! " + e.getMessage());
		}
	}

	public static ZkClient getZKClient() {
		return zkClient;
	}

	public static Set<TopicAndPartition> getAllPartitions() {
		List<String> topics = getChildrenParentMayNotExist(BrokerIdsPath);
		if (topics == null)
			return new HashSet<TopicAndPartition>();
		else {
			return topics.stream()
					.flatMap(topic -> getChildren(getTopicPartitionsPath(topic)).stream()
							.map(partition -> Integer.parseInt(partition))
							.map(partition -> new TopicAndPartition(topic, partition)))
					.collect(Collectors.toSet());
		}
	}

	public static Map<String, List<Integer>> getPartitionsForTopics(List<String> topics) {
		Map<String, List<Integer>> topicPartitions = new HashMap<String, List<Integer>>();
		Map<String, Map<Integer, List<Integer>>> ps4Topics = getPartitionAssignmentForTopics(topics);
		ps4Topics.forEach((topic, PartitionReplica) -> {
			List<Integer> partitions = new ArrayList<Integer>();
			PartitionReplica.forEach((partition, replicas) -> {
				partitions.add(partition);
			});
			Collections.sort(partitions, new Comparator<Integer>() {
				@Override
				public int compare(Integer pid1, Integer pid2) {
					return pid1 < pid2 ? 1 : -1;
				}
			});
			topicPartitions.put(topic, partitions);
		});
		return topicPartitions;

	}

	public static Map<String, Map<Integer, List<Integer>>> getPartitionAssignmentForTopics(List<String> topics) {
		Map<String, Map<Integer, List<Integer>>> ret = new HashMap<String, Map<Integer, List<Integer>>>();
		topics.forEach(topic -> {
			String data = readDataMaybeNull(ZKUtils.BrokerTopicsPath + "/" + topic).getData();
			if (data == null) {
				ret.put(topic, null);
			} else {
				JSONObject json = new JSONObject(data).getJSONObject("partitions");
				Map<Integer, List<Integer>> replicaMap = new HashMap<Integer, List<Integer>>();
				for (String pid : json.keySet()) {
					JSONArray bidsJson = json.getJSONArray(pid);
					List<Integer> replicas = new ArrayList<Integer>();
					bidsJson.forEach(bid -> {
						replicas.add((Integer) bid);
					});
					replicaMap.put(Integer.parseInt(pid), replicas);
				}
				ret.put(topic, replicaMap);
			}
		});
		return ret;
	}

	public static List<String> getAllTopics() {
		List<String> topics = getChildrenParentMayNotExist(BrokerTopicsPath);
		return topics == null ? new ArrayList<String>() : topics;
	}

	public static Optional<BrokerInfo> getBrokerInfo(int brokerId) {
		ZkDataAndStat broker = readDataMaybeNull(BrokerIdsPath + "/" + brokerId);
		if (broker == null || broker.getData() == null) {
			return Optional.empty();
		}
		String brokerInfo = new String(readDataMaybeNull("/brokers/ids/" + brokerId).getData());

		BrokerInfo bi = new BrokerInfo();
		bi.setBid(brokerId);
		JSONObject jsonObj = new JSONObject(brokerInfo);
		if (jsonObj.has("host")) {
			bi.setHost(jsonObj.get("host").toString());
		}
		if (jsonObj.has("port")) {
			bi.setPort(Integer.parseInt(jsonObj.get("port").toString()));
		}
		if (jsonObj.has("jmx_port")) {
			bi.setJmxPort(Integer.parseInt(jsonObj.get("jmx_port").toString()));
		}
		if (jsonObj.has("version")) {
			bi.setVersion(Integer.parseInt(jsonObj.get("version").toString()));
		}
		if (jsonObj.has("timestamp")) {
			bi.setTimestamp(Long.parseLong(jsonObj.get("timestamp").toString()));
		}
		return Optional.of(bi);
	}

	public static List<String> getConsumersInGroup(String group) {
		ZKGroupDirs dirs = new ZKGroupDirs(group);
		return getChildren(dirs.getConsumerRegistryDir());

	}

	public static boolean pathExists(String path) {
		return zkClient.exists(path);
	}

	public static List<String> getChildren(String path) {
		List<String> children = null;
		try {
			children = zkClient.getChildren(path);
		} catch (Exception e) { // that should be
								// {org.apache.zookeeper.KeeperException$NoNodeException}
			children = new ArrayList<String>();
		}
		return children;
	}

	public static ZkDataAndStat readDataMaybeNull(String path) {
		Stat stat = new Stat();
		String data = null;
		try {
			data = zkClient.readData(path, stat);
		} catch (Exception e) {
			LOG.warn("Path: " + path + " do not exits in ZK!" + e.getMessage());
		}
		return new ZkDataAndStat(data, stat);
	}

	public static List<BrokerInfo> getBrokers() throws Exception {
		List<BrokerInfo> kafkaHosts = new ArrayList<BrokerInfo>();
		List<String> ids = getChildren("/brokers/ids");
		for (String id : ids) {
			try {
				Optional<BrokerInfo> bi = getBrokerInfo(Integer.parseInt(id));
				if (bi.isPresent()) {
					kafkaHosts.add(bi.get());
				} else {
					LOG.warn("Dirty broker node [/brokers/ids" + id + "]");
				}
			} catch (Exception e) {
				LOG.error("getBrokers failed! {}", e);
			}
		}
		return kafkaHosts;
	}

	// TODO for those invalid consumer id content, is that a concerned part?
	// public static Map<String, List<ConsumerThreadId>>
	// getConsumersPerTopic(String
	// group,
	// boolean excludeInternalTopics) {
	// List<String> consumers = getChildren(ZkUtils.ConsumersPath() + "/" +
	// group +
	// "/ids");
	// Map<String, List<ConsumerThreadId>> consumersPerTopicMap = new
	// HashMap<String, List<ConsumerThreadId>>();
	// for (String consumer : consumers) {
	// TopicCount topicCount = constructTopicCount(group, consumer,
	// zkUtilsFromZK, excludeInternalTopics);
	// }
	//
	// return null;
	//
	// }
	//
	// private static TopicCount constructTopicCount(String group, String
	// consumerId, ZkUtils zkUtilsFromZK,
	// boolean excludeInternalTopics) {
	// ZkDataAndStat consumerData = readDataMaybeNull(
	// ZkUtils.ConsumersPath() + "/" + group + "/ids" + "/" + consumerId);
	// String topicCountString = consumerData.getData();
	// String subscriptionPattern = null;
	// Map<String, Integer> topMap = null;
	// //
	// {"version":1,"subscription":{"EC2_SHOPPINGCART":1},"pattern":"static","timestamp":"1498610861408"}
	// if (topicCountString != null) {
	// JSONObject topicCountJson = new JSONObject(topicCountString);
	//
	// }
	// return null;
	// }

	private static List<String> getChildrenParentMayNotExist(String path) {
		try {
			return zkClient.getChildren(path);
		} catch (ZkNoNodeException e) {
			LOG.error("getChildrenParentMayNotExist error! path: " + path, e);
		}
		return new ArrayList<String>();
	}

	private static String getTopicPartitionsPath(String topic) {
		return getTopicPath(topic) + "/partitions";
	}

	private static String getTopicPath(String topic) {
		return ZKUtils.BrokerTopicsPath + "/" + topic;
	}

	public static void close() {
		try {
			if (zkClient != null) {
				zkClient.close();
				zkClient = null;
			}
			if (zkConnection != null) {
				zkConnection.close();
				zkConnection = null;
			}
		} catch (InterruptedException e) {
			LOG.error("ZKUtils close() error! ", e);
		}
	}
}

class ZKGroupDirs {

	private String consumerDir = ZKUtils.ConsumersPath;

	private String consumerGroupDir;
	private String consumerRegistryDir;
	private String consumerGroupOffsetsDir;
	private String consumerGroupOwnersDir;

	public ZKGroupDirs(String group) {
		this.consumerGroupDir = consumerDir + "/" + group;
		this.consumerRegistryDir = consumerGroupDir + "/ids";
		this.consumerGroupOffsetsDir = consumerGroupDir + "/offsets";
		this.consumerGroupOwnersDir = consumerGroupDir + "/owners";
	}

	public String getConsumerDir() {
		return consumerDir;
	}

	public void setConsumerDir(String consumerDir) {
		this.consumerDir = consumerDir;
	}

	public String getConsumerGroupDir() {
		return consumerGroupDir;
	}

	public void setConsumerGroupDir(String consumerGroupDir) {
		this.consumerGroupDir = consumerGroupDir;
	}

	public String getConsumerRegistryDir() {
		return consumerRegistryDir;
	}

	public void setConsumerRegistryDir(String consumerRegistryDir) {
		this.consumerRegistryDir = consumerRegistryDir;
	}

	public String getConsumerGroupOffsetsDir() {
		return consumerGroupOffsetsDir;
	}

	public void setConsumerGroupOffsetsDir(String consumerGroupOffsetsDir) {
		this.consumerGroupOffsetsDir = consumerGroupOffsetsDir;
	}

	public String getConsumerGroupOwnersDir() {
		return consumerGroupOwnersDir;
	}

	public void setConsumerGroupOwnersDir(String consumerGroupOwnersDir) {
		this.consumerGroupOwnersDir = consumerGroupOwnersDir;
	}

}
