package com.ideotechnologies.jira.tabpanels;

import com.atlassian.crowd.embedded.ofbiz.db.DataAccessException;
import com.atlassian.jira.datetime.DateTimeFormatterFactory;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.IssueRelationConstants;
import com.atlassian.jira.issue.RendererManager;
import com.atlassian.jira.issue.action.IssueActionComparator;
import com.atlassian.jira.issue.comments.Comment;
import com.atlassian.jira.issue.comments.CommentComparator;
import com.atlassian.jira.issue.comments.CommentManager;
import com.atlassian.jira.issue.comments.CommentPermissionManager;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutManager;
import com.atlassian.jira.issue.tabpanels.CommentAction;
import com.atlassian.jira.issue.tabpanels.GenericMessageAction;
import com.atlassian.jira.plugin.issuetabpanel.AbstractIssueTabPanel;
import com.google.common.collect.Lists;
import com.opensymphony.user.User;
import org.ofbiz.core.entity.GenericEntityException;
import org.ofbiz.core.entity.GenericValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CommentTabPanelClientsView extends AbstractIssueTabPanel {
	private final CommentManager commentManager;
    private final CommentPermissionManager commentPermissionManager;
    private final FieldLayoutManager fieldLayoutManager;
    private final RendererManager rendererManager;
    private final IssueManager issueManager;
    private final DateTimeFormatterFactory dateTimeFormatterFactory;
    private static final String ROLE_ID = "roleid";

	public CommentTabPanelClientsView(final CommentManager commentManager, final CommentPermissionManager commentPermissionManager,
            final IssueManager issueManager, final FieldLayoutManager fieldLayoutManager,
            final RendererManager rendererManager, DateTimeFormatterFactory dateTimeFormatterFactory)
    {
        this.commentManager = commentManager;
        this.commentPermissionManager = commentPermissionManager;
        this.issueManager = issueManager;
        this.fieldLayoutManager = fieldLayoutManager;
        this.rendererManager = rendererManager;
        this.dateTimeFormatterFactory = dateTimeFormatterFactory;
    }

	@Override
	public List getActions(Issue issue, User user) {
		// Get the list of Comment objects for the given Issue that this user can see.
       // Collection<Comment> userComments = commentManager.getCommentsForUser(issue, user);

        String role=descriptor.getParams().get(ROLE_ID);
        Long roleLevelId=Long.valueOf(role);

		Collection<Comment> userComments = getCommentsForRole(issue, roleLevelId);//commentManager.getCommentsForUser(issue, user);
        // We need to turn these into CommentAction objects for display on the web.
        List<CommentAction> commentActions = new ArrayList<CommentAction>();

        // We want to do these checks ONCE. Doing them for each iteration of the loop below is very inefficient.
        boolean issueIsInEditableWorkflow = issueManager.isEditable(issue);
        // Allow issueIsInEditableWorkflow shortcut this check
        boolean canDeleteAllComments = issueIsInEditableWorkflow && commentPermissionManager.hasDeleteAllPermission(user, issue);
        // If you can delete all comments, then you can delete your own comments, and we can shortcut this check
        boolean canDeleteOwnComments = canDeleteAllComments ||
                                       (issueIsInEditableWorkflow && commentPermissionManager.hasDeleteOwnPermission(user, issue));
        boolean canEditAllComments = issueIsInEditableWorkflow && commentPermissionManager.hasEditAllPermission(user, issue);
        boolean canEditOwnComments = canEditAllComments ||
                                     (issueIsInEditableWorkflow && commentPermissionManager.hasEditOwnPermission(user, issue));

        for (final Comment comment : userComments)
        {
            boolean canDelete = canDeleteAllComments || (canDeleteOwnComments && commentManager.isUserCommentAuthor(user, comment));
            boolean canEdit = canEditAllComments || (canEditOwnComments && commentManager.isUserCommentAuthor(user, comment));
            
            commentActions.add(new CommentAction(descriptor, comment, canEdit, canDelete, rendererManager, fieldLayoutManager, dateTimeFormatterFactory));
        }

        // This is a bit of a hack to indicate that there are no comments to display
        if (commentActions.isEmpty())
        {
            GenericMessageAction action = new GenericMessageAction(descriptor.getI18nBean().getText("viewissue.nocomments"));
            return Lists.newArrayList(action);
        }

        // TODO: We should retrieve them sorted correctly in the first place.
        Collections.sort(commentActions, IssueActionComparator.COMPARATOR);
        return commentActions;
	}

	@Override
	public boolean showPanel(Issue issue, User remoteUser) {
		// TODO Auto-generated method stub
		return true;
	}
	
	
	
	public List<Comment> getCommentsForRole(Issue issue, Long roleLevelId)
    {
        List<Comment> visibleComments = new ArrayList<Comment>();

        try
        {
            // get a List<GenericValue> of comments
            List<GenericValue> allComments = issueManager.getEntitiesByIssueObject(IssueRelationConstants.COMMENTS, issue);

            for (final GenericValue commentGV : allComments)
            {
                Comment comment = commentManager.convertToComment(commentGV);

              //  if (commentPermissionManager.hasBrowsePermission(user, comment))
                if (comment.getRoleLevelId()!=null && comment.getRoleLevelId().equals(roleLevelId))
                {
                    visibleComments.add(comment);
                }
            }
        }
        catch (GenericEntityException e)
        {
            throw new DataAccessException(e);
        }
        Collections.sort(visibleComments, CommentComparator.COMPARATOR);
        return visibleComments;
    }
	 

}
