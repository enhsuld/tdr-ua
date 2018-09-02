package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;


/**
 * The persistent class for the LNK_DIRECTION_RISK database table.
 * 
 */
@Entity
@Table(name="LNK_DIRECTION_RISK")
@NamedQuery(name="LnkDirectionRisk.findAll", query="SELECT l FROM LnkDirectionRisk l")
public class LnkDirectionRisk implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LNK_DIRECTION_RISK_SEQ", sequenceName="LNK_DIRECTION_RISK_SEQ", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="LNK_DIRECTION_RISK_SEQ")
	private long id;

	private long directionid;
	
	private long riskid;
	
	//bi-directional many-to-one association to LutAuditDir
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name="directionid",insertable=false,updatable=false)
	private LutAuditDir lutAuditDir;

	//bi-directional many-to-one association to LutRisk
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name="riskid",insertable=false,updatable=false)
	private LutRisk lutRisk;

	public LnkDirectionRisk() {
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

	public long getDirectionid() {
		return directionid;
	}

	public void setDirectionid(long directionid) {
		this.directionid = directionid;
	}

	public long getRiskid() {
		return riskid;
	}

	public void setRiskid(long riskid) {
		this.riskid = riskid;
	}
	
	

}