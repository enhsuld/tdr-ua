package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;


/**
 * The persistent class for the LNK_TRYOUT_NOTICE database table.
 * 
 */
@Entity
@Table(name="LNK_TRYOUT_NOTICE")
@NamedQuery(name="LnkTryoutNotice.findAll", query="SELECT l FROM LnkTryoutNotice l")
public class LnkTryoutNotice implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	private long dirid;
	
	private long noticeid;
	
	private long tryoutid;

	//bi-directional many-to-one association to LutAuditDir
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	@JoinColumn(name="dirid",insertable=false,updatable=false)
	private LutAuditDir lutAuditDir;

	//bi-directional many-to-one association to LutNotice
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	@JoinColumn(name="dirid",insertable=false,updatable=false)
	private LutNotice lutNotice;

	//bi-directional many-to-one association to LutTryout
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	@JoinColumn(name="tryoutid",insertable=false,updatable=false)
	private LutTryout lutTryout;

	public LnkTryoutNotice() {
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

	public LutTryout getLutTryout() {
		return this.lutTryout;
	}

	public void setLutTryout(LutTryout lutTryout) {
		this.lutTryout = lutTryout;
	}

	public long getDirid() {
		return dirid;
	}

	public void setDirid(long dirid) {
		this.dirid = dirid;
	}

	public long getNoticeid() {
		return noticeid;
	}

	public void setNoticeid(long noticeid) {
		this.noticeid = noticeid;
	}

	public long getTryoutid() {
		return tryoutid;
	}

	public void setTryoutid(long tryoutid) {
		this.tryoutid = tryoutid;
	}
	
	

}