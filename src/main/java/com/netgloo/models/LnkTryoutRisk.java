package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;


/**
 * The persistent class for the LNK_TRYOUT_RISK database table.
 * 
 */
@Entity
@Table(name="LNK_TRYOUT_RISK")
@NamedQuery(name="LnkTryoutRisk.findAll", query="SELECT l FROM LnkTryoutRisk l")
public class LnkTryoutRisk implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LNK_TRYOUT_RISK_SEQ", sequenceName="LNK_TRYOUT_RISK_SEQ",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="LNK_TRYOUT_RISK_SEQ")
	@Column(name = "id", unique = true, nullable = false)
	private long id;
	
	private long dirid;
	
	private long riskid;
	
	private long tryoutid;

	//bi-directional many-to-one association to LutAuditDir
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	@JoinColumn(name="dirid",insertable=false,updatable=false)
	private LutAuditDir lutAuditDir;

	//bi-directional many-to-one association to LutRisk
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	@JoinColumn(name="riskid",insertable=false,updatable=false)
	private LutRisk lutRisk;

	//bi-directional many-to-one association to LutTryout
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	@JoinColumn(name="tryoutid",insertable=false,updatable=false)
	private LutTryout lutTryout;

	public LnkTryoutRisk() {
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

	public LutRisk getLutRisk() {
		return this.lutRisk;
	}

	public void setLutRisk(LutRisk lutRisk) {
		this.lutRisk = lutRisk;
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

	public long getRiskid() {
		return riskid;
	}

	public void setRiskid(long riskid) {
		this.riskid = riskid;
	}

	public long getTryoutid() {
		return tryoutid;
	}

	public void setTryoutid(long tryoutid) {
		this.tryoutid = tryoutid;
	}

	
	
}