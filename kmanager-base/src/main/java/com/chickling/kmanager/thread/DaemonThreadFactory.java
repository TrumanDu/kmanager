package com.chickling.kmanager.thread;

import java.util.concurrent.ThreadFactory;

/**
 * 
 * @author Hulva Luva.H
 * @since 2017-10-16
 *
 */
public class DaemonThreadFactory implements ThreadFactory {
	private String prefix = "";

	public DaemonThreadFactory(String prefix) {
		this.prefix = prefix;
	}

	@Override
	public Thread newThread(Runnable r) {
		Thread t = new Thread(r, "DaemonThread-" + prefix);
		t.setDaemon(true);
		if (t.getPriority() != Thread.NORM_PRIORITY)
			t.setPriority(Thread.NORM_PRIORITY);
		return t;
	}
}
