package com.chickling.kmanager.kafka;

import javax.management.MBeanServerConnection;

/**
 * @author luva
 * @since 2017-10-16
 *
 */
public interface JMXExecutor {
	
	public void doWithConnection(MBeanServerConnection mBeanServerConnection);
	
}
