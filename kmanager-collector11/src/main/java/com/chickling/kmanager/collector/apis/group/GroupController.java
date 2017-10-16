package com.chickling.kmanager.collector.apis.group;

import java.util.Collection;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
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
public class GroupController implements IGroupController {

	public Collection<GroupSummary> goups() {
		return null;
	}

}
