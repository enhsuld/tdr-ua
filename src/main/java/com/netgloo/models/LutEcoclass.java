package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the LUT_ECOCLASS database table.
 * 
 */
@Entity
@Table(name="LUT_ECOCLASS")
@NamedQuery(name="LutEcoclass.findAll", query="SELECT l FROM LutEcoclass l")
public class LutEcoclass implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	private String code;

	private String name;

	public LutEcoclass() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}