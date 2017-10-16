package com.chickling.kmanager.controller.apis.cluster;

import java.util.Collection;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.chickling.kmanager.controller.core.Manager;
import com.chickling.kmanager.model.ClusterSummary;

/**
 * 
 * @author Hulva Luva.H
 * @since 2017-10-16
 *
 */
@RestController
@RequestMapping("/cluster")
@CrossOrigin(origins = "http://localhost")
public class ClusterController {

	@RequestMapping(value = "/clusters", method = RequestMethod.GET)
	public Collection<ClusterSummary> clusters() {
		return Manager.clusterMetadatas();
	}
	
	@RequestMapping(value = "/summary/{clusterName}", method = RequestMethod.GET)
	public ClusterSummary cluster(@PathVariable String clusterName) {
		return Manager.clusterMetadata(clusterName);
	}
	
	// TODO trend?
	
}
