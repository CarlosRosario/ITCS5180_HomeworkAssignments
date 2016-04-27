package com.example.group26.stayintouchwithfragments;

/**
 * Created by Carlos on 4/27/2016.
 */
public class Conversation {

    private String conversationID;
    private String participant1;
    private String participant2;
    private String participant1Email;
    private String participant2Email;
    private String deletedBy;
    private String isArchived_by_participant1;
    private String isArchived_by_participant2;

    public String getParticipant1Email() {
        return participant1Email;
    }

    public void setParticipant1Email(String participant1Email) {
        this.participant1Email = participant1Email;
    }

    public String getParticipant2Email() {
        return participant2Email;
    }

    public void setParticipant2Email(String participant2Email) {
        this.participant2Email = participant2Email;
    }

    public String getConversationID() {
        return conversationID;
    }

    public void setConversationID(String conversationID) {
        this.conversationID = conversationID;
    }

    public String getParticipant1() {
        return participant1;
    }

    public void setParticipant1(String participant1) {
        this.participant1 = participant1;
    }

    public String getParticipant2() {
        return participant2;
    }

    public void setParticipant2(String participant2) {
        this.participant2 = participant2;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public String getIsArchived_by_participant1() {
        return isArchived_by_participant1;
    }

    public void setIsArchived_by_participant1(String isArchived_by_participant1) {
        this.isArchived_by_participant1 = isArchived_by_participant1;
    }

    public String getIsArchived_by_participant2() {
        return isArchived_by_participant2;
    }

    public void setIsArchived_by_participant2(String isArchived_by_participant2) {
        this.isArchived_by_participant2 = isArchived_by_participant2;
    }
}
