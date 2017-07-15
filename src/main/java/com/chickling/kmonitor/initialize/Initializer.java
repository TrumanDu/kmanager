package com.chickling.kmonitor.initialize;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.chickling.kmonitor.alert.TaskContent;
import com.chickling.kmonitor.alert.TaskHandler;
import com.chickling.kmonitor.alert.TaskManager;
import com.chickling.kmonitor.config.AppConfig;
import com.chickling.kmonitor.core.OffsetGetter;
import com.chickling.kmonitor.core.ZKOffsetGetter;
import com.chickling.kmonitor.core.db.ElasticsearchOffsetDB;
import com.chickling.kmonitor.core.db.OffsetDB;
import com.chickling.kmonitor.email.EmailSender;
import com.chickling.kmonitor.model.KafkaInfo;
import com.google.gson.Gson;

/**
 * @author Hulva Luva.H
 *
 */
@Component
public class Initializer implements CommandLineRunner {

	private static Logger LOG = LoggerFactory.getLogger(Initializer.class);

	// TODO schedule pool size? any other schedule?
	private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

	private static ExecutorService worker;

	public static BlockingQueue<KafkaInfo> offsetInfoCacheQueue;

	public static OffsetDB db = null;

	public static OffsetGetter og = null;

	private static final int DEFAULT_THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();

	public static boolean isAlertEnabled = true;

	@Autowired
	AppConfig config;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.boot.CommandLineRunner#run(java.lang.String[])
	 */
	@Override
	public void run(String... args) throws Exception {
		db = new ElasticsearchOffsetDB(config);
		og = new ZKOffsetGetter(config);

		if (config.getIsAlertEnabled()) {
			EmailSender.setConfig(config);
			TaskManager.init(config);
			offsetInfoCacheQueue = new LinkedBlockingQueue<KafkaInfo>(config.getOffsetInfoCacheQueue());

			worker = Executors.newFixedThreadPool(
					config.getOffsetInfoHandler() != null ? config.getOffsetInfoHandler() : DEFAULT_THREAD_POOL_SIZE);

			for (int i = 0; i < config.getOffsetInfoHandler(); i++) {
				worker.submit(new TaskHandler());
			}

			File dir = new File(config.getTaskFolder());
			if (!dir.exists()) {
				dir.mkdir();
			}
			// Scan task folder load tasks to memory
			File[] listOfFiles = dir.listFiles();
			for (File file : listOfFiles) {
				if (file.isFile() && (file.getName().substring(file.getName().lastIndexOf('.') + 1).equals("task"))) {
					String strTaskContent = TaskManager.loadFileContent(file.getAbsolutePath());
					TaskManager.addTask(new Gson().fromJson(strTaskContent, TaskContent.class));
				}
			}
		} else {
			isAlertEnabled = false;
		}

		scheduler.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				ExecutorService pool = Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE);
				List<String> groups = og.getGroups();
				groups.forEach(group -> {
					try {
						pool.submit(new GenerateKafkaInfoTask(group));
					} catch (Exception e) {
						LOG.warn("Ops..." + e.getMessage());
					}
				});
				pool.shutdown();
			}

		}, 0, config.getDataCollectFrequency() * 1000, TimeUnit.MILLISECONDS);
	}

	class GenerateKafkaInfoTask implements Runnable {
		private String group;

		public GenerateKafkaInfoTask(String _group) {
			this.group = _group;
		}

		@Override
		public void run() {
			KafkaInfo kafkaInfo = null;
			try {
				kafkaInfo = og.getInfo(group, new ArrayList<String>());
				if (config.getIsAlertEnabled() && !offsetInfoCacheQueue.offer(kafkaInfo))
					LOG.warn("Offer kafkaInfo into offsetInfoCacheQueue faild..Queue zise: "
							+ config.getOffsetInfoCacheQueue() + ".Current cached: " + offsetInfoCacheQueue.size());
				db.batchInsert(kafkaInfo.getOffsets());
			} catch (Exception e) {
				LOG.warn("GenerateKafkaInfoTask for group: " + this.group + " failed.", e);
			}
		}

	}

}
