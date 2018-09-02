package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;


/**
 * The persistent class for the LNK_RISKDIR database table.
 * 
 */
@Entity
@Table(name="LNK_RISKDIR")
@NamedQuery(name="LnkRiskdir.findAll", query="SELECT l FROM LnkRiskdir l")
public class LnkRiskdir implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LINK_RISKDIR_SEQ", sequenceName="LINK_RISKDIR_SEQ",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="LINK_RISKDIR_SEQ")
	@Column(name = "id", unique = true, nullable = false)
	private long id;
	
	private long dirid;
	
	private long riskid;

	//bi-directional many-to-one association to LutAuditDir
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name="dirid",insertable=false,updatable=false)
	private LutAuditDir lutAuditDir;

	//bi-directional many-to-one association to LutRisk
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name="riskid",insertable=false,updatable=false)
	private LutRisk lutRisk;

	public LnkRiskdir() {
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
	
	

}