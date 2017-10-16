package com.chickling.kmanager.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.LoggerFactory;

/**
 * 
 * @author Hulva Luva.H
 * @since 2017-10-16
 *
 */
public class KmanagerThreadFactory implements ThreadFactory {
	private AtomicInteger counter = new AtomicInteger(0);
	private String prefix = "";

	public KmanagerThreadFactory(String prefix) {
		this.prefix = prefix;
	}

	@Override
	public Thread newThread(Runnable r) {
		Thread t = new Thread(r, prefix + "-" + counter.incrementAndGet());
		t.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

			@Override
			public void uncaughtException(Thread t, Throwable e) {
				LoggerFactory.getLogger(t.getName()).error(e.getMessage(), e);
			}
		});
		return t;
	}
}
