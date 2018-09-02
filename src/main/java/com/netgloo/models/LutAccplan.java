package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the LUT_ACCPLAN database table.
 * 
 */
@Entity
@Table(name="LUT_ACCPLAN")
@NamedQuery(name="LutAccplan.findAll", query="SELECT l FROM LutAccplan l")
public class LutAccplan implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	private String acccode;

	private String accname;

	private String fiscal;

	private String freebalance;

	public LutAccplan() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAcccode() {
		return this.acccode;
	}

	public void setAcccode(String acccode) {
		this.acccode = acccode;
	}

	public String getAccname() {
		return this.accname;
	}

	public void setAccname(String accname) {
		this.accname = accname;
	}

	public String getFiscal() {
		return this.fiscal;
	}

	public void setFiscal(String fiscal) {
		this.fiscal = fiscal;
	}

	public String getFreebalance() {
		return this.freebalance;
	}

	public void setFreebalance(String freebalance) {
		this.freebalance = freebalance;
	}

}