package com.chickling.kmonitor.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Hulva Luva.H
 *
 */
@Component
public class AppConfig {
	@Value("${elasticsearch.clustername}")
	private String esclusterName;
	@Value("${elasticsearch.hosts}")
	private String esHosts;
	@Value("${elasticsearch.index}")
	private String esindex;
	@Value("${elasticsearch.doctype}")
	private String esdocType;

	@Value("${kafka.data.collect.frequency:60}")
	private Long dataCollectFrequency;

	@Value("${zookeeper.hosts}")
	private String zk;
	@Value("${zookeeper.session.timeout:30*1000}")
	private Integer zkSessionTimeout;
	@Value("${zookeeper.connection.timeout:30*1000}")
	private Integer zkConnectionTimeout;

	@Value("${alert.enable:true}")
	private Boolean isAlertEnabled;

	@Value("${offsetinfo.cachequeue.size:100}")
	private Integer offsetInfoCacheQueue;
	@Value("${offsetinfo.handler.worker}")
	private Integer offsetInfoHandler;
	@Value("${task.storage.folder:tasks}")
	private String taskFolder;

	@Value("${mail.auth}")
	private Boolean auth;
	@Value("${mail.user:user}")
	private String mailUser;
	@Value("${mail.password:pwd}")
	private String mailPwd;
	@Value("${mail.smtp.host}")
	private String mailHost;
	@Value("${mail.sender}")
	private String sender;
	@Value("${mail.subject}")
	private String subject;

	@Value("${offsetinfo.excludeby.lastseen}")
	private Long excludeByLastSeen;

	public Boolean getIsAlertEnabled() {
		return isAlertEnabled;
	}

	public void setIsAlertEnabled(Boolean isAlertEnabled) {
		this.isAlertEnabled = isAlertEnabled;
	}

	public Boolean getAuth() {
		return auth;
	}

	public void setAuth(Boolean auth) {
		this.auth = auth;
	}

	public String getMailUser() {
		return mailUser;
	}

	public void setMailUser(String mailUser) {
		this.mailUser = mailUser;
	}

	public String getMailPwd() {
		return mailPwd;
	}

	public void setMailPwd(String mailPwd) {
		this.mailPwd = mailPwd;
	}

	public String getMailHost() {
		return mailHost;
	}

	public void setMailHost(String mailHost) {
		this.mailHost = mailHost;
	}

	public String getEsclusterName() {
		return esclusterName;
	}

	public void setEsclusterName(String esclusterName) {
		this.esclusterName = esclusterName;
	}

	public String getEsHosts() {
		return esHosts;
	}

	public void setEsHosts(String esHosts) {
		this.esHosts = esHosts;
	}

	public String getEsindex() {
		return esindex;
	}

	public void setEsindex(String esindex) {
		this.esindex = esindex;
	}

	public String getEsdocType() {
		return esdocType;
	}

	public void setEsdocType(String esdocType) {
		this.esdocType = esdocType;
	}

	public Long getDataCollectFrequency() {
		return dataCollectFrequency;
	}

	public void setDataCollectFrequency(Long dataCollectFrequency) {
		this.dataCollectFrequency = dataCollectFrequency;
	}

	public String getZk() {
		return zk;
	}

	public void setZk(String zk) {
		this.zk = zk;
	}

	public Integer getZkSessionTimeout() {
		return zkSessionTimeout;
	}

	public void setZkSessionTimeout(Integer zkSessionTimeout) {
		this.zkSessionTimeout = zkSessionTimeout;
	}

	public Integer getZkConnectionTimeout() {
		return zkConnectionTimeout;
	}

	public void setZkConnectionTimeout(Integer zkConnectionTimeout) {
		this.zkConnectionTimeout = zkConnectionTimeout;
	}

	public Integer getOffsetInfoCacheQueue() {
		return offsetInfoCacheQueue;
	}

	public void setOffsetInfoCacheQueue(Integer offsetInfoCacheQueue) {
		this.offsetInfoCacheQueue = offsetInfoCacheQueue;
	}

	public Integer getOffsetInfoHandler() {
		return offsetInfoHandler;
	}

	public void setOffsetInfoHandler(Integer offsetInfoHandler) {
		this.offsetInfoHandler = offsetInfoHandler;
	}

	public String getTaskFolder() {
		return taskFolder;
	}

	public void setTaskFolder(String taskFolder) {
		this.taskFolder = taskFolder;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Long getExcludeByLastSeen() {
		return excludeByLastSeen;
	}

	public void setExcludeByLastSeen(Long excludeByLastSeen) {
		this.excludeByLastSeen = excludeByLastSeen;
	}

}
