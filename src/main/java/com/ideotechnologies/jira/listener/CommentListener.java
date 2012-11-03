package com.ideotechnologies.jira.listener;


import com.atlassian.jira.ComponentManager;
import com.atlassian.jira.event.issue.AbstractIssueEventListener;
import com.atlassian.jira.event.issue.IssueEvent;
import com.atlassian.jira.issue.comments.Comment;
import com.atlassian.jira.issue.comments.CommentImpl;
import com.atlassian.jira.issue.comments.CommentManager;
import com.ideotechnologies.jira.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.StringTokenizer;

public class CommentListener extends AbstractIssueEventListener {

private static final Logger log = LoggerFactory.getLogger(CommentListener.class);
private long restrictedRoleLevelId;
private long notRestrictedRoleLevelId;
private String listOfNotRestrictedEvents;

    public CommentListener(){
    }

    @Override
 public void init(Map params){
	 String restrictedRoleLevelIdString  =(String) params.get(Constants.RESTRICTED_ROLE_LEVEL_ID);
	 restrictedRoleLevelId=new Long(restrictedRoleLevelIdString);
     String notRestrictedRoleLevelIdString  =(String) params.get(Constants.NOT_RESTRICTED_ROLE_LEVEL_ID);
     notRestrictedRoleLevelId=new Long(notRestrictedRoleLevelIdString);
     listOfNotRestrictedEvents  =(String) params.get(Constants.LIST_OF_NOT_RESTRICTED_EVENTS);
    }

 @Override
 public String[] getAcceptedParams(){
     return new String[]{Constants.RESTRICTED_ROLE_LEVEL_ID,Constants.NOT_RESTRICTED_ROLE_LEVEL_ID,Constants.LIST_OF_NOT_RESTRICTED_EVENTS};
 }


    public void restrictCommentVisibility(IssueEvent event) {

        String currentToken;
        Boolean restrictVisibility=true;

        CommentManager commentManager= ComponentManager.getInstance().getCommentManager();
        Comment lastComment=event.getComment();

        StringTokenizer tokenizer=new StringTokenizer(listOfNotRestrictedEvents,";");

        while (tokenizer.hasMoreTokens()) {
            currentToken = tokenizer.nextToken();
            Long longCurrentToken=new Long(currentToken);
            if (longCurrentToken.equals(event.getEventTypeId())) {
                restrictVisibility=false;
            }

        }

        if ((lastComment != null) && (lastComment.getRoleLevelId()==null)){
            CommentImpl lastCommentImpl=(CommentImpl) lastComment;
            if (restrictVisibility == true)
               lastCommentImpl.setRoleLevelId(restrictedRoleLevelId);
            else
                lastCommentImpl.setRoleLevelId(notRestrictedRoleLevelId);
            commentManager.update(lastCommentImpl,false);
        }


    }


    @Override
 public void issueCreated(IssueEvent event){
     restrictCommentVisibility(event);
	 log.debug("issueCreated");
 }

 
 public void customEvent(IssueEvent event){
     restrictCommentVisibility(event);
	 log.debug("customEvent");
 }
 
 public void issueAssigned(IssueEvent event){
     restrictCommentVisibility(event);
	 log.debug("issueAssigned");
 }

 public void issueClosed(IssueEvent event){
     restrictCommentVisibility(event);
     log.debug("issueClosed");
 }

 public void issueCommented(IssueEvent event){
     restrictCommentVisibility(event);
	 log.debug("issueCommented ");
 }

 public void issueCommentEdited(IssueEvent event){
     restrictCommentVisibility(event);
     log.debug("issueCommentEdited");
 }


 public void issueMoved(IssueEvent event){
     restrictCommentVisibility(event);
     log.debug("issueMoved ");
 }

 public void issueReopened(IssueEvent event){
     restrictCommentVisibility(event);
     log.debug("issueReopened");
 }

 public void issueResolved(IssueEvent event){
     restrictCommentVisibility(event);
     log.debug("issueResolved");
 }

 public void issueStarted(IssueEvent event){
     restrictCommentVisibility(event);
     log.debug("issueStarted");
 }

 public void issueStopped(IssueEvent event){
     restrictCommentVisibility(event);
     log.debug("issueStopped ");
 }

 public void issueUpdated(IssueEvent event){
     restrictCommentVisibility(event);
     log.debug("issueUpdated");
 }
 
}
