package com.chickling.kmanager.controller.core;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import com.chickling.kmanager.model.ClusterSummary;

/**
 * 
 * @author Hulva Luva.H
 * @since 2017-10-16
 *
 */

public class Manager {
	private final static ConcurrentHashMap<String, ClusterSummary> clusters = new ConcurrentHashMap<String, ClusterSummary>();

	public static void newClusterAdded(ClusterSummary cluster) {
		clusters.put(cluster.getName(), cluster);
	}
	
	public static ClusterSummary clusterMetadata(String clusterName) {
		return clusters.get(clusterName);
	}
	
	public static Collection<ClusterSummary> clusterMetadatas() {
		return clusters.values();
	}
}
