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

	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	
	public Tag(String tag, int id) {
		this.setName(tag);
		this.setId(id);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		// keep all tags lowercase and trimmed
		this.name = name.trim().toLowerCase();
	}

}
