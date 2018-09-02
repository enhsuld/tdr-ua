package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the LUT_ACT database table.
 * 
 */
@Entity
@Table(name="LUT_ACT")
@NamedQuery(name="LutAct.findAll", query="SELECT l FROM LutAct l")
public class LutAct implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	private String actdescribe;

	private String zaalt;

	public LutAct() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getActdescribe() {
		return this.actdescribe;
	}

	public void setActdescribe(String actdescribe) {
		this.actdescribe = actdescribe;
	}

	public String getZaalt() {
		return this.zaalt;
	}

	public void setZaalt(String zaalt) {
		this.zaalt = zaalt;
	}

}