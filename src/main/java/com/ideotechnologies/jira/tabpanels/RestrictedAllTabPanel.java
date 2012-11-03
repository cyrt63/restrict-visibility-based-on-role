package com.ideotechnologies.jira.tabpanels;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.tabpanels.AllTabPanel;
import com.atlassian.jira.security.groups.DefaultGroupManager;
import com.atlassian.jira.util.JiraDurationUtils;
import com.atlassian.plugin.PluginAccessor;
import com.opensymphony.user.User;

public class RestrictedAllTabPanel extends AllTabPanel {
	private DefaultGroupManager defaultGroupManager;
	private JiraDurationUtils jiraDurationUtils;
	private static final String GROUP_NAME = "groupname";
	public RestrictedAllTabPanel(PluginAccessor pluginAccessor) {
		super(pluginAccessor);
	}
	public boolean showPanel(Issue issue, User remoteUser) {
		String group=descriptor.getParams().get(GROUP_NAME);
		if (remoteUser != null)
		    return remoteUser.inGroup(group);
       
		return false;
	}

}
