package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;


/**
 * The persistent class for the LNK_RISKDIR database table.
 * 
 */
@Entity
@Table(name="LNK_RISK_LAW")
@NamedQuery(name="LnkRiskLaw.findAll", query="SELECT l FROM LnkRiskLaw l")
public class LnkRiskLaw implements Serializable {
	@Override
	public String toString() {
		return "LnkRiskLaw [id=" + id + ", lawid=" + lawid + ", riskid=" + riskid + ", zuilid=" + zuilid + ", zaalt="
				+ zaalt + ", lname=" + lname + ", lzuil=" + lzuil + ", lzaalt=" + lzaalt + ", lawcategory="
				+ lawcategory + ", lutLaw=" + lutLaw + ", lutRisk=" + lutRisk + "]";
	}

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LNK_RISK_LAW_SEQ", sequenceName="LNK_RISK_LAW_SEQ",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="LNK_RISK_LAW_SEQ")
	@Column(name = "id", unique = true, nullable = false)
	private long id;
	
	private long lawid;
	
	private long riskid;
	
	private long zuilid;
	private long zaalt;
	
	private String lname;
	private String lzuil;
	private String lzaalt;
	
	private long lawcategory;

	//bi-directional many-to-one association to LutLaws
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name="lawid",insertable=false,updatable=false)
	private LutLaw lutLaw;

	//bi-directional many-to-one association to LutRisk
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name="riskid",insertable=false,updatable=false)
	private LutRisk lutRisk;

	public LnkRiskLaw() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LutLaw getLutLawr() {
		return this.lutLaw;
	}

	public void setLutLaw(LutLaw lutLaw) {
		this.lutLaw = lutLaw;
	}

	public LutRisk getLutRisk() {
		return this.lutRisk;
	}

	public void setLutRisk(LutRisk lutRisk) {
		this.lutRisk = lutRisk;
	}

	public long getLawid() {
		return lawid;
	}

	public void setLawid(long lawid) {
		this.lawid = lawid;
	}

	public long getRiskid() {
		return riskid;
	}

	public void setRiskid(long riskid) {
		this.riskid = riskid;
	}

	public long getZuilid() {
		return zuilid;
	}

	public void setZuilid(long zuilid) {
		this.zuilid = zuilid;
	}

	public long getZaalt() {
		return zaalt;
	}

	public void setZaalt(long zaalt) {
		this.zaalt = zaalt;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getLzuil() {
		return lzuil;
	}

	public void setLzuil(String lzuil) {
		this.lzuil = lzuil;
	}

	public String getLzaalt() {
		return lzaalt;
	}

	public void setLzaalt(String lzaalt) {
		this.lzaalt = lzaalt;
	}

	public long getLawcategory() {
		return lawcategory;
	}

	public void setLawcategory(long lawcategory) {
		this.lawcategory = lawcategory;
	}

	public LutLaw getLutLaw() {
		return lutLaw;
	}
	
	

}