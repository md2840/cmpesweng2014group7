package com.group7.urbsourceandroid.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.sql.Date;
import java.sql.Timestamp;

public class Experience implements Serializable {

    /**
     * Compiler-generated serialization version UID.
     */
    private static final long serialVersionUID = 9212137404203406823L;
    private int id=-1;
    private User author;
    private String text;
    private String mood="good";
    private ArrayList<Tag> tags;
    private boolean textChanged=false;
    private boolean saved=false;
    private int spam = 0;
    public final int SPAM_THRESHOLD = 5000;
    private Timestamp creationTime;
    private Timestamp modificationTime;
    private Date expirationDate;
    private boolean userMarkedSpam=false;
    private int numberOfComments;
    private boolean upvotedByUser=false;
    private boolean downvotedByUser=false;
    private String location;



    public Experience() {

    }

    public Experience(int id, User author, String text, List<Tag> tags, String location) {
        this(author, text, tags,location);
        this.id = id;
    }

    public Experience(User author, String text, List<Tag> tags,String location) {
        this();
        this.text = text;
        this.tags = new ArrayList<Tag>(tags);
        this.author = author;
        this.location=location;
    }

    public Experience(int id, User author, String text, Tag[] tags, String location) {
        this(author, text, tags, location);
        this.id = id;
    }

    public Experience(User author, String text, Tag[] tags,String location) {
        this();
        this.text = text;
        this.tags = new ArrayList<Tag>(Arrays.asList(tags));
        this.author = author;
        this.location=location;
    }

    public int getId() {
        return id;
    }

    public Experience setId(int id) {
        this.id = id;
        if (id < 0)
            this.saved = false;
        return this;
    }

    public String getText() {
        return text;
    }

    public Experience setText(String text) {
        this.text = text;
        this.textChanged = true;
        return this;
    }
    public String getLocation() {
        return location;
    }

    public Experience setLocation(String location) {
        this.location = location;
        return this;
    }

    public boolean isTextChanged() {
        return textChanged;
    }



    public boolean addTag(Tag t) {
        if(tags==null){
            tags= new ArrayList<Tag>();
            tags.add(t);
            return true;
        }else{
            if (tags.contains(t))
                return false;

            tags.add(t);

            return true;
        }
    }


    public ArrayList<Tag> getTags() {
        return tags;
    }

    public User getAuthor() {
        return author;
    }

    /**
     * To be used by a BeanPropertyRowMapper
     *
     * @param  author ID for new experience
     */
    public Experience setAuthor(User author) {
        this.author = author;
        return this;
    }


    public boolean isSaved() {
        return saved;
    }

    public Experience setAsSaved() {
        this.saved = true;
        return this;
    }

    public String getMood() {
        return mood;
    }

    public Experience setMood(String mood) {
        this.mood = mood.trim().toLowerCase();
        return this;
    }

    public int getSpam() {
        return spam;
    }

    public Experience setSpam(int spam) {
        this.spam = spam;
        return this;
    }

    public boolean exceedsSpamLimit() {
        return spam > SPAM_THRESHOLD;
    }

    public void incrementSpam() {
        this.spam++;
    }

    public void decrementSpam() {
        this.spam--;
    }

    /**
     * @return the creationTime
     */
    public Timestamp getCreationTime() {
        return creationTime;
    }

    /**
     * @param creationTime the creationTime to set
     */
    public Experience setCreationTime(Timestamp creationTime) {
        this.creationTime = creationTime;
        return this;
    }

    /**
     * @return the modificationTime
     */
    public Timestamp getModificationTime() {
        return modificationTime;
    }

    /**
     * @param modificationTime the modificationTime to set
     */
    public Experience setModificationTime(Timestamp modificationTime) {
        this.modificationTime = modificationTime;
        return this;
    }

    /**
     * @return the expirationDate
     */
    public Date getExpirationDate() {
        return expirationDate;
    }

    /**
     * @param expirationDate the expirationDate to set
     */
    public Experience setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    /**
     * @return the userMarkedSpam
     */
    public boolean isUserMarkedSpam() {
        return userMarkedSpam;
    }

    /**
     * @param userMarkedSpam the userMarkedSpam to set
     */
    public Experience setUserMarkedSpam(boolean userMarkedSpam) {
        this.userMarkedSpam = userMarkedSpam;
        return this;
    }

    public int getNumberOfComments() {
        return numberOfComments;
    }

    public void setNumberOfComments(int numberOfComments) {
        this.numberOfComments = numberOfComments;
    }

    public boolean isUpvotedByUser() {
        return upvotedByUser;
    }

    public void setUpvotedByUser(boolean upvotedByUser) {
        this.upvotedByUser = upvotedByUser;
    }

    public boolean isDownvotedByUser() {
        return downvotedByUser;
    }

    public void setDownvotedByUser(boolean downvotedByUser) {
        this.downvotedByUser = downvotedByUser;
    }

}
