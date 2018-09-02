package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;


/**
 * The persistent class for the LNK_DIRECTION_NOTICE database table.
 * 
 */
@Entity
@Table(name="LNK_DIRECTION_NOTICE")
@NamedQuery(name="LnkDirectionNotice.findAll", query="SELECT l FROM LnkDirectionNotice l")
public class LnkDirectionNotice implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LNK_DIRECTION_NOTICE_SEQ", sequenceName="LNK_DIRECTION_NOTICE_SEQ", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="LNK_DIRECTION_NOTICE_SEQ")
	private long id;
	
	private long directionid;
	
	private long noticeid;

	//bi-directional many-to-one association to LutAuditDir
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name="directionid",insertable=false,updatable=false)
	private LutAuditDir lutAuditDir;

	//bi-directional many-to-one association to LutNotice
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name="noticeid",insertable=false,updatable=false)
	private LutNotice lutNotice;

	public LnkDirectionNotice() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LutAuditDir getLutAuditDir() {
		return this.lutAuditDir;
	}

	public void setLutAuditDir(LutAuditDir lutAuditDir) {
		this.lutAuditDir = lutAuditDir;
	}

	public LutNotice getLutNotice() {
		return this.lutNotice;
	}

	public void setLutNotice(LutNotice lutNotice) {
		this.lutNotice = lutNotice;
	}

	public long getDirectionid() {
		return directionid;
	}

	public void setDirectionid(long directionid) {
		this.directionid = directionid;
	}

	public long getNoticeid() {
		return noticeid;
	}

	public void setNoticeid(long noticeid) {
		this.noticeid = noticeid;
	}

	
	
	

}