package com.chickling.kmanager.collector.apis.group;

import java.util.Collection;

import com.chickling.kmanager.model.GroupSummary;

/**
 * 
 * @author Hulva Luva.H
 * @since 2017-10-16
 *
 */
public interface IGroupController {

	public abstract Collection<GroupSummary> goups();
	
}