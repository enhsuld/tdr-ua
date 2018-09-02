package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the LUT_DECISIONS database table.
 * 
 */
@Entity
@Table(name="LUT_AUDITRESULTS")
@NamedQuery(name="LutAuditResults.findAll", query="SELECT l FROM LutAuditResults l")
public class LutAuditResults implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	private String resultname;

	public LutAuditResults() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getResultname() {
		return this.resultname;
	}

	public void setResultname(String resultname) {
		this.resultname = resultname;
	}

}