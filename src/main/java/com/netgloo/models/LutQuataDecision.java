package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the LUT_QUATA_DECISION database table.
 * 
 */
@Entity
@Table(name="LUT_QUATA_DECISION")
@NamedQuery(name="LutQuataDecision.findAll", query="SELECT l FROM LutQuataDecision l")
public class LutQuataDecision implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	private String name;

	public LutQuataDecision() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}