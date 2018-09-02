package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.netgloo.models.LnkMainAttach;
import com.netgloo.models.MainAuditRegistration;

import com.netgloo.models.LutAuditWork;

import java.math.BigDecimal;


/**
 * The persistent class for the FIN_FORM_B1_2 database table.
 * 
 */
@Entity
@Table(name="FIN_FORM_B1_2")
@NamedQuery(name="FinFormB12.findAll", query="SELECT f FROM FinFormB12 f")
public class FinFormB12 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="FIN_FORM_B1_2_SEQ", sequenceName="FIN_FORM_B1_2_SEQ",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="FIN_FORM_B1_2_SEQ")
	@Column(name = "id", unique = true, nullable = false)
	private long id;

	private String data1;

	private String data2;

	private long orgcatid;

	private String orgcode;

	private long stepid;

	private long userid;
	
	private long filelnkid;
	
	private long planid;
	
	private String cyear;
	
	private long workid;

	//bi-directional many-to-one association to LnkMainAttach
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name="filelnkid",nullable=false, insertable=false,updatable=false)
	private LnkMainAttach lnkMainAttach;

	//bi-directional many-to-one association to MainAuditRegistration
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name="planid",nullable=false, insertable=false,updatable=false)
	private MainAuditRegistration mainAuditRegistration;

	public FinFormB12() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getData1() {
		return this.data1;
	}

	public void setData1(String data1) {
		this.data1 = data1;
	}

	public String getData2() {
		return this.data2;
	}

	public void setData2(String data2) {
		this.data2 = data2;
	}

	public long getOrgcatid() {
		return this.orgcatid;
	}

	public void setOrgcatid(long orgcatid) {
		this.orgcatid = orgcatid;
	}

	public String getOrgcode() {
		return this.orgcode;
	}

	public void setOrgcode(String orgcode) {
		this.orgcode = orgcode;
	}

	public long getStepid() {
		return this.stepid;
	}

	public void setStepid(long stepid) {
		this.stepid = stepid;
	}

	public long getUserid() {
		return this.userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public LnkMainAttach getLnkMainAttach() {
		return this.lnkMainAttach;
	}

	public void setLnkMainAttach(LnkMainAttach lnkMainAttach) {
		this.lnkMainAttach = lnkMainAttach;
	}

	public MainAuditRegistration getMainAuditRegistration() {
		return this.mainAuditRegistration;
	}

	public void setMainAuditRegistration(MainAuditRegistration mainAuditRegistration) {
		this.mainAuditRegistration = mainAuditRegistration;
	}

	public long getFilelnkid() {
		return filelnkid;
	}

	public void setFilelnkid(long filelnkid) {
		this.filelnkid = filelnkid;
	}

	public long getPlanid() {
		return planid;
	}

	public void setPlanid(long planid) {
		this.planid = planid;
	}

	public String getCyear() {
		return cyear;
	}

	public void setCyear(String cyear) {
		this.cyear = cyear;
	}
	
	public long getWorkid() {
		return workid;
	}

	public void setWorkid(long workid) {
		this.workid = workid;
	}
	

}