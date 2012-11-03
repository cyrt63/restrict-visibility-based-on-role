package com.ideotechnologies.jira.tabpanels;

import com.atlassian.jira.bc.issue.worklog.WorklogService;
import com.atlassian.jira.config.properties.ApplicationProperties;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.RendererManager;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutManager;
import com.atlassian.jira.issue.tabpanels.WorklogTabPanel;
import com.atlassian.jira.security.groups.DefaultGroupManager;
import com.atlassian.jira.util.JiraDurationUtils;
import com.atlassian.jira.web.FieldVisibilityManager;
import com.opensymphony.user.User;

public class RestrictedWorklogTabPanel extends WorklogTabPanel {
	private DefaultGroupManager defaultGroupManager;
	private JiraDurationUtils jiraDurationUtils;
	private static final String GROUP_NAME = "groupname";
	public RestrictedWorklogTabPanel(WorklogService worklogService,
			JiraDurationUtils jiraDurationUtils,
			FieldLayoutManager fieldLayoutManager,
			RendererManager rendererManager,
			ApplicationProperties applicationProperties,
			FieldVisibilityManager fieldVisibilityManager) {
		super(worklogService, jiraDurationUtils, fieldLayoutManager, rendererManager,
				applicationProperties, fieldVisibilityManager);
		this.defaultGroupManager = defaultGroupManager;
		this.jiraDurationUtils = jiraDurationUtils;
	}
	public boolean showPanel(Issue issue, User remoteUser) {
		String group=descriptor.getParams().get(GROUP_NAME);
		if (remoteUser != null)
			return remoteUser.inGroup(group);
       
		return false;
	}
}
