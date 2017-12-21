package com.chickling.kmanager.model;

/**
 * @author luva
 * @since 2017-10-16
 *
 */
public class ClusterSummary {
  private String name;
  private String heartbeatCheckUrl;
  private String status;
  private Long joinedTime;
  private Long lastAvialableTime;
  private Long lastHeartbeatCheckTime;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getHeartbeatCheckUrl() {
    return heartbeatCheckUrl;
  }

  public void setHeartbeatCheckUrl(String heartbeatCheckUrl) {
    this.heartbeatCheckUrl = heartbeatCheckUrl;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Long getLastAvialableTime() {
    return lastAvialableTime;
  }

  public void setLastAvialableTime(Long lastAvialableTime) {
    this.lastAvialableTime = lastAvialableTime;
  }

  public Long getLastHeartbeatCheckTime() {
    return lastHeartbeatCheckTime;
  }

  public void setLastHeartbeatCheckTime(Long lastHeartbeatCheckTime) {
    this.lastHeartbeatCheckTime = lastHeartbeatCheckTime;
  }

  public Long getJoinedTime() {
    return joinedTime;
  }

  public void setJoinedTime(Long joinedTime) {
    this.joinedTime = joinedTime;
  }


}
