package com.chickling.kmanager.collector.apis.setting;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chickling.kmanager.model.KmanagerSetting;

/**
 * @author Hulva Luva.H
 * @since 2017-10-18
 *
 */
@RestController
@RequestMapping("/setting")
@CrossOrigin(origins = "http://localhost")
public class SettingController {

	
	public KmanagerSetting setting() {
		return null;
	}
}
