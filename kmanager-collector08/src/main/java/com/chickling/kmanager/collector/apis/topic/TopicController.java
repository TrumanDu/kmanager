package com.chickling.kmanager.collector.apis.topic;

import java.util.Collection;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.chickling.kmanager.model.TopicSummary;

/**
 * @author Hulva Luva.H
 * @since 2017-10-18
 *
 */
@RestController
@RequestMapping("/topic")
@CrossOrigin(origins = "http://localhost")
public class TopicController {

	@RequestMapping(value = "/summary/{topic}", method = RequestMethod.GET)
	public TopicSummary topic(@PathVariable String topic) {
		return null;

	}

	@RequestMapping(value = "/summarys", method = RequestMethod.GET)
	public Collection<TopicSummary> topics() {
		return null;
	}
}
