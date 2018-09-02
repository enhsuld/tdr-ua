package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.netgloo.models.LnkDirectionNotice;
import com.netgloo.models.LnkDirectionProcedure;
import com.netgloo.models.LnkDirectionRisk;

import com.netgloo.models.LnkRiskdir;

import com.netgloo.models.LnkTryoutNotice;
import com.netgloo.models.LnkTryoutProcedure;
import com.netgloo.models.LnkTryoutRisk;


import java.util.List;


/**
 * The persistent class for the LUT_AUDIT_DIR database table.
 * 
 */
@Entity
@Table(name="LUT_AUDIT_DIR")
@NamedQuery(name="LutAuditDir.findAll", query="SELECT l FROM LutAuditDir l")
public class LutAuditDir implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="LUT_AUDIT_DIR_SEQ", sequenceName="LUT_AUDIT_DIR_SEQ", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="LUT_AUDIT_DIR_SEQ")
	private long id;

	private String name;

	private String shortname;
	
	/*//bi-directional many-to-one association to LutAuditWork
	@OneToMany(mappedBy="lutAuditDir")
	@JsonManagedReference
	private List<LutAuditWork> lutAuditWorks;*/
	
	//bi-directional many-to-one association to LnkDirectionNotice
	@OneToMany(mappedBy="lutAuditDir")
	@JsonManagedReference
	private List<LnkDirectionNotice> lnkDirectionNotices;
	
	//bi-directional many-to-one association to LnkDirectionProcedure
	@OneToMany(mappedBy="lutAuditDir")
	@JsonManagedReference
	private List<LnkDirectionProcedure> lnkDirectionProcedures;
	
	//bi-directional many-to-one association to LnkDirectionRisk
	@OneToMany(mappedBy="lutAuditDir")
	@JsonManagedReference
	private List<LnkDirectionRisk> lnkDirectionRisks;
	
	//bi-directional many-to-one association to LnkRiskdir
	@OneToMany(mappedBy="lutAuditDir")
	@JsonManagedReference
	private List<LnkRiskdir> lnkRiskdirs;
	
	//bi-directional many-to-one association to LnkTryoutNotice
	@OneToMany(mappedBy="lutAuditDir")
	@JsonManagedReference
	private List<LnkTryoutNotice> lnkTryoutNotices;

	//bi-directional many-to-one association to LnkTryoutProcedure
	@OneToMany(mappedBy="lutAuditDir")
	@JsonManagedReference
	private List<LnkTryoutProcedure> lnkTryoutProcedures;

	//bi-directional many-to-one association to LnkTryoutRisk
	@OneToMany(mappedBy="lutAuditDir")
	@JsonManagedReference
	private List<LnkTryoutRisk> lnkTryoutRisks;
	
	//bi-directional many-to-one association to LutFactor
	@OneToMany(mappedBy="lutAuditDir")
	@JsonManagedReference
	private List<LutFactor> lutFactors;
	
	//bi-directional many-to-one association to LnkMainFormT2
	@OneToMany(mappedBy="lutAuditDir")
	@JsonBackReference
	private List<LnkMainFormT2> lnkMainFormT2s;

	public LutAuditDir() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortname() {
		return this.shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

	/*public List<LutAuditWork> getLutAuditWorks() {
		return this.lutAuditWorks;
	}

	public void setLutAuditWorks(List<LutAuditWork> lutAuditWorks) {
		this.lutAuditWorks = lutAuditWorks;
	}

	public LutAuditWork addLutAuditWork(LutAuditWork lutAuditWork) {
		getLutAuditWorks().add(lutAuditWork);
		lutAuditWork.setLutAuditDir(this);

		return lutAuditWork;
	}

	public LutAuditWork removeLutAuditWork(LutAuditWork lutAuditWork) {
		getLutAuditWorks().remove(lutAuditWork);
		lutAuditWork.setLutAuditDir(null);

		return lutAuditWork;
	}*/
	
	public List<LnkDirectionNotice> getLnkDirectionNotices() {
		return this.lnkDirectionNotices;
	}

	public void setLnkDirectionNotices(List<LnkDirectionNotice> lnkDirectionNotices) {
		this.lnkDirectionNotices = lnkDirectionNotices;
	}

	public LnkDirectionNotice addLnkDirectionNotice(LnkDirectionNotice lnkDirectionNotice) {
		getLnkDirectionNotices().add(lnkDirectionNotice);
		lnkDirectionNotice.setLutAuditDir(this);

		return lnkDirectionNotice;
	}

	public LnkDirectionNotice removeLnkDirectionNotice(LnkDirectionNotice lnkDirectionNotice) {
		getLnkDirectionNotices().remove(lnkDirectionNotice);
		lnkDirectionNotice.setLutAuditDir(null);

		return lnkDirectionNotice;
	}
	
	public List<LnkDirectionProcedure> getLnkDirectionProcedures() {
		return this.lnkDirectionProcedures;
	}

	public void setLnkDirectionProcedures(List<LnkDirectionProcedure> lnkDirectionProcedures) {
		this.lnkDirectionProcedures = lnkDirectionProcedures;
	}

	public LnkDirectionProcedure addLnkDirectionProcedure(LnkDirectionProcedure lnkDirectionProcedure) {
		getLnkDirectionProcedures().add(lnkDirectionProcedure);
		lnkDirectionProcedure.setLutAuditDir(this);

		return lnkDirectionProcedure;
	}

	public LnkDirectionProcedure removeLnkDirectionProcedure(LnkDirectionProcedure lnkDirectionProcedure) {
		getLnkDirectionProcedures().remove(lnkDirectionProcedure);
		lnkDirectionProcedure.setLutAuditDir(null);

		return lnkDirectionProcedure;
	}
	
	public List<LnkDirectionRisk> getLnkDirectionRisks() {
		return this.lnkDirectionRisks;
	}

	public void setLnkDirectionRisks(List<LnkDirectionRisk> lnkDirectionRisks) {
		this.lnkDirectionRisks = lnkDirectionRisks;
	}

	public LnkDirectionRisk addLnkDirectionRisk(LnkDirectionRisk lnkDirectionRisk) {
		getLnkDirectionRisks().add(lnkDirectionRisk);
		lnkDirectionRisk.setLutAuditDir(this);

		return lnkDirectionRisk;
	}

	public LnkDirectionRisk removeLnkDirectionRisk(LnkDirectionRisk lnkDirectionRisk) {
		getLnkDirectionRisks().remove(lnkDirectionRisk);
		lnkDirectionRisk.setLutAuditDir(null);

		return lnkDirectionRisk;
	}
	
	public List<LnkRiskdir> getLnkRiskdirs() {
		return this.lnkRiskdirs;
	}

	public void setLnkRiskdirs(List<LnkRiskdir> lnkRiskdirs) {
		this.lnkRiskdirs = lnkRiskdirs;
	}

	public LnkRiskdir addLnkRiskdir(LnkRiskdir lnkRiskdir) {
		getLnkRiskdirs().add(lnkRiskdir);
		lnkRiskdir.setLutAuditDir(this);

		return lnkRiskdir;
	}

	public LnkRiskdir removeLnkRiskdir(LnkRiskdir lnkRiskdir) {
		getLnkRiskdirs().remove(lnkRiskdir);
		lnkRiskdir.setLutAuditDir(null);

		return lnkRiskdir;
	}
	public List<LnkTryoutNotice> getLnkTryoutNotices() {
		return this.lnkTryoutNotices;
	}

	public void setLnkTryoutNotices(List<LnkTryoutNotice> lnkTryoutNotices) {
		this.lnkTryoutNotices = lnkTryoutNotices;
	}

	public LnkTryoutNotice addLnkTryoutNotice(LnkTryoutNotice lnkTryoutNotice) {
		getLnkTryoutNotices().add(lnkTryoutNotice);
		lnkTryoutNotice.setLutAuditDir(this);

		return lnkTryoutNotice;
	}

	public LnkTryoutNotice removeLnkTryoutNotice(LnkTryoutNotice lnkTryoutNotice) {
		getLnkTryoutNotices().remove(lnkTryoutNotice);
		lnkTryoutNotice.setLutAuditDir(null);

		return lnkTryoutNotice;
	}

	public List<LnkTryoutProcedure> getLnkTryoutProcedures() {
		return this.lnkTryoutProcedures;
	}

	public void setLnkTryoutProcedures(List<LnkTryoutProcedure> lnkTryoutProcedures) {
		this.lnkTryoutProcedures = lnkTryoutProcedures;
	}

	public LnkTryoutProcedure addLnkTryoutProcedure(LnkTryoutProcedure lnkTryoutProcedure) {
		getLnkTryoutProcedures().add(lnkTryoutProcedure);
		lnkTryoutProcedure.setLutAuditDir(this);

		return lnkTryoutProcedure;
	}

	public LnkTryoutProcedure removeLnkTryoutProcedure(LnkTryoutProcedure lnkTryoutProcedure) {
		getLnkTryoutProcedures().remove(lnkTryoutProcedure);
		lnkTryoutProcedure.setLutAuditDir(null);

		return lnkTryoutProcedure;
	}

	public List<LnkTryoutRisk> getLnkTryoutRisks() {
		return this.lnkTryoutRisks;
	}

	public void setLnkTryoutRisks(List<LnkTryoutRisk> lnkTryoutRisks) {
		this.lnkTryoutRisks = lnkTryoutRisks;
	}

	public LnkTryoutRisk addLnkTryoutRisk(LnkTryoutRisk lnkTryoutRisk) {
		getLnkTryoutRisks().add(lnkTryoutRisk);
		lnkTryoutRisk.setLutAuditDir(this);

		return lnkTryoutRisk;
	}

	public LnkTryoutRisk removeLnkTryoutRisk(LnkTryoutRisk lnkTryoutRisk) {
		getLnkTryoutRisks().remove(lnkTryoutRisk);
		lnkTryoutRisk.setLutAuditDir(null);

		return lnkTryoutRisk;
	}
	
	public List<LutFactor> getLutFactors() {
		return this.lutFactors;
	}

	public void setLutFactors(List<LutFactor> lutFactors) {
		this.lutFactors = lutFactors;
	}

	public LutFactor addLutFactor(LutFactor lutFactor) {
		getLutFactors().add(lutFactor);
		lutFactor.setLutAuditDir(this);

		return lutFactor;
	}

	public LutFactor removeLutFactor(LutFactor lutFactor) {
		getLutFactors().remove(lutFactor);
		lutFactor.setLutAuditDir(null);

		return lutFactor;
	}

	public List<LnkMainFormT2> getLnkMainFormT2s() {
		return this.lnkMainFormT2s;
	}

	public void setLnkMainFormT2s(List<LnkMainFormT2> lnkMainFormT2s) {
		this.lnkMainFormT2s = lnkMainFormT2s;
	}


}