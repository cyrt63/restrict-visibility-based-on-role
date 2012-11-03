package com.ideotechnologies.jira.tabpanels;

//import com.atlassian.crowd.embedded.api.User;

import com.atlassian.jira.datetime.DateTimeFormatterFactory;
import com.atlassian.jira.issue.AttachmentManager;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.changehistory.ChangeHistoryManager;
import com.atlassian.jira.issue.history.DateTimeFieldChangeLogHelper;
import com.atlassian.jira.issue.tabpanels.ChangeHistoryTabPanel;
import com.atlassian.jira.security.groups.DefaultGroupManager;
import com.atlassian.jira.util.JiraDurationUtils;
import com.opensymphony.user.User;

public class RestrictedChangeHistoryTabPanel extends ChangeHistoryTabPanel {

	private DefaultGroupManager defaultGroupManager;
	private JiraDurationUtils jiraDurationUtils;
	private static final String GROUP_NAME = "groupname";
	public RestrictedChangeHistoryTabPanel(
			ChangeHistoryManager changeHistoryManager,
			AttachmentManager attachmentManager,
			JiraDurationUtils jiraDurationUtils,
			CustomFieldManager customFieldManager,
			DateTimeFormatterFactory dateTimeFormatterFactory,
			DateTimeFieldChangeLogHelper changeLogHelper,
			DefaultGroupManager defaultGroupManager) {

		super(changeHistoryManager, attachmentManager, jiraDurationUtils,
				customFieldManager, dateTimeFormatterFactory, changeLogHelper);
	
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
