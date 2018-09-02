package com.netgloo.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.netgloo.models.LutAuditDir;



/**
 * The persistent class for the LUT_FACTOR database table.
 * 
 */
@Entity
@Table(name="LUT_FACTOR")
@NamedQuery(name="LutFactor.findAll", query="SELECT l FROM LutFactor l")
public class LutFactor implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LUT_FACTOR_SEQ", sequenceName="LUT_FACTOR_SEQ", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="LUT_FACTOR_SEQ")
	private long id;

	private String factorname;
	
	private long groupid;

	private String fnumber;
	
	private int gorderid;
	
	private long dirid;
	
	private long decid;
	
	private long riskid;
	
	private long critid;
	
	private long tryid;
	
	private long isactive;
	
	private String description;
	
	private String crition;
	
	@Transient
	private int rtype;
	
	@Transient
	private String risknames;
	
	@Transient
	private String treatment;
	
	@Transient
	private List risks;
	
	@Transient
	private String criname;
	
	@Transient
	private String dirname;
	
	
/*	@OneToMany(mappedBy="lutFactor")
	@JsonBackReference
	private List<LnkMainFormT2> lnkMainFormT2s;*/
	
	//bi-directional many-to-one association to LnkFactorCategory
	@OneToMany(mappedBy="lutFactor")
	private List<LnkFactorCategory> lnkFactorCategories;
	
	//bi-directional many-to-one association to LutFactorCriterion
	@OneToMany(mappedBy="lutFactor")
	@JsonBackReference
	private List<LutFactorCriterion> lutFactorCriterions;

	//bi-directional many-to-one association to LutAuditDir
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name="dirid",insertable=false,updatable=false)
	private LutAuditDir lutAuditDir;
	
	//bi-directional many-to-one association to LutGroupOfFactor
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name="groupid",insertable=false,updatable=false)
	private LutGroupOfFactor lutGroupOfFactor;
	
	private int orderid;

	public LutFactor() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getRtype() {
		return rtype;
	}

	public void setRtype(int rtype) {
		this.rtype = rtype;
	}

	public String getFactorname() {
		return this.factorname;
	}

	public void setFactorname(String factorname) {
		this.factorname = factorname;
	}

	public LutAuditDir getLutAuditDir() {
		return this.lutAuditDir;
	}

	public void setLutAuditDir(LutAuditDir lutAuditDir) {
		this.lutAuditDir = lutAuditDir;
	}

	public long getDirid() {
		return dirid;
	}

	public void setDirid(long dirid) {
		this.dirid = dirid;
	}
	
	public LutGroupOfFactor getLutGroupOfFactor() {
		return this.lutGroupOfFactor;
	}

	public void setLutGroupOfFactor(LutGroupOfFactor lutGroupOfFactor) {
		this.lutGroupOfFactor = lutGroupOfFactor;
	}

	public long getGroupid() {
		return groupid;
	}

	
	
	public String getTreatment() {
		return treatment;
	}

	public void setTreatment(String treatment) {
		this.treatment = treatment;
	}

	public void setGroupid(long groupid) {
		this.groupid = groupid;
	}

	public int getOrderid() {
		return orderid;
	}

	public void setOrderid(int orderid) {
		this.orderid = orderid;
	}

	public String getFnumber() {
		return fnumber;
	}

	public void setFnumber(String fnumber) {
		this.fnumber = fnumber;
	}

	public int getGorderid() {
		return gorderid;
	}

	public void setGorderid(int gorderid) {
		this.gorderid = gorderid;
	}


	public long getDecid() {
		return decid;
	}

	public void setDecid(long decid) {
		this.decid = decid;
	}

	public long getRiskid() {
		return riskid;
	}

	public void setRiskid(long riskid) {
		this.riskid = riskid;
	}

	public long getCritid() {
		return critid;
	}

	public void setCritid(long critid) {
		this.critid = critid;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getCrition() {
		return crition;
	}

	public void setCrition(String crition) {
		this.crition = crition;
	}

	public List<LutFactorCriterion> getLutFactorCriterions() {
		return this.lutFactorCriterions;
	}

	public void setLutFactorCriterions(List<LutFactorCriterion> lutFactorCriterions) {
		this.lutFactorCriterions = lutFactorCriterions;
	}

	public LutFactorCriterion addLutFactorCriterion(LutFactorCriterion lutFactorCriterion) {
		getLutFactorCriterions().add(lutFactorCriterion);
		lutFactorCriterion.setLutFactor(this);

		return lutFactorCriterion;
	}

	public LutFactorCriterion removeLutFactorCriterion(LutFactorCriterion lutFactorCriterion) {
		getLutFactorCriterions().remove(lutFactorCriterion);
		lutFactorCriterion.setLutFactor(null);

		return lutFactorCriterion;
	}

	public String getRisknames() {
		return risknames;
	}

	public void setRisknames(String risknames) {
		this.risknames = risknames;
	}

	public List getRisks() {
		return risks;
	}

	public void setRisks(List risks) {
		this.risks = risks;
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
	
/*	public List<LnkMainFormT2> getLnkMainFormT2s() {
		return this.lnkMainFormT2s;
	}

	public void setLnkMainFormT2s(List<LnkMainFormT2> lnkMainFormT2s) {
		this.lnkMainFormT2s = lnkMainFormT2s;
	}*/
	
	public long getTryid() {
		return tryid;
	}

	public void setTryid(long tryid) {
		this.tryid = tryid;
	}

	public long getIsactive() {
		return isactive;
	}

	public void setIsactive(long isactive) {
		this.isactive = isactive;
	}

	public List<LnkFactorCategory> getLnkFactorCategories() {
		return this.lnkFactorCategories;
	}

	public void setLnkFactorCategories(List<LnkFactorCategory> lnkFactorCategories) {
		this.lnkFactorCategories = lnkFactorCategories;
	}

	public LnkFactorCategory addLnkFactorCategory(LnkFactorCategory lnkFactorCategory) {
		getLnkFactorCategories().add(lnkFactorCategory);
		lnkFactorCategory.setLutFactor(this);

		return lnkFactorCategory;
	}

	public LnkFactorCategory removeLnkFactorCategory(LnkFactorCategory lnkFactorCategory) {
		getLnkFactorCategories().remove(lnkFactorCategory);
		lnkFactorCategory.setLutFactor(null);

		return lnkFactorCategory;
	}
}