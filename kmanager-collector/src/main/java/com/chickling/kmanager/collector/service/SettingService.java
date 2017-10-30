package com.chickling.kmanager.collector.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chickling.kmanager.model.KmanagerSetting;

/**
 * @author Hulva Luva.H
 * @since 2017-10-18
 *
 */
@Service
public class SettingService {

	private final KmanagerSetting setting;

	@Autowired
	public SettingService(KmanagerSetting setting) {
		this.setting = setting;
	}
	
	
}
