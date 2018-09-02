package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the LUT_REQUIREMENT database table.
 * 
 */
@Entity
@Table(name="LUT_REQUIREMENT")
@NamedQuery(name="LutRequirement.findAll", query="SELECT l FROM LutRequirement l")
public class LutRequirement implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	
	private String adescribe;

	private String zaalt;

	public LutRequirement() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAdescribe() {
		return this.adescribe;
	}

	public void setAdescribe(String adescribe) {
		this.adescribe = adescribe;
	}

	public String getZaalt() {
		return this.zaalt;
	}

	public void setZaalt(String zaalt) {
		this.zaalt = zaalt;
	}

}