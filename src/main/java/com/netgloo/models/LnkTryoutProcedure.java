package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;


/**
 * The persistent class for the LNK_TRYOUT_PROCEDURE database table.
 * 
 */
@Entity
@Table(name="LNK_TRYOUT_PROCEDURE")
@NamedQuery(name="LnkTryoutProcedure.findAll", query="SELECT l FROM LnkTryoutProcedure l")
public class LnkTryoutProcedure implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	private long dirid;
	
	private long procedureid;
	
	private long tryoutid;

	//bi-directional many-to-one association to LutAuditDir
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	@JoinColumn(name="dirid",insertable=false,updatable=false)
	private LutAuditDir lutAuditDir;

	//bi-directional many-to-one association to LutProcedure
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	@JoinColumn(name="procedureid",insertable=false,updatable=false)
	private LutProcedure lutProcedure;

	//bi-directional many-to-one association to LutTryout
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	@JoinColumn(name="tryoutid",insertable=false,updatable=false)
	private LutTryout lutTryout;

	public LnkTryoutProcedure() {
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

	public LutProcedure getLutProcedure() {
		return this.lutProcedure;
	}

	public void setLutProcedure(LutProcedure lutProcedure) {
		this.lutProcedure = lutProcedure;
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

	public long getProcedureid() {
		return procedureid;
	}

	public void setProcedureid(long procedureid) {
		this.procedureid = procedureid;
	}

	public long getTryoutid() {
		return tryoutid;
	}

	public void setTryoutid(long tryoutid) {
		this.tryoutid = tryoutid;
	}
	
	

}