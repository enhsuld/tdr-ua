package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the LUT_FINBLCNAME database table.
 * 
 */
@Entity
@Table(name="LUT_FINBLCNAME")
@NamedQuery(name="LutFinblcname.findAll", query="SELECT l FROM LutFinblcname l")
public class LutFinblcname implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	private String code;

	private String name;

	public LutFinblcname() {
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