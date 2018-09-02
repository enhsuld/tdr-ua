package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the LNK_WORK_CATEGORY database table.
 * 
 */
@Entity
@Table(name="LNK_WORK_CATEGORY")
@NamedQuery(name="LnkWorkCategory.findAll", query="SELECT l FROM LnkWorkCategory l")
public class LnkWorkCategory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LNK_WORK_CATEGORY_SEQ", sequenceName="LNK_WORK_CATEGORY_SEQ",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="LNK_WORK_CATEGORY_SEQ")
	private long id;

	private long catid;
	
	private long workid;

	//bi-directional many-to-one association to LutAuditWork
	@ManyToOne
	@OrderBy("orderid")
	@JoinColumn(name="workid",nullable = false,insertable=false,updatable=false)
	private LutAuditWork lutAuditWork;

	public LnkWorkCategory() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCatid() {
		return this.catid;
	}

	public void setCatid(long catid) {
		this.catid = catid;
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