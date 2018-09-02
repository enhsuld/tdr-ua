package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the LNK_WORK_AU_TYPE database table.
 * 
 */
@Entity
@Table(name="LNK_WORK_AU_TYPE")
@NamedQuery(name="LnkWorkAuType.findAll", query="SELECT l FROM LnkWorkAuType l")
public class LnkWorkAuType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	private long workid;
	
	private long typeid;

	//bi-directional many-to-one association to LutAuditWork
	@ManyToOne
	@JoinColumn(name="workid",nullable = false,insertable=false,updatable=false)
	private LutAuditWork lutAuditWork;

	//bi-directional many-to-one association to LutReason
	@ManyToOne
	@JoinColumn(name="typeid",nullable = false,insertable=false,updatable=false)
	private LutReason lutReason;

	public LnkWorkAuType() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LutAuditWork getLutAuditWork() {
		return this.lutAuditWork;
	}

	public void setLutAuditWork(LutAuditWork lutAuditWork) {
		this.lutAuditWork = lutAuditWork;
	}

	public LutReason getLutReason() {
		return this.lutReason;
	}

	public void setLutReason(LutReason lutReason) {
		this.lutReason = lutReason;
	}

	public long getWorkid() {
		return workid;
	}

	public void setWorkid(long workid) {
		this.workid = workid;
	}

	public long getTypeid() {
		return typeid;
	}

	public void setTypeid(long typeid) {
		this.typeid = typeid;
	}
	
	

}