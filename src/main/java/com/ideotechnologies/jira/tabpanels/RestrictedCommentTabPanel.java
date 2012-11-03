package com.ideotechnologies.jira.tabpanels;

import com.atlassian.jira.datetime.DateTimeFormatterFactory;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.RendererManager;
import com.atlassian.jira.issue.comments.CommentManager;
import com.atlassian.jira.issue.comments.CommentPermissionManager;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutManager;
import com.atlassian.jira.issue.tabpanels.CommentTabPanel;
import com.atlassian.jira.security.groups.DefaultGroupManager;
import com.opensymphony.user.User;

public class RestrictedCommentTabPanel extends CommentTabPanel {
	 	private final CommentManager commentManager;
	    private final CommentPermissionManager commentPermissionManager;
	    private final FieldLayoutManager fieldLayoutManager;
	    private final RendererManager rendererManager;
	    private final IssueManager issueManager;
	    private final DateTimeFormatterFactory dateTimeFormatterFactory;
	    private DefaultGroupManager defaultGroupManager;
	    private static final String GROUP_NAME = "groupname";
	public RestrictedCommentTabPanel(CommentManager commentManager,
			CommentPermissionManager commentPermissionManager,
			IssueManager issueManager, FieldLayoutManager fieldLayoutManager,
			RendererManager rendererManager,
			DateTimeFormatterFactory dateTimeFormatterFactory) {
		super(commentManager, commentPermissionManager, issueManager,
				fieldLayoutManager, rendererManager, dateTimeFormatterFactory);
		this.commentManager = commentManager;
        this.commentPermissionManager = commentPermissionManager;
        this.issueManager = issueManager;
        this.fieldLayoutManager = fieldLayoutManager;
        this.rendererManager = rendererManager;
        this.dateTimeFormatterFactory = dateTimeFormatterFactory;
        this.defaultGroupManager = defaultGroupManager;
	}

	
	
	public boolean showPanel(Issue issue, User remoteUser) {
		String group=descriptor.getParams().get(GROUP_NAME);
		if (remoteUser != null)
			return remoteUser.inGroup(group);
	       
	    return false;
	}
}
