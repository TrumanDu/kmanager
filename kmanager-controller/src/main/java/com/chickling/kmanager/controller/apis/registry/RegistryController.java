package com.chickling.kmanager.controller.apis.registry;

import com.chickling.kmanager.controller.core.CollectorRegistry;
import com.chickling.kmanager.model.ClusterSummary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Hulva Luva.H
 * @since 2017年11月11日
 * @description
 *
 */
@RestController
@RequestMapping("/registry")
@CrossOrigin(origins = "http://localhost")
public class RegistryController {
  private static final Logger LOG = LoggerFactory.getLogger(RegistryController.class);

  /*** collector regist.
   * 
   * @param cluster which collector stands
   * @return status
   */
  @RequestMapping("/regist")
  public String regist(@RequestBody ClusterSummary cluster) {
    if (CollectorRegistry.checkIfNameExists(cluster.getName())) {
      return "NAME_EXISTS";
    }
    cluster.setStatus(CollectorRegistry.heartbeatCheck(cluster.getHeartbeatCheckUrl()));
    Long timestamp = System.currentTimeMillis();
    cluster.setJoinedTime(timestamp);
    cluster.setLastHeartbeatCheckTime(timestamp);
    cluster.setLastAvialableTime(timestamp);
    CollectorRegistry.add(cluster);
    return "OK";
  }

}
