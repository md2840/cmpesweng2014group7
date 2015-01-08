package com.urbsource.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Model representing a comment in database.
 * 
 * @author Mehmet Emre
 */
public class Experience implements Serializable {

	/**
	 * Compiler-generated serialization version UID.
	 */
	private static final long serialVersionUID = 9212137404203406823L;
	private int id=-1;
	private User author;
	private String text="";
	private String mood="good";
	private ArrayList<Tag> tags;
	private ArrayList<Tag> removedTags=new ArrayList<Tag>();
	private ArrayList<Tag> addedTags=new ArrayList<Tag>();
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
	private String source;
	private int points;
	private String location;

	/**
	 * Default constructor. Needed for the class to be a bean.
	 */
	public Experience() {
		creationTime = new Timestamp(new java.util.Date().getTime());
		modificationTime = new Timestamp(new java.util.Date().getTime());
		expirationDate = new Date(new java.util.Date().getTime());
		this.tags = new ArrayList<Tag>();
	}
	
	/**
	 * Creates an {@link Experience} with given id, author, text and tags.
	 * 
	 * @param id ID of the experience
	 * @param author author of the experience
	 * @param text content of the experience
	 * @param tags list of tags experience has
	 */
	public Experience(int id, User author, String text, List<Tag> tags) {
		this(author, text, tags);
		this.id = id;
	}

	/**
	 * Creates a new {@link Experience} with given author, text and tags.
	 * 
	 * @param author author of the new experience
	 * @param text content of the new experience
	 * @param tags list of tags the new experience has
	 */
	public Experience(User author, String text, List<Tag> tags) {
		this();
		this.text = text;
		this.tags = new ArrayList<Tag>(tags);
		this.author = author;
	}

	/**
	 * Creates an {@link Experience} with given id, author, text and tags.
	 * 
	 * @param id ID of the experience
	 * @param author author of the experience
	 * @param text content of the experience
	 * @param tags list of tags experience has
	 */
	public Experience(int id, User author, String text, Tag[] tags) {
		this(author, text, tags);
		this.id = id;
	}

	/**
	 * Creates a new {@link Experience} with given author, text and tags.
	 * 
	 * @param author author of the new experience
	 * @param text content of the new experience
	 * @param tags list of tags the new experience has
	 */
	public Experience(User author, String text, Tag[] tags) {
		this();
		this.text = text;
		this.tags = new ArrayList<Tag>(Arrays.asList(tags));
		this.author = author;
	}
	
	/**
	 * Returns ID of the experience.
	 * 
	 * @return ID of the experience
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets ID of the experience to given value
	 * 
	 * @param id ID to be set
	 * @return this experience
	 */
	public Experience setId(int id) {
		this.id = id;
		if (id < 0)
			this.saved = false;
		return this;
	}

	/**
	 * Returns content of the experience.
	 * 
	 * @return content of the experience
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets content of the experience to given value
	 * 
	 * @param id content to be set
	 * @return this experience
	 */
	public Experience setText(String text) {
		this.text = text;
		this.textChanged = true;
		this.saved = false;
		return this;
	}

	/**
	 * Returns whether the content of experience has changed.
	 * 
	 * @return whether the content of experience has changed
	 */
	public boolean isTextChanged() {
		return textChanged;
	}

	/**
	 * Returns whether the tags of experience has changed.
	 * 
	 * @return whether the tags of experience has changed
	 */
	public boolean isTagsChanged() {
		return !(addedTags.isEmpty() && removedTags.isEmpty());
	}

	/**
	 * Adds given tag to experience.
	 * 
	 * @param t tag to be added
	 * @return {@code true} if success, {@code false} otherwise
	 */
	public boolean addTag(Tag t) {
		if (tags.contains(t))
			return false;

		tags.add(t);
		
		if (removedTags.contains(t))
			removedTags.remove(t);
		else
			addedTags.add(t);

		saved = false;
		return true;
	}

	/**
	 * Removes given tag to experience.
	 * 
	 * @param t tag to be removed
	 * @return {@code true} if success, {@code false} otherwise
	 */
	public boolean removeTag(Tag t) {
		if (removedTags.contains(t))
			return false;

		tags.remove(t);
		
		if (addedTags.contains(t))
			addedTags.remove(t);
		else
			removedTags.add(t);
		
		saved = false;
		return true;
	}

	/**
	 * Returns list of tags that experience has.
	 * 
	 * @return list of tags that experience has
	 */
	public ArrayList<Tag> getTags() {
		return tags;
	}	

	/**
	 * Returns list of tags that are removed from experience
	 * 
	 * @return list of tags that are removed from experience
	 */
	public ArrayList<Tag> getRemovedTags() {
		return removedTags;
	}

	/**
	 * Returns list of tags that are added to experience
	 * 
	 * @return list of tags that are added to experience
	 */
	public ArrayList<Tag> getAddedTags() {
		return addedTags;
	}

	public User getAuthor() {
		return author;
	}

	/**
	 * Sets author of this experience to given value. To be used by a BeanPropertyRowMapper
	 * 
	 * @param authorId author ID for new experience
	 */
	public Experience setAuthor(User author) {
		this.author = author;
		return this;
	}

	/**
	 * Returns whether this experience is saved.
	 * 
	 * @return whether the experience is saved
	 */
	public boolean isSaved() {
		return saved;
	}

	/**
	 * Mark comment as saved.
	 * 
	 * @return this experience
	 */
	public Experience setAsSaved() {
		this.saved = true;
		return this;
	}

	/**
	 * Returns the mood of this experience.
	 * 
	 * @return mood of this experience
	 */
	public String getMood() {
		return mood;
	}

	/**
	 * Sets mood of this experience to given value.
	 * 
	 * @param mood mood to be set
	 * @return this experience
	 */
	public Experience setMood(String mood) {
		this.mood = mood.trim().toLowerCase();
		return this;
	}

	/**
	 * Returns the experience's spam count.
	 * 
	 * @return the experience's spam count
	 */
	public int getSpam() {
		return spam;
	}

	/**
	 * Sets the experience's spam count to given value.
	 * 
	 * @param spam spam count to be set
	 * @return this experience
	 */
	public Experience setSpam(int spam) {
		this.spam = spam;
		return this;
	}
	
	/**
	 * Returns whether experience exceeds spam limit.
	 * 
	 * @return whether experience exceeds spam limit
	 */
	public boolean exceedsSpamLimit() {
		return spam > SPAM_THRESHOLD;
	}

	/**
	 * Increments spam count
	 */
	public void incrementSpam() {
		this.spam++;
	}
	
	/**
	 * decrements spam count
	 */
	public void decrementSpam() {
		this.spam--;
	}

	/**
	 * Return creation time of this experience.
	 * 
	 * @return the creation time of this experience
	 */
	public Timestamp getCreationTime() {
		return creationTime;
	}

	/**
	 * Sets creation time of this experience to given value.
	 * 
	 * @param creationTime the creation time to set
	 */
	public Experience setCreationTime(Timestamp creationTime) {
		this.creationTime = creationTime;
		return this;
	}

	/**
	 * Returns modification time of this experience.
	 * 
	 * @return the modification time of this experience
	 */
	public Timestamp getModificationTime() {
		return modificationTime;
	}

	/**
	 * Sets modification time of this experience to given value.
	 * 
	 * @param modificationTime the modification time to set
	 * @return this experience
	 */
	public Experience setModificationTime(Timestamp modificationTime) {
		this.modificationTime = modificationTime;
		return this;
	}

	/**
	 * Returns expiration date of this experience.
	 * 
	 * @return the expiration date of this experience
	 */
	public Date getExpirationDate() {
		return expirationDate;
	}

	/**
	 * Sets expiration date of this experience to given value.
	 * 
	 * @param expirationDate the expiration date to set
	 * @return this experience
	 */
	public Experience setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
		return this;
	}

	/**
	 * @return whether user marked this experience as spam
	 */
	public boolean isUserMarkedSpam() {
		return userMarkedSpam;
	}

	/**
	 * Updates whether user marked this experience as spam
	 * 
	 * @param userMarkedSpam boolean value to indicate whether user marked this experience as spam
	 * @return this experience
	 */
	public Experience setUserMarkedSpam(boolean userMarkedSpam) {
		this.userMarkedSpam = userMarkedSpam;
		return this;
	}

	/**
	 * Returns the number of comments.
	 * 
	 * @return the number of comments
	 */
	public int getNumberOfComments() {
		return numberOfComments;
	}

	/**
	 * Sets the number of comments to given value.
	 * 
	 * @param numberOfComments the value which the number of comments to be set to
	 * @return this experience
	 */
	public Experience setNumberOfComments(int numberOfComments) {
		this.numberOfComments = numberOfComments;
		return this;
	}

	/**
	 * Returns whether the experience is upvoted by user.
	 * 
	 * @return whether the experience is upvoted by user
	 */
	public boolean isUpvotedByUser() {
		return upvotedByUser;
	}

	/**
	 * Updates the record of whether the experience is upvoted by user.
	 * 
	 * @param upvotedByUser boolean value indicating whether the experience is upvoted by user
	 * @return this experience
	 */
	public Experience setUpvotedByUser(boolean upvotedByUser) {
		this.upvotedByUser = upvotedByUser;
		return this;
	}

	/**
	 * Returns whether the experience is downvoted by user.
	 * 
	 * @return whether the experience is downvoted by user
	 */
	public boolean isDownvotedByUser() {
		return downvotedByUser;
	}

	/**
	 * Updates the record of whether the experience is downvoted by user.
	 * 
	 * @param downvotedByUser boolean value indicating whether the experience is downvoted by user
	 * @return this experience
	 */
	public Experience setDownvotedByUser(boolean downvotedByUser) {
		this.downvotedByUser = downvotedByUser;
		return this;
	}

	/**
	 * Returns a string indicating source of this experience (like search, popular etc.).
	 * 
	 * @return a string indicating source of this experience
	 */
	public String getSource() {
		return source;
	}

	/**
	 * Updates source of this experience to given value.
	 * 
	 * @param source string indicating new source of this experience
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * Returns how many points this experience has.
	 * 
	 * @return how many points this experience has
	 */
	public int getPoints() {
		return points;
	}

	/**
	 * Updates how many points this experience has.
	 * 
	 * @param points new value of how many points this experience has
	 * @return this experience
	 */
	public Experience setPoints(int points) {
		this.points = points;
		return this;
	}
	
	/**
	 * Returns names of tags this experience has as a comma-separated string.
	 * 
	 * @return names of tags this experience has as a comma-separated string
	 */
	public String getTagNames() {
		if (tags.size() == 0) {
			return "";
		}
		StringBuilder tagNames = new StringBuilder(tags.get(0).getName());
		for (int i = 1, iMax = tags.size(); i < iMax; ++i) {
			tagNames.append(',');
			tagNames.append(tags.get(i).getName());
		}
		return tagNames.toString();
	}
	
	public Experience setLocation(String location) {
		this.location = location;
		return this;
	}
	public String getLocation() {
		return location;
	}
	
}
