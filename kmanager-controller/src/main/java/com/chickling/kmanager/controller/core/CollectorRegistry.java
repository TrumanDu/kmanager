package com.chickling.kmanager.controller.core;

import com.chickling.kmanager.model.ClusterSummary;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


/**
 * @author Hulva Luva.H
 * @since 2017年11月11日
 * @description
 *
 */
public class CollectorRegistry {
  private final static Logger LOG = LoggerFactory.getLogger(CollectorRegistry.class);

  private final static ConcurrentHashMap<String, ClusterSummary> clusters = new ConcurrentHashMap<String, ClusterSummary>();

  static RestTemplate restTemplate;
  static SimpleClientHttpRequestFactory httpRequestFactory;

  static {
    httpRequestFactory = new SimpleClientHttpRequestFactory();
    httpRequestFactory.setConnectTimeout(1000);
    httpRequestFactory.setReadTimeout(1000);
    restTemplate = new RestTemplate();
    restTemplate.setRequestFactory(httpRequestFactory);
  }


  public static void add(ClusterSummary cluster) {
    clusters.put(cluster.getName(), cluster);
  }

  public static boolean checkIfNameExists(String name) {
    return clusters.containsKey(name);
  }

  public static boolean checkIfHeartbeatCheckWork(String heartbeatCheckUrl) {
    if ("OK".equals(heartbeatCheck(heartbeatCheckUrl))) {
      return true;
    }
    return false;
  }

  public static String heartbeatCheck(String url) {
    String status = "OK";
    try {
      ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
      status = response.getBody();
    } catch (RestClientException e) {
      // TODO what's in e?
      LOG.debug("Heartbeat Check", e);
      status = "NA";
    }
    return status;
  }

}
