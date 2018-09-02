package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the LUT_AUDIT_LEVEL database table.
 * 
 */
@Entity
@Table(name="LUT_AUDIT_LEVEL")
@NamedQuery(name="LutAuditLevel.findAll", query="SELECT l FROM LutAuditLevel l")
public class LutAuditLevel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LUT_AUDIT_LEVEL", sequenceName="LUT_AUDIT_LEVEL_SEQ")
	@GeneratedValue(strategy=GenerationType.AUTO) 
	//@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="LUT_AUDIT_LEVEL_SEQ")
	@Column(name = "id", unique = true, nullable = false)
	private long id;

	private String levelname;

	public LutAuditLevel() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLevelname() {
		return this.levelname;
	}

	public void setLevelname(String levelname) {
		this.levelname = levelname;
	}

}