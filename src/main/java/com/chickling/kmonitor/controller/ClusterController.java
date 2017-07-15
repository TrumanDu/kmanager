package com.chickling.kmonitor.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.chickling.kmonitor.initialize.Initializer;
import com.chickling.kmonitor.model.Node;

/**
 * 
 * @author Hulva Luva.H
 *
 */
@RestController
public class ClusterController {

	@RequestMapping(value = "/clusterlist", method = RequestMethod.GET)
	public Node getTopicList() {
		return Initializer.og.getClusterViz();
	}
}
