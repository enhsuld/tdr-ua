package com.netgloo.models;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;




/**
 * The persistent class for the LNK_MAIN_FORM_T2 database table.
 * 
 */
@Entity
@Table(name="LNK_MAIN_FORM_T2")
@NamedQuery(name="LnkMainFormT2.findAll", query="SELECT l FROM LnkMainFormT2 l")
public class LnkMainFormT2 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LNK_MAIN_FORM_T2_SEQ", sequenceName="LNK_MAIN_FORM_T2_SEQ",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="LNK_MAIN_FORM_T2_SEQ")
	@Column(name = "id", unique = true, nullable = false)
	private long id;

	private long decid;

	private String description;

	private long dirid;
	
	private String criname;

	private long factorid;

	private long riskid;
	
	private long mid;
	
	private int rtype;
	
	private Integer stepid;
	
	private Integer groupid;
	
	@Transient
	private String dirname;
	
	@Transient
	private String riskname;
	
	//bi-directional many-to-one association to LutAuditDir
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name="dirid", insertable=false, updatable=false)
	private LutAuditDir lutAuditDir;

	//bi-directional many-to-one association to LutFactor
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name="factorid", insertable=false, updatable=false)
	private LutFactor lutFactor;

	//bi-directional many-to-one association to LutFactorCriterion
/*	@ManyToOne
	@JsonBackReference
	@JoinColumn(name="critid", insertable=false, updatable=false)
	private LutFactorCriterion lutFactorCriterion;*/
	

	//bi-directional many-to-one association to MainAuditRegistration
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name="mid", insertable=false, updatable=false)
	private MainAuditRegistration mainAuditRegistration;

	//bi-directional many-to-one association to LnkRiskT2
	@OneToMany(mappedBy="lnkMainFormT2")
	@JsonBackReference
	private List<LnkRiskT2> lnkRiskT2s;
	
	public LnkMainFormT2() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getDecid() {
		return this.decid;
	}

	public void setDecid(long decid) {
		this.decid = decid;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getDirid() {
		return this.dirid;
	}

	public void setDirid(long dirid) {
		this.dirid = dirid;
	}

	public long getFactorid() {
		return this.factorid;
	}

	public void setFactorid(long factorid) {
		this.factorid = factorid;
	}
	
	public long getMid() {
		return mid;
	}

	public void setMid(long mid) {
		this.mid = mid;
	}

	public long getRiskid() {
		return this.riskid;
	}

	public void setRiskid(long riskid) {
		this.riskid = riskid;
	}
	
	
	public LutAuditDir getLutAuditDir() {
		return this.lutAuditDir;
	}

	public void setLutAuditDir(LutAuditDir lutAuditDir) {
		this.lutAuditDir = lutAuditDir;
	}
	
	public int getRtype() {
		return rtype;
	}

	public void setRtype(int rtype) {
		this.rtype = rtype;
	}
	
	public Integer getGroupid() {
		return groupid;
	}

	public void setGroupid(Integer groupid) {
		this.groupid = groupid;
	}

	public LutFactor getLutFactor() {
		return this.lutFactor;
	}

	public void setLutFactor(LutFactor lutFactor) {
		this.lutFactor = lutFactor;
	}



	/*	public LutFactorCriterion getLutFactorCriterion() {
		return this.lutFactorCriterion;
	}

	public void setLutFactorCriterion(LutFactorCriterion lutFactorCriterion) {
		this.lutFactorCriterion = lutFactorCriterion;
	}
*/
	public MainAuditRegistration getMainAuditRegistration() {
		return this.mainAuditRegistration;
	}

	public void setMainAuditRegistration(MainAuditRegistration mainAuditRegistration) {
		this.mainAuditRegistration = mainAuditRegistration;
	}
	
	public List<LnkRiskT2> getLnkRiskT2s() {
		return this.lnkRiskT2s;
	}

	public void setLnkRiskT2s(List<LnkRiskT2> lnkRiskT2s) {
		this.lnkRiskT2s = lnkRiskT2s;
	}

	public LnkRiskT2 addLnkRiskT2(LnkRiskT2 lnkRiskT2) {
		getLnkRiskT2s().add(lnkRiskT2);
		lnkRiskT2.setLnkMainFormT2(this);

		return lnkRiskT2;
	}

	public LnkRiskT2 removeLnkRiskT2(LnkRiskT2 lnkRiskT2) {
		getLnkRiskT2s().remove(lnkRiskT2);
		lnkRiskT2.setLnkMainFormT2(null);

		return lnkRiskT2;
	}

	public String getCriname() {
		return criname;
	}

	public void setCriname(String criname) {
		this.criname = criname;
	}

	public String getDirname() {
		return dirname;
	}

	public void setDirname(String dirname) {
		this.dirname = dirname;
	}

	public String getRiskname() {
		return riskname;
	}

	public void setRiskname(String riskname) {
		this.riskname = riskname;
	}

	public int getStepid() {
		return stepid;
	}

	public void setStepid(int stepid) {
		this.stepid = stepid;
	}
	
	
}