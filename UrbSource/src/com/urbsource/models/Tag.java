/**
 * 
 */
package com.urbsource.models;

import java.io.Serializable;

/**
 * The Tag DB model. Stores structured information about tags.
 * 
 * @author Mehmet Emre
 */
public class Tag implements Serializable {
	
	/**
	 * Compiler-generated serialization version UID.
	 */
	private static final long serialVersionUID = -6018630074302708032L;
	private int id;
	private String name;

	/**
	 * Default constructor. Needed for the class to be a bean.
	 */
	public Tag() {}
	
	/**
	 * Creates a {@link Tag} with given label and ID.
	 * 
	 * @param tag label of the tag
	 * @param id ID of the tag
	 */
	public Tag(String tag, int id) {
		this.setName(tag);
		this.setId(id);
	}

	/**
	 * Returns ID of this tag.
	 * 
	 * @return ID of this tag
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets ID of this tag to given value.
	 * 
	 * @param id ID to be set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Returns name of this tag.
	 * 
	 * @return name of this tag
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets name of this tag to given value.
	 * 
	 * @param id names to be set
	 */
	public void setName(String name) {
		// keep all tags lowercase and trimmed
		this.name = name.trim().toLowerCase();
	}

	/**
	 * Checks whether this tag is equal to given tag.
	 * 
	 * @param t tag to compare against
	 * @return result of equality test
	 */
	public boolean equals(Tag t) {
		return this.name == t.name;
	}
}
