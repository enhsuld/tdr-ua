package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.math.BigDecimal;


/**
 * The persistent class for the FS_FORM_A4 database table.
 * 
 */
@Entity
@Table(name="FS_FORM_A4")
@NamedQuery(name="FsFormA4.findAll", query="SELECT f FROM FsFormA4 f")
public class FsFormA4 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="FS_FORM_A4_SEQ", sequenceName="FS_FORM_A4_SEQ",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="FS_FORM_A4_SEQ")
	@Column(name = "id", unique = true, nullable = false)
	private long id;

	private String cyear;

	private String data1;

	private String data2;

	private String data3;

	private String data4;

	private String data5;

	private String data6;

	private long orgcatid;

	private String orgcode;

	private long stepid;
	
	private long planid;
	
	private String description1;
	
	private String description2;
	
	private String description3;
	
	@Column(name="\"PERCENT\"")
	private String percent;
	
	private long isselect;

	//bi-directional many-to-one association to MainAuditRegistration
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="planid",nullable = false,insertable=false,updatable=false)
	@JsonBackReference
	private MainAuditRegistration mainAuditRegistration;

	public FsFormA4() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCyear() {
		return this.cyear;
	}

	public void setCyear(String cyear) {
		this.cyear = cyear;
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

	public String getData3() {
		return this.data3;
	}

	public void setData3(String data3) {
		this.data3 = data3;
	}

	public String getData4() {
		return this.data4;
	}

	public void setData4(String data4) {
		this.data4 = data4;
	}

	public String getData5() {
		return this.data5;
	}

	public void setData5(String data5) {
		this.data5 = data5;
	}

	public String getData6() {
		return this.data6;
	}

	public void setData6(String data6) {
		this.data6 = data6;
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

	public MainAuditRegistration getMainAuditRegistration() {
		return this.mainAuditRegistration;
	}

	public void setMainAuditRegistration(MainAuditRegistration mainAuditRegistration) {
		this.mainAuditRegistration = mainAuditRegistration;
	}

	public long getPlanid() {
		return planid;
	}

	public void setPlanid(long planid) {
		this.planid = planid;
	}

	public String getPercent() {
		return percent;
	}

	public void setPercent(String percent) {
		this.percent = percent;
	}

	public long getIsselect() {
		return isselect;
	}

	public void setIsselect(long isselect) {
		this.isselect = isselect;
	}

	public String getDescription1() {
		return description1;
	}

	public void setDescription1(String description1) {
		this.description1 = description1;
	}

	public String getDescription2() {
		return description2;
	}

	public void setDescription2(String description2) {
		this.description2 = description2;
	}

	public String getDescription3() {
		return description3;
	}

	public void setDescription3(String description3) {
		this.description3 = description3;
	}
	

}