package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the LNK_WORK_LEVEL database table.
 * 
 */
@Entity
@Table(name="LNK_WORK_LEVEL")
@NamedQuery(name="LnkWorkLevel.findAll", query="SELECT l FROM LnkWorkLevel l")
public class LnkWorkLevel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	private long levelid;
	
	private long workid;

	//bi-directional many-to-one association to LutAuditWork
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="workid",nullable = false,insertable=false,updatable=false)
	private LutAuditWork lutAuditWork;

	public LnkWorkLevel() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getLevelid() {
		return this.levelid;
	}

	public void setLevelid(long levelid) {
		this.levelid = levelid;
	}

	public LutAuditWork getLutAuditWork() {
		return this.lutAuditWork;
	}

	public void setLutAuditWork(LutAuditWork lutAuditWork) {
		this.lutAuditWork = lutAuditWork;
	}

	public long getWorkid() {
		return workid;
	}

	public void setWorkid(long workid) {
		this.workid = workid;
	}
	
	

}