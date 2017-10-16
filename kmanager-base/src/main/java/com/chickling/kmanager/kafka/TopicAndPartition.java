package com.chickling.kmanager.kafka;

/**
 * @author Hulva Luva.H
 * @since 2017-10-16
 *
 */
public class TopicAndPartition {
	private String topic;
	private int partition;

	public TopicAndPartition() {
		super();
	}

	public TopicAndPartition(String topic, int partition) {
		super();
		this.topic = topic;
		this.partition = partition;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public int getPartition() {
		return partition;
	}

	public void setPartition(int partition) {
		this.partition = partition;
	}

}
