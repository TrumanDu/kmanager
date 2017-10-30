package com.chickling.kmanager.collector.apis.group;

import java.util.Collection;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.chickling.kmanager.model.GroupSummary;

/**
 * 
 * @author Hulva Luva.H
 * @since 2017-10-16
 *
 */
@RestController
@RequestMapping("/group")
@CrossOrigin(origins = "http://localhost")
public class GroupController {

	@RequestMapping(value = "/summary/{group}", method = RequestMethod.GET)
	public GroupSummary group(@PathVariable String group) {
		return null;
	}
	
	
	@RequestMapping(value = "/summarys", method = RequestMethod.GET)
	public Collection<GroupSummary> goups() {
		return null;
	}

}